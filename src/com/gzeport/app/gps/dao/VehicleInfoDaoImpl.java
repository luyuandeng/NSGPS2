package com.gzeport.app.gps.dao;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.gzeport.app.gps.common.NSGPSConstats;
import com.gzeport.app.gps.common.ResponseXmlBean;
import com.gzeport.app.gps.pojo.ControlCarInfo;
import com.gzeport.app.gps.pojo.VehicleInfo;
import com.gzeport.app.gps.util.StringUtil;
/**
 * @ClassName VehicleInfoDaoImpl
 * @Description  GPS车辆信息 DAO层 开启与停止具体处理类
 * @author luyd luyuandeng@gzeport.com
 * @date 2013-5-28
 */
public class VehicleInfoDaoImpl extends AbstractDAO implements IVehicleInfoDao,Serializable {
	
	Log log = LogFactory.getLog(this.getClass().getName());
	
	private static final long serialVersionUID = -5942481889688418515L;
	
	private  Boolean isMustValiateUser ;
	
	
	public Boolean getIsMustValiateUser() {
		return isMustValiateUser;
	}
	public void setIsMustValiateUser(Boolean isMustValiateUser) {
		this.isMustValiateUser = isMustValiateUser;
	}

	/**
	 * @功能: 保存对象 
	 * @编码: luyd luyuandeng@gzeport.com
	 * @时间: 2012-10-30下午11:49:15 
	 */
	public void addVehicleInfo( VehicleInfo instance) {
		 this.getHibernateTemplate().saveOrUpdate(instance);
	}

	
	/**
	 * @功能: 启动GPS数据传输 请求
	 * @编码: luyd luyuandeng@gzeport.com 2013-5-24 下午3:23:12
	 */
	public ResponseXmlBean startGetGpsData(String userName, String password,
			String queryType, String plate, String inAreaNo, String sTime,
			String eTime) {
		ResponseXmlBean xmlbean = new ResponseXmlBean();
		xmlbean.setInAreaNo(inAreaNo);
		xmlbean.setPlate(plate);
		xmlbean.setStartTime(sTime);
		xmlbean.setEndTime(eTime);
		xmlbean.setResponseCode(queryType);
		boolean  isSuccess =true ;
		if(isMustValiateUser==true) { //做一个是否需要用户验证的开关
			  isSuccess =	this.ValidateUser(userName, password, NSGPSConstats.SYS_FUNID); //用户验证
		}
		if(isSuccess){  //通过验证
			//判断必填的参数有没有为空
			if((queryType!=null&&plate!=null&&inAreaNo!=null&&sTime!=null)&&
					(!queryType.equals("")&&!plate.equals("")&&!inAreaNo.equals("")&&!sTime.equals(""))){
			}else {
				xmlbean.setResultCode( NSGPSConstats.RESPONSE_FAILURE);
				xmlbean.setMessage("failure: the args is not  right ,please check it!");
				return xmlbean;
			}
			try {
				if(queryType.equals(NSGPSConstats.REQUEST_TYPE_OF1)){ //开始请求
					Date startdate =null;
				    String[] plateArray= plate.split("\\|",-1);   //如果查询多个车牌号的是以|分隔的 所以要分割取出来
					String[] inAreaNoArray= inAreaNo.split("\\|",-1);    
					int len =plateArray.length;
					String carPlate =null;   
					String carAreaNo =null;
					/** * @查询历史数据 */
					  if(eTime!=null&&!"".equals(eTime)){ 
						    startdate = StringUtil.parseDate(sTime, "yyyy-MM-dd HH:mm:ss");
						    Date endDate = StringUtil.parseDate(eTime, "yyyy-MM-dd HH:mm:ss");
							for(int i =0;i<len;i++){   //取得所有车牌号
							    carPlate =plateArray[i];
								carAreaNo =inAreaNoArray[i];
								this.updateSetData(userName,carPlate,carAreaNo,startdate,endDate, NSGPSConstats.CARPLATE_HIS); 
							}
							xmlbean.setResultCode( NSGPSConstats.RESPONSE_SUCCESS);
					  }else{ /***@请求实时数据的 */
					log.info(plate+"请求实时GPS数据！");
							startdate = StringUtil.parseDate(sTime, "yyyy-MM-dd HH:mm:ss");
							for(int i =0;i<len;i++){
								carPlate =plateArray[i];
							    carAreaNo =inAreaNoArray[i];
								if(carAreaNo!=null){
									this.updateSetData(userName,carPlate,carAreaNo,null,null, NSGPSConstats.CARPLATE_ADD);  // 更新数据库表	
								}
							}
							xmlbean.setResultCode( NSGPSConstats.RESPONSE_SUCCESS);
					  }
				}else{
					xmlbean.setResultCode( NSGPSConstats.RESPONSE_FAILURE);
					xmlbean.setMessage( " the requestType is not right!");
				}
			} catch (ParseException e) {
				xmlbean.setResultCode( NSGPSConstats.RESPONSE_FAILURE);
				xmlbean.setMessage( "  System has  exception  case by "+e.getMessage() );
				e.printStackTrace();
			}
			
		}else{ //用户验证不通过
			xmlbean.setResultCode( NSGPSConstats.RESPONSE_FAILURE);
			xmlbean.setMessage("userinfo validate error ,username or password input error!");
		}
 		return xmlbean;
	}
	
	
	/**
	 * @功能: 更新保存到  数据库 监控表
	 * @编码: luyd luyuandeng@gzeport.com 2013-5-20 下午3:49:30
	 */
	public Boolean updateSetData(final String userName, final String carNo,final String carAreaNo,final Date sDatetime,final Date eDateTime, final String flag){
		Boolean bool= true;
		
		try{ 
			if(NSGPSConstats.CARPLATE_HIS.equals(flag)){ // 查询历史 数据 请求
				 ControlCarInfo	 nsCarInfo =  new ControlCarInfo();
				 nsCarInfo.setPlate(carNo);
				 nsCarInfo.setInareano(carAreaNo);
				 nsCarInfo.setStatus(NSGPSConstats.NSCARINFO_STATUS1);
				 nsCarInfo.setStartTime(sDatetime);
				 nsCarInfo.setStopTime(eDateTime);
				 nsCarInfo.setIsHistory(NSGPSConstats.NSCARINFO_ISHISTORY1);
				 nsCarInfo.setReqUser(userName);
				 this.getHibernateTemplate().save(nsCarInfo);
			}else{
				ControlCarInfo nsCarInfo = this.hasControllingCarInfo(userName,carNo,carAreaNo);  //检查是否有正在监控 传输GPS
				 if(NSGPSConstats.CARPLATE_ADD.equals(flag)){  //启动请求
					 if(nsCarInfo!=null) {  //已启动    属重复请求  更新
						 nsCarInfo.setStopTime(new Date());
						 nsCarInfo.setStatus(NSGPSConstats.NSCARINFO_STATUS2);
						 this.getHibernateTemplate().update(nsCarInfo) ;
					 }  //没有 新请求
					 nsCarInfo =  new ControlCarInfo();
					 nsCarInfo.setPlate(carNo);
					 nsCarInfo.setInareano(carAreaNo);
					 nsCarInfo.setStatus(NSGPSConstats.NSCARINFO_STATUS1);
					 nsCarInfo.setStartTime(new Date());
					 nsCarInfo.setLastGenTime(new Date());
					 nsCarInfo.setIsHistory(NSGPSConstats.NSCARINFO_ISHISTORY0);
					 nsCarInfo.setReqUser(userName);
					 this.getHibernateTemplate().save(nsCarInfo);
					 
				 }else if(NSGPSConstats.CARPLATE_DEL.equals(flag)){ //停止请求
					 if(nsCarInfo!=null) {
						// log.info("进入更新停止车辆状态方法"+nsCarInfo.getSeqid()+nsCarInfo.getPlate());
						 nsCarInfo.setStatus(NSGPSConstats.NSCARINFO_STATUS2);    //2停止监控
						 nsCarInfo.setStopTime(new Date());
						 this.getHibernateTemplate().update(nsCarInfo) ;
					 }
				 }
			}
		}catch(Exception ex){
			bool =false;
			log.info("系统发生异常："+ex.getMessage());
		}
		return bool;
	}

