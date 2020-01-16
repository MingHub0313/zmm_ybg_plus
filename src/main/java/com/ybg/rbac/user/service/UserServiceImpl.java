package com.ybg.rbac.user.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.social.connect.Connection;
import org.springframework.stereotype.Repository;
import com.ybg.base.jdbc.BaseMap;
import com.ybg.base.jdbc.util.DateUtil;
import com.ybg.base.jdbc.util.QvoConditionUtil;
import com.ybg.base.util.DesUtils;
import com.ybg.base.util.Json;
import com.ybg.base.util.Page;
import com.ybg.base.util.SystemConstant;
import com.ybg.base.util.VrifyCodeUtil;
import com.ybg.component.email.sendemail.SendEmailInter;
import com.ybg.component.email.sendemail.SendQqmailImpl;
import com.ybg.rbac.RbacConstant;
import com.ybg.rbac.user.UserStateConstant;
import com.ybg.rbac.user.dao.UserDao;
import com.ybg.rbac.user.domain.UserDTO;
import com.ybg.rbac.user.qvo.UserQuery;

import net.sf.json.JSONObject;

/***
 * @author https://gitee.com/YYDeament/88ybg
 * 
 * @date 2016/10/1
 */
@Repository
public class UserServiceImpl implements UserService {

	@Autowired
	UserDao userDao;

	/**
	 * 新增并返回主键
	 * 
	 * @throws Exception
	 **/
	@Override
	// @CacheEvict(value = "userCache", allEntries = true)
	public UserDTO save(UserDTO user) throws Exception {
		return userDao.save(user);
	}

	/** 根据id删除 **/
	@Override
	// @CacheEvict(value = "userCache", allEntries = true)
	public void removebyid(String id) {
		BaseMap<String, Object> updatemap = new BaseMap<String, Object>();
		BaseMap<String, Object> wheremap = new BaseMap<String, Object>();
		wheremap.put("id", id);
		updatemap.put("isdelete", "1");
		userDao.update(updatemap, wheremap);
	}

	/** 注册 发送邮箱失败删除 **/
	@Override
	// @CacheEvict(value = "userCache", allEntries = true)
	public void remove(BaseMap<String, Object> wheremap) {
		userDao.remove(wheremap);
	}

	/** 根据某些属性来更新 不再限定固定属性 **/
	// @Override
	// @CacheEvict(value = "userCache",allEntries = true)
	// public void update(BaseMap<String, Object> updatemap,BaseMap<String, Object>
	// wheremap){
	// userDao.update(updatemap, wheremap);
	// }
	//

	/**
	 * 获取单个信息
	 * 
	 * @throws Exception
	 **/
	@Override
	// @Cacheable(value = "userCache", key = "#root.method.name+#root.args[0]")
	public UserDTO get(String id) throws Exception {
		return userDao.loginById(id);
	}

	/**
	 * 分页查询用户信息
	 * 
	 * @throws Exception
	 **/
	@Override
	// @Cacheable(value = "userCache", key =
	// "#root.method.name+#root.args[0]+#root.method.name+#root.args[1]")
	public Page list(Page page, UserQuery qvo) throws Exception {
		return userDao.list(page, qvo);
	}

	/**
	 * 不分页查询用户信息
	 * 
	 * @throws Exception
	 **/
	@Override
	// @Cacheable(value = "userCache", key = "#root.method.name+#root.args[0]")
	public List<UserDTO> list(UserQuery qvo) throws Exception {
		return userDao.list(qvo);
	}

	/** 登陆 **/
	@Override
	// @Cacheable(value = "userCache", key = "#root.method.name+#root.args[0]")
	public UserDTO login(String loginname) {
		return userDao.login(loginname);
	}

	/**
	 * 清楚注册不激活的用户
	 * 
	 * @throws Exception
	 **/
	@Override
	// @CacheEvict(value = "userCache", allEntries = true)
	public void removeExpired() throws Exception {
		userDao.removeExpired();
	}

	@Override
	public boolean checkisExist(UserQuery qvo) {
		return userDao.checkisExist(qvo);
	}

	@Override
	public List<String> findUserIdsWithConnection(Connection<?> connection) {
		return userDao.findUserIdsWithConnection(connection);
	}

	@Override
	public void updateUserRole(String userid, List<String> roleids) {
		userDao.updateUserRole(userid, roleids);
	}

