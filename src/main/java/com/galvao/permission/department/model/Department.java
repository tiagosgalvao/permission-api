package com.galvao.permission.department.model;

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
public class Department {
	@Id
	private UUID id;
	private String name;
	private UUID parentId;
}