	/**
	 * @功能: 检查是否存在 存在则返回记录 不存在返回空 
	 * @编码: luyd luyuandeng@gzeport.com 2013-5-28 下午3:13:14
	 */
	@SuppressWarnings("unchecked")
	private ControlCarInfo hasControllingCarInfo(String reqUser,String carNo,String inAreaNo) {
		ControlCarInfo carinfo =null;
		String hql ="from ControlCarInfo cf where cf.reqUser =?  and  cf.plate = ? and cf.inareano=? and cf.status='1' and cf.isHistory='0' ";
		List<ControlCarInfo> list =this.getHibernateTemplate().find(hql,new Object[]{reqUser,carNo,inAreaNo});
		if(list!=null&&list.size()>0){
			carinfo =(ControlCarInfo) list.get(0);	
		} 
		return carinfo;
	}


	/**
	 * @功能: 请求停止传输GPS实时数据 
	 * @编码: luyd luyuandeng@gzeport.com 2013-5-27 上午10:09:26
	 */
	@Override
	public ResponseXmlBean stopGetGpsData(String userName, String password,
			String queryType, String plate, String inAreaNo, String sTime) {
		ResponseXmlBean xmlbean = new ResponseXmlBean();
		xmlbean.setInAreaNo(inAreaNo);
		xmlbean.setPlate(plate);
		xmlbean.setEndTime(sTime);
		xmlbean.setResponseCode(queryType);
		boolean  isSuccess =true ;
		if(isMustValiateUser==true) { //做一个是否需要用户验证的开关
			  isSuccess =	this.ValidateUser(userName, password, NSGPSConstats.SYS_FUNID); //用户验证
		}
		if(isSuccess){  //通过验证
			if((queryType!=null&&plate!=null&&inAreaNo!=null&&sTime!=null)&&
					(!queryType.equals("")&&!plate.equals("")&&!inAreaNo.equals("")&&!sTime.equals(""))){
			}else {
				xmlbean.setResultCode( NSGPSConstats.RESPONSE_FAILURE);
				xmlbean.setMessage("failure: the args is not  right ,please check it!");
				return xmlbean;
			}
			try{
				if(queryType.equals(NSGPSConstats.REQUEST_TYPE_OF2)){ //停止请求
					String[] plateArray= plate.split("\\|",-1);
					String[] inAreaNoArray= inAreaNo.split("\\|",-1);    
					 int len =plateArray.length;
					for(int i =0;i<len;i++){
						String carPlate =plateArray[i];
						String areaNo =inAreaNoArray[i];
					  	this.updateSetData(userName,carPlate,areaNo,null,null, NSGPSConstats.CARPLATE_DEL);  //更新监控表
					}
					xmlbean.setResultCode( NSGPSConstats.RESPONSE_SUCCESS);
				}else{
					xmlbean.setResultCode( NSGPSConstats.RESPONSE_FAILURE);
					xmlbean.setMessage( " the requestType is not right!");
				}
			}catch(Exception ex){
				xmlbean.setResultCode( NSGPSConstats.RESPONSE_FAILURE);
				xmlbean.setMessage("the system has Exception case by !"+ex.getMessage());
				log.info("系统产生异常"+ex.getMessage());
				ex.printStackTrace();
			}
		}else{
			xmlbean.setResultCode( NSGPSConstats.RESPONSE_FAILURE);
			xmlbean.setMessage("userinfo validate error ,username or password input error!");
		}
 		return xmlbean;
	}
	/**
	 * @功能: 查询历史数据DAO实现 
	 * @编码: luyd luyuandeng@gzeport.com 2013-6-9 上午10:43:28
	 */
	@SuppressWarnings("unchecked")
	public List<VehicleInfo> getHistoryGpsDataList(String plate,
			Date startTime, Date stopTime) {
		List <VehicleInfo> gpsDataList = null;
		String queryString = "from VehicleInfo   cf  where cf.Plate = ?  and cf.Gpstime >=? and cf.Gpstime < ? order by cf.Gpstime ";
		gpsDataList = this.getHibernateTemplate().find(queryString , new Object[]{plate,startTime,stopTime});
		return gpsDataList;
	}
	
	/**
	 * @功能: 返回实时数据列表 
	 * @编码: luyd luyuandeng@gzeport.com 2013-6-9 上午11:10:38
	 */
	@SuppressWarnings("unchecked")
	public List<VehicleInfo> getGpsImmediateDataList(String plate,
			Date lastGenTime) {
		List <VehicleInfo> gpsDataList = null;
		String queryString = "from VehicleInfo   cf  where cf.Plate = ?  and cf.Gpstime >? order by cf.Gpstime  ";
		gpsDataList = this.getHibernateTemplate().find(queryString , new Object[]{plate,lastGenTime });
		return gpsDataList;
	}
}
