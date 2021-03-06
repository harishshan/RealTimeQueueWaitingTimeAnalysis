package com.harish.rtqwta.controllers;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.harish.rtqwta.constants.CommonConstants;
import com.harish.rtqwta.dao.TreatmentTypeDAO;
import com.harish.rtqwta.entity.PatientDetails;
import com.harish.rtqwta.entity.TreatmentType;
import com.harish.rtqwta.util.CommonUtil;
import com.harish.rtqwta.util.PatientDetailsComparator;
import com.harish.rtqwta.util.TreatmentTypeComparator;


@Controller
public class CommonController {

	@Autowired
	private TreatmentTypeDAO treatmentTypeDAO;
	
    private final Logger logger = LoggerFactory.getLogger(CommonController.class);
    
    @Autowired
    private Gson gson;

    @RequestMapping(value = "/getTreatmentType", method = RequestMethod.GET)
    public @ResponseBody JsonArray getTreatmentType(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
    	JsonArray treatmentTypeArray = new JsonArray();
        try {
        	List<TreatmentType> treatmentTypeList = treatmentTypeDAO.getTreatmentTypeList();
        	Collections.sort(treatmentTypeList, new TreatmentTypeComparator());
        	for(TreatmentType treatmentType: treatmentTypeList){
        		JsonObject jsonObject= new JsonObject();
        		jsonObject.addProperty(CommonConstants.TreatmentType.TREATMENT_TYPE_ID, treatmentType.getTreatmentTypeId());
        		jsonObject.addProperty(CommonConstants.TreatmentType.TREATMENT_TYPE_NAME, treatmentType.getTreatmentType());
        		JsonArray doctorList=new JsonArray();
        		for(String doctor: treatmentType.getDoctorList()){
        			JsonObject doctorJsonObject = new JsonObject();
        			doctorJsonObject.addProperty(CommonConstants.TreatmentType.DOCTOR_NAME, doctor);
        			doctorList.add(doctorJsonObject);
        		}
        		jsonObject.add(CommonConstants.TreatmentType.DOCTOR_LIST, doctorList);
        		treatmentTypeArray.add(jsonObject);
        	}
        } catch (Exception ex) {
        	logger.error("Exception:", ex);
        } finally{
        	return treatmentTypeArray;
        }
    }
    
    @RequestMapping(value = "/getNewPatientId", method = RequestMethod.GET)
    public @ResponseBody long getNewPatientId(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
    	long newPatientId=0;
        try {
        	newPatientId = treatmentTypeDAO.getNewPatientId(CommonConstants.CommonCounter.PATIENT);        	
        } catch (Exception ex) {
        	logger.error("Exception:", ex);
        } finally{
        	return newPatientId;
        }
    }
    
    @RequestMapping(value = "/getDoctorList/{treatmentType}", method = RequestMethod.GET)
    public @ResponseBody JsonArray getDoctorList(HttpServletRequest request, HttpServletResponse response, HttpSession session, @PathVariable String treatmentType) {
    	JsonArray jsonArray = new JsonArray();
        try {
        	List<String> doctorList = treatmentTypeDAO.getDoctorList(treatmentType);
        	for(String doctor: doctorList){
	        	JsonObject doctorJsonObject = new JsonObject();
				doctorJsonObject.addProperty(CommonConstants.TreatmentType.DOCTOR_NAME, doctor);
				jsonArray.add(doctorJsonObject);
        	}
        } catch (Exception ex) {
        	logger.error("Exception:", ex);
        } finally{
        	return jsonArray;
        }
    }
    @RequestMapping(value = "/getNewTokenNumber/{treatmentType}", method = RequestMethod.GET)
    public @ResponseBody long getNewTokenNumber(HttpServletRequest request, HttpServletResponse response, HttpSession session, @PathVariable String treatmentType) {
    	long newTokenNumber=0;
        try {
        	newTokenNumber = treatmentTypeDAO.getNewTokenNumber(treatmentType);        	
        } catch (Exception ex) {
        	logger.error("Exception:", ex);
        } finally{
        	return newTokenNumber;
        }
    }
    
