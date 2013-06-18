package com.gzeport.app.gps.help;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import com.google.gson.Gson;
import com.gzeport.app.gps.common.NSGPSConstats;
import com.gzeport.app.gps.dao.IVehicleInfoDao;
import com.gzeport.app.gps.pojo.VehicleInfo;

public class CarGpsDataHelp {
	Log log = LogFactory.getLog(this.getClass().getName());
	
	private RedisTemplate<Serializable, Serializable> redisTemplate;
	
	private IVehicleInfoDao vehicleInfoDao;
	
	public IVehicleInfoDao getVehicleInfoDao() {
		return vehicleInfoDao;
	}
	public void setVehicleInfoDao(IVehicleInfoDao vehicleInfoDao) {
		this.vehicleInfoDao = vehicleInfoDao;
	}


	private  static Gson gson = new Gson();
	
	public RedisTemplate<Serializable, Serializable> getRedisTemplate() {
		return redisTemplate;
	}


	public void setRedisTemplate(
			RedisTemplate<Serializable, Serializable> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	
	/**
	 * @功能: GPS数据采集的方法 
	 * @编码: luyd luyuandeng@gzeport.com 2013-5-24 下午2:19:15
	 */
	public void getGpsDataByJredis(){
    	 VehicleInfo vinfo= null;
    	 try{
    	   	 vinfo=	(VehicleInfo) this.getRedisTemplate().execute(new RedisCallback<Object>() {   
    			 public  VehicleInfo  doInRedis(RedisConnection connection)   
    			      throws DataAccessException {  
    					byte[] cardata= connection.rPop(redisTemplate.getStringSerializer().serialize(NSGPSConstats.EPORT_QUEUE_GPS));   
    					VehicleInfo info  = null;
    					if(cardata!=null){ //如果从队列取到了数据
    						String jsonStr =redisTemplate.getStringSerializer().deserialize(cardata);
    //log.debug("cardata is >>"+jsonStr); 
    					    info = gson.fromJson(jsonStr, VehicleInfo.class);   
    					    String carPlate = info.getPlate();
    					    info.setPlatecolor(carPlate.substring(carPlate.length()-1));
    					    info.setPlate(carPlate.substring(0,carPlate.length()-2));
    					}
    					return info ;
    			 	}
    		});
    	 }catch(Exception ex){
    		 log.info("系统出现异常"+ex.getMessage());
    	 }
		//最后入物理数据库操作
    	 if(vinfo!=null){
    		 this.vehicleInfoDao.addVehicleInfo(vinfo);	 
    	 }
	}
	
}
