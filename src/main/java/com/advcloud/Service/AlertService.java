package com.advcloud.Service;

import java.util.List;

import com.advcloud.Model.Alert;
import com.advcloud.Model.Webapp;

public interface AlertService {


	Alert addAlertsToNotifierDB(Webapp a);

	Alert changeMailStatus(Alert alert);

	Webapp updateAlertStatus(Webapp a);

	Webapp findWebappAlert(Alert finalAlert);

	void updateWebappAlert(Webapp web);

	Alert changeMailStatusForAlertsNotSent(Alert a);

	Webapp updateAlertStatusAfterMailSentInWebapp(Alert a);

}