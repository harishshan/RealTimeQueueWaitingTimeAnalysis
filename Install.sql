CREATE KEYSPACE rtqwta WITH REPLICATION={'class':'SimpleStrategy','replication_factor':1};
CREATE TABLE rtqwta.treatment_type(treatment_type_id int PRIMARY KEY, treatment_type text, doctor_list List<text>);

INSERT INTO  rtqwta.treatment_type(treatment_type_id, treatment_type, doctor_list) VALUES(1,'Surgery',['Harish','Jagan','Rohith']);
INSERT INTO  rtqwta.treatment_type(treatment_type_id, treatment_type, doctor_list) VALUES(2,'Scan',['Sabari','Mega','Yuvaraj']);
INSERT INTO  rtqwta.treatment_type(treatment_type_id, treatment_type, doctor_list) VALUES(3,'Pharmacy',['Vijay','Murali','Ashok']);
INSERT INTO  rtqwta.treatment_type(treatment_type_id, treatment_type, doctor_list) VALUES(4,'X-ray',['Adithya','Sai Praveen','Shekar Sharma']);
INSERT INTO  rtqwta.treatment_type(treatment_type_id, treatment_type, doctor_list) VALUES(5,'CT-Scan',['Vishwa','Vikas','Gopi']);
INSERT INTO  rtqwta.treatment_type(treatment_type_id, treatment_type, doctor_list) VALUES(6,'MRI-Scan',['Ajay','Ganesh','Siva']);


CREATE TABLE rtqwta.common_counter(counter_value counter,table_name  text, PRIMARY KEY (table_name));
UPDATE rtqwta.common_counter SET counter_value = counter_value + 1 WHERE table_name='Patient';

CREATE TABLE rtqwta.patient_details(patient_id int PRIMARY KEY,patient_name  text, patient_age int, location text, treatment_type text, 
	token_number text, admission_ts timestamp, treatment_start_ts timestamp, treatment_complete_ts timestamp);
