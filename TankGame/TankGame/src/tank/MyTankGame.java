package tank;

import java.awt.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

/*
 * ���ܣ�̹�˴�ս2.0
 * 1: ����̹��
 * 2���ҷ�̹�˿��������ƶ�
 * 3: ��������̹��
 * 4: �ҷ�̹�˿��Է��ӵ� 
 * 5���ӵ���������(�������������)
 * 6: ���ҵ�̹�˻��е���̹��ʱ�򣬵��˱�ը����ʧ
 *    ���ж��ӵ��Ƿ����̹�ˣ�ʲôʱ����ã�
 *    ����ը��1����׼������ͼ��2������Bomb�ࣻ3���ڻ��е���̹��ʱ����ը��Vector 4�����ƣ�
 * 7: ����̹���ڹ涨��Χ�ƶ�
 * 8������̹��Ҳ�ܷ��ӵ�
 * 9: ������̹�˻����ҷ�̹�ˣ��ҷ�̹����ʧ
 * 10��������˫�������ӵ�����ǽ�岿�֣��ӵ�����ʧ
 */

public class MyTankGame extends JFrame{
    MyPanel mp = null;
    public static void main(String[] args) {
        // TODO Auto-generated method stub
//        MyTankGame game = new MyTankGame();
        Login login=new Login();
//    	Restart restart=new Restart();
    }

    public MyTankGame(){
        mp =  new MyPanel(); 
        Thread t = new Thread(mp);
        t.start(); 
        this.add(mp);
        //ע�����
        this.addKeyListener(mp);
        this.setSize(1200, 900);
        this.setTitle("̹�˴�ս-tarena");
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
}
class MyPanel extends JPanel implements KeyListener,Runnable{
	   static BufferedImage qianBufferedImage;
	//��������洢�з�̹�ˡ��ӵ��ȣ����������������ػ�����Ԫ�ء�����g����Ϊ���ʴ���
	
	//����һ���ҷ�̹��
    Hero hero = null;

    //������˵�̹��
    Vector<EnemyTank> ets = new Vector<EnemyTank>();

    //����һ��ը���ļ���
    Vector<Bomb>  bombs = new Vector<Bomb>();

    //����̹�˶���
    int enSize = 4;

    //��������ͼƬ��ͼƬ���л����������һ��ը��
    Image image1 = null;
    Image image2 = null;
    Image image3 = null;
    
