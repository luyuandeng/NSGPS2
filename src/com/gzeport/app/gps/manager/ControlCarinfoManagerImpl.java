package com.gzeport.app.gps.manager;

import java.util.List;

import com.gzeport.app.gps.dao.IControlCarInfoDao;
import com.gzeport.app.gps.pojo.ControlCarInfo;
/**
 * @ClassName NsCarinfoManagerImpl
 * @Description 南沙车辆GPS监控管理类
 * @author luyd luyuandeng@gzeport.com
 * @date 2013-5-30
 */
public class ControlCarinfoManagerImpl  implements IControlCarInfoManager{
	
	private IControlCarInfoDao controlCarInfoDao;


	public IControlCarInfoDao getControlCarInfoDao() {
		return controlCarInfoDao;
	}
	public void setControlCarInfoDao(IControlCarInfoDao controlCarInfoDao) {
		this.controlCarInfoDao = controlCarInfoDao;
	}
	@Override
	public void addVehicleInfo(ControlCarInfo instance) {
	}
	/**
	 * @功能: 监控车辆列表 
	 * @编码: luyd luyuandeng@gzeport.com 2013-5-30 下午6:26:47
	 */
	@Override
	public List<ControlCarInfo> getNsCarINfoList() {
		return this.controlCarInfoDao.getNsCarINfoList();
	}

	/**
	 * @功能: 更新车辆监控状态  为监控 或未监控 
	 * @编码: luyd luyuandeng@gzeport.com 2013-5-31 下午3:21:38
	 */
	@Override
	public boolean updateControlCarStatus(String seqId,String status) {
		 return this.controlCarInfoDao.updateControlCarStatus(seqId,  status);
 	}

}
