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

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.atos.bioscore.GetBioScoreBoot.dao.NewBatchEntity;
import com.atos.bioscore.GetBioScoreBoot.dao.RidData;
import com.atos.bioscore.GetBioScoreBoot.utils.ApiUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class GetDemoDataService {

	@Value("${packet.searchfield.id}")
	private String packetSerFid;
	
	@Value("${packet.searchfield.url}")
	private String serFieldurl;
	
	@Value("${mosip.api.version}")
	private String mosipApiVer;
	
	@Value("${mosip.fields.fetch}")
	private String[] filedsSer;
	
	@Value("${ssl.check.disable:false}")
	private boolean disablesslcheck;
	
	@Autowired
	ApiUtils apiutil;
	
	Logger logger = LoggerFactory.getLogger(GetDemoDataService.class);
	
	
	private HttpClient httpClient;
	
	
	public GetDemoDataService() {
		super();
		try {
			inithttpclient();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

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
		if(disablesslcheck)
		httpClient = HttpClient.newBuilder().sslContext(sslContext).version(HttpClient.Version.HTTP_1_1)
				.connectTimeout(Duration.ofSeconds(10)).build();
		else
			httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1)
			.connectTimeout(Duration.ofSeconds(10)).build();
	}
	
	public RidData readPacketFields(NewBatchEntity procEnt) {
		RidData newridData = new RidData();
		try {
			logger.info("Processing Demo Data for RID/UIN:" + procEnt.getRid());
			ObjectMapper objectmap = new ObjectMapper();
			objectmap.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
//			objectmap.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
//			objectmap.disable(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS);
			Map<String, Object> demoDataRequest = new HashMap<String, Object>();
			demoDataRequest.put("id", packetSerFid);
			demoDataRequest.put("requesttime", apiutil.getUTCCurrentDateTime());
			demoDataRequest.put("version", mosipApiVer);
			Map<String, Object> demoDataRequestBody = new HashMap<String, Object>();
			demoDataRequestBody.put("id",procEnt.getRid());
			demoDataRequestBody.put("fields",filedsSer);
			demoDataRequestBody.put("bypassCache",true);
			demoDataRequestBody.put("source", "REGISTRATION_CLIENT");
			demoDataRequestBody.put("process", "NEW");
			demoDataRequest.put("request", demoDataRequestBody);
			HttpRequest request = HttpRequest.newBuilder()
					.POST(HttpRequest.BodyPublishers.ofString(objectmap.writeValueAsString(demoDataRequest)))
					.uri(URI.create(serFieldurl))
					.setHeader("Content-Type", "application/json")
					.setHeader("Cookie", "Authorization=" + AuthService.tokenVal)
					.build();
			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
			if( response.statusCode() == 200 ) {
				Map<String,Object> resp = objectmap.readValue(response.body(),new TypeReference<Map<String,Object>>(){});
				Object errs = resp.get("errors");
				if(errs != null) {
					List<Object> errlist =  objectmap.convertValue( resp.get("errors"),new TypeReference<List<Object>>(){});
					if(errlist.size() > 0) {
						newridData.setErrormsg(errs.toString());
						logger.error("Processing Demo Data for RID/UIN:" + procEnt.getRid() + " Failed with errors :" + errs.toString());
						return newridData;
					}
				}
				Map<String,Object> respData =  objectmap.convertValue(resp.get("response"),new TypeReference<Map<String,Object>>(){});
				Map<String,Object> demofields =  objectmap.convertValue(respData.get("fields"),new TypeReference<Map<String,Object>>(){});
				logger.debug("Received Data for RID/UIN " + procEnt.getRid() + ":" + demofields.toString());
				Map<String,String> demofieldsString = new HashMap<String,String>();
				for(String fkey : demofields.keySet()) {
					if(demofields.get(fkey) != null )
						demofieldsString.put(fkey, demofields.get(fkey).toString());
					else
						demofieldsString.put(fkey, null);
				}
				logger.debug("String map Data for RID/UIN " + procEnt.getRid() + ":" + demofieldsString.toString());
				newridData  = objectmap.convertValue(demofieldsString, RidData.class);
				logger.debug("Converted Data for RID/UIN " + procEnt.getRid() + ":" + newridData.toString());
				logger.info("Processing Demo Data Success for RID/UIN:" + procEnt.getRid());
				return newridData;
			}
			else {
				logger.error("Processing Demo Data for RID/UIN:" + procEnt.getRid() + " Failed with Status code:" + response.statusCode());
				newridData.setErrormsg("Status code:" + response.statusCode());
				return newridData;
			}
		}
		catch(Exception e) {
			logger.error("Processing Demo Data for RID/UIN:" + procEnt.getRid() + " Failed:" + e.getMessage());
			newridData.setErrormsg(e.getMessage());
			e.printStackTrace();
			return newridData;
		}
	}
}
