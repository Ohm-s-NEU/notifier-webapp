package com.advcloud.webapp.dao;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.advcloud.Model.Webapp;

public interface WebappDao extends CrudRepository<Webapp, Integer> {
	

	@Query(value= "select webapp from Webapp webapp where webapp.status=0")
	List<Webapp> findAllLatest();
	
}

