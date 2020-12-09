package com.galvao.permission.permission.dto;

import com.galvao.permission.permission.enums.PermissionType;
import com.galvao.permission.permission.model.Permission;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;

@Value
@Document
@AllArgsConstructor
public class PermissionDTO {
	private UUID id;
	@NonNull
	private UUID userId;
	@NonNull
	private UUID departmentId;
	@NonNull
	private PermissionType permissionType;

	public PermissionDTO(Permission permission) {
		id = permission.getId();
		userId = permission.getUserId();
		departmentId = permission.getDepartmentId();
		permissionType = permission.getPermissionType();
	}
}
