package com.jsitarski.irc.methods;

public class IRCUser {
	// TODO FINISH ADD NICK/PASS

	private String userName;

	public IRCUser(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof IRCUser))
			return false;
		IRCUser other = (IRCUser) obj;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "IRCUser [userName=" + userName + "]";
	}

}
