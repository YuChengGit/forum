package com.yucheng.forum.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import com.yucheng.forum.dao.CategoryDao;
import com.yucheng.forum.dao.MessageDao;
import com.yucheng.forum.dao.UserDao;
import com.yucheng.forum.model.Category;
import com.yucheng.forum.model.PageBean;
import com.yucheng.forum.model.User;
import com.yucheng.forum.service.PageService;
import com.yucheng.forum.util.HostHolder;

@Controller
public class AdminUsersController {
	@Autowired
	private UserDao userDao;

	@Autowired
	private HostHolder localHost;

	@Autowired
	private HostHolder hostHolder;
	@Autowired
	private CategoryDao categoryDao;
	@Autowired
	private PageService pageService;
	@Autowired
	private MessageDao messageDao;
	/**
	 * 分页处理
	 * @param category
	 * @param currentPage
	 * @param model
	 * @return
	 */
	@RequestMapping(path="/Users/{currentPage}", method=RequestMethod.GET)
	public String displayUserPage(@PathVariable int currentPage, Model model) {
		List<Category> modules = categoryDao.findAll();
		PageBean<User> pageUser = pageService.findUsersByPage(currentPage, 5);
		int UsersTotalNum = userDao.countUsers().intValue();
		List<User> users = userDao.getAllUser();
		
		User user=localHost.getUser();
		model.addAttribute("newMessage", messageDao.countMessageByToId(user.getId()));
		model.addAttribute("user", user);
		model.addAttribute("users", users);
		model.addAttribute("modules",modules);
		model.addAttribute("UsersTotalNum", UsersTotalNum);
		model.addAttribute("userDao", userDao);
		model.addAttribute("currentPage", pageUser.getCurrentPage());
		model.addAttribute("totalPage", pageUser.getTotalPage());
		model.addAttribute("hasNext", pageUser.getIsMore());
		return "AdminUsers";
	}
	/**
	 * 删除一个用户
	 * @param id
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("/deleteUserById/{id}")
	public View deleteTopic(@PathVariable Long id,Model model,HttpServletRequest request) {
		User user = hostHolder.getUser();
		model.addAttribute(user);
		userDao.deleteUserById(id);
		String contextPath = request.getContextPath();
		return new RedirectView(contextPath + "/Users/1");
	}
	
	
	/**
	 * 页面跳转bug
	 * @param request
	 * @return
	 */
/*	@RequestMapping(path = "/topics/user/message", method = RequestMethod.GET)
	public View topicTransformUser(HttpServletRequest request) {
		String contextPath = request.getContextPath();
		return new RedirectView(contextPath + "/message");
	}
	
	@RequestMapping(path = "/topics/other/message", method = RequestMethod.GET)
	public View topicTransformOther(HttpServletRequest request) {
		String contextPath = request.getContextPath();
		return new RedirectView(contextPath + "/message");
	}
	
	@RequestMapping(path = "/topics/web/message", method = RequestMethod.GET)
	public View topicTransformWeb(HttpServletRequest request) {
		String contextPath = request.getContextPath();
		return new RedirectView(contextPath + "/message");
	}*/
}
