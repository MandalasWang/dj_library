package com.djcps.library.model.vo;

import java.util.Date;

import com.djcps.library.model.User;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author djsxs
 *
 */
public class BookVo {
	private Integer bookId;

    private String bookName;

    private String bookAuthor;

    private String bookPublish;

    private Double bookPrice;

    private String bookIntroduction;
    
    private String bookImage;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date bookDate;
    
    private Integer isborrowedout;
    /**可借天数*/
    private Integer dateCount;
    /**续借次数*/
    private Integer borrowCount;
    /**起始日期*/
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date beginDate;
    /**剩余天数*/
    private Long  remainDayCount;
    /**借书用户信息*/
    private User user;
	public Integer getBookId() {
		return bookId;
	}
	public void setBookId(Integer bookId) {
		this.bookId = bookId;
	}
	public String getBookName() {
		return bookName;
	}
	public void setBookName(String bookName) {
		this.bookName = bookName;
	}
	public String getBookAuthor() {
		return bookAuthor;
	}
	public void setBookAuthor(String bookAuthor) {
		this.bookAuthor = bookAuthor;
	}
	public String getBookPublish() {
		return bookPublish;
	}
	public void setBookPublish(String bookPublish) {
		this.bookPublish = bookPublish;
	}
	public Double getBookPrice() {
		return bookPrice;
	}
	public void setBookPrice(Double bookPrice) {
		this.bookPrice = bookPrice;
	}
	public String getBookIntroduction() {
		return bookIntroduction;
	}
	public void setBookIntroduction(String bookIntroduction) {
		this.bookIntroduction = bookIntroduction;
	}
	public String getBookImage() {
		return bookImage;
	}
	public void setBookImage(String bookImage) {
		this.bookImage = bookImage;
	}
	public Date getBookDate() {
		return bookDate;
	}
	public void setBookDate(Date bookDate) {
		this.bookDate = bookDate;
	}
	public Integer getIsborrowedout() {
		return isborrowedout;
	}
	public void setIsborrowedout(Integer isborrowedout) {
		this.isborrowedout = isborrowedout;
	}
	public Integer getDateCount() {
		return dateCount;
	}
	public void setDateCount(Integer dateCount) {
		this.dateCount = dateCount;
	}
	public Integer getBorrowCount() {
		return borrowCount;
	}
	public void setBorrowCount(Integer borrowCount) {
		this.borrowCount = borrowCount;
	}
	public Date getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	public Long getRemainDayCount() {
		return remainDayCount;
	}
	public void setRemainDayCount(Long remainDayCount) {
		this.remainDayCount = remainDayCount;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public BookVo(Integer bookId, String bookName, String bookAuthor, String bookPublish, Double bookPrice,
			String bookIntroduction, String bookImage, Date bookDate, Integer isborrowedout, Integer dateCount,
			Integer borrowCount, Date beginDate, Long remainDayCount, User user) {
		super();
		this.bookId = bookId;
		this.bookName = bookName;
		this.bookAuthor = bookAuthor;
		this.bookPublish = bookPublish;
		this.bookPrice = bookPrice;
		this.bookIntroduction = bookIntroduction;
		this.bookImage = bookImage;
		this.bookDate = bookDate;
		this.isborrowedout = isborrowedout;
		this.dateCount = dateCount;
		this.borrowCount = borrowCount;
		this.beginDate = beginDate;
		this.remainDayCount = remainDayCount;
		this.user = user;
	}
	public BookVo() {
		super();
	}
	
	
	
    
}