    @RequestMapping(value = "/admitPatient", method = RequestMethod.POST, consumes="application/json")
    public @ResponseBody JsonObject admitPatient(HttpServletRequest request, HttpServletResponse response, HttpSession session, @RequestBody String patientDetailsJson) {
    	JsonObject jsonObject =new JsonObject();
        try {
        	PatientDetails patientDetails = gson.fromJson(patientDetailsJson, PatientDetails.class);
        	patientDetails.setAdmission_TS(new Date());
        	treatmentTypeDAO.admitPatient(patientDetails);
        	jsonObject.addProperty("message", "Successfully Admitted Patient");
        } catch (Exception ex) {
        	logger.error("Exception:", ex);
        	jsonObject.addProperty("error", ex.getMessage());
        } finally{
        	return jsonObject;
        }
    }
    
    @RequestMapping(value = "/getPatientListForTreatment/{treatmentType}", method = RequestMethod.GET)
    public @ResponseBody JsonArray getPatientListForTreatment(HttpServletRequest request, HttpServletResponse response, HttpSession session,@PathVariable String treatmentType) {
    	JsonArray patientList = new JsonArray();
        try {
        	String statuses[] = { CommonConstants.PatientDetails.Status.STARTED, CommonConstants.PatientDetails.Status.WAITING};
        	long lastUpdatedTreatmentTime = treatmentTypeDAO.getLastTreatmentTime(treatmentType);
        	int i=0;
        	long totalTreatmentTime=-(lastUpdatedTreatmentTime);
			for(String status:statuses){
	        	List<PatientDetails> patientDetailsList = treatmentTypeDAO.getPatientListForTreatment(treatmentType, status);
	        	Collections.sort(patientDetailsList, new PatientDetailsComparator());	        	
	        	
	        	for(PatientDetails patientDetails: patientDetailsList){
	        		JsonObject jsonObject= new JsonObject();
	        		jsonObject.addProperty(CommonConstants.PatientDetails.PATIENT_ID, patientDetails.getPatient_id());
	        		jsonObject.addProperty(CommonConstants.PatientDetails.PATIENT_NAME, patientDetails.getPatient_name());
	        		jsonObject.addProperty(CommonConstants.PatientDetails.PATIENT_AGE, patientDetails.getPatient_age());
	        		jsonObject.addProperty(CommonConstants.PatientDetails.PATIENT_GENDER, patientDetails.getPatient_gender());
	        		jsonObject.addProperty(CommonConstants.PatientDetails.LOCATION, patientDetails.getLocation());
	        		jsonObject.addProperty(CommonConstants.PatientDetails.TREATMENT_TYPE, patientDetails.getTreatment_type());
	        		jsonObject.addProperty(CommonConstants.PatientDetails.TOKEN_NUMBER, patientDetails.getToken_number());
	        		jsonObject.addProperty(CommonConstants.PatientDetails.ADMISSION_TS, patientDetails.getAdmission_TS()!=null?patientDetails.getAdmission_TS().toString():null);
	        		jsonObject.addProperty(CommonConstants.PatientDetails.TREATMENT_START_TS, patientDetails.getTreatment_start_TS()!=null?patientDetails.getTreatment_start_TS().toString():null);
	        		jsonObject.addProperty(CommonConstants.PatientDetails.TREATMENT_COMPLETE_TS, patientDetails.getTreatment_complete_TS()!=null?patientDetails.getTreatment_complete_TS().toString():null);
	        		jsonObject.addProperty(CommonConstants.PatientDetails.TREATMENT_TIME, CommonUtil.getTreatmentTime(patientDetails.getTreatment_start_TS(),patientDetails.getTreatment_complete_TS()));
	        		jsonObject.addProperty(CommonConstants.PatientDetails.DOCTOR, patientDetails.getDoctor_name());
	        		jsonObject.addProperty(CommonConstants.PatientDetails.STATUS, patientDetails.getStatus());
	        		jsonObject.addProperty(CommonConstants.PatientDetails.AVERAGE_TREATMENT_TIME, CommonUtil.getAverageTime(patientDetails.getAverage_treatment_time()));
	        		//if(status.equals(CommonConstants.PatientDetails.Status.WAITING)){
	        			if(i==0){
	        				jsonObject.addProperty(CommonConstants.PatientDetails.AVERAGE_WAITING_TIME, CommonUtil.getAverageTime(0));
	        			}else{
	        				jsonObject.addProperty(CommonConstants.PatientDetails.AVERAGE_WAITING_TIME, CommonUtil.getAverageTime(totalTreatmentTime));
	        			}	        			
	        		//}
	        		i++;	        		
	        		totalTreatmentTime +=patientDetails.getAverage_treatment_time();//<lastCompletedTreatmenttime?patientDetails.getAverage_treatment_time():patientDetails.getTreatmentTime();
	        		patientList.add(jsonObject);
	        	}
			}
        } catch (Exception ex) {
        	logger.error("Exception:", ex);
        } finally{
        	return patientList;
        }
    }
    
