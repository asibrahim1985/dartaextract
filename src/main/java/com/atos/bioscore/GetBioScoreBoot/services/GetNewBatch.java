package com.atos.bioscore.GetBioScoreBoot.services;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.atos.bioscore.GetBioScoreBoot.dao.NewBatchEntity;
import com.atos.bioscore.GetBioScoreBoot.repo.NewBatchRepo;
import com.atos.bioscore.GetBioScoreBoot.repo.RidDataRepo;
import com.zaxxer.hikari.HikariDataSource;

@Service
public class GetNewBatch {
	
	
	
	Logger logger = LoggerFactory.getLogger(GetNewBatch.class);
	
	@Autowired
	RidDataRepo riddataRepo;
	
	@Autowired
	NewBatchRepo newBatch;
	
	@Qualifier("regprcDatasource")
	@Autowired
	private HikariDataSource hdsreg;
	
	public void fillProcessingTable() {
		LocalDateTime maxCreateDate = riddataRepo.getMaxCreateDate();
		newBatch.trunTable();
		logger.info("Max creation date found is:" + maxCreateDate);
		logger.info("Starting Filling Processing Table");
		if(maxCreateDate != null)
			fillTablewithDate(maxCreateDate);
		else
			fillTableFirstTime();
		logger.info("Processing Table Filled Successfully");
	}
	
	public void fillTablewithDate(LocalDateTime maxCreateDate) {
		Connection con = null;
		try {
			con = hdsreg.getConnection();
			Statement stmt = con.createStatement();
//			String sql = "select id ,reg_type ,cr_dtimes  from registration where cr_dtimes  >= '";
//			sql = sql +  maxCreateDate + "' order by cr_dtimes";
			String sql = "select id ,reg_type ,cr_dtimes from registration where  cr_dtimes >= '2022-02-17' and cr_dtimes < '2022-02-18' and ( id like '%10108%'  or  id like '%10169%' )";
			logger.info("SQL DATE:" + sql);
			ResultSet allrs = stmt.executeQuery(sql);
			while(allrs.next()) {
				NewBatchEntity ent = new NewBatchEntity();
				ent.setRid(allrs.getString(1));
				ent.setRegType(allrs.getString(2));
				ent.setCreationDate(allrs.getTimestamp(3).toLocalDateTime());
				ent.setProcessed("");
				newBatch.save(ent);
			}
		}
		catch(Exception e) {
			logger.error("Failed to get new Batch:" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void fillTableFirstTime() {
		Connection con = null;
		try {
			con = hdsreg.getConnection();
			Statement stmt = con.createStatement();
			ResultSet allrs = stmt.executeQuery("select id ,reg_type ,cr_dtimes  from registration");
			while(allrs.next()) {
				NewBatchEntity ent = new NewBatchEntity();
				ent.setRid(allrs.getString(1));
				ent.setRegType(allrs.getString(2));
				ent.setCreationDate(allrs.getTimestamp(3).toLocalDateTime());
				ent.setProcessed("");
				newBatch.save(ent);
			}
		}
		catch(Exception e) {
			logger.error("Failed to get new Batch:" + e.getMessage());
			e.printStackTrace();
		}
	}
	
}
