package com.smartcontactmanager.demo.helper;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Component
public class SessionHelper {

	public void removeMessageFromSession() {

		try {

			/* we need session object to remove the message */
			/*
			 * RequestContextHolder by using this we will get the session n need to
			 * typecast
			 */
			System.out.println("removing the message");
			HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();

			session.removeAttribute("message");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
