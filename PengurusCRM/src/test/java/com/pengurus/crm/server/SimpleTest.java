package com.pengurus.crm.server;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.pengurus.crm.daos.UserDAO;
import com.pengurus.crm.entities.User;
import com.pengurus.crm.enums.UserRole;

@ContextConfiguration(locations = {"classpath:com/pengurus/crm/server/testContext.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class SimpleTest {
	
	@Autowired
	private UserDAO ud;
	
	@Autowired
	private AuthorizationService authService;
	
	private Long id;
	
	@Before 
	public void createUser(){
		List<UserRole> li = new ArrayList<UserRole>();
		User u = new User(li, "Username", "pass", "descr");
		Assert.assertNotNull(u);
		id = ud.create(u);
		Assert.assertTrue(id!=-1);
		User u2 = new User(li, "Username2", "pass", "descr2");
		id = ud.create(u2);
	}
	
	@Test
	public void logIn(){
		authService.login("Username", "pass");
		Assert.assertTrue(SecurityContextHolder.getContext().getAuthentication().isAuthenticated());
	}
	

}
