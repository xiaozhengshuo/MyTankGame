package tank;

import java.util.*;

class Bomb{
	//定义炸弹坐标
    int x, y;
    //炸弹的生命周期
    int life = 9;
    boolean isLive = true;
    public Bomb(int x, int y){
        this.x = x;
        this.y = y;
    }

    //减少生命
    public void  lifeDown(){
        if(life > 0){
            life--;
        }
        else{
           this.isLive = false;
        }
    }

}


class Shot implements Runnable{
    int x;
    int y;
    int direct;
    int speed = 5;
    boolean isLive = true;
    public Shot(int x, int y, int direct){
        this.x = x;
        this.y = y;
        this.direct = direct;
    }
    
    public void run(){
        while(true){
            try{
                Thread.sleep(50);
            }catch(Exception e){
                e.printStackTrace();
            }
            switch(direct){
                case 0:
                    y-=speed;
                    break;
                case 1:
                    x+=speed;
                    break;
                case 2:
                    y+=speed;
                    break;
                case 3:
                    x-=speed;
                    break;
            }
            //判断子弹是否碰到边缘
            if(x<0||x>1200||y<0||y>900){
                this.isLive = false;
                break;
            }
        }   
   }
}



class Tank{
	//坦克横坐标
    int x = 0;
    //坦克纵坐标
    int y = 0;
    //坦克方向  
    //0 表示上，1表示右，2 表示下，3 表示左
    int direct = 0;
    //坦克速度
    int speed = 3;
    //坦克颜色
    int Color;
    boolean isLive = true;

    public int getColor(){
        return Color;
    }
    public void setColor(int color){
        Color = color;
    }
    public int getSpeed(){
        return speed;
    }
    public void setSpeed(int speed){
        this.speed = speed;
    }
    public int getDirect(){
        return direct;
    }
    public void setDirect(int direct){
        this.direct = direct;
    }
    public Tank(int x, int y){
        this.x = x; 
        this.y = y;
    }
    public int getX(){
        return x;
    }

    public void setX(int x){
        this.x = x;
    }

    public int getY(){
        return y;
    }

    public void setY(int y){
        this.y = y;
    }



}

//敌人的坦克
class EnemyTank extends Tank implements Runnable{   
    int times = 0;
    //定义一个向量，可以存放敌人的子弹
    Vector<Shot>ss = new Vector<Shot>();
    //敌人添加子弹应该在刚刚创建坦克和敌人的坦克子弹死亡之后

    public EnemyTank(int x, int y){
        super(x, y); 
    }
    @Override
    public void run(){
        // TODO Auto-generated method stub
        while(true){
                switch(this.direct){
                case 0:
                	//说明坦克正在向上走
                    for(int i = 0; i < 30; i++){
                    	//敌人坦克在我的范围内移动
                        if(y>0){
                            y-=speed;
                        }
                        try{
                            Thread.sleep(50);
                        } catch (InterruptedException e){
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    break;
                case 1:
                    for(int i = 0; i < 30; i++){
                        if(x<1000){
                            x+=speed;
                        }
                        try{
                            Thread.sleep(50);
                        } catch (InterruptedException e){
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    break;
                case 2:
                    for(int i = 0; i < 30; i++){
                        if(y<800){
                            y+=speed;
                        }
                        try{
                            Thread.sleep(50);
                        } catch (InterruptedException e){
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }               
                    break;
                case 3:
                    for(int i = 0; i < 30; i++){
                        if(x > 0){
                            x-=speed;
                        }
                        try{
                            Thread.sleep(50);
                        } catch (InterruptedException e){
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }       
                    break;
                }
                //              判断是否需要给坦克加入新的子弹
                this.times++;
                if(times%2==0){
                   if(isLive){
                	   if(ss.size()<5){
                          Shot s =null;
                          switch(direct){
                          	case 0:
                          	//创建一颗子弹
                          	s = new Shot(x+10, y, 0);
                          	//把子弹加入到向量
                          	ss.add(s);
                          	break;
                          	case 1:s = new Shot(x+30, y+10, 1);
                          	ss.add(s);break;
                            case 2:s = new Shot(x+10, y+30, 2);
                            ss.add(s);
                            break;
                            case 3:s = new Shot(x, y+10, 3);
                            ss.add(s);
                            break;
                          	}
                        //启动子弹线程
                        Thread t = new Thread(s);
                        t.start();
                            }
                        }
                }   

                //让坦克随机产生一个新的方向
                this.direct = (int)(Math.random()*4);
                //判断敌人坦克是否死亡
                if(this.isLive == false){
                	//让坦克死亡后，退出进程
                    break;
                }           
        }
    }

}


//我的坦克
class Hero extends Tank{
	//Vector可以自动增长的对象数组
    Vector<Shot> ss = new Vector<Shot>();
    Shot s = null;
    public Hero(int x, int y){
        super(x, y);
    }

    //开火,子弹发射位置
    public void shotEnemy(){
    	switch(this.direct){
        case 0:
        	//创建一颗子弹
            s = new Shot(x+20, y, 0);
            //把子弹加入到向量
            ss.add(s);
            break;
        case 1:
            s = new Shot(x+34, y+27, 1);
            ss.add(s);
            break;
        case 2:
            s = new Shot(x+20, y+68, 2);
            ss.add(s);
            break;
        case 3:
            s = new Shot(x, y+27, 3);
            ss.add(s);
            break;
        }
        Thread t = new Thread(s);
        t.start();
    }

    //坦克向上移动
    public void moveUP(){
        y-=speed;
    }
    //坦克向右移动
    public void moveRight(){
        x+=speed;
    }
    public void moveDown(){
        y+=speed;
    }
    public void moveLeft(){
        x-=speed;
    }
}
