package com.harish.rtqwta.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.harish.rtqwta.entity.PatientDetails;

public class EventProcessUtil {
	private Gson gson;
	private final Logger logger = LoggerFactory.getLogger(EventProcessUtil.class);
	public String getEventString(PatientDetails patientDetails){
		String eventString=null;
		try{		
			eventString= gson.toJson(patientDetails, PatientDetails.class);
		}catch(Exception ex){
			logger.error("Exception:",ex);
		}finally{
			return eventString;
		}
	}
	public Gson getGson() {
		return gson;
	}
	public void setGson(Gson gson) {
		this.gson = gson;
	}
	
}
