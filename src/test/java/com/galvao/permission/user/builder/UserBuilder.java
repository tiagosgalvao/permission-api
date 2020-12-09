package com.galvao.permission.user.builder;

import com.galvao.permission.user.model.User;

import static com.galvao.permission.base.service.BaseService.USER_ID;

public class UserBuilder {

	public static User createSavedModel() {
		return User.builder().id(USER_ID).nickname("USER").build();
	}
}