    @RequestMapping(value = "/getTreatmentCompletedPatientList/{treatmentType}", method = RequestMethod.GET)
    public @ResponseBody JsonArray getTreatmentCompletedPatientList(HttpServletRequest request, HttpServletResponse response, HttpSession session, @PathVariable String treatmentType) {
    	JsonArray patientList = new JsonArray();
        try {
        	List<PatientDetails> patientDetailsList = treatmentTypeDAO.getTreatmentCompletedPatientList(treatmentType);
        	Collections.sort(patientDetailsList, new PatientDetailsComparator());
        	for(PatientDetails patientDetails: patientDetailsList){
        		JsonObject jsonObject= new JsonObject();
        		jsonObject.addProperty(CommonConstants.PatientDetails.PATIENT_ID, patientDetails.getPatient_id());
        		jsonObject.addProperty(CommonConstants.PatientDetails.PATIENT_NAME, patientDetails.getPatient_name());
        		jsonObject.addProperty(CommonConstants.PatientDetails.PATIENT_AGE, patientDetails.getPatient_age());
        		jsonObject.addProperty(CommonConstants.PatientDetails.PATIENT_GENDER, patientDetails.getPatient_gender());
        		jsonObject.addProperty(CommonConstants.PatientDetails.LOCATION, patientDetails.getLocation());
        		jsonObject.addProperty(CommonConstants.PatientDetails.TREATMENT_TYPE, patientDetails.getTreatment_type());
        		jsonObject.addProperty(CommonConstants.PatientDetails.TOKEN_NUMBER, patientDetails.getToken_number());
        		jsonObject.addProperty(CommonConstants.PatientDetails.ADMISSION_TS, patientDetails.getAdmission_TS()!=null?patientDetails.getAdmission_TS().toString():null);
        		jsonObject.addProperty(CommonConstants.PatientDetails.TREATMENT_START_TS, patientDetails.getTreatment_start_TS()!=null?patientDetails.getTreatment_start_TS().toString():null);
        		jsonObject.addProperty(CommonConstants.PatientDetails.TREATMENT_COMPLETE_TS, patientDetails.getTreatment_complete_TS()!=null?patientDetails.getTreatment_complete_TS().toString():null);
        		jsonObject.addProperty(CommonConstants.PatientDetails.DOCTOR, patientDetails.getDoctor_name());
        		jsonObject.addProperty(CommonConstants.PatientDetails.STATUS, patientDetails.getStatus());
        		patientList.add(jsonObject);
        	}
        } catch (Exception ex) {
        	logger.error("Exception:", ex);
        } finally{
        	return patientList;
        }
    }
    @RequestMapping(value = "/getHistoricalTreatmentCompletedPatientList", method = RequestMethod.GET)
    public @ResponseBody JsonArray getHistoricalTreatmentCompletedPatientList(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
    	JsonArray patientList = new JsonArray();
        try {
        	List<PatientDetails> patientDetailsList = treatmentTypeDAO.getHistoricalTreatmentCompletedPatientList();
        	Collections.sort(patientDetailsList, new PatientDetailsComparator());
        	for(PatientDetails patientDetails: patientDetailsList){
        		JsonObject jsonObject= new JsonObject();
        		jsonObject.addProperty(CommonConstants.PatientDetails.PATIENT_ID, patientDetails.getPatient_id());
        		jsonObject.addProperty(CommonConstants.PatientDetails.PATIENT_NAME, patientDetails.getPatient_name());
        		jsonObject.addProperty(CommonConstants.PatientDetails.PATIENT_AGE, patientDetails.getPatient_age());
        		jsonObject.addProperty(CommonConstants.PatientDetails.PATIENT_GENDER, patientDetails.getPatient_gender());
        		jsonObject.addProperty(CommonConstants.PatientDetails.LOCATION, patientDetails.getLocation());
        		jsonObject.addProperty(CommonConstants.PatientDetails.TREATMENT_TYPE, patientDetails.getTreatment_type());
        		jsonObject.addProperty(CommonConstants.PatientDetails.TOKEN_NUMBER, patientDetails.getToken_number());
        		jsonObject.addProperty(CommonConstants.PatientDetails.ADMISSION_TS, patientDetails.getAdmission_TS()!=null?patientDetails.getAdmission_TS().toString():null);
        		jsonObject.addProperty(CommonConstants.PatientDetails.TREATMENT_START_TS, patientDetails.getTreatment_start_TS()!=null?patientDetails.getTreatment_start_TS().toString():null);
        		jsonObject.addProperty(CommonConstants.PatientDetails.TREATMENT_COMPLETE_TS, patientDetails.getTreatment_complete_TS()!=null?patientDetails.getTreatment_complete_TS().toString():null);
        		jsonObject.addProperty(CommonConstants.PatientDetails.DOCTOR, patientDetails.getDoctor_name());
        		jsonObject.addProperty(CommonConstants.PatientDetails.STATUS, patientDetails.getStatus());
        		patientList.add(jsonObject);
        	}
        } catch (Exception ex) {
        	logger.error("Exception:", ex);
        } finally{
        	return patientList;
        }
    }
    
