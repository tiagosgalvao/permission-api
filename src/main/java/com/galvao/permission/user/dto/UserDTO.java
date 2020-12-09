package com.galvao.permission.user.dto;

import com.galvao.permission.user.model.User;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@Document
@AllArgsConstructor
public class UserDTO {
	private UUID id;
	private String nickname;

	public UserDTO(User user) {
		id = user.getId();
		nickname = user.getNickname();
	}
}
