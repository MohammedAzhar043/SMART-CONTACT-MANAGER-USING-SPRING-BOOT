package com.smartcontactmanager.demo.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smartcontactmanager.demo.entities.Contact;
import com.smartcontactmanager.demo.entities.User;

public interface ContactRepository extends JpaRepository<Contact, Integer> {

	// pagination

	/*
	 * @Query("from Contact as d where d.user.id=:userId") public List<Contact>
	 * findContactsByUser(@Param("userId") int userId);
	 */

	// cuurentPage-page
	// contact per page =5
	@Query("from Contact as d where d.user.id=:userId")
	public Page<Contact> findContactsByUser(@Param("userId") int userId, Pageable pageable);

	/* Pageable-this contains two things,current page and contacts per page */

	/* finding the contacts of the user both condition should be matched */
	/* this is for search*/
	public List<Contact> findByNameContainingAndUser(String name,User user);
}
