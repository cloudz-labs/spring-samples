package com.skcc.security.sample.controller;

import java.security.Principal;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
	
	/**
	 * 권한에 상관없이 접근 가능한 URL.
	 * @return
	 */
	@RequestMapping("/anonymous")
	public String helloAnonymous() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		if (principal instanceof UserDetails) {
			return ((UserDetails) principal).getUsername();
		} else {
			return principal.toString();
		}
	}
	
	/**
	 * ADMIN 권한만 접근 가능한 URL.
	 * @param principal
	 * @return
	 */
	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping("/admin")
	public Principal helloAdmin(Principal principal) {
		return principal;
	}
	
	/**
	 * ADMIN, USER 권한이 접근 가능한 URL.
	 * @param principal
	 * @return
	 */
	@PreAuthorize("hasRole('USER')")
	@RequestMapping("/user")
	public Principal helloUser(Principal principal) {
		return principal;
	}
	
}
