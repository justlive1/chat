package cn.my.chatclient.swing;

import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JDialog;
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
public class AlertDialog {

	private JPanel jContentPane;
	private JLabel jLabelAlertInfo;
	private JButton jButton;
	private JDialog jDialog;

	private CallBack callback;
	
	AlertDialog() {
		jDialog();
	}

	public void alert(String info) {
		jLabelAlertInfo.setText(info);
		jDialog().setVisible(true);
	}

	public void alert(String info, CallBack callback) {
		alert(info);
		this.callback = callback;
	}

	private JDialog jDialog(){
		if(jDialog == null){
			jDialog = new JDialog();
			jDialog.setModal(true);
			jDialog.setSize(new Dimension(200, 100));
			jDialog.setTitle("message");
			jDialog.setResizable(false);
			jDialog.setContentPane(jContentPane());
			jDialog.setLocationRelativeTo(null);
			jDialog.addWindowListener(new java.awt.event.WindowAdapter() {
				public void windowClosing(java.awt.event.WindowEvent e) {
					if (callback != null) {
						callback.call();
						callback = null;
					}
				}
			});
		}
		return jDialog;
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
					jDialog().setVisible(false);
					if (callback != null) {
						callback.call();
						callback = null;
					}
					jLabelAlertInfo.setText("");
				}
			});
		}
		return jButton;
	}

}
