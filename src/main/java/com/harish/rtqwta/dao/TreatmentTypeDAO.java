package com.harish.rtqwta.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.harish.rtqwta.constants.CommonConstants;
import com.harish.rtqwta.controllers.CommonController;
import com.harish.rtqwta.entity.Analysis;
import com.harish.rtqwta.entity.PatientDetails;
import com.harish.rtqwta.entity.TreatmentType;
import com.harish.rtqwta.util.CassandraConnection;
import com.harish.rtqwta.util.EventProcessUtil;
import com.harish.rtqwta.util.KafkaUtil;
import com.harish.rtqwta.util.RandomUtil;

public class TreatmentTypeDAO {
	private CassandraConnection cassandraConnection;
	private final Logger logger = LoggerFactory.getLogger(CommonController.class);
	private KafkaUtil kafkaUtil;
	private EventProcessUtil eventProcessUtil;
	
	public List<TreatmentType> getTreatmentTypeList(){
		List<TreatmentType> treatmentTypeList = new ArrayList<TreatmentType>();
		try{
			StringBuilder queryBuilder=new StringBuilder("SELECT treatment_type_id, treatment_type, doctor_list FROM Treatment_Type");
			ResultSet resultSet=cassandraConnection.getSession().execute(queryBuilder.toString());
			for(Row row:resultSet.all()){
				TreatmentType treatmentType = new TreatmentType();
				treatmentType.setTreatmentTypeId(row.getInt(CommonConstants.TreatmentType.TREATMENT_TYPE_ID));
				treatmentType.setTreatmentType(row.getString(CommonConstants.TreatmentType.TREATMENT_TYPE_NAME));
				treatmentType.setDoctorList(row.getList(CommonConstants.TreatmentType.DOCTOR_LIST, String.class));
				treatmentTypeList.add(treatmentType);
			}
		}catch (Exception ex) {
			logger.error("Exception:", ex);
		}finally{
			return treatmentTypeList;
		}
	}
	
	public synchronized long getNewPatientId(String patientType){
		long newPatientId=0;
		try{
			StringBuilder insertQueryBuilder=new StringBuilder("UPDATE common_counter SET counter_value = counter_value + 1 WHERE table_name=?");
			cassandraConnection.getSession().execute(insertQueryBuilder.toString(), patientType);
			StringBuilder selectQueryBuilder=new StringBuilder("SELECT counter_value FROM common_counter WHERE table_name=?");
			ResultSet resultSet=cassandraConnection.getSession().execute(selectQueryBuilder.toString(),patientType);
			Row row=resultSet.one();
			newPatientId=row.getLong(CommonConstants.CommonCounter.COUNTER_VALUE);
		}catch (Exception ex) {
			logger.error("Exception:", ex);
		}finally{
			return newPatientId;
		}
	}
	
	
	public synchronized Long getNewTokenNumber(String treatmentType){
		long newTokenNumber=0;
		try{
			StringBuilder insertQueryBuilder=new StringBuilder("UPDATE common_counter SET counter_value = counter_value + 1 WHERE table_name=?");
			cassandraConnection.getSession().execute(insertQueryBuilder.toString(), treatmentType);
			StringBuilder queryBuilder=new StringBuilder("SELECT counter_value FROM common_counter WHERE table_name=?");
			ResultSet resultSet=cassandraConnection.getSession().execute(queryBuilder.toString(),treatmentType);
			Row row=resultSet.one();
			newTokenNumber=row.getLong(CommonConstants.CommonCounter.COUNTER_VALUE);
		}catch (Exception ex) {
			logger.error("Exception:", ex);
		}finally{
			return newTokenNumber;
		}
	}
	public List<String> getDoctorList(String treatmentType){
		List<String> doctorList = new ArrayList<String>();
		try{
			StringBuilder queryBuilder=new StringBuilder("SELECT doctor_list FROM Treatment_Type where Treatment_Type=? ALLOW FILTERING");
			ResultSet resultSet=cassandraConnection.getSession().execute(queryBuilder.toString(), treatmentType);
			Row row = resultSet.one();
			doctorList = row.getList(CommonConstants.TreatmentType.DOCTOR_LIST, String.class);
		}catch (Exception ex) {
			logger.error("Exception:", ex);
		}finally{
			return doctorList;
		}
	}
	public synchronized void updateAnalysis(Analysis analysis, String patientType){
		try{
			String tableName = "analysis";
			if(patientType.equals(CommonConstants.CommonCounter.HISTORICAL_PATIENT)){
				tableName ="historical_analysis";
			}
			StringBuilder queryBuilder=new StringBuilder("SELECT category, sub_category, patients_count, total_waiting_time, avg_waiting_time, total_treatment_time, avg_treatment_time FROM ")
				.append(tableName).append(" WHERE category=? AND sub_category=?");
			
				
			ResultSet resultSet=cassandraConnection.getSession().execute(queryBuilder.toString(),analysis.getCategory(), analysis.getSub_category());
			Row row=resultSet.one();
			if(row!=null){
				analysis.setPatients_count(analysis.getPatients_count() +row.getLong(CommonConstants.Analysis.PATIENTS_COUNT));
				analysis.setTotal_waiting_time(analysis.getWaiting_time() + row.getLong(CommonConstants.Analysis.TOTAL_WAITING_TIME));
				analysis.setAvg_waiting_time(analysis.getTotal_waiting_time() / analysis.getPatients_count());
				analysis.setTotal_treatment_time(analysis.getTreatment_time() + row.getLong(CommonConstants.Analysis.TOTAL_TREATMENT_TIME));
				analysis.setAvg_treatment_time(analysis.getTotal_treatment_time() / analysis.getPatients_count());
			} 
			StringBuilder updateQueryBuilder=new StringBuilder("UPDATE ").append(tableName).append(" SET patients_count = ?, total_waiting_time = ?, avg_waiting_time =?, total_treatment_time = ?, avg_treatment_time = ? ")
				.append("WHERE category=? and sub_category=?");
			cassandraConnection.getSession().execute(updateQueryBuilder.toString(), analysis.getPatients_count(), analysis.getTotal_waiting_time(), analysis.getAvg_treatment_time(),
					analysis.getTotal_treatment_time(), analysis.getAvg_treatment_time(), analysis.getCategory(), analysis.getSub_category());
		}catch (Exception ex) {
			logger.error("Exception:", ex);
		}finally{
		}
	}
	
