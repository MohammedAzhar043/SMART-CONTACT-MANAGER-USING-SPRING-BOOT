package com.smartcontactmanager.demo.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class Configurations {

	@Bean
	public CustomDetailServiceImp getCustomDetailServiceImp() {

		return new CustomDetailServiceImp();
	}

	@Bean
	public BCryptPasswordEncoder getBCryptPasswordEncoder() {

		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {

		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();

		authenticationProvider.setUserDetailsService(getCustomDetailServiceImp());
		authenticationProvider.setPasswordEncoder(getBCryptPasswordEncoder());

		return authenticationProvider;

	}
	
	/*
	 * @Bean public AuthenticationManager
	 * authenticationManager(AuthenticationConfiguration
	 * authenticationConfiguration) throws Exception { return
	 * authenticationConfiguration.getAuthenticationManager(); }
	 */
	 

//	loginPage()-- even if we were using the use/index still that page is appearing thats why we have specified the custom login 
//	loginProcessingUrl(null)--the url to submit the username and password
//	defaultSuccessUrl --the landing page after the successful login
//	failureUrl - the landing page after an unsuccessful login 

	@Bean
	public SecurityFilterChain SecurityFilterChain(HttpSecurity http) throws Exception {

		http.authorizeHttpRequests().requestMatchers("/admin/**").hasRole("ADMIN").requestMatchers("/user/**")
				.hasRole("USER").requestMatchers("/**").permitAll().and().formLogin().loginPage("/signin")
				.loginProcessingUrl("/dologin").defaultSuccessUrl("/user/index").and().csrf().disable();

		return http.build();

	}
}
