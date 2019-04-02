package com.djcps.library.service.impl;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;

import com.djcps.library.mapper.BookMapper;
import com.djcps.library.mapper.BorrowingBooksMapper;
import com.djcps.library.mapper.UserMapper;
import com.djcps.library.model.Book;
import com.djcps.library.model.BorrowingBooks;
import com.djcps.library.model.vo.BookVo;
import com.djcps.library.model.vo.PageVo;
import com.djcps.library.service.BookService;

/**
 * @author djsxs
 *
 */
@Service("bookservice")
public  class BookServiceImpl implements BookService {

	@Resource
	private BookMapper bookMapper;
	@Resource
	private UserMapper userMapper;
	@Resource
	private BorrowingBooksMapper borrowingBooksMapper;

	

	@Override
	public List<Book> listBook() {
		List<Book> list = bookMapper.listBook();
		return list;
	}

	@Override
	public PageVo selectAllBook(int pageNum) {
		int bookTotalCounts = bookMapper.getBookTotalCounts();
		PageVo pageVo = new PageVo();
		int pageSize = 10;
		int pageIndex = 0;
		int totalPage = 0;
		if (0 == pageNum) {
			pageIndex = 1;
			pageSize = 10;
		} else {
			pageIndex = (pageNum - 1) * pageSize;
			pageSize = pageNum * 10;
		}
		if (bookTotalCounts > 0) {
			totalPage = (bookTotalCounts - 1) / 10 + 1;
		}
		pageVo.setPageIndex(pageIndex);
		pageVo.setTotalPage(totalPage);
		pageVo.setPageSize(pageSize);
		List<Book> list = bookMapper.selectAllByCondition(pageIndex, pageSize);
		pageVo.setBookList(list);
		return pageVo;
	}
	
	@Override
	public PageVo findBookByTheOnsaleDate(int pageNum) {
		int bookTotalCounts = bookMapper.getBookTotalCounts();
		PageVo pageVo = new PageVo();
		int pageSize = 10;
		int pageIndex = 0;
		int totalPage = 0;
		if (0 == pageNum) {
			pageIndex = 1;
			pageSize = 10;
		} else {
			pageIndex = (pageNum - 1) * pageSize;
			pageSize = pageNum * 10;
		}
		if (bookTotalCounts > 0) {
			totalPage = (bookTotalCounts - 1) / 10 + 1;
		}
		pageVo.setPageIndex(pageIndex);
		pageVo.setTotalPage(totalPage);
		pageVo.setPageSize(pageSize);
		List<Book> list = bookMapper.findBookByTheOnsaleDate(pageIndex, pageSize);
		pageVo.setBookList(list);
		return pageVo;
	}
	
