package com.car.controller;

import java.text.NumberFormat;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.car.dto.Board;
import com.car.dto.Bookmark;
import com.car.dto.Car;
import com.car.dto.Member;
import com.car.service.MemberService;
import com.car.service.BoardService;
import com.car.service.BookMarkService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/mypage")
@SessionAttributes("member")
public class MypageController {

	@Value("${pw-role.password-rejex}")
	private String passwordRegex;
	
	
    @Autowired
    private BookMarkService bookMarkService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private BoardService boardService;
    
	@ModelAttribute("member")
	public Member setMember() {
		return new Member(); // 기본 Member 객체를 세션에 저장
	}
	
	
    @GetMapping("/")
    public String backhome() {
        return "index.html";
    }
    
    @GetMapping("/no_login")
    public String mypageNo_login(Model model) {
    	
    	 return "member/login.html";
    }
    
    /*
     * 회원정보 수정(나의 정보)
     */
    @GetMapping("/myinfo")
    public String myinfoview(@ModelAttribute("member") Member members, Model model) {

        Member member = memberService.findByMemberId(members.getMemberId());
        if (member.getMemberSocial().equals("kakao") || member.getMemberSocial().equals("naver")) {
            return "mypage/myinfo.social.html";
        } else {
            return "mypage/myinfo.html";
        }
    }
   
    /*
     * 닉네임 수정
     */
    @PostMapping("/myinfo/updatenickname")
    public String myInfoNicknameUpdate(@ModelAttribute("member") Member members,
                                       @RequestParam("memberNickname") String memberNickname,
                                       Model model) {
        Member member = memberService.findByMemberId(members.getMemberId());
        List<Member> memberList = memberService.findAllMember();
        
        for(Member memberOne : memberList) {
        	
        	if (member != null && !memberOne.getMemberNickname().equals(memberNickname)) {
        		model.addAttribute("msg", "중복된 닉네임입니다. 다시 작성해주세요");
                model.addAttribute("url", "/mypage/myinfo/");
                return "alert"; 
        		
            }
        }
    	memberService.updateMember(member, memberNickname);
        model.addAttribute("msg", "닉네임이 성공적으로 변경되었습니다! 확인해주세요.");
        model.addAttribute("url", "/mypage/myinfo/");
        return "alert";  
        
    }
    
    /*
     * 회원정보 모두 수정
     */
    @PostMapping("/myinfo/updateAll")
    public String myInfoUpdateAll(@ModelAttribute("member") Member members,
                                  @RequestParam("memberPw") String memberPw,
                                  @RequestParam("memberName") String memberName,
                                  @RequestParam("memberEmail") String memberEmail,
                                  @RequestParam("memberPhoneNum") String memberPhoneNum,
                                  Model model) {
    	
    	final Pattern PASSWORD_PATTERN = Pattern.compile(passwordRegex);
    	List<Member> memberList = memberService.findAllMember();
        Member member = memberService.findByMemberId(members.getMemberId());
        if (member != null ) {
        	if(!PASSWORD_PATTERN.matcher(member.getMemberPw()).matches()) {
        		model.addAttribute("msg", "비밀번호는 8~16자의 영문 대/소문자, 숫자, 특수문자를 사용해야 합니다.");
                model.addAttribute("url", "/mypage/myinfo/" +  members.getMemberId());
                return "alert";  
        	}
        	
        	for(Member memberOne : memberList) {
        		
        		if (!memberOne.getMemberEmail().equals(memberEmail)) {
            		model.addAttribute("msg", "중복된 이메일입니다. 다시 작성해주세요");
                    model.addAttribute("url", "/mypage/myinfo/");
                    return "alert"; 
                }
        	 }
            member.setMemberPw(memberPw);
            member.setMemberName(memberName);
            
            member.setMemberEmail(memberEmail);
            member.setMemberPhoneNum(memberPhoneNum);
            
            memberService.updateAllMember(members.getMemberId(), member);
            model.addAttribute("msg", "회원정보가 성공적으로 변경되었습니다! 확인해주세요.");
            model.addAttribute("url", "/mypage/myinfo/");
            return "alert";  
        } else {
            return "redirect:/mypage/myinfo/";
        }
    }
    
