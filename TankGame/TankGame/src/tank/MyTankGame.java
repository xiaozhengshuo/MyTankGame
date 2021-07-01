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
 * 功能：坦克大战2.0
 * 1: 画出坦克
 * 2：我方坦克可以上下移动
 * 3: 画出敌人坦克
 * 4: 我方坦克可以发子弹 
 * 5：子弹可以连发(最多可以连发五颗)
 * 6: 当我的坦克击中敌人坦克时候，敌人爆炸、消失
 *    （判断子弹是否击中坦克；什么时候调用）
 *    （爆炸：1、先准备三张图；2、定义Bomb类；3、在击中敌人坦克时放入炸弹Vector 4、绘制）
 * 7: 敌人坦克在规定范围移动
 * 8：敌人坦克也能发子弹
 * 9: 当敌人坦克击中我方坦克，我方坦克消失
 * 10：当敌我双方发射子弹击中墙体部分，子弹会消失
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
        //注册监听
        this.addKeyListener(mp);
        this.setSize(1200, 900);
        this.setTitle("坦克大战-tarena");
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
}
class MyPanel extends JPanel implements KeyListener,Runnable{
	   static BufferedImage qianBufferedImage;
	//创建数组存储敌方坦克、子弹等，并创建方法批量重绘数组元素。传参g，作为画笔存在
	
	//定义一个我方坦克
    Hero hero = null;

    //定义敌人的坦克
    Vector<EnemyTank> ets = new Vector<EnemyTank>();

    //定义一个炸弹的集合
    Vector<Bomb>  bombs = new Vector<Bomb>();

    //敌人坦克多少
    int enSize = 4;

    //定义三张图片的图片的切换，才能组成一颗炸弹
    Image image1 = null;
    Image image2 = null;
    Image image3 = null;
    
