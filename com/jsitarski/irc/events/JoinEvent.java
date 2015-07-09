package com.jsitarski.irc.events;

import java.util.EventListener;

import com.jsitarski.irc.methods.IRCUser;

public class JoinEvent extends MessageEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -196052511340185208L;
	private String ircChannel;

	public JoinEvent(IRCUser user, String ircChannel) {
		super(user);
		this.ircChannel = ircChannel;
	}

	public String getIrcChannelName() {
		return ircChannel;
	}

	@Override
	public void fireEvent(EventListener ml) {
		((MessageListener) ml).onUserJoin(this);

	}
}
