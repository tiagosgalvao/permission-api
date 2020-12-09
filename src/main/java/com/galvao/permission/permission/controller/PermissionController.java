package com.galvao.permission.permission.controller;

import com.galvao.permission.permission.dto.PermissionDTO;
import com.galvao.permission.permission.enums.PermissionType;
import com.galvao.permission.permission.model.Permission;
import com.galvao.permission.permission.service.PermissionService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.CREATED;

@Slf4j
@RestController
@Tag(name = "Permissions")
@RequestMapping("v1/permissions")
@RequiredArgsConstructor
public class PermissionController {

	private final PermissionService permissionService;

	@Operation(description = "Get all permissions")
	@GetMapping
	public Flux<PermissionDTO> getAllPermissions() {
		log.info("getAllPermissions");
		return permissionService.findAllPermissions();
	}

	@Operation(description = "Get permission by user")
	@GetMapping("/users/{userId}")
	public Flux<PermissionDTO> getPermissionByUser(@PathVariable UUID userId) {
		log.info("getPermissionByUser userId: {}", userId);
		return permissionService.findByUserId(userId);
	}

	@Operation(description = "Get permission by user and department")
	@GetMapping("/users/{userId}/departaments/{departmentId}")
	public Mono<PermissionDTO> getPermissionByUserAndDepartment(@PathVariable UUID userId, @PathVariable UUID departmentId) {
		log.info("getPermissionByUserAndDepartment userId: {} departmentId: {}.", userId, departmentId);
		return permissionService.findByUserIdAndDepartment(userId, departmentId);
	}

	@Operation(description = "Assign permission")
	@PostMapping("/users/{userId}/departaments/{departmentId}")
	@ResponseStatus(CREATED)
	public Mono<PermissionDTO> setPermission(@PathVariable UUID userId, @PathVariable UUID departmentId,
	                                         @RequestParam PermissionType permissionType) {
		log.info("setPermission userId: {} departmentId: {} permissionType: {}", userId, departmentId, permissionType);
		return permissionService.setPermission(Permission.builder().userId(userId).departmentId(departmentId).permissionType(permissionType).build());
	}

	@Operation(description = "Revoke permissions by id")
	@DeleteMapping("/{id}")
	public Mono<ResponseEntity<Void>> deletePermissionById(@PathVariable UUID id) {
		log.info("deletePermission id: {} ", id);
		return permissionService.deleteById(id)
				.map(r -> ResponseEntity.ok().<Void>build())
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@Operation(description = "Revoke permissions")
	@DeleteMapping("/users/{userId}/departaments/{departmentId}")
	public Mono<ResponseEntity<Void>> deletePermissionById(@PathVariable UUID userId, @PathVariable UUID departmentId) {
		log.info("deletePermission userId: {} departmentId: {} ", userId, departmentId);
		return permissionService.deleteByUserIdAndDepartamentId(userId, departmentId)
				.map(r -> ResponseEntity.ok().<Void>build())
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

}

