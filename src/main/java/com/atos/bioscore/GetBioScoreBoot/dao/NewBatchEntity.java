package com.atos.bioscore.GetBioScoreBoot.dao;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="collect_riddata")
public class NewBatchEntity {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private long id;
	
	@Column(name = "rid", length = 300)
	private String rid;
	
	@Column(name = "create_date")
	private LocalDateTime creationDate;
	
	@Column(name = "reg_type",length = 50)
	private String regType;
	
	@Column(name = "processed")
	private String processed;

	public NewBatchEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

	public NewBatchEntity(long id, String rid, LocalDateTime creationDate, String regType, String processed) {
		super();
		this.id = id;
		this.rid = rid;
		this.creationDate = creationDate;
		this.regType = regType;
		this.processed = processed;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getRid() {
		return rid;
	}

	public void setRid(String rid) {
		this.rid = rid;
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}

	public String getRegType() {
		return regType;
	}

	public void setRegType(String regType) {
		this.regType = regType;
	}

	public String getProcessed() {
		return processed;
	}

	public void setProcessed(String processed) {
		this.processed = processed;
	}
	
}
