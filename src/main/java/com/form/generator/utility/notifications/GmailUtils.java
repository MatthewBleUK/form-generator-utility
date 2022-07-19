package com.form.generator.utility.notifications;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.gmail.Gmail;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GmailUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(GmailUtils.class);
	private static final String APPLICATION_NAME = "Java Auth";
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	private static String refreshToken;
	private static String clientId;
	private static String clientSecret;
	private static String tokenUrl;
	private static File credentials;
	public static Gmail service = null;

	@Autowired
	public GmailUtils(
			@Value("${google.client.refresh-token}") String refreshToken,
			@Value("${google.client.id}") String clientId,
			@Value("${google.client.secret}") String clientSecret,
			@Value("${google.client.token.url}") String tokenUrl,
			@Value("${google.credentials}") File credentials) {

		GmailUtils.refreshToken = refreshToken;
		GmailUtils.clientId = clientId;
		GmailUtils.clientSecret = clientSecret;
		GmailUtils.tokenUrl = tokenUrl;
		GmailUtils.credentials = credentials;
	}

	/**
	 * Sets Google Client Secret, Access Token and Refresh Token
	 */
	public static Gmail getGmailService() throws IOException, GeneralSecurityException {

		InputStream in = new FileInputStream(credentials);
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

		// Credential builder
		Credential authorize = new GoogleCredential
				.Builder()
				.setTransport(GoogleNetHttpTransport.newTrustedTransport())
				.setJsonFactory(JSON_FACTORY)
				.setClientSecrets(clientSecrets.getDetails().getClientId(),
						clientSecrets.getDetails().getClientSecret())
				.build()
				.setAccessToken(getAccessToken())
				.setRefreshToken(refreshToken);

		// Create Gmail service
		final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

		service = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, authorize)
				.setApplicationName(GmailUtils.APPLICATION_NAME).build();

		return service;
	}

	/**
	 * Access tokens expire every hour, the method below will generate
	 * a new access token every time it is called using the refresh token,
	 * client secret and clientId
	 */
	private static String getAccessToken() {

		try {
			Map<String, Object> params = new LinkedHashMap<>();

			params.put("grant_type", "refresh_token");
			params.put("client_id", clientId);
			params.put("client_secret", clientSecret);
			params.put("refresh_token", refreshToken);

			StringBuilder postData = new StringBuilder();

			for (Map.Entry<String, Object> param : params.entrySet()) {

				if (postData.length() != 0) {

					postData.append('&');
				}

				postData.append(URLEncoder.encode(param.getKey(), StandardCharsets.UTF_8));
				postData.append('=');
				postData.append(URLEncoder.encode(String.valueOf(param.getValue()), StandardCharsets.UTF_8));
			}

			byte[] postDataBytes = postData.toString().getBytes(StandardCharsets.UTF_8);

			URL url = new URL(tokenUrl);

			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setDoOutput(true);
			con.setUseCaches(false);
			con.setRequestMethod("POST");
			con.getOutputStream().write(postDataBytes);

			BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
			StringBuilder buffer = new StringBuilder();

			for (String line = reader.readLine(); line != null; line = reader.readLine()) {

				buffer.append(line);
			}

			JSONObject json = new JSONObject(buffer.toString());

			return json.getString("access_token");

		} catch (Exception ex) {

			String message = "Error while trying to get Access Token";
			LOGGER.error(message);

			throw new RuntimeException(message, ex);
		}
	}
}