    /*
     * 회원탈퇴
     */
    @PostMapping("/myinfo/delete")
    public String myInfoDeletePwCheck(@ModelAttribute("member") Member members,
                                      @RequestParam("memberPw") String memberPw,
                                      Model model) {
        Member member = memberService.findByMemberId(members.getMemberId());
        if (member != null && member.getMemberPw().equals(memberPw)) {
            memberService.deleteMember(member);
            model.addAttribute("msg", "회원탈퇴가 완료되었습니다.");
            model.addAttribute("url", "/");
            return "alert";  
        } else {
            model.addAttribute("msg", "비밀번호가 일치하지 않습니다. 확인해주세요.");
            model.addAttribute("url", "/mypage/myinfo/");
            return "alert";
        }
    }
    
    
    /*===================================
     * 즐겨찾기
     */
    @GetMapping("/bookmark")
    public String myPagebookmarkList(@ModelAttribute("member") Member members, Model model) {

        List<Bookmark> bookmarkCarID = bookMarkService.findAllBookmarkCar(members.getMemberId());
        
        System.out.println(bookmarkCarID);
        List<Car> bookmarkCarList = bookMarkService.findAllCar(bookmarkCarID);
        for(Car car: bookmarkCarList) {
        	car.setCar_max_price(NumberFormat.getInstance().format(Long.parseLong(car.getCar_max_price().replace(" ", ""))));
        	car.setCar_max_price(car.getCar_max_price()+"만원");
        	car.setCar_min_price(NumberFormat.getInstance().format(Long.parseLong(car.getCar_min_price().replace(" ", ""))));

        	car.setCar_min_price(car.getCar_min_price()+"만원");
        }
        
        model.addAttribute("BookmarkCarList", bookmarkCarList);
        return "mypage/bookmark.html";
    }
    

    /*
     * 즐겨찾기 추가
     * */
    @PostMapping("/bookmark/{carId}")
    public String myPagebookmarkAdd(@PathVariable("carId") String carId, @ModelAttribute("member") Member members,Model model,Bookmark bookmark) {
      
    	
    	bookmark.setCarId(Integer.parseInt(carId));
    	bookmark.setMemberId(members.getMemberId());
    	Bookmark save_bookmark = bookMarkService.insertMember(bookmark);
        model.addAttribute("msg", "즐겨찾기가 추가되었습니다..");
        model.addAttribute("url", "/mypage/getbookmark/" + carId); 
        return "alert";
    }

    /*
     * bookmark 삭제
     */
    @PostMapping("/bookmark/delete/{carId}")
    public String myPagebookmarkdelete(@PathVariable("carId") String carId,@ModelAttribute("member") Member members, Model model) {
    	System.out.println(carId);
        bookMarkService.findBookmarkByCarDelete(Integer.parseInt(carId), members.getMemberId());
        model.addAttribute("msg", "즐겨찾기가 삭제되었습니다.");
        model.addAttribute("url", "/mypage/bookmark");
        return "alert";  
    }
    
    /*
     * 
     * */
    
    @GetMapping("/getbookmark/{carId}")
    public String myPagegetbookmark(@PathVariable("carId") int carId, Model model) {
       
    	Car car = bookMarkService.selectCar(carId);
    	
    	model.addAttribute("car", car);
        return null;
    	// return "alert"; <----- 수정해야함 자동차 상세보기 페이지경로  
    }
    
    /*===================================
     * 나의 게시물
     */
    @GetMapping("/myBoard")
    public String myPageMyboard(@ModelAttribute("member") Member members, Model model) {
        Member member = memberService.findByMemberId( members.getMemberId());
        List<Board> boards = boardService.boardList(member);
        model.addAttribute("boards", boards);
        return "mypage/myboard.html";
    }
    
    /*
     * 내 게시물 상세 조회
     */
    @PostMapping("/myBoard/{boardId}")
    public String myPagemyboard(@PathVariable("boardId") Long boardId, Model model) {
        // 추가 로직이 필요할 경우 작성
    	Board board = boardService.selectBoard(boardId);
    	
    	model.addAttribute("board", board);
    	return null;
      //  return "alert";   <----- 수정해야함 글 상세보기 페이지경로  
    }
}