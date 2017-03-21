package com.harish.rtqwta.bolts;

import java.util.Date;
import java.util.Map;

import com.google.gson.Gson;
import com.harish.rtqwta.entity.PatientDetails;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class TimeCalculationBolt implements IRichBolt {
	private OutputCollector collector;
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
	}
	public void execute(Tuple input) {
		String message = input.getString(0);
		Gson gson = new Gson();
		PatientDetails patientDetails = gson.fromJson(message, PatientDetails.class);
		Date admissionTime = patientDetails.getAdmission_TS();
		Date treatmentStartTime = patientDetails.getTreatment_start_TS();
		Date treatmentCompleteTime = patientDetails.getTreatment_complete_TS();
		long waitingTime = (treatmentStartTime.getTime() - admissionTime.getTime())/1000;
		long treatmentTime = (treatmentCompleteTime.getTime() - treatmentStartTime.getTime())/1000;
		patientDetails.setWaitingTime(waitingTime);
		patientDetails.setTreatmentTime(treatmentTime);
		String newMessage = gson.toJson(patientDetails, PatientDetails.class);
		collector.emit(new Values(newMessage));
		collector.ack(input);
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("patient-details"));
	}

	public void cleanup() {
	}

	public Map<String, Object> getComponentConfiguration() {
		return null;
	}
}
