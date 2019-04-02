package com.djcps.library.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.djcps.library.model.Book;
import com.djcps.library.model.User;
import com.djcps.library.model.vo.BookVo;
import com.djcps.library.model.vo.PageVo;

/**
 * @author djsxs
 *
 */
public interface UserService {
 
	/**注册账户
	 * @param userName
	 * @param password
	 * @param phone
	 * @return
	 */
	int  registerUser(String userName, String password, String phone);

	/**用户登陆
	 * @param phone
	 * @param password
	 * @param request
	 * @return
	 */
	int userLogin(String phone, String password,HttpServletRequest request,HttpServletResponse response);

	/**更新用户信息
	 * @param userName
	 * @param password
	 * @param phone
	 * @return
	 */
	int updateUser(String userName, String password, String phone);

	/**通过phone查询用户
	 * @param phone
	 * @return
	 */
	User findUserByUserPhone(String phone);

	/**用过id查询用户
	 * @param id
	 * @return
	 */
	User findUserById(Integer id);

	/**用户借书
	 * @param request
	 * @return
	 */
	int userBorrowBook(HttpServletRequest request);

	/**还书
	 * @param brrowingbookid
	 * @param isreturn
	 * @return
	 */
	int returnBook(Integer brrowingbookid);

	/**根据书名查找书籍
	 * @param bookName
	 * @return
	 */
	List<Book> findBookBybookName(String bookName);


	/**续借
	 * @param request
	 * @return
	 */
	int continueBorrowBook(HttpServletRequest request);
	
	
	/**根据userId获取借书信息
	 * @param pageNum
	 * @param userId
	 * @return
	 */
	PageVo getBorrowBookMsg(Integer pageNum,Integer userId);
	
	String selectPwdByPhone(String phone);

	int androidUserBorrowBook(String barCode, Integer userId);

	int androidContinueBorrowBook(String barCode, Integer userId);
	
}
