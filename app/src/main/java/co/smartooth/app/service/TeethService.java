package co.smartooth.app.service;

import java.util.List;
import co.smartooth.app.vo.TeethInfoVO;
import co.smartooth.app.vo.TeethMeasureVO;
import co.smartooth.app.vo.ToothMeasureVO;
import co.smartooth.app.vo.UserVO;

/**
 * 작성자 : 정주현 
 * 작성일 : 2022. 4. 28 ~
 */
public interface TeethService {
	
	
	// 회원 치아 상태 값 INSERT
	public void insertUserTeethInfo(TeethInfoVO teethInfoVO) throws Exception;
	
	
	
	// 회원의 치아 상태 값 조회
	public List<TeethInfoVO> selectUserTeethInfo(UserVO userVO) throws Exception; 
	
	
	
	// 회원 치아 측정 값 INSERT
	public void insertUserTeethMeasureValue(TeethMeasureVO teethMeasureVO) throws Exception;

	
	
	// 회원 치아 측정 값 UPDATE
	public void updateUserTeethMeasureValue(TeethMeasureVO teethMeasureVO) throws Exception;
	
	
	
	// 회원 개별 치아 측정 값 INSERT
	public void insertUserToothMeasureValue(ToothMeasureVO toothMeasureVO) throws Exception;

	
	
	// 회원 개별 치아 측정 값 UPDATE
	public void updateUserToothMeasureValue(ToothMeasureVO toothMeasureVO) throws Exception; 
	
	
	
	// 회원 치아 측정 값을 저장하기 위해 현재 회원이 측정한 측정 값이 오늘 데이터인지 확인 후 값 반환(0 : 오늘X, 1: 오늘)
	public Integer selectUserTeethMeasureValueByDate(TeethMeasureVO teethMeasureVO) throws Exception;

	
	
	// 회원 개별 치아 측정 값을 저장하기 위해 현재 회원이 측정한 측정 값이 오늘 데이터인지 확인 후 값 반환(0 : 오늘X, 1: 오늘)
	public Integer selectUserToothMeasureValueByDate(ToothMeasureVO toothMeasureVO) throws Exception;
	
	
	
	// 회원의 치아 측정 값 조회 (기간)
	public List<TeethMeasureVO> selectUserTeethMeasureValue(TeethMeasureVO teethMeasureVO) throws Exception;
	
	
	
	// 회원 치아 개별 측정 값 조회 (기간)
	public List<ToothMeasureVO> selectUserToothMeasureValue(ToothMeasureVO toothMeasureVO) throws Exception;

	
	
}