	@Override
	public Json updateUserBaseInfo(UserDTO user) {
		Json j = new Json();
		j.setSuccess(true);
		BaseMap<String, Object> updatemap = new BaseMap<String, Object>();
		BaseMap<String, Object> wheremap = new BaseMap<String, Object>();
		updatemap.put("state", user.getState());
		updatemap.put("email", user.getEmail());
		updatemap.put("phone", user.getPhone());
		wheremap.put("id", user.getId());
		try {
			userDao.update(updatemap, wheremap);
			// 更新角色
			userDao.updateUserRole(user.getId(), user.getRoleids());
		} catch (Exception e) {
			j.setMsg("操作失败");
			return j;
		}
		j.setMsg("操作成功");
		return j;
	}

	@Override
	public Json reLife(String username, String salt) throws Exception {
		Json j = new Json();
		if (QvoConditionUtil.checkString(username)) {
			j.setSuccess(false);
			j.setMsg("用户名为空");
			return j;
		}

		UserQuery qvo = new UserQuery();
		qvo.setUsername(username);
		qvo.setState(UserStateConstant.DIE);
		List<UserDTO> list = userDao.list(qvo);
		if (list != null && list.size() == 1) {
			BaseMap<String, Object> updatemap = new BaseMap<String, Object>();
			BaseMap<String, Object> wheremap = new BaseMap<String, Object>();
			updatemap.put("state", UserStateConstant.OK);
			wheremap.put("username", list.get(0).getUsername());
			userDao.update(updatemap, wheremap);
			j.setSuccess(false);
			j.setMsg("激活成功 ，现在可以登录");
			return j;
		}
		j.setSuccess(false);
		j.setMsg("该链接已经失效");
		return j;
	}

	@Override
	public Json updatePassword(UserDTO user, String password, String newPassword) throws Exception {
		Json j = new Json();
		j.setSuccess(true);
		j.setMsg("操作成功");
		if (user == null) {
			j.setMsg("您尚未登陆");
			return j;
		}
		if (newPassword.length() <= RbacConstant.MIN_PASSWORD_LENTH) {
			j.setMsg("新密码太短");
			return j;
		}
		if (!password.equals(new DesUtils().decrypt(user.getCredentialssalt()))) {
			j.setMsg("密码错误！");
			return j;
		}
		BaseMap<String, Object> updatemap = new BaseMap<String, Object>();
		BaseMap<String, Object> wheremap = new BaseMap<String, Object>();
		updatemap.put("password", RbacConstant.getpwd(newPassword));
		updatemap.put("credentialssalt", new DesUtils().encrypt(newPassword));
		wheremap.put("id", user.getId());
		userDao.update(updatemap, wheremap);
		return j;
	}

	@Override
	public Json resetPassword(String encryptInfo, String password) {
		Json j = new Json();
		j.setSuccess(true);
		j.setMsg("操作成功");
		try {
			JSONObject json = JSONObject.fromObject(new DesUtils().decrypt(encryptInfo));
			String userid = json.getString("uid");
			String dietime = json.getString("dietime");
			if (!dietime.equals(DateUtil.getDate())) {
				j.setSuccess(false);
				j.setMsg("操作失败！时间已过");
				return j;
			}
			UserDTO user = userDao.getById(userid);
			if (user == null) {
				return null;
			}
			if (user.getState().equals(UserStateConstant.LOCK)) {
				j.setSuccess(false);
				j.setMsg("账号被锁 ，无法使用");
				return j;
			}
			if (user.getState().equals(UserStateConstant.DIE)) {
				j.setSuccess(false);
				j.setMsg("账号未激活 ，无法使用");
				return j;
			}
			if (!user.getState().equals(UserStateConstant.OK)) {
				j.setSuccess(false);
				j.setMsg("未知原因 ，无法使用");
				return j;
			}
			BaseMap<String, Object> updatemap = new BaseMap<String, Object>();
			BaseMap<String, Object> wheremap = new BaseMap<String, Object>();
			updatemap.put("password", RbacConstant.getpwd(password));
			updatemap.put("credentialssalt", new DesUtils().encrypt(password));
			wheremap.put("id", user.getId());
			userDao.update(updatemap, wheremap);
		} catch (Exception e) {
			e.printStackTrace();
			j.setSuccess(false);
			j.setMsg("操作失败！");
			return j;
		}
		return j;
	}

