package com.xzp.forum.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xzp.forum.dao.CategoryDao;
import com.xzp.forum.model.Category;
import com.xzp.forum.model.User;
import com.xzp.forum.util.HostHolder;

@Controller
public class CategoryController {
	@Autowired
	private CategoryDao categoryDao;
	@Autowired
	HostHolder hostHolder;
/*	@RequestMapping(path = "/category", method = RequestMethod.GET)
	public String updateCategory(@PathVariable String id, Model model) {
		List<Category> categorys = categoryDao.findAll();
		model.addAttribute("categorys",categorys);
		return "online";
	}*/
	
	@RequestMapping(path = "/category", method = RequestMethod.GET)
	@ResponseBody
	public List<Category> getAllCategory(Model model) {
		List<Category> categorys = categoryDao.findAll();
		return categorys;
	}
}
