package com.smartcontactmanager.demo.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.smartcontactmanager.demo.dao.ContactRepository;
import com.smartcontactmanager.demo.dao.UserReposotery;
import com.smartcontactmanager.demo.entities.Contact;
import com.smartcontactmanager.demo.entities.User;

@RestController
public class SearchController {

	
	@Autowired
	private UserReposotery reposotery;
	@Autowired
	private ContactRepository contactRepository;
	
	/*search handler*/
	
	@GetMapping("/search/{query}")
	public ResponseEntity<?> search( @PathVariable("query") String query,Principal principal){
		
		System.out.println(query);
		User user = this.reposotery.getUserByName(principal.getName());
		List<Contact> contacts = this.contactRepository.findByNameContainingAndUser(query, user);
		
		return ResponseEntity.ok(contacts);
	}
}
