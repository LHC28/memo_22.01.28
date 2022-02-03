package com.memo.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.memo.common.EncryptUtils;
import com.memo.user.bo.UserBO;

@RequestMapping("/user")
@Controller
public class UserController {

	@Autowired
	private UserBO userBO;
	
	@RequestMapping("/sign_up_view")
	public String signUpView(
			Model model
			) {
		model.addAttribute("viewName", "user/sign_up");
		
		return "template/layout";
	}
	
	@RequestMapping("/sign_up_for_submit")
	public String signUpForSubmit(
			@RequestParam("loginId") String loginId
			,@RequestParam("password") String password
			,@RequestParam("name") String name
			,@RequestParam("email") String email
			) {
		// hashing
		String encryptPassword = EncryptUtils.md5(password);
		
		// DB에 저장
		userBO.addUser(loginId, encryptPassword, name, email);
		
		return "redirect:/user/sign_in_view";
	}
	
	// 로그인 화면
	@RequestMapping("/sign_in_view")
	public String signInView(
			Model model
			) {
		model.addAttribute("viewName", "user/sign_in");
		return "template/layout";
	}
	
	@RequestMapping("/sign_out")
	public String signOut(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.removeAttribute("userLoginId");
		session.removeAttribute("userName");
		session.removeAttribute("userId");
		return "redirect:/user/sign_in_view";
	}
}
