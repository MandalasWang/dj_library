package com.djcps.library.controller;

import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.util.DigestUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.djcps.library.common.RetResponse;
import com.djcps.library.common.RetResult;
import com.djcps.library.model.Book;
import com.djcps.library.model.BorrowingBooks;
import com.djcps.library.model.User;
import com.djcps.library.model.vo.BookVo;
import com.djcps.library.model.vo.PageVo;
import com.djcps.library.service.BookService;
import com.djcps.library.service.BorrowingBooksRecordService;
import com.djcps.library.service.UserService;

/**
 * @author djsxs
 *
 */
@Validated
@RestController
@RequestMapping(value = "/user", method = { RequestMethod.GET, RequestMethod.POST })
public class UserController {
	@Resource(name = "userservice")
	private UserService userService;
	@Resource(name = "bookservice")
	private BookService bookService;
	@Resource(name = "borrowingBooksRecordService")
	private BorrowingBooksRecordService borrowingBooksRecordService;

	/**
	 * 用户注册功能
	 * 
	 * @param name
	 * @param pwd
	 * @param phone
	 * @return
	 */
	@RequestMapping(value = "/userRegister", method = RequestMethod.POST)
	public RetResult<String> registerUser(
			@Param(value = "userName") @NotBlank(message = "{user.name.notBlank}") @Pattern(regexp = "^[\\u4e00-\\u9fa5]{2,5}$|^[\\dA-Za-z_]{1,12}$", message = "{account.adminName.space}") String userName,
			@Param(value = "password") @Pattern(regexp = "^[a-zA-Z0-9]{6,10}$", message = "{account.userPwd.space}") String password,
			@Param(value = "phone") @Pattern(regexp = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|166|198|199|(147))\\d{8}$", message = "{account.userPhone.space}") String phone) {
		int row = userService.registerUser(userName, password, phone);
		if (row == 0) {
			return RetResponse.makeRsp(400, "用户注册失败！");
		}
		return RetResponse.makeOKRsp();
	}

	/**
	 * 用户登录功能的实现
	 * 
	 * @param phone
	 * @return
	 */
	@RequestMapping(value = "/userLogin", method = RequestMethod.POST)
	public RetResult<Object> userLogin(
			@RequestParam("phone") @Pattern(regexp = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|166|198|199|(147))\\d{8}$", message = "{account.userPhone.space}") String phone,
			@RequestParam("password") @Pattern(regexp = "^[a-zA-Z0-9]{6,10}$", message = "{account.userPwd.space}") String password,
			HttpServletRequest request, HttpServletResponse response) {
		int row = userService.userLogin(phone, password, request, response);
		if (0 == row) {
			return RetResponse.makeRsp(400, "账号或密码错误!");
		} else if (2 == row) {
			return RetResponse.makeRsp(400, "该账号已在其他地方登录!");
		} else {
			User user = userService.findUserByUserPhone(phone);
			HttpSession sessoin = request.getSession();
			Cookie sessionName = new Cookie("sessionName", user.getUserName().toString());
			System.out.println("sessionname" + sessionName.toString());
			// JSESSIONID
			Cookie sessionId = new Cookie("JSESSIONID", request.getSession().getId()); // 作为session的idName
			System.out.println("sessionid" + sessionId.toString());
			// 设置浏览器关闭cookie就失效
			sessionName.setMaxAge(-1);
			// 浏览器关闭cookie就失效
			sessionId.setMaxAge(-1);
			response.addCookie(sessionName);
			response.addCookie(sessionId);
			// 以秒为单位，即在没有活动20分钟后，session将失效
			// sessoin.setMaxInactiveInterval(20*60);
			UUID token = UUID.randomUUID();
			// 放入session中
			sessoin.setAttribute("usertoken", token.toString());
			Cookie user_token = new Cookie("usertoken", token.toString());
			Cookie cDate = new Cookie("lastVisited", DateFormatUtils.format(new java.util.Date(), "yyyy-MM-dd"));
			user_token.setMaxAge(-1);
			response.addCookie(user_token);
			response.addCookie(cDate);
			return RetResponse.makeOKRsp(user);
		}
	}

	/**
	 * 返回个人信息页面
	 * 
	 * @return
	 */
	@RequestMapping("/userMessagePage")
	public RetResult<User> userMessagePage(HttpServletRequest request) {
		User sessionUser = (User) request.getSession().getAttribute("user");
		User user = userService.findUserById(sessionUser.getUserId());
		return RetResponse.makeOKRsp(user);
	}

