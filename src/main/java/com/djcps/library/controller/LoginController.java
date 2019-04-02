package com.djcps.library.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.djcps.library.common.RetResponse;
import com.djcps.library.common.RetResult;
import com.djcps.library.model.Admin;
import com.djcps.library.model.User;


/**
 * @author djsxs
 *
 */
@RestController
@RequestMapping("/")
public class LoginController {
	@Resource
	private AdminController adminController;
	@Resource
	private UserController usercontroller;

	@RequestMapping("login")
	public RetResult<Object> release(HttpServletRequest request,HttpServletResponse response) {
		String phone = request.getParameter("phone");
		String password = request.getParameter("password");
		Admin admin = adminController.findAdminByPhone(phone);
		User user = usercontroller.findUserByPhone(phone);
		if (admin != null) {
			return adminController.adminLogin(phone, password, request,response);
		} else if (user != null) {
			return usercontroller.userLogin(phone, password, request,response);
		}
		return RetResponse.makeRsp(400,"无法访问");
	}
}
