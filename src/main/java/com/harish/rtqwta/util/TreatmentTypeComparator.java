package com.harish.rtqwta.util;

import java.util.Comparator;

import com.harish.rtqwta.entity.TreatmentType;

public class TreatmentTypeComparator implements Comparator<TreatmentType> {
	public int compare(TreatmentType tt1, TreatmentType tt2) {
		return tt1.getTreatmentType().compareTo(tt2.getTreatmentType());
	}
}
