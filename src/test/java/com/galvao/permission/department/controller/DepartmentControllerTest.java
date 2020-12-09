package com.galvao.permission.department.controller;

import com.galvao.permission.department.dto.DepartmentDTO;
import com.galvao.permission.department.enums.HierarchyLevel;
import com.galvao.permission.department.service.DepartmentService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.galvao.permission.base.service.BaseService.CHILD_ID;
import static com.galvao.permission.base.service.BaseService.PARENT_ID;
import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.when;

@ActiveProfiles("LOCAL")
@WebFluxTest(DepartmentController.class)
class DepartmentControllerTest {

	@Autowired
	private WebTestClient webTestClient;

	@MockBean
	private DepartmentService departmentService;

	@Test
	void findDepartments() {
		List<DepartmentDTO> departments = List.of(new DepartmentDTO(PARENT_ID, PARENT_ID.toString(), null, List.of()));
		Flux<DepartmentDTO> savedDepartment = Flux.just(new DepartmentDTO(PARENT_ID, PARENT_ID.toString(), null, List.of()));
		when(departmentService.findAll()).thenReturn(savedDepartment);

		EntityExchangeResult<List<DepartmentDTO>> entityExchangeResult = webTestClient.get().uri("/v1/departments")
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.exchange()
				.expectStatus().isOk()
				.expectBodyList(DepartmentDTO.class)
				.returnResult();

		assertEquals(departments, entityExchangeResult.getResponseBody());
	}

	@Test
	void findDepartmentByIdWithParent() {
		var expectedParentDepartment = new DepartmentDTO(PARENT_ID, PARENT_ID.toString(), null, List.of());
		var expectedChildDepartment = new DepartmentDTO(CHILD_ID, CHILD_ID.toString(), expectedParentDepartment, List.of());
		Mono<DepartmentDTO> savedDepartment = Mono.just(expectedChildDepartment);
		when(departmentService.readDepartmentWithParents(expectedChildDepartment.getId())).thenReturn(savedDepartment);

		EntityExchangeResult<DepartmentDTO> entityExchangeResult = webTestClient.get()
				.uri("/v1/departments/{id}?hierarchyLevel={hierarchyLevel}", expectedChildDepartment.getId(), HierarchyLevel.PARENT.toString())
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody(DepartmentDTO.class)
				.returnResult();

		assertEquals(expectedChildDepartment, entityExchangeResult.getResponseBody());
	}

	@Test
	void findDepartmentByIdWithChildren() {
		var expectedChildDepartment = new DepartmentDTO(CHILD_ID, CHILD_ID.toString(), null, List.of());
		var expectedParentDepartment = new DepartmentDTO(PARENT_ID, PARENT_ID.toString(), null, List.of(expectedChildDepartment));
		Mono<DepartmentDTO> savedDepartment = Mono.just(expectedParentDepartment);
		when(departmentService.readDepartmentWithChildren(expectedParentDepartment.getId())).thenReturn(savedDepartment);

		EntityExchangeResult<DepartmentDTO> entityExchangeResult = webTestClient.get()
				.uri("/v1/departments/{id}?hierarchyLevel={hierarchyLevel}", expectedParentDepartment.getId(), HierarchyLevel.CHILD.toString())
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody(DepartmentDTO.class)
				.returnResult();

		assertEquals(expectedParentDepartment, entityExchangeResult.getResponseBody());
	}

}