	@Override
	public Json registerByEmail(UserDTO user, String email) throws Exception {
		Json j = new Json();

		boolean namestander = user.getUsername().trim().startsWith("qq") || user.getUsername().trim().startsWith("sina")
				|| user.getUsername().trim().startsWith("github") || user.getUsername().trim().startsWith("baidu")
				|| user.getUsername().trim().startsWith("weixin");
		if (namestander) {
			j.setSuccess(false);
			j.setMsg("不能以qq、sina、weixin、baidu、github 开头注册");
			return j;
		}
		j.setSuccess(true);
		j.setMsg("我们将发送邮箱到您的邮箱中进行验证，大约3小时左右不验证将删除注册信息");
		String now = DateUtil.getDateTime();
		user.setCredentialssalt(new DesUtils().encrypt(user.getPassword()));
		user.setPassword(RbacConstant.getpwd(user.getPassword()));
		user.setRoleids(RbacConstant.initRole());
		user.setPhone("");
		user.setState(UserStateConstant.DIE);
		user.setCreatetime(now);
		try {
			userDao.save(user);
		} catch (Exception e) {
			e.printStackTrace();
			j.setMsg("创建失败，已存在该用户");
			return j;
		}
		String url = SystemConstant.getSystemdomain() + "/common/login_do/relife.do?userid=" + user.getId()
				+ "&username=" + user.getUsername() + "&salt=" + user.getCredentialssalt();
		// 获取激活邮件的hmtl内容
		String contemt = this.getActiveContent(url, user.getUsername());
		try {
			SendEmailInter send = new SendQqmailImpl();
			send.sendMail(email, SystemConstant.getSystemName() + "注册", contemt);
		} catch (Exception e) {
			e.printStackTrace();
			BaseMap<String, Object> wheremap = new BaseMap<String, Object>();
			wheremap.put("id", user.getId());
			userDao.remove(wheremap);
			j.setMsg("发送邮箱失败，可能被提供方拦截，再试一次或者换一种邮箱类型");
			return j;
		}
		return j;

	}

	/**
	 * 获取拼接的激活邮件的内容
	 * 
	 * @param url
	 *            激活链接
	 * @param username
	 *            用户名
	 * @return 字符串形的邮件内容
	 */
	private String getActiveContent(String activeurl, String username) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<html><head>");
		buffer.append("<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\">");
		buffer.append("<base target=\"_blank\" />");
		buffer.append("</head>");
		buffer.append("<body>尊敬的 ，");
		buffer.append(username);
		buffer.append(" 您好！<br>");
		buffer.append("请点击");
		buffer.append("<a href=" + activeurl + ">激活</a>");
		buffer.append("激活您的账号,<br>");
		buffer.append("为保障您的帐号安全，请在3小时内点击该链接<br>");
		buffer.append("如无法点击请您将下面链接<br><span style=\"color:blue\">" + activeurl
				+ "</span><br>复制到浏览器地址栏访问。 若如果您已激活，请忽略本邮件，由此给您带来的不便请谅解。<br><br><br>");
		buffer.append("本邮件由系统自动发出，请勿直接回复！ ");
		buffer.append("</body></html>");
		return buffer.toString();
	}

	@Override
	public Json forgetPwd(String username) throws Exception {
		Json j = new Json();
		// 首先检测验证码
		j.setSuccess(true);
		UserQuery userqvo = new UserQuery();
		userqvo.setUsername(username);
		List<UserDTO> userlist = userDao.list(userqvo);
		if (userlist == null || userlist.size() == 0) {
			j.setSuccess(false);
			j.setMsg("无此账号");
			return j;
		}
		UserDTO user = userlist.get(0);
		if (user.getState().equals(UserStateConstant.LOCK)) {
			j.setSuccess(false);
			j.setMsg("账号被锁 ，无法使用");
			return j;
		}
		if (user.getState().equals(UserStateConstant.DIE)) {
			j.setSuccess(false);
			j.setMsg("账号未激活 ，无法使用");
			return j;
		}
		if (!user.getState().equals(UserStateConstant.OK)) {
			j.setSuccess(false);
			j.setMsg("未知原因 ，无法使用");
			return j;
		}
		// 加密 的字符串 防止 用户知道自己的ID
		JSONObject json = new JSONObject();
		json.put("uid", user.getId());
		json.put("dietime", DateUtil.getDate());
		String encryptInfo = json.toString();
		encryptInfo = "encryptInfo=" + new DesUtils().encrypt(encryptInfo);
		String contemt = "<a href='" + SystemConstant.getSystemdomain() + "/common/login_do/resetpwd.do?" + encryptInfo
				+ "'>重置密码，有效期截止到当天晚上24：00</a>";
		try {
			SendEmailInter send = new SendQqmailImpl();
			send.sendMail(user.getEmail(), SystemConstant.getSystemName() + "-找回密码", contemt);
		} catch (Exception e) {
			e.printStackTrace();
			j.setMsg("发送邮箱失败，可能被提供方拦截");
			return j;
		}
		j.setMsg("发送邮箱成功，请到邮箱重置密码");
		return j;

	}

}
