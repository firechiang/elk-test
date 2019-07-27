package com.firecode.elktest.biz.dao;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.firecode.elktest.biz.BasicTest;
import com.firecode.elktest.biz.domain.User;

public class UserDaoTest extends BasicTest {
	
	@Autowired
	private UserRepository userDao;
	
	@Test
	public void findByID(){
		User user = userDao.findById(1L).orElse(null);
	    System.err.println(user);
	}
}
