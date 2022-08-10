package co.smartooth.app.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import co.smartooth.app.vo.CalibrationVO;
import co.smartooth.app.vo.TeethInfoVO;
import co.smartooth.app.vo.TeethMeasureVO;
import co.smartooth.app.vo.ToothMeasureVO;
import co.smartooth.app.vo.UserVO;

/**
 * 작성자 : 정주현 
 * 작성일 : 2022. 04. 28
 * 수정일 : 2022. 08. 03
 */
@Mapper
public interface UserMapper {
	

	// 일반 회원 아이디 중복 체크
	public int duplicateChkId(String userId) throws Exception;
	
	
	
	// 회원 등록 (회원가입)
	public void insertUserInfo(UserVO userVO) throws Exception;
	
	
	
	// 회원 정보 업데이트
	public void updateUserInfo(UserVO userVO) throws Exception;

	
	
	// 회원 번호 (회원 번호 생성 전 SEQ_NO) 조회
	public Integer selectUserSeqNo(@Param("userType") String userType) throws Exception;

	
	
	// 회원 번호 (회원 번호 생성 전 SEQ_STR) 조회
	public int selectUserSeqStr() throws Exception;
	
	
	
	// 회원 번호 (회원 번호 생성 후 SEQ_NO) 업데이트 
	public void updateUserSeqNo(@Param("userType") String userType, @Param("seqNo") int seqNo) throws Exception;

	
	
	// 회원 번호 (회원 번호 생성 후 SEQ_STR) 업데이트 
	public void updateUserSeqStr(@Param("seqStr") int seqStr) throws Exception;

	
	
	// 회원 정보 조회
	public List<UserVO> selectUserInfo(UserVO userVO) throws Exception;

	
	
	// 회원 비밀번호 변경(찾기)
	public void updateUserPwd(UserVO userVO) throws Exception;
	
	
	
	// 회원 삭제
	// public void deleteUser(String userId) throws Exception;
	
	
    
}