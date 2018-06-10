package com.security.rsa;

import java.security.PrivateKey;
import java.util.Locale;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.security.util.RSAUtil;
import com.security.vo.RSA;
import com.security.vo.UserInfo;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	RSAUtil rsaUtil = new RSAUtil();
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		return "home";
	}
	
	@RequestMapping("/loginForm")
	public String loginForm(HttpSession session, Model model) {
		// RSA 키 생성
	    PrivateKey key = (PrivateKey) session.getAttribute("RSAprivateKey");
	    if (key != null) { // 기존 key 파기
	    	session.removeAttribute("RSAprivateKey");
	    }
	    
	    RSA rsa = rsaUtil.createRSA();
	    model.addAttribute("modulus", rsa.getModulus());
	    model.addAttribute("exponent", rsa.getExponent());
	    session.setAttribute("RSAprivateKey", rsa.getPrivateKey());
		return "loginForm";
	}

	@RequestMapping(value="/loginPro", method=RequestMethod.POST)
	public String loginPro(HttpSession session, Model model, UserInfo userInfo, RedirectAttributes ra) {
		// 개인키 취득
	    PrivateKey key = (PrivateKey) session.getAttribute("RSAprivateKey");
	    if (key == null) {
	        ra.addFlashAttribute("resultMsg", "비정상 적인 접근 입니다.");
	        return "redirect:/loginForm";
	    }
	 
	    // session에 저장된 개인키 초기화
	    session.removeAttribute("RSAprivateKey");
	 
	    // 아이디/비밀번호 복호화
	    try {
	    		logger.info(userInfo.toString());
	        String email = rsaUtil.getDecryptText(key, userInfo.getEmail());
	        String password = rsaUtil.getDecryptText(key, userInfo.getPassword());
	        logger.info("email : " + email + " / password : " + password);
	        
	        // 복호화된 평문을 재설정
	        userInfo.setEmail(email);
	        userInfo.setPassword(password);
	        logger.info(userInfo.toString());
	        
	    } catch (Exception e) {
	        ra.addFlashAttribute("resultMsg", "비정상 적인 접근 입니다.");
	        return "redirect:/loginForm";
	    }

		return "redirect:/home";
	}
}