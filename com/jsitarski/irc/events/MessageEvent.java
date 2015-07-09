package com.jsitarski.irc.events;

import java.util.EventListener;
import java.util.EventObject;

public class MessageEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8731760605563287502L;
	private Object message;

	public MessageEvent(Object message) {
		super(message);
		this.message = message;
	}

	public Object getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return "MessageEvent [message=" + message + "]";
	}

	public void fireEvent(EventListener ml) {
		((MessageListener) ml).onMessage(this);
	}

}
