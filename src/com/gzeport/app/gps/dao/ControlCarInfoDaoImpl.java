package com.gzeport.app.gps.dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.gzeport.app.gps.common.NSGPSConstats;
import com.gzeport.app.gps.pojo.ControlCarInfo;
/**
 * @ClassName NsCarInfoDaoImpl
 * @Description 监控车辆信息
 * @author luyd luyuandeng@gzeport.com
 * @date 2013-5-30
 */
public class ControlCarInfoDaoImpl extends HibernateDaoSupport implements IControlCarInfoDao {

	Log log = LogFactory.getLog(this.getClass().getName());
	
	
	private IVehicleInfoDao  vehInfoDAO  ;

	public IVehicleInfoDao getVehInfoDAO() {
		return vehInfoDAO;
	}

	public void setVehInfoDAO(IVehicleInfoDao vehInfoDAO) {
		this.vehInfoDAO = vehInfoDAO;
	}



	@Override
	public void addVehicleInfo(ControlCarInfo instance) {
		
	}

	/**
	 * @功能: 监控车辆列表 
	 * @编码: luyd luyuandeng@gzeport.com 2013-5-31 下午3:51:35
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ControlCarInfo> getNsCarINfoList() {
		List<ControlCarInfo>  list = null;
		list = (List<ControlCarInfo> ) this.getHibernateTemplate().execute(new HibernateCallback(){
				public Object doInHibernate(Session session) throws HibernateException, SQLException {
					String sql  = " select tb.* from t_control_carinfo tb where tb.seqid in " +
							"(select mytab.sid from (select max(t.seqid) sid, t.plate,t.req_user " +
							"from t_control_carinfo t  group by t.plate,t.req_user) mytab) ";
              					Query query = session.createSQLQuery(sql).addEntity(ControlCarInfo.class);
              				//	query.setFirstResult(0);
              					//query.setMaxResults(15);
					List <ControlCarInfo> infolist =query.list();
					return infolist;
				}});
		return list;
	}

	
	/**
	 * @功能: 监控车辆列表 
	 * @编码: luyd luyuandeng@gzeport.com 2013-5-31 下午3:51:35
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ControlCarInfo> getControlCarInfoList() {
		List<ControlCarInfo>  list = null;
		list = (List<ControlCarInfo> ) this.getHibernateTemplate().execute(new HibernateCallback(){
				public Object doInHibernate(Session session) throws HibernateException, SQLException {
					String hql = " from ControlCarInfo cf  where cf.status = '1' ";
              					Query query = session.createQuery(hql);
              				//	query.setFirstResult(0);
              					//query.setMaxResults(15);
					List <ControlCarInfo> infolist =query.list();
					return infolist;
				}});
		return list;
	}


	
	public boolean  updateControlCarInfo(Object o  ){
		this.getHibernateTemplate().update(o);
		return true;
	}
	/**
	 * @功能:  更新状态
	 * @编码: luyd luyuandeng@gzeport.com 2013-6-17 下午2:50:46
	 */
	public boolean updateControlCarStatus(String seqId,  String status) {
		try{
			ControlCarInfo cf =this.getHibernateTemplate().get(ControlCarInfo.class, Long.valueOf(seqId));
			if(cf!=null){
				cf.setStatus(status);
				this.getHibernateTemplate().update(cf);
				return true;
			}
		}catch(Exception e){
			log.info("系统发生异常：:"+e.getMessage());
			e.printStackTrace();
		}
		return false;
	}
	
	
}
