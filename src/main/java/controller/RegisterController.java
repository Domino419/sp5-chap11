package controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import spring.DuplicateMemberException;
import spring.MemberRegisterService;
import spring.RegisterRequest;

/**
 * class         : RegisterController
 * date          : 24-12-17
 * description   : 회원 등록 기능을 처리하는 컨트롤러 클래스.
 *                 각 요청에 대해 회원 등록 단계별 처리 및 예외 처리를 담당.
 */
@Controller
public class RegisterController {

	private static final Logger log = LoggerFactory.getLogger(RegisterController.class);
	private MemberRegisterService memberRegisterService;

	/**
	 * method        : setMemberRegisterService
	 * date          : 24-12-17
	 * param         : MemberRegisterService memberRegisterService - 회원 등록 서비스를 주입받는 객체
	 * return        : void
	 * description   : MemberRegisterService를 주입받아 해당 서비스의 기능을 사용하도록 설정.
	 */

	public void setMemberRegisterService(
			MemberRegisterService memberRegisterService) {
		this.memberRegisterService = memberRegisterService;
	}

	/**
	 * method        : handleStep1
	 * date          : 24-12-17
	 * param         : none
	 * return        : String - "register/step1" JSP 페이지 반환
	 * description   : 회원가입 첫 번째 단계 페이지로 이동.
	 */
	@RequestMapping("/register/step1")
	public String handleStep1() {
		return "register/step1";
	}

	/**
	 * method        : handleStep2
	 * date          : 24-12-17
	 * param         : Boolean agree - 약관 동의 여부
	 *               : Model model - 뷰에 전달할 데이터를 저장하는 객체
	 * return        : String - 다음 단계 페이지 또는 현재 단계 페이지 반환
	 * description   : 약관 동의 여부를 확인하고, 동의 시 회원가입 두 번째 단계로 이동.
	 *                 동의하지 않을 경우 첫 번째 단계로 돌아감.
	 *                 http://localhost:8090/register/step1
	 */
	@PostMapping("/register/step2")
	public String handleStep2(
			@RequestParam(value = "agree", defaultValue = "false") Boolean agree,
			Model model) {
		if (!agree) {
			return "register/step1";  // 약관 미동의 시 다시 첫 번째 단계로 이동
		}
		model.addAttribute("registerRequest", new RegisterRequest());
		return "register/step2";      // 동의 시 두 번째 단계로 이동
	}

	/**
	 * method        : handleStep2Get
	 * date          : 24-12-17
	 * param         : none
	 * return        : String - 리다이렉트 URL 반환
	 * description   : GET 방식으로 두 번째 단계에 접근했을 때 첫 번째 단계로 리다이렉트.
	 */
	@GetMapping("/register/step2")
	public String handleStep2Get() {
		return "redirect:/register/step1";
	}

	/**
	 * method        : handleStep3
	 * date          : 24-12-17
	 * param         : RegisterRequest regReq - 회원 정보를 담는 객체
	 * return        : String - 성공 시 세 번째 단계 페이지 반환, 실패 시 두 번째 단계 페이지 반환
	 * description   : 회원 정보를 등록하고 중복 예외 발생 시 처리.
	 */
	@PostMapping("/register/step3")
	public String handleStep3(RegisterRequest regReq) {
		try {
			memberRegisterService.regist(regReq);
			return "register/step3";
		} catch (DuplicateMemberException ex) {
			return "register/step2";
		}
	}

	/**
	 * method        : test
	 * date          : 24-12-17
	 * param         : none
	 * return        : void
	 * description   : 테스트용 메소드. 로그에 "test" 메시지를 출력.
	 * http://localhost:8090/test
	 */
	@GetMapping("/test")
	public void  test() {
		log.info("test");
	}


}
