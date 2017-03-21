package com.harish.rtqwta.entity;

import java.util.Date;

public class PatientDetails {
	private int patient_id;
	private String patient_name;
	private int patient_age;
	private String location;
	private String token_number;
	private String treatment_type;
	private Date  admission_TS;
	private Date treatment_start_TS;
	private Date treatment_complete_TS;
	private String doctor;
	private String status;
	private long waitingTime;
	private long treatmentTime;
	public int getPatient_id() {
		return patient_id;
	}
	public void setPatient_id(int patient_id) {
		this.patient_id = patient_id;
	}
	public String getPatient_name() {
		return patient_name;
	}
	public void setPatient_name(String patient_name) {
		this.patient_name = patient_name;
	}
	public int getPatient_age() {
		return patient_age;
	}
	public void setPatient_age(int patient_age) {
		this.patient_age = patient_age;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getToken_number() {
		return token_number;
	}
	public void setToken_number(String token_number) {
		this.token_number = token_number;
	}
	public String getTreatment_type() {
		return treatment_type;
	}
	public void setTreatment_type(String treatment_type) {
		this.treatment_type = treatment_type;
	}
	public Date getAdmission_TS() {
		return admission_TS;
	}
	public void setAdmission_TS(Date admission_TS) {
		this.admission_TS = admission_TS;
	}
	public Date getTreatment_start_TS() {
		return treatment_start_TS;
	}
	public void setTreatment_start_TS(Date treatment_start_TS) {
		this.treatment_start_TS = treatment_start_TS;
	}
	public Date getTreatment_complete_TS() {
		return treatment_complete_TS;
	}
	public void setTreatment_complete_TS(Date treatment_complete_TS) {
		this.treatment_complete_TS = treatment_complete_TS;
	}
	public String getDoctor() {
		return doctor;
	}
	public void setDoctor(String doctor) {
		this.doctor = doctor;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public long getWaitingTime() {
		return waitingTime;
	}
	public void setWaitingTime(long waitingTime) {
		this.waitingTime = waitingTime;
	}
	public long getTreatmentTime() {
		return treatmentTime;
	}
	public void setTreatmentTime(long treatmentTime) {
		this.treatmentTime = treatmentTime;
	}
	

}
