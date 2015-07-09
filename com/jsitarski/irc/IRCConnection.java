package com.jsitarski.irc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class IRCConnection {
	protected String host;
	protected int port;
	protected volatile IRCStreamWriter outWriter = null;
	protected InputStream inputStream = null;
	protected BufferedReader bufferedReader = null;
	protected Socket socket;
	private boolean connected = false;

	public IRCConnection(String host) {
		this(host, 6667);
	}

	public IRCConnection(String host, int port) {
		this.host = host;
		this.port = port;
		connect();
	}

	private void connect() {
		try {
			socket = new Socket(host, port);
			socket.setKeepAlive(true);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (socket != null) {
			System.out.println("ONLINE: ");
			OutputStream oStream = null;
			try {
				oStream = socket.getOutputStream();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (oStream != null) {
				outWriter = new IRCStreamWriter(oStream);
			}
			InputStream istream = null;
			try {
				istream = socket.getInputStream();
				bufferedReader = new BufferedReader(new InputStreamReader(istream));
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (istream != null) {
				connected = true;
				inputStream = istream;
			}
		}
	}

	/**
	 * Gets the buffered reader.
	 *
	 * @return the buffered reader
	 */
	public BufferedReader getBufferedReader() {
		return bufferedReader;
	}

	/**
	 * Gets the host.
	 *
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * Gets the port.
	 *
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Gets the output writer.
	 *
	 * @return the output writer
	 */
	public IRCStreamWriter getOutputWriter() {
		return outWriter;
	}

	/**
	 * Gets the input stream.
	 *
	 * @return the input stream
	 */
	public InputStream getInputStream() {
		return inputStream;
	}

	@Override
	public String toString() {
		return "IRCConnection [host=" + host + ", port=" + port + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof IRCConnection))
			return false;
		IRCConnection other = (IRCConnection) obj;
		if (host == null) {
			if (other.host != null)
				return false;
		} else if (!host.equals(other.host))
			return false;
		if (port != other.port)
			return false;
		return true;
	}

	/**
	 * Checks if is connected.
	 *
	 * @return true, if is connected
	 */
	public boolean isConnected() {
		return socket != null && socket.isConnected() && connected;
	}

	/**
	 * Closes the socket and all the streams.
	 */
	public void close() {
		if (isConnected())
			try {
				outWriter.writeLine("/quit");
			} catch (IOException e) {
				e.printStackTrace();
			}
		try {
			socket.close();
			bufferedReader.close();
			outWriter.close();
			inputStream.close();
			connected = false;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
