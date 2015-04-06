package com.hlb.dblogging.services;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.hlb.dblogging.jpa.repository.UsersRepository;

@Service
public class UserMessageServiceImpl implements UserMessageService {
	
	
    private String txt1;

	@Resource
	UsersRepository usersRepo;
	
	  

	public List<String> getListOfUsers(String username) {
		
		 return usersRepo.findUserList(username);
	}
	
	
}

