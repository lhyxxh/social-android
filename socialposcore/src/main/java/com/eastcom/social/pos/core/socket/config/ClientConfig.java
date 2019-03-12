package com.eastcom.social.pos.core.socket.config;

public class ClientConfig {
	private String host;
	private int port;
	private String signboard;
	
	public ClientConfig(String host, int port, String signboard){
		this.host = host;
		this.port = port;
		this.signboard = signboard;
	}
	
	public String getHost() {
		return host;
	}
	
	public int getPort() {
		return port;
	}

	public String getSignboard() {
		return signboard;
	}

	
}
