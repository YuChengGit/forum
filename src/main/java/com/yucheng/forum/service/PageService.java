package com.yucheng.forum.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.yucheng.forum.dao.TopicDao;
import com.yucheng.forum.dao.UserDao;
import com.yucheng.forum.model.Category;
import com.yucheng.forum.model.PageBean;
import com.yucheng.forum.model.Topic;
import com.yucheng.forum.model.User;

@Service
public class PageService {
	
	@Autowired
	private TopicDao topicDao;
	@Autowired
	private UserDao userDao;
	
	public PageBean<Topic> findItemByPage(String category_id, int currentPage, int pageSize) {
		int countNums = 0; // 总记录数
		if(Long.valueOf(category_id) == 1) {
			countNums=topicDao.findAll().size();
		}else{
			countNums = topicDao.findTopicsByCategoryIdOrderByCreatedDateDesc(Long.valueOf(category_id)).size(); // 全部商品
		}
		// 设置分页信息，分别是当前页数和每页显示的总记录数【记住：必须在mapper接口中的方法执行之前设置该分页信息】
		PageHelper.startPage(currentPage, pageSize);
		List<Topic> allTopic=null;
		if(Long.valueOf(category_id) == 1) {
			allTopic=topicDao.findAll();
		}else{
			allTopic = topicDao.findTopicsByCategoryIdOrderByCreatedDateDesc(Long.valueOf(category_id)); // 全部商品
		}
		PageBean<Topic> pageData = new PageBean<>(currentPage, pageSize, countNums);
		pageData.setItems(allTopic);
//		pageData.setCurrentPage(currentPage);
//		pageData.setPageSize(pageSize);
//		pageData.setTotalPage(countNums/pageSize);
//		pageData.setIsMore(currentPage<(countNums/pageSize)?true:false);
		return pageData;
	}
	
	public PageBean<Topic> findItemByPageSearch(String category_id, int currentPage, int pageSize,String title) {
		int countNums = 0; // 总记录数
		Topic topic = new Topic();
		topic.setCategoryId(Long.valueOf(category_id));
		topic.setTitle(title);
		if(Long.valueOf(category_id) == 1) {
			countNums=topicDao.findAllBySearch(topic).size();
		}else{
			countNums = topicDao.findTopicsByCategoryIdOrderByCreatedDateDescSearch(topic).size(); // 全部商品
		}
		// 设置分页信息，分别是当前页数和每页显示的总记录数【记住：必须在mapper接口中的方法执行之前设置该分页信息】
		PageHelper.startPage(currentPage, pageSize);
		List<Topic> allTopic=null;
		if(Long.valueOf(category_id) == 1) {
			allTopic=topicDao.findAllBySearch(topic);
		}else{
			allTopic = topicDao.findTopicsByCategoryIdOrderByCreatedDateDescSearch(topic); // 全部商品
		}
		PageBean<Topic> pageData = new PageBean<>(currentPage, pageSize, countNums);
		pageData.setItems(allTopic);
//		pageData.setCurrentPage(currentPage);
//		pageData.setPageSize(pageSize);
//		pageData.setTotalPage(countNums/pageSize);
//		pageData.setIsMore(currentPage<(countNums/pageSize)?true:false);
		return pageData;
	}
	
	public PageBean<User> findUsersByPage(int currentPage, int pageSize) {
		int countNums = 0; // 总记录数
		Long countUsers = userDao.countUsers();
		countNums =countUsers.intValue();
		// 设置分页信息，分别是当前页数和每页显示的总记录数【记住：必须在mapper接口中的方法执行之前设置该分页信息】
		PageHelper.startPage(currentPage, pageSize);
		List<User> allUsers=null;
		allUsers=userDao.getAllUser();
		PageBean<User> pageData = new PageBean<>(currentPage, pageSize, countNums);
		pageData.setItems(allUsers);
//		pageData.setCurrentPage(currentPage);
//		pageData.setPageSize(pageSize);
//		pageData.setTotalPage(countNums/pageSize);
//		pageData.setIsMore(currentPage<(countNums/pageSize)?true:false);
		return pageData;
	}
	
	
	public PageBean<Topic> findItemByUser(String userId, int currentPage, int pageSize) {
		int countNums = topicDao.findTopicsByUser_IdOrderByCreatedDateDesc(Long.valueOf(userId)).size(); // 总记录数
		// 设置分页信息，分别是当前页数和每页显示的总记录数【记住：必须在mapper接口中的方法执行之前设置该分页信息】
		PageHelper.startPage(currentPage, pageSize);
		List<Topic> allTopic=topicDao.findTopicsByUser_IdOrderByCreatedDateDesc(Long.valueOf(userId));
		PageBean<Topic> pageData = new PageBean<>(currentPage, pageSize, countNums);
		pageData.setItems(allTopic);
		return pageData;
	}
}
