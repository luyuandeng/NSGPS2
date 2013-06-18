package com.gzeport.app.gps.dao;


import java.util.Date;
import java.util.List;

import com.gzeport.app.gps.common.ResponseXmlBean;
import com.gzeport.app.gps.pojo.VehicleInfo;

public interface IVehicleInfoDao {
	
	/**
	 * @功能: 开始请求 
	 * @编码: luyd luyuandeng@gzeport.com 2013-6-9 上午10:03:23
	 */
	public ResponseXmlBean startGetGpsData(String userName, String password,
			String queryType, String plate, String inAreaNo, String sTime,
			String eTime);

	
	/**
	 * @功能: 实例化对象 
	 * @编码: luyd luyuandeng@gzeport.com 2013-6-9 上午10:03:36
	 */
	public void addVehicleInfo(VehicleInfo instance);
	
	
	/**
	 * @功能: 停止请求 
	 * @编码: luyd luyuandeng@gzeport.com 2013-6-9 上午10:03:08
	 */
	public ResponseXmlBean stopGetGpsData(String userName, String password,
			String queryType, String plate, String inAreaNo, String sTime);
	
	
	/**
	 * @功能: 更新数据库监控表 
	 * @编码: luyd luyuandeng@gzeport.com 2013-6-9 上午10:02:46
	 */
	public Boolean updateSetData(  String userName,String carNo,  String carAreaNo,Date sdate,  Date edate,   String flag);

	
	/**
	 * @功能: 获取历史数据DAO接口 
	 * @编码: luyd luyuandeng@gzeport.com 2013-6-9 上午10:39:13
	 */
	public List<VehicleInfo> getHistoryGpsDataList(String plate,
			Date startTime, Date stopTime);

	/**
	 * @功能: 返回实时GPS数据 
	 * @编码: luyd luyuandeng@gzeport.com 2013-6-9 上午11:08:53
	 */
	public List<VehicleInfo> getGpsImmediateDataList(String plate, Date lastGenTime);
	
	
}
