package com.advcloud.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
			return null;
		}
	}

	public void updateWebappAlert(Webapp web) {
		try {
			if (web != null) {
				web.setStatus(2);
				webappDao.save(web);
			} else {
				System.out.println("Error updating webapp alert db");
			}
		} catch (Exception e) {
			System.out.println("Error occured. At Exception");
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
			return null;
		}
	}

}