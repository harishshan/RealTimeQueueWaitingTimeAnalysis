package com.harish.rtqwta.entity;

import java.util.List;

public class TreatmentType {
	
	private int treatmentTypeId;
	private String treatmentType;
	private List<String> doctorList;
	public int getTreatmentTypeId() {
		return treatmentTypeId;
	}

	public void setTreatmentTypeId(int treatmentTypeId) {
		this.treatmentTypeId = treatmentTypeId;
	}

	public String getTreatmentType() {
		return treatmentType;
	}

	public void setTreatmentType(String treatmentType) {
		this.treatmentType = treatmentType;
	}

	public List<String> getDoctorList() {
		return doctorList;
	}

	public void setDoctorList(List<String> doctorList) {
		this.doctorList = doctorList;
	}
}
