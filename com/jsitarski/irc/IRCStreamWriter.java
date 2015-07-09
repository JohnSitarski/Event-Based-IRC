package com.jsitarski.irc;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * The Class IRCStreamWriter.
 */
public class IRCStreamWriter extends OutputStreamWriter {

	private OutputStream outputStream;

	/**
	 * Instantiates a new IRC stream writer.
	 *
	 * @param outputStream the output stream
	 */
	public IRCStreamWriter(OutputStream outputStream) {
		super(outputStream);
		this.outputStream = outputStream;
	}


	/**
	 * Write line.
	 *
	 * @param message the message
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void writeLine(String message) throws IOException {
		if (outputStream != null) {
			if (message != null) {
				if (!message.endsWith("\r\n")) {
					message = message + "\r\n";
				}
				write(message);
				flush();
			}
		}
	}

}
