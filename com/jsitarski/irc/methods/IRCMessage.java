package com.jsitarski.irc.methods;

public class IRCMessage {

	private String senderName;
	private String message;

	public IRCMessage(String senderName, String message) {
		this.senderName = senderName;
		this.message = message;
	}

	@Override
	public String toString() {
		return "IRCMessage [senderName=" + senderName + ", message=" + message + "]";
	}

	public String getSenderName() {
		return senderName;
	}

	public String getMessage() {
		return message;
	}

}
