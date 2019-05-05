package com.yucheng.forum.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yucheng.forum.dao.MessageDao;
import com.yucheng.forum.model.User;
import com.yucheng.forum.util.HostHolder;

@Controller
public class DashboardController {
	private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);
	
	@Autowired
	private MessageDao messageDao;
	
	@Autowired
	private HostHolder hostHolder;
	@RequestMapping("/dashboard")
	public String dashboard(Model model) {
		User user = hostHolder.getUser();
		model.addAttribute("newMessage", messageDao.countMessageByToId(user.getId()));
		model.addAttribute("user", user);
		return "dashboard";
	}
}
