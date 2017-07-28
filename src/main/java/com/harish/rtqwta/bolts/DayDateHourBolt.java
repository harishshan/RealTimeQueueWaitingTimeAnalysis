package com.harish.rtqwta.bolts;

import java.text.SimpleDateFormat;
import java.util.Date;
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

public class DayDateHourBolt implements IRichBolt {
	private OutputCollector collector;
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
	}
	public void execute(Tuple input) {
		String message = input.getString(0);
		Gson gson = new Gson();
		PatientDetails patientDetails = gson.fromJson(message, PatientDetails.class);
		Date admissionTime = patientDetails.getAdmission_TS();
		
		long waitingTime = patientDetails.getWaitingTime();
		long treatmentTime = patientDetails.getTreatmentTime();
		
		
		TreatmentTypeDAO treatmentTypeDAO = new TreatmentTypeDAO();
		CassandraConnection cassandraConnection = new CassandraConnection();
		cassandraConnection.setHostname("localhost");
		cassandraConnection.setDatabase("rtqwta");
		cassandraConnection.setPort(9042);
		cassandraConnection.initMethod();
		treatmentTypeDAO.setCassandraConnection(cassandraConnection);
		
		Analysis dayAnalysis = new Analysis();
		dayAnalysis.setCategory(CommonConstants.Analysis.Category.DAY);
		dayAnalysis.setSub_category(Integer.toString(admissionTime.getDay()));		
		dayAnalysis.setWaiting_time(waitingTime);
		dayAnalysis.setTreatment_time(treatmentTime);	
		dayAnalysis.setPatients_count(1);
		treatmentTypeDAO.updateAnalysis(dayAnalysis,patientDetails.getPatient_type());
		
		/*Analysis dateAnalysis = new Analysis();
		dateAnalysis.setCategory(CommonConstants.Analysis.Category.DATE);
		SimpleDateFormat dateformat = new SimpleDateFormat("ddMMyyyy");		
		dateAnalysis.setSub_category(dateformat.format(admissionTime));		
		dateAnalysis.setWaiting_time(waitingTime);
		dateAnalysis.setTreatment_time(treatmentTime);	
		dateAnalysis.setPatients_count(1);			
		treatmentTypeDAO.updateAnalysis(dateAnalysis,patientDetails.getPatient_type());*/
		
		Analysis hourAnalysis = new Analysis();
		hourAnalysis.setCategory(CommonConstants.Analysis.Category.HOUR);
		hourAnalysis.setSub_category(Integer.toString(admissionTime.getHours()));	
		hourAnalysis.setWaiting_time(waitingTime);
		hourAnalysis.setTreatment_time(treatmentTime);	
		hourAnalysis.setPatients_count(1);			
		treatmentTypeDAO.updateAnalysis(hourAnalysis,patientDetails.getPatient_type());
		
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
