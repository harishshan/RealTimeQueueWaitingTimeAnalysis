package com.harish.rtqwta.entity;

public class Analysis {
	private String category;
	private String sub_category;
	private long patients_count;
	private long total_waiting_time;
	private long avg_waiting_time;
	private long total_treatment_time;
	private long avg_treatment_time;
	private long waiting_time;
	private long treatment_time;
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getSub_category() {
		return sub_category;
	}
	public void setSub_category(String sub_category) {
		this.sub_category = sub_category;
	}
	public long getPatients_count() {
		return patients_count;
	}
	public void setPatients_count(long patients_count) {
		this.patients_count = patients_count;
	}
	public long getTotal_waiting_time() {
		return total_waiting_time;
	}
	public void setTotal_waiting_time(long total_waiting_time) {
		this.total_waiting_time = total_waiting_time;
	}
	public long getAvg_waiting_time() {
		return avg_waiting_time;
	}
	public void setAvg_waiting_time(long avg_waiting_time) {
		this.avg_waiting_time = avg_waiting_time;
	}
	public long getTotal_treatment_time() {
		return total_treatment_time;
	}
	public void setTotal_treatment_time(long total_treatment_time) {
		this.total_treatment_time = total_treatment_time;
	}
	public long getAvg_treatment_time() {
		return avg_treatment_time;
	}
	public void setAvg_treatment_time(long avg_treatment_time) {
		this.avg_treatment_time = avg_treatment_time;
	}
	public long getWaiting_time() {
		return waiting_time;
	}
	public void setWaiting_time(long waiting_time) {
		this.waiting_time = waiting_time;
	}
	public long getTreatment_time() {
		return treatment_time;
	}
	public void setTreatment_time(long treatment_time) {
		this.treatment_time = treatment_time;
	}
	
}
