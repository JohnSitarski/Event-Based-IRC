package com.jsitarski.irc.events;

import java.util.EventListener;

public class PingEvent extends MessageEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String serverName;

	public PingEvent(String message, String serverName) {
		super(message);
		this.serverName = serverName;
	}

	public String getServerName() {
		return serverName;
	}

	@Override
	public void fireEvent(EventListener ml) {
		((MessageListener) ml).onPing(this);

	}

}
