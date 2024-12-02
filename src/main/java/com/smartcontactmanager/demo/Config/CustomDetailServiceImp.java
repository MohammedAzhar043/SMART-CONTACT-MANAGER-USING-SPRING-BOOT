package com.smartcontactmanager.demo.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.smartcontactmanager.demo.dao.UserReposotery;
import com.smartcontactmanager.demo.entities.User;

public class CustomDetailServiceImp implements UserDetailsService {

	@Autowired
	private UserReposotery reposotery;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User userByUserName = reposotery.getUserByUserName(username);

		if (userByUserName == null) {
			throw new UsernameNotFoundException("could not found user!!!!");
		}

		CustomDetailServices customDetailServices = new CustomDetailServices(userByUserName);

		return customDetailServices;

	}

}
