package com.yucheng.forum.dao;

import java.util.List;


import org.apache.ibatis.annotations.Mapper;

import com.yucheng.forum.model.Category;

@Mapper
public interface CategoryDao {
	String TABLE_NAME = "category";
	String INSERT_FIELDS = "name";
	String SELECT_FIELDS = "id,name";
	
	/**
	 * 获得所有模块
	 * @return List<Category>
	 */
	List<Category> findAll();
	
	/**
	 * 修改模块名
	 */
	void updateCategory(Category category);
	
	/**
	 * 根据id查找模块信息
	 */
	Category getCategory(Long id);
	/**
	 * 获取分类数量
	 */
	Long countCategory();
}
