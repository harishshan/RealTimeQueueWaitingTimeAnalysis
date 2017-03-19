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
import com.harish.rtqwta.util.PatientDetailsComparator;
import com.harish.rtqwta.util.TreatmentTypeComparator;


@Controller
public class CommonController {

	@Autowired
	private TreatmentTypeDAO treatmentTypeDAO;
	
    private final Logger logger = LoggerFactory.getLogger(CommonController.class);

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
        	newPatientId = treatmentTypeDAO.getNewPatientId();        	
        } catch (Exception ex) {
        	logger.error("Exception:", ex);
        } finally{
        	return newPatientId;
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
        	Gson gson = new Gson();
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
    
    @RequestMapping(value = "/getPatientListForTreatment", method = RequestMethod.GET)
    public @ResponseBody JsonArray getPatientListForTreatment(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
    	JsonArray patientList = new JsonArray();
        try {
        	List<PatientDetails> patientDetailsList = treatmentTypeDAO.getPatientListForTreatment();
        	Collections.sort(patientDetailsList, new PatientDetailsComparator());
        	for(PatientDetails patientDetails: patientDetailsList){
        		JsonObject jsonObject= new JsonObject();
        		jsonObject.addProperty(CommonConstants.PatientDetails.PATIENT_ID, patientDetails.getPatient_id());
        		jsonObject.addProperty(CommonConstants.PatientDetails.PATIENT_NAME, patientDetails.getPatient_name());
        		jsonObject.addProperty(CommonConstants.PatientDetails.PATIENT_AGE, patientDetails.getPatient_age());
        		jsonObject.addProperty(CommonConstants.PatientDetails.LOCATION, patientDetails.getLocation());
        		jsonObject.addProperty(CommonConstants.PatientDetails.TREATMENT_TYPE, patientDetails.getTreatment_type());
        		jsonObject.addProperty(CommonConstants.PatientDetails.TOKEN_NUMBER, patientDetails.getToken_number());
        		jsonObject.addProperty(CommonConstants.PatientDetails.ADMISSION_TS, patientDetails.getAdmission_TS()!=null?patientDetails.getAdmission_TS().toString():null);
        		jsonObject.addProperty(CommonConstants.PatientDetails.TREATMENT_START_TS, patientDetails.getTreatment_start_TS()!=null?patientDetails.getTreatment_start_TS().toString():null);
        		jsonObject.addProperty(CommonConstants.PatientDetails.TREATMENT_COMPLETE_TS, patientDetails.getTreatment_complete_TS()!=null?patientDetails.getTreatment_complete_TS().toString():null);
        		patientList.add(jsonObject);
        	}
        } catch (Exception ex) {
        	logger.error("Exception:", ex);
        } finally{
        	return patientList;
        }
    }

	public TreatmentTypeDAO getTreatmentTypeDAO() {
		return treatmentTypeDAO;
	}

	public void setTreatmentTypeDAO(TreatmentTypeDAO treatmentTypeDAO) {
		this.treatmentTypeDAO = treatmentTypeDAO;
	}
}
