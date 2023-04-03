package com.example.demo.service;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
/*
 * here i am trying to fetch the data form web to my application.
 */
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

import javax.annotation.PostConstruct;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.demoModels.LocationState;

@Service
public class coronaVirusDataService {
	
	private static String VURUS_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";
	
	private List<LocationState> allStats = new ArrayList<>();
	
	public List<LocationState> getAllStats(){
		return allStats;
	}
	
	@PostConstruct //for understand @postConstruct and @Service watch(12.55)
	@Scheduled(cron = "* * * * * *")
	public void fetchVirusData() throws IOException, InterruptedException {
		
		List<LocationState> newStats = new ArrayList<>();
		HttpClient client = HttpClient.newHttpClient();//only java 11 have HttpClient method.(so i use jdk 11)
		
		HttpRequest request = HttpRequest.newBuilder()
			.uri(URI.create(VURUS_DATA_URL))
			.build();
		
		HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
//		System.out.println(httpResponse.body());
		
		StringReader csvBodyReader = new StringReader(httpResponse.body());
		
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);
		
		for (CSVRecord record : records) {
			
			LocationState locationState = new LocationState();
			
		    locationState.setState(record.get("Province/State"));
		    locationState.setCountry(record.get("Country/Region"));
		    
		    int latestCases = Integer.parseInt(record.get(record.size()-1));
		    locationState.setLatestTotalCases(latestCases);
		    
		    int PreviousCases = Integer.parseInt(record.get(record.size()-2));
		    locationState.setDiffFromPrevDay(latestCases-PreviousCases);
		    
		    System.out.println(locationState.toString());
		    newStats.add(locationState);
		}
		this.allStats = newStats;
		        
		
	}
	
}
