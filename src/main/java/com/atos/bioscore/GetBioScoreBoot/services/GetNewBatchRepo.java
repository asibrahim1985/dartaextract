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

import com.atos.bioscore.GetBioScoreBoot.dao.NewBatchRepoEntity;
import com.atos.bioscore.GetBioScoreBoot.repo.NewBatchRepoRepo;
import com.atos.bioscore.GetBioScoreBoot.repo.RidDataIdrepoRepo;
import com.zaxxer.hikari.HikariDataSource;

@Service
public class GetNewBatchRepo {

	Logger logger = LoggerFactory.getLogger(GetNewBatchRepo.class);
	
	@Qualifier("mvDatasource")
	@Autowired
	private HikariDataSource hdsmv;
	
	@Qualifier("regprcDatasource")
	@Autowired
	private HikariDataSource hdsreg;
	
	@Autowired
	NewBatchRepoRepo newBatchRepo;
	
	@Autowired
	RidDataIdrepoRepo RidDataIdreporepo;
	
	public void fillProcessingTable() {
		
		LocalDateTime maxCreateDate = RidDataIdreporepo.getMaxCreateDate();
		newBatchRepo.trunTable();
		logger.info("Max creation date found is:" + maxCreateDate);
		logger.info("Starting Filling Processing Table");
		if(maxCreateDate != null)
			fillTablewithDate(maxCreateDate);
		else
			fillTableFirstTime();
		logger.info("Processing Table Filled Successfully");
	}
	
	public void fillTablewithDate(LocalDateTime maxCreateDate) { 
//		Connection con = null;
		Connection conreg = null;
		try {
//			con = hdsmv.getConnection();
			conreg = hdsreg.getConnection();
			Statement stmt = conreg.createStatement();
//			String sql = "select id ,reg_type ,cr_dtimes  from registration where cr_dtimes  >= '";
//			sql = sql +  maxCreateDate + "' order by cr_dtimes";
			String sql = "select id ,reg_type ,cr_dtimes  from registration r where cr_dtimes <  '2022-01-18'  and reg_type = 'NEW'";
			logger.info("SQL DATE:" + sql);
			ResultSet allrs = stmt.executeQuery(sql);
			while(allrs.next()) {
				NewBatchRepoEntity ent = new NewBatchRepoEntity();
				ent.setRid(allrs.getString(1));
				ent.setRegType(allrs.getString(2));
				ent.setCreationDate(allrs.getTimestamp(3).toLocalDateTime());
				ent.setProcessed("");
				ent.setErrmg("");
				getUinfromRid(conreg,ent);
				newBatchRepo.save(ent);
			}
		}
		catch(Exception e) {
			logger.error("Failed to get new Batch:" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void fillTableFirstTime() {
//		Connection con = null;
		Connection conreg = null;
		try {
//			con = hdsmv.getConnection();
			conreg = hdsreg.getConnection();
			Statement stmt = conreg.createStatement();
			ResultSet allrs = stmt.executeQuery("select id ,reg_type ,cr_dtimes  from registration");
//			ResultSet allrs = stmt.executeQuery("select regid , reg_type ,ent_date from mv_cases mc1 where mc1.ent_date = (select max(mc2.ent_date) from mv_cases mc2 where mc1.regid = mc2.regid )");
			while(allrs.next()) {
				NewBatchRepoEntity ent = new NewBatchRepoEntity();
				ent.setRid(allrs.getString(1));
				ent.setRegType(allrs.getString(2));
				ent.setCreationDate(allrs.getTimestamp(3).toLocalDateTime());
				ent.setProcessed("");
				ent.setErrmg("");
				getUinfromRid(conreg,ent);
				newBatchRepo.save(ent);
			}
		}
		catch(Exception e) {
			logger.error("Failed to get new Batch:" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	private void getUinfromRid(Connection conreg , NewBatchRepoEntity ent ) {
		try {
			Statement stmt = conreg.createStatement();
			ResultSet allrs = stmt.executeQuery("SELECT uin FROM rid_uin_link where reg_id = '" + ent.getRid() + "'");
			if(allrs.next()) {
				ent.setUin(allrs.getString(1));
			} else {
				ent.setErrmg("Could not get UIN from RID");
			}
		}
		catch(Exception e) {
			ent.setErrmg("Could not get UIN from RID");
			logger.error("Failed to get new Batch:" + e.getMessage());
			e.printStackTrace();
		}
		
	}
}
