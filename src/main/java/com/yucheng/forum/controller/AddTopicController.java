package com.yucheng.forum.controller;

import java.util.Date;
import java.util.List;
import java.util.Objects;

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
import com.yucheng.forum.dao.TopicDao;
import com.yucheng.forum.dao.UserDao;
import com.yucheng.forum.model.Category;
import com.yucheng.forum.model.Topic;
import com.yucheng.forum.model.User;
import com.yucheng.forum.util.HostHolder;

/**
 * 添加话题的接口
 * 
 * @author xiezhiping
 *
 */
@Controller
public class AddTopicController {
	@Autowired
	private UserDao userDao;

	@Autowired
	private TopicDao topicDao;

	@Autowired
	private AnswerDao answerDao;
	
	@Autowired
	private MessageDao messageDao;
	
	@Autowired
	private CategoryDao categoryDao;
	
	@Autowired
	private HostHolder hostHolder;

	@RequestMapping(path = "/addTopic", method = RequestMethod.GET)
	public String displayMyProfile(Model model) {
		User user = hostHolder.getUser();
		Long points = userDao.getPoints(user.getId());

		Long numberOfTopics = topicDao.countTopicsByUser_Id(user.getId());
		Long numberOfAnswers = answerDao.countAnswersByUser_Id(user.getId());
		Long numberOfHelped = answerDao.countAnswersByUser_IdAndUseful(user.getId(), true);

		List<Category> categorys = categoryDao.findAll();
		model.addAttribute("user", user);
		model.addAttribute("newMessage", messageDao.countMessageByToId(user.getId()));
		model.addAttribute("points", points);
		model.addAttribute("numberOfTopics", numberOfTopics);
		model.addAttribute("numberOfAnswers", numberOfAnswers);
		model.addAttribute("numberOfHelped", numberOfHelped);
		model.addAttribute("categorys", categorys);
		return "addTopic";
	}

	@RequestMapping(path = "/addTopic/{id}", method = RequestMethod.GET)
	public String displayProfileById(@PathVariable Long id, Model model) {
		User user = userDao.getUserById(id);
		Long points = userDao.getPoints(user.getId());

		Long numberOfTopics = topicDao.countTopicsByUser_Id(id);
		Long numberOfAnswers = answerDao.countAnswersByUser_Id(id);
		Long numberOfHelped = answerDao.countAnswersByUser_IdAndUseful(id, true);

		model.addAttribute("user", user);
		model.addAttribute("newMessage", messageDao.countMessageByToId(user.getId()));
		model.addAttribute("points", points);
		model.addAttribute("numberOfTopics", numberOfTopics);
		model.addAttribute("numberOfAnswers", numberOfAnswers);
		model.addAttribute("numberOfHelped", numberOfHelped);

		return "addTopic";
	}

	@RequestMapping(path = "/addTopic", method = RequestMethod.POST)
	public View addTask(@RequestParam("category") String category, @RequestParam("title") String title,
			@RequestParam("content") String content, @RequestParam("code") String code,
			@RequestParam("id_user") String id_user, HttpServletRequest request) {
		Topic topic = new Topic();
		topic.setCategoryId(Long.parseLong(category));
		if (Objects.equals(code, "")) {
			topic.setCode(null);
		} else {
			topic.setCode(code);
		}
		topic.setContent(content);
		topic.setTitle(title);
		topic.setCreatedDate(new Date());
		topic.setIdUser(Integer.parseInt(id_user));
		topic.setUser(userDao.getUserById(Long.parseLong(id_user)));

		topicDao.addTopic(topic);
		String contextPath = request.getContextPath();
		return new RedirectView(contextPath + "/topics/"+category+"/1");
	}
	
}
