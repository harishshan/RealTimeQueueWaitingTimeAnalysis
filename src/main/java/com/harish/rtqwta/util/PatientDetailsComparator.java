package com.harish.rtqwta.util;

import java.util.Comparator;

import com.harish.rtqwta.entity.PatientDetails;

public class PatientDetailsComparator implements Comparator<PatientDetails> {
	public int compare(PatientDetails pd1, PatientDetails pd2) {
		return pd1.getPatient_id()- pd2.getPatient_id();
	}
}
