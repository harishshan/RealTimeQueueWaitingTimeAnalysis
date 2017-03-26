CREATE KEYSPACE rtqwta WITH REPLICATION={'class':'SimpleStrategy','replication_factor':1};
CREATE TABLE rtqwta.treatment_type(treatment_type_id int PRIMARY KEY, treatment_type text, doctor_list List<text>);

INSERT INTO  rtqwta.treatment_type(treatment_type_id, treatment_type, doctor_list) VALUES(1,'Surgery',['Harish','Jagan','Rohith']);
INSERT INTO  rtqwta.treatment_type(treatment_type_id, treatment_type, doctor_list) VALUES(2,'Scan',['Sabari','Mega','Yuvaraj']);
INSERT INTO  rtqwta.treatment_type(treatment_type_id, treatment_type, doctor_list) VALUES(3,'Pharmacy',['Vijay','Murali','Ashok']);
INSERT INTO  rtqwta.treatment_type(treatment_type_id, treatment_type, doctor_list) VALUES(4,'X-ray',['Adithya','Sai Praveen','Shekar Sharma']);
INSERT INTO  rtqwta.treatment_type(treatment_type_id, treatment_type, doctor_list) VALUES(5,'CT-Scan',['Vishwa','Vikas','Gopi']);
INSERT INTO  rtqwta.treatment_type(treatment_type_id, treatment_type, doctor_list) VALUES(6,'MRI-Scan',['Ajay','Ganesh','Siva']);


CREATE TABLE rtqwta.common_counter(counter_value counter,table_name  text, PRIMARY KEY (table_name));
UPDATE rtqwta.common_counter SET counter_value = counter_value + 0 WHERE table_name='Patient';
UPDATE rtqwta.common_counter SET counter_value = counter_value + 0 WHERE table_name='Historical_Patient';

CREATE TABLE rtqwta.patient_details(patient_id int PRIMARY KEY,patient_name  text, patient_age int, patient_gender text, location text, treatment_type text, 
	token_number text, admission_ts timestamp, treatment_start_ts timestamp, treatment_complete_ts timestamp, doctor text, status text, expected_treatment_start_ts timestamp, expected_treatment_complete_ts timestamp);
CREATE TABLE rtqwta.historical_patient_details(patient_id int PRIMARY KEY,patient_name  text, patient_age int, patient_gender text, location text, treatment_type text, 
	token_number text, admission_ts timestamp, treatment_start_ts timestamp, treatment_complete_ts timestamp, doctor text, status text);
	
CREATE TABLE rtqwta.analysis(category text, sub_category text, patients_count bigint, total_waiting_time bigint, avg_waiting_time bigint, total_treatment_time bigint, avg_treatment_time bigint, 
	PRIMARY KEY(category, sub_category));
CREATE TABLE rtqwta.historical_analysis(category text, sub_category text, patients_count bigint, total_waiting_time bigint, avg_waiting_time bigint, total_treatment_time bigint, avg_treatment_time bigint, 
	PRIMARY KEY(category, sub_category));

