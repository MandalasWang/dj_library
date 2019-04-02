package com.djcps.library.service;

import java.util.List;

import com.djcps.library.model.BookCategory;



public interface BookCategoryService {
	int insertBookCategory(String bookCategoryName);
	int deleteBookCategory(Integer bookCategoryId);
	List<BookCategory> selectBookCategory();
	BookCategory selectBookCategoryById(Integer bookCategoryId);
	int updateBookCategory(Integer bookCategoryId,String bookCategoryName);
}
