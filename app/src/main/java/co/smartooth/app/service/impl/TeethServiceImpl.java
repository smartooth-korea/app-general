package co.smartooth.app.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.smartooth.app.mapper.TeethMapper;
import co.smartooth.app.service.TeethService;
import co.smartooth.app.vo.TeethInfoVO;
import co.smartooth.app.vo.TeethMeasureVO;
import co.smartooth.app.vo.ToothMeasureVO;
import co.smartooth.app.vo.UserVO;

/**
 * 작성자 : 정주현 
 * 작성일 : 2022. 4. 28 ~
 */
@Service
public class TeethServiceImpl implements TeethService{
	
	
	@Autowired(required = false)
	TeethMapper teethMapper;

	
	// 회원 치아 상태 값 INSERT
	@Override
	public void insertUserTeethInfo(TeethInfoVO teethInfoVO) throws Exception {
		teethMapper.insertUserTeethInfo(teethInfoVO);
	}
	
	
	
	// 회원의 치아 상태 값 조회
	@Override
	public List<TeethInfoVO> selectUserTeethInfo(UserVO userVO) throws Exception {
		return teethMapper.selectUserTeethInfo(userVO);
	}
	
	
	
	// 회원 치아 측정 값 INSERT
	@Override
	public void insertUserTeethMeasureValue(TeethMeasureVO teethMeasureVO) throws Exception {
		teethMapper.insertUserTeethMeasureValue(teethMeasureVO);
	}

	
	
	// 회원 치아 측정 값 입력 UPDATE
	@Override
	public void updateUserTeethMeasureValue(TeethMeasureVO teethMeasureVO) throws Exception {
		teethMapper.updateUserTeethMeasureValue(teethMeasureVO);
	}
	
	
	
	// 회원 개별 치아 측정 값 INSERT
	@Override
	public void insertUserToothMeasureValue(ToothMeasureVO toothMeasureVO) throws Exception {
		teethMapper.insertUserToothMeasureValue(toothMeasureVO);
	}
	
	
	
	// 회원 개별 치아 측정 값 UPDATE
	@Override
	public void updateUserToothMeasureValue(ToothMeasureVO toothMeasureVO) throws Exception {
		teethMapper.updateUserToothMeasureValue(toothMeasureVO);
		
	}
	
	
	
	// 회원 충치 개수 UPDATE
	//@Override
	//public void updateUserCavityCnt(TeethMeasureVO teethMeasureVO) throws Exception {
	//	teethMapper.updateUserCavityCnt(teethMeasureVO);
	//}
	
	
	
	// 회원 치아 측정 값을 저장하기 위해 현재 회원이 측정한 측정 값이 오늘 데이터인지 확인 후 값 반환(0 : 오늘X, 1: 오늘)
	@Override
	public Integer selectUserTeethMeasureValueByDate(TeethMeasureVO teethMeasureVO) throws Exception {
		return teethMapper.selectUserTeethMeasureValueByDate(teethMeasureVO);
	}
	
	
	
	// 회원 치아 측정 값을 저장하기 위해 현재 회원이 측정한 측정 값이 오늘 데이터인지 확인 후 값 반환(0 : 오늘X, 1: 오늘)
	@Override
	public Integer selectUserToothMeasureValueByDate(ToothMeasureVO toothMeasureVO) throws Exception {
		return teethMapper.selectUserToothMeasureValueByDate(toothMeasureVO);
	}

	
	
	// 회원의 치아 측정 값 조회 (기간)
	@Override
	public List<TeethMeasureVO> selectUserTeethMeasureValue(TeethMeasureVO teethMeasureVO) throws Exception {
		return teethMapper.selectUserTeethMeasureValue(teethMeasureVO);
	}
	
	
	
	// 회원 치아 개별 측정 값 조회 (기간)
	@Override
	public List<ToothMeasureVO> selectUserToothMeasureValue(ToothMeasureVO toothMeasureVO) throws Exception {
		return teethMapper.selectUserToothMeasureValue(toothMeasureVO);
	}

	

}