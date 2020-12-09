package com.galvao.permission.department.service;

import com.galvao.permission.department.dto.DepartmentDTO;
import com.galvao.permission.department.model.Department;
import com.galvao.permission.department.repository.DepartmentRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DepartmentService {

	private final DepartmentRepository departmentRepository;

	public Flux<DepartmentDTO> findAll() {
		return departmentRepository.findAll()
				.map(department -> DepartmentDTO.builder().id(department.getId()).name(department.getName()).parent(null).children(List.of()).build());
	}

	public Mono<DepartmentDTO> readDepartmentWithParents(UUID departmentId) {
		return departmentRepository.findById(departmentId).flatMap(department -> addParent(department.getParentId(), department));
	}

	public Mono<DepartmentDTO> readDepartmentWithChildren(UUID departmentId) {
		return departmentRepository.findById(departmentId).flatMap(this::addChildren);
	}

	private Mono<DepartmentDTO> addParent(UUID parentId, Department department) {
		return Mono.justOrEmpty(parentId)
				.flatMap(departmentRepository::findById)
				.flatMap(parentDepartment -> addParent(parentDepartment.getParentId(), parentDepartment))
				.map(parentDepartmentDTO -> DepartmentDTO.builder()
						.id(department.getId())
						.name(department.getName())
						.parent(parentDepartmentDTO)
						.children(List.of())
						.build())
				.switchIfEmpty(Mono.just(DepartmentDTO.builder().id(department.getId()).name(department.getName()).parent(null).children(List.of()).build()));
	}

	private Mono<DepartmentDTO> addChildren(Department department) {
		return departmentRepository.findByParentId(department.getId())
				.flatMap(this::addChildren)
				.collectList()
				.map(childDepartments -> DepartmentDTO.builder()
						.id(department.getId())
						.name(department.getName())
						.parent(null)
						.children(childDepartments)
						.build());
	}

}
