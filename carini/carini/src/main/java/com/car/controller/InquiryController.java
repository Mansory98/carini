package com.car.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.car.dto.Inquiry;
import com.car.dto.Member;
import com.car.service.InquiryService;
import com.car.validation.BoardUpdateFormValidation;
import com.car.validation.InquiryWriteValidation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@SessionAttributes({"user", "pagingInfo"})
@RequestMapping("/inquiry")
public class InquiryController {

	@Autowired
	private InquiryService inquiryService;
	
	/*
	 * 문의함 리스트
	 * */
	@GetMapping("/inquiryList")
	public ResponseEntity<Map<String, Object>> inquiryList(HttpSession session,Model model,HttpServletRequest request) {
		
		Member user = (Member) session.getAttribute("user");
		
		Map<String, Object> response = new HashMap<>();
		if( user == null ) { 
			response.put("message", "로그인후 이용 가능합니다.");
			response.put("success", false);
			response.put("redirectUrl", "/member_login");
	        return ResponseEntity.ok(response);
	     }
		
		List<Inquiry> inquirys = inquiryService.findbyIdinquiry(user);
		System.out.println(inquirys.get(1).getReContentRq());
		response.put("success", true);
		response.put("inquirys", inquirys);
		
		return ResponseEntity.ok(response);
	}
	
	/*
	 * 문의함 상세보기
	 * */
	@GetMapping("/getinquiry")
	public ResponseEntity<Map<String, Object>> getinquiry(@ModelAttribute Inquiry inquiry,HttpServletRequest request,Model model) {
		Map<String, Object> response = new HashMap<>();
		Inquiry getinquiry = inquiryService.findbyreIdinquiry(inquiry.getReId());

		if(getinquiry==null) {
			response.put("message", "게시글이 존재하지 않습니다.");
			response.put("success", false);
	        return ResponseEntity.ok(response);

		}
		if(!getinquiry.isReCheckRq()) {
			getinquiry.setReDateRq(null);
			getinquiry.getReDateRq();
		}
		response.put("inquiry", getinquiry);
		response.put("success", true);
        return ResponseEntity.ok(response);
	}
	
	/*
	 * 문의작성
	 * */
	@PostMapping("/inquirywrite")
	public ResponseEntity<Map<String, Object>> inquirywrite(@ModelAttribute Inquiry inquiry,
			@Validated @ModelAttribute("InquiryWriteValidation") InquiryWriteValidation InquiryValidation,BindingResult bindingResult ,
			HttpSession session,HttpServletRequest request,
			Model model) {
		Map<String, Object> response = new HashMap<>();
		 if (bindingResult.hasErrors()) {
			 List<ObjectError> errors = bindingResult.getAllErrors();
			 response.put("errors", errors);
			 return ResponseEntity.ok(response);
		}

		Member user = (Member) session.getAttribute("user");
		inquiry.setMemberId(user.getMemberId());
		inquiry.setReHero(user.getMemberNickname());
		inquiry.setReCheckRq(false);
		inquiryService.inquiryWrite(inquiry);
		System.out.println(session.getAttribute("originalUrl"));
		response.put("redirectUrl", session.getAttribute("originalUrl"));
		response.put("message", "정상적으로 작성되었습니다");
		response.put("success", true);
		return ResponseEntity.ok(response); 
	}
	
	/*
	 * 문의함 삭제
	 * */
	@PostMapping("/inquirydelete")
	public ResponseEntity<Map<String, Object>> inquirydelete(@ModelAttribute Inquiry inquiry,HttpSession session,HttpServletRequest request) {
		Member user = (Member) session.getAttribute("user");

		
		
		Map<String, Object> response = new HashMap<>();
		if( user == null ) { 
			response.put("message", "로그인후 이용 가능합니다.");
			response.put("success", false);
			response.put("redirectUrl", "/member_login");
	        return ResponseEntity.ok(response);
	     }
		inquiryService.inquirydelte(inquiry);
		response.put("message","성공적으로 삭제 되었습니다." );
		response.put("redirectUrl", session.getAttribute("originalUrl"));
		
		return ResponseEntity.ok(response);
	}
}