	public void admitPatient(PatientDetails p){
		try{
			p=calculateExpectedTreatmentTime(p, CommonConstants.CommonCounter.PATIENT);
			StringBuilder insertQueryBuilder=new StringBuilder("INSERT INTO patient_details(patient_id, patient_name, patient_age, patient_gender, location, treatment_type, ")
					.append("token_number, admission_ts, treatment_start_ts, treatment_complete_ts, doctor, status) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");
			cassandraConnection.getSession().execute(insertQueryBuilder.toString(),p.getPatient_id(),p.getPatient_name(),p.getPatient_age(),p.getPatient_gender(), p.getLocation(),
					p.getTreatment_type(),p.getToken_number(),p.getAdmission_TS(),p.getTreatment_start_TS(),p.getTreatment_complete_TS(),p.getDoctor_name(), CommonConstants.PatientDetails.Status.WAITING);
		}catch (Exception ex) {
			logger.error("Exception:", ex);
		}finally{
			
		}
	}
	public PatientDetails calculateExpectedTreatmentTime(PatientDetails p, String patientType){
		try{
			String tableName = "analysis";
			if(patientType.equals(CommonConstants.CommonCounter.HISTORICAL_PATIENT)){
				tableName ="historical_analysis";
			}
			Map<String,String> categories = new HashMap<String,String>();
			int age = p.getPatient_age();
			if(age<=20){
				categories.put(CommonConstants.Analysis.Category.AGE,CommonConstants.Analysis.SubCategory.BELOW_TWENTY);
			} else if(age>20 && age<=40){
				categories.put(CommonConstants.Analysis.Category.AGE,CommonConstants.Analysis.SubCategory.TWENTY_TO_FOURTY);
			} else if(age>40 && age<=60){
				categories.put(CommonConstants.Analysis.Category.AGE,CommonConstants.Analysis.SubCategory.FOURTY_TO_SIXTY);
			} else if(age>60 && age<=80){
				categories.put(CommonConstants.Analysis.Category.AGE,CommonConstants.Analysis.SubCategory.SIXTY_TO_EIGHTY);
			} else {
				categories.put(CommonConstants.Analysis.Category.AGE,CommonConstants.Analysis.SubCategory.ABOVE_EIGHTY);
			}
			String gender = p.getPatient_gender();
			if(gender.equalsIgnoreCase(CommonConstants.Analysis.SubCategory.MALE)){
				categories.put(CommonConstants.Analysis.Category.GENDER,CommonConstants.Analysis.SubCategory.MALE);
			} else if(gender.equalsIgnoreCase(CommonConstants.Analysis.SubCategory.FEMALE)){
				categories.put(CommonConstants.Analysis.Category.GENDER,CommonConstants.Analysis.SubCategory.FEMALE);
			} else{
				categories.put(CommonConstants.Analysis.Category.GENDER,CommonConstants.Analysis.SubCategory.OTHERS);
			}
			categories.put(CommonConstants.Analysis.Category.TREATMENT_TYPE,p.getPatient_gender());
			categories.put(CommonConstants.Analysis.Category.DAY,Integer.toString(p.getAdmission_TS().getDay()));
			categories.put(CommonConstants.Analysis.Category.DATE,new SimpleDateFormat("ddMMyyyy").format(p.getAdmission_TS()));
			categories.put(CommonConstants.Analysis.Category.HOUR,Integer.toString(p.getAdmission_TS().getHours()));
			
			int counter=0;long totalWaitingTime=0;long totalTreatmentTime=0;
			for(Map.Entry<String, String> category: categories.entrySet()){
				StringBuilder queryBuilder=new StringBuilder("SELECT category, sub_category, patients_count, total_waiting_time, avg_waiting_time, total_treatment_time, avg_treatment_time FROM ")
						.append(tableName).append(" WHERE category=? AND sub_category=?");
				ResultSet resultSet=cassandraConnection.getSession().execute(queryBuilder.toString(),category.getKey(), category.getValue());
				Row row=resultSet.one();
				if(row!=null){
					long avgWaitingTime=row.getLong(CommonConstants.Analysis.AVG_WAITING_TIME);
					long avgTreatmentTime=row.getLong(CommonConstants.Analysis.AVG_TREATMENT_TIME);
					totalWaitingTime+=avgWaitingTime;
					totalTreatmentTime+=avgTreatmentTime;
					counter++;
				}				
			}
			int watitingTime = (int)totalWaitingTime/counter;
			int treatmentTime = (int)totalTreatmentTime/counter;
			Calendar treatmentStartCalender = Calendar.getInstance();
			treatmentStartCalender.setTimeInMillis(p.getAdmission_TS().getTime());
			treatmentStartCalender.add(Calendar.SECOND, watitingTime);	
			p.setExpected_treatment_start_ts(treatmentStartCalender.getTime());
			
			Calendar treatmentCompleteCalender = Calendar.getInstance();
			treatmentCompleteCalender.setTimeInMillis(treatmentStartCalender.getTime().getTime());
			treatmentCompleteCalender.add(Calendar.SECOND, treatmentTime);
			p.setExpected_treatment_complete_ts(treatmentCompleteCalender.getTime());
		}catch (Exception ex) {
			logger.error("Exception:", ex);
		}finally{
			return p;
		}
	}
	public void historicalDataCookUp(long count) throws Exception{
		try{
			Random random = new Random();
			String patient_names_bag[] = {"ABHI","ALOK","Aaditya","Aashna","Aastha","Abhinav","Abhishek","Abigail","Aditi","Aditya","Aishwarya","Ajay","Ajeet","Akansha","Akshay",
					"Amit","Angel","Aniket","Anil","Anirudh","Anish","Anisha","Anjali","Anjana","Ankit","Anubhav","Anurag","Anusha","Anushri","Archita","Arjun","Arun",
					"Arusha","Arya","Aryan","Ashish","Aswini","Avi","Avinash","Ayushi","Chandralekha","Crowny","Dawn","Deep","Deepak","Deepro","Dhruv","Dilmini","GIRISH",
					"Gayatri","Harish","Ira","Isha","Ishita","Jatin","Kalyani","Karan","Kartik","Katherine","Khushi","Krishna","Kunal","Lavanya","Lily","MOHIT","Mahima",
					"Manoj","Mary","Mayank","Mitali","N.Priyanka","NISHA","Naveen","Neelam","Neeraj","Neha","Niharika","Nikhil","Nikita","Nishant","Nishita","Niti","Nitin",
					"PRATEEK","Paaus","Papuii colney","Parth","Pavithra","Pranav","Prashant","Pratik","Priyanka","ROHIT","Radhika","Raghav","Raj","Rajeev","Raju","Ram",
					"Ramanan","Rashi","Rishabh","Rishita","Rohan","Rutuja","SHAIL","SUNNY","SURESH","Sadaf","Sakshi","Sam","Sam","Shashank","Shekhar","Shreya","Siddharth",
					"Siya","Sneha","Soham","Suhani","Sumit","Sunil","Tanya","Tisha","Vaibhav","Varun","Vinay","abdul","ajith","akash","anamika","ananya","ankur","anu",
					"arti","atul","debbie","deepa","dia","diksha","dinesh","divya","diya","gokul","indhumathi","ishika","jay","john","juvina","kamalika","kavya","krish",
					"krithika","kumar","leah","mahesh","manish","manisha","manu","mayur","moii chhangte","natasha","nishi","pawan","prachi","prince","priya","rahul",
					"rakesh","ramya","rhea","ria","riya","sanchit","sanjana","sanjay","sara","sarah","sasashy","seema","shaan","shivam","shivangi","shivani","shrinidhi",
					"shyam","simran","swati","tanu","tanvi","tushar","vaishnavi","vani","varsha","vedant","vidhya","vikas","vishal","vivek","yash"};
			String locations_bag[] = {"Kancheepuram", "Delhi", "Gurgaon", "Coimbatore", "Bangalore", "Chennai", "Hydrabed", "Pune", "San Francisco", "SunnyVale", "Santa Clara", "Berlin", "Hamburg", "Bristol", "York"};
			String patient_gender_bag[] ={"Male","Femaile","Others"};
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	        String minDateStr = "01-01-2016 00:00:00";
	        String maxDateStr = "31-12-2016 23:59:59";            
			List<TreatmentType> treatmentTypeList = getTreatmentTypeList();
			for(long i=0;i<count;i++){
				PatientDetails p = new PatientDetails();
				p.setPatient_id((int)getNewPatientId(CommonConstants.CommonCounter.HISTORICAL_PATIENT));
				p.setPatient_name(patient_names_bag[RandomUtil.getRandomInt(0, 199)]);
				p.setPatient_age(RandomUtil.getRandomInt(0, 70));
				p.setPatient_gender(patient_gender_bag[RandomUtil.getRandomInt(0, 2)]);
				p.setLocation(locations_bag[RandomUtil.getRandomInt(0, 14)]);
				
				TreatmentType treatmentType = treatmentTypeList.get(RandomUtil.getRandomInt(0, treatmentTypeList.size()-1));
				p.setTreatment_type(treatmentType.getTreatmentType());
				p.setToken_number(getNewTokenNumber(treatmentType.getTreatmentType()).toString());
				p.setDoctor_name(treatmentType.getDoctorList().get(RandomUtil.getRandomInt(0, 2)));						
				
				Date admission_TS = RandomUtil.getRandomDate(formatter.parse(minDateStr),formatter.parse(maxDateStr));				
				p.setAdmission_TS(admission_TS);
				
				Calendar treatmentStartCalender = Calendar.getInstance();
				treatmentStartCalender.setTimeInMillis(admission_TS.getTime());
				treatmentStartCalender.add(Calendar.SECOND, RandomUtil.getRandomInt(1000, 18000));			    
				p.setTreatment_start_TS(treatmentStartCalender.getTime());
				
				Calendar treatmentCompleteCalender = Calendar.getInstance();
				treatmentCompleteCalender.setTimeInMillis(treatmentStartCalender.getTime().getTime());
				treatmentCompleteCalender.add(Calendar.SECOND, RandomUtil.getRandomInt(1000, 3000));
				p.setTreatment_complete_TS(treatmentCompleteCalender.getTime());
				p.setStatus(CommonConstants.PatientDetails.Status.COMPLETED);
				p.setTreatment_type(CommonConstants.CommonCounter.HISTORICAL_PATIENT);
				StringBuilder insertQueryBuilder=new StringBuilder("INSERT INTO historical_patient_details(patient_id, patient_name, patient_age, patient_gender, location, treatment_type,")
						.append("token_number, admission_ts, treatment_start_ts, treatment_complete_ts, doctor, status) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");
				cassandraConnection.getSession().execute(insertQueryBuilder.toString(),p.getPatient_id(),p.getPatient_name(),p.getPatient_age(),p.getPatient_gender(), p.getLocation(),
						p.getTreatment_type(),p.getToken_number(),p.getAdmission_TS(),p.getTreatment_start_TS(),p.getTreatment_complete_TS(),p.getDoctor_name(), p.getStatus());
				String patientString = eventProcessUtil.getEventString(p);
				kafkaUtil.sendMessage(patientString);
			}
			
		}catch (Exception ex) {
			throw ex;
		}finally{
			
		}
	}
	