	/**
	 * 用户更新信息
	 * 
	 * @param userName
	 * @param password
	 * @return
	 */
	@RequestMapping("/updateUserByphone")
	public RetResult<String> updateUserByphone(
			@Param("userName") @NotBlank(message = "{user.name.notBlank}") String userName,
			@Param("phone") @Pattern(regexp = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|166|198|199|(147))\\d{8}$", message = "{account.userPhone.space}") String phone,
			@Param("newPassword") @Pattern(regexp = "^[a-zA-Z0-9]{6,10}$", message = "{account.userPwd.space}") String newPassword) {
		String md5Password = DigestUtils.md5DigestAsHex(newPassword.getBytes());
		int row = userService.updateUser(userName, md5Password, phone);
		if (row == 0) {
			return RetResponse.makeRsp(400, "更新用户失败");
		}
		return RetResponse.makeOKRsp();
	}

	@RequestMapping("/isRightPassword")
	public RetResult<String> isRightPassword(
			@Param("phone") @Pattern(regexp = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|166|198|199|(147))\\d{8}$", message = "{account.userPhone.space}") String phone,
			@Param("oldPassword") @Pattern(regexp = "^[a-zA-Z0-9]{6,10}$", message = "{account.userPwd.space}") String oldPassword) {
		User user = userService.findUserByUserPhone(phone);
		String md5Password = DigestUtils.md5DigestAsHex(oldPassword.getBytes());
		if (!md5Password.equals(user.getUserPwd())) {
			return RetResponse.makeRsp(400, "密码错误");
		}
		return RetResponse.makeOKRsp();
	}

	/**
	 * 用户退出登陆
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/userLogOut")
	public void userLogOut(HttpServletRequest request) {
		request.getSession().invalidate();

	}

	/**
	 * //验证用户是否存在
	 * 
	 * @param phone
	 * @return
	 */
	@RequestMapping("/isUserExist")
	public RetResult<String> isUserExist(
			@Param("phone") @Pattern(regexp = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|166|198|199|(147))\\d{8}$", message = "{account.userPhone.space}") String phone) {
		User users = userService.findUserByUserPhone(phone);
		if (null == users) {
			return RetResponse.makeRsp(400, "用户已存在");
		}
		return RetResponse.makeOKRsp();
	}

	/**
	 * 用户借书
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/borrowing")
	public RetResult<String> userBorrowBook(HttpServletRequest request) {
		int row = userService.userBorrowBook(request);
		if (row == 0) {
			return RetResponse.makeRsp(400, "借书失败");
		}else if (row == 2) {
			return RetResponse.makeRsp(400, "该书已被借出！");
		} else if (row == 3) {
			return RetResponse.makeRsp(400, "您的借书次数已耗尽");}
		return RetResponse.makeOKRsp();
	}

	/**
	 * 返回借书页面
	 * 
	 * @return
	 */
	@RequestMapping("/borrowingPage")
	public RetResult<String> borrowing() {
		return RetResponse.makeOKRsp();
	}

	/**
	 * 返还书籍
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/returnBook")
	public RetResult<String> userReturnBook(@RequestParam("borrowBookId") Integer borrowBookId) {
		int row = userService.returnBook(borrowBookId);
		if (row == 0) {
			return RetResponse.makeRsp(400, "书籍归还失败");

		}
		return RetResponse.makeOKRsp();
	}

	/**
	 * 书籍续借
	 * 
	 * @param borrowBookid
	 * @return
	 */
	@RequestMapping("/continueBorrowBook")
	public RetResult<String> continueBorrowBook(HttpServletRequest request) {
		int row = userService.continueBorrowBook(request);
		if (row == 0) {
			return RetResponse.makeRsp(400, "续借失败！");
		}else if(row==2) {
			return RetResponse.makeRsp(400, "该书只能续借一次！");
		}
		return RetResponse.makeOKRsp();
	}

	/**
	 * 按照书名进行模糊查询
	 */
	@RequestMapping("/findBooksByBookName")
	public RetResult<List<Book>> findBooksByBookName(HttpServletRequest request) {
		String bookName = request.getParameter("bookName");
		List<Book> list = userService.findBookBybookName(bookName);
		return RetResponse.makeOKRsp(list);
	}

	
	@RequestMapping("/getBorrowBookMsgByUserId")
	public RetResult<PageVo> getBorrowBookMsgByUserId(@RequestParam("pageNum") int pageNum,
			HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		PageVo pageVo = userService.getBorrowBookMsg(pageNum, user.getUserId());

		/*
		 * Integer userId = Integer.valueOf(request.getParameter("userId")); PageVo
		 * pageVo = userService.getBorrowBookMsg(pageNum, userId);
		 */

		if (pageVo == null) {
			return RetResponse.makeRsp(400, "记录查询失败！");
		} else {
			return RetResponse.makeOKRsp(pageVo);
		}
	}

