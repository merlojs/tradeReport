package com.jpmorgan.challenge;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class MapUtil {
	
	public static Map<String, Double> orderByDate(Map<String, Double> map) {
    	Set<Entry<String, Double>> set = map.entrySet();
        List<Entry<String, Double>> list = new ArrayList<Entry<String, Double>>(set);
        
        Collections.sort(list, new Comparator<Map.Entry<String, Double>>(){
            DateFormat f = new SimpleDateFormat("dd-MMM-yyyy");
            @Override
            public int compare (Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                try {
                    return f.parse(o1.getKey()).compareTo(f.parse(o2.getKey()));
                } catch (ParseException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        });
        
        map.clear();
        for (Entry<String, Double> entry : list) {
        	map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }
    
    public static Map<String, Double> orderByValue(Map<String, Double> map) {
    	Set<Entry<String, Double>> set = map.entrySet();
        List<Entry<String, Double>> list = new ArrayList<Entry<String, Double>>(set);
        Collections.sort(list, new Comparator<Map.Entry<String, Double>>(){
			public int compare( Map.Entry<String, Double> o1, Map.Entry<String, Double> o2 )
		    {
		        return (o2.getValue()).compareTo(o1.getValue() );
		    }
		} );
        
        map.clear();
        for (Entry<String, Double> entry : list) {
        	map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }	
}

