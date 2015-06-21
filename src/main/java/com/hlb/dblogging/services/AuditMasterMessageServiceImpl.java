package com.hlb.dblogging.services;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hlb.dblogging.jpa.model.AuditMaster;
import com.hlb.dblogging.jpa.repository.AuditMasterRepository;
import com.hlb.dblogging.log.utility.ApplLogger;

@Service
public class AuditMasterMessageServiceImpl implements AuditMasterMessageService {

	@Resource
	AuditMasterRepository auditMasterRepo;
	
	@Override
	public List<AuditMaster> getListOfMessages() {
		return	auditMasterRepo.findAllMessages();
	}

	

}
