package com.jsitarski.irc.events;

import java.util.EventListener;

import com.jsitarski.irc.methods.IRCMessage;

public class ChannelMessageEvent extends MessageEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3055607654767773991L;

	public ChannelMessageEvent(IRCMessage message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void fireEvent(EventListener ml) {
		((MessageListener) ml).onChannelMessage(this);
	}

}
