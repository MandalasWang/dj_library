package com.djcps.library.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.djcps.library.model.Admin;
/**
 * @author djsxs
 *
 */
@Mapper
public interface AdminMapper {
	
	
	/**检测管理员账号是否存在
	 * @param phone
	 * @return
	 */
	Admin adminIsExist(@Param("phone")String phone);

	/**管理员登陆
	 * @param phone
	 * @param password
	 * @return
	 */
	Admin adminLogin(@Param("phone")String phone, @Param("password")String password);
 
	
	/**通过手机查找管理员
	 * @param phone
	 * @return
	 */
	Admin findAdminByPhone(@Param("phone")String phone);
	
}