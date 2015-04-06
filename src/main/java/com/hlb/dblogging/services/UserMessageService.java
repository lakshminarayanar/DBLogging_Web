package com.hlb.dblogging.services;

import java.util.List;

import com.hlb.dblogging.jpa.model.Users;

public interface UserMessageService {

	

	List<String> getListOfUsers(String username);
}
