package com.smartcontactmanager.demo.Config;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.smartcontactmanager.demo.entities.User;

public class CustomDetailServices implements UserDetails {

	private User user;

	public CustomDetailServices(User user) {
		super();
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		SimpleGrantedAuthority SimpleGrantedAuthority = new SimpleGrantedAuthority(user.getRole());

		return List.of(SimpleGrantedAuthority);
	}

	
	@Override
	public String getPassword() {
		String password = user.getPassword();
		return password;
	}

	@Override
	public String getUsername() {
		String name = user.getName();
		return name;
	}

}
