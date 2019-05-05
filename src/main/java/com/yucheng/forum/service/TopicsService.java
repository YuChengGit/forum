package com.yucheng.forum.service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yucheng.forum.async.EventModel;
import com.yucheng.forum.async.EventProducer;
import com.yucheng.forum.async.EventType;
import com.yucheng.forum.dao.AnswerDao;
import com.yucheng.forum.dao.TopicDao;
import com.yucheng.forum.dao.UserDao;
import com.yucheng.forum.model.Answer;
import com.yucheng.forum.model.Topic;
import com.yucheng.forum.model.User;
import com.yucheng.forum.util.EntityType;
import com.yucheng.forum.util.HostHolder;

@Service
public class TopicsService {

	@Autowired
	private TopicDao topicDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private AnswerDao answerDao;
	
	@Autowired
	private EventProducer eventProducer;
	
	@Autowired
	HostHolder hostHolder;

	public List<Topic> getTopicsByCategory(String category_id) {
		if (Long.valueOf(category_id)==1) {
			return topicDao.findAll();
		} else {
			return topicDao.findTopicsByCategoryIdOrderByCreatedDateDesc(Long.valueOf(category_id));
		}
	}

	public List<Topic> getTopicsByUser(String userId) {
		return topicDao.findTopicsByUser_IdOrderByCreatedDateDesc(Long.valueOf(userId));
	}
	
	public void setUseful(Boolean setStatus,Long answerId) {
		answerDao.setUsefulForAnswer(setStatus,answerId);
		//触发点赞的异步队列
		User user = hostHolder.getUser();
			EventModel eventModel = new EventModel(EventType.USEFUL);
			eventModel.setCreatedDate(new Date());
			eventModel.setActorId(Integer.parseInt(String.valueOf(user.getId())));
			eventModel.setEntityId(answerId.intValue());
			eventModel.setEntityType(EntityType.ENTITY_USEFUL);
			eventModel.setEntityOwnerId(answerDao.getIdUserById(answerId).intValue());
			eventProducer.fireEvent(eventModel);
	}

	public void addAnswer(String content, String code, String id_topic, String id_user) {
		Answer answer = new Answer();
		answer.setContent(content);
		if (Objects.equals(code, "")) {
			answer.setCode(null);
		} else {
			answer.setCode(code);
		}
		answer.setCreatedDate(new Date());
		answer.setUseful(false);
		answer.setTopic(topicDao.findTopicById(Long.valueOf(id_topic)));
		answer.setUser(userDao.getUserById(Long.parseLong(id_user)));
		answer.setIdTopic(Integer.parseInt(id_topic));
		answer.setIdUser(Integer.parseInt(id_user));
		answerDao.addAnswer(answer);

		// 触发评论的异步队列
		User user = hostHolder.getUser();
		// 如果评论自己的话题不会触发站内信通知
		if (user.getId() != topicDao.getId_userById(Long.parseLong(id_topic))) {
			EventModel eventModel = new EventModel(EventType.COMMENT);
			eventModel.setCreatedDate(new Date());
			eventModel.setActorId(Integer.parseInt(String.valueOf(user.getId())));
			eventModel.setEntityId(Integer.valueOf(id_topic));
			eventModel.setEntityType(EntityType.ENTITY_COMMENT);
			eventModel.setEntityOwnerId(topicDao.getId_userById(Long.parseLong(id_topic)));
			eventProducer.fireEvent(eventModel);
		}
	}
}
