package com.advcloud.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import com.advcloud.Model.Alert;
import com.advcloud.Model.Webapp;
import com.advcloud.Service.AlertService;
import com.advcloud.Service.SESService;
import com.advcloud.notifier.dao.AlertDao;
import com.advcloud.webapp.dao.WebappDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@Component
public class AlertController {

	@Autowired
	private SESService sesService;

	@Autowired
	AlertService alertService;

	@Autowired
	WebappDao webappDao;

	@Autowired
	AlertDao alertDao;

	@Value("${esHost}")
	private String esHost;

	private static final Logger logger = LoggerFactory.getLogger(AlertController.class);

	@Scheduled(initialDelay = 0, fixedRate = 300000)
	public void startNotifierEvery5mins() throws IOException {
		logger.info("Thread started to run in an interval of 5 minutes");

		List<Webapp> webappList = new ArrayList<Webapp>();
		webappList = webappDao.findAllLatest();

		for (Webapp a : webappList) {
			Alert returnedAlert = (Alert) alertService.addAlertsToNotifierDB(a);
			Webapp initialwebapp = alertService.updateAlertStatus(a);
		}

		logger.info("Alerts from Webapp DB are inserted in Notifier DB");

		List<Alert> alertList = new ArrayList<Alert>();
		alertList = alertDao.findAllLatestForAlert();

		logger.info("List of Latest Alerts Selected");

		for (Alert a : alertList) {
			if (a != null) {
				logger.info("Elasticsearch in progress");
				JSONObject searchedElasticData = searchElasticIndex(a.getCategory(), a.getKeyword());
				logger.info("elasticSearchData =", searchedElasticData);
				logger.info("Elasticsearch completed");
				if (searchedElasticData != null) {
					logger.info("Shooting email");
					boolean shootEmail = sendEmail(searchedElasticData, a.getUserName());
					if (shootEmail == true) {
						Alert finalAlert = alertService.changeMailStatus(a);
						if (finalAlert != null) {
							logger.info("Updated mail status as sent in notifier db");
						} else {
							logger.warn("Error updating mail status as sent in notifier db");
						}
						Webapp web = alertService.updateAlertStatusAfterMailSentInWebapp(a);
						if (web != null) {
							logger.info("Updated mail status as sent in webapp db");
						} else {
							logger.warn("Error updating mail status as sent in webapp db");
						}
					} else {
						logger.error("Error sending mail");
					}
				} else {
					logger.warn("Error retrieving elasticsearch data");
					Alert unusedAlert = alertService.changeMailStatusForAlertsNotSent(a);
					if (unusedAlert != null) {
						logger.info("Updated mail status as unsent in notifier db and webapp db");
					} else {
						logger.warn("Error updating mail status as unsent in webapp db and notifier db");
					}
				}
			} else {
				logger.warn("Error accessing notifier db table");
			}
		}
	}

	private boolean sendEmail(JSONObject data, String userName) {
		boolean mail = sesService.sendEmail(data, userName);
		if (mail = true) {
			logger.info("Mail has been sent through SES");
			return true;
		} else {
			logger.error("Error triggering mail through SES");
			return false;
		}
	}

	private RestHighLevelClient restHighLevelClient() {
		int esPort = 9200;
		return new RestHighLevelClient(RestClient.builder(new HttpHost(esHost, esPort)));
	}

	private JSONObject searchElasticIndex(String category, String keyword) throws IOException {
		// TODO Auto-generated method stub
		// Create a Bool query
		BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
		boolQuery.must(QueryBuilders.matchQuery("title", "*" + keyword + "*"));
		// Create a search request
		// pass your indexes in place of indexA, indexB
		SearchRequest searchRequest = new SearchRequest(category);
		// CReate a search Source
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(boolQuery);
		searchRequest.source(searchSourceBuilder);
		// Create object to get Response
		SearchResponse searchResponse = restHighLevelClient().search(searchRequest, RequestOptions.DEFAULT);
		// Parsing response
		SearchHit[] searchHits = searchResponse.getHits().getHits();
		if (searchHits.length == 0) {
			logger.warn("No elastic search match found");
			return null;
		} else {
			logger.info("Elastic search match found");
		}

		List<Map<String, Object>> list = new ArrayList<>();
		for (SearchHit s : searchHits) {
			Map<String, Object> sourceAsMap = new HashMap<>();
			sourceAsMap.put("title", s.getSourceAsMap().get("title"));
			sourceAsMap.put("url", s.getSourceAsMap().get("url"));
			list.add(sourceAsMap);
		}

		List<JSONObject> jsonObj = new ArrayList<JSONObject>();
		for (Map<String, Object> data : list) {
			JSONObject obj = new JSONObject(data);
			jsonObj.add(obj);
		}
		JSONArray test = new JSONArray(jsonObj);
		
		JSONObject res = new JSONObject();
		res.put("data", test);
		logger.info("Added elastic search match to json object");
		return res;
	}

}