	@Override
	public List<Book> findBookByOnRecently() {
		Date date = null;
		try {
			date = DateUtils.parseDate(DateFormatUtils.format(new Date(), "yyyy-MM-dd"), "yyyy-MM-dd");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return bookMapper.getBookOnRecently(date);
	}

	@Override
	public PageVo findHotBook(int pageNum) {
		int bookTotalCounts = bookMapper.getBookTotalCounts();
		PageVo pageVo = new PageVo();
		int pageSize = 10;
		int pageIndex = 0;
		int totalPage = 0;
		if (0 == pageNum) {
			pageIndex = 1;
			pageSize = 10;
		} else {
			pageIndex = (pageNum - 1) * pageSize;
			pageSize = pageNum * 10;
		}
		if (bookTotalCounts > 0) {
			totalPage = (bookTotalCounts - 1) / 10 + 1;
		}
		pageVo.setPageIndex(pageIndex);
		pageVo.setTotalPage(totalPage);
		pageVo.setPageSize(pageSize);
		List<Book> list = bookMapper.findHotBook(pageIndex, pageSize);
		pageVo.setBookList(list);
		return pageVo;
	}

	@Override
	public PageVo getBookListBybookCondition(Book bookCondition, int pageNum) {
		int bookTotalCounts = bookMapper.getBookTotalCountsBybookCondition(bookCondition);
		PageVo pageVo = new PageVo();
		int pageSize = 10;
		int pageIndex = 0;
		int totalPage = 0;
		if (0 == pageNum) {
			pageIndex = 1;
			pageSize = 10;
		} else {
			pageIndex = (pageNum - 1) * pageSize;
			pageSize = pageNum * 10;
		}
		if (bookTotalCounts > 0) {
			totalPage = (bookTotalCounts - 1) / 10 + 1;
		}
		pageVo.setPageIndex(pageIndex);
		pageVo.setTotalPage(totalPage);
		pageVo.setPageSize(pageSize);
		List<Book> list = bookMapper.getBookListBybookCondition(bookCondition, pageIndex, pageSize);
		pageVo.setBookList(list);
		return pageVo;
	}
	
	@Override
	public Book findBookByBarCode(String barCode) {
		return bookMapper.findBookByBarCode(barCode);
	}
	
	@Override
	public BookVo getBorrowedMsg(Integer bookId, Integer isreturn) {
		BorrowingBooks borrowingBooks = borrowingBooksMapper.getBorrowMsgBybookId(Integer.valueOf(bookId), isreturn);
		Book book = bookMapper.getBookMsgByid(Integer.valueOf(bookId));
		// 创建一个工具类封装数据
		BookVo bookVo = new BookVo();
		// 封装书籍的id
		bookVo.setBookId(Integer.valueOf(bookId));
		// 封装书籍的名称
		bookVo.setBookName(book.getBookName());
		bookVo.setBookAuthor(book.getBookAuthor());
		bookVo.setBookPublish(book.getBookPublish());
		bookVo.setBookPrice(book.getBookPrice());
		bookVo.setBookIntroduction(book.getBookIntroduction());
		bookVo.setBookImage(book.getBookImage());
		bookVo.setBookDate(book.getBookDate());
		bookVo.setIsborrowedout(book.getIsborrowedout());
		bookVo.setDateCount(book.getDateCount());
		bookVo.setBorrowCount(book.getBorrowCount());
		if (book.getIsborrowedout() == 1 ||book.getIsborrowedout() == 2) {
			// 封装书籍的借书起始日期
			bookVo.setBeginDate(borrowingBooks.getDate());
			Date today = null;
			Date enddate = borrowingBooks.getLastdate();
			try {
				today = DateUtils.parseDate(DateFormatUtils.format(new Date(), "yyyy-MM-dd"), "yyyy-MM-dd");
			} catch (ParseException e) {
				e.printStackTrace();
			}
			// *计算剩余天数*/
			long remainDayCount = (enddate.getTime() - today.getTime()) / (24 * 60 * 60 * 1000);
			bookVo.setRemainDayCount(remainDayCount);
			bookVo.setUser(userMapper.findUserById(borrowingBooks.getUserId()));
		} else {
			// 封装书籍的借书起始日期
			bookVo.setBeginDate(null);
			bookVo.setRemainDayCount(0L);
			bookVo.setUser(null);
		}
		return bookVo;
	}

	@Override
	public BookVo getBorrowedMsgAndroid(String barCode, Integer isreturn) {
		Book book = bookMapper.findBookByBarCode(barCode);
		BorrowingBooks borrowingBooks = borrowingBooksMapper.getBorrowMsgBybookId(book.getBookId(), isreturn);
		// 创建一个工具类封装数据
		BookVo bookVo = new BookVo();
		// 封装书籍的id
		bookVo.setBookId(book.getBookId());
		// 封装书籍的名称
		bookVo.setBookName(book.getBookName());
		bookVo.setBookAuthor(book.getBookAuthor());
		bookVo.setBookPublish(book.getBookPublish());
		bookVo.setBookPrice(book.getBookPrice());
		bookVo.setBookIntroduction(book.getBookIntroduction());
		bookVo.setBookImage(book.getBookImage());
		bookVo.setBookDate(book.getBookDate());
		bookVo.setIsborrowedout(book.getIsborrowedout());
		bookVo.setDateCount(book.getDateCount());
		bookVo.setBorrowCount(book.getBorrowCount());
		if (book.getIsborrowedout() == 1 ||book.getIsborrowedout() == 2) {
			// 封装书籍的借书起始日期
			bookVo.setBeginDate(borrowingBooks.getDate());
			Date today = null;
			Date enddate = borrowingBooks.getLastdate();
			try {
				today = DateUtils.parseDate(DateFormatUtils.format(new Date(), "yyyy-MM-dd"), "yyyy-MM-dd");
			} catch (ParseException e) {
				e.printStackTrace();
			}
			// *计算剩余天数*/
			long remainDayCount = (enddate.getTime() - today.getTime()) / (24 * 60 * 60 * 1000);
			bookVo.setRemainDayCount(remainDayCount);
			bookVo.setUser(userMapper.findUserById(borrowingBooks.getUserId()));
		} else {
			// 封装书籍的借书起始日期
			bookVo.setBeginDate(null);
			bookVo.setRemainDayCount(0L);
			bookVo.setUser(null);
		}
		return bookVo;
	}
}