    //��̬��:�������һ�α����ص�������ʱ��ִ��һ�Σ�ר���������ؾ�̬��Դ��	��Ϸ����ͼƬ��Դ	
    public static BufferedImage end;
    static{
    	/**
		 * Java��Ӳ���м���ͼƬ���ڴ��У�
		 * ImageIo.read()������java��ר�Ŵ����Ӳ�̼���ͼƬ���ڴ�ķ�������Ϊ�Ǿ�̬������ֱ�ӵ��ü��ɣ�����Ҫʵ����
		 * �÷�����Ҫ�Ĳ�����             ShootGame.class ��õ�ǰ��ļ�����������·��
		 *					 ShootGame.class.getResource("�ļ���")
		 */
		//IO����������Ҫ�쳣����
    	 try {
			end = ImageIO.read(MyTankGame.class.getResource("gameover.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
   
    //��д���췽��
    public MyPanel(){
        hero = new Hero(600,700);
        //���˵�̹�˳�ʼ��
        for(int i = 0; i <enSize; i++){
        	//�������˵�̹�˶���
            EnemyTank et = new EnemyTank((i+1)*200, 0);
            et.setColor(0);
            et.setDirect(2);
            //��������̹��
            Thread t = new Thread(et);
            t.start();

            //������̹�����һ���ӵ�
            Shot s = new Shot(et.x+10,et.y+30,2);
            et.ss.add(s);
            Thread t2 = new Thread(s);
            t2.start();

            //����
            ets.add(et);
        }
//       try{
//            image1 = ImageIO.read(new File("image1.png"));
//            //image2 = ImageIO.read(new File("image2.png"));
//            //image3 = ImageIO.read(new File("image3.png"));
//
//        }catch(Exception e){
//           // e.printStackTrace();
//        }
        //��ʼ������ͼƬ
       try {
			image1 = ImageIO.read(MyTankGame.class.getResource("bomb_1.gif"));
			image2 = ImageIO.read(MyTankGame.class.getResource("bomb_2.gif"));
			image3 = ImageIO.read(MyTankGame.class.getResource("bomb_3.gif"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }

    /**
	 * .paint()������JPanel��ĸ���JComponent�ķ���
	 * ���в���g �൱�ڻ��ʹ��ߣ����кܶ�  .drawXXX()����
	 * .setVisible()��ʾ�������Զ�����paint()�����������ʵ��Ч����ʾ
	 */
	
	//��дpaint()��������������Ͼ���չʾʲô����
    public void paint(Graphics g){
        super.paint(g);
        BufferedImage myTankImage;
        BufferedImage beijinImage;
     

       //���� g.fillRect(0, 0, 1200, 900);
        try {
            beijinImage = ImageIO.read(MyTankGame.class.getResource("beijin.jpg"));
            g.drawImage(beijinImage, 0, 0, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        g.setFont(new Font("����",1,23));
        g.setColor(Color.RED);
        g.drawString("������"+mount, 0, 20);
        //�����Լ���̹��
        if(hero.isLive==true){
            this.drawTank(hero.getX(), hero.getY(), g, this.hero.direct, 1);
        }
        //�ҷ�̹�˱����ٵ�����������


        //��ss��ȡ��ÿһ���ӵ���������
        for(int i = 0; i <hero.ss.size(); i++){
        	//ȡ���ӵ�
            Shot myShot = hero.ss.get(i);
          //��Ϊ�ӵ���̹�˵ȳ��������֣��޷���ÿһ������ʵ���ػ档�ʴ�������洢�����������������ػ�����Ԫ��
            if(myShot!=null&&myShot.isLive==true){
                //g.draw3DRect(myShot.x, myShot.y, 1, 1, false);
                //�ӵ�����
                g.setColor(Color.cyan);
            	g.drawOval(myShot.x , myShot.y, 10,10);
            }
            if(myShot.isLive==false){
            	//��ss������ɾ�����ӵ�
                hero.ss.remove(myShot);
            }
        }

        //����ը��.��������ֵ������ͬ�̶ȵı�ըͼƬ
        for(int i = 0; i < bombs.size();i++){
            Bomb b = bombs.get(i);
            if(b.life > 6){
                g.drawImage(image1, b.x, b.y,30,30, this);
            }else if(b.life>4){
                g.drawImage(image2, b.x, b.y,30,30, this);
            }else{
                g.drawImage(image3, b.x, b.y,30,30, this);
            }
            //��b������ֵ����
            b.lifeDown();
            //���ը������ֵ==0�߳�ȥ
            if(b.life == 0){
                bombs.remove(b);
            }
        }

        //��������̹��
        for(int i = 0 ; i < ets.size(); i++){
            EnemyTank et = ets.get(i);
            if(et.isLive){
                this.drawTank(et.getX(), et.getY(), g,et.getDirect(), 0);
                //���������ӵ�
                for(int j = 0; j < et.ss.size();j++){
                     Shot enemyShot = et.ss.get(j);
                     if(enemyShot.isLive){
                         //g.draw3DRect(enemyShot.x, enemyShot.y, 1, 1, false);
                         g.setColor(Color.red);
                    	 g.drawOval(enemyShot.x , enemyShot.y, 10,10);
                     }else{
                    	 //����̹������
                         et.ss.remove(enemyShot);
                     }
                }
            }
        }
        
        //���û�ǽ��ķ���
        //g.drawRect(100, 100, 100, 100);
        drawWall(g);
        if(hero.isLive==false){         
        	g.drawImage(end, 0, 0,null);
        	g.setFont(new Font("����",1,23));
        	g.setColor(Color.red);
        	g.drawString("��F1���¿�ʼ", 0, 20);
        }
    }

    //�����ӵ��Ƿ�����ҷ�̹��
    public void hitMe(){
    	//ȡ��ÿһ������̹��
        for(int i = 0; i < this.ets.size(); i++){
        	//ȡ������̹��
            EnemyTank et = ets.get(i);
            if(et.isLive==true){
                for(int j = 0; j < et.ss.size(); j++){
                	//ȡ���ӵ�
                    Shot enemyShot = et.ss.get(j);
                    if(enemyShot.isLive==true){
                        this.hitTank(enemyShot, hero);
                    }                   
                }
            }
        }
    }

    //�ҷ��ӵ��Ƿ���е���̹��
    public void hitEnemyTank(){
    	//�ж��Ƿ���е��˵�̹��
        for(int i = 0; i < hero.ss.size(); i++){
            Shot myShot = hero.ss.get(i);
            //�ж��ӵ��Ƿ���Ч
            if(myShot.isLive==true){
            	//ȡ��ÿһ��̹�������ж�
                for(int j = 0; j < ets.size(); j++){
                    EnemyTank et = ets.get(j);
                    if(et.isLive==true){
                        this.hitTank(myShot,et);
                    }
                }
            }
        }
    }


static int mount=0;
    //дһ������ר���ж��ӵ��Ƿ����̹�˻���ǽ��
    public void hitTank(Shot s, Tank tk){

        switch(tk.direct){
        //���̹�˷������ϻ�������
            case 0:
            case 2:
            	//����̹��
                if(s.x>tk.x&&s.x<tk.x+50&&s.y>tk.y&&s.y<tk.y+68){
                	//�����ӵ�����
                    s.isLive = false;
                    //̹������
                    tk.isLive = false;
                    mount+=1;
                    //����һ��ը��������Vector
                    Bomb b = new Bomb(tk.x, tk.y);
                    bombs.add(b);
                    
                    //new MyTankGame();
                    /*if(tk==hero){
                    	hero.isLive=false;
                    }*/
                    
                }
                //�ж��Ƿ����ǽ��
                if((s.x>250&&s.x<475&&s.y>300&&s.y<350)||(s.x>730&&s.x<950&&s.y>300&&s.y<350)){
                	//�����ӵ�����
                    s.isLive = false;
                    
                    //̹������
                    //et.isLive = false;
                    	
                    //����һ��ը��������Vector
                    Bomb b = new Bomb(s.x, s.y);
                    bombs.add(b);
                }
                    break;
            case 1:
            case 3:
                if(s.x>tk.x&&s.x<tk.x+68&&s.y>tk.y&&s.y<tk.y+50){
                		//��������
                        s.isLive = false;
                        //����̹������
                        tk.isLive = false;
                        mount+=1;
                        //�ӵ�����Ч��
                        Bomb b = new Bomb(tk.x, tk.y);
                        bombs.add(b);
                }
                
                
                if((s.x>250&&s.x<475&&s.y>300&&s.y<350)||(s.x>730&&s.x<950&&s.y>300&&s.y<350)){
                	//�����ӵ�����
                    s.isLive = false;
                    //̹������
                    //et.isLive = false;
                    
                  //�ӵ�����Ч��
                    Bomb b = new Bomb(s.x, s.y);
                    bombs.add(b);

                }
                break;
        } 
    }
//��תͼƬ
    public static BufferedImage rotateImage(final BufferedImage bufferedimage,final int degree) {
        int w = bufferedimage.getWidth();
        int h = bufferedimage.getHeight();
        int type = bufferedimage.getColorModel().getTransparency();
        BufferedImage img;
        Graphics2D graphics2d;
        (graphics2d = (img = new BufferedImage(w, h, type))
                .createGraphics()).setRenderingHint(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2d.rotate(Math.toRadians(degree), w / 2, h / 2);
        graphics2d.drawImage(bufferedimage, 0, 0, null);
        graphics2d.dispose();
        return img;
    }
    //����̹��
    public void drawTank(int x , int y, Graphics g, int direct, int type){
    	//̹������
        BufferedImage Tankimage = null;
        try {
        switch(type){
            case 0:
                g.setColor(Color.cyan);
                    Tankimage = ImageIO.read(MyTankGame.class.getResource("hitTank.png"));;
                break;
            case 1:
                g.setColor(Color.yellow);
                Tankimage = ImageIO.read(MyTankGame.class.getResource("MyTank.png"));;
                break;
        }
        //̹�˷���
        switch(direct){
        //����
        
            case 0:
                g.drawImage(Tankimage, x, y, null);
                break;
            case 1:
            	//����
                g.drawImage(rotateImage(Tankimage, 90), x, y, null);
                break;
            case 2:
                g.drawImage(rotateImage(Tankimage, 180), x, y, null);
                break;
            case 3:
                g.drawImage(rotateImage(Tankimage, 270), x, y, null);
                break;

        }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
   
    //����ǽ��
    public void drawWall(Graphics g){
    	try {
			qianBufferedImage=ImageIO.read(MyTankGame.class.getResource("qiang.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	g.drawImage(qianBufferedImage, 250, 300, null);
    }
    
    //������������w  ��s  ��a ��d
    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub
        if(e.getKeyCode()==KeyEvent.VK_W){
            this.hero.setDirect(0);
            this.hero.moveUP();
        }else if(e.getKeyCode()==KeyEvent.VK_D){
            this.hero.setDirect(1);
            this.hero.moveRight();
        }else if(e.getKeyCode()==KeyEvent.VK_S){
            this.hero.setDirect(2);
            this.hero.moveDown();
        }
        else if(e.getKeyCode()==KeyEvent.VK_A){
            this.hero.setDirect(3);
            this.hero.moveLeft();
        }
        //J������
        if(e.getKeyCode()==KeyEvent.VK_J){ 
            if(this.hero.ss.size()<=4&&this.hero.isLive==true){
                this.hero.shotEnemy();
            }
        }
        if (e.getKeyCode()==KeyEvent.VK_F1) {
			MyTankGame myTankGame=new MyTankGame();
			mount=0;
			this.setVisible(false);
		}

        //���淢���仯�����»���Panel
        this.repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub

    }

    
    public void run(){
        while(true){
            try{
                Thread.sleep(100);
            }
            catch(Exception e){
                e.printStackTrace();
            }
            this.hitEnemyTank();
            //�����жϵ��˵��ӵ��Ƿ�����ҷ�̹��
            this.hitMe();
            this.repaint();
            
        }
        
    }
}
