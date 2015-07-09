package com.jsitarski.irc.events;

import java.util.EventListener;

public interface MessageListener extends EventListener {

	void onMessage(MessageEvent me);

	void onPing(PingEvent pEvent);

	void onUserJoin(JoinEvent jEvent);

	
	void onUserLeave(LeaveEvent lEvent);

	void onChannelMessage(ChannelMessageEvent cMessageEvent);

	void onPrivateMessage(PrivateMessageEvent pMessageEvent);

}
