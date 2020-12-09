package com.galvao.permission.permission.service;

import com.galvao.permission.department.dto.DepartmentDTO;
import com.galvao.permission.department.service.DepartmentService;
import com.galvao.permission.exception.exeptions.DepartmentNotFound;
import com.galvao.permission.exception.exeptions.UserNotFound;
import com.galvao.permission.permission.dto.PermissionDTO;
import com.galvao.permission.permission.model.Permission;
import com.galvao.permission.permission.repository.PermissionRepository;
import com.galvao.permission.user.model.User;
import com.galvao.permission.user.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PermissionService {

	private final DepartmentService departmentService;
	private final UserRepository userRepository;
	private final PermissionRepository permissionRepository;

	public Flux<PermissionDTO> findAllPermissions() {
		return permissionRepository.findAll().map(PermissionDTO::new);
	}

	public Mono<PermissionDTO> setPermission(Permission permission) {
		return validateRequest(permission)
				.flatMap(departmentDTO -> permissionRepository.findByUserIdAndDepartmentId(permission.getUserId(), permission.getDepartmentId()))
				.flatMap(perm -> permissionRepository.save(Permission.builder()
						.id(perm.getId())
						.userId(permission.getUserId())
						.departmentId(permission.getDepartmentId())
						.permissionType(permission.getPermissionType())
						.build()))
				.switchIfEmpty(permissionRepository.insert(Permission.builder()
						.id(UUID.randomUUID())
						.userId(permission.getUserId())
						.departmentId(permission.getDepartmentId())
						.permissionType(permission.getPermissionType())
						.build()))
				.map(PermissionDTO::new);
	}

	private Mono<DepartmentDTO> validateRequest(Permission permission) {
		return Mono.just(permission)
				.flatMap(this::findById)
				.switchIfEmpty(Mono.error(new UserNotFound(permission.getUserId().toString())))
				.flatMap(user -> departmentService.readDepartmentWithParents(permission.getDepartmentId()))
				.switchIfEmpty(Mono.error(new DepartmentNotFound(permission.getDepartmentId().toString())));
	}

	private Mono<User> findById(Permission permission) {
		return userRepository.findById(permission.getUserId());
	}

	public Flux<PermissionDTO> findByUserId(UUID userId) {
		return permissionRepository.findByUserId(userId).map(PermissionDTO::new);
	}

	public Mono<PermissionDTO> findByUserIdAndDepartment(UUID userId, UUID departmentId) {
		return Mono.just(departmentId)
				.flatMap(departmentService::readDepartmentWithParents)
				.expand(response -> {
					if (Objects.isNull(response.getParent())) {
						return Mono.empty();
					}
					return Mono.just(response.getParent());
				})
				.collectList()
				.flatMap(departmentDTO -> findPermission(userId, departmentDTO))
				.map(PermissionDTO::new);
	}

	private Mono<Permission> findPermission(UUID userId, List<DepartmentDTO> departments) {
		return permissionRepository.findByUserId(userId).collectList().flatMap(permission -> findPermissionUsingHierarchy(departments, permission));
	}

	private Mono<Permission> findPermissionUsingHierarchy(List<DepartmentDTO> departments, List<Permission> permission) {
		for (DepartmentDTO department : departments) {
			Permission perm = permission.stream().filter(p -> p.getDepartmentId().equals(department.getId())).findFirst().orElse(null);
			if (Objects.nonNull(perm)) {
				return Mono.just(perm);
			}
		}
		return Mono.empty();
	}

	public Mono<Permission> deleteById(UUID permissionId) {
		return permissionRepository.findById(permissionId)
				.flatMap(permission -> permissionRepository.delete(permission).then(Mono.just(permission)));
	}

	public Mono<Permission> deleteByUserIdAndDepartamentId(UUID userId, UUID departmentId) {
		return permissionRepository.findByUserIdAndDepartmentId(userId, departmentId)
				.flatMap(permission -> permissionRepository.delete(permission).then(Mono.just(permission)));
	}

}
