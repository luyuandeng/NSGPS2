package com.gzeport.app.gps.manager;

import java.io.Serializable;

import com.gzeport.app.gps.common.ResponseXmlBean;
import com.gzeport.app.gps.dao.IVehicleInfoDao;

public class VehicleInfoManagerImpl implements IVehicleInfoManager,Serializable {
	
	private static final long serialVersionUID = -5942481889688418515L;
	private IVehicleInfoDao  vehileInfoDao ;

	public IVehicleInfoDao getVehileInfoDao() {
		return vehileInfoDao;
	}

	public void setVehileInfoDao(IVehicleInfoDao vehileInfoDao) {
		this.vehileInfoDao = vehileInfoDao;
	}

	/**
	 * @功能: 开始请求 
	 * @编码: luyd luyuandeng@gzeport.com 2013-6-9 上午10:07:03
	 */
	@Override
	public ResponseXmlBean startGetGpsData(String userName, String password,
			String queryType, String plate, String inAreaNo, String sTime,
			String eTime) {
		ResponseXmlBean xmlBean =null;
		xmlBean  =   this.vehileInfoDao.startGetGpsData(  userName,   password,
		   			  queryType,   plate,   inAreaNo,   sTime,
					  eTime);
		return xmlBean;
	}


	/**
	 * @功能: 停止请求 
	 * @编码: luyd luyuandeng@gzeport.com 2013-6-9 上午10:07:03
	 */
	@Override
	public ResponseXmlBean stopGetGpsData(String userName, String password,
			String queryType, String plate, String inAreaNo, String sTime) {
		ResponseXmlBean  xmlBean=  null;
		xmlBean  =   this.vehileInfoDao.stopGetGpsData (  userName,   password,
	   			  queryType,   plate,   inAreaNo,   sTime);

		return xmlBean;
	}


}