	public User findUserByPhone(String phone) {
		return userService.findUserByUserPhone(phone);
	}

	@RequestMapping("/androidUpdateUserByphone")
	public RetResult<String> AndroidUpdateUserByphone(
			@Param("userName") @NotBlank(message = "{user.name.notBlank}") String userName,
			@Param("phone") @Pattern(regexp = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|166|198|199|(147))\\d{8}$", message = "{account.userPhone.space}") String phone,
			@Param("oldPassword") @Pattern(regexp = "^[a-zA-Z0-9]{6,10}$", message = "{account.userPwd.space}") String oldPassword,
			@Param("newPassword") @Pattern(regexp = "^[a-zA-Z0-9]{6,10}$", message = "{account.userPwd.space}") String newPassword) {
		String oldPasswordMd5 = DigestUtils.md5DigestAsHex(oldPassword.getBytes());
		if (!oldPasswordMd5.equals(userService.selectPwdByPhone(phone))) {
			return RetResponse.makeRsp(400, "旧密码错误");
		}
		String newPasswordMd5 = DigestUtils.md5DigestAsHex(newPassword.getBytes());
		int row = userService.updateUser(userName, newPasswordMd5, phone);
		if (row == 0) {
			return RetResponse.makeRsp(400, "更新用户失败");
		}
		return RetResponse.makeOKRsp();
	}

	@RequestMapping("/androidGetBorrowBookMsgByUserId")
	public RetResult<PageVo> AndroidGetBorrowBookMsgByUserId(@RequestParam("pageNum") int pageNum,
			@RequestParam("userId") Integer userId) {
		PageVo pageVo = userService.getBorrowBookMsg(pageNum, userId);
		if (pageVo == null) {
			return RetResponse.makeRsp(400, "记录查询失败！");
		} else {
			return RetResponse.makeOKRsp(pageVo);
		}
	}

	@RequestMapping("/androidFindBookByScanBarCode")
	public RetResult<BookVo> userFindBookByBarCode(@RequestParam("barCode") String barCode) {
		Integer isreturn = 0;
		BookVo bookVo = bookService.getBorrowedMsgAndroid(barCode, isreturn);
		if (bookVo == null) {
			return RetResponse.makeRsp(400, "查无此书");
		}
		return RetResponse.makeOKRsp(bookVo);
	}

	@RequestMapping("/androidScanBorrowing")
	public RetResult<String> androidScanBorrowing(@RequestParam("barCode") String barCode,
			@RequestParam("userId") Integer userId) {
		int row = userService.androidUserBorrowBook(barCode, userId);
		if (row == 0) {
			return RetResponse.makeRsp(400, "您并无借书权限");
		} else if (row == 1) {
			return RetResponse.makeRsp(400, "您的借书次数已耗尽");
		}else if (row == 2) {
			return RetResponse.makeRsp(400, "该书籍已被他人借阅");
		}
		return RetResponse.makeOKRsp();
	}

	@RequestMapping("/androidScanReturnBook")
	public RetResult<String> androidScanReturnBook(@RequestParam("barCode") String barCode,
			@RequestParam("userId") Integer userId) {
		Book book = bookService.findBookByBarCode(barCode);
		BorrowingBooks borrowingBooks = borrowingBooksRecordService.getBorrowMsgBybookId(book.getBookId(), userId);
		if (borrowingBooks == null) {
			return RetResponse.makeRsp(400, "书籍归还失败，这本书并不是你借阅书籍");
		}
		int row = userService.returnBook(borrowingBooks.getId());
		if (row == 0) {
			return RetResponse.makeRsp(400, "书籍归还失败");
		}
		return RetResponse.makeOKRsp();
	}

	@RequestMapping("/androidContinueBorrowBook")
	public RetResult<String> androidContinueBorrowBook(@RequestParam("barCode") String barCode,
			@RequestParam("userId") Integer userId) {
		int row = userService.androidContinueBorrowBook(barCode, userId);
		if (row == 0) {
			return RetResponse.makeRsp(400, "续借失败,这本书并不是你借阅书籍");
		}
		return RetResponse.makeOKRsp();
	}
}
