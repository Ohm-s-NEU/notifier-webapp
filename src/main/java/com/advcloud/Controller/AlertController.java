package com.advcloud.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.advcloud.KafkaProducerService;
import com.advcloud.Model.Alert;
import com.advcloud.Model.Webapp;
import com.advcloud.Service.AlertService;
import com.advcloud.Service.Service;
import com.advcloud.notifier.dao.AlertDao;
import com.advcloud.webapp.dao.WebappDao;

@Controller
@Component
public class AlertController {

	@Autowired
	private Service service;

	@Autowired
	AlertService alertService;

	@Autowired
	WebappDao webappDao;

	@Autowired
	AlertDao alertDao;

	@Autowired
	KafkaProducerService kafkaProducerService;

	@Scheduled(initialDelay = 0, fixedRate = 300000)
	public void startNotifierEvery5mins() throws IOException {

		List<Webapp> webappList = new ArrayList<Webapp>();
		webappList = webappDao.findAllLatest();

		for (Webapp a : webappList) {
			Alert returnedAlert = (Alert) alertService.addAlertsToNotifierDB(a);
			Webapp initialwebapp = alertService.updateAlertStatus(a);
		}

		List<Alert> alertList = new ArrayList<Alert>();
		alertList = alertDao.findAllLatestForAlert();

		for (Alert a : alertList) {
			if (a !=null) {
				JSONObject searchedElasticData = searchElasticIndex(a.getCategory(), a.getKeyword());
				if (searchedElasticData != null) {
					boolean shootEmail = sendEmail(searchedElasticData, a.getUserName());
					if (shootEmail == true) {
						Alert finalAlert = alertService.changeMailStatus(a);
					}
				} else {
					System.out.println("Error retrieving elasticsearch data");
				}
			} else {
				System.out.println("Error accessing notifierAlerts table");
			}
		}
	}

	private boolean sendEmail(JSONObject data, String userName) {
		service.sendEmail(data,userName);
		return true;
	}

	private RestHighLevelClient restHighLevelClient() {
		String esHost = "100.71.110.133";
		Integer esPort = 9200;
		return new RestHighLevelClient(RestClient.builder(new HttpHost(esHost, esPort)));
	}

	private JSONObject searchElasticIndex(String category, String keyword) throws IOException {
		// TODO Auto-generated method stub
		// Create a Bool query
		BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
		String a = "*"+category+"*";
		System.out.println(a);
		boolQuery.must(QueryBuilders.matchQuery("title", a));
		// Create a search request
		// pass your indexes in place of indexA, indexB
		SearchRequest searchRequest = new SearchRequest("top");
		// CReate a search Source
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(boolQuery);
		searchRequest.source(searchSourceBuilder);
		// Create object to get Response
		SearchResponse searchResponse = restHighLevelClient().search(searchRequest, RequestOptions.DEFAULT);
		// Parsing response
		SearchHit[] searchHits = searchResponse.getHits().getHits();
		System.out.println(searchHits);
		if (searchHits.length == 0) {
			return null;
		}

		List<Map<String, Object>> list = new ArrayList<>();		
		for( SearchHit s : searchHits) {
			Map<String,Object> sourceAsMap = new HashMap<>();
			sourceAsMap.put("id",s.getSourceAsMap().get("id"));
			sourceAsMap.put("title",s.getSourceAsMap().get("title"));
			sourceAsMap.put("url",s.getSourceAsMap().get("url"));
			list.add(sourceAsMap);
		}
		
		List<JSONObject> jsonObj = new ArrayList<JSONObject>();
		for(Map<String, Object> data : list) {
		    JSONObject obj = new JSONObject(data);
		    jsonObj.add(obj);
		}
		JSONArray test = new JSONArray(jsonObj);
		
		JSONObject res = new JSONObject();
		res.put("data", test);
		
		System.out.println(res.toString());


		return res;
	}

}