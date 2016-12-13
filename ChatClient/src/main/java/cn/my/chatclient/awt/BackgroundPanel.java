package cn.my.chatclient.awt;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class BackgroundPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	private String image;
	private BufferedImage background;
	
	BackgroundPanel(){
		
	}
	BackgroundPanel(String image){
		this.image=image;
		try {
			background=ImageIO.read(getClass().getResource(image));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		if("background.jpg".equals(image))
			g.drawImage(background, -45,0, null);
		else
			g.drawImage(background,0,10,null);
	}
}