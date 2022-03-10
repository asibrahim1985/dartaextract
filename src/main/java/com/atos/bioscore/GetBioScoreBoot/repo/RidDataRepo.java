package com.atos.bioscore.GetBioScoreBoot.repo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.atos.bioscore.GetBioScoreBoot.dao.RidData;


public interface RidDataRepo extends JpaRepository<RidData,Long> {

	@Query(value="SELECT max(create_date)  from  rid_data",nativeQuery = true)
	LocalDateTime getMaxCreateDate();
	
	List<RidData>  findByExtracted(String ext);
	
	List<RidData>  findByExtractedAndStatus(String ext,String status);
	
	@Query(value="SELECT id  from  rid_data where status = 'SUCCESS' and extracted = '' ",nativeQuery = true)
	List<Long> getExtractids();
	
}
