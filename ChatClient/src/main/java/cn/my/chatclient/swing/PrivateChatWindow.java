package cn.my.chatclient.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.springframework.beans.factory.annotation.Autowired;

import cn.my.chatclient.service.OptionsService;
import cn.my.chatclient.util.LocalStorage;

/**
 * 私聊窗口
 * 
 * @author WB
 *
 */
@org.springframework.stereotype.Component
public class PrivateChatWindow {

	private Map<String, ChatWindow> chats = new ConcurrentHashMap<String, PrivateChatWindow.ChatWindow>();

	@Autowired
	OptionsService optService;

	public void show(String target) {

		if (!chats.containsKey(target)) {
			chats.putIfAbsent(target, new ChatWindow(target));
		}

		chats.get(target).jFrame();
	}
	
	public void showChatMsg(String from, String msg){
		
		if (!chats.containsKey(from)) {
			show(from);
		}
		chats.get(from).showMessage(msg);
	}

	class ChatWindow {
		private JFrame jFrame;
		private JPanel jContentPane;
		private JButton jButton;
		private JScrollPane jScrollPane;
		private JScrollPane jScrollPaneOut;
		private JTextArea jTextArea;
		private String target;
		private JTextArea jTextAreaOut;

		ChatWindow(String target) {
			this.target = target;
			jFrame().setTitle("与" + target + "聊天中...");
			jFrame().setVisible(true);
		}

		JScrollPane jScrollPane() {
			if (jScrollPane == null) {
				jScrollPane = new JScrollPane();
				jScrollPane.setBounds(new Rectangle(2, 5, 358, 197));
				jScrollPane.setViewportView(jTextArea());
			}
			return jScrollPane;
		}

		JScrollPane jScrollPaneOut() {
			if (jScrollPaneOut == null) {
				jScrollPaneOut = new JScrollPane();
				jScrollPaneOut.setBounds(new Rectangle(2, 233, 358, 60));
				jScrollPaneOut.setViewportView(jTextAreaOut());
			}
			return jScrollPaneOut;
		}

		// 关闭聊天窗口
		void closeFrame() {
			jFrame.dispose();
		}

		JTextArea jTextArea() {
			if (jTextArea == null) {
				jTextArea = new JTextArea();
				jTextArea.setEnabled(true);
				jTextArea.setEditable(false);
			}
			return jTextArea;
		}

		JFrame jFrame() {
			if (jFrame == null) {
				jFrame = new JFrame();
				jFrame.setLayout(null);
				jFrame.setSize(new Dimension(361, 349));
				jFrame.setResizable(false);
				jFrame.setContentPane(jContentPane());
				jFrame.addWindowListener(new java.awt.event.WindowAdapter() {
					public void windowClosing(java.awt.event.WindowEvent e) {
						jFrame.dispose();
						chats.remove(target);
					}
				});
			}
			return jFrame;
		}

		JPanel jContentPane() {
			if (jContentPane == null) {
				jContentPane = new BackgroundPanel();
				jContentPane.setLayout(null);
				jContentPane.add(jButton(), null);
				jContentPane.setVisible(true);
				jContentPane.add(jScrollPane(), null);
				jContentPane.add(jScrollPaneOut(), null);
			}
			return jContentPane;
		}

		JTextArea jTextAreaOut() {
			if (jTextAreaOut == null) {
				jTextAreaOut = new JTextArea();
			}
			return jTextAreaOut;
		}

		JButton jButton() {
			if (jButton == null) {
				jButton = new JButton();
				jButton.setBounds(new Rectangle(293, 295, 60, 25));
				jButton.setBackground(new Color(0, 128, 255, 150));
				jButton.setText("send");
				jButton.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						sendMessage();
					}
				});
			}
			return jButton;
		}

		void sendMessage() {

			String msg = jTextAreaOut().getText();

			if (msg.length() > 0) {
				optService.sendToOne(LocalStorage.getUncheck().getName(), target, msg);
				jTextAreaOut().setText("");
			}
			

		}

		void showMessage(String message) {
			this.jTextArea.setText(this.jTextArea.getText() + target + "说:\n   " + message + "\n");
			this.jScrollPane.getVerticalScrollBar().setValue(jScrollPane.getVerticalScrollBar().getMaximum());
		}

		JTextArea jTextArea(Container c) {
			JTextArea textArea = null;
			for (int i = 0; i < c.getComponentCount(); i++) {
				Component cnt = c.getComponent(i);
				if (cnt instanceof JTextField) {
					return (JTextArea) cnt;
				}
				if (cnt instanceof Container) {
					textArea = jTextArea((Container) cnt);
					if (textArea != null) {
						return textArea;
					}
				}
			}
			return textArea;
		}
	}

}
