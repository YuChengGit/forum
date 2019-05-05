package com.yucheng.forum.configuration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.yucheng.forum.dao.UserDao;
import com.yucheng.forum.model.User;

public class MyUserDetailsService implements UserDetailsService {

	private boolean accountNonExpired = true;
	private boolean credentialsNonExpired = true;
	private boolean accountNonLocked = true;

	@Autowired
	private UserDao userDao;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			User user = this.userDao.getUserByUsername(username);
			System.out.println(user);
			if (null == user) {
				throw new UsernameNotFoundException("Can't find user by username: " + username);
			}

			List<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<>();

			// grant roles to user
				String role=user.getRole();
				grantedAuthorities.add(new SimpleGrantedAuthority(role));
			

			return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
					user.isEnabled(), accountNonExpired, credentialsNonExpired, accountNonLocked, grantedAuthorities);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
