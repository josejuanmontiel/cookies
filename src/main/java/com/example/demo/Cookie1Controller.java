package com.example.demo;

import java.util.Arrays;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class Cookie1Controller {

	@RequestMapping("/cookie1")
	public String greeting(
			@RequestParam(value = "rp", required = false) String reqparam,
			@RequestParam(value = "sp", required = false) String sesparam,
			@RequestParam(value = "cp", required = false) String cookiparam,
			HttpSession session,
 			HttpServletResponse response,
			Model model) {

    if (reqparam!=null) model.addAttribute("rp", reqparam);
		if (sesparam!=null) session.setAttribute("sp", sesparam);
		System.out.println("sp="+sesparam);

		if (cookiparam!=null) {
			Cookie newCookie = new Cookie("cp", cookiparam);
			newCookie.setMaxAge(24 * 60 * 60);
			response.addCookie(newCookie);
		}

		return "cookie1";
	}

	public static String getCookieValue(HttpServletRequest req, String cookieName) {
		return Arrays.stream(req.getCookies()).
				filter(c -> c.getName().equals(cookieName)).
				findFirst().
				map(Cookie::getValue).
				orElse(null);
	}

}
