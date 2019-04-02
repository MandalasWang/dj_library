package com.djcps.library.model.vo;





/**
 * @author djsxs
 *
 */
public class BorrowingBooksVo {
	private Integer borrowBookId;
	private Integer bookId;
	private String bookName;
	private Double bookPrice;
	private Integer isborrowedout;
	private String desc;
	/** 可借天数 */
	private Integer dateCount;
	/** 已借日期 */
	private Long borrowedDays;
	public Integer getBorrowBookId() {
		return borrowBookId;
	}
	public void setBorrowBookId(Integer borrowBookId) {
		this.borrowBookId = borrowBookId;
	}
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
	public Double getBookPrice() {
		return bookPrice;
	}
	public void setBookPrice(Double bookPrice) {
		this.bookPrice = bookPrice;
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
	public Long getBorrowedDays() {
		return borrowedDays;
	}
	public void setBorrowedDays(Long borrowedDays) {
		this.borrowedDays = borrowedDays;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	
}
