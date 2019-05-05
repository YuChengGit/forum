package com.yucheng.forum.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

import com.yucheng.forum.model.PageBean;
import com.yucheng.forum.model.Topic;
import com.yucheng.forum.service.PageService;
import com.yucheng.forum.util.ForumUtil;

@Controller
public class PageHelperController {
	
	@Autowired
	public PageService pageService;
	
//	@RequestMapping(path="/topics/{category}/{currentPage}",method=RequestMethod.GET)
//	@ResponseBody
	public String pageHelper(@PathVariable String category, @PathVariable int currentPage) {
		PageBean<Topic> pageTopic=pageService.findItemByPage(category, currentPage, 5);
		List<Topic> topics=pageTopic.getItems();
		return ForumUtil.getJSONString(1, topics);
	}
}
