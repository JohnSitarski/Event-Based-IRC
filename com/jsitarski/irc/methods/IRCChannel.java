package com.jsitarski.irc.methods;

import java.io.IOException;

import com.jsitarski.irc.IRCStreamWriter;

public class IRCChannel {

	private String channelName;
	private IRCStreamWriter outWriter;

	public IRCChannel(String channelName, IRCStreamWriter outWriter) {
		this.channelName = "#" + channelName;
		this.outWriter = outWriter;
		connect();
	}

	@Override
	public String toString() {
		return "IRCChannel [channelName=" + channelName + ", outWriter=" + outWriter + "]";
	}

	/**
	 * Gets the channel name.
	 *
	 * @return the channel name
	 */
	public String getChannelName() {
		return channelName;
	}

	/**
	 * Gets the out writer.
	 *
	 * @return the out writer
	 */
	public IRCStreamWriter getOutWriter() {
		return outWriter;
	}

	public void connect() {
		if (outWriter != null) {
			try {
				outWriter.writeLine("JOIN " + channelName);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void leave() {
		if (outWriter != null) {
			try {
				outWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Send public message.
	 *
	 * @param message the message
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void sendPublicMessage(String message) throws IOException {
		if (outWriter != null) {
			sendPrivMsg(channelName, message);
		}
	}

	/**
	 * Send private message.
	 *
	 * @param target the target
	 * @param message the message
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void sendPrivMsg(String target, String message) throws IOException {
		if (outWriter != null) {
			outWriter.writeLine("PRIVMSG " + target + " :" + message);
		}
	}
}
