package com.yucheng.forum.controller;

import java.util.List;


import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.yucheng.forum.dao.CategoryDao;
import com.yucheng.forum.dao.MessageDao;
import com.yucheng.forum.dao.UserDao;
import com.yucheng.forum.model.Category;
import com.yucheng.forum.model.User;
import com.yucheng.forum.util.HostHolder;

@Controller
public class AdminModulesController {
	@Autowired
	private UserDao userDao;

	@Autowired
	private HostHolder localHost;
	
	@Autowired
	private HostHolder hostHolder;
	@Autowired
	private CategoryDao categoryDao;
	
	@Autowired
	private MessageDao messageDao;
	/**
	 * 分页处理
	 * @param category
	 * @param currentPage
	 * @param model
	 * @return
	 */
	@RequestMapping(path="/Modules", method=RequestMethod.GET)
	public String displayUserPage(Model model) {
		List<Category> modules = categoryDao.findAll();
		int ModulessTotalNum = categoryDao.countCategory().intValue();
		
		User user=localHost.getUser();
		model.addAttribute("newMessage", messageDao.countMessageByToId(user.getId()));
		model.addAttribute("user", user);
		model.addAttribute("modules",modules);
		model.addAttribute("ModulessTotalNum", ModulessTotalNum);
		model.addAttribute("userDao", userDao);
		return "AdminModules";
	}
	
	/**
	 * 模块修改
	 */
	@RequestMapping(path = "/updateModules",method=RequestMethod.GET)
	public String updateModules(HttpServletRequest request,Model model) {
		String[] parameterValues = request.getParameterValues("name");
		for(int i=1;i<parameterValues.length+1;i++) {
			Category category = new Category();
			category.setId(new Long(i));
			category.setName(parameterValues[i-1]);
			categoryDao.updateCategory(category);
		}
		
		List<Category> modules = categoryDao.findAll();
		int ModulessTotalNum = categoryDao.countCategory().intValue();
		
		User user=localHost.getUser();
		model.addAttribute("user", user);
		model.addAttribute("modules",modules);
		model.addAttribute("ModulessTotalNum", ModulessTotalNum);
		model.addAttribute("userDao", userDao);
		return "AdminModules";
	}
}