    //静态块:仅在类第一次被加载到方法区时被执行一次，专门用来加载静态资源。	游戏结束图片资源	
    public static BufferedImage end;
    static{
    	/**
		 * Java从硬盘中加载图片到内存中：
		 * ImageIo.read()方法：java中专门处理从硬盘加载图片到内存的方法。因为是静态方法，直接调用即可，不需要实例化
		 * 该方法需要的参数：             ShootGame.class 获得当前类的加载器的所在路径
		 *					 ShootGame.class.getResource("文件名")
		 */
		//IO流几乎都需要异常处理
    	 try {
			end = ImageIO.read(MyTankGame.class.getResource("gameover.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
   
    //重写构造方法
    public MyPanel(){
        hero = new Hero(600,700);
        //敌人的坦克初始化
        for(int i = 0; i <enSize; i++){
        	//创建敌人的坦克对象
            EnemyTank et = new EnemyTank((i+1)*200, 0);
            et.setColor(0);
            et.setDirect(2);
            //启动敌人坦克
            Thread t = new Thread(et);
            t.start();

            //给敌人坦克添加一颗子弹
            Shot s = new Shot(et.x+10,et.y+30,2);
            et.ss.add(s);
            Thread t2 = new Thread(s);
            t2.start();

            //加入
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
        //初始化三张图片
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
	 * .paint()方法是JPanel类的父类JComponent的方法
	 * 含有参数g 相当于画笔工具，含有很多  .drawXXX()方法
	 * .setVisible()显示方法会自动调用paint()方法，以完成实现效果显示
	 */
	
	//重写paint()方法来定义面板上具体展示什么内容
    public void paint(Graphics g){
        super.paint(g);
        BufferedImage myTankImage;
        BufferedImage beijinImage;
     

       //背景 g.fillRect(0, 0, 1200, 900);
        try {
            beijinImage = ImageIO.read(MyTankGame.class.getResource("beijin.jpg"));
            g.drawImage(beijinImage, 0, 0, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        g.setFont(new Font("仿宋",1,23));
        g.setColor(Color.RED);
        g.drawString("分数："+mount, 0, 20);
        //画出自己的坦克
        if(hero.isLive==true){
            this.drawTank(hero.getX(), hero.getY(), g, this.hero.direct, 1);
        }
        //我方坦克被击毁弹出结束画面


        //从ss中取出每一颗子弹，并绘制
        for(int i = 0; i <hero.ss.size(); i++){
        	//取出子弹
            Shot myShot = hero.ss.get(i);
          //因为子弹、坦克等成批量出现，无法对每一个对象实现重绘。故创建数组存储，并创建方法批量重绘数组元素
            if(myShot!=null&&myShot.isLive==true){
                //g.draw3DRect(myShot.x, myShot.y, 1, 1, false);
                //子弹设置
                g.setColor(Color.cyan);
            	g.drawOval(myShot.x , myShot.y, 10,10);
            }
            if(myShot.isLive==false){
            	//从ss向量中删除该子弹
                hero.ss.remove(myShot);
            }
        }

        //画出炸弹.根据生命值更换不同程度的爆炸图片
        for(int i = 0; i < bombs.size();i++){
            Bomb b = bombs.get(i);
            if(b.life > 6){
                g.drawImage(image1, b.x, b.y,30,30, this);
            }else if(b.life>4){
                g.drawImage(image2, b.x, b.y,30,30, this);
            }else{
                g.drawImage(image3, b.x, b.y,30,30, this);
            }
            //让b的生命值减少
            b.lifeDown();
            //如果炸弹生命值==0踢出去
            if(b.life == 0){
                bombs.remove(b);
            }
        }

        //画出敌人坦克
        for(int i = 0 ; i < ets.size(); i++){
            EnemyTank et = ets.get(i);
            if(et.isLive){
                this.drawTank(et.getX(), et.getY(), g,et.getDirect(), 0);
                //画出敌人子弹
                for(int j = 0; j < et.ss.size();j++){
                     Shot enemyShot = et.ss.get(j);
                     if(enemyShot.isLive){
                         //g.draw3DRect(enemyShot.x, enemyShot.y, 1, 1, false);
                         g.setColor(Color.red);
                    	 g.drawOval(enemyShot.x , enemyShot.y, 10,10);
                     }else{
                    	 //敌人坦克死亡
                         et.ss.remove(enemyShot);
                     }
                }
            }
        }
        
        //调用画墙体的方法
        //g.drawRect(100, 100, 100, 100);
        drawWall(g);
        if(hero.isLive==false){         
        	g.drawImage(end, 0, 0,null);
        	g.setFont(new Font("宋体",1,23));
        	g.setColor(Color.red);
        	g.drawString("按F1重新开始", 0, 20);
        }
    }

    //敌人子弹是否击中我方坦克
    public void hitMe(){
    	//取出每一个敌人坦克
        for(int i = 0; i < this.ets.size(); i++){
        	//取出敌人坦克
            EnemyTank et = ets.get(i);
            if(et.isLive==true){
                for(int j = 0; j < et.ss.size(); j++){
                	//取出子弹
                    Shot enemyShot = et.ss.get(j);
                    if(enemyShot.isLive==true){
                        this.hitTank(enemyShot, hero);
                    }                   
                }
            }
        }
    }

    //我方子弹是否击中敌人坦克
    public void hitEnemyTank(){
    	//判断是否击中敌人的坦克
        for(int i = 0; i < hero.ss.size(); i++){
            Shot myShot = hero.ss.get(i);
            //判断子弹是否有效
            if(myShot.isLive==true){
            	//取出每一个坦克与它判断
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
    //写一个函数专门判断子弹是否击中坦克或者墙体
    public void hitTank(Shot s, Tank tk){

        switch(tk.direct){
        //如果坦克方向是上或者是下
            case 0:
            case 2:
            	//击中坦克
                if(s.x>tk.x&&s.x<tk.x+50&&s.y>tk.y&&s.y<tk.y+68){
                	//击中子弹死亡
                    s.isLive = false;
                    //坦克死亡
                    tk.isLive = false;
                    mount+=1;
                    //创建一颗炸弹，放入Vector
                    Bomb b = new Bomb(tk.x, tk.y);
                    bombs.add(b);
                    
                    //new MyTankGame();
                    /*if(tk==hero){
                    	hero.isLive=false;
                    }*/
                    
                }
                //判断是否击中墙体
                if((s.x>250&&s.x<475&&s.y>300&&s.y<350)||(s.x>730&&s.x<950&&s.y>300&&s.y<350)){
                	//击中子弹死亡
                    s.isLive = false;
                    
                    //坦克死亡
                    //et.isLive = false;
                    	
                    //创建一颗炸弹，放入Vector
                    Bomb b = new Bomb(s.x, s.y);
                    bombs.add(b);
                }
                    break;
            case 1:
            case 3:
                if(s.x>tk.x&&s.x<tk.x+68&&s.y>tk.y&&s.y<tk.y+50){
                		//击中死亡
                        s.isLive = false;
                        //敌人坦克死亡
                        tk.isLive = false;
                        mount+=1;
                        //子弹击中效果
                        Bomb b = new Bomb(tk.x, tk.y);
                        bombs.add(b);
                }
                
                
                if((s.x>250&&s.x<475&&s.y>300&&s.y<350)||(s.x>730&&s.x<950&&s.y>300&&s.y<350)){
                	//击中子弹死亡
                    s.isLive = false;
                    //坦克死亡
                    //et.isLive = false;
                    
                  //子弹击中效果
                    Bomb b = new Bomb(s.x, s.y);
                    bombs.add(b);

                }
                break;
        } 
    }
//旋转图片
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
    //画出坦克
    public void drawTank(int x , int y, Graphics g, int direct, int type){
    	//坦克类型
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
        //坦克方向
        switch(direct){
        //向上
        
            case 0:
                g.drawImage(Tankimage, x, y, null);
                break;
            case 1:
            	//向右
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
    
   
    //画出墙体
    public void drawWall(Graphics g){
    	try {
			qianBufferedImage=ImageIO.read(MyTankGame.class.getResource("qiang.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	g.drawImage(qianBufferedImage, 250, 300, null);
    }
    
    //按键监听，上w  下s  左a 右d
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
        //J键开火
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

        //界面发生变化则重新绘制Panel
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
            //函数判断敌人的子弹是否击中我方坦克
            this.hitMe();
            this.repaint();
            
        }
        
    }
}
