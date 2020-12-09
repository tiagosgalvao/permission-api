package com.galvao.permission.user.repository;

import com.galvao.permission.user.model.User;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveMongoRepository<User, UUID> {
	Mono<User> findById(UUID userId);
}
