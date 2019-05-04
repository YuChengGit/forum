package com.xzp.forum.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import com.xzp.forum.dao.MessageDao;
import com.xzp.forum.model.User;
import com.xzp.forum.util.HostHolder;

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
