package com.djcps.library.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author djsxs
 *
 */
public class LoginInterceptor implements HandlerInterceptor {
	/**在请求处理之前进行调用（Controller方法调用之前*/
	@Override
	public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o)
			throws Exception {
		HttpSession session = httpServletRequest.getSession();
		String user = (String) session.getAttribute("user"); // 获取登录的session信息
		if (user != null) {
			return true;
		} else {
			httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/login/index"); // 未登录自动跳转界面
			return false;
		}
	}

	/** 请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后）*/
	@Override
	public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o,
			ModelAndView modelAndView) throws Exception {

		System.out.println("postHandle被调用\n");
	}

	/** 在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行（主要是用于进行资源清理工作）*/
	@Override
	public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			Object o, Exception e) throws Exception {
		System.out.println("afterCompletion被调用\n");
	}
}