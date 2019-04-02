package com.djcps.library.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.djcps.library.common.RetResponse;
import com.djcps.library.common.RetResult;
import com.djcps.library.model.Admin;
import com.djcps.library.model.Book;
import com.djcps.library.model.BookCategory;
import com.djcps.library.model.vo.PageVo;
import com.djcps.library.service.AdminService;
import com.djcps.library.service.BookCategoryService;
import com.djcps.library.service.BookService;

/**
 * @author djsxs
 *
 */
@Validated
@RestController
@RequestMapping("/admin")
public class AdminController {
	@Autowired
	@Qualifier(value = "adminservice")
	private AdminService adminService;
	@Autowired
	@Qualifier(value = "bookservice")
	private BookService bookService;
	@Autowired
	@Qualifier(value = "bookcategoryservice")
	private BookCategoryService bookCategoryService;

	/**
	 * 判断admin用户名是否存在
	 * 
	 * @param adminName
	 * @return
	 */
	@RequestMapping("/isAdminExist")
	public RetResult<String> adminIsExist(
			@RequestParam("phone") @Pattern(regexp = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|166|198|199|(147))\\d{8}$", message = "{account.adminPhone.space}") String phone) {
		boolean b = adminService.adminIsExist(phone);
		if (!b) {
			return RetResponse.makeRsp(400, "该用户名已存在");
		} else {
			return RetResponse.makeOKRsp();
		}
	}

