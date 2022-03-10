package com.atos.bioscore.GetBioScoreBoot.services;

import org.springframework.stereotype.Component;

import com.atos.bioscore.GetBioScoreBoot.dao.NewBatchEntity;
import com.atos.bioscore.GetBioScoreBoot.dao.RidData;
import com.atos.bioscore.GetBioScoreBoot.repo.NewBatchRepo;
import com.atos.bioscore.GetBioScoreBoot.repo.RidDataRepo;
import com.atos.bioscore.GetBioScoreBoot.utils.EncryptionUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
//import org.springframework.scheduling.annotation.Async;

@Component
public class PopulaeUinData {
	
	Logger logger = LoggerFactory.getLogger(PopulaeUinData.class);
	
	
	@Value("${encrypt.data:false}")
	private boolean encryptData;
	
	@Value("${mosip.fields.fetch}")
	private List<String> allFields;
	
	
	
	@Autowired
	NewBatchRepo newBatchRepo;
	
	@Autowired
	GetDemoDataService getDemo;
	
	@Autowired
	RidDataRepo riddatarepo;
	
	@Autowired
	GetBioData getBio;
	
	@Autowired
	EncryptionUtil encUtil;
	
//	@Async
	public void popBioData() {
	    List<NewBatchEntity> procBatch =  newBatchRepo.findByProcessed("");
	    for(NewBatchEntity procEnt : procBatch) { 
	    	RidData newridData = getDemo.readPacketFields(procEnt);
	    	if(newridData.getErrormsg() == null)
	    		newridData.setErrormsg("");
	    	getBio.getBioScoreAndData(procEnt, newridData,false);
	    	if(newridData.getParentOrGuardianRID() != null || newridData.getParentOrGuardianUIN() != null)
	    		getBio.getBioScoreAndData(procEnt, newridData,true);
	    	newridData.setRid(procEnt.getRid());
	    	newridData.setRegType(procEnt.getRegType());
	    	newridData.setCreationDate(procEnt.getCreationDate());
	    	newridData.setExtracted("");
	    	if(newridData.getErrormsg().length() > 500) 
	    		newridData.setErrormsg(newridData.getErrormsg().substring(0,450));
	    	if (newridData.getErrormsg().trim().length() > 0)
	    		newridData.setStatus("FAILED");
	    	else
	    		newridData.setStatus("SUCCESS");
	    	if(encryptData) {
	    		RidData encnewridData  = encryptData(newridData);
	    		riddatarepo.save(encnewridData);
	    	} else {
	    		riddatarepo.save(newridData);
	    	}
	    	procEnt.setProcessed("X");
	    	newBatchRepo.save(procEnt);
	    }
	}
	
	private RidData encryptData(RidData newridData ) {
		RidData encnewridData = new RidData();
		ObjectMapper objectmap = new ObjectMapper().registerModule(new JavaTimeModule());
		objectmap.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
		objectmap.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		objectmap.disable(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS);
		Map<String,Object> dataMAp = objectmap.convertValue(newridData, new TypeReference<Map<String,Object>>(){});
		for(String fKey : allFields) {
			encryptField(fKey,dataMAp,newridData.getRid());
		}
		encryptField("face",dataMAp,newridData.getRid());
		encryptField("leftIris",dataMAp,newridData.getRid());
		encryptField("rightIris",dataMAp,newridData.getRid());
		encnewridData = objectmap.convertValue(dataMAp,RidData.class);
		return encnewridData;
	}
	
	private void encryptField(String fKey, Map<String,Object> dataMAp,String rid) {
		if(dataMAp.get(fKey) != null) {
			try {
				String encryptedFieldData = encUtil.encryptTextAES(dataMAp.get(fKey).toString());
				dataMAp.put(fKey, encryptedFieldData);
			}
			catch(Exception e) {
				logger.error("Failed to encrypt RID/UIN " + rid 
				+ "field " + fKey + ":" + e.getMessage());
			}
		}
		else {
			logger.debug("Failed to encrypt RID/UIN " + rid + " field not found:" + fKey);
		}
	}
}