	public List<PatientDetails> getPatientListForTreatment(){
		List<PatientDetails> patientDetailsList = new ArrayList<PatientDetails>();
		try{
			String statuses[] = {CommonConstants.PatientDetails.Status.WAITING, CommonConstants.PatientDetails.Status.STARTED};
			for(String status:statuses){
				StringBuilder queryBuilder=new StringBuilder("SELECT patient_id, patient_name, patient_age, patient_gender, location, treatment_type, token_number, admission_ts, treatment_start_ts, treatment_complete_ts, doctor, status, expected_treatment_start_ts, expected_treatment_complete_ts FROM patient_details where status = ? ALLOW FILTERING");
				ResultSet resultSet=cassandraConnection.getSession().execute(queryBuilder.toString(), status);
				for(Row row:resultSet.all()){
					PatientDetails patientDetails = new PatientDetails();
					patientDetails.setPatient_id(row.getInt(CommonConstants.PatientDetails.PATIENT_ID));
					patientDetails.setPatient_name(row.getString(CommonConstants.PatientDetails.PATIENT_NAME));
					patientDetails.setPatient_age(row.getInt(CommonConstants.PatientDetails.PATIENT_AGE));
					patientDetails.setPatient_gender(row.getString(CommonConstants.PatientDetails.PATIENT_GENDER));
					patientDetails.setLocation(row.getString(CommonConstants.PatientDetails.LOCATION));
					TreatmentType treatmentType = new TreatmentType();
					treatmentType.setTreatmentType(row.getString(CommonConstants.PatientDetails.TREATMENT_TYPE));
					patientDetails.setTreatment_type(treatmentType.getTreatmentType());
					patientDetails.setToken_number(row.getString(CommonConstants.PatientDetails.TOKEN_NUMBER));
					patientDetails.setAdmission_TS(row.getTimestamp(CommonConstants.PatientDetails.ADMISSION_TS));
					patientDetails.setTreatment_start_TS(row.getTimestamp(CommonConstants.PatientDetails.TREATMENT_START_TS));
					patientDetails.setTreatment_complete_TS(row.getTimestamp(CommonConstants.PatientDetails.TREATMENT_COMPLETE_TS));
					patientDetails.setDoctor_name(row.getString(CommonConstants.PatientDetails.DOCTOR));
					patientDetails.setStatus(row.getString(CommonConstants.PatientDetails.STATUS));
					patientDetails.setExpected_treatment_start_ts(row.getTimestamp(CommonConstants.PatientDetails.EXPECTED_TREATMENT_START_TS));
					patientDetails.setExpected_treatment_complete_ts(row.getTimestamp(CommonConstants.PatientDetails.EXPECTED_TREATMENT_COMPLETE_TS));
					patientDetailsList.add(patientDetails);
				}
			}
		}catch (Exception ex) {
			logger.error("Exception:", ex);
		}finally{
			return patientDetailsList;
		}
	}
	
