package com.microservice.loan.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class UserClientHandler {

	private final RestTemplate restTemplate;

	@Value("${loan_microservice.verify_user.url}")
	private String verifyUserUrl;

	@Autowired
	public UserClientHandler(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public boolean verifyUser(Long userId) {
		String url = UriComponentsBuilder.fromHttpUrl(verifyUserUrl).queryParam("userId", userId).toUriString();
		ResponseEntity<Boolean> response = restTemplate.getForEntity(url, Boolean.class);
		return response.getBody() != null && response.getBody();
	}
}
