package com.gzeport.app.gps.help;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gzeport.app.gps.common.FileManagerBean;
import com.gzeport.app.gps.common.NSGPSConstats;
import com.gzeport.app.gps.common.VehicleInfoXmlBean;
import com.gzeport.app.gps.dao.IControlCarInfoDao;
import com.gzeport.app.gps.dao.IVehicleInfoDao;
import com.gzeport.app.gps.pojo.ControlCarInfo;
import com.gzeport.app.gps.pojo.VehicleInfo;
import com.gzeport.app.gps.util.StringUtil;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyReplacer;
import com.thoughtworks.xstream.io.xml.XppDriver;

public class PollingCarGpsJobDetail {
	
	private static Log log =LogFactory.getLog(PollingCarGpsJobDetail.class);
	
	private IVehicleInfoDao vehicleInfoDao;
	private IControlCarInfoDao controlCarInfoDao;
	private FileManagerBean gpsFileManagerBean;
	
	public FileManagerBean getGpsFileManagerBean() {
		return gpsFileManagerBean;
	}
	public void setGpsFileManagerBean(FileManagerBean gpsFileManagerBean) {
		this.gpsFileManagerBean = gpsFileManagerBean;
	}
	public IVehicleInfoDao getVehicleInfoDao() {
		return vehicleInfoDao;
	}
	public void setVehicleInfoDao(IVehicleInfoDao vehicleInfoDao) {
		this.vehicleInfoDao = vehicleInfoDao;
	}
	public IControlCarInfoDao getControlCarInfoDao() {
		return controlCarInfoDao;
	}
	public void setControlCarInfoDao(IControlCarInfoDao controlCarInfoDao) {
		this.controlCarInfoDao = controlCarInfoDao;
	}
	
	private static  Lock lock = new ReentrantLock();// 锁  
	
	/**
	 * @功能: 轮询监控表 生成报文 
	 * @编码: luyd luyuandeng@gzeport.com 2013-6-9 上午10:19:29
	 */
	@SuppressWarnings("unused")
	public  void pollingCarGpsData(){
		log.info("进入轮询方法执行.................................................");
		lock.lock();
		List<ControlCarInfo> infoList = null;
		infoList = this.controlCarInfoDao.getControlCarInfoList();
		List <VehicleInfo> gpsDataList = null;
		VehicleInfo vinfo = new VehicleInfo();
		try{
			if(infoList!=null){
				for(ControlCarInfo carinfo :infoList){
					if(carinfo.getIsHistory().equals(NSGPSConstats.NSCARINFO_ISHISTORY1)){ //取历史数据的
						gpsDataList = this.vehicleInfoDao.getHistoryGpsDataList(carinfo.getPlate(),carinfo.getStartTime(),carinfo.getStopTime());
						if(gpsDataList!=null&&gpsDataList.size()>0){ //查询到的数据集
							this.cretateGpsDataFile(gpsDataList, carinfo.getInareano(),carinfo.getReqUser());
						}
						carinfo.setStatus(NSGPSConstats.NSCARINFO_STATUS2);
						this.controlCarInfoDao.updateControlCarInfo(carinfo);
					}else{   // 返回实时数据（到入库的数据库表查询大于 记录时间上一生成时间的）
						gpsDataList = this.vehicleInfoDao.getGpsImmediateDataList(carinfo.getPlate(),carinfo.getLastGenTime());
						if(gpsDataList!=null&&gpsDataList.size()>0){ //查询到的数据集 生成GPS数据报文
							this.cretateGpsDataFile(gpsDataList, carinfo.getInareano(),carinfo.getReqUser());
							VehicleInfo vf  =gpsDataList.get(gpsDataList.size()-1);
							carinfo.setLastGenTime( vf.getGpstime());
							this.controlCarInfoDao.updateControlCarInfo(carinfo);   //更新时间
						}
					}
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
			log.info("系统产生异常"+ex.getMessage());
		}finally{
			lock.unlock(); //释放锁
		}
	}
	
	
	/**
	 * @功能: 生成报文 
	 * @编码: luyd luyuandeng@gzeport.com 2013-6-9 上午11:31:51
	 */
	private void  cretateGpsDataFile(List <VehicleInfo> mylist,String inAreaNo ,String reqUser){
		if(mylist!=null&&mylist.size()>0){
  			String fileNameBase =this.gpsFileManagerBean.getLocalBaseDir()+File.separator+this.gpsFileManagerBean.getLocalUploadDir()+File.separator+reqUser;
  	  		File f = new File(fileNameBase);
  	  		if(!f.exists()) {  f.mkdirs();  }  //如查目录不存在则创建
  			VehicleInfoXmlBean xmlBean =null ;
  	  		for(VehicleInfo vinfo :mylist){
  	  			XStream xs = new XStream(new XppDriver(new XmlFriendlyReplacer("_-", "_")));
  					xs.alias("VehicleInfo", VehicleInfoXmlBean.class);
  					vinfo.setInAreaNo(inAreaNo);   //设置入区编号  车次
  					xmlBean = new VehicleInfoXmlBean(vinfo); //待传入对象转换为XML对象
  					String xml = NSGPSConstats.PATTERN + StringUtil.trim(xs.toXML(xmlBean));
  					String fileName =fileNameBase+ File.separator+"GPS"+StringUtil.parseDateToString(new Date(),"yyyyMMdd")+StringUtil.getUniqueId()+".xml";
  					StringUtil.doc2XmlFileOneLine(xml, fileName, null);  //生成数据报文
  	  		}
  		}
	}
	

}
