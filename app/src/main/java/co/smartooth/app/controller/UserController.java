package co.smartooth.app.controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import co.smartooth.app.service.MailAuthService;
import co.smartooth.app.service.TeethService;
import co.smartooth.app.service.UserService;
import co.smartooth.app.utils.AES256Util;
import co.smartooth.app.utils.JwtTokenUtil;
import co.smartooth.app.vo.TeethInfoVO;
import co.smartooth.app.vo.UserVO;

/**
 * 작성자 : 정주현 
 * 작성일 : 2022. 04. 28
 * 수정일 : 2022. 08. 03
 * @RestController를 쓰지 않는 이유는 몇 안되는 mapping에 jsp를 반환해줘야하는게 있으므로 @Controller를 사용한다.
 * @RestAPI로 반환해야할 경우 @ResponseBody를 사용하여 HashMap으로 return 해준다.
 */
@Controller
public class UserController {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	// 인증 여부 확인 flag
	private static boolean tokenValidation = false; 

	@Autowired(required = false)
	private UserService userService;
	
	@Autowired(required = false)
	private TeethService teethService;
	
	@Autowired(required = false)
	private MailAuthService mailAuthService;
	
	
	
	/**
	 * 기능   : 회원 ID 중복 확인
	 * 작성자 : 정주현 
	 * 작성일 : 2022. 05. 10
	 */
	@PostMapping(value = {"/app/user/duplicateChkId.do"})
	@ResponseBody
	public HashMap<String,Object> duplicateChkId(@RequestBody HashMap<String, String> paramMap){

		logger.debug("========== UserController ========== duplicateChkId.do ==========");
		
		HashMap<String,Object> hm = new HashMap<String,Object>();
		int isExistRow = 0;

		String userId = (String)paramMap.get("userId");
		// Parameter = userId 값 검증 (Null 체크 및 공백 체크)
		if(userId == null || userId.equals("")) {
			hm.put("code", "401");
			hm.put("msg", "This ID does not exist.");
			return hm;
		}

		try {
			// 회원 아이디 중복 체크 :: 회원 테이블에 값이 존재하는지 여부 확인
			isExistRow =  userService.duplicateChkId(userId);
			if(isExistRow == 0) { // 아이디가 없을 경우 0
				hm.put("code", "000");
				hm.put("msg", "This ID is an available.");
			}else { // 등록 되어 있는 아이디가 있을 경우 1
				hm.put("code", "402");
				hm.put("msg", "This ID is already registered.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			hm.put("code", "500");
			hm.put("msg", "Server Error");
		}
		
		return hm;
	}
	
	
	
	/**
	 * 기능   : 앱을 통한 회원가입 API
	 *			 앱으로 가입을 하는 것이므로 USER_NO 생성은 일반 유저만 생성이 가능 
	 * 			 JSON 형식의 값을 받아 Database 에 UPDATE
	 * 작성자 : 정주현 
	 * 작성일 : 2022. 05. 10
	 * 수정일 : 2022. 08. 01
	 */
	@PostMapping(value = {"/app/user/register.do"})
	@ResponseBody
	public HashMap<String,Object> registerUser(@RequestBody HashMap<String, Object> paramMap) throws Exception{
		
		logger.debug("========== UserController ========== register.do ==========");
		
		// 회원 번호
		String userSeqNo = null;
		//회원 ID
		String userId = null;
		// 회원 비밀번호
		String userPwd = null;
		// 회원 이름
		String userType = null;
		// 회원 생일
		String userNm = null;
		// 회원 타입 (개인, 단체, 그룹)
		String userBirthday = null;
		// 회원 거주 - 국
		String userCountry = null;
		// 회원 거주 - 주
		String userState = null;
		// 회원 상세 주소
		String userAddress = null;
		// 회원 전화번호
		String userTelNo = null;
		// 회원 성별
		String userSex = null;
		// 푸쉬토큰
		String pushToken = null;
		// 회원 치아 상태
		String teethStatus = null;
		
		int seqNo = 0;
		int seqIntStr = 0;
		char seqStr = '\u0000';
		
		HashMap<String,Object> hm = new HashMap<String,Object>();
		TeethInfoVO teethInfoVO = new TeethInfoVO();
		UserVO userVO = new UserVO();
		
		try {
			
			userPwd = (String)paramMap.get("userPwd");
			// 비밀번호 암호화
			AES256Util aes256Util = new AES256Util();
			userPwd = aes256Util.aesEncode(userPwd);
			// 회원 구분
			userType = (String)paramMap.get("userType");
			// 회원 거주 국
			userCountry = (String)paramMap.get("userCountry");
			// 회원 거주 주
			userState = (String)paramMap.get("userState");
			
			userVO.setUserType(userType);
			
			// 회원 번호 (회원 번호 생성 전 SEQ_NO) 조회 후 생성
			seqNo = userService.selectUserSeqNo(userType);
			seqIntStr = userService.selectUserSeqStr();
			seqStr = (char)seqIntStr;
			

			// 시퀀스가 1,000,000을 넘어갈 경우 0으로 변경 후 앞자리 알파벳을 변경함
			if(seqNo==1000000) {
				seqIntStr++;
				seqNo = 0;
			}else {
				seqNo++;
			}
			
			userVO.setSeqNo(seqNo);
			// 회원 회원번호 생성 및 등록
			// GR : 개인, ST : 학생, PA : 부모, TC : 선생, CO : 코디, MA : 매니저
			
			if(userState == null || userState.equals("")) {
				// KR-GR-A0000001
				// 알파벳 증가식을 넣어줘야함
				userSeqNo = userCountry+"-"+userType+"-"+seqStr+String.format("%07d", seqNo);
			}
			
			// 회원 ID
			userId = (String)paramMap.get("userId");
			// 회원 이름
			userNm = (String)paramMap.get("userNm");
			// 회원 생일
			userBirthday = (String)paramMap.get("userBirthday");
			// 회원 상세주소
			userAddress = (String)paramMap.get("userAddress");
			// 회원 전화번호
			userTelNo = (String)paramMap.get("userTelNo");
			// 회원 성별
			userSex = (String)paramMap.get("userSex");
			// 푸쉬토큰
			pushToken = (String)paramMap.get("pushToken");
			// 치아 상태
			teethStatus = (String)paramMap.get("teethStatus");
			
			userVO.setUserId(userId);
			userVO.setUserNo(userSeqNo);
			userVO.setUserPwd(userPwd);
			userVO.setUserNm(userNm);
			userVO.setUserType(userType);
			userVO.setUserBirthday(userBirthday);
			userVO.setUserCountry(userCountry);
			userVO.setUserState(userState);
			userVO.setUserAddress(userAddress);
			userVO.setUserTelNo(userTelNo);
			userVO.setUserSex(userSex);
			userVO.setPushToken(pushToken);
			
			teethInfoVO.setUserId(userId);
			teethInfoVO.setUserNo(userSeqNo);
			teethInfoVO.setTeethStatus(teethStatus); 
			
			// 일반 회원 등록 (회원가입)
			userService.insertUserInfo(userVO);
			// 일반 회원 치아 상태 등록
			teethService.insertUserTeethInfo(teethInfoVO);
			// 일반 회원 번호 (일반 회원 번호 생성 후 SEQ_NO) 업데이트 
			userService.updateUserSeqNo(userType ,seqNo);
			userService.updateUserSeqStr(seqIntStr);
			
			hm.put("code", "000");
			hm.put("msg", "Success");
			
		} catch (Exception e) {
			
			hm.put("code", "500");
			hm.put("msg", "Server Error");
			e.printStackTrace();
			
		}
		return hm;
	}
	
	
	
	
	/**
	 * 기능   : 회원 정보(개인정보) 조회
	 * 작성자 : 정주현 
	 * 작성일 : 2022. 05. 25
	 */
	@PostMapping(value = {"/app/user/selectUserInfo.do"})
	@ResponseBody
	public HashMap<String,Object> selectUserInfo(@RequestBody HashMap<String, Object> paramMap, HttpServletRequest request) throws Exception {
		
		logger.debug("========== UserController ========== selectUserInfo.do ==========");
		
		String userId = null;
		String userNo = null;
		String userAuthToken = null;
		
		HashMap<String,Object> hm = new HashMap<String,Object>();
		List<UserVO> userInfo = new ArrayList<UserVO>();
		UserVO userVO = new UserVO();
		
		userId = (String)paramMap.get("userId");
		
		
		// Parameter = userId 값 검증 (Null 체크 및 공백 체크)
		userId= (String)paramMap.get("userId");
		if(userId == null || userId.equals("") || userId.equals(" ")) {
			hm.put("code", "401");
			hm.put("msg", "This ID does not exist.");
			return hm;
		}
		userNo = (String)paramMap.get("userNo");
		userAuthToken = request.getHeader("Authorization");
		// TOKEN 검증
		JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
		tokenValidation = jwtTokenUtil.validateToken(userAuthToken);
		
		if(tokenValidation) {
			try {
				userVO.setUserId(userId);
				userVO.setUserNo(userNo);
				// 회원 정보 조회
				userInfo = userService.selectUserInfo(userVO);
				hm.put("userInfo", userInfo);
			} catch (Exception e) {
				hm.put("code", "500");
				hm.put("msg", "Server Error");
				e.printStackTrace();
			}
		}else {
			hm.put("code", "400");
			hm.put("msg", "The token is not valid.");
		}
		return hm;
	}
	
	
	
	/**
	 * 기능   : 회원 정보(개인정보) 수정
	 * 작성자 : 정주현 
	 * 작성일 : 2022. 05. 30
	 */
	@PostMapping(value = {"/app/user/updateUserInfo.do"})
	@ResponseBody
	public HashMap<String,Object> updateUserInfo(@RequestBody HashMap<String, Object> paramMap, HttpServletRequest request) throws Exception {
		
		logger.debug("========== UserController ========== updateUserInfo.do ==========");
		
		String userId = null;
		String userNo = null;
		String userAuthToken = null;

		HashMap<String,Object> hm = new HashMap<String,Object>();
		UserVO userVO = new UserVO();
		
		userId = (String)paramMap.get("userId");
		
		// Parameter = userId 값 검증 (Null 체크 및 공백 체크)
		userId= (String)paramMap.get("userId");
		if(userId == null || userId.equals("") || userId.equals(" ")) {
			hm.put("code", "401");
			hm.put("msg", "This ID does not exist.");
			return hm;
		}
		
		userAuthToken = request.getHeader("Authorization");
		// TOKEN 검증
		JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
		tokenValidation = jwtTokenUtil.validateToken(userAuthToken);
		
		if (tokenValidation) {
			try {
				
				userVO.setUserId(userId);
				userVO.setUserNo(userNo);
				userVO.setUserNm((String)paramMap.get("userNm"));
				userVO.setUserBirthday((String)paramMap.get("userBirthday"));
				userVO.setUserCountry((String)paramMap.get("userCountry"));
				userVO.setUserState((String)paramMap.get("userState"));
				userVO.setUserAddress((String)paramMap.get("userAddress"));
				userVO.setUserTelNo((String)paramMap.get("userTelNo"));
				userService.updateUserInfo(userVO);
				
			} catch (Exception e) {
				hm.put("code", "500");
				hm.put("msg", "Server Error");
				e.printStackTrace();
			}
		}else {
			hm.put("code", "400");
			hm.put("msg", "The token is not valid.");
		}
		return hm;
	}
	
	
	
	
	/**
	 * 기능   : 비밀번호 찾기 (이메일 발송)
	 * 작성자 : 정주현 
	 * 작성일 : 2022. 06. 09
	 */
	@PostMapping(value = {"/app/user/findUserPwd.do"})
	@ResponseBody
		public HashMap<String,Object> findUserPwd(@RequestBody HashMap<String, String> paramMap) {
		
		logger.debug("========== UserController ========== findUserPwd.do ==========");

		int isExistId = 0;
		String userId = null;
		String emailAuthKey = null;
		HashMap<String,Object> hm = new HashMap<String,Object>();
		JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
		
		
		// Parameter :: userId 값 검증
		userId = (String)paramMap.get("userId");
		// (Null 체크 및 공백 체크)
		if(userId == null || userId.equals("")) {
			hm.put("code", "401");
			hm.put("msg", "There is no ID.");
			return hm;
		}
		
		emailAuthKey = jwtTokenUtil.createToken(userId);
		
		try {
			// 아이디 중복 체크 :: ID가 없을 경우 0, ID가 있을 경우 1
			isExistId = userService.duplicateChkId(userId);
		} catch (Exception e) {
			hm.put("code", "500");
			hm.put("msg", "Server Error");
			e.printStackTrace();
		}

		if (isExistId == 1) { // 아이디가 있는 경우 메일 발송
			try {
				// 이메일 안에 비밀번호 변경 url을 전송하도록 함
				mailAuthService.sendResetPasswordMail(userId, emailAuthKey);
			} catch (Exception e) {
				hm.put("code", "500");
				hm.put("msg", "Server Error");
				e.printStackTrace();
				return hm;
			}
			hm.put("code", "000");
			hm.put("msg", "Mail Sent Completed.");
		} else { // 아이디가 없을 경우 JSON code 및 msg RETURN
			hm.put("code", "405");
			hm.put("msg", "This ID does not exist.");
		}
		return hm;
	}	
	
	
	
	
	/**
	 * 기능   : 비밀번호 초기화 전 메일 인증
	 * 비고   : API 문서엔 적혀있지 않음 
	 * 작성자 : 정주현 
	 * 작성일 : 2022. 06. 02
	 * 수정일 : 2022. 08. 01
	 */
	@GetMapping(value = {"/app/user/resetUserPwd.do"})
	  public String userList(HttpServletRequest request, Model model) throws Exception {
		
		logger.debug("========== UserController ========== resetUserPwd.do ==========");
		
		boolean validation = false;
		String userId = null;
		String emailAuthKey = null;
		
		userId = request.getParameter("userId");
		emailAuthKey = request.getParameter("emailAuthKey");
		
		// 복호화
		AES256Util aes256Util = new AES256Util();
		userId = aes256Util.aesDecode(userId);
		
		// 토큰 검증
		JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
		validation = jwtTokenUtil.validateToken(emailAuthKey);
		model.addAttribute("userId", userId);
		
		if(validation) {
		    return "/user/resetUserPwd";
		    // React 사용 시 필수
		    // return "redirect:http://13.124.37.209:3000/app/user/resetUserPwd.jsp.do?userId="+userId;
		}else {
			return "/status/500_mailAuth";
		}
	  }
	
	
	
	/**
	 * 기능   : 회원 비밀번호 UPDATE
	 * 작성자 : 정주현 
	 * 작성일 : 2022. 06. 9
	 */
//	@CrossOrigin(origins = "http://13.124.37.209:3000")
//	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping(value = {"/app/user/updateUserPwd.do"})
	@ResponseBody
	public HashMap<String,Object> updateUserPwd(@RequestBody HashMap<String, String> paramMap) {
		
		logger.debug("========== UserController ========== updateUserPwd.do ==========");
		
		String userId = null;
		String userPwd = null;
		HashMap<String,Object> hm = new HashMap<String,Object>();
		UserVO userVO = new UserVO();
		
		// Parameter :: userId 값 검증
		userId = (String)paramMap.get("userId");
		// (Null 체크 및 공백 체크)
		if(userId == null || userId.equals("")) {
			hm.put("code", "401");
			hm.put("msg", "There is no ID.");
			return hm;
		}
		
		// 비밀번호 암호화
		userPwd = (String)paramMap.get("userPwd");
		AES256Util aes256Util = new AES256Util();
		userPwd = aes256Util.aesEncode(userPwd);
		
		userVO.setUserId(userId);
		userVO.setUserPwd(userPwd);
		
		try {
			userService.updateUserPwd(userVO);
			hm.put("code", "000");
			hm.put("msg", "Password change complete.");
		} catch (Exception e) {
			hm.put("code", "500");
			hm.put("msg", "Server Error");
			e.printStackTrace();
		}
		
		return hm;
	}	
	
	
	
	
	/**
	 * 기능   : 회원 삭제
	 * 작성자 : 정주현 
	 * 작성일 : 2022. 05. 24
	 */
//	@PostMapping(value = {"/app/user/deleteUser.do"})
//	@ResponseBody
//	public HashMap<String,Object> deleteUser(HttpServletRequest request) throws Exception {
//		
//		HashMap<String,Object> hm = new HashMap<String,Object>();
//		String userId = request.getParameter("userId");
//		String userAuthToken = request.getHeader("Authorization");
//		String userPwd = request.getParameter("userPwd");
//		
//		// 회원 인증 필요 - token이나 비밀번호 사용
//		// 비밀번호 암호화 
//		AES256Util aes256Util = new AES256Util();
//		userPwd = aes256Util.aesEncode(userPwd);
//		
//		AuthVO authVO = new AuthVO();
//		
//		authVO.setUserId(userId);
//		authVO.setUserPwd(userPwd);
//		
//		JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
//		boolean tmp = jwtTokenUtil.validateToken(userAuthToken);
//		try {
//			// 로그인 확인
//			loginChkByIdPwd = authService.loginChkByIdPwd(authVO);
//			if (loginChkByIdPwd == 0) { // 0일 경우는 Database에 ID와 비밀번호가 틀린 것
//				// 아이디와 비밀번호 어떤게 틀린 것인지 확인하고자함
//				isIdExist = authService.isIdExist(authVO.getUserId());
//				if (isIdExist == 0) {
//					hm.put("code", "405");
//					hm.put("msg", "This ID does not exist.");
//				} else {
//					hm.put("code", "406");
//					hm.put("msg", "The password is wrong.");
//				}
//			} else { // 로그인이 정상적으로 완료된 경우 회원의 정보를 제공하고 LOG를 INSERT
//				// 회원 삭제
//				userService.deleteUser(userId);
//				hm.put("code", "000");
//				hm.put("msg", "deleted");
//			}
//		} catch (Exception e) {
//			hm.put("code", "500");
//			hm.put("msg", "Server error");
//			e.printStackTrace();
//		}
//		return hm;
//	}
}
