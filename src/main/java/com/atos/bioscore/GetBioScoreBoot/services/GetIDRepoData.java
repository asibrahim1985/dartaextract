package com.atos.bioscore.GetBioScoreBoot.services;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.atos.bioscore.GetBioScoreBoot.dao.NewBatchRepoEntity;
import com.atos.bioscore.GetBioScoreBoot.dao.RidDataIdrepo;
import com.atos.bioscore.GetBioScoreBoot.utils.ApiUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class GetIDRepoData {

	@Value("${idrepo.read.url}")
	private String reporeadurl;
	
	@Value("${mosip.api.version}")
	private String mosipApiVer;
	
	@Value("${ssl.check.disable:false}")
	private boolean disablesslcheck;
	
	@Autowired
	ApiUtils apiutil;
	
	Logger logger = LoggerFactory.getLogger(GetIDRepoData.class);
	
	private HttpClient httpClient;
	
//	public GetIDRepoData() {
//		super();
//		System.out.println("************IDREPO*********************");
//		try {
//			inithttpclient();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	private void inithttpclient() throws Exception {
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
			}
		} };
		SSLContext sslContext = SSLContext.getInstance("TLS");
		sslContext.init(null, trustAllCerts, new SecureRandom());
		if (disablesslcheck)
			httpClient = HttpClient.newBuilder().sslContext(sslContext).version(HttpClient.Version.HTTP_1_1)
					.connectTimeout(Duration.ofSeconds(10)).build();
		else
			httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1)
					.connectTimeout(Duration.ofSeconds(10)).build();
	}



	public RidDataIdrepo getIDRepoData(NewBatchRepoEntity procEnt) {
		logger.info("Get IDREPO DATA for UIN/RID " + procEnt.getUin());
		RidDataIdrepo retVal = new RidDataIdrepo();
		try {
			inithttpclient();
			ObjectMapper objectmap = new ObjectMapper();
			objectmap.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
//			objectmap.disable(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS);
			objectmap.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			HttpRequest request = HttpRequest.newBuilder()
					.GET().uri(URI.create(reporeadurl + procEnt.getUin() + "?type=all"))
					.setHeader("Content-Type", "application/json")
					.setHeader("Cookie", "Authorization=" + AuthService.tokenVal)
					.build();
			HttpResponse<String> response;
			response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
			logger.debug(response.body()); 
			Map<String,Object> resp = objectmap.readValue(response.body().toString(),new TypeReference<Map<String,Object>>(){});
			Object errs = resp.get("errors");
			if(errs != null) {
				List<Object> errlist =  objectmap.convertValue( resp.get("errors"),new TypeReference<List<Object>>(){});
				if(errlist.size() > 0) {
					retVal.setErrormsg("FAILED IDREPO:" + errs.toString());
					logger.error("FAILED IDREPO for UIN/RID " + procEnt.getUin() + ":" + errs.toString()); 
					return retVal;
				}		
			}
			logger.debug("Received Data resp for RID/UIN " + procEnt.getRid() + ":" + resp);
			Map<String,Object> respData =  objectmap.convertValue(resp.get("response"),new TypeReference<Map<String,Object>>(){});
			logger.debug("Received Data Response for RID/UIN " + procEnt.getRid() + ":" + respData.toString());
			Map<String,Object> demofields =  objectmap.convertValue(respData.get("identity"),new TypeReference<Map<String,Object>>(){});
			List<Map<String,Object>> documents =  objectmap.convertValue(respData.get("documents"),new TypeReference<List<Map<String,Object>>>(){});
			logger.debug("Received Data for RID/UIN " + procEnt.getUin() + ":" + demofields.toString());
			
			Map<String,String> demofieldsString = new HashMap<String,String>();
			for(String fkey : demofields.keySet()) {
				if(demofields.get(fkey) != null )
					demofieldsString.put(fkey, objectmap.writeValueAsString(demofields.get(fkey)));
				else
					demofieldsString.put(fkey, null);
			}
			List<Map<String,Object>> bios = documents.stream().filter(x -> x.get("category").toString().equals("individualBiometrics")).collect(Collectors.toList());
			logger.debug("String map Data for RID/UIN " + procEnt.getUin() + ":" + demofieldsString.toString());
			retVal  = objectmap.convertValue(demofieldsString, RidDataIdrepo.class);
			logger.debug("Converted Data for RID/UIN " + procEnt.getUin() + ":" + retVal.toString());
			retVal.setErrormsg("");
			if(bios!= null && bios.size() > 0) {
				retVal.setBiodata(bios.get(0).get("value").toString());  
				logger.debug("BIO Data for RID/UIN {} , successfully read" , procEnt.getUin());
			}
			logger.info("Processing Demo Data Success for RID/UIN:" + procEnt.getUin());
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ID REPO Call failed for UIN/RID " + procEnt.getUin() + ":" + e.getMessage()); 
			retVal.setErrormsg(e.getMessage());
		}
		return retVal;
		
	}
}
