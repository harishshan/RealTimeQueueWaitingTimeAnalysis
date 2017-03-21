package com.harish.rtqwta.util;

import java.util.Properties;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kafka.admin.AdminUtils;
import kafka.admin.RackAwareMode;
import kafka.utils.ZKStringSerializer$;
import kafka.utils.ZkUtils;

public class KafkaUtil {
	private String zookeeperHosts;
	private String kafkaHosts;
	private String topicName;
	private int noOfPartitions;
	private int noOfReplication;
	private final Logger logger = LoggerFactory.getLogger(KafkaUtil.class);
	public void createEventTopic(){
		ZkClient zkClient=null;
        try {
            int sessionTimeOutInMs = 15 * 1000; // 15 secs
            int connectionTimeOutInMs = 10 * 1000; // 10 secs
            //For New Version
            zkClient = new ZkClient(zookeeperHosts, sessionTimeOutInMs, connectionTimeOutInMs, ZKStringSerializer$.MODULE$);
           
            ZkUtils zkUtils = new ZkUtils(zkClient, new ZkConnection(zookeeperHosts), false);
            Properties topicConfiguration = new Properties();
            if (AdminUtils.topicExists(zkUtils, topicName)){
            	logger.warn("Topic "+ topicName +" already exists");
            } else{
            	AdminUtils.createTopic(zkUtils, topicName, noOfPartitions, noOfReplication, topicConfiguration, RackAwareMode.Disabled$.MODULE$);
            }
            //For older version
        } catch (Exception ex) {
            logger.error("Exception:",ex);
        } finally {
            if (zkClient != null) {
                zkClient.close();
            }
        }
	}
	public void sendMessage(String message){
		Producer<String, String> producer=null;
		try{
			Properties props = new Properties();
			props.put("bootstrap.servers", kafkaHosts);
			props.put("acks", "all");
			props.put("retries", 0);
			props.put("batch.size", 16384);
			props.put("linger.ms", 1);
			props.put("buffer.memory", 33554432);
			props.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
			props.put("value.serializer","org.apache.kafka.common.serialization.StringSerializer");
			producer = new KafkaProducer<String, String>(props);
		    producer.send(new ProducerRecord<String, String>(topicName, message));
		}catch(Exception ex){
			logger.error("Exception:",ex);
		}finally{
			producer.close();
		}
	}
	
	public String getKafkaHosts() {
		return kafkaHosts;
	}
	public void setKafkaHosts(String kafkaHosts) {
		this.kafkaHosts = kafkaHosts;
	}
	public String getZookeeperHosts() {
		return zookeeperHosts;
	}
	public void setZookeeperHosts(String zookeeperHosts) {
		this.zookeeperHosts = zookeeperHosts;
	}
	public String getTopicName() {
		return topicName;
	}
	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}
	public int getNoOfPartitions() {
		return noOfPartitions;
	}
	public void setNoOfPartitions(int noOfPartitions) {
		this.noOfPartitions = noOfPartitions;
	}
	public int getNoOfReplication() {
		return noOfReplication;
	}
	public void setNoOfReplication(int noOfReplication) {
		this.noOfReplication = noOfReplication;
	}
}
