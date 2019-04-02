package com.djcps.library.model.vo;

import java.util.List;

import com.djcps.library.model.Book;
import com.djcps.library.model.BorrowingBooks;
import com.djcps.library.model.User;

/**
 * @author djsxs
 *
 */
public class PageVo {
	/**封装 前端传来的第几页*/
	private Integer pageIndex; 
	/**每一页有几个记录*/
	private Integer pageSize; 
	/**总共几页*/
	private Integer totalPage; 
	/**书籍集合*/
	private List<Book> bookList; 
	
	private List<BookVo> bookVoList; 
	/**借书记录集合*/
	private List<BorrowingBooks> borrowingBookslist; 
	
	private List<User> userList;
	/**借书记录的封装类集合*/
	private List<BorrowingBooksVo> borrowingBooksVoList;
	public Integer getPageIndex() {
		return pageIndex;
	}
	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = pageIndex;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Integer getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}
	public List<Book> getBookList() {
		return bookList;
	}
	public void setBookList(List<Book> bookList) {
		this.bookList = bookList;
	}
	public List<BookVo> getBookVoList() {
		return bookVoList;
	}
	public void setBookVoList(List<BookVo> bookVoList) {
		this.bookVoList = bookVoList;
	}
	public List<BorrowingBooks> getBorrowingBookslist() {
		return borrowingBookslist;
	}
	public void setBorrowingBookslist(List<BorrowingBooks> borrowingBookslist) {
		this.borrowingBookslist = borrowingBookslist;
	}
	public List<User> getUserList() {
		return userList;
	}
	public void setUserList(List<User> userList) {
		this.userList = userList;
	}
	public List<BorrowingBooksVo> getBorrowingBooksVoList() {
		return borrowingBooksVoList;
	}
	public void setBorrowingBooksVoList(List<BorrowingBooksVo> borrowingBooksVoList) {
		this.borrowingBooksVoList = borrowingBooksVoList;
	}
	public PageVo(Integer pageIndex, Integer pageSize, Integer totalPage, List<Book> bookList, List<BookVo> bookVoList,
			List<BorrowingBooks> borrowingBookslist, List<User> userList, List<BorrowingBooksVo> borrowingBooksVoList) {
		super();
		this.pageIndex = pageIndex;
		this.pageSize = pageSize;
		this.totalPage = totalPage;
		this.bookList = bookList;
		this.bookVoList = bookVoList;
		this.borrowingBookslist = borrowingBookslist;
		this.userList = userList;
		this.borrowingBooksVoList = borrowingBooksVoList;
	}
	public PageVo() {
		super();
	}
	@Override
	public String toString() {
		return "PageVo [pageIndex=" + pageIndex + ", pageSize=" + pageSize + ", totalPage=" + totalPage + ", bookList="
				+ bookList + ", bookVoList=" + bookVoList + ", borrowingBookslist=" + borrowingBookslist + ", userList="
				+ userList + ", borrowingBooksVoList=" + borrowingBooksVoList + "]";
	}
}
