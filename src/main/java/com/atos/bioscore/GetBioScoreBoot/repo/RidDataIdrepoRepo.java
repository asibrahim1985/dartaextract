package com.atos.bioscore.GetBioScoreBoot.repo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.atos.bioscore.GetBioScoreBoot.dao.RidDataIdrepo;

public interface RidDataIdrepoRepo extends JpaRepository<RidDataIdrepo,Long>{
	
	@Query(value="SELECT max(create_date)  from  rid_data_idrepo",nativeQuery = true)
	LocalDateTime getMaxCreateDate();
	
	List<RidDataIdrepo>  findByExtracted(String ext);
	
	List<RidDataIdrepo>  findByExtractedAndStatus(String ext,String status);
	
	@Query(value="SELECT id  from  rid_data_idrepo where status = 'SUCCESS' and extracted = '' ",nativeQuery = true)
	List<Long> getExtractids();

}