	public List<PatientDetails> getTreatmentCompletedPatientList(){
		List<PatientDetails> patientDetailsList = new ArrayList<PatientDetails>();
		try{
			StringBuilder queryBuilder=new StringBuilder("SELECT patient_id, patient_name, patient_age, patient_gender, location, treatment_type, token_number, admission_ts, treatment_start_ts, treatment_complete_ts, doctor, status FROM patient_details where status = ? ALLOW FILTERING");
			ResultSet resultSet=cassandraConnection.getSession().execute(queryBuilder.toString(),CommonConstants.PatientDetails.Status.COMPLETED);
			for(Row row:resultSet.all()){
				PatientDetails patientDetails = new PatientDetails();
				patientDetails.setPatient_id(row.getInt(CommonConstants.PatientDetails.PATIENT_ID));
				patientDetails.setPatient_name(row.getString(CommonConstants.PatientDetails.PATIENT_NAME));
				patientDetails.setPatient_age(row.getInt(CommonConstants.PatientDetails.PATIENT_AGE));
				patientDetails.setPatient_gender(row.getString(CommonConstants.PatientDetails.PATIENT_GENDER));
				patientDetails.setLocation(row.getString(CommonConstants.PatientDetails.LOCATION));
				TreatmentType treatmentType = new TreatmentType();
				treatmentType.setTreatmentType(row.getString(CommonConstants.PatientDetails.TREATMENT_TYPE));
				patientDetails.setTreatment_type(treatmentType.getTreatmentType());
				patientDetails.setToken_number(row.getString(CommonConstants.PatientDetails.TOKEN_NUMBER));
				patientDetails.setAdmission_TS(row.getTimestamp(CommonConstants.PatientDetails.ADMISSION_TS));
				patientDetails.setTreatment_start_TS(row.getTimestamp(CommonConstants.PatientDetails.TREATMENT_START_TS));
				patientDetails.setTreatment_complete_TS(row.getTimestamp(CommonConstants.PatientDetails.TREATMENT_COMPLETE_TS));
				patientDetails.setDoctor_name(row.getString(CommonConstants.PatientDetails.DOCTOR));
				patientDetails.setStatus(row.getString(CommonConstants.PatientDetails.STATUS));
				patientDetailsList.add(patientDetails);
			}
		}catch (Exception ex) {
			logger.error("Exception:", ex);
		}finally{
			return patientDetailsList;
		}
	}
	public List<PatientDetails> getHistoricalTreatmentCompletedPatientList(){
		List<PatientDetails> patientDetailsList = new ArrayList<PatientDetails>();
		try{
			StringBuilder queryBuilder=new StringBuilder("SELECT patient_id, patient_name, patient_age, patient_gender, location, treatment_type, token_number, admission_ts, treatment_start_ts, treatment_complete_ts, doctor, status FROM historical_patient_details where status = ? ALLOW FILTERING");
			ResultSet resultSet=cassandraConnection.getSession().execute(queryBuilder.toString(),CommonConstants.PatientDetails.Status.COMPLETED);
			for(Row row:resultSet.all()){
				PatientDetails patientDetails = new PatientDetails();
				patientDetails.setPatient_id(row.getInt(CommonConstants.PatientDetails.PATIENT_ID));
				patientDetails.setPatient_name(row.getString(CommonConstants.PatientDetails.PATIENT_NAME));
				patientDetails.setPatient_age(row.getInt(CommonConstants.PatientDetails.PATIENT_AGE));
				patientDetails.setPatient_gender(row.getString(CommonConstants.PatientDetails.PATIENT_GENDER));
				patientDetails.setLocation(row.getString(CommonConstants.PatientDetails.LOCATION));
				TreatmentType treatmentType = new TreatmentType();
				treatmentType.setTreatmentType(row.getString(CommonConstants.PatientDetails.TREATMENT_TYPE));
				patientDetails.setTreatment_type(treatmentType.getTreatmentType());
				patientDetails.setToken_number(row.getString(CommonConstants.PatientDetails.TOKEN_NUMBER));
				patientDetails.setAdmission_TS(row.getTimestamp(CommonConstants.PatientDetails.ADMISSION_TS));
				patientDetails.setTreatment_start_TS(row.getTimestamp(CommonConstants.PatientDetails.TREATMENT_START_TS));
				patientDetails.setTreatment_complete_TS(row.getTimestamp(CommonConstants.PatientDetails.TREATMENT_COMPLETE_TS));
				patientDetails.setDoctor_name(row.getString(CommonConstants.PatientDetails.DOCTOR));
				patientDetails.setStatus(row.getString(CommonConstants.PatientDetails.STATUS));
				patientDetailsList.add(patientDetails);
			}
		}catch (Exception ex) {
			logger.error("Exception:", ex);
		}finally{
			return patientDetailsList;
		}
	}
	
