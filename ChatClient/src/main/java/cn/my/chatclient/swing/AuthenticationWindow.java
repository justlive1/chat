package cn.my.chatclient.swing;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;

import javax.annotation.PostConstruct;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import cn.my.chatclient.service.OptionsService;

/**
 * 登陆窗口
 * 
 * @author WB
 *
 */
@Component
public class AuthenticationWindow {

	private JFrame jFrame;
	private JPanel jPanel;
	private JButton loginBtn, registerBtn;
	private JLabel userNameText, passwordText;
	private JTextField userNameField;
	private JPasswordField passwordField;

	@Autowired
	OptionsService optionsService;
	@Autowired
	WindowsDispacher dispacher;

	@PostConstruct
	void init() {

		jframe().setVisible(true);

	}

	private JTextField userNameField() {
		if (userNameField == null) {
			userNameField = new JTextField();
			userNameField.setBounds(new Rectangle(90, 66, 266, 33));
			// userNameField.addKeyListener(new java.awt.event.KeyAdapter() {
			// public void keyPressed(java.awt.event.KeyEvent e) {
			// if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
			// doLogin();
			// }
			// }
			// });
		}
		return userNameField;
	}

	private JPasswordField passwordField() {
		if (passwordField == null) {
			passwordField = new JPasswordField();
			passwordField.setBounds(new Rectangle(90, 121, 266, 33));
			passwordField.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyPressed(java.awt.event.KeyEvent e) {
					if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
						doLogin();
					}
				}
			});
		}
		return passwordField;
	}

	private JButton loginBtn() {
		if (loginBtn == null) {
			loginBtn = new JButton();
			loginBtn.setBounds(new Rectangle(90, 172, 120, 41));
			loginBtn.setText("login in");
			loginBtn.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					doLogin();
				}
			});
		}
		return loginBtn;
	}

	private JButton registerBtn() {
		if (registerBtn == null) {
			registerBtn = new JButton();
			registerBtn.setBounds(new Rectangle(235, 172, 120, 41));
			registerBtn.setText("sign up");
			registerBtn.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					doRegister();
				}
			});
		}
		return registerBtn;
	}

	private JFrame jframe() {
		if (jFrame == null) {
			jFrame = new JFrame();
			jFrame.setSize(new Dimension(389, 274));
			jFrame.setTitle("Authentication");
			jFrame.setResizable(false);
			jFrame.setContentPane(jContentPane());
			jFrame.setLocationRelativeTo(null);
			jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		}
		return jFrame;
	}

	private JPanel jContentPane() {
		if (jPanel == null) {
			userNameText = new JLabel();
			userNameText.setBounds(new Rectangle(18, 63, 335, 38));
			userNameText.setFont(new Font("Dialog", Font.BOLD, 14));
			userNameText.setText("username");
			passwordText = new JLabel();
			passwordText.setBounds(new Rectangle(18, 117, 335, 38));
			passwordText.setFont(new Font("Dialog", Font.BOLD, 14));
			passwordText.setText("password");
			jPanel = new BackgroundPanel();
			jPanel.setLayout(null);
			jPanel.add(userNameText, null);
			jPanel.add(passwordText, null);
			jPanel.add(userNameField(), null);
			jPanel.add(passwordField(), null);
			jPanel.add(loginBtn(), null);
			jPanel.add(registerBtn(), null);
		}
		return jPanel;
	}

	private void doLogin() {

		String username = userNameField().getText();
		String password = new String(passwordField().getPassword());

		if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
			dispacher.alert("请输入用户名和密码");
			return;
		}

		optionsService.login(username, password);

	}

	private void doRegister() {

		String username = userNameField().getText();
		String password = new String(passwordField().getPassword());

		if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
			dispacher.alert("请输入用户名和密码");
			return;
		}

		optionsService.register(username, password);

	}
	
	public void loginSuccessed() {
		userNameField().setText("");
		passwordField().setText("");
		jframe().setVisible(false);
		dispacher.showFriends();
	}

	public void registerSuccessed() {
		loginBtn().setEnabled(true);
		registerBtn().setEnabled(true);
		dispacher.alert("注册成功");
	}

}
