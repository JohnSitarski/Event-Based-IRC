package com.jsitarski.irc.events;

import java.util.EventListener;

import com.jsitarski.irc.methods.IRCUser;

public class LeaveEvent extends MessageEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8735792467772520543L;
	private String channelName;

	public LeaveEvent(IRCUser message, String channelName) {
		super(message);
		this.channelName = channelName;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public void fireEvent(EventListener el) {
		((MessageListener) el).onUserLeave(this);
	}

}
