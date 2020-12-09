package com.galvao.permission.user.controller;

import com.galvao.permission.user.builder.UserBuilder;
import com.galvao.permission.user.dto.UserDTO;
import com.galvao.permission.user.service.UserService;

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

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.when;

@ActiveProfiles("LOCAL")
@WebFluxTest(UserController.class)
class UserControllerTest {

	private static final String BASE_PATH = "/v1/users";

	@Autowired
	private WebTestClient webTestClient;

	@MockBean
	private UserService userService;

	@Test
	void findAll() {
		List<UserDTO> users = List.of(new UserDTO(UserBuilder.createSavedModel()));
		Flux<UserDTO> savedUser = Flux.just(new UserDTO(UserBuilder.createSavedModel()));
		when(userService.findAll()).thenReturn(savedUser);

		EntityExchangeResult<List<UserDTO>> entityExchangeResult = webTestClient.get().uri(BASE_PATH)
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.exchange()
				.expectStatus().isOk()
				.expectBodyList(UserDTO.class)
				.returnResult();

		assertEquals(users, entityExchangeResult.getResponseBody());
	}

	@Test
	void findUserById() {
		UserDTO user = new UserDTO(UserBuilder.createSavedModel());
		Mono<UserDTO> savedUser = Mono.just(user);
		when(userService.findById(user.getId())).thenReturn(savedUser);

		EntityExchangeResult<UserDTO> entityExchangeResult = webTestClient.get()
				.uri(BASE_PATH + "/{userId}", user.getId())
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody(UserDTO.class)
				.returnResult();

		assertEquals(user, entityExchangeResult.getResponseBody());
	}

}
