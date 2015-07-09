package com.jsitarski.irc.events;

import java.util.EventListener;

import com.jsitarski.irc.methods.IRCMessage;

public class PrivateMessageEvent extends MessageEvent{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4929829256751030319L;

	public PrivateMessageEvent(IRCMessage message) {
		super(message);
		
	}

	@Override
	public void fireEvent(EventListener ml) {
		((MessageListener) ml).onPrivateMessage(this);
	}

}
