package com.smart.configration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.smart.dao.UserRepo;
import com.smart.entities.User;

public class Userdetailservice implements UserDetailsService{

	@Autowired
	private UserRepo userrep;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user= userrep.getUserByUserName(username);
		if(user==null) {
		throw new UsernameNotFoundException("Could not found User");
		}
		CustomUserDetail custom = new CustomUserDetail(user);
		return custom;
	}

}
