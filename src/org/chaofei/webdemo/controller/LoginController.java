package org.chaofei.webdemo.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.chaofei.webdemo.constant.Constant;
import org.chaofei.webdemo.entity.UserInfo;
import org.chaofei.webdemo.service.UserInfoService;
import org.chaofei.webdemo.util.AESUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("passport")
public class LoginController {
	@Resource(name="userInfoService")
	UserInfoService userInfoService;
	
	@ResponseBody
	@RequestMapping(value="login")
	public String login(@RequestParam(value="account", required = true) String account,
			@RequestParam(value="password", required = true) String password,
			HttpServletRequest request) {
		UserInfo userInfo = userInfoService.queryUserInfoByUserName(account);
		String loginResult = "";
		HttpSession session = request.getSession();
		AESUtil aesUtil = AESUtil.getInstance();
		String encryptPassword = aesUtil.encrypt(password);
		if (null != userInfo && encryptPassword.equals(userInfo.getPassword())) {
			loginResult = Constant.SUCCESS;
			session.setAttribute("usertype", userInfo.getUserType());
			session.setAttribute("account", userInfo.getUserName());
		} else {
			loginResult = Constant.FAILED;
		}
		return loginResult;
	}
	
	@RequestMapping(value="exist")
	public void exist(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession();
			session.removeAttribute("account");
			response.sendRedirect("/webdemo");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}