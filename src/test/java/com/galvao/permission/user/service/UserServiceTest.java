package com.galvao.permission.user.service;

import com.galvao.permission.base.service.BaseService;
import com.galvao.permission.user.builder.UserBuilder;
import com.galvao.permission.user.dto.UserDTO;
import com.galvao.permission.user.model.User;
import com.galvao.permission.user.repository.UserRepository;

import org.junit.jupiter.api.Test;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class UserServiceTest extends BaseService {

	@Tested
	private UserService userService;

	@Injectable
	private UserRepository userRepository;

	@Test
	void findAll() {
		User user = UserBuilder.createSavedModel();
		new Expectations() {{
			userRepository.findAll();
			result = Flux.just(user);
		}};

		StepVerifier.create(userService.findAll())
				.expectNext(new UserDTO(user))
				.verifyComplete();
	}

	@Test
	void findById() {
		User user = UserBuilder.createSavedModel();
		new Expectations() {{
			userRepository.findById(user.getId());
			result = Mono.just(user);
		}};

		StepVerifier.create(userService.findById(user.getId()))
				.expectNext(new UserDTO(user))
				.verifyComplete();
	}

}