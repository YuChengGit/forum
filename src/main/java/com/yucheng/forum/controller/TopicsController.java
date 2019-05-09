package com.yucheng.forum.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import com.yucheng.forum.dao.AnswerDao;
import com.yucheng.forum.dao.CategoryDao;
import com.yucheng.forum.dao.MessageDao;
import com.yucheng.forum.dao.UserDao;
import com.yucheng.forum.model.PageBean;
import com.yucheng.forum.model.Topic;
import com.yucheng.forum.model.User;
import com.yucheng.forum.service.PageService;
import com.yucheng.forum.service.TopicsService;
import com.yucheng.forum.util.HostHolder;

@Controller
public class TopicsController {
	@Autowired
	private UserDao userDao;

	@Autowired
	private AnswerDao answerDao;
	
	@Autowired
	private MessageDao messageDao;
	
	@Autowired
	private HostHolder localHost;
	
	@Autowired
	private TopicsService topicsService;
	
	@Autowired
	private PageService pageService;
	
	@Autowired
	private CategoryDao categoryDao;
	/**
	 * 分页处理
	 * @param category
	 * @param currentPage
	 * @param model
	 * @return
	 */
	@RequestMapping(path="/topics/{category_id}/{currentPage}", method=RequestMethod.GET)
	public String displayTopicPage(@PathVariable String category_id, @PathVariable int currentPage, Model model) {
		PageBean<Topic> pageTopic=pageService.findItemByPage(category_id, currentPage, 5);
		List<Topic> pageList=pageTopic.getItems();
		String header = setHeader(category_id);
		int topicsTotalNum=topicsService.getTopicsByCategory(category_id).size();
		
		User user=localHost.getUser();
		model.addAttribute("user", user);
		model.addAttribute("newMessage", messageDao.countMessageByToId(user.getId()));
		model.addAttribute("topics", pageList);
		model.addAttribute("topicsTotalNum", topicsTotalNum);
		model.addAttribute("header", header);
		model.addAttribute("answerDao", answerDao);
		model.addAttribute("userDao", userDao);
		model.addAttribute("currentPage", pageTopic.getCurrentPage());
		model.addAttribute("totalPage", pageTopic.getTotalPage());
		model.addAttribute("hasNext", pageTopic.getIsMore());
		model.addAttribute("isUserTopicPage", false);
		return "topics";
	}
	
	@RequestMapping(path = "/topics/user/{id}_{currentPage}", method = RequestMethod.GET)
	public String displayTopicsByUser(@PathVariable String id, @PathVariable int currentPage, Model model) {
//		List<Topic> topics = topicsService.getTopicsByUser(id);
		PageBean<Topic> pageTopic=pageService.findItemByUser(id, currentPage, 10);
		List<Topic> topics=pageTopic.getItems();
		int topicsTotalNum=topicsService.getTopicsByUser(id).size();
		String header = "User's topics";
		
		User user=localHost.getUser();
		model.addAttribute("user", user);
		model.addAttribute("newMessage", messageDao.countMessageByToId(user.getId()));
		model.addAttribute("topics", topics);
		model.addAttribute("header", header);
		model.addAttribute("answerDao", answerDao);
		model.addAttribute("userDao", userDao);
		model.addAttribute("currentPage", pageTopic.getCurrentPage());
		model.addAttribute("totalPage", pageTopic.getTotalPage());
		model.addAttribute("hasNext", pageTopic.getIsMore());
		model.addAttribute("topicsTotalNum", topicsTotalNum);
		model.addAttribute("isUserTopicPage", true);
		model.addAttribute("isSearch", false);
		return "topics"; 
	}
	
	@RequestMapping(path="/search/{category_id}/{currentPage}")
	public String search(@PathVariable String category_id,@PathVariable int currentPage,@RequestParam String title,Model model) {
		PageBean<Topic> pageTopic=pageService.findItemByPageSearch(category_id, currentPage, 10,title);
		List<Topic> pageList=pageTopic.getItems();
		String header = setHeader(category_id);
		int topicsTotalNum=topicsService.getTopicsByCategory(category_id).size();
		
		User user=localHost.getUser();
		model.addAttribute("user", user);
		model.addAttribute("newMessage", messageDao.countMessageByToId(user.getId()));
		model.addAttribute("topics", pageList);
		model.addAttribute("topicsTotalNum", topicsTotalNum);
		model.addAttribute("header", header);
		model.addAttribute("answerDao", answerDao);
		model.addAttribute("userDao", userDao);
		model.addAttribute("currentPage", pageTopic.getCurrentPage());
		model.addAttribute("totalPage", pageTopic.getTotalPage());
		model.addAttribute("hasNext", pageTopic.getIsMore());
		model.addAttribute("isUserTopicPage", false);
		model.addAttribute("isSearch", true);
		return "topics";
	}

	private String setHeader(String category_id) {
		if(Long.valueOf(category_id)>=2&&Long.valueOf(category_id)<=7) {
			return categoryDao.getCategory(Long.valueOf(category_id)).getName();
		}else if(Long.valueOf(category_id)==1) {
			return "All topics";
		}else {
			return "User's topics";
		}
	}
	
	/**
	 * 页面跳转bug
	 * @param request
	 * @return
	 */
	@RequestMapping(path = "/topics/user/message", method = RequestMethod.GET)
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
	}
}
