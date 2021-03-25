package com.advcloud.Service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.advcloud.Controller.AlertController;
import com.advcloud.Model.Alert;
import com.advcloud.Model.Webapp;
import com.advcloud.notifier.dao.AlertDao;

import com.advcloud.webapp.dao.WebappDao;

@Service
public class AlertServiceImpl implements AlertService {

	@Autowired
	private WebappDao webappDao;

	@Autowired
	private AlertDao alertDao;
	
	private static final Logger logger = LoggerFactory.getLogger(AlertServiceImpl.class);

	public Alert addAlertsToNotifierDB(Webapp a) {

		try {
			if (a != null) {
				Alert alert = new Alert();
				alert.setUserName(a.getUserName());
				alert.setCategory(a.getCategory());
				alert.setKeyword(a.getKeyword());
				alert.setId(a.getId());
				alert.setStatus(1);
				alertDao.save(alert);
				return alert;
			} else {
				return null;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}

	}

	public Alert changeMailStatus(Alert alert) {

		try {
			if (alert != null) {
				alert.setStatus(2);
				alertDao.save(alert);
				return alert;
			} else {
				return null;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}

	}

	public Alert changeMailStatusForAlertsNotSent(Alert a) {

		try {
			if (a != null) {
				a.setStatus(4);
				alertDao.save(a);
				Webapp webapp = webappDao.findExistingAlert(a.getId(), a.getUserName());
				if (webapp != null) {
					webapp.setStatus(4);
					webappDao.save(webapp);
				}
				return a;
			} else {
				return null;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}

	}

	public Webapp findWebappAlert(Alert finalAlert) {
		try {
			if (finalAlert != null) {
				Webapp webapp = webappDao.findExistingAlert(finalAlert.getId(), finalAlert.getUserName());
				if (webapp != null) {
					return webapp;
				} else {
					return null;
				}
			}
			return null;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}

	}

	public Webapp updateAlertStatus(Webapp a) {
		try {
			if (a != null) {
				a.setStatus(1);
				webappDao.save(a);
				return a;
			} else {
				return null;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	public void updateWebappAlert(Webapp web) {
		try {
			if (web != null) {
				web.setStatus(2);
				webappDao.save(web);
			} else {
				logger.warn("Error updating webapp alert db");
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	public Webapp updateAlertStatusAfterMailSentInWebapp(Alert a) {
		try {
			if (a != null) {
				Webapp webapp = webappDao.findExistingAlert(a.getId(), a.getUserName());
				if (webapp != null) {
					webapp.setStatus(2);
					webappDao.save(webapp);
					return webapp;
				} else {
					return null;
				}
			}
			return null;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}

}