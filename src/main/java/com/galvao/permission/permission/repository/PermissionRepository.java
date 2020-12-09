package com.galvao.permission.permission.repository;

import com.galvao.permission.permission.model.Permission;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface PermissionRepository extends ReactiveMongoRepository<Permission, UUID> {
	Mono<Permission> findByUserIdAndDepartmentId(UUID userId, UUID departmentId);

	Flux<Permission> findByUserId(UUID userId);
}
