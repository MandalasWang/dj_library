package com.djcps.library.service;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.djcps.library.model.Admin;
import com.djcps.library.model.Book;
import com.djcps.library.model.vo.PageVo;

/**
 * @author djsxs
 *
 */
public interface AdminService {

	/**管理员账号是否存在
	 * @param phone
	 * @return
	 */
	boolean adminIsExist(String phone);

	/**管理员登陆
	 * @param phone
	 * @param password
	 * @param request
	 * @return
	 */
	Admin adminLogin(String phone, String password,HttpServletRequest request,HttpServletResponse response);

	/**根据id删除书籍
	 * @param bookId
	 * @return
	 */
	int delBookByid(Integer bookId);

	/**添加书籍信息
	 * @param file
	 * @param request
	 * @return
	 */
	int addBookMsg(MultipartFile file, HttpServletRequest request);
	
	/**根据bookId查找书籍信息
	 * @param bookId
	 * @return
	 */
	Book getBookMsg(Integer bookId);

	/**更新书籍信息
	 * @param file
	 * @param request
	 * @return
	 */
	int updateBookMsg(MultipartFile file, HttpServletRequest request);

	
	/**根据页数获取用户
	 * @param pageNum
	 * @return
	 */
	PageVo selectAllUser(int pageNum);
	
	/**是否启动(禁用)用户
	 * @param id
	 * @param power
	 * @return
	 */
	int isAllowBorrow(Integer id,Integer power);
	
	/**通过手机查询管理员
	 * @param phone
	 * @return
	 */
	Admin findAdminByPhone(String phone);
	
	
	/**用户排名
	 * @param pageNum
	 * @return
	 */
	PageVo autoSortScore(int pageNum);
	
	/**获取借书详细
	 * @param pageNum
	 * @return
	 */
	PageVo getBorrowBookMsg(Integer pageNum);
	
	
	int deleteBorrowingBooks(Integer bookId);
}
