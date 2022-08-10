<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그인</title>
</head>
<script src="https://code.jquery.com/jquery-latest.min.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	
	$('#submit').click(function(){ 
	    
		var userId = $("#userId").val();
	    var userPwd = $("#userPwd").val();
		
		$.ajax({
			type:'POST',   //post 방식으로 전송
			url:'/app/login.do',   //데이터를 주고받을 파일 주소
			data:JSON.stringify ({
				
				"userId" : userId
				,"userPwd" : userPwd
				,"loginIp" : "127.0.0.1"
				
			}),
			dataType:'JSON', //데이터 타입 JSON
			contentType : "application/json; charset=UTF-8",
			success : function(data){   //파일 주고받기가 성공했을 경우. data 변수 안에 값을 담아온다.
				$('#message').html(data);  //현재 화면 위 id="message" 영역 안에 data안에 담긴 html 코드를 넣어준다. 
			}
		});
	});
});
</script>
<body>
        <input type="text" id="userId" name="userId" value="dean880111@gmail.com"/><br/>
        <input type="password" id="userPwd" name="userPwd" value="dkagh1505!@"/><br/>
		로그인 >>> <input type="button" id="submit" value="버튼"/>
</body>
</html>