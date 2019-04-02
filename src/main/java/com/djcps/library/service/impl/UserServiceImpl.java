package com.djcps.library.service.impl;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.DigestUtils;

import com.djcps.library.mapper.BookMapper;
import com.djcps.library.mapper.BorrowingBooksMapper;
import com.djcps.library.mapper.UserMapper;
import com.djcps.library.model.Book;
import com.djcps.library.model.BorrowingBooks;
import com.djcps.library.model.User;
import com.djcps.library.model.vo.BorrowingBooksVo;
import com.djcps.library.model.vo.PageVo;
import com.djcps.library.service.UserService;

/**
 * @author djsxs
 *
 */
@Service("userservice")
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;

	@Resource
	private BookMapper bookMapper;

	@Resource
	private BorrowingBooksMapper borrowingBooksMapper;

	@Override
	public int registerUser(String userName, String password, String phone) {
		User user = new User();
		user.setUserName(userName);
		user.setUserPwd(password);
		user.setUserPhone(phone);
		user.setIsAllowBorrow(1);
		user.setScore(0.0);
		String md5Password = DigestUtils.md5DigestAsHex(user.getUserPwd().getBytes());
		user.setUserPwd(md5Password);
		int row = userMapper.register(user);
		return row;
	}

	@Override
	public int userLogin(String userPhone, String userPwd, HttpServletRequest request, HttpServletResponse response) {
		String md5Password = DigestUtils.md5DigestAsHex(userPwd.getBytes());
		User user = userMapper.findUser(userPhone, md5Password);
		if(null == userMapper.findUser(userPhone, md5Password)){
			return 0;
		}else if (request.getSession().getAttribute("usertoken") == null) {
			UUID token = UUID.randomUUID();
			// 放入session中
			request.getSession().setAttribute("usertoken", token.toString());
			Cookie user_token = new Cookie("usertoken", token.toString());
			Cookie cDate = new Cookie("lastVisited", DateFormatUtils.format(new java.util.Date(),"yyyy-MM-dd"));
			user_token.setMaxAge(-1);
			response.addCookie(user_token);
			response.addCookie(cDate);		
			request.getSession().setAttribute("user", user);
			return 1;
		}
		// 通过请求直接获取request中包含的所有的cookie
		Cookie[] cookies = request.getCookies();
		// 遍历所有的cookie,然后根据cookie的key值来获取value值
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				System.out.println("cookie"+cookie);
				if (cookie.getValue().equals(user.getUserName().toString())) {
					return 2;
				}
			}
		}
       return 1;
	}

	@Override
	public int updateUser(String userName, String password, String phone) {
		User user = new User();
		user.setUserName(userName);
		user.setUserPhone(phone);
		user.setUserPwd(password);
		int row = userMapper.updateUser(user);
		return row;
	}

	@Override
	public User findUserByUserPhone(String phone) {
		User users = userMapper.findUserByUserPhone(phone);
		return users;
	}

	@Override
	public User findUserById(Integer id) {
		User users = userMapper.findUserById(id);
		return users;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public synchronized  int userBorrowBook(HttpServletRequest request) {
		// 获取前端传来的数据
		String bookId = request.getParameter("bookId");
		User user = (User) request.getSession().getAttribute("user");
		int borrowingCount=borrowingBooksMapper.getBorrowingBookTotalCountsByUserId(user.getUserId());
		// 测试代码
		Integer isAllowBorrow = user.getIsAllowBorrow();
		Book book = bookMapper.getBookMsgByid(Integer.valueOf(bookId));
		if (isAllowBorrow < 1  ) {
			return 0;
		}else if(book.getIsborrowedout() == 1) {
			return 2;
		}else if (4 < borrowingCount) {
			return 3;
		}
		BorrowingBooks borrowingBooks = new BorrowingBooks();
		// 存储bookid
		borrowingBooks.setBookId(Integer.valueOf(bookId));
		// 存储用户id
		borrowingBooks.setUserId(user.getUserId());
		// 改行为测试代码行
		/* borrowingBooks.setUserId(1); */
		borrowingBooks.setIsreturn(0);
		Date startDate = null;
		try {
			startDate = DateUtils.parseDate(DateFormatUtils.format(new Date(), "yyyy-MM-dd"), "yyyy-MM-dd");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		borrowingBooks.setDate(startDate);
		int datecount = bookMapper.getBookdateCount(Integer.valueOf(bookId));
		Date lastDate = DateUtils.addDays(startDate, datecount);
		borrowingBooks.setLastdate(lastDate);
		int row = borrowingBooksMapper.borrowBook(borrowingBooks);
		if (1 > row) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		int score = 1;
		int row2 = userMapper.addScoreByUserId(borrowingBooks.getUserId(), score);
		if (row2 < 1) {
			return 0;
		}
		int isBorrowed = 1;
		return bookMapper.updateBookByid(borrowingBooks.getBookId(), isBorrowed);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int returnBook(Integer brrowingbookid) {
		// 通过brrowingbookid获取当前书的借书记录
		BorrowingBooks borrowingBooks = borrowingBooksMapper.getBorrowMsgByid(brrowingbookid);
		// 还书
		int isreturn = 1;
		int row = borrowingBooksMapper.retunBook(isreturn, brrowingbookid);
		/** int row1 = borrowingBooksMapper.delBorrowingRecordByisreturn(); */
		if (row < 1) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		int score = 3;
		// 修改分数
		userMapper.addScoreByUserId(borrowingBooks.getUserId(), score);
		int isBorrowed = 0;
		Date returnDate = null;
		try {
			returnDate = DateUtils.parseDate(DateFormatUtils.format(new Date(), "yyyy-MM-dd"), "yyyy-MM-dd");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		borrowingBooksMapper.updateReturnDateByid(brrowingbookid, returnDate);
		return bookMapper.updateBookByid(borrowingBooks.getBookId(), isBorrowed);
	}

	@Override
	public int continueBorrowBook(HttpServletRequest request) {
		String borrowBookId = request.getParameter("borrowBookId");
		User user = (User) request.getSession().getAttribute("user");
		BorrowingBooks borrowingBooks = borrowingBooksMapper.getBorrowMsgByid(Integer.valueOf(borrowBookId));
		Book book=bookMapper.getBookMsgByid(borrowingBooks.getBookId());
		
		// 获取用户的权限值
		Integer isAllowBorrow = user.getIsAllowBorrow();
		// 判断用户是否拥有权限
		if (isAllowBorrow != 1) {
			return 0;
		}else if (book.getIsborrowedout()==2) {
			return 2;
		}
		int datecount = bookMapper.getBookdateCount(borrowingBooks.getBookId());
		// 增加一倍借阅时间
		Date lastDate = DateUtils.addDays(borrowingBooks.getLastdate(), datecount);
		borrowingBooks.setLastdate(lastDate);
		int row = borrowingBooksMapper.updatelastdateByid(borrowingBooks);
		if (row == 0) {
			return 0;
		}
		int score = 2;
		// 增加用户分数
		userMapper.addScoreByUserId(borrowingBooks.getUserId(), score);
		return bookMapper.updateBookborrowCountById(borrowingBooks.getBookId());
	}

	@Override
	public List<Book> findBookBybookName(String bookName) {
		return bookMapper.findBookBybookName(bookName);
	}

	

	@Override
	public PageVo getBorrowBookMsg(Integer pageNum, Integer userId) {
		// 获取借书记录行数
		Integer borrowBookTotals = borrowingBooksMapper.getBorrowingBookTotalCountsByUserId(userId);
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
		if (borrowBookTotals > 0) {
			totalPage = (borrowBookTotals - 1) / 10 + 1;
		}
		pageVo.setPageIndex(pageIndex);
		pageVo.setTotalPage(totalPage);
		pageVo.setPageSize(pageSize);
		// 获取借书着的借书记录所有信息
		List<BorrowingBooks> borrowingBooksList = borrowingBooksMapper.getBorrowingBookMsgByUserId(userId, pageIndex,
				pageSize);
		List<BorrowingBooksVo> borrowingBooksVoList = new ArrayList<BorrowingBooksVo>();
		// 封装borrowingBooksVoList
		for (BorrowingBooks borrowingBooks : borrowingBooksList) {
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
	public String selectPwdByPhone(String phone) {
		String oldPwd = userMapper.selectPwdByPhone(phone);
		return oldPwd;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public synchronized int androidUserBorrowBook(String barCode, Integer userId) {
		User user = userMapper.findUserById(userId);
		Integer isAllowBorrow = user.getIsAllowBorrow();
		Book book = bookMapper.findBookByBarCode(barCode);
		if (isAllowBorrow < 1 ) {
			return 0;
		}else if (book.getIsborrowedout() == 1) {
			return 2;
		}
		int borrowingCount=borrowingBooksMapper.getBorrowingBookTotalCountsByUserId(user.getUserId());
		if (borrowingCount>5) {
			return 1;
		}
		BorrowingBooks borrowingBooks = new BorrowingBooks();
		// 存储bookid
		borrowingBooks.setBookId(book.getBookId());
		// 存储用户id
		borrowingBooks.setUserId(user.getUserId());
		// 改行为测试代码行
		/* borrowingBooks.setUserId(1); */
		borrowingBooks.setIsreturn(0);
		Date startDate = null;
		try {
			startDate = DateUtils.parseDate(DateFormatUtils.format(new Date(), "yyyy-MM-dd"), "yyyy-MM-dd");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		borrowingBooks.setDate(startDate);
		int datecount = bookMapper.getBookdateCount(book.getBookId());
		Date lastDate = DateUtils.addDays(startDate, datecount);
		borrowingBooks.setLastdate(lastDate);
		int row = borrowingBooksMapper.borrowBook(borrowingBooks);
		if (row < 1) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		int score = 1;
		int row2 = userMapper.addScoreByUserId(borrowingBooks.getUserId(), score);
		if (row2 < 1) {
			return 0;
		}
		int isBorrowed = 1;
		return bookMapper.updateBookByid(borrowingBooks.getBookId(), isBorrowed);
	}

	@Override
	public int androidContinueBorrowBook(String barCode, Integer userId) {
		Book book = bookMapper.findBookByBarCode(barCode);
		User user = userMapper.findUserById(userId);
		// 获取用户的权限值
		Integer isAllowBorrow = user.getIsAllowBorrow();
		// 判断用户是否拥有权限
		if (isAllowBorrow != 1 || book.getIsborrowedout() == 0) {
			return 0;
		}
		int isreturn = 0;
		BorrowingBooks borrowingBooks = borrowingBooksMapper.getBorrowMsgBybookIdAndUserId(book.getBookId(), userId,
				isreturn);
		if (borrowingBooks == null) {
			return 0;
		}
		int datecount = bookMapper.getBookdateCount(borrowingBooks.getBookId());
		// 增加一倍借阅时间
		Date lastDate = DateUtils.addDays(borrowingBooks.getLastdate(), datecount);
		borrowingBooks.setLastdate(lastDate);
		int row = borrowingBooksMapper.updatelastdateByid(borrowingBooks);
		if (row == 0) {
			return 0;
		}
		int score = 2;
		// 增加用户分数
		userMapper.addScoreByUserId(borrowingBooks.getUserId(), score);
		return bookMapper.updateBookborrowCountById(borrowingBooks.getBookId());
	}

}
