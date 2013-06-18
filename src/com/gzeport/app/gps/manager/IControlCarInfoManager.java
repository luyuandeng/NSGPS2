package com.gzeport.app.gps.manager;

import java.util.List;

import com.gzeport.app.gps.pojo.ControlCarInfo;

public interface IControlCarInfoManager {
	
	public void addVehicleInfo(ControlCarInfo instance);
	
	public List<ControlCarInfo> getNsCarINfoList();
	
	public boolean updateControlCarStatus(String seqid,String status);

}
