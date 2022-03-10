package com.atos.bioscore.GetBioScoreBoot.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.atos.bioscore.GetBioScoreBoot.dao.NewBatchRepoEntity;

public interface NewBatchRepoRepo extends JpaRepository<NewBatchRepoEntity,Long> {
	
	@Modifying
	@Transactional
	@Query(value="truncate table collect_repodata", nativeQuery = true)
	public void trunTable();
	
	public List<NewBatchRepoEntity> findByProcessedAndErrmg(String proc , String err);
	public List<NewBatchRepoEntity> findByProcessedAndErrmgOrderByCreationDateAsc(String proc , String err);

}
