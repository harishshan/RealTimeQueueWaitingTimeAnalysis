package com.harish.rtqwta.util;

import java.util.UUID;

import com.harish.rtqwta.bolts.AgeBolt;
import com.harish.rtqwta.bolts.DayDateHourBolt;
import com.harish.rtqwta.bolts.GenderBolt;
import com.harish.rtqwta.bolts.TimeCalculationBolt;
import com.harish.rtqwta.bolts.TreatmentTypeBolt;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.topology.TopologyBuilder;
import storm.kafka.BrokerHosts;
import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.StringScheme;
import storm.kafka.ZkHosts;

public class StromEngine {

	public static void main(String[] args) {
		try{
        	Config config = new Config();
            config.setDebug(true);
            config.put(Config.TOPOLOGY_MAX_SPOUT_PENDING, 1);
            String zkConnString = "localhost:2181";
            String topic = "event";
            BrokerHosts hosts = new ZkHosts(zkConnString);
            
            SpoutConfig kafkaSpoutConfig = new SpoutConfig (hosts, topic, "/" + topic,  UUID.randomUUID().toString());
            kafkaSpoutConfig.bufferSizeBytes = 1024 * 1024 * 4;
            kafkaSpoutConfig.fetchSizeBytes = 1024 * 1024 * 4;
            kafkaSpoutConfig.scheme = new SchemeAsMultiScheme(new StringScheme());

            TopologyBuilder builder = new TopologyBuilder();
            builder.setSpout("Kafka-Spout", new KafkaSpout(kafkaSpoutConfig));
            builder.setBolt("TimeCalulation-Bolt", new TimeCalculationBolt()).shuffleGrouping("Kafka-Spout");
            builder.setBolt("AgeBolt", new AgeBolt()).shuffleGrouping("TimeCalulation-Bolt");
            builder.setBolt("GenderBolt", new GenderBolt()).shuffleGrouping("AgeBolt");
            builder.setBolt("TreatmentTypeBolt", new TreatmentTypeBolt()).shuffleGrouping("GenderBolt");
            builder.setBolt("DayDateHourBolt", new DayDateHourBolt()).shuffleGrouping("TreatmentTypeBolt");
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("KafkaStormSample", config, builder.createTopology());
            Thread.sleep(Long.MAX_VALUE);     
            cluster.shutdown();
        }catch(Exception ex){
        	ex.printStackTrace();
        }
	}

}
