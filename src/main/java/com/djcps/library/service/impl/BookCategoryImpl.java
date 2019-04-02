package com.djcps.library.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.djcps.library.mapper.BookCategoryMapper;
import com.djcps.library.model.BookCategory;
import com.djcps.library.service.BookCategoryService;

@Service("bookcategoryservice")
public class BookCategoryImpl implements BookCategoryService{
	@Resource
	private BookCategoryMapper bookCategoryMapper;
	@Override
	public int insertBookCategory(String bookCategoryName) {
		return bookCategoryMapper.insertBookCategory(bookCategoryName);
	}

	@Override
	public int deleteBookCategory(Integer bookCategoryId) {
		return bookCategoryMapper.deleteBookCategory(bookCategoryId);
	}

	@Override
	public List<BookCategory> selectBookCategory() {
		List<BookCategory> bookCategoryList=bookCategoryMapper.selectBookCategory();
		return bookCategoryList;
	}

	@Override
	public BookCategory selectBookCategoryById(Integer bookCategoryId) {
		return bookCategoryMapper.selectBookCategoryById(bookCategoryId);
	}

	@Override
	public int updateBookCategory(Integer bookCategoryId,String bookCategoryName) {
		return bookCategoryMapper.updateBookCategory(bookCategoryId,bookCategoryName);
	}

}
