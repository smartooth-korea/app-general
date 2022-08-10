package co.smartooth.app.service.impl;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import co.smartooth.app.mapper.UserMapper;
import co.smartooth.app.service.UserService;
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
@Service
public class UserServiceImpl implements UserService{
	
	
	@Autowired(required = false)
	UserMapper userMapper;

	
	
	
	// 회원 아이디 중복 체크
	@Override
	public int duplicateChkId(String userId) throws Exception {
		return userMapper.duplicateChkId(userId);
	}

	
	
	//	회원 등록 (회원가입)
	@Override
	public void insertUserInfo(UserVO userVO) throws Exception {
		userMapper.insertUserInfo(userVO);
	}
	
	
	
	// 회원 정보 업데이트
	@Override
	public void updateUserInfo(UserVO userVo) throws Exception {
		userMapper.updateUserInfo(userVo);
	}
	
	
	
	// 회원 번호 (회원 번호 생성 전 SEQ_NO) 조회
	@Override
	public Integer selectUserSeqNo(@Param("userType") String userType) throws Exception {
		return userMapper.selectUserSeqNo(userType);
	}
	
	
	
	// 회원 번호 (회원 번호 생성 전 SEQ_STR) 조회
	@Override
	public int selectUserSeqStr() throws Exception {
		return userMapper.selectUserSeqStr();
	}
	
	
	
	// 회원 번호 (회원 번호 생성 후 SEQ_NO) 업데이트 
	@Override
	public void updateUserSeqNo(@Param("userType") String userType, @Param("seqNo") int seqNo) throws Exception {
		userMapper.updateUserSeqNo(userType, seqNo);
	}

	
	
	// 회원 번호 (회원 번호 생성 후 SEQ_NO) 업데이트 
	@Override
	public void updateUserSeqStr(@Param("seqStr") int seqStr) throws Exception {
		userMapper.updateUserSeqStr(seqStr);
	}

	
	
	// 회원 정보 조회
	@Override
	public List<UserVO> selectUserInfo(UserVO userVO) throws Exception {
		return userMapper.selectUserInfo(userVO);
	}
	
	
	
	// 회원 비밀번호 변경(찾기)
	@Override
	public void updateUserPwd(UserVO userVO) throws Exception {
		userMapper.updateUserPwd(userVO);
	}
	
	
	
    // 회원의 삭제
    // @Override
    // public void deleteUser(String userId) throws Exception {
    //		userMapper.deleteUser(userId);
    // }
    
	
	
}