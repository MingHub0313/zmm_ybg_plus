package com.ybg.rbac.user.service;

import java.util.List;
import org.springframework.social.connect.Connection;
import com.ybg.base.jdbc.BaseMap;
import com.ybg.base.util.Json;
import com.ybg.base.util.Page;
import com.ybg.rbac.user.domain.UserDTO;
import com.ybg.rbac.user.qvo.UserQuery;

/***
 * @author https://gitee.com/YYDeament/88ybg
 * 
 * @date 2016/10/1
 */
/** @author Administrator */
public interface UserService {

	/**
	 * 创建用户
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	UserDTO save(UserDTO user) throws Exception;

	/***
	 * 管理员 更新 用户的状态,email,电话，角色
	 * 
	 * 
	 * 
	 * @param user
	 * @return
	 */
	Json updateUserBaseInfo(UserDTO user);

	/**
	 * 分页查询
	 * 
	 * @param page
	 * @param qvo
	 * @return
	 * @throws Exception
	 */
	Page list(Page page, UserQuery qvo) throws Exception;

	/**
	 * 不分页查询
	 * 
	 * @param qvo
	 * @return
	 * @throws Exception
	 */
	List<UserDTO> list(UserQuery qvo) throws Exception;

	/**
	 * 登陆
	 * 
	 * @param loginname
	 * @return
	 */
	UserDTO login(String loginname);

	/**
	 * 删除用户
	 * 
	 * @param wheremap
	 */
	void remove(BaseMap<String, Object> wheremap);

	/**
	 * 清楚注册不激活的用户
	 * 
	 * @throws Exception
	 **/
	void removeExpired() throws Exception;

	/**
	 * 根据ID删除用户
	 * 
	 * @param id
	 */
	void removebyid(String id);

	/**
	 * 根据ID获取用户
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	UserDTO get(String id) throws Exception;

	/**
	 * 查询账户是否已存在
	 * 
	 * @param qvo
	 * @return
	 */
	boolean checkisExist(UserQuery qvo);

	/**
	 * 获取用户社交账号
	 * 
	 * @param connection
	 * 
	 * @return
	 */
	public List<String> findUserIdsWithConnection(Connection<?> connection);

	/**
	 * 更新用户的角色依赖
	 * 
	 * @param userid
	 * @param roleids
	 */
	void updateUserRole(String userid, List<String> roleids);

	/**
	 * 邮箱激活
	 * 
	 * @param username
	 *            登录名
	 * @param salt
	 *            签名校验
	 * @return
	 * @throws Exception
	 */
	Json reLife(String username, String salt) throws Exception;

	/**
	 * 更新密码
	 * 
	 * @param user
	 *            原有用户
	 * @param password
	 *            原密码
	 * @param newPassword
	 *            新密码
	 * @return
	 * @throws Exception
	 */
	Json updatePassword(UserDTO user, String password, String newPassword) throws Exception;

	/** 点击 重置密码上的地址，跳到平台去重置密码的操作 **/
	/**
	 * @param encryptInfo
	 *            加密信息
	 * @param password
	 *            新 密码
	 * @return
	 */
	Json resetPassword(String encryptInfo, String password);

	/**
	 * 注册 （通过邮箱）
	 * 
	 * @param user
	 * @param email
	 * @return
	 * @throws Exception
	 */
	Json registerByEmail(UserDTO user, String email) throws Exception;

	/**
	 * 忘记密码（通过邮箱）
	 * 
	 * @param username
	 * @return
	 * @throws Exception 
	 */
	Json forgetPwd(String username) throws Exception;

}
