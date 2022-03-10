package com.atos.bioscore.GetBioScoreBoot.services;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.atos.bioscore.GetBioScoreBoot.dao.NewBatchRepoEntity;
import com.atos.bioscore.GetBioScoreBoot.dao.RidData;
import com.atos.bioscore.GetBioScoreBoot.dao.RidDataIdrepo;
import com.atos.bioscore.GetBioScoreBoot.repo.NewBatchRepoRepo;
import com.atos.bioscore.GetBioScoreBoot.repo.RidDataIdrepoRepo;
import com.atos.bioscore.GetBioScoreBoot.utils.EncryptionUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Component
public class PopulateRepoData {
	
	Logger logger = LoggerFactory.getLogger(PopulateRepoData.class);

	@Value("${encrypt.data:false}")
	private boolean encryptData;
	
	@Value("${mosip.fields.fetch}")
	private List<String> allFields;
	
	@Autowired
	NewBatchRepoRepo newBatchRepo;
	
	@Autowired
	GetIDRepoData getIDRepo;
	
	@Autowired
	RidDataIdrepoRepo idrepo;
	
	@Autowired
	EncryptionUtil encUtil;
	
	public void popData() {
		List<NewBatchRepoEntity> procBatch = newBatchRepo.findByProcessedAndErrmgOrderByCreationDateAsc("", "");
		for(NewBatchRepoEntity procEnt : procBatch) {
			RidDataIdrepo  ridrepoid = getIDRepo.getIDRepoData(procEnt);
			
			ridrepoid.setRid(procEnt.getRid());
			ridrepoid.setUin(procEnt.getUin());
			ridrepoid.setRegType(procEnt.getRegType());
			ridrepoid.setCreationDate(procEnt.getCreationDate());
			ridrepoid.setExtracted("");
	    	if(ridrepoid.getErrormsg().length() > 500) 
	    		ridrepoid.setErrormsg(ridrepoid.getErrormsg().substring(0,450));
	    	if (ridrepoid.getErrormsg().trim().length() > 0)
	    		ridrepoid.setStatus("FAILED");
	    	else
	    		ridrepoid.setStatus("SUCCESS");
	    	idrepo.save(ridrepoid);
			procEnt.setProcessed("X");
			if(encryptData) {
				RidDataIdrepo encnewridData  = encryptData(ridrepoid);
				idrepo.save(encnewridData);
	    	} else {
	    		idrepo.save(ridrepoid);
	    	}
			newBatchRepo.save(procEnt);
		}
	}
	
	private RidDataIdrepo encryptData(RidDataIdrepo newridData ) {
		RidDataIdrepo encnewridData = new RidDataIdrepo();
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
		encnewridData = objectmap.convertValue(dataMAp,RidDataIdrepo.class);
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
