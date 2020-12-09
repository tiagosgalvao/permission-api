package com.galvao.permission.permission.service;

import com.galvao.permission.base.service.BaseService;
import com.galvao.permission.department.builder.DepartmentBuilder;
import com.galvao.permission.department.dto.DepartmentDTO;
import com.galvao.permission.department.model.Department;
import com.galvao.permission.department.repository.DepartmentRepository;
import com.galvao.permission.department.service.DepartmentService;
import com.galvao.permission.permission.builder.PermissionBuilder;
import com.galvao.permission.permission.dto.PermissionDTO;
import com.galvao.permission.permission.model.Permission;
import com.galvao.permission.permission.repository.PermissionRepository;
import com.galvao.permission.user.builder.UserBuilder;
import com.galvao.permission.user.model.User;
import com.galvao.permission.user.repository.UserRepository;

import org.junit.jupiter.api.Test;

import java.util.List;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class PermissionServiceTest extends BaseService {

	@Tested
	private PermissionService permissionService;

	@Injectable
	private UserRepository userRepository;

	@Injectable
	private PermissionRepository permissionRepository;

	@Injectable
	private DepartmentService departmentService;

	@Injectable
	private DepartmentRepository departmentRepository;

	@Test
	void findAll() {
		Permission permission = PermissionBuilder.createSavedPermission();
		new Expectations() {{
			permissionRepository.findAll();
			result = Flux.just(permission);
		}};

		StepVerifier.create(permissionService.findAllPermissions())
				.expectNext(new PermissionDTO(permission))
				.verifyComplete();
	}

	@Test
	void findByUser() {
		Permission permission = PermissionBuilder.createSavedPermission();
		new Expectations() {{
			permissionRepository.findByUserId(permission.getUserId());
			result = Flux.just(permission);
		}};

		StepVerifier.create(permissionService.findByUserId(permission.getUserId()))
				.expectNext(new PermissionDTO(permission))
				.verifyComplete();
	}

	@Test
	void findByUserIdAndDepartmentId() {
		Permission permission = PermissionBuilder.createSavedPermission();
		Department departament = DepartmentBuilder.createSavedParentDepartment();
		User user = UserBuilder.createSavedModel();
		new Expectations() {{
			departmentService.readDepartmentWithParents(departament.getId());
			result = Mono.just(new DepartmentDTO(departament.getId(), departament.getName(), null, List.of()));

			permissionRepository.findByUserId(user.getId());
			result = Flux.just(permission);
		}};

		StepVerifier.create(permissionService.findByUserIdAndDepartment(permission.getUserId(), permission.getDepartmentId()))
				.expectNext(new PermissionDTO(permission))
				.verifyComplete();
	}

	@Test
	void findByUserIdAndDepartmentIdUsingHierarchy() {
		Department parent = DepartmentBuilder.createSavedParentDepartment();
		Department child = DepartmentBuilder.createSavedChildWithParent();
		Permission permission = PermissionBuilder.createSavedPermissionDefinedDepartment(parent.getId());
		User user = UserBuilder.createSavedModel();

		new Expectations() {{
			permissionRepository.findByUserId(user.getId());
			result = Flux.just(permission);

			DepartmentDTO parentDTO = new DepartmentDTO(parent.getId(), parent.getName(), null, List.of());
			DepartmentDTO childDTO = new DepartmentDTO(child.getId(), child.getName(), parentDTO, null);
			departmentService.readDepartmentWithParents(child.getId());
			result = Mono.just(childDTO);
		}};

		StepVerifier.create(permissionService.findByUserIdAndDepartment(user.getId(), child.getId()))
				.expectNext(new PermissionDTO(permission))
				.verifyComplete();
	}

	@Test
	void shouldNotFindByUserIdAndDepartmentId() {
		Permission permission = PermissionBuilder.createSavedPermission();
		Department departament = DepartmentBuilder.createSavedParentDepartment();
		User user = UserBuilder.createSavedModel();

		new Expectations() {{
			departmentService.readDepartmentWithParents(departament.getId());
			result = Mono.just(new DepartmentDTO(departament.getId(), departament.getName(), null, List.of()));

			permissionRepository.findByUserId(user.getId());
			result = Flux.empty();
		}};

		StepVerifier.create(permissionService.findByUserIdAndDepartment(permission.getUserId(), permission.getDepartmentId()))
				.verifyComplete();
	}

	@Test
	void setNewPermission() {
		User user = UserBuilder.createSavedModel();
		Department departament = DepartmentBuilder.createSavedParentDepartment();
		Permission permissionToSave = PermissionBuilder.createPermissionToBeSaved();
		Permission savedPermission = PermissionBuilder.createSavedPermission();
		new Expectations() {{
			userRepository.findById(permissionToSave.getUserId());
			result = Mono.just(user);

			departmentService.readDepartmentWithParents(departament.getId());
			result = Mono.just(new DepartmentDTO(departament.getId(), departament.getName(), null, null));

			permissionRepository.findByUserIdAndDepartmentId(savedPermission.getUserId(), savedPermission.getDepartmentId());
			result = Mono.empty();

			permissionRepository.insert(withAny(permissionToSave));
			result = Mono.just(savedPermission);
		}};
		StepVerifier.create(permissionService.setPermission(permissionToSave))
				.expectNext(new PermissionDTO(savedPermission))
				.verifyComplete();
	}

	@Test
	void updatePermission() {
		User user = UserBuilder.createSavedModel();
		Department departament = DepartmentBuilder.createSavedParentDepartment();
		Permission permissionToSave = PermissionBuilder.createPermissionViewToBeSaved();
		Permission savedPermission = PermissionBuilder.createSavedPermission();
		Permission updatedPermission = Permission.builder().id(savedPermission.getId()).userId(savedPermission.getUserId())
				.departmentId(savedPermission.getUserId()).permissionType(permissionToSave.getPermissionType()).build();
		new Expectations() {{
			userRepository.findById(permissionToSave.getUserId());
			result = Mono.just(user);

			departmentService.readDepartmentWithParents(permissionToSave.getDepartmentId());
			result = Mono.just(new DepartmentDTO(departament.getId(), departament.getName(), null, null));

			permissionRepository.findByUserIdAndDepartmentId(savedPermission.getUserId(), savedPermission.getDepartmentId());
			result = Mono.just(savedPermission);


			permissionRepository.save(withAny(updatedPermission));
			result = Mono.just(updatedPermission);
		}};
		StepVerifier.create(permissionService.setPermission(permissionToSave))
				.expectNext(new PermissionDTO(updatedPermission))
				.verifyComplete();
	}

	@Test
	void deleteById() {
		Permission permission = PermissionBuilder.createSavedPermission();
		new Expectations() {{
			permissionRepository.findById(permission.getId());
			result = Mono.just(permission);

			permissionRepository.delete(withAny(permission));
			result = Mono.just(permission);
		}};
		StepVerifier.create(permissionService.deleteById(permission.getId()))
				.expectNext(permission)
				.verifyComplete();
	}

	@Test
	void deleteByIdWhenPermissionNotFound() {
		Permission permission = PermissionBuilder.createSavedPermission();
		new Expectations() {{
			permissionRepository.findById(permission.getId());
			result = Mono.just(permission);

			permissionRepository.delete(withAny(permission));
			result = Mono.empty();
		}};
		StepVerifier.create(permissionService.deleteById(permission.getId()))
				.expectNext(permission)
				.verifyComplete();
	}

	@Test
	void deleteByUserIdAndDepartamentId() {
		Department departament = DepartmentBuilder.createDepartmentToBeSaved();
		User user = UserBuilder.createSavedModel();
		Permission permission = PermissionBuilder.createSavedPermission();
		new Expectations() {{
			permissionRepository.findByUserIdAndDepartmentId(withAny(user.getId()), withAny(departament.getId()));
			result = Mono.just(permission);

			permissionRepository.delete(withAny(permission));
			result = Mono.just(permission);
		}};
		StepVerifier.create(permissionService.deleteByUserIdAndDepartamentId(permission.getUserId(), permission.getDepartmentId()))
				.expectNext(permission)
				.verifyComplete();
	}

	@Test
	void deleteByUserIdAndDepartamentIdWhenPermissionNotFound() {
		Department departament = DepartmentBuilder.createDepartmentToBeSaved();
		User user = UserBuilder.createSavedModel();
		Permission permission = PermissionBuilder.createSavedPermission();
		new Expectations() {{
			permissionRepository.findByUserIdAndDepartmentId(withAny(user.getId()), withAny(departament.getId()));
			result = Mono.empty();
		}};

		StepVerifier.create(permissionService.deleteByUserIdAndDepartamentId(permission.getUserId(), permission.getDepartmentId()))
				.verifyComplete();
	}

}