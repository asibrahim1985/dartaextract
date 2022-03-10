package com.atos.bioscore.GetBioScoreBoot.services;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AuthService {

	private HttpClient httpClient;

	public static String tokenVal;

	public static String tooAction;
	
	public static String skipload;

	public static String soureofdata;

	@Value("${token.request.url}")
	private String tokenUrl;

	@Value("${token.request.id}")
	private String tokenReqID;

	@Value("${mosip.api.version}")
	private String mAppVersion;

	@Value("${biocol.client.id}")
	private String biocolClient;

	@Value("${biocol.client.secret}")
	private String biocolSercet;

	@Value("${biocol.client.appId}")
	private String biocolAppId;
	
	
	@Value("${ssl.check.disable:false}")
	private boolean disablesslcheck;

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

	public void getTokenFromMosip() throws Exception {
		inithttpclient();
		ObjectMapper objectmap = new ObjectMapper();
		Map<String, Object> authRequest = new HashMap<String, Object>();
		authRequest.put("id", tokenReqID);
		authRequest.put("version", mAppVersion);
		authRequest.put("requesttime", "2020-07-01T10:15:30Z");
		Map<String, Object> authRequestBody = new HashMap<String, Object>();
		authRequestBody.put("clientId", biocolClient);
		authRequestBody.put("secretKey", biocolSercet);
		authRequestBody.put("appId", biocolAppId);
		authRequest.put("request", authRequestBody);
		try {
			HttpRequest request = HttpRequest.newBuilder()
					.POST(HttpRequest.BodyPublishers.ofString(objectmap.writeValueAsString(authRequest)))
					.uri(URI.create(tokenUrl)).setHeader("Content-Type", "application/json").build();
			HttpResponse<String> response;
			response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
			if (response.statusCode() != 200) {
				System.out.println("Auth FAiled status code" + response.statusCode());
				throw new Exception(response.statusCode() + ":StatusCode");
			}
			HttpHeaders headers = response.headers();
			Optional<String> token = headers.firstValue("Authorization");
			if (token.isPresent()) {
				tokenVal = token.get();
			} else {
				System.out.println("Auth FAiled");
				System.out.println(response.body());
				throw new Exception(response.body().toString() + ":StatusCode");
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}

	}

}
