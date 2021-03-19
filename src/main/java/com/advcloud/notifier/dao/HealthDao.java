package com.advcloud.notifier.dao;

import com.advcloud.Model.Alert;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HealthDao extends CrudRepository<Alert, Integer> {
	
	 @Query(value = "select 1", nativeQuery = true)
	 int checkDbStatus();

}