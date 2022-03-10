package com.atos.bioscore.GetBioScoreBoot.config;



import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.atos.bioscore.GetBioScoreBoot.services.AuthService;
import com.atos.bioscore.GetBioScoreBoot.services.ExtractionIDRepo;
import com.atos.bioscore.GetBioScoreBoot.services.ExtractionService;
import com.atos.bioscore.GetBioScoreBoot.services.GetNewBatch;
import com.atos.bioscore.GetBioScoreBoot.services.GetNewBatchRepo;
import com.atos.bioscore.GetBioScoreBoot.services.PopulaeUinData;
import com.atos.bioscore.GetBioScoreBoot.services.PopulateRepoData;

@Component 
public class ApplicationExecutor {
	
	Logger logger = LoggerFactory.getLogger(ApplicationExecutor.class);
	
	@Autowired
	PopulaeUinData popData;
	
	@Autowired
	PopulateRepoData popRepoData;
	
	@Autowired
	AuthService authSer;
	
	@Autowired
	GetNewBatch createNewBatch;
	
	@Autowired
	GetNewBatchRepo createNewBatchRepo;
	
	@Autowired
	ExtractionService extService;
	
	@Autowired
	ExtractionIDRepo extractIDrepo;

	@PostConstruct
	public void runBioExtractionSteps() throws Exception {
		authSer.getTokenFromMosip();
		logger.info("Token Generated Successfuly");
		logger.info("Starting Tool in mode {} {} {}",AuthService.tooAction,AuthService.soureofdata,AuthService.skipload);
		if(AuthService.tooAction.equalsIgnoreCase("COLLECT")) {
			if(AuthService.soureofdata.equalsIgnoreCase("REG")) {
				if(AuthService.skipload.equals("LOAD"))
					createNewBatch.fillProcessingTable();
				popData.popBioData();
			} else {
				if(AuthService.skipload.equals("LOAD"))
					createNewBatchRepo.fillProcessingTable();
				popRepoData.popData();
			}
			
		} else {
			if(AuthService.soureofdata.equalsIgnoreCase("REG"))
				extService.extractData();
			else
				extractIDrepo.extractData();
		}
	}
	
}
