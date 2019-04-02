package com.djcps.library.service.impl;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.djcps.library.common.BarCodeUtil;
import com.djcps.library.common.FileUpLoadUtil;
import com.djcps.library.common.RandomUtil;
import com.djcps.library.mapper.AdminMapper;
import com.djcps.library.mapper.BookMapper;
import com.djcps.library.mapper.BorrowingBooksMapper;
import com.djcps.library.mapper.UserMapper;
import com.djcps.library.model.Admin;
import com.djcps.library.model.Book;
import com.djcps.library.model.BorrowingBooks;
import com.djcps.library.model.User;
import com.djcps.library.model.vo.BorrowingBooksVo;
import com.djcps.library.model.vo.PageVo;
import com.djcps.library.service.AdminService;

/**
 * @author djsxs
 *
 */
@Service("adminservice")
public class AdminServiceImpl implements AdminService {

	@Resource
	private AdminMapper adminMapper;
	@Resource
	private BookMapper bookMapper;
	@Resource
	private UserMapper userMapper;
	@Resource
	private BorrowingBooksMapper borrowingBooksMapper;

	/**
	 * 确认管理员登录名是否重复
	 */
	@Override
	public boolean adminIsExist(String phone) {
		Admin admin = adminMapper.adminIsExist(phone);
		boolean flag = false;
		if (admin != null) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 管理员登录
	 */
	@Override
	public Admin adminLogin(String phone, String password, HttpServletRequest request, HttpServletResponse response) {
		if (request.getSession().getAttribute("admintoken") == null) {
			UUID token = UUID.randomUUID();
			// 放入session中
			request.getSession().setAttribute("admintoken", token.toString());
			Cookie admin_token = new Cookie("token", token.toString());
			Cookie cDate = new Cookie("lastVisited", DateFormatUtils.format(new Date(), "yyyy-MM-dd"));
			admin_token.setMaxAge(-1);
			response.addCookie(admin_token);
			response.addCookie(cDate);
			Admin admin = adminMapper.adminLogin(phone, password);
			if (admin == null) {
				return null;
			} else {
				request.getSession().setAttribute("admin", admin);
				return admin;
			}
		}
		// 通过请求直接获取request中包含的所有的cookie
		Cookie[] cookies = request.getCookies();
		// 遍历所有的cookie,然后根据cookie的key值来获取value值
		String token = "";
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("adminName")) {
					token = cookie.getValue();
				}
			}
		}
		if (token.equals(request.getSession().getAttribute("admintoken"))) {
			Admin admin = adminMapper.adminLogin(phone, password);
			if (admin == null) {
				return null;
			} else {
				request.getSession().setAttribute("admin", admin);
				return admin;
			}
		} else {
			return null;
		}
	}
	/**
	 * 根据书籍ID删除书籍
	 */
	@Override
	public int delBookByid(Integer bookId) {
		int row = bookMapper.delBookByid(bookId);
		return row;
	}
	/**
	 * 
	 * 书籍信息添加
	 */
	@Override
	public int addBookMsg(MultipartFile file, HttpServletRequest request) {
		String filePath = "D:\\work\\java\\eclipse-workspace\\dj_library\\src\\main\\resources\\static\\upload\\barCode\\";
		String bookName = request.getParameter("bookName");
		String price = request.getParameter("price");
		String desc = request.getParameter("desc");
		String datecount = request.getParameter("datecount");
		String bookAuthor = request.getParameter("bookAuthor");
		String bookPublish = request.getParameter("bookPublish");
		String bookCategory = request.getParameter("bookCategory");
		String barCode = request.getParameter("barCode");
		// 后台接收数据进行长度限制判断
		if (bookName.length() > 10 || desc.length() > 50 || bookAuthor.length() > 10 || bookPublish.length() > 20) {
			return 0;
		}
		Book book = new Book();
		book.setBookName(bookName);
		book.setBookIntroduction(desc);
		book.setDateCount(Integer.valueOf(datecount));
		book.setBookPrice(Double.valueOf(price));
		book.setBookAuthor(bookAuthor);
		book.setBookPublish(bookPublish);
		book.setBorrowCount(0);
		book.setIsborrowedout(0);
		book.setBookCategory(Integer.valueOf(bookCategory));
		Date bookDate = null;
		try {
			bookDate = DateUtils.parseDate(DateFormatUtils.format(new Date(), "yyyy-MM-dd"), "yyyy-MM-dd");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		book.setBookDate(bookDate);
		File dest = FileUpLoadUtil.fileUpLoad(file);
		book.setBookImage(dest.getName());
		if (barCode==null) {
			// 生成二维码图片
			String newbarCode = "DJCPS" + RandomUtil.generateString(8);
			BarCodeUtil.generateFile(newbarCode, filePath + newbarCode + ".png");
			book.setBarCode(newbarCode);
		} else {
			// 生成二维码图片
			BarCodeUtil.generateFile(barCode, filePath + barCode + ".png");
			book.setBarCode(barCode);
		}
		return bookMapper.addBook(book);
	}

	/**
	 * 获取书籍信息
	 */
	@Override
	public Book getBookMsg(Integer bookId) {
		Book book = bookMapper.getBookMsgByid(bookId);
		String imgPath = "/upload/img/" + book.getBookImage();
		book.setBookImage(imgPath);
		return book;
	}
	/**
	 * 更新书籍信息
	 */
	@Override
	public int updateBookMsg(MultipartFile file, HttpServletRequest request) {
		String filePath = "D:\\work\\java\\eclipse-workspace\\dj_library\\src\\main\\resources\\static\\upload\\barCode\\";
		String bookId = request.getParameter("bookId");
		String bookName = request.getParameter("bookName");
		String price = request.getParameter("price");
		String desc = request.getParameter("desc");
		String datecount = request.getParameter("datecount");
		String bookAuthor = request.getParameter("bookAuthor");
		String bookPublish = request.getParameter("bookPulish");
		String bookCategory = request.getParameter("bookCategory");
		String barCode = request.getParameter("barCode");
		// 后台接收数据进行长度限制判断
		if (bookName.length() > 10 || desc.length() > 50 || bookAuthor.length() > 10 || bookPublish.length() > 20) {
			return 0;
		}
		Book book = new Book();
		book.setBookId(Integer.valueOf(bookId));
		book.setBookName(bookName);
		book.setBookIntroduction(desc);
		book.setDateCount(Integer.valueOf(datecount));
		book.setBookPrice(Double.valueOf(price));
		book.setBookAuthor(bookAuthor);
		book.setBookPublish(bookPublish);
		book.setBookCategory(Integer.valueOf(bookCategory));
		// 图片上传
		File dest = FileUpLoadUtil.fileUpLoad(file);
		book.setBookImage(dest.getName());
		if (barCode==null) {
			// 生成二维码图片
			String newbarCode = "DJCPS" + RandomUtil.generateString(8);
			BarCodeUtil.generateFile(newbarCode, filePath + newbarCode + ".png");
			book.setBarCode(newbarCode);
		} else {
			// 生成二维码图片
			BarCodeUtil.generateFile(barCode, filePath + barCode + ".png");
			book.setBarCode(barCode);
		}
		return bookMapper.updateBook(book);
	}

	@Override
	public PageVo selectAllUser(int pageNum) {
		int userTotalCounts = userMapper.getUserTotalCounts();
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
		if (userTotalCounts > 0) {
			totalPage = (userTotalCounts - 1) / 10 + 1;
		}
		pageVo.setPageIndex(pageIndex);
		pageVo.setTotalPage(totalPage);
		pageVo.setPageSize(pageSize);
		List<User> list = userMapper.selectAllByCondition(pageIndex, pageSize);
		pageVo.setUserList(list);
		return pageVo;
	}

	@Override
	public int isAllowBorrow(Integer id, Integer power) {
		return userMapper.isAllowBorrowByid(id, power);
	}

	@Override
	public Admin findAdminByPhone(String phone) {
		return adminMapper.findAdminByPhone(phone);
	}

	@Override
	public PageVo autoSortScore(int pageNum) {
		int userTotalCounts = userMapper.getUserTotalCounts();
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
		if (userTotalCounts > 0) {
			totalPage = (userTotalCounts - 1) / 10 + 1;
		}
		pageVo.setPageIndex(pageIndex);
		pageVo.setTotalPage(totalPage);
		pageVo.setPageSize(pageSize);
		List<User> list = userMapper.autoSortScore(pageIndex, pageSize);
		pageVo.setUserList(list);
		return pageVo;
	}

	@Override
	public PageVo getBorrowBookMsg(Integer pageNum) {
		int borrowBookTotal = borrowingBooksMapper.getBorrowingTotalCount();
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
		if (borrowBookTotal > 0) {
			totalPage = (borrowBookTotal - 1) / 10 + 1;
		}
		pageVo.setPageIndex(pageIndex);
		pageVo.setTotalPage(totalPage);
		pageVo.setPageSize(pageSize);
		List<BorrowingBooks> borrowingBookList = borrowingBooksMapper.selectAllByCondition(pageIndex, pageSize);
		List<BorrowingBooksVo> borrowingBooksVoList = new ArrayList<BorrowingBooksVo>();
		for(BorrowingBooks borrowingBooks : borrowingBookList) {
			BorrowingBooksVo borrowingBooksVo = new BorrowingBooksVo();
			borrowingBooksVo.setBorrowBookId(borrowingBooks.getId());
			Date beginDate = borrowingBooks.getDate();
			Date toDay = new Date();
			long borrowedDays = (toDay.getTime() - beginDate.getTime()) / (24 * 3600 * 1000);
			borrowingBooksVo.setBorrowedDays(borrowedDays);
			Book book = bookMapper.getBookMsgByid(borrowingBooks.getBookId());
			borrowingBooksVo.setIsborrowedout(book.getIsborrowedout());
			borrowingBooksVo.setBookId(book.getBookId());
			borrowingBooksVo.setDateCount(book.getDateCount());
			borrowingBooksVo.setBookName(book.getBookName());
			borrowingBooksVo.setBookPrice(book.getBookPrice());
			borrowingBooksVo.setDesc(book.getBookIntroduction());
			borrowingBooksVoList.add(borrowingBooksVo);
		}
		pageVo.setBorrowingBooksVoList(borrowingBooksVoList);
		return pageVo;
	}

	@Override
	public int deleteBorrowingBooks(Integer bookId) {
		return borrowingBooksMapper.deleteBorrowingBooks(bookId);
	}

}
