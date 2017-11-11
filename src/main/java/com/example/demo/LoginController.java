package com.example.demo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Controller
public class LoginController {

	@Autowired
	private RestTemplate restTemplate;
	
	@GetMapping("/login")
	public String goTologin() {
		return "login";
	}
	
	@PostMapping("/login")
	public String login(
			@RequestParam(value="username", required=false) String username,
			@RequestParam(value="password", required=false) String password,
			HttpSession session) {

		if (session.getAttribute("user")!=null) {
			return "redirect:/isLogged";
		} else if ("admin".equals(username) && "password".equals(password)) {
			session.setAttribute("user", username);
			return "redirect:/isLogged";
		} else {
			return "redirect:/login";
		}
	}

	@GetMapping("/logout")
	public String login(HttpSession session) {
		session.invalidate();
		return "redirect:/login";
	}
	
	@GetMapping("/isLogged")
	public String isLogged(@CookieValue(value="jsessionfroma", required=false) String jsessionfroma, HttpSession session, Model model) {
		model.addAttribute("isLogged","admin".equals(session.getAttribute("user")));

		if (jsessionfroma!=null) {
			ParameterizedTypeReference<String> typeRef = new ParameterizedTypeReference<String>() {};
			HttpHeaders requestHeaders = new HttpHeaders();
			requestHeaders.add("Cookie", "JSESSIONID=" + jsessionfroma + "; domain=a.127.0.0.1.xip.io;");
			HttpEntity requestEntity = new HttpEntity(null, requestHeaders);
			
			ResponseEntity<String> response = restTemplate.exchange("http://a.127.0.0.1.xip.io:9000/isLogged", HttpMethod.GET, requestEntity, typeRef);
			
			boolean isRemoteLogged = response.getBody().contains("isLogged? true");
			
			Pattern pattern = Pattern.compile("---(.*?)---");
			Matcher matcher = pattern.matcher(response.getBody());
			String username = "";
			while (matcher.find()) {
				username = matcher.group(1);
			}
			if (isRemoteLogged) {
				session.setAttribute("user",username);
			}
			
			System.out.println(response.getBody());		
		}
		
		return "isLogged";
	}
	
}
