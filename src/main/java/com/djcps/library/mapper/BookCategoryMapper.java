package com.djcps.library.mapper;

import java.util.List;

import com.djcps.library.model.BookCategory;

import io.lettuce.core.dynamic.annotation.Param;

public interface BookCategoryMapper {
	int insertBookCategory(@Param("bookCategoryName")String bookCategoryName);
	int deleteBookCategory(@Param("bookCategoryId")Integer bookCategoryId);
	List<BookCategory> selectBookCategory();
	BookCategory selectBookCategoryById(@Param("bookCategoryId")Integer bookCategoryId);
	int updateBookCategory(@Param("bookCategoryId")Integer bookCategoryId,@Param("bookCategoryName")String bookCategoryName);
}
