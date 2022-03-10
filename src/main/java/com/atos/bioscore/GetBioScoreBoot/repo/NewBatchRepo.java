package com.atos.bioscore.GetBioScoreBoot.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.atos.bioscore.GetBioScoreBoot.dao.NewBatchEntity;


public interface NewBatchRepo  extends JpaRepository<NewBatchEntity,Long>  {
	
	List<NewBatchEntity> findByProcessed(String proc);

	@Modifying
	@Transactional
	@Query(value="truncate table collect_riddata", nativeQuery = true)
	public void trunTable();
}
