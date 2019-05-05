package com.yucheng.forum.service;

import java.util.Date;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.yucheng.forum.dao.UserDao;
import com.yucheng.forum.model.User;

/**
 * 注册的service层
 * 
 * @author xiezhiping
 *
 */
@Service
public class RegisterService {
	@Autowired
	UserDao userDao;

	@Autowired
	PasswordEncoder passwordEncoder;

	public String reg(String username, String password, String introduction) {
		User user = new User();
		if (userDao.getUserByUsername(username) == null) {
			user.setUsername(username);
			user.setPassword(password);
			user.setPassword(passwordEncoder.encode(password));
			user.setRole("USER");
			if (Objects.equals(introduction, "")) {
				user.setIntroduction(null);
			} else {
				user.setIntroduction(introduction);
			}
			user.setCreatedDate(new Date());
			userDao.addUser(user);
			return "/login";
		} else {
			return "/register";
		}
	}
}
