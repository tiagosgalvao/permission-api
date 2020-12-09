package com.galvao.permission.permission.controller;

import com.galvao.permission.permission.builder.PermissionBuilder;
import com.galvao.permission.permission.dto.PermissionDTO;
import com.galvao.permission.permission.enums.PermissionType;
import com.galvao.permission.permission.model.Permission;
import com.galvao.permission.permission.service.PermissionService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;
import java.util.UUID;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.galvao.permission.base.service.BaseService.PARENT_ID;
import static com.galvao.permission.base.service.BaseService.USER_ID;
import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.when;

@WebFluxTest(PermissionController.class)
class PermissionControllerTest {

	private static final String BASE_PATH = "/v1/permissions";

	@Autowired
	private WebTestClient webTestClient;

	@MockBean
	private PermissionService permissionService;

	@Test
	void findDepartments() {
		PermissionDTO permission = new PermissionDTO(UUID.randomUUID(), USER_ID, PARENT_ID, PermissionType.EDIT);
		List<PermissionDTO> permissions = List.of(permission);
		Flux<PermissionDTO> savedPermission = Flux.just(permission);
		when(permissionService.findAllPermissions()).thenReturn(savedPermission);


		EntityExchangeResult<List<PermissionDTO>> entityExchangeResult = webTestClient.get().uri(BASE_PATH)
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.exchange()
				.expectStatus().isOk()
				.expectBodyList(PermissionDTO.class)
				.returnResult();

		assertEquals(permissions, entityExchangeResult.getResponseBody());
	}

	@Test
	void findByUserId() {
		PermissionDTO permission = new PermissionDTO(UUID.randomUUID(), USER_ID, PARENT_ID, PermissionType.EDIT);
		List<PermissionDTO> permissions = List.of(permission);
		Flux<PermissionDTO> savedPermission = Flux.just(permission);
		when(permissionService.findByUserId(USER_ID)).thenReturn(savedPermission);

		EntityExchangeResult<List<PermissionDTO>> entityExchangeResult = webTestClient.get().uri(BASE_PATH + "/users/{userId}", USER_ID)
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.exchange()
				.expectStatus().isOk()
				.expectBodyList(PermissionDTO.class)
				.returnResult();

		assertEquals(permissions, entityExchangeResult.getResponseBody());
	}

	@Test
	void findByUserIdAndDepartment() {
		PermissionDTO permission = new PermissionDTO(UUID.randomUUID(), USER_ID, PARENT_ID, PermissionType.EDIT);
		Mono<PermissionDTO> savedPermission = Mono.just(permission);
		when(permissionService.findByUserIdAndDepartment(USER_ID, PARENT_ID)).thenReturn(savedPermission);

		EntityExchangeResult<PermissionDTO> entityExchangeResult = webTestClient.get()
				.uri(BASE_PATH + "/users/{userId}/departaments/{departmentId}", USER_ID, PARENT_ID)
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody(PermissionDTO.class)
				.returnResult();

		assertEquals(permission, entityExchangeResult.getResponseBody());
	}

	@Test
	void setPermission() {
		Permission permission = Permission.builder().userId(USER_ID).departmentId(PARENT_ID).permissionType(PermissionType.EDIT).build();
		PermissionDTO expectedPermission = new PermissionDTO(permission);
		Mono<PermissionDTO> savedPermission = Mono.just(expectedPermission);
		when(permissionService.setPermission(permission)).thenReturn(savedPermission);

		webTestClient.post()
				.uri(BASE_PATH + "/users/{userId}/departaments/{departmentId}?permissionType={permissionType}",
						USER_ID, PARENT_ID, PermissionType.EDIT)
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.exchange()
				.expectStatus()
				.isCreated()
				.expectBody(PermissionDTO.class).isEqualTo(expectedPermission);
	}

	@Test
	void deletePermissionByIdWhenItExists() {
		Permission permissionToDelete = PermissionBuilder.createSavedPermission();
		when(permissionService.deleteById(permissionToDelete.getId())).thenReturn(Mono.just(permissionToDelete));

		webTestClient.delete().uri(BASE_PATH + "/{permissionId}", permissionToDelete.getId())
				.exchange()
				.expectStatus().isOk();
	}

	@Test
	void deletePermissionByIdWhenItDoesntExists() {
		Permission permissionToDelete = PermissionBuilder.createSavedPermission();
		when(permissionService.deleteById(permissionToDelete.getId())).thenReturn(Mono.empty());

		webTestClient.delete().uri(BASE_PATH + "/{permissionId}", permissionToDelete.getId())
				.exchange()
				.expectStatus().isNotFound();
	}

	@Test
	void deleteByUserIdAndDepartamentIdWhenItExists() {
		Permission permissionToDelete = PermissionBuilder.createSavedPermission();
		when(permissionService.deleteByUserIdAndDepartamentId(permissionToDelete.getUserId(),
				permissionToDelete.getDepartmentId())).thenReturn(Mono.just(permissionToDelete));

		webTestClient.delete().uri(BASE_PATH + "/users/{userId}/departaments/{departmentId}", USER_ID, PARENT_ID)
				.exchange()
				.expectStatus().isOk();
	}

	@Test
	void deleteByUserIdAndDepartamentIdWhenItDoesntExists() {
		Permission permissionToDelete = PermissionBuilder.createSavedPermission();
		when(permissionService.deleteByUserIdAndDepartamentId(permissionToDelete.getUserId(),
				permissionToDelete.getDepartmentId())).thenReturn(Mono.empty());

		webTestClient.delete().uri(BASE_PATH + "/users/{userId}/departaments/{departmentId}", USER_ID, PARENT_ID)
				.exchange()
				.expectStatus().isNotFound();
	}

}
