package co.smartooth.app.vo;

import java.io.Serializable;

public class TeethMeasureVO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	// 회원 이름 : 추가된 내용
	private String userNm;
	// 회원 번호
	private String userNo;
	// 회원 아이디
	private String userId;
	// 검색 (시작일)
	private String startDt;
	// 검색 (종료일)
	private String endDt;
	// 치아 측정 일자
	private String measureDt;
	// 32개의 치아 변수
	private int t01;
	private int t02;
	private int t03;
	private int t04;
	private int t05;
	private int t06;
	private int t07;
	private int t08;
	private int t09;
	private int t10;
	private int t11;
	private int t12;
	private int t13;
	private int t14;
	private int t15;
	private int t16;
	private int t17;
	private int t18;
	private int t19;
	private int t20;
	private int t21;
	private int t22;
	private int t23;
	private int t24;
	private int t25;
	private int t26;
	private int t27;
	private int t28;
	private int t29;
	private int t30;
	private int t31;
	private int t32;
	
	// 충치 상태에 따른 개수
	// 일반 0~12 이하
	private int cavityNormal;
	// 경고 20이상
    private int cavityWarning;
    // 경계 30이상 - 충치
    private int cavityCnt;
    
    // 측정인 아이디
    private String measurerId;
	
    
	public String getUserNm() {
        return userNm;
    }
    public void setUserNm(String userNm) {
        this.userNm = userNm;
    }
	public String getUserNo() {
		return userNo;
	}
	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getStartDt() {
		return startDt;
	}
	public void setStartDt(String startDt) {
		this.startDt = startDt;
	}
	public String getEndDt() {
		return endDt;
	}
	public void setEndDt(String endDt) {
		this.endDt = endDt;
	}

	public int getT01() {
		return t01;
	}
	public void setT01(int t01) {
		this.t01 = t01;
	}
	public int getT02() {
		return t02;
	}
	public void setT02(int t02) {
		this.t02 = t02;
	}
	public int getT03() {
		return t03;
	}
	public void setT03(int t03) {
		this.t03 = t03;
	}
	public int getT04() {
		return t04;
	}
	public void setT04(int t04) {
		this.t04 = t04;
	}
	public int getT05() {
		return t05;
	}
	public void setT05(int t05) {
		this.t05 = t05;
	}
	public int getT06() {
		return t06;
	}
	public void setT06(int t06) {
		this.t06 = t06;
	}
	public int getT07() {
		return t07;
	}
	public void setT07(int t07) {
		this.t07 = t07;
	}
	public int getT08() {
		return t08;
	}
	public void setT08(int t08) {
		this.t08 = t08;
	}
	public int getT09() {
		return t09;
	}
	public void setT09(int t09) {
		this.t09 = t09;
	}
	public int getT10() {
		return t10;
	}
	public void setT10(int t10) {
		this.t10 = t10;
	}
	public int getT11() {
		return t11;
	}
	public void setT11(int t11) {
		this.t11 = t11;
	}
	public int getT12() {
		return t12;
	}
	public void setT12(int t12) {
		this.t12 = t12;
	}
	public int getT13() {
		return t13;
	}
	public void setT13(int t13) {
		this.t13 = t13;
	}
	public int getT14() {
		return t14;
	}
	public void setT14(int t14) {
		this.t14 = t14;
	}
	public int getT15() {
		return t15;
	}
	public void setT15(int t15) {
		this.t15 = t15;
	}
	public int getT16() {
		return t16;
	}
	public void setT16(int t16) {
		this.t16 = t16;
	}
	public int getT17() {
		return t17;
	}
	public void setT17(int t17) {
		this.t17 = t17;
	}
	public int getT18() {
		return t18;
	}
	public void setT18(int t18) {
		this.t18 = t18;
	}
	public int getT19() {
		return t19;
	}
	public void setT19(int t19) {
		this.t19 = t19;
	}
	public int getT20() {
		return t20;
	}
	public void setT20(int t20) {
		this.t20 = t20;
	}
	public int getT21() {
		return t21;
	}
	public void setT21(int t21) {
		this.t21 = t21;
	}
	public int getT22() {
		return t22;
	}
	public void setT22(int t22) {
		this.t22 = t22;
	}
	public int getT23() {
		return t23;
	}
	public void setT23(int t23) {
		this.t23 = t23;
	}
	public int getT24() {
		return t24;
	}
	public void setT24(int t24) {
		this.t24 = t24;
	}
	public int getT25() {
		return t25;
	}
	public void setT25(int t25) {
		this.t25 = t25;
	}
	public int getT26() {
		return t26;
	}
	public void setT26(int t26) {
		this.t26 = t26;
	}
	public int getT27() {
		return t27;
	}
	public void setT27(int t27) {
		this.t27 = t27;
	}
	public int getT28() {
		return t28;
	}
	public void setT28(int t28) {
		this.t28 = t28;
	}
	public int getT29() {
		return t29;
	}
	public void setT29(int t29) {
		this.t29 = t29;
	}
	public int getT30() {
		return t30;
	}
	public void setT30(int t30) {
		this.t30 = t30;
	}
	public int getT31() {
		return t31;
	}
	public void setT31(int t31) {
		this.t31 = t31;
	}
	public int getT32() {
		return t32;
	}
	public void setT32(int t32) {
		this.t32 = t32;
	}
	public String getMeasureDt() {
		return measureDt;
	}
	public void setMeasureDt(String measureDt) {
		this.measureDt = measureDt;
	}
	
    public int getCavityCnt() {
        return cavityCnt;
    }
    public void setCavityCnt(int cavityCnt) {
        this.cavityCnt = cavityCnt;
    }
    public int getCavityNormal() {
        return cavityNormal;
    }
    public void setCavityNormal(int cavityNormal) {
        this.cavityNormal = cavityNormal;
    }
    public int getCavityWarning() {
        return cavityWarning;
    }
    public void setCavityWarning(int cavityWarning) {
        this.cavityWarning = cavityWarning;
    }
    
    public String getMeasurerId() {
		return measurerId;
	}
	public void setMeasurerId(String measurerId) {
		this.measurerId = measurerId;
	}
	
}
