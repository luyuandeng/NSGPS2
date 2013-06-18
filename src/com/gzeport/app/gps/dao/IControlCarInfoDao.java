package com.gzeport.app.gps.dao;

import java.util.List;

import com.gzeport.app.gps.pojo.ControlCarInfo;

public interface IControlCarInfoDao {

	public void addVehicleInfo(ControlCarInfo instance);
	
	public List<ControlCarInfo> getNsCarINfoList();

	public boolean updateControlCarStatus(String seqId, String status);
	
	public boolean  updateControlCarInfo(Object o  );

	public   List<ControlCarInfo> getControlCarInfoList();
}
