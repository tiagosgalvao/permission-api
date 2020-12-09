package com.galvao.permission.permission.builder;

import com.galvao.permission.permission.enums.PermissionType;
import com.galvao.permission.permission.model.Permission;

import java.util.UUID;

import static com.galvao.permission.base.service.BaseService.PARENT_ID;
import static com.galvao.permission.base.service.BaseService.USER_ID;

public class PermissionBuilder {

	public static Permission createPermissionToBeSaved() {
		return Permission.builder()
				.userId(USER_ID)
				.departmentId(PARENT_ID)
				.permissionType(PermissionType.EDIT)
				.build();
	}

	public static Permission createPermissionViewToBeSaved() {
		return Permission.builder()
				.userId(USER_ID)
				.departmentId(PARENT_ID)
				.permissionType(PermissionType.VIEW)
				.build();
	}

	public static Permission createSavedPermission() {
		return Permission.builder()
				.id(UUID.randomUUID())
				.userId(USER_ID)
				.departmentId(PARENT_ID)
				.permissionType(PermissionType.EDIT)
				.build();
	}

	public static Permission createSavedPermissionDefinedDepartment(UUID departmentId) {
		return Permission.builder()
				.id(UUID.randomUUID())
				.userId(USER_ID)
				.departmentId(departmentId)
				.permissionType(PermissionType.EDIT)
				.build();
	}
}
