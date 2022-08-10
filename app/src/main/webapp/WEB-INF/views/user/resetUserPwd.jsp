<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="shortcut icon" type="image/x-icon" href="/imgs/common/logo/logo_img_ori.png">
<link rel="stylesheet" href="css/bootstrap.resetpassword.css">
<link rel="stylesheet" href="css/common/sub.css">
<title>Smartooth ::: Reset Your Password</title>
<style type="text/css">
/* .textButton{ */
/* 	text-align: center; */
/* 	height: 110px; */
/* 	width: 500px; */
/* 	font-size: 35px; */
/* 	border-radius: 15px; */
/* 	border: 2px solid gray; */
/* } */

/* #resetButton{ */
/* 	background-color: #448fe1; */
/* 	border-color: #448fe1; */
/* 	border-radius: 5px; */
/* 	color : #ffffff; */
/* 	text-decoration: inherit; */
/* 	height: 150px; */
/* 	width: 540px; */
/* 	font-size: 40px; */
/* 	font-weight: bold; */
/* 	text-align: center; */
/* } */
body{
	font-family: AppleSDGothicNeoR;
	background-color: #333333;
}

</style>
</head>

<script src="https://code.jquery.com/jquery-latest.min.js"></script>
<script type="text/javascript">

	// input 태그 값 추출
	var strPwd1 = $('#userPwd1').val();
	var strPwd2 = $('#userPwd2').val();
	
	function pwdChk(){
		var userId = $('#userId').val();
		var regExpPw = /^(?=.*[a-zA-z])(?=.*[0-9])(?=.*[$`~!@$!%*#^?&\\(\\)\-_=+]).{8,16}$/;
		var strPwd1 = $('#userPwd1').val();
		var strPwd2 = $('#userPwd2').val();
		if(strPwd1 != strPwd2){
			alert("The passwords entered do not match.");
			return false;
		}else{
			if(!regExpPw.test(strPwd1)){
				alert("8-16 characters including English/special characters/numbers");
				return false;
			}else{
				$.ajax({
					type:'POST',   //post 방식으로 전송
					url:'/app/user/updateUserPwd.do',   //데이터를 주고받을 파일 주소
					data:JSON.stringify ({
						"userId" : userId
						,"userPwd" : strPwd1
					}),   //위의 변수에 담긴 데이터를 전송해준다.
					dataType:'JSON',   //json 파일 형식으로 값을 담아온다.
					contentType : "application/json; charset=UTF-8",
					success : function(data){   //파일 주고받기가 성공했을 경우. data 변수 안에 값을 담아온다.
						alert("Your password has been changed."); 
						// 앱으로 연결하는 SCRIPT 입력
						var visitedAt = (new Date()).getTime(); // 방문 시간
						var userAgent = navigator.userAgent;
						
			 			if (userAgent.match(/iPhone/)) {
			 				setTimeout(function() {
									if ((new Date()).getTime() - visitedAt < 2000) {
										location.replace('https://apps.apple.com/app/id1548711244'); // 마켓주소
										// location.replace('itms-apps://itunes.apple.com/app/1548711244'); // 커스텀스킴주소
									}
							}, 700);
			 			}else{
			 				window.close();
			 			}
			 		
					},
					error:function(request,status,error){
						alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"
								+"\n"
								+"\n"
								+"An error has occurred."+"\n"+"We would appreciate it if you could e-mail that message."
								+"\n"
								+"\n"+"smartooth.system@gmail.com"
								+"\n"
								+"\n"+"We'll take care of it ASAP."
								+"\n"
								+"\n");
					}
				});
			}
		}
	}
</script>
<body>
<!-- 	<div align="center" style="position: relative; top: 80px;"> -->
<!-- 		<img alt="smartooth_log" src="https://www.smartooth.co/img/logo/logo_origin.png" style="width: 400px;"> -->
<!-- 	</div> -->
<!-- 	<div style="height:100px;"></div> -->
<!-- 	<div style="height:60px;"></div> -->
<%-- 	<input type="hidden" id="userId" name="userId" value="${userId}"> --%>
<!-- 	<div align="center"> -->
<!-- 		<div> -->
<!-- 			<input type="password" id="userPwd1" class="textButton" name="userPwd"/> -->
<!-- 		<div style="height:30px;"></div> -->
<!-- 			<input type="password" id="userPwd2" class="textButton" name="userPwd"/> -->
<!-- 		</div> -->
<!-- 		<div> -->
<!-- 		<div style="height:50px;"></div> -->
<!-- 			<input type="button" id="resetButton" value="Reset Your Password" onclick="pwdChk()"> -->
<!-- 		</div> -->
<!-- 	</div> -->
	
	<div class="jumbotron vertical-center">
		<input type="hidden" id="userId" name="userId" value="${userId}">
		<div class="container">
			<div id="login_logo_background">
				<img id="logo_img" src="/imgs/login/login_logo_background.png" alt="㈜스마투스코리아 로고" style="width: 400px;" />
			</div>
			<div class="commonHeight20"></div>
			<h3 style="text-align: center; color: white; font-weight: bold;">비밀번호 변경 페이지</h3>
			<div class="commonHeight40"></div>
			<div class="form-group">
				<input type="password" class="form-control" placeholder="Password" id="userPwd1" name="userPwd1" maxlength="20">
			</div>
			<div class="commonHeight30"></div>
			<div class="form-group">
				<input type="password" class="form-control" placeholder="Password" id="userPwd2" name="userPwd2" maxlength="20">
			</div>
			<div class="commonHeight30"></div>
			<div class="form-group">
				<input type="button" id="login_btn" style="width: 30%;" value="Reset" onclick="pwdChk();" class="btn btn-primary form-control" value="로그인">
			</div>
		</div>
	</div>
	
</body>
</html>