	/**
	 * 管理员登录后台
	 */
	@RequestMapping("/adminLogin")
	public RetResult<Object> adminLogin(
			@RequestParam("phone") @Pattern(regexp = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|166|198|199|(147))\\d{8}$", message = "{account.adminPhone.space}") String phone,
			@RequestParam("password") @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "{account.adminPwd.space}") @Size(min = 6, max = 12, message = "{account.adminPwd.size}") @NotBlank(message = "{admin.password.notBlank}") String password,
			HttpServletRequest request,HttpServletResponse response) {
		Admin admin = adminService.adminLogin(phone, password, request,response);
		if (null == admin) {
			return RetResponse.makeRsp(400, "用户名登录失败");
		}
		return RetResponse.makeOKRsp(admin);
	}

	/**
	 * 管理员删除书籍
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/delBook")
	public RetResult<String> delBook(HttpServletRequest request) {
		String bookId = request.getParameter("bookId");
		adminService.deleteBorrowingBooks(Integer.valueOf(bookId));
		int row = adminService.delBookByid(Integer.valueOf(bookId));
		if (row == 0) {
			return RetResponse.makeRsp(400, "书籍删除失败");
		}
		return RetResponse.makeOKRsp();
	}

	/**
	 * 返回管理员登录界面
	 * 
	 * @return
	 */
	@GetMapping("/adminLoginPage")
	public ModelAndView adminLoginPage() {
		ModelAndView mAndView = new ModelAndView();
		mAndView.setViewName("adminLogin");
		return mAndView;
	}

	/**
	 * 添加书籍信息
	 * 
	 * @param file
	 * @param request
	 * @return
	 */
	@PostMapping("/addBook")
	public RetResult<String> addBookMsg(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
		//判断文件是否为空
		if (file.isEmpty()) {
			return RetResponse.makeRsp(400, "上传文件信息为空！");
		}
		int row = adminService.addBookMsg(file, request);
		if (row == 0) {
			return RetResponse.makeRsp(400, "书籍信息保存失败！");
		}
		return RetResponse.makeOKRsp();
	}

	@PostMapping("/updateBook")
	public RetResult<String> updateBookMsg(@RequestParam(value = "file", required = false) MultipartFile file,
			HttpServletRequest request) {
		if (file.isEmpty()) {
			return RetResponse.makeRsp(400, "上传文件信息为空！");
		}
		int row = adminService.updateBookMsg(file, request);
		if (row == 0) {
			return RetResponse.makeRsp(400, "书籍信息保存失败！");
		}
		return RetResponse.makeOKRsp();
	}

	/**
	 * 返回书籍信息
	 * 
	 * @param bookId
	 * @return
	 */
	@RequestMapping("/returnBookmsg")
	public RetResult<Book> returnBookMsg(Integer bookId) {
		Book book = adminService.getBookMsg(bookId);
		if (book == null) {
			return RetResponse.makeRsp(400, "书籍信息查找不存在");
		}
		return RetResponse.makeOKRsp(book);
	}

	@RequestMapping("/returnBookCategoryName")
	public RetResult<List<BookCategory>> returnBookCategoryName() {
		List<BookCategory> bookCategoryList = bookCategoryService.selectBookCategory();
		if (bookCategoryList == null) {
			return RetResponse.makeRsp(400, "书籍信息查找不存在");
		}
		return RetResponse.makeOKRsp(bookCategoryList);
	}

	@RequestMapping("/userList")
	public RetResult<PageVo> userList(@RequestParam("pageNum") int pageNum) {
		PageVo pVo = adminService.selectAllUser(pageNum);
		return RetResponse.makeOKRsp(pVo);
	}

	@RequestMapping("/isAllowBorrow")
	public RetResult<String> isAllowBorrow(@RequestParam("userId") Integer userId,
			@RequestParam("power") @DecimalMax(value = "1") @DecimalMin(value = "0") Integer power) {
		int row = adminService.isAllowBorrow(userId, power);
		if (row == 0) {
			return RetResponse.makeRsp(400, "用户禁用(启用)失败");
		}
		return RetResponse.makeOKRsp();
	}

	

	@RequestMapping("/autoSortScore")
	public RetResult<PageVo> autoSortScore(@RequestParam("pageNum") int pageNum) {
		PageVo pVo = adminService.autoSortScore(pageNum);
		return RetResponse.makeOKRsp(pVo);
	}

	/**
	 * huoqu 管理员返回所有用户借书记录页面
	 * 
	 * @param pageNum
	 * @return
	 */
	@RequestMapping("/getBorrowBookMsg")
	public RetResult<PageVo> getBorrowBookMsg(@RequestParam("pageNum") int pageNum) {
		PageVo pageVo = adminService.getBorrowBookMsg(pageNum);
		if (pageVo == null) {
			return RetResponse.makeRsp(400, "记录查询失败！");
		} else {
			return RetResponse.makeOKRsp(pageVo);
		}
	}

	@RequestMapping("/addBookCatogory")
	public RetResult<String> addBookCatogory(@RequestParam("bookCategoryName") String bookCategoryName) {
		//超出书籍类别长度
		if (bookCategoryName.length() > 20) {
			return RetResponse.makeRsp(400, "书籍类别名称过长！");
		}
		int row = bookCategoryService.insertBookCategory(bookCategoryName);
		if (row < 1) {
			return RetResponse.makeRsp(400, "添加书籍类别失败！");
		}
		return RetResponse.makeOKRsp();
	}

	@RequestMapping("/deleteBookCatogory")
	public RetResult<String> deleteBookCatogory(@RequestParam("bookCategoryId") String bookCategoryId) {
		//根据bookCategoryId删除书籍类别
		int row = bookCategoryService.insertBookCategory(bookCategoryId);
		if (row < 1) {
			return RetResponse.makeRsp(400, "删除书籍类别失败！");
		}
		return RetResponse.makeOKRsp();
	}

	@RequestMapping("/updateBookCatogory")
	public RetResult<String> updateBookCatogory(@RequestParam("bookCategoryId") String bookCategoryId,
			@RequestParam("bookCategoryName") String bookCategoryName) {
		//更改书籍目录名称
		int row = bookCategoryService.updateBookCategory(Integer.valueOf(bookCategoryId), bookCategoryName);
		if (row < 1) {
			return RetResponse.makeRsp(400, "删除书籍类别失败！");
		}
		return RetResponse.makeOKRsp();
	}

	
	@RequestMapping("/androidAddBookByBarCode")
	public RetResult<Book> androidAddBookByBarCode(@RequestParam("barCode") String barCode) {
		Book book = bookService.findBookByBarCode(barCode);
		if (book == null) {
			return RetResponse.makeRsp(300, "请跳转到新书上架页面");
		}
		return RetResponse.makeOKRsp(book);
	}

	/**
	 * 根据phone查询管理员信息
	 * 
	 * @param phone
	 * @return
	 */
	public Admin findAdminByPhone(String phone) {
		return adminService.findAdminByPhone(phone);
	}
}
