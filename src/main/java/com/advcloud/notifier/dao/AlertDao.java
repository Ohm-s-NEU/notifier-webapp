package com.advcloud.notifier.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.advcloud.Model.Alert;
import com.advcloud.Model.Webapp;

public interface AlertDao extends CrudRepository<Alert, Integer> {
	

	@Query(value= "select alert from Alert alert where alert.status=0")
	List<Alert> findAllLatestForAlert();
	
}
