package com.harish.rtqwta.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.Session;

public class CassandraConnection {
	private Cluster cluster;
	private Session session;
	private String hostname;
	private int port;
	private String database;

	private final Logger logger = LoggerFactory.getLogger(CassandraConnection.class);
	 
	public void initMethod() {
		try {
			this.cluster = Cluster.builder().addContactPoint(hostname).withPort(port).build();
			final Metadata metadata = cluster.getMetadata();
			logger.info("Connected to cluster: "+ metadata.getClusterName());
			for (final Host host : metadata.getAllHosts()) {
				logger.info("Datacenter: "+host.getDatacenter()+"; Host: "+host.getAddress()+"; Rack: "+host.getRack());
			}
			session = cluster.connect(database);
		} catch (Exception ex) {
			logger.error("Exception:", ex);
		}
	}

	public Cluster getCluster() {
		return cluster;
	}

	public void setCluster(Cluster cluster) {
		this.cluster = cluster;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}
}
