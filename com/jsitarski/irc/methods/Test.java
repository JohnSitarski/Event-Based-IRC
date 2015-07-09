package com.jsitarski.irc.methods;

import java.awt.Color;
import java.awt.EventQueue;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import com.jsitarski.irc.events.ChannelMessageEvent;
import com.jsitarski.irc.events.IRCDispatcher;
import com.jsitarski.irc.events.JoinEvent;
import com.jsitarski.irc.events.LeaveEvent;
import com.jsitarski.irc.events.MessageEvent;
import com.jsitarski.irc.events.MessageListener;
import com.jsitarski.irc.events.PingEvent;
import com.jsitarski.irc.events.PrivateMessageEvent;

public class Test extends JFrame implements MessageListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2835355151978068586L;
	private JPanel contentPane;
	private JTextArea textArea;
	static IRCDispatcher dispatcher;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					IRCClient client = new IRCClient("irc.rizon.net", 6667 , "srsrrsdsa");
					dispatcher = new IRCDispatcher(client, new IRCChannel("scrubbots", client.getOutputWriter()));
					Test frame = new Test();
					dispatcher.addListener(frame);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Test() {
		setTitle("IRC TEST");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 435, 260);
		contentPane.add(scrollPane);

		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		textArea.setForeground(Color.CYAN);
		textArea.setBackground(Color.BLACK);
	}

	@Override
	public void onMessage(MessageEvent me) {
		textArea.setText(textArea.getText() + "\n" + me);
		// System.out.println(me);
		System.out.println(me.getMessage());
	}

	@Override
	public void onPing(PingEvent pEvent) {
		try {
			dispatcher.getClient().getOutputWriter().writeLine(pEvent.getMessage().toString().replace("PING", "PONG"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onUserJoin(JoinEvent jEvent) {

	}

	@Override
	public void onChannelMessage(ChannelMessageEvent cMessageEvent) {
		System.out.println(cMessageEvent);
	}

	@Override
	public void onPrivateMessage(PrivateMessageEvent pMessageEvent) {
		System.out.println(pMessageEvent);
	}

	@Override
	public void onUserLeave(LeaveEvent lEvent) {
		// TODO Auto-generated method stub

	}
}
