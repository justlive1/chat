package cn.my.chatclient.awt;

import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.springframework.stereotype.Component;

import cn.my.chatclient.core.CallBack;

/**
 * 消息弹出框
 * 
 * @author WB
 *
 */
@Component
public class AlertWindow {

	private JFrame jFrame;
	private JPanel jContentPane;
	private JLabel jLabelAlertInfo;
	private JButton jButton;

	private CallBack callback;

	AlertWindow() {
		jFrame();
	}

	public void alert(String info) {
		jLabelAlertInfo.setText(info);
		jFrame().setVisible(true);
	}

	public void alert(String info, CallBack callback) {
		alert(info);
		this.callback = callback;
	}

	private JFrame jFrame() {
		if (jFrame == null) {
			jFrame = new JFrame();
			jFrame.setSize(new Dimension(200, 100));
			jFrame.setTitle("message");
			jFrame.setResizable(false);
			jFrame.setContentPane(jContentPane());
			jFrame.setLocationRelativeTo(null);
			jFrame.addWindowListener(new java.awt.event.WindowAdapter() {
				public void windowClosing(java.awt.event.WindowEvent e) {
					if (callback != null) {
						callback.call();
						callback = null;
					}
				}
			});
		}
		return jFrame;
	}

	private JPanel jContentPane() {
		if (jContentPane == null) {
			jLabelAlertInfo = new JLabel("",JLabel.CENTER);
			jLabelAlertInfo.setBounds(new Rectangle(10, 20, 180, 20));
			jLabelAlertInfo.setEnabled(false);
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(jLabelAlertInfo, null);
			jContentPane.add(jButton(), null);
		}
		return jContentPane;
	}

	private JButton jButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setBounds(new Rectangle(78, 50, 45, 20));
			jButton.setText("orz");
			jButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					jFrame().setVisible(false);
					if (callback != null) {
						callback.call();
						callback = null;
					}
				}
			});
		}
		return jButton;
	}

}
