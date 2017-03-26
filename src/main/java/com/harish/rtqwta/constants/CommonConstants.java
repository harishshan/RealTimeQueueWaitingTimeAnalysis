package com.harish.rtqwta.constants;

public class CommonConstants {
	public static class TreatmentType{
		public static final String TREATMENT_TYPE_ID = "Treatment_Type_Id";
		public static final String TREATMENT_TYPE_NAME = "Treatment_Type";
		public static final String DOCTOR_LIST = "Doctor_List";
		public static final String DOCTOR_NAME = "Doctor_Name";
	}
	public static class CommonCounter{
		public static final String PATIENT = "Patient";
		public static final String HISTORICAL_PATIENT = "Historical_Patient";
		public static final String COUNTER_VALUE = "counter_value";
		public static final String TABLE_NAME = "table_name";
	}
	public static class PatientDetails{
		public static final String PATIENT_ID = "patient_id";
		public static final String PATIENT_NAME = "patient_name";
		public static final String PATIENT_AGE = "patient_age";
		public static final String PATIENT_GENDER = "patient_gender";
		public static final String LOCATION = "location";
		public static final String TREATMENT_TYPE = "treatment_type";
		public static final String TOKEN_NUMBER = "token_number";
		public static final String ADMISSION_TS = "admission_ts";
		public static final String TREATMENT_START_TS = "treatment_start_ts";
		public static final String TREATMENT_COMPLETE_TS = "treatment_complete_ts";
		public static final String DOCTOR = "doctor";
		public static final String EXPECTED_TREATMENT_START_TS = "expected_treatment_start_ts";
		public static final String EXPECTED_TREATMENT_COMPLETE_TS = "expected_treatment_complete_ts";
		public static final String STATUS = "status";
		public static class Status{
			public static final String COMPLETED ="C";
			public static final String WAITING ="W";
			public static final String STARTED="S";
		}
	}

	public static class Analysis{
		public static final String CATEGORY = "category";
		public static final String SUB_CATEGORY = "sub_category";
		public static final String PATIENTS_COUNT = "patients_count";
		public static final String TOTAL_WAITING_TIME = "total_waiting_time";
		public static final String AVG_WAITING_TIME = "avg_waiting_time";
		public static final String TOTAL_TREATMENT_TIME = "total_treatment_time";
		public static final String AVG_TREATMENT_TIME = "avg_treatment_time";
		public static class Category{
			public static final String AGE = "AGE";
			public static final String GENDER = "GENDER";
			public static final String TREATMENT_TYPE = "TREATMENT_TYPE";
			public static final String DAY = "DAY";
			public static final String DATE = "DATE";
			public static final String HOUR = "HOUR";
		}
		public static class SubCategory{
			public static final String BELOW_TWENTY = "20";
			public static final String TWENTY_TO_FOURTY = "20-40";
			public static final String FOURTY_TO_SIXTY = "40-60";
			public static final String SIXTY_TO_EIGHTY = "60-80";
			public static final String ABOVE_EIGHTY = "80";
			public static final String MALE = "MALE";
			public static final String FEMALE = "FEMALE";
			public static final String OTHERS = "OTHERS";
		}
	}
}
