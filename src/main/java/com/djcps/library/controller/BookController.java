package com.djcps.library.controller;


import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.djcps.library.common.RetResponse;
import com.djcps.library.common.RetResult;
import com.djcps.library.model.Book;
import com.djcps.library.model.vo.BookVo;
import com.djcps.library.model.vo.PageVo;
import com.djcps.library.service.BookService;

/**
 * @author djsxs
 *
 */
@RestController
@RequestMapping(value = "/book")
public class BookController {
	@Autowired
	@Qualifier("bookservice")
	private BookService bookService;

	/**
	 * 返回所有书籍页面
	 * 
	 * @return
	 */
	@RequestMapping("/allBook")
	public RetResult<List<Book>> allBook() {
		List<Book> list = bookService.listBook();
		if (list == null) {
			return RetResponse.makeRsp(400, "查询失败");
		}
		return RetResponse.makeOKRsp(list);
	}

	/**
	 * 按页分查询书籍列表
	 * 
	 * @param pageNum
	 * @return
	 */
	@RequestMapping("/listBook")
	public RetResult<PageVo> listBookByPageNum(@RequestParam("pageNum") int pageNum) {
		PageVo pageVo = bookService.selectAllBook(pageNum);
		if (pageVo == null) {
			return RetResponse.makeRsp(400, "查询书籍列表失败！");
		} else {
			return RetResponse.makeOKRsp(pageVo);
		}
	}

	/**
	 * 按照书籍上架时间查询
	 * 
	 * @return
	 */
	@RequestMapping("/findBookByOnsaleDate")
	public RetResult<PageVo> findBookByTheOnSaleDate(@RequestParam("pageNum") int pageNum) {
		PageVo pageVo = bookService.findBookByTheOnsaleDate(pageNum);
		if (pageVo == null) {
			return RetResponse.makeRsp(400, "查询书籍上架时间失败！");
		} else {
			return RetResponse.makeOKRsp(pageVo);
		}
	}

	@RequestMapping("/findBookByOnRecently")
	public RetResult<List<Book>> findBookByOnRecently() {
		List<Book> list = bookService.findBookByOnRecently();
		if (list == null) {
			return RetResponse.makeRsp(400, "查询近期上架书籍失败！");
		} else {
			return RetResponse.makeOKRsp(list);
		}
	}

	@RequestMapping("/findHotBook")
	public RetResult<PageVo> findHotBook(@RequestParam("pageNum") int pageNum) {
		PageVo pageVo = bookService.findHotBook(pageNum);
		if (pageVo == null) {
			return RetResponse.makeRsp(400, "查询热门书籍失败！");
		} else {
			return RetResponse.makeOKRsp(pageVo);
		}
	}

	@RequestMapping("/getBookListBybookCondition")
	public RetResult<PageVo> getBookListBybookCondition(
			@RequestParam(value = "bookName", required = false) String bookName,
			@RequestParam(value = "bookDate", required = false) String bookDate,
			@RequestParam(value = "isborrowedout", required = false) Integer isborrowedout,
			@RequestParam("pageNum") int pageNum) {
		Date newbookDate = null;
		if (!"".equals(bookDate)) {
			try {
				newbookDate = DateUtils.parseDate(bookDate, "yyyy-MM-dd");
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else {
			newbookDate = null;
		}
		Book bookCondition = compactBookConditionToSearch(bookName, newbookDate, isborrowedout);
		PageVo pageVo = bookService.getBookListBybookCondition(bookCondition, Integer.valueOf(pageNum));
		if (pageVo == null) {
			return RetResponse.makeRsp(400, "条件查询失败！");
		} else {
			return RetResponse.makeOKRsp(pageVo);
		}
	}

	private Book compactBookConditionToSearch(String bookName, Date newbookDate, Integer isborrowedout) {
		Book bookCondition = new Book();
		if (bookName != null) {
			bookCondition.setBookName(bookName);
		}
		if (newbookDate != null) {
			bookCondition.setBookDate(newbookDate);
		}
		if (isborrowedout != null) {
			bookCondition.setIsborrowedout(isborrowedout);
		}
		return bookCondition;
	}
	/**
	 * 根据书籍的id查询该书籍的详细借阅信息
	 * 
	 * @param bookId
	 * @return
	 */
	@RequestMapping("/getBorrowedMsgBybookId")
	public RetResult<BookVo> getBorrowedMsgBybookId(@RequestParam("bookId") Integer bookId) {
		Integer isreturn = 0;
		BookVo bookVo = bookService.getBorrowedMsg(bookId, isreturn);
		if (bookVo == null) {
			return RetResponse.makeRsp(400, "书籍信息返回失败！");
		} else {
			return RetResponse.makeOKRsp(bookVo);
		}
	}

}
