package tank;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JWindow;

public class Login extends JFrame implements MouseListener{
	LoginPanel loginPanel;
	LoginBut loginBut;
	public Login() {
		loginBut=new LoginBut();
		loginPanel=new LoginPanel();
		this.setSize(830, 473);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(3);
		this.setVisible(true);
		this.add(loginBut);
		this.add(loginPanel);
		loginBut.addMouseListener(this);
		
		
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if (e.getModifiers()==16) {
			this.setVisible(false);
			MyTankGame game = new MyTankGame();
		}
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
class LoginPanel extends JPanel{
	public LoginPanel() {
		// TODO Auto-generated constructor stub
		this.setSize(821, 434);
		this.setLocation(19, 20);
		
	}
	public void paint(Graphics g){
		BufferedImage loginImage;
		try {
			loginImage=ImageIO.read(new File("images/Login.png"));
			g.drawImage(loginImage, 0, 0, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
class LoginBut extends JButton{
	BufferedImage btnBufferedImage;
	public LoginBut() {
		// TODO Auto-generated constructor stub
		this.setSize(140, 40);
		this.setLocation(330,330);
		try {
			btnBufferedImage=ImageIO.read(MyTankGame.class.getResource("Login.png"));
			ImageIcon imageIcon=new ImageIcon(btnBufferedImage);
			this.setIcon(imageIcon);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}


