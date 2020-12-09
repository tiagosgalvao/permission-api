package com.galvao.permission.department.repository;

import com.galvao.permission.department.model.Department;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

import reactor.core.publisher.Flux;

@Repository
public interface DepartmentRepository extends ReactiveMongoRepository<Department, UUID> {
	Flux<Department> findByParentId(UUID parentId);
}
