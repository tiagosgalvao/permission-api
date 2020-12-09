package com.galvao.permission.department.builder;

import com.galvao.permission.department.model.Department;

import static com.galvao.permission.base.service.BaseService.CHILD_ID;
import static com.galvao.permission.base.service.BaseService.PARENT_ID;

public class DepartmentBuilder {

	public static Department createDepartmentToBeSaved() {
		return Department.builder()
				.name("Finance")
				.build();
	}

	public static Department createSavedParentDepartment() {
		return Department.builder()
				.id(PARENT_ID)
				.name("Parent")
				.build();
	}

	public static Department createSavedChildWithParent() {
		return Department.builder()
				.id(CHILD_ID)
				.name("Child")
				.parentId(PARENT_ID)
				.build();
	}
}
