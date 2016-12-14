package cn.my.chatclient.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.my.chatclient.service.OptionsService;

/**
 * 好友窗口
 * @author WB
 *
 */
@Component
public class FriendsWindow {

	private JFrame jFrame;
	private JPanel jContentPane;
	private JButton jButtonFind ;
	private JScrollPane jScrollPane ;
	private JTree jTree ;
	private Map<String, DefaultMutableTreeNode> allFriends = new HashMap<>();//所有好友

	@Autowired
	OptionsService optService;
	
	public void show(){
		optService.loadOnlineUsers();
		jFrame().setVisible(true);
	}

	public void showAllFriend(List<String> allFriends){
		
		this.allFriends.clear();
		
		allFriends.forEach(r -> {
			this.allFriends.put(r, new DefaultMutableTreeNode(r));
		});
		
		jScrollPane.setViewportView(jTree(initTree()));
		jScrollPane.setBackground(new Color(111,11,22,1));
	}

	public void hide(){
		jFrame().setVisible(false);
	}
	
	private JFrame jFrame() {
		if (jFrame == null) {
			jFrame = new JFrame();
			jFrame.setSize(new Dimension(210, 442));
			jFrame.setTitle("Talking");
			jFrame.setResizable(false);
			jFrame.setContentPane(jContentPane());
			jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			jFrame.setLocationRelativeTo(null);
		}
		return jFrame;
	}

	private JPanel jContentPane() {
		if (jContentPane == null) {
			jContentPane = new BackgroundPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getJButtonFind(), null);
			jContentPane.add(jScrollPane(), null);
		}
		return jContentPane;
	}
	private JButton getJButtonFind() {
		if (jButtonFind == null) {
			jButtonFind = new JButton();
			jButtonFind.setBounds(new Rectangle(1, 384, 200, 29));
			jButtonFind.setText("chat all");
			jButtonFind.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					//TODO
					System.out.println(1);
				}
			});
		}
		return jButtonFind;
	}

	private JScrollPane jScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setBounds(new Rectangle(2, 52, 200, 330));
		}
		return jScrollPane;
	}

	private JTree jTree(DefaultMutableTreeNode nodes) {
		jTree = new JTree(nodes);
		jTree.putClientProperty("JTree.lineStyle" , "Angeled");
		//给当前好友树添加一个双击事件
		jTree.addMouseListener(new MouseAdapter(){
			  public void mouseClicked(MouseEvent e){
				  if(e.getClickCount() == 2){
					  DefaultMutableTreeNode selectedNode=(DefaultMutableTreeNode)jTree.getLastSelectedPathComponent();
					  if(!selectedNode.toString().startsWith("好友列表")){
						  //TOOD
						  System.out.println(selectedNode);
					  }
				  	}
				  }
			});
		return jTree;
	}



	//初始化好友树的方法
	public DefaultMutableTreeNode initTree(){
		DefaultMutableTreeNode root = new DefaultMutableTreeNode ("好友列表("+allFriends.size()+")");
		if (this.allFriends != null && this.allFriends.size() > 0) {
			for (Map.Entry<String, DefaultMutableTreeNode> entry : allFriends.entrySet()) {
				// TODO 不将自己放在好友列表上
				
				root.add(entry.getValue());
			}
		}
		return root;
	}
}
