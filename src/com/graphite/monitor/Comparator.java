package com.graphite.monitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class Comparator {

	public static void compare(Map<String,Map<Long,Double>> comparableMetrics) {
		
		// invert the entries
		Map<Long, List<String>> invertedComparableMetrics = new HashMap<>();
		
		Set<Entry<String,Map<Long,Double>>> entries = comparableMetrics.entrySet();
		for (Entry<String,Map<Long,Double>> entry : entries) {
			
			String metricName = entry.getKey();
			
			Map<Long,Double> values = entry.getValue();
			if (values == null) continue;
			Set<Entry<Long,Double>> valueEntries = values.entrySet();
			for (Entry<Long,Double> valueEntry : valueEntries) {
				
				Long time = valueEntry.getKey();
				Double value = valueEntry.getValue();
				
				String valueToAdd = metricName + " = " + value;
				
				List<String> currentTimeValue = invertedComparableMetrics.get(time);
				if (currentTimeValue == null) {
					List<String> newTimeValue = new ArrayList<String>();
					newTimeValue.add(valueToAdd);
					invertedComparableMetrics.put(time, newTimeValue);
				} else {
					currentTimeValue.add(valueToAdd);
				}
				
			}
			
		}
		
		prettyPrint(invertedComparableMetrics);
		
	}

	// @ 1386622980:
	//    - C1 = 130271.0
	//    - C2 = ...
	public static void prettyPrint(Map<Long, List<String>> metricMap) {
		
		Set<Long> keySet = metricMap.keySet();
		List<Long> keyList = new ArrayList<Long>();
		keyList.addAll(keySet);
		Collections.sort(keyList);
		
		for (Long key : keyList) {
			
			System.out.println("@ " + key);
			
			List<String> values = metricMap.get(key);
			Collections.sort(values);
			
			for (String value : values) {
				
				System.out.println("\t- " + value);
				
			}
			
		}
		
	}	
	
}