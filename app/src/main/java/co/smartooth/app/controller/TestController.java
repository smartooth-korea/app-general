package co.smartooth.app.controller;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ch.qos.logback.classic.Logger;
import lombok.extern.slf4j.Slf4j;

/**
 * 작성자 : 정주현 
 * 작성일 : 2022. 4. 28 ~
 * @RestController를 쓰지 않는 이유는 몇 안되는 mapping에 jsp를 반환해줘야하는게 있으므로 @Controller를 사용한다.
 * @RestAPI로 반환해야할 경우 @ResponseBody를 사용하여 HashMap으로 return 해준다.
 */
@Slf4j
@Controller
public class TestController {
    

	@RequestMapping(value = {"/test/app/user/register.do"})
	public String regist() {
		return "/test/register";
	}

	@RequestMapping(value = {"/test/app/user/emailAuth.do"})
	public String emailAuth() {
		return "/test/emailAuth";
	}
	
	
	@RequestMapping(value = {"/test/app/user/login.do"})
	public String login() {
		return "/test/login";
	}
	
	@RequestMapping(value = {"/test/app/user/deviceInfo.do"})
	public String device() {
		return "/test/deviceInfo";
	}
	
	@RequestMapping(value = {"/test/app/user/selectUserInfo.do"})
	public String selectUserInfo() {
		return "/test/selectUserInfo";
	}
	
	@RequestMapping(value = {"/test/app/user/selectUserList.do"})
    public String selectUserList() {
        return "/test/selectUserList";
    }
	

	@RequestMapping(value = {"/test/app/user/selectUserTeethInfo.do"})
	public String teethInfo() {
		return "/test/selectUserTeethInfo";
	}
	
	@RequestMapping(value = {"/test/app/user/selectUserTeethMeasureValue.do"})
	public String teethMeasureValue() {
		return "/test/selectUserTeethMeasureValue";
	}

	@RequestMapping(value = {"/test/app/user/selectUserToothMeasureValue.do"})
	public String selectUserToothMeasureValue() {
		return "/test/selectUserToothMeasureValue";
	}
 	
	@RequestMapping(value = {"/test/app/user/deleteUser.do"})
	public String deleteUser() {
		return "/test/deleteUser";
	}
	
	@RequestMapping(value = {"/test/app/user/insertCalibrationInfoValue.do"})
	public String insertCalibrationInfo() {
		return "/test/insertCalibrationInfoValue";
	}
	
	@RequestMapping(value = {"/test/app/user/findUserPwd.do"})
	public String findUserPwd() {
		return "/test/findUserPwd";
	}
	
	@GetMapping(value = {"/test/app/user/react.do"})
	public String reactDemo(){
		return "Hello React";
	}
	
	@GetMapping(value= {"/test/app/user/selectUserIsMeasuringValue.do"})
	public String selectUserIsMeasuringValue(){
	    return "/test/selectUserIsMeasuringValue";
	}
	
	@GetMapping(value= {"/test/web/login"})
    public String webLogin(){
        return "/test/webLogin";
    }
	
	@RequestMapping(value= {"/test/app/user/selectUserListTC.do"})
    public String selectUserListTC(){
        return "/test/selectUserListTC";
    }
	
	@RequestMapping(value= {"/test/app/user/selectUserListSTbyTC.do"})
    public String selectUserListSTbyTC(){
        return "/test/selectUserListSTbyTC";
    }
	
	
	// INSERT 및 UPDATE
	@RequestMapping(value = {"/test/app/user/insertUserMeasureValue.do"})
	public String insertUserMeasureValue() {
		return "/test/insertUserMeasureValue";
	}
	
	
}
