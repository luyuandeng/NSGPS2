package com.gzeport.app.gps.pojo;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "T_CONTROL_CARINFO", schema = "PORTAL")
public class ControlCarInfo implements java.io.Serializable {

	private static final long serialVersionUID = -6822364632733757253L;
	private Long seqid;
	private String plate;
	private String inareano;
	private String status; 
	private Date startTime;
	private Date stopTime; 
	private String isHistory;
	private Date lastGenTime;
	private String reqUser;
	

	// Constructors

	

	/** default constructor */
	public ControlCarInfo() {
	}

	/** minimal constructor */
	public ControlCarInfo(Long seqid, String plate, String inareano,
			String status) {
		this.seqid = seqid;
		this.plate = plate;
		this.inareano = inareano;
		this.status = status;
	}

	/** full constructor */
	public ControlCarInfo(Long seqid, String plate, String inareano,
			String status, Date starttime, Date stoptime) {
		this.seqid = seqid;
		this.plate = plate;
		this.inareano = inareano;
		this.status = status;
		this.startTime = starttime;
		this.stopTime = stoptime;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="seqid_nscarinfo")
	@SequenceGenerator(name="seqid_nscarinfo",sequenceName="PORTAL.NSCARINFO_SEQ",allocationSize=1)
	@Column(name = "SEQID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getSeqid() {
		return this.seqid;
	}

	public void setSeqid(Long seqid) {
		this.seqid = seqid;
	}

	@Column(name = "PLATE", nullable = false, length = 10)
	public String getPlate() {
		return this.plate;
	}

	public void setPlate(String plate) {
		this.plate = plate;
	}

	@Column(name = "IN_AREA_NO", nullable = false, length = 15)
	public String getInareano() {
		return this.inareano;
	}

	public void setInareano(String inareano) {
		this.inareano = inareano;
	}

	@Column(name = "STATUS", nullable = false, length = 1)
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "START_TIME", length = 18)
	public Date getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "STOP_TIME", length = 18)
	public Date getStopTime() {
		return this.stopTime;
	}

	public void setStopTime(Date stopTime) {
		this.stopTime = stopTime;
	}

	@Column(name = "IS_HISTORY", nullable = false, length = 1)
	public String getIsHistory() {
		return isHistory;
	}

	public void setIsHistory(String isHistory) {
		this.isHistory = isHistory;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_GEN_TIME", length = 18)
	public Date getLastGenTime() {
		return lastGenTime;
	}

	public void setLastGenTime(Date lastGenTime) {
		this.lastGenTime = lastGenTime;
	}
	
	@Column(name = "REQ_USER", nullable = false, length = 30)
	public String getReqUser() {
		return reqUser;
	}

	public void setReqUser(String reqUser) {
		this.reqUser = reqUser;
	}

}