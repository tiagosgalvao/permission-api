package com.galvao.permission.department.service;

import com.galvao.permission.department.builder.DepartmentBuilder;
import com.galvao.permission.department.dto.DepartmentDTO;
import com.galvao.permission.department.model.Department;
import com.galvao.permission.department.repository.DepartmentRepository;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class DepartmentServiceTest {

	private static final UUID PARENT_ID = UUID.randomUUID();
	private static final UUID CHILD_ID = UUID.randomUUID();
	private static final UUID CHILD_ID_2 = UUID.randomUUID();

	@Tested
	private DepartmentService departmentService;

	@Injectable
	private DepartmentRepository departmentRepository;

	@Test
	void findAll() {
		Department departament = DepartmentBuilder.createDepartmentToBeSaved();
		new Expectations() {{
			departmentRepository.findAll();
			result = Flux.just(departament);
		}};

		StepVerifier.create(departmentService.findAll())
				.expectNext(new DepartmentDTO(departament.getId(), departament.getName(), null, List.of()))
				.verifyComplete();
	}

	@Test
	void findAllWithNoDepartments() {
		new Expectations() {{
			departmentRepository.findAll();
			result = Flux.just();
		}};

		StepVerifier.create(departmentService.findAll())
				.verifyComplete();
	}

	@Test
	void readDepartmentWithParents() {
		var expectedParentDepartment = new DepartmentDTO(PARENT_ID, PARENT_ID.toString(), null, List.of());
		var expectedChildDepartment = new DepartmentDTO(CHILD_ID, CHILD_ID.toString(), expectedParentDepartment, List.of());
		new Expectations() {{
			departmentRepository.findById(CHILD_ID);
			result = Mono.just(new Department(CHILD_ID, CHILD_ID.toString(), PARENT_ID));

			departmentRepository.findById(PARENT_ID);
			result = Mono.just(new Department(PARENT_ID, PARENT_ID.toString(), null));
		}};

		StepVerifier.create(departmentService.readDepartmentWithParents(CHILD_ID))
				.expectNext(expectedChildDepartment)
				.verifyComplete();
	}

	@Test
	void readDepartmentWithChildren() {
		var expectedChildDepartment1 = new DepartmentDTO(CHILD_ID, CHILD_ID.toString(), null, List.of());
		var expectedChildDepartment2 = new DepartmentDTO(CHILD_ID_2, CHILD_ID_2.toString(), null, List.of());
		var expectedParentDepartment = new DepartmentDTO(PARENT_ID, PARENT_ID.toString(), null, List.of(expectedChildDepartment1, expectedChildDepartment2));
		new Expectations() {{
			departmentRepository.findById(PARENT_ID);
			result = Mono.just(new Department(PARENT_ID, PARENT_ID.toString(), null));

			departmentRepository.findByParentId(PARENT_ID);
			result = Flux.just(new Department(CHILD_ID, CHILD_ID.toString(), PARENT_ID), new Department(CHILD_ID_2, CHILD_ID_2.toString(), PARENT_ID));

			departmentRepository.findByParentId(CHILD_ID);
			result = Flux.empty();

			departmentRepository.findByParentId(CHILD_ID_2);
			result = Flux.empty();
		}};

		StepVerifier.create(departmentService.readDepartmentWithChildren(PARENT_ID))
				.expectNext(expectedParentDepartment)
				.verifyComplete();
	}

}