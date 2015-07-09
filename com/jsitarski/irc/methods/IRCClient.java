package com.jsitarski.irc.methods;

import java.io.IOException;

import com.jsitarski.irc.IRCConnection;

public class IRCClient extends IRCConnection {
	private String nickname;

	public IRCClient(String host, int port, String nickname) {
		super(host, port);
		this.nickname = nickname;
		init();
	}

	public String getNickname() {
		return nickname;
	}

	private void init() {
		if (outWriter != null && nickname != null) {
			try {
				outWriter.writeLine("NICK :" + nickname);
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				outWriter.writeLine("USER " + nickname + " * * :" + nickname);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
