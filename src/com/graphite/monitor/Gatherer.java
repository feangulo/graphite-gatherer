package com.graphite.monitor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Gatherer {

	/*
	 * Sample query:
	 * 
	 * http://graphite/render/?target=sumSeries(*.agents.nyc-graphite03*.cache.queries)&format=json&from=-720s&until=-120s
	 */
	public static void main(String args[]) throws Exception {
		
		String host = "";
		int from = 120; // by default, query two minutes ago
		int until = 60; // skip the latest minute (due to incomplete recent data)
		int sleep = 60000;
		int repeat = 0;
		
		if (args.length == 1) {
			host = args[0];
		} else if (args.length == 5) {
			host = args[0];
			from = Integer.valueOf(args[1]);
			until = Integer.valueOf(args[2]);
			sleep = Integer.valueOf(args[3]);
			repeat = Integer.valueOf(args[4]);
		} else {
			System.err.println("Need correct parameters...");
			System.exit(0);
		}
		
		while(true) {

			Map<String,Map<Long,Double>> allMetrics = new HashMap<String,Map<Long,Double>>();

			allMetrics.put("I1", queryMetric(host, from, until, Metrics.I1, repeat));
			allMetrics.put("I2", queryMetric(host, from, until, Metrics.I2, repeat));
			allMetrics.put("I3", queryMetric(host, from, until, Metrics.I3, repeat));
			allMetrics.put("I4", queryMetric(host, from, until, Metrics.I4, repeat));
			allMetrics.put("I5", queryMetric(host, from, until, Metrics.I5, repeat));

			Comparator.compare(allMetrics);
			allMetrics = new HashMap<String,Map<Long,Double>>();

			allMetrics.put("C1", queryMetric(host, from, until, Metrics.C1, repeat));
			allMetrics.put("C2", queryMetric(host, from, until, Metrics.C2, repeat));
			allMetrics.put("C3", queryMetric(host, from, until, Metrics.C3, repeat));
			allMetrics.put("C4", queryMetric(host, from, until, Metrics.C4, repeat));
			allMetrics.put("C5", queryMetric(host, from, until, Metrics.C5, repeat));

			Comparator.compare(allMetrics);
			allMetrics = new HashMap<String,Map<Long,Double>>();

			allMetrics.put("Q1", queryMetric(host, from, until, Metrics.Q1, repeat));
			allMetrics.put("Q2", queryMetric(host, from, until, Metrics.Q2, repeat));
			allMetrics.put("Q3", queryMetric(host, from, until, Metrics.Q3, repeat));
			allMetrics.put("Q4", queryMetric(host, from, until, Metrics.Q4, repeat));

			Comparator.compare(allMetrics);
			allMetrics = new HashMap<String,Map<Long,Double>>();

			allMetrics.put("LA", queryMetric(host, from, until, Metrics.LA, repeat));
			allMetrics.put("L4", queryMetric(host, from, until, Metrics.L4, repeat));
			allMetrics.put("L5", queryMetric(host, from, until, Metrics.L5, repeat));
			allMetrics.put("L6", queryMetric(host, from, until, Metrics.L6, repeat));

			Comparator.compare(allMetrics);
			allMetrics = new HashMap<String,Map<Long,Double>>();

			allMetrics.put("RA", queryMetric(host, from, until, Metrics.RA, repeat));
			allMetrics.put("R4", queryMetric(host, from, until, Metrics.R4, repeat));
			allMetrics.put("R5", queryMetric(host, from, until, Metrics.R5, repeat));
			allMetrics.put("R6", queryMetric(host, from, until, Metrics.R6, repeat));

			Comparator.compare(allMetrics);
		
			Thread.sleep(sleep);
		
		}
		
	}
	
	private static Map<Long,Double> queryMetric(String host, int from, int until, String metricPath, int repeat) {
		
		StringBuilder sb = new StringBuilder(host).append("/render/?target=")
									.append(metricPath)
									.append("&format=json&from=-")
									.append(from)
									.append("s&until=-")
									.append(until)
									.append("s");
		String endpoint = sb.toString();
		
		HttpGet getRequest = null;
		try {
			getRequest = buildGetRequest(endpoint);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		Map<Long,Double> results = new HashMap<Long,Double>();
		
		for (int i = 0; i < repeat; i++) {
			results = executeRequest(getRequest);
		}
		
		return results;
		
	}
	
	private static HttpGet buildGetRequest(String endpoint) throws Exception {
		
		URI uri = new URIBuilder().setScheme("http")
	   			   			      .setHost(endpoint)
	   			   			      .build();
		
		System.out.println("Executing request: " + uri);
		
		return new HttpGet(uri);
		
	}
	
	public static Map<Long,Double> executeRequest(HttpGet request) {
		
		CloseableHttpClient httpClient = HttpClientBuilder.create().disableAutomaticRetries().build();

		HttpResponse response = null;
		try {
			
			response = httpClient.execute(request);
			
			if (response != null) {
				
				if (response.getStatusLine() != null) {
					
					int responseCode = response.getStatusLine().getStatusCode();
					
					if (responseCode == 200) {
						
						InputStream inputStream = response.getEntity().getContent();
						ByteArrayOutputStream buffer = new ByteArrayOutputStream();
						
						int nRead;
						byte[] data = new byte[16384];
						
						while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
							buffer.write(data, 0, nRead);
						}
						
						buffer.flush();
						
						return parseJson(buffer.toByteArray());
						
					}
					
				} else {
				
					System.out.println("Received null status line");
					
				}
				
			} else {
			
				System.out.println("Received null response");
				
			}
			
		} catch (Exception ex) {
			System.out.println("Encountered exception: " + ex);
		}
		
		return null;
		
	}
	
	public static Map<Long, Double> parseJson(byte[] jsonResponse) throws JsonProcessingException, IOException {
		
		Map<Long, Double> responseValues = new HashMap<Long, Double>();
		
		ObjectMapper mapper = new ObjectMapper();
		
		JsonNode responseContainer = mapper.readTree(jsonResponse);
		
		Iterator<JsonNode> responseContainerIterator = responseContainer.iterator();
		
		while (responseContainerIterator.hasNext()) {
			
			JsonNode informationNode = responseContainerIterator.next();
			
			Iterator<String> informationFieldNames = informationNode.fieldNames();
			while (informationFieldNames.hasNext()) {
				
				String informationFieldName = informationFieldNames.next();
				
				if (informationFieldName.equals("datapoints")) {
					
					JsonNode datapoints = informationNode.path(informationFieldName);
					
					Iterator<JsonNode> datapointsIterator = datapoints.iterator();
					while (datapointsIterator.hasNext()) {
						
						JsonNode singleDatapoint = datapointsIterator.next();
						
						Iterator<JsonNode> singleDatapointIterator = singleDatapoint.iterator();
						while (singleDatapointIterator.hasNext()) {
							
							JsonNode value = singleDatapointIterator.next();
							JsonNode time = singleDatapointIterator.next();
							
							responseValues.put(time.asLong(), value.asDouble());
							
							
							
						}
						
					}
					
				}
				
			}
			
		}
		
		return responseValues;
		
	}
	
}