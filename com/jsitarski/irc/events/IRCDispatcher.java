package com.jsitarski.irc.events;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

import com.jsitarski.irc.exceptions.NickNameInUseException;
import com.jsitarski.irc.exceptions.UnableToConnectException;
import com.jsitarski.irc.methods.IRCChannel;
import com.jsitarski.irc.methods.IRCClient;
import com.jsitarski.irc.methods.IRCMessage;
import com.jsitarski.irc.methods.IRCUser;

public class IRCDispatcher {
	private Object lock = new Object();
	private final List<EventListener> listeners = new ArrayList<EventListener>();

	private boolean running = true;

	private final List<String> IRC_INPUT = new ArrayList<String>();

	private IRCClient client;

	private IRCChannel channel;
	// rizon network regex statements.
	private final String CHANNEL_MESSAGE_REGEX = "(PRIVMSG #([a-z]|[A-Z]|))\\w+";
	private final String CHANNEL_MESSAGE_SENDER_REGEX = "(:([A-Z]|[a-z]|[0-9]))\\w+";
	private final String PRIVATE_MESSAGE_REGEX = "(PRIVMSG ([a-z]|[A-Z]|[0-9]))\\w+ :";
	private final String JOIN_MESSAGE_REGEX = "(JOIN )+(:#)+([A-Z]|[a-z]|[1-9])\\w+";
	private final String LEAVE_MESSAGE_REGEX = "((PART #)+([A-Z]|[a-z]|[0-9])\\w+)|(QUIT)";

	public IRCClient getClient() {
		return client;
	}

	public IRCChannel getChannel() {
		return channel;
	}

	// && TBot.getBot().getScriptHandler().getScript() != null
	public IRCDispatcher(final IRCClient client, final IRCChannel channel) {
		this.client = client;
		this.channel = channel;
		Thread clientThread = new Thread(new Runnable() {

			@Override
			public void run() {
				channel.connect();
				while (client.isConnected() && running) {
					try {
						updateIrcInput();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				channel.leave();
				client.close();
			}

		});
		clientThread.setName("IRC THREAD");
		clientThread.start();
	}

	public void stop() {
		running = false;
	}

	public void addListener(final EventListener event) {
		synchronized (lock) {
			listeners.add(event);
		}
	}

	public void removeListener(final EventListener event) {
		synchronized (lock) {
			listeners.remove(event);
		}
	}

	public void clear() {
		synchronized (lock) {
			listeners.clear();
		}
	}

	private void updateIrcInput() throws IOException {
		if (client != null && client.isConnected()) {
			BufferedReader br = client.getBufferedReader();
			if (br != null) {
				String message = null;
				if (br.ready()) {
					message = br.readLine();
					if (message != null) {
						IRC_INPUT.add(message);
						// fires default event.
	
						fireEvent(new MessageEvent(message));
	
						if (message.contains("(No more connections allowed on that IP)")) {
							try {
								throw new UnableToConnectException("Too many connections on ip address");
							} catch (UnableToConnectException e) {
								System.out.println("ERROR: " + message);
								client.close();
							}
						}
	
						if (message.contains("Nickname is already in use.") && message.contains("losslessone.com 433 *")) {
							try {
								throw new NickNameInUseException("Nick name in use");
							} catch (NickNameInUseException e) {
								System.out.println("ERROR: " + message);
								client.close();
	
							}
						}
	
						// fires ping event.
						if (message.startsWith("PING")) {
							fireEvent(new PingEvent(message, message.split(":")[0]));
						}
	
						// fires channel message..
						if (message.split(CHANNEL_MESSAGE_REGEX, 2).length > 1) {
							String channelMessage = message.split(CHANNEL_MESSAGE_REGEX)[1];
							String chMessage = channelMessage.substring(channelMessage.indexOf(":") + 1);
							String senderName = "";
							if (message.split(CHANNEL_MESSAGE_SENDER_REGEX, 2).length > 1) {
								senderName = message.split("!~", 2)[0].substring(1);
							}
							fireEvent(new ChannelMessageEvent(new IRCMessage(senderName, chMessage)));
						}
	
						else if (message.split(PRIVATE_MESSAGE_REGEX, 2).length > 1) {
							String privateMessage = message.split(PRIVATE_MESSAGE_REGEX, 2)[1];
							if (message.contains("PRIVMSG ") && message.contains(":")) {
								String senderName = "";
								if (message.split(CHANNEL_MESSAGE_SENDER_REGEX, 2).length > 1) {
									senderName = message.split("!~", 2)[0].substring(1);
								}
								if (!senderName.equalsIgnoreCase(client.getNickname())) {
									fireEvent(new PrivateMessageEvent(new IRCMessage(senderName, privateMessage)));
								}
							}
						}
	
						else if (message.split(JOIN_MESSAGE_REGEX, 2).length > 1) {
							if (message.split("#").length > 1) {
								String channelName = message.split("#")[1];
								String userName = message.split("!~", 2)[0].substring(1);
								if (channelName != null && userName != null) {
									fireEvent(new JoinEvent(new IRCUser(userName), channelName));
								}
							}
						} else if (message.split(LEAVE_MESSAGE_REGEX, 2).length > 1) {
							String channelName = "ALL";
							if (message.split("#").length > 1) {
								// channel name...
								channelName = message.split("#")[1];
							}
	
							String userName = message.split("!~", 2)[0].substring(1);
							fireEvent(new LeaveEvent(new IRCUser(userName), channelName));
						}
					}
				}
			}
		}
		try {
			Thread.sleep(55, 60);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void fireEvent(MessageEvent me) {
		synchronized (lock) {
			if (me != null) {
				for (int i = 0; i < listeners.size(); i++) {
					EventListener el = listeners.get(i);
					if (el instanceof MessageListener) {
						((MessageListener) el).onMessage(me);
	
						if (me instanceof PingEvent) {
							((MessageListener) el).onPing((PingEvent) me);
						}
						if (me instanceof ChannelMessageEvent) {
							((MessageListener) el).onChannelMessage((ChannelMessageEvent) me);
						}
						if (me instanceof PrivateMessageEvent) {
							((MessageListener) el).onPrivateMessage((PrivateMessageEvent) me);
						}
						if (me instanceof JoinEvent)
							((MessageListener) el).onUserJoin((JoinEvent) me);
						if (me instanceof LeaveEvent) {
							((MessageListener) el).onUserLeave((LeaveEvent) me);
						}
					}
				}
			}
		}
	}

}