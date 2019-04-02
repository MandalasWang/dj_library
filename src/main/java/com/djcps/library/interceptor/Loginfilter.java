package com.djcps.library.interceptor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.djcps.library.common.RetResponse;
import com.djcps.library.common.RetResult;
import com.djcps.library.controller.AdminController;

/**@Component
@WebFilter(urlPatterns="/admin/adminLogin",filterName="loginfilter")*/
/**
 * @author djsxs
 *
 */

/* @WebFilter(filterName = "loginFilter", urlPatterns = { "/*" }) */
public class Loginfilter implements Filter {
	String[] includeUrls = new String[] { "/dj_library/login/", "/dj_library/user/userLogin/", "/dj_library/admin/adminLogin/" };
	@Override
	public void destroy() {
		
	}
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		HttpSession session = request.getSession(false);
		String uri = request.getRequestURI();
		System.out.println("filter url:"+uri);
		boolean needFilter = isNeedFilter(uri);
		if (!needFilter) { //不需要过滤直接传给下一个过滤器
            filterChain.doFilter(servletRequest, servletResponse);
        } else { //需要过滤器
            // session中包含user对象,则是登录状态
            if(null != session && null != session.getAttribute("user")){
                filterChain.doFilter(request, response);
            }else{
            	request.getSession().setAttribute("message", "用户名不存在，请重新登录");
            	return ;
            }
        }
	}
	public boolean isNeedFilter(String uri) {
        for (String includeUrl : includeUrls) {
            if(includeUrl.equals(uri)) {
                return false;
            }
        }
        return true;
    }
	@Override
	public void init(FilterConfig arg0) throws ServletException {
		System.out.println("init filter");
	}
}
