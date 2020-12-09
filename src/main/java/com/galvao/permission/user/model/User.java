package com.galvao.permission.user.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Document
@Builder
@AllArgsConstructor
public class User {
	@Id
	private UUID id;
	private String nickname;
}
