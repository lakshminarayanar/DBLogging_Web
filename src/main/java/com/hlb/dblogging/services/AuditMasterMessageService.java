package com.hlb.dblogging.services;

import java.util.Date;
import java.util.List;

import com.hlb.dblogging.jpa.model.AuditMaster;

public interface AuditMasterMessageService {

	List<AuditMaster> getListOfMessages();
	
}
