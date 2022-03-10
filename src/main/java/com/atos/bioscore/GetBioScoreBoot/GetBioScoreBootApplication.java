package com.atos.bioscore.GetBioScoreBoot;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import com.atos.bioscore.GetBioScoreBoot.services.AuthService;

@Configuration
@EnableAsync
@SpringBootApplication
public class GetBioScoreBootApplication {

	
	
	public static void main(String[] args) {
		if(args != null && args.length > 0) {
			AuthService.tooAction = args[0];
			AuthService.soureofdata = args[1];
		}
		else {
			AuthService.tooAction="COLLECT";
			AuthService.soureofdata="REPO";
		}
		if(args.length >= 3)
			AuthService.skipload = args[2];
		SpringApplication.run(GetBioScoreBootApplication.class, args);
		
	}

}
