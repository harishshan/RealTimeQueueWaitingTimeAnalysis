package com.harish.rtqwta.dao;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.harish.rtqwta.constants.CommonConstants;
import com.harish.rtqwta.controllers.CommonController;
import com.harish.rtqwta.entity.PatientDetails;
import com.harish.rtqwta.entity.TreatmentType;
import com.harish.rtqwta.util.CassandraConnection;

public class TreatmentTypeDAO {
	private CassandraConnection cassandraConnection;
	private final Logger logger = LoggerFactory.getLogger(CommonController.class);
	
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
	
	public synchronized long getNewPatientId(){
		long newPatientId=0;
		try{
			StringBuilder insertQueryBuilder=new StringBuilder("UPDATE common_counter SET counter_value = counter_value + 1 WHERE table_name=?");
			cassandraConnection.getSession().execute(insertQueryBuilder.toString(), CommonConstants.CommonCounter.PATIENT);
			StringBuilder selectQueryBuilder=new StringBuilder("SELECT counter_value FROM common_counter WHERE table_name=?");
			ResultSet resultSet=cassandraConnection.getSession().execute(selectQueryBuilder.toString(),CommonConstants.CommonCounter.PATIENT);
			Row row=resultSet.one();
			newPatientId=row.getLong(CommonConstants.CommonCounter.COUNTER_VALUE);
		}catch (Exception ex) {
			logger.error("Exception:", ex);
		}finally{
			return newPatientId;
		}
	}
	
	public synchronized long getNewTokenNumber(String treatmentType){
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
	
	public void admitPatient(PatientDetails p){
		try{
			StringBuilder insertQueryBuilder=new StringBuilder("INSERT INTO patient_details(patient_id, patient_name, patient_age, location, treatment_type, ")
					.append("token_number, admission_ts, treatment_start_ts, treatment_complete_ts) VALUES(?,?,?,?,?,?,?,?,?)");
			cassandraConnection.getSession().execute(insertQueryBuilder.toString(),p.getPatient_id(),p.getPatient_name(),p.getPatient_age(),p.getLocation(),
					p.getTreatment_type(),p.getToken_number(),p.getAdmission_TS(),p.getTreatment_start_TS(),p.getTreatment_complete_TS());
		}catch (Exception ex) {
			logger.error("Exception:", ex);
		}finally{
			
		}
	}
	
	public List<PatientDetails> getPatientListForTreatment(){
		List<PatientDetails> patientDetailsList = new ArrayList<PatientDetails>();
		try{
			StringBuilder queryBuilder=new StringBuilder("SELECT patient_id, patient_name, patient_age, location, treatment_type, token_number, admission_ts, treatment_start_ts, treatment_complete_ts FROM patient_details");
			ResultSet resultSet=cassandraConnection.getSession().execute(queryBuilder.toString());
			for(Row row:resultSet.all()){
				PatientDetails patientDetails = new PatientDetails();
				patientDetails.setPatient_id(row.getInt(CommonConstants.PatientDetails.PATIENT_ID));
				patientDetails.setPatient_name(row.getString(CommonConstants.PatientDetails.PATIENT_NAME));
				patientDetails.setPatient_age(row.getInt(CommonConstants.PatientDetails.PATIENT_AGE));
				patientDetails.setLocation(row.getString(CommonConstants.PatientDetails.LOCATION));
				patientDetails.setTreatment_type(row.getString(CommonConstants.PatientDetails.TREATMENT_TYPE));
				patientDetails.setToken_number(row.getString(CommonConstants.PatientDetails.TOKEN_NUMBER));
				patientDetails.setAdmission_TS(row.getTimestamp(CommonConstants.PatientDetails.ADMISSION_TS));
				patientDetails.setTreatment_start_TS(row.getTimestamp(CommonConstants.PatientDetails.TREATMENT_START_TS));
				patientDetails.setTreatment_complete_TS(row.getTimestamp(CommonConstants.PatientDetails.TREATMENT_COMPLETE_TS));
				patientDetailsList.add(patientDetails);
			}
		}catch (Exception ex) {
			logger.error("Exception:", ex);
		}finally{
			return patientDetailsList;
		}
	}
	
	public CassandraConnection getCassandraConnection() {
		return cassandraConnection;
	}
	public void setCassandraConnection(CassandraConnection cassandraConnection) {
		this.cassandraConnection = cassandraConnection;
	}
}
