package com.gzeport.app.gps.manager;

import com.gzeport.app.gps.common.ResponseXmlBean;


public interface IVehicleInfoManager {
	
	/**
	 * @功能: 开始请求 
	 * @编码: luyd luyuandeng@gzeport.com 2013-6-9 上午10:01:50
	 */
	 public ResponseXmlBean startGetGpsData(String userName, String password,
			String queryType, String plate, String inAreaNo, String sTime,
			String eTime);
	 
	/**
	 * @功能: 停止请求 
	 * @编码: luyd luyuandeng@gzeport.com 2013-6-9 上午10:02:03
	 */
	public ResponseXmlBean stopGetGpsData(String userName, String password,
				String queryType, String plate, String inAreaNo, String sTime) ;
}
