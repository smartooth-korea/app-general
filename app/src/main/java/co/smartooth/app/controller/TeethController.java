package co.smartooth.app.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import co.smartooth.app.service.TeethService;
import co.smartooth.app.utils.JwtTokenUtil;
import co.smartooth.app.vo.TeethInfoVO;
import co.smartooth.app.vo.TeethMeasureVO;
import co.smartooth.app.vo.ToothMeasureVO;
import co.smartooth.app.vo.UserVO;
import lombok.extern.slf4j.Slf4j;

/**
 * 작성자 : 정주현 
 * 작성일 : 2022. 07. 14
 * 수정일 : 2022. 08. 02
 * @RestController를 쓰지 않는 이유 : 
 *    몇 개 안되지만 가끔 mapping 중 jsp를 return 하는 호출이 있으므로 @Controller를 사용
 * @RestAPI로 반환해야할 경우 @ResponseBody를 사용하여 HashMap으로 return
 **/
@Slf4j
@RestController
public class TeethController {
    
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired(required = false)
	private TeethService teethService;
	
	// 인증 패스
	private static boolean tokenValidation = false; 
	
	
	/**
	 * 기능   : 회원의 치아 상태 값 등록
	 * 작성자 : 정주현 
	 * 작성일 : 2022. 05. 20
	 * 수정일 : 2022. 07. 14
	 */
	@PostMapping(value = {"/app/user/insertUserTeethInfo.do"})
	@ResponseBody
	public HashMap<String,Object> insertUserTeethInfo(@RequestBody HashMap<String, Object> paramMap) {
		
		logger.debug("========== TeethController ========== insertUserTeethInfo.do ==========");
		
		String userId = null;
		String userNo = null;
		String teethStatus = null;
		
		HashMap<String,Object> hm = new HashMap<String,Object>();
		TeethInfoVO teethInfoVO = new TeethInfoVO();

		userId = (String)paramMap.get("userId");
		// Parameter = userId 값 검증 (Null 체크 및 공백 체크)
		if(userId == null || userId.equals("") || userId.equals(" ")) {
			hm.put("code", "401");
			hm.put("msg", "There is no ID.");
			return hm;
		}
		userNo = (String)paramMap.get("userNo");
		teethStatus = (String)paramMap.get("teethStatus");
		
		try {
			teethInfoVO.setUserId(userId);
			teethInfoVO.setUserNo(userNo);
			teethInfoVO.setTeethStatus(teethStatus);
			// 회원 치아 상태 INSERT
			teethService.insertUserTeethInfo(teethInfoVO);
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
	 * 기능   : 회원의 치아 측정 값을 등록 또는 업데이트
	 * 작성자 : 정주현 
	 * 작성일 : 2022. 05. 25
	 * 	수정일 : 2022. 07. 14
	 */
	@PostMapping(value = {"/app/user/insertUserMeasureValue.do"})
	@ResponseBody
	public HashMap<String,Object> insertUserMeasureValue(@RequestBody HashMap<String, Object> paramMap, HttpServletRequest request) {
		
		logger.debug("========== TeethController ========== insertUserMeasureValue.do ==========");
		
		String measurerId = null;
		String userId = null;
		String userNo = null;
		String userAuthToken = null;

		// 32개 치아 측정 값 
		int[] teethValue = new int[32];
		// 충치 수치 정상 갯수
        int cavityNormal = 0;
        // 충치 수치 경고 갯수
        int cavityWarning = 0;
        // 충치 갯수
        int cavityCnt = 0;
		// ROW 값이 있는지 없는지 확인하는 변수
		int isExistRow = 0;
		
		HashMap<String,Object> hm = new HashMap<String,Object>();

		TeethMeasureVO teethMeasureVO = new TeethMeasureVO();

		// 오늘 날짜 구하기 (SYSDATE)
		LocalDate now = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String sysDate = now.format(formatter);
		
		// 측정인 아이디
		measurerId= (String)paramMap.get("measurerId");
		userId= (String)paramMap.get("userId");
		
		// Parameter = userId 값 검증 (Null 체크 및 공백 체크)
		if(userId == null || userId.equals("") || userId.equals(" ")) {
			hm.put("code", "401");
			hm.put("msg", "This ID does not exist.");
			return hm;
		}
		

		// Map에서 32개의 데이터 추출
		for (int i = 0; i < teethValue.length + 1; i++) {
			if (i == 0) {
				teethValue[i] = Integer.parseInt((String) paramMap.get("t0" + (i + 1)));
				i++;
			} else if (i < 10) {
				teethValue[i - 1] = Integer.parseInt((String) paramMap.get("t0" + i));
			} else {
				teethValue[i - 1] = Integer.parseInt((String) paramMap.get("t" + i));
			}
		}
		
		
		// 조회한 치아 32개의 충치 수치로 치아 분류
		for (int i = 0; i < teethValue.length; i++) {
			if (teethValue[i] < 13) { // 12 이하일 경우 정상 치아
				cavityNormal++;
			} else if (teethValue[i] < 13 || teethValue[i] < 19) { // 13이상 19이하일 경우 충치 상태 경고
				cavityWarning++;
			} else { // 20이상 일 경우 충치
				cavityCnt++;
			}
		}
		
		userNo = (String)paramMap.get("userNo");
		
		userAuthToken = request.getHeader("Authorization");
		// TOKEN 검증
		JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
		tokenValidation = jwtTokenUtil.validateToken(userAuthToken);
		
		if(true) {
			try {
				
				teethMeasureVO.setT01(teethValue[0]); teethMeasureVO.setT02(teethValue[1]); teethMeasureVO.setT03(teethValue[2]); teethMeasureVO.setT04(teethValue[3]); 
				teethMeasureVO.setT05(teethValue[4]); teethMeasureVO.setT06(teethValue[5]); teethMeasureVO.setT07(teethValue[6]); teethMeasureVO.setT08(teethValue[7]); 
				teethMeasureVO.setT09(teethValue[8]); teethMeasureVO.setT10(teethValue[9]); teethMeasureVO.setT11(teethValue[10]); teethMeasureVO.setT12(teethValue[11]);
				teethMeasureVO.setT13(teethValue[12]); teethMeasureVO.setT14(teethValue[13]); teethMeasureVO.setT15(teethValue[14]); teethMeasureVO.setT16(teethValue[15]); 
				teethMeasureVO.setT17(teethValue[16]); teethMeasureVO.setT18(teethValue[17]); teethMeasureVO.setT19(teethValue[18]); teethMeasureVO.setT20(teethValue[29]);
				teethMeasureVO.setT21(teethValue[20]); teethMeasureVO.setT22(teethValue[21]); teethMeasureVO.setT23(teethValue[22]); teethMeasureVO.setT24(teethValue[23]);
				teethMeasureVO.setT25(teethValue[24]); teethMeasureVO.setT26(teethValue[25]); teethMeasureVO.setT27(teethValue[26]); teethMeasureVO.setT28(teethValue[27]); 
				teethMeasureVO.setT29(teethValue[28]); teethMeasureVO.setT30(teethValue[29]); teethMeasureVO.setT31(teethValue[30]); teethMeasureVO.setT32(teethValue[31]);

				teethMeasureVO.setUserId(userId);
				teethMeasureVO.setUserNo(userNo);
				teethMeasureVO.setMeasureDt(sysDate);
				teethMeasureVO.setMeasurerId(measurerId);
				teethMeasureVO.setCavityWarning(cavityWarning);
				teethMeasureVO.setCavityNormal(cavityNormal);
				teethMeasureVO.setCavityCnt(cavityCnt);
				
				// 회원 치아 측정 값을 저장하기 위해 현재 회원이 측정한 측정 값이 오늘 데이터인지 확인 후 값 반환(0 : 오늘X / 1: 오늘)
				isExistRow = teethService.selectUserTeethMeasureValueByDate(teethMeasureVO);
				if(isExistRow == 0){ // 0일 경우는 Database에 값이 없는 경우 ::: INSERT
					teethService.insertUserTeethMeasureValue(teethMeasureVO);
				}else { // 1이상일 경우 Database에 값이 있는 경우 ::: UPDATE
					teethService.updateUserTeethMeasureValue(teethMeasureVO);
				}

				// 충치 갯수
				// teethService.updateUserCavityCnt(teethMeasureVO);
				
				hm.put("code", "000");
				hm.put("msg", "Success");
				
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
	 * 기능   : 회원의 치아 상태 값 조회
	 * 작성자 : 정주현 
	 * 작성일 : 2022. 5. 20
	 * 수정일 : 2022-07-14
	 */
	@PostMapping(value = {"/app/user/selectUserTeethInfo.do"})
	@ResponseBody
	public HashMap<String,Object> selectUserTeethInfo(@RequestBody HashMap<String, Object> paramMap, HttpServletRequest request) {
		
		logger.debug("========== TeethController ========== selectUserTeethInfo.do ==========");
		
		String userId = null;
		String userAuthToken = null;
		
		HashMap<String,Object> hm = new HashMap<String,Object>();
		List<TeethInfoVO> userTeethInfo = new ArrayList<TeethInfoVO>();
		
		// Parameter = userId 값 검증 (Null 체크 및 공백 체크)
		userId= (String)paramMap.get("userId");
		if(userId == null || userId.equals("") || userId.equals(" ")) {
			hm.put("code", "401");
			hm.put("msg", "This ID does not exist.");
			return hm;
		}
		
		userAuthToken = request.getHeader("Authorization");
		// JWT TOKEN 검증
		JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
		tokenValidation = jwtTokenUtil.validateToken(userAuthToken);
		
		if(tokenValidation) {

			try {
				UserVO userVO = new UserVO();
				userVO.setUserId((String)paramMap.get("userId"));
				userVO.setUserNo((String)paramMap.get("userNo"));
				// 회원의 치아 상태 값 조회
				userTeethInfo = teethService.selectUserTeethInfo(userVO);
			} catch (Exception e) {
				hm.put("code", "500");
				hm.put("msg", "Server Error");
				e.printStackTrace();
			}
			hm.put("userTeethInfo", userTeethInfo);
			hm.put("code", "000");
			hm.put("msg", "Success");
		}else {
			hm.put("code", "400");
			hm.put("msg", "The token is not valid.");
		}
		return hm;
	}
	
	
	
	
	/**
	 * 기능   : 회원의 치아 측정 값 조회
	 * 작성자 : 정주현 
	 * 작성일 : 2022. 05. 16
	 * 수정일 : 2022. 08. 09
	 *       	 기간  조회 (startDt, endDt)
	 */
	@PostMapping(value = {"/app/user/selectUserTeethMeasureValue.do"})
	@ResponseBody
	public HashMap<String,Object> selectUserMeasureValue(@RequestBody HashMap<String, Object> paramMap, HttpServletRequest request) throws Exception {
		
		logger.debug("========== TeethController ========== selectUserTeethMeasureValue.do ==========");
		
		String userId = null;
		String userAuthToken = null;
		LocalDate now = null;
		String today = null;
		String startDt = null;
		String endDt = null;
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		
		HashMap<String,Object> hm = new HashMap<String,Object>();
		List<TeethMeasureVO> userTeethValues = new ArrayList<TeethMeasureVO>();
		TeethMeasureVO teethInfoVO = new TeethMeasureVO();

		userId = (String)paramMap.get("userId");
		startDt = (String)paramMap.get("startDt");
		endDt = (String)paramMap.get("endDt");

		// Parameter = userId 값 검증 (Null 체크 및 공백 체크)
		userId= (String)paramMap.get("userId");
		if(userId == null || userId.equals("") || userId.equals(" ")) {
			hm.put("code", "401");
			hm.put("msg", "This ID does not exist.");
			return hm;
		}
		
		// StartDt 와 EndDt가 NULL이거나 빈 값("") 일 경우 오늘 날짜를 대입
		if(startDt == null || "".equals(startDt)) {
			now = LocalDate.now();
			today = now.format(formatter);
			startDt = today;
		}else if(endDt == null || "".equals(endDt)) {
			now = LocalDate.now();
			today = now.format(formatter);
			endDt = today;
		}
		
		userAuthToken = request.getHeader("Authorization");
		// TOKEN 검증
		JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
		tokenValidation = jwtTokenUtil.validateToken(userAuthToken);
		
		if(tokenValidation) {
			try {
				teethInfoVO.setUserId(userId);
				teethInfoVO.setStartDt(startDt);
				teethInfoVO.setEndDt(endDt);
				// 회원의 치아 측정 값 조회 (기간)
				userTeethValues = teethService.selectUserTeethMeasureValue(teethInfoVO);
				hm.put("userTeethValues", userTeethValues);
				hm.put("code", "000");
				hm.put("msg", "Success");
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
	 * 기능   : 회원 치아 개별 측정 값 조회 (기간)
	 * 작성자 : 정주현 
	 * 작성일 : 2022. 05. 26
	 * 수정일 : 2022. 08. 09
 	 *		      기간  조회 (startDt, endDt)
	 */
	@PostMapping(value = {"/app/user/selectUserToothMeasureValue.do"})
	@ResponseBody
	public HashMap<String,Object> selectUserMeasureToothValue(@RequestBody HashMap<String, Object> paramMap, HttpServletRequest request) throws Exception {
		
		logger.debug("========== TeethController ========== selectUserToothMeasureValue.do ==========");
		
		int isExistRow = 0;
		String userId = null;
		String userNo = null;
		String userAuthToken = null;
		String toothNo = null;
		String toothValue = null;
		String startDt = null;
		String endDt = null;
		
		HashMap<String,Object> hm = new HashMap<String,Object>();
		List<ToothMeasureVO> userToothValues = new ArrayList<ToothMeasureVO>();
		List<TeethMeasureVO> userTeethValues = new ArrayList<TeethMeasureVO>();
		ToothMeasureVO toothMeasureVO = new ToothMeasureVO();
		TeethMeasureVO teethMeasureVO = new TeethMeasureVO();
		
		// 오늘 날짜 구하기 (SYSDATE)
		LocalDate now = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String sysDate = now.format(formatter);
		
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
		
		if(tokenValidation) {
			
			try {
				userNo = (String)paramMap.get("userNo");
				// 치아 번호
				toothNo = (String)paramMap.get("toothNo");
				// 치아 측정 값
				toothValue = (String)paramMap.get("toothValue");
				// 검색기간 (시작일)
				startDt = (String)paramMap.get("startDt");
				// 검색기간 (종료일)
				endDt = (String)paramMap.get("endDt");
				
				toothMeasureVO.setUserId(userId);
				toothMeasureVO.setUserNo(userNo);
				toothMeasureVO.setStartDt(startDt);
				toothMeasureVO.setEndDt(endDt);
				toothMeasureVO.setToothNo(toothNo);
				toothMeasureVO.setMeasureDt(sysDate);
			
				if(toothValue != null && !toothValue.equals("") && !toothValue.equals(" ")) { // 값이 있으면 업데이트 
					// 측정값을 입력할 당시, 오늘 날짜와 비교한 후 오늘 일 경우 업데이트, 아닐 경우 INSERT해줘야함
					toothMeasureVO.setToothValue(Integer.parseInt(toothValue));
					isExistRow = teethService.selectUserToothMeasureValueByDate(toothMeasureVO);
					if(isExistRow == 0) {
						teethService.insertUserToothMeasureValue(toothMeasureVO);
					}else {
						teethService.updateUserToothMeasureValue(toothMeasureVO);
					}
				}else {
					
				}
				
				// 회원 치아 개별 측정 값 조회 (기간)
				// 오늘 날짜로 검색 했을 때 0이 나올 경우, 지난 값을 가져와야됨
				userToothValues = teethService.selectUserToothMeasureValue(toothMeasureVO);
				
				// 회원의 32개 치아 즉정 데이터 조회(기간)
				teethMeasureVO.setUserNo(userNo);
				teethMeasureVO.setUserId(userId);
				teethMeasureVO.setStartDt(startDt);
				teethMeasureVO.setEndDt(endDt);
				teethMeasureVO.setMeasureDt(sysDate);
				
				// 회원 치아 측정 값을 저장하기 위해 현재 회원이 측정한 측정 값이 오늘 데이터인지 확인 후 값 반환(0 : 오늘X / 1: 오늘)
				// userId로 ST_TEETH_MEASURE 테이블에 값이 있는지 없는지 확인
				isExistRow = teethService.selectUserTeethMeasureValueByDate(teethMeasureVO);
				if(isExistRow == 0){ // 0일 경우는 Database에 값이 없는 경우 ::: INSERT
					teethService.insertUserTeethMeasureValue(teethMeasureVO);
				}
				
				// 모든 치아에 대한 조회 값 (오늘의 값이 없을 경우 최근 값으로 불러와야함)
				userTeethValues = teethService.selectUserTeethMeasureValue(teethMeasureVO);
				
				hm.put("userToothValues", userToothValues);
				hm.put("userTeethValues", userTeethValues);
				hm.put("code", "000");
				hm.put("msg", "Success");
				
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
	
}
