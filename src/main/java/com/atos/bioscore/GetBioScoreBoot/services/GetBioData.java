package com.atos.bioscore.GetBioScoreBoot.services;

import java.io.IOException;
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
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class GetBioData {

	@Value("${biometeric.read.url}")
	private String bioreadurl;

	@Value("${mosip.api.version}")
	private String mosipApiVer;

	@Value("${ssl.check.disable:false}")
	private boolean disablesslcheck;

	@Autowired
	ApiUtils apiutil;

	public GetBioData() {
		super();
		try {
			inithttpclient();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	Logger logger = LoggerFactory.getLogger(GetBioData.class);

	private HttpClient httpClient;

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

	public void getBioScoreAndData(NewBatchEntity procEnt, RidData newridData,boolean guard) {
		logger.info("Get BIO DATA for UIN/RID " + procEnt.getRid());
		try {
			ObjectMapper objectmap = new ObjectMapper();
			Map<String, Object> bioRequest = new HashMap<String, Object>();
			bioRequest.put("id", "string");
			bioRequest.put("version", mosipApiVer);
			bioRequest.put("requesttime", apiutil.getUTCCurrentDateTime());
			Map<String, Object> bioRequestBody = new HashMap<String, Object>();
			bioRequestBody.put("bypassCache", true);
			bioRequestBody.put("id", procEnt.getRid());
			if(guard)
				bioRequestBody.put("person", "parentOrGuardianBiometrics");
			else
				bioRequestBody.put("person", "individualBiometrics");
			bioRequestBody.put("source", "REGISTRATION_CLIENT");
			bioRequestBody.put("process", "NEW");
			bioRequest.put("request", bioRequestBody);
			try {
				HttpRequest request = HttpRequest.newBuilder()
						.POST(HttpRequest.BodyPublishers.ofString(objectmap.writeValueAsString(bioRequest)))
						.uri(URI.create(bioreadurl)).setHeader("Content-Type", "application/json")
						.setHeader("Cookie", "Authorization=" + AuthService.tokenVal).build();
				HttpResponse<String> response;
				response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
				Map<String, Object> resp = objectmap.readValue(response.body(),
						new TypeReference<Map<String, Object>>() {
						});
				Object errs = resp.get("errors");
				if (errs != null) {
					List<Object> errlist = objectmap.convertValue(resp.get("errors"),
							new TypeReference<List<Object>>() {
							});
					if (errlist.size() > 0) {
						newridData.setErrormsg("FAILED BIO:" + errs.toString());
						logger.error("BIO-FAILED for UIN/RID " + procEnt.getRid() + ":" + errs.toString());
						return;
					}

				}
				Map<String, Object> respbody = objectmap.convertValue(resp.get("response"),
						new TypeReference<Map<String, Object>>() {
						});
				if (respbody == null) {
					newridData.setErrormsg("FAILED BIO:response body is null");
					logger.error("BIO-FAILED for UIN/RID " + procEnt.getRid() + ":BIO:response body is null");
					return;
				}
				List<Map<String, Object>> segs = objectmap.convertValue(respbody.get("segments"),
						new TypeReference<List<Map<String, Object>>>() {
						});
				for (Map<String, Object> ent : segs) {
					Map<String, Object> bdbInfo = objectmap.convertValue(ent.get("bdbInfo"),
							new TypeReference<Map<String, Object>>() {
							});
					Map<String, Object> quality = objectmap.convertValue(bdbInfo.get("quality"),
							new TypeReference<Map<String, Object>>() {
							});
					String type = bdbInfo.get("type").toString();
					String subtype = bdbInfo.get("subtype").toString();
					if(guard) {
						if (type.contains("IRIS")) {
							if (subtype.contains("Left")) {
								newridData.setIrisleftGuaridanScore(quality.get("score").toString());
								newridData.setIrisleftGuaridan(ent.get("bdb").toString());
							} else {
								
								newridData.setIrisrightGuaridan(ent.get("bdb").toString());
								newridData.setIrisrightGuaridanScore(quality.get("score").toString());
							}
						} else {
							newridData.setFaceGuaridan(ent.get("bdb").toString());
							newridData.setFaceGuaridanScore((quality.get("score").toString()));
						}
					} else {
						if (type.contains("IRIS")) {
							if (subtype.contains("Left")) {
								newridData.setLeftIrisScore(quality.get("score").toString());
								newridData.setLeftIris(ent.get("bdb").toString());
							} else {
								newridData.setRightIris(ent.get("bdb").toString());
								newridData.setRightIrisScore(quality.get("score").toString());
							}
						} else {
							newridData.setFace(ent.get("bdb").toString());
							newridData.setFaceScore(quality.get("score").toString());
						}

					}
					
				}
				logger.info("Get BIO DATA Success for UIN/RID " + procEnt.getRid());

			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
				logger.error("BIO-FAILED for UIN/RID " + procEnt.getRid() + ":" + e.getMessage());

			}
		} catch (Exception e) {
			logger.error("BIO-FAILED for UIN/RID " + procEnt.getRid() + ":" + e.getMessage());
			e.printStackTrace();
			newridData.setErrormsg("FAILED-BIO" + e.getMessage());
		}

	}

}
