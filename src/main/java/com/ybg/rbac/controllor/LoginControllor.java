package com.ybg.rbac.controllor;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ybg.base.jdbc.BaseMap;
import com.ybg.base.jdbc.util.DateUtil;
import com.ybg.base.util.DesUtils;
import com.ybg.base.util.Json;
import com.ybg.base.util.SystemConstant;
import com.ybg.base.util.VrifyCodeUtil;
import com.ybg.component.email.sendemail.SendEmailInter;
import com.ybg.component.email.sendemail.SendQqmailImpl;
import com.ybg.rbac.RbacConstant;
import com.ybg.rbac.resources.service.ResourcesService;
import com.ybg.rbac.support.controller.LoginProxyController;
import com.ybg.rbac.support.domain.Loginproxy;
import com.ybg.rbac.user.UserStateConstant;
import com.ybg.rbac.user.domain.UserDTO;
import com.ybg.rbac.user.qvo.UserQuery;
import com.ybg.rbac.user.service.LoginServiceImpl;
import com.ybg.rbac.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;

/**
 * @author Deament
 * 
 * @date 2016/9/31
 ***/
@Api(tags = "平台登录操作")
@Controller
public class LoginControllor {

	@Autowired
	UserService userService;
	@Autowired
	ResourcesService resourcesService;
	@Autowired
	LoginServiceImpl loginService;
	@Autowired
	AuthenticationManager authenticationManager;

	@ApiOperation(value = "登录页面", notes = "", produces = MediaType.TEXT_HTML_VALUE)
	@RequestMapping(value = { "/common/login_do/tologin.do", "/" }, method = { RequestMethod.GET, RequestMethod.POST })
	public String tologin(ModelMap map) {
		map.put("icp", SystemConstant.getICP());
		map.put("systemname", SystemConstant.getSystemName());
		map.put("systemdomain", SystemConstant.getSystemdomain());
		return "/login";
	}

	@ApiOperation(value = "备案，版权声明信息", notes = "", produces = MediaType.TEXT_HTML_VALUE)
	@ResponseBody
	@RequestMapping(value = { "/common/login_do/system_authinfo.do" }, method = { RequestMethod.GET,
			RequestMethod.POST })
	public String systemAuthinfo() {
		return "© 2016-2016 " + SystemConstant.getSystemdomain() + " 版权所有 ICP证：" + SystemConstant.getICP();
	}

	@ApiOperation(value = "退出系统 ", notes = "", produces = MediaType.TEXT_HTML_VALUE)
	@RequestMapping(value = "/common/login_do/loginout.do", method = RequestMethod.GET)
	public String loginout(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return "redirect:/common/login_do/tologin.do";
	}

