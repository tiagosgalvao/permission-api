package com.galvao.permission.permission.model;

import com.galvao.permission.permission.enums.PermissionType;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

import lombok.Builder;
import lombok.Value;

@Value
@Document
@Builder
public class Permission {
	@Id
	private UUID id;
	private UUID userId;
	private UUID departmentId;
	private PermissionType permissionType;
}