    @RequestMapping(value = "/startTreatmentForPatientId/{patientId}", method = RequestMethod.GET)
    public @ResponseBody JsonObject startTreatmentForPatientId(HttpServletRequest request, HttpServletResponse response, HttpSession session, @PathVariable int patientId) {
    	JsonObject jsonObject =new JsonObject();
        try {
        	treatmentTypeDAO.startTreatmentForPatientId(patientId);
        	jsonObject.addProperty("message", "Treatment started successfully");
        } catch (Exception ex) {
        	logger.error("Exception:", ex);
        	jsonObject.addProperty("error", ex.getMessage());
        } finally{
        	return jsonObject;
        }
    }
    
    @RequestMapping(value = "/completeTreatmentForPatientId/{patientId}", method = RequestMethod.GET)
    public @ResponseBody JsonObject completeTreatmentForPatientId(HttpServletRequest request, HttpServletResponse response, HttpSession session, @PathVariable int patientId) {
    	JsonObject jsonObject =new JsonObject();
        try {
        	long treatmenttime=treatmentTypeDAO.completeTreatmentForPatientId(patientId);
        	jsonObject.addProperty("message", "Treatment completed successfully</br> Time taken "+CommonUtil.getAverageTime(treatmenttime));
        } catch (Exception ex) {
        	logger.error("Exception:", ex);
        	jsonObject.addProperty("error", ex.getMessage());
        } finally{
        	return jsonObject;
        }
    }
    
    @RequestMapping(value = "/historicalDataCookUp/{count}", method = RequestMethod.GET)
    public @ResponseBody JsonObject historicalDataCookUp(HttpServletRequest request, HttpServletResponse response, HttpSession session, @PathVariable long count) {
    	JsonObject jsonObject =new JsonObject();
        try {
        	treatmentTypeDAO.historicalDataCookUp(count);
        	jsonObject.addProperty("message", "History Data Cookup completed successfully");
        } catch (Exception ex) {
        	logger.error("Exception:", ex);
        	jsonObject.addProperty("error", ex.getMessage());
        } finally{
        	return jsonObject;
        }
    }

	public TreatmentTypeDAO getTreatmentTypeDAO() {
		return treatmentTypeDAO;
	}

	public void setTreatmentTypeDAO(TreatmentTypeDAO treatmentTypeDAO) {
		this.treatmentTypeDAO = treatmentTypeDAO;
	}
}