	@ApiOperation(value = "登录系统 ", notes = "", produces = MediaType.ALL_VALUE)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "username", value = "帐号", dataType = "java.lang.String", required = true),
			@ApiImplicitParam(name = "password", value = "密码", dataType = "java.lang.String", required = true) })
	@ResponseBody
	@RequestMapping(value = "/common/login_do/login.do", method = { RequestMethod.GET, RequestMethod.POST })
	public Json login(String username, String password, HttpServletRequest httpServletRequest, ModelMap map)
			throws Exception {
		// 首先检测验证码
		Json json = new Json();
		json.setSuccess(false);
		if (!VrifyCodeUtil.checkvrifyCode(httpServletRequest, map)) {
			json.setMsg("验证码不正确");
			return json;
		}
		// String username = ServletUtil.getStringParamDefaultBlank(httpServletRequest,
		// "username");
		// String password = ServletUtil.getStringParamDefaultBlank(httpServletRequest,
		// "password");
		Loginproxy proxy = LoginProxyController.login(httpServletRequest, username, password, null);
		if (proxy.isSuccess()) {
			json.setSuccess(true);
			json.setMsg("登录成功");
			return json;
		} else {
			json.setMsg(proxy.getResult());
		}
		return json;
	}

	@ApiOperation(value = "无权限提示页面 ", notes = "", produces = MediaType.TEXT_HTML_VALUE)
	@RequestMapping(value = { "/common/login_do/unauthorizedUrl.do" }, method = { RequestMethod.GET,
			RequestMethod.POST })
	public String unauthorizedUrl() throws Exception {
		return "/denied";
	}

	/**
	 * 注册
	 *
	 * @throws Exception
	 **/
	@ApiOperation(value = "注册", notes = " ", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@RequestMapping(value = "/common/login_do/register.do", method = { RequestMethod.GET, RequestMethod.POST })
	public Json register(UserDTO user, @RequestParam(name = "email", required = true) String email,
			@RequestParam(name = VrifyCodeUtil.PARAMETERNAME, required = true) String vrifyCode, HttpSession session)
			throws Exception {

		Json j = new Json();
		if (!VrifyCodeUtil.checkvrifyCode(vrifyCode, session)) {
			j.setSuccess(false);
			j.setMsg("验证码不正确！");
			return j;
		}
		return userService.registerByEmail(user, email);
	}

	@ApiOperation(value = "激活邮箱页面", notes = "", produces = MediaType.TEXT_HTML_VALUE)
	@RequestMapping(value = "/common/login_do/relife.do", method = RequestMethod.GET)
	public String relife(@RequestParam(name = "username", required = true) String username,
			@RequestParam(name = "salt", required = true) String salt, ModelMap map) throws Exception {

		Json j = userService.reLife(username, salt);
		map.put("error", j.getMsg());
		return "/login";
	}

	// /** 忘记密码 **/
	@ApiOperation(value = "忘记密码", notes = " ", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "username", value = "帐号", dataType = "java.lang.String", required = true),
			@ApiImplicitParam(name = "password", value = "密码", dataType = "java.lang.String", required = true) })
	@ResponseBody
	@RequestMapping(value = "/common/login_do/forgetPwd.do", method = RequestMethod.POST)
	public Json forgetPwd(@RequestParam(name = "username", required = true) String username,
			@RequestParam(name = VrifyCodeUtil.PARAMETERNAME, required = true) String vrifyCode, HttpSession session)
			throws Exception {
		Json j = new Json();
		// 首先检测验证码
		if (!VrifyCodeUtil.checkvrifyCode(vrifyCode, session)) {
			j.setSuccess(false);
			j.setMsg("验证码不正确");
			return j;
		}
		return userService.forgetPwd(username);
	}

	// /** 重置密码初始化 **/
	@ApiOperation("重置密码页面")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "encryptInfo", value = "加密信息", dataType = "java.lang.String", required = true),
			@ApiImplicitParam(name = "password", value = "密码", dataType = "java.lang.String", required = true) })
	@RequestMapping(value = "/common/login_do/resetpwd.do", method = RequestMethod.GET)
	public String resetpwd(@RequestParam(name = "encryptInfo", required = true) String encryptInfo, Model model) {
		try {
			JSONObject json = JSONObject.fromObject(new DesUtils().decrypt(encryptInfo));
			String userid = json.getString("uid");
			String dietime = json.getString("dietime");
			if (dietime.equals(DateUtil.getDate())) {
				UserDTO user = userService.get(userid);
				if (user.getState().equals(UserStateConstant.LOCK)) {
					return "/lock";
				}
				if (user.getState().equals(UserStateConstant.DIE)) {
					return "/die";
				}
				if (!user.getState().equals(UserStateConstant.OK)) {
					return "";
				}
				model.addAttribute("encryptInfo", encryptInfo);
				return "/reset";
			}
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("msg", "加密信息错误");
			return "/fail";
		}
		model.addAttribute("msg", "该链接已过期");
		return "/fail";
	}

	// /** 重置密码 **/
	@ApiOperation(value = "重置密码", notes = " ", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "encryptInfo", value = "加密信息", dataType = "java.lang.String", required = true),
			@ApiImplicitParam(name = "password", value = "密码", dataType = "java.lang.String", required = true) })
	@ResponseBody
	@RequestMapping(value = "/common/login_do/resetpassword.do", method = { RequestMethod.GET, RequestMethod.POST })
	public Json resetpassword(@RequestParam(name = "encryptInfo", required = true) String encryptInfo,
			@RequestParam(name = "password", required = true) String password) throws Exception {
		return userService.resetPassword(encryptInfo, password);
	}

	/** 检测账号是否存在 **/
	@ApiOperation(value = " 检测账号是否存在", notes = " ", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@RequestMapping(value = { "/common/login_do/isexist.do" }, method = { RequestMethod.GET, RequestMethod.POST })
	public boolean isexist(UserQuery qvo) {
		return userService.checkisExist(qvo);
	}

	/**
	 * 修改密码
	 * 
	 * @throws Exception
	 **/
	@ApiOperation(value = "修改密码", notes = " ", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@RequestMapping(value = { "/common/login_do/modifypwd.do" }, method = { RequestMethod.GET, RequestMethod.POST })
	public Json modifypwd(@AuthenticationPrincipal UserDTO user,
			@RequestParam(name = VrifyCodeUtil.PARAMETERNAME, required = true) String vrifyCode,
			HttpServletRequest httpServletRequest, @RequestParam(name = "password", required = true) String password,
			@RequestParam(name = "newPassword", required = true) String newPassword) throws Exception {
		Json j = new Json();
		// 首先检测验证码
		if (!VrifyCodeUtil.checkvrifyCode(vrifyCode, httpServletRequest)) {
			j.setSuccess(false);
			j.setMsg("验证码不正确");
			return j;
		}
		return userService.updatePassword(user, password, newPassword);
	}

	/**
	 * 清除过期没有激活的用户<br>
	 * // @Scheduled(cron = "1 * * * * ? ") 1分钟一次
	 * 
	 * @throws Exception
	 **/
	@Scheduled(cron = "0 0 */6 * * ?")
	public void cleanuser() throws Exception {
		// XXX 好像还有点问题
		userService.removeExpired();
	}

	/** 个人账号设置首页 **/
	@RequestMapping(value = { "/common/accountsetting.do" }, method = { RequestMethod.GET, RequestMethod.POST })
	public String accountsetting() {
		return "/index/account";
	}

}
