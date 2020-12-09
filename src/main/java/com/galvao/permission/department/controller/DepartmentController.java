package com.galvao.permission.department.controller;

import com.galvao.permission.department.dto.DepartmentDTO;
import com.galvao.permission.department.enums.HierarchyLevel;
import com.galvao.permission.department.service.DepartmentService;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Profile("LOCAL")
@Slf4j
@RestController
@Tag(name = "Departments")
@RequestMapping("v1/departments")
@RequiredArgsConstructor
public class DepartmentController {

	private final DepartmentService departmentService;

	@Operation(description = "Get all departments")
	@GetMapping
	public Flux<DepartmentDTO> getAll() {
		log.info("getAll departments");
		return departmentService.findAll();
	}

	@Operation(description = "Get department by uuid")
	@GetMapping("/{id}")
	public Mono<DepartmentDTO> getDepartamentWithParentsById(@PathVariable UUID id,
	                                                         @RequestParam HierarchyLevel hierarchyLevel) {
		log.info("getDepartamentWithParentsById id: {} hierarchyLevel: {}.", id, hierarchyLevel);
		if (HierarchyLevel.PARENT == hierarchyLevel) {
			return departmentService.readDepartmentWithParents(id);
		} else {
			return departmentService.readDepartmentWithChildren(id);
		}
	}

}
