package com.smartcontactmanager.demo.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.smartcontactmanager.demo.entities.User;

public interface UserReposotery extends CrudRepository<User, Integer> {

	//spring security
	//custom method
	//we are passing email as conditions 
	
	@Query("select u from User u where u.email =:email")
	public User getUserByUserName(@Param("email")String email);
	
	//we are passing name as conditions 
	@Query("select u from User u where u.name =:username")
	public User getUserByName(@Param("username")String userName);
}
