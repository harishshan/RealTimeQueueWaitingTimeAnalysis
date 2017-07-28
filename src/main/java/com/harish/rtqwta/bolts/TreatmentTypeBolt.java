package com.harish.rtqwta.bolts;

import java.util.Map;

import com.google.gson.Gson;
import com.harish.rtqwta.constants.CommonConstants;
import com.harish.rtqwta.dao.TreatmentTypeDAO;
import com.harish.rtqwta.entity.Analysis;
import com.harish.rtqwta.entity.PatientDetails;
import com.harish.rtqwta.util.CassandraConnection;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class TreatmentTypeBolt implements IRichBolt {
	private OutputCollector collector;
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
	}
	public void execute(Tuple input) {
		String message = input.getString(0);
		Gson gson = new Gson();
		PatientDetails patientDetails = gson.fromJson(message, PatientDetails.class);
		String TreatmentType = patientDetails.getTreatment_type();
		long waitingTime = patientDetails.getWaitingTime();
		long treatmentTime = patientDetails.getTreatmentTime();
		
		Analysis analysis = new Analysis();
		analysis.setCategory(CommonConstants.Analysis.Category.TREATMENT_TYPE);
		analysis.setSub_category(TreatmentType);
		
		analysis.setWaiting_time(waitingTime);
		analysis.setTreatment_time(treatmentTime);	
		analysis.setPatients_count(1);
		TreatmentTypeDAO treatmentTypeDAO = new TreatmentTypeDAO();
		CassandraConnection cassandraConnection = new CassandraConnection();
		cassandraConnection.setHostname("localhost");
		cassandraConnection.setDatabase("rtqwta");
		cassandraConnection.setPort(9042);
		cassandraConnection.initMethod();
		treatmentTypeDAO.setCassandraConnection(cassandraConnection);
		treatmentTypeDAO.updateAnalysis(analysis,patientDetails.getPatient_type());
		cassandraConnection.getSession().close();
		cassandraConnection.getCluster().close();
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