	public void startTreatmentForPatientId(int patientId){
		try{
			StringBuilder queryBuilder=new StringBuilder("UPDATE patient_details SET treatment_start_ts = ?, status = ? WHERE patient_id = ?");
			cassandraConnection.getSession().execute(queryBuilder.toString(), new Date(),CommonConstants.PatientDetails.Status.STARTED, patientId);			
		}catch (Exception ex) {
			logger.error("Exception:", ex);
		}finally{
		}
	}
	public void completeTreatmentForPatientId(int patientId){
		try{
			StringBuilder updatequeryBuilder=new StringBuilder("UPDATE patient_details SET treatment_complete_ts = ?, status = ? WHERE patient_id = ?");
			cassandraConnection.getSession().execute(updatequeryBuilder.toString(), new Date(),CommonConstants.PatientDetails.Status.COMPLETED, patientId);
			StringBuilder queryBuilder=new StringBuilder("SELECT patient_id, patient_name, patient_age, patient_gender, location, treatment_type, token_number, admission_ts, treatment_start_ts, treatment_complete_ts, doctor, status FROM patient_details where status = ? ALLOW FILTERING");
			ResultSet resultSet=cassandraConnection.getSession().execute(queryBuilder.toString(),CommonConstants.PatientDetails.Status.COMPLETED);
			Row row =resultSet.one();
			PatientDetails patientDetails = new PatientDetails();
			patientDetails.setPatient_id(row.getInt(CommonConstants.PatientDetails.PATIENT_ID));
			patientDetails.setPatient_name(row.getString(CommonConstants.PatientDetails.PATIENT_NAME));
			patientDetails.setPatient_age(row.getInt(CommonConstants.PatientDetails.PATIENT_AGE));
			patientDetails.setPatient_gender(row.getString(CommonConstants.PatientDetails.PATIENT_GENDER));
			patientDetails.setLocation(row.getString(CommonConstants.PatientDetails.LOCATION));
			TreatmentType treatmentType = new TreatmentType();
			treatmentType.setTreatmentType(row.getString(CommonConstants.PatientDetails.TREATMENT_TYPE));
			patientDetails.setTreatment_type(treatmentType.getTreatmentType());
			patientDetails.setToken_number(row.getString(CommonConstants.PatientDetails.TOKEN_NUMBER));
			patientDetails.setAdmission_TS(row.getTimestamp(CommonConstants.PatientDetails.ADMISSION_TS));
			patientDetails.setTreatment_start_TS(row.getTimestamp(CommonConstants.PatientDetails.TREATMENT_START_TS));
			patientDetails.setTreatment_complete_TS(row.getTimestamp(CommonConstants.PatientDetails.TREATMENT_COMPLETE_TS));
			patientDetails.setDoctor_name(row.getString(CommonConstants.PatientDetails.DOCTOR));
			patientDetails.setStatus(row.getString(CommonConstants.PatientDetails.STATUS));	
			patientDetails.setPatient_type(CommonConstants.CommonCounter.PATIENT);
			String patientString = eventProcessUtil.getEventString(patientDetails);
			kafkaUtil.sendMessage(patientString);
		}catch (Exception ex) {
			logger.error("Exception:", ex);
		}finally{
		}
	}
	
	public CassandraConnection getCassandraConnection() {
		return cassandraConnection;
	}
	public void setCassandraConnection(CassandraConnection cassandraConnection) {
		this.cassandraConnection = cassandraConnection;
	}

	public KafkaUtil getKafkaUtil() {
		return kafkaUtil;
	}

	public void setKafkaUtil(KafkaUtil kafkaUtil) {
		this.kafkaUtil = kafkaUtil;
	}

	public EventProcessUtil getEventProcessUtil() {
		return eventProcessUtil;
	}

	public void setEventProcessUtil(EventProcessUtil eventProcessUtil) {
		this.eventProcessUtil = eventProcessUtil;
	}
	
}
