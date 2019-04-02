package com.djcps.library.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.djcps.library.model.User;

/**
 * @author djsxs
 *
 */
@Mapper
public interface UserMapper {
	/**
	 * 注册账户
	 * 
	 * @param user
	 * @return
	 */
	public int register(User user);

	/**
	 * 登陆
	 * 
	 * @param userPhone
	 * @param userPwd
	 * @return
	 */
	public User findUser(@Param(value = "userPhone") String userPhone, @Param(value = "userPwd") String userPwd);

	/**
	 * 更新用户
	 * 
	 * @param user
	 * @return
	 */
	public int updateUser(User user);

	/**
	 * 通过phone查找用户
	 * 
	 * @param phone
	 * @return
	 */
	public User findUserByUserPhone(@Param(value = "phone") String phone);

	/**
	 * 通过id查找用户
	 * 
	 * @param id
	 * @return
	 */
	public User findUserById(@Param(value = "id") Integer id);

	/**
	 * 得到用户总数
	 * 
	 * @return
	 */
	int getUserTotalCounts();

	/**
	 * 分页查询用户信息
	 * 
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	List<User> selectAllByCondition(@Param("pageIndex") int pageIndex, @Param("pageSize") int pageSize);

	/**
	 * 是否启动(禁用)用户操作
	 * 
	 * @param id
	 * @param power
	 * @return
	 */
	public int isAllowBorrowByid(@Param(value = "userId") Integer id, @Param(value = "power") Integer power);

	
	/**按页分数分数排行
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public List<User> autoSortScore(@Param(value = "pageIndex") int pageIndex, @Param(value = "pageSize") int pageSize);

	/**根据userId增加该用户分数
	 * @param userId
	 * @param score
	 * @return
	 */
	int addScoreByUserId(@Param(value = "userId") Integer userId, @Param(value = "score") int score);
	
	String selectPwdByPhone(@Param(value = "phone")String phone);

}
