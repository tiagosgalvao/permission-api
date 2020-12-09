package com.galvao.permission.user.service;

import com.galvao.permission.exception.exeptions.UserNotFound;
import com.galvao.permission.user.dto.UserDTO;
import com.galvao.permission.user.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.util.UUID;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	public Mono<UserDTO> findById(UUID userId) {
		return userRepository.findById(userId)
				.map(UserDTO::new)
				.switchIfEmpty(Mono.error(new UserNotFound(userId.toString())));
	}

	public Flux<UserDTO> findAll() {
		return userRepository.findAll().map(UserDTO::new);
	}

}
