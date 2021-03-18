package com.advcloud.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.advcloud.notifier.dao.HealthDao;

@RestController
public class HealthController {

	@Autowired
	HealthDao healthdao;

	@RequestMapping(value = "/readyStatus", method = RequestMethod.GET)
	public ResponseEntity<?> readinessProbe() {
		try {
			int status = healthdao.checkDbStatus();
			if (status == 1) {
				return ResponseEntity.ok("Application is Ready");
			} else {
				return new ResponseEntity<>("DB_SERVICE_UNAVAILABLE ", HttpStatus.SERVICE_UNAVAILABLE);
			}
		} catch (Exception e) {
			return new ResponseEntity<>("SERVICE_UNAVAILABLE ", HttpStatus.SERVICE_UNAVAILABLE);
		}

	}

	@RequestMapping(value = "/healthStatus", method = RequestMethod.GET)
	public ResponseEntity<?> livenessProbe() {
		try {
			int status = healthdao.checkDbStatus();
			if (status == 1) {
				return ResponseEntity.ok("Application is Healthy");
			} else {
				return new ResponseEntity<>("DB_UNAVAILABLE_APPLICATION_UNHEALTHY ", HttpStatus.SERVICE_UNAVAILABLE);
			}
		} catch (Exception e) {
			return new ResponseEntity<>("APPLICATION_UNHEALTHY ", HttpStatus.SERVICE_UNAVAILABLE);

		}

	}

}