package com.galvao.permission.user.controller;

import com.galvao.permission.user.dto.UserDTO;
import com.galvao.permission.user.service.UserService;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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
@Tag(name = "Users")
@RequestMapping("v1/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@Operation(description = "Get users")
	@GetMapping
	public Flux<UserDTO> getUsers() {
		log.info("getUsers");
		return userService.findAll();
	}

	@Operation(description = "Get user by id")
	@GetMapping("/{id}")
	public Mono<UserDTO> getUsersById(@PathVariable UUID id) {
		log.info("getUsersById id: {}", id);
		return userService.findById(id);
	}

}
