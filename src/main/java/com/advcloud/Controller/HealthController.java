package com.advcloud.Controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	
	private static final Logger logger = LoggerFactory.getLogger(HealthController.class);

	@RequestMapping(value = "/readyStatus", method = RequestMethod.GET)
	public ResponseEntity<?> readinessProbe() {
		try {
			int status = healthdao.checkDbStatus();
			if(status == 1) {
                logger.info("********************************NOTIFIER Application is Ready********************************");
                return ResponseEntity.ok("Application is Ready");
            } else {
                logger.warn("********************************NOTIFIER - DB_SERVICE_UNAVAILABLE******************************** ");
                return new ResponseEntity<>("DB_SERVICE_UNAVAILABLE ", HttpStatus.SERVICE_UNAVAILABLE);
            }
		} catch (Exception e) {
			logger.error("********************************NOTIFIER - DB_SERVICE_UNAVAILABLE******************************** ");
			return new ResponseEntity<>("SERVICE_UNAVAILABLE ", HttpStatus.SERVICE_UNAVAILABLE);
		}

	}

	@RequestMapping(value = "/healthStatus", method = RequestMethod.GET)
	public ResponseEntity<?> livenessProbe() {
		try {
			int status = healthdao.checkDbStatus();
			if(status == 1) {
                logger.info("********************************NOTIFIER Application is Healthy********************************");
                return ResponseEntity.ok("Application is Ready");
            } else {
                logger.warn("********************************NOTIFIER - DB_SERVICE_UNAVAILABLE_APPLICATION_UNHEALTHY******************************** ");
                return new ResponseEntity<>("DB_SERVICE_UNAVAILABLE ", HttpStatus.SERVICE_UNAVAILABLE);
            }
		} catch (Exception e) {
			logger.error("********************************NOTIFIER - APPLICATION_UNHEALTHY******************************** ");
			return new ResponseEntity<>("APPLICATION_UNHEALTHY ", HttpStatus.SERVICE_UNAVAILABLE);

		}

	}

}