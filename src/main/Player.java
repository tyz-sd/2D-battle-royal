package main;

import java.awt.event.KeyEvent;
import map.Drawable;
import map.GameMap;
import java.awt.event.MouseEvent;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.concurrent.TimeUnit;
import java.util.Date;

import Weapon.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.time.*;

import client.client;
import sound.musicStaff;

public class Player extends Entity implements Drawable{
	static String shoot_filepath = "./resources/sound/shoot.wav";
	public musicStaff shoot_sound = new musicStaff(shoot_filepath);
	static String get_filepath = "./resources/sound/get.wav";
	public musicStaff get_sound = new musicStaff(get_filepath);
	static String walking_filepath = "./resources/sound/walking.wav";
	public musicStaff walking_sound = new musicStaff(walking_filepath);
	static String running_filepath = "./resources/sound/running.wav";
	public musicStaff running_sound = new musicStaff(running_filepath);
	static String reload_filepath = "./resources/sound/reload.wav";
	public musicStaff reload_sound = new musicStaff(reload_filepath);
	private static BufferedImage[] IMG_SNAKE_HEAD = new BufferedImage[3];
	{
		try {
			IMG_SNAKE_HEAD[0] = ImageIO.read(new File("resources\\soldier2.png"));
			IMG_SNAKE_HEAD[1] = ImageIO.read(new File("resources\\soldier1.png"));
			IMG_SNAKE_HEAD[2] = ImageIO.read(new File("resources\\soldier3.png"));
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public int no;//编号
	public final static int block_size = 32;//地图块大小
	public int speed;//移动速度
	private int hp;//血条
	private int ep;//能量
	public int killcount = 0;//杀敌数
	public int v_distance = 1000;//视距
	public double sight = 1.5;//视野角度(弧度制)
	public boolean fast_moving = false;
	public int cur_weapon = 0;
	public int cur_medicine = 0;
	public int cur_armor = 0;
	public boolean shoot = false;
	public boolean reload = false;
	public boolean healling = false;
	public int x,y;
	public int a545_39 = 0;
	public int a762_39 = 0;
	public int a762_54r = 0;
	public long last_reload_time =0;
	public long last_healling_time = 0;
	public ArrayList<Weapon> bag = new ArrayList<Weapon>();
	public ArrayList<Medicine> medicine_bag = new ArrayList<Medicine>();
	public ArrayList<Armor> armor_bag = new ArrayList<Armor>();
	public int current_Image = 0;
	public int current_Image_count = 0;
	public double direction;
	public static BufferedImage[] newImgSnakeHead = new BufferedImage[3];//旋转后的人物图片
	boolean up, down, left, right;//移动方向
	public int inRingTime = 0;
	GameMap gameMap;
	public Player(int x, int y,int no, GameMap gameMap) {
		this.live = true;
		this.x = x;
		this.y = y;
		this.img = null;//ImageUtil.images.get("snake_body");
		this.width = 30;//img.getWidth(null);
		this.height = 30;//img.getHeight(null);
		this.speed = 3;
		this.hp = 10000;
		this.ep = 0;
		this.gameMap = gameMap;
		this.no = no;
		newImgSnakeHead[0] = IMG_SNAKE_HEAD[0];
		newImgSnakeHead[1] = IMG_SNAKE_HEAD[1];
		newImgSnakeHead[2] = IMG_SNAKE_HEAD[2];
	}

	/**
	 * 接收键盘按下事件
	 * @param e
	 */
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_A:
			left = true;
			break;
		case KeyEvent.VK_S:
			down = true;
			break;
		case KeyEvent.VK_D:
			right = true;
			break;
		case KeyEvent.VK_W:
			up = true;
			break;
		case KeyEvent.VK_1:
			if(bag.size() >= 1) {
				reload = false;
				healling = false;
				cur_weapon = 1;
			}
			break;
		case KeyEvent.VK_2:
			if(bag.size() == 2) {
				reload = false;
				healling = false;
				cur_weapon = 2;
			}
			break;
		case KeyEvent.VK_3:
			if(medicine_bag.size() >= 1) cur_medicine = 1;
			healling = false;
			break;
		case KeyEvent.VK_4:
			if(medicine_bag.size() >= 2) cur_medicine = 2;
			healling = false;
			break;
		case KeyEvent.VK_5:
			if(medicine_bag.size() >= 3) cur_medicine = 3;
			healling = false;
			break;
		case KeyEvent.VK_6:
			if(medicine_bag.size() >= 4) cur_medicine = 4;
			healling = false;
			break;
		case KeyEvent.VK_7:
			if(medicine_bag.size() >= 5) cur_medicine = 5;
			healling = false;
			break;
		case KeyEvent.VK_8:
			if(medicine_bag.size() == 6) cur_medicine = 6;
			healling = false;
			break;
		case KeyEvent.VK_F:
			if(client.nearest_weapon!=null) {
				get_sound.playMusic(false);
				if(bag.size() < 2)
					if(cur_weapon == 0) {
						cur_weapon = 1;
						bag.add(client.nearest_weapon);
						bag.get(0).last_shoot_time=0;
						client.nearest_weapon.Picked = true;
					}
					else {
						bag.add(client.nearest_weapon);
						bag.get(1).last_shoot_time=0;
						client.nearest_weapon.Picked = true;
					}
				else {
					bag.get(cur_weapon-1).Picked = false;
					bag.get(cur_weapon-1).pos_x = this.x;
					bag.get(cur_weapon-1).pos_y = this.y;
					bag.remove(cur_weapon-1);
					bag.add(cur_weapon-1,client.nearest_weapon);
					bag.get(cur_weapon-1).last_shoot_time=0;
					client.nearest_weapon.Picked = true;
				}
			}
			else if(client.nearest_bullet != null) {
				get_sound.playMusic(false);
				if(client.nearest_bullet.name == "7.62*39") {
					a762_39 += 30;
				}
				else if(client.nearest_bullet.name == "5.45*39") {
					a545_39 += 30;
				}
				else if(client.nearest_bullet.name == "7.62*54R") {
					a762_54r += 30;
				}
				client.bullet.remove(client.nearest_bullet);
			}
			else if(client.nearest_medicine != null){
				get_sound.playMusic(false);
				if(medicine_bag.size() < 6)
					if(cur_medicine == 0) {
						cur_medicine = 1;
						medicine_bag.add(client.nearest_medicine);
						client.nearest_medicine.Picked = true;
					}
					else {
						medicine_bag.add(client.nearest_medicine);
						client.nearest_medicine.Picked = true;
					}
				else {
					medicine_bag.get(cur_medicine-1).Picked = false;
					medicine_bag.get(cur_medicine-1).pos_x = this.x;
					medicine_bag.get(cur_medicine-1).pos_y = this.y;
					medicine_bag.remove(cur_medicine-1);
					medicine_bag.add(cur_medicine-1,client.nearest_medicine);
					client.nearest_medicine.Picked = true;
				}
			}
			else if(client.nearest_armor != null) {
				get_sound.playMusic(false);
				if(armor_bag.size() == 0)
					if(cur_armor == 0) {
						cur_armor = 1;
						armor_bag.add(client.nearest_armor);
						client.nearest_armor.Picked = true;
					}
					else {}
				else {
					armor_bag.get(cur_armor-1).Picked = false;
					armor_bag.get(cur_armor-1).pos_x = this.x;
					armor_bag.get(cur_armor-1).pos_y = this.y;
					armor_bag.remove(cur_armor-1);
					armor_bag.add(cur_armor-1,client.nearest_armor);
					client.nearest_armor.Picked = true;
				}
			}
			break;
		case KeyEvent.VK_R:
			reload();
			break;
		case KeyEvent.VK_SHIFT:
			fast_moving = true;
			break;
		case KeyEvent.VK_SPACE:
			healling();
			break;
		}
	}
	/**
	 * 换弹函数（原来的KeyEvent.VK_R直接剪切过来）
	 */
	public void reload() {
		if(bag.size() != 0) {
			if(!reload) {
				reload = true;
				Calendar calendar = Calendar.getInstance();
				last_reload_time = calendar.getTimeInMillis();
			}
		}
	}
	public void healling() {
		if(cur_medicine != 0) {
			if(hp>=7500 && (medicine_bag.get(cur_medicine-1).name == "bandage")) return;
			if(hp>=7500 && (medicine_bag.get(cur_medicine-1).name == "medicine bag")) return;
			if(hp>9999 && (medicine_bag.get(cur_medicine-1).name == "medicine box")) return;
			if(!healling) {
				healling = true;
				Calendar calendar = Calendar.getInstance();
				last_healling_time = calendar.getTimeInMillis();
			}
		}
	}
	
	
	
	/**
	 * 接收键盘松开事件
	 * @param e
	 */
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_A:
			left = false;
			break;
		case KeyEvent.VK_S:
			down = false;
			break;
		case KeyEvent.VK_D:
			right = false;
			break;
		case KeyEvent.VK_W:
			up = false;
			break;
		case KeyEvent.VK_SHIFT:
			fast_moving = false;
		}
	}
	/**
	 * 移动
	 */
	public void move() {
		double cur_speed = ((up ^ down) & (left ^ right)) ? speed/1.4 : speed;
		if(fast_moving){
			sight = 1;
		}
		if(!fast_moving){
			sight = 1.5;
		}
		int nx = x, ny = y;
		if (up)
			ny -= fast_moving?1.5*cur_speed:cur_speed;
		if (down)
			ny += fast_moving?1.5*cur_speed:cur_speed;
		if (left)
			nx -= fast_moving?1.5*cur_speed:cur_speed;
		if (right)
			nx += fast_moving?1.5*cur_speed:cur_speed;
		if ((up||down||left||right) && this.live) {
			if(fast_moving && !running_sound.isPlaying &&(Player.cnt_distance(this.x, this.y, client.me.x, client.me.y) < client.me.v_distance)){
				if(walking_sound.myclip.isRunning())
					walking_sound.stopMusic();
				running_sound.playMusic(false);
			}
			if(!fast_moving && !walking_sound.isPlaying &&(Player.cnt_distance(this.x, this.y, client.me.x, client.me.y) < client.me.v_distance)){
				if(running_sound.myclip.isRunning())
					running_sound.stopMusic();
				walking_sound.playMusic(false);
			}
			if (current_Image == 0) {
				current_Image_count = 0;
				current_Image = 1;
			}
			else {
				if (current_Image_count == 15) {
					current_Image = 3 - current_Image;
					current_Image_count = 0;
				}
				else
					current_Image_count++;
			}
		}
		else {
			if(running_sound.isPlaying)
				running_sound.stopMusic();
			if(walking_sound.isPlaying)
				walking_sound.stopMusic();
			current_Image_count = 0;
			current_Image = 0;
		}
		if(gameMap.playerPassable(x, ny) == 1){
			y = ny;   
		}
		if(gameMap.playerPassable(nx, y) == 1){
			x = nx;
		}
	}
	/*
	 * 转动视角
	 */
	public void mouseDragged(MouseEvent e) {
		int x_= gameMap.screenToWorldX(e.getX(), x); // modified by msj
		int y_= gameMap.screenToWorldY(e.getY(), y);
		this.direction=azimuthAngle(x, y, x_, y_);
	}
	public void mousePressed(MouseEvent e) {
		if(!shoot && !reload && !healling) {
			shoot = true;
		}
	}
	public void mouseReleased(MouseEvent e) {
		shoot = false;
	}
	/**
	 * 绘制
	 */
	public void draw(Graphics g) {}
	public void draw(int x, int y, Graphics g) {
		int x_1 = (int) (x + v_distance*Math.sin(this.direction + sight/2));
		int y_1 = (int) (y + v_distance*Math.cos(this.direction + sight/2));
		int x_2 = (int) (x + v_distance*Math.sin(this.direction - sight/2));
		int y_2 = (int) (y + v_distance*Math.cos(this.direction - sight/2));
		g.setColor(Color.BLACK);
		g.drawLine(x, y, x_1, y_1);
		g.drawLine(x, y, x_2, y_2);
		
		
		double startDegree = (this.direction - sight/2)*180/Math.PI - 90;
		double extentDegree = sight * 180 / Math.PI;
		Graphics2D g2d = (Graphics2D)g;
		/*
        Area outter = new Area(new Rectangle(x - MyFrame.Window_Width/2 - 1, y - MyFrame.Window_Height/2-1,
        		MyFrame.Window_Width + 2, MyFrame.Window_Height+2));*/
        Area outter = new Area(new Rectangle(0, 0, client.gameMap.getWidth(), client.gameMap.getHeight()));
        Arc2D.Double inner = new Arc2D.Double(x-v_distance, y-v_distance, v_distance*2, v_distance*2, startDegree, extentDegree, Arc2D.PIE);
        outter.subtract(new Area(inner));// remove the ellipse from the original area
        g2d.setColor(new Color(0,0,0,50));
        g2d.fill(outter);
		
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		GraphicsConfiguration gc = gd.getDefaultConfiguration();
		BufferedImage rotateImg = create(newImgSnakeHead[current_Image],Math.PI-this.direction,gc);
		int rotate_height = rotateImg.getHeight()/5;
		int rotate_width = rotateImg.getWidth()/5;
		g.drawImage(rotateImg, x-rotate_width/2, y-rotate_height/2, rotate_width,rotate_height,null);//绘制
		move();//移动
		shoot();
		drawReload(g);
		ep = Math.max(0, ep-1);
		if(ep>0) hp = Math.min(10000, hp+1);
	}
	/**
	 * 处理出界问题
	 */
	public void shoot() {
		Calendar calendar = Calendar.getInstance();
		long cur_time = calendar.getTimeInMillis();
		if(shoot) {
			if(cur_weapon != 0 && bag.get(cur_weapon-1).bullet_num != 0) {
				if(((long)(bag.get(cur_weapon-1).frate) < Math.abs(cur_time - bag.get(cur_weapon-1).last_shoot_time))) {
					shoot_sound.playMusic(false);
					Bullet tmp_bullet = bag.get(cur_weapon-1).bullet;
					Bullet b = new Bullet(tmp_bullet.damage,tmp_bullet.speed,this.no);
					b.type = true;
					b.pos_x = x;
					b.pos_y = y;
					b.angle = this.direction;
					client.bullets.add(b);
					bag.get(cur_weapon-1).bullet_num -= 1;
					bag.get(cur_weapon-1).last_shoot_time = cur_time;
				}
				else {
					return;
				}
			}
		}
	}
	private void outOfBounds() {
		if (x <= 32)
			x = 32;
		if (x >= (MyFrame.Window_Width - 32))
			x = MyFrame.Window_Width - 32;
		if (y <= 32)
			y = 32;
		if (y >= (MyFrame.Window_Height - 32))
			y = MyFrame.Window_Height - 32;
	}
	public void draw_bag(Graphics g) {
		int x1 = 320, x2 = 640, y = 680, width = 320, height = 100;
		int y1 = 780;
		double hp_percent = Math.max(((double)hp)/10000, 0);
		double x3 = hp_percent * 640.0;
		double ep_percent = ((double)ep)/10000;
		double x4 = ep_percent * 640.0; 
		if(ep_percent > 0.2) {speed = 4;}
		else {speed = 3;}
		g.setColor(Color.BLACK);
		g.drawRect(x1, y, width, height);
		g.drawRect(x2, y, width, height);
		if(cur_weapon != 0) {
			if(cur_weapon == 1) {
				g.setColor(Color.green);
				g.fillRect(x1, y, width, height);
				g.setColor(Color.black);
				g.drawRect(x1, y, width, height);//替换成枪的图片
			}
			else {
				g.setColor(Color.green);
				g.fillRect(x2, y, width, height);
				g.setColor(Color.black);
				g.drawRect(x2, y, width, height);//替换成枪的图片	
			}
		}
		g.drawRect(x1,y1+10,2*width,10);
		g.drawRect(x1,y1,2*width,10);
		g.setColor(Color.BLACK);
		g.drawRect(x1-height, y, height, height);
		if(cur_armor != 0) {
			g.setColor(Color.green);
			g.fillRect(x1-height, y, height, height);
			g.setColor(Color.black);
			g.drawRect(x1-height, y, height, height);
		}
		if(hp_percent > 0.3)
			g.setColor(Color.green);
		else
			g.setColor(Color.red);
		g.fillRect(x1,y1,(int)x3,10);
		g.setColor(Color.blue);
		g.fillRect(x1,y1+10,(int)x4,10);
		g.setColor(Color.black);
		int x = 1150;
		int y_[] = {150,200,250,300,350,400};
		for(int i=0;i<6;i++) g.drawRect(x, y_[i], 50, 50);
		if(cur_medicine != 0) {
				g.setColor(Color.green);
				g.fillRect(x, y_[cur_medicine-1], 50, 50);
				g.setColor(Color.black);
				g.drawRect(x, y_[cur_medicine-1], 50, 50);
		}
	}
	public void drawReload(Graphics g) {
		if(cur_weapon == 0) return;
		if(reload == false) return;
		Weapon cur = bag.get(cur_weapon-1);
		if(cur.bullet_num == cur.max_bullet) return;
		Calendar calendar = Calendar.getInstance();
		long cur_time = calendar.getTimeInMillis();
		long delta_time = Math.abs(cur_time - last_reload_time);
		if(delta_time > cur.reloadTime) {
			switch(cur.bullet.name) {
			case"7.62*54R":
				if(a762_54r >= (cur.max_bullet - cur.bullet_num)) {
					a762_54r -= (cur.max_bullet - cur.bullet_num);
					cur.bullet_num = cur.max_bullet;
				}
				else {
					cur.bullet_num += a762_54r;
					a762_54r = 0;
				}
				break;
			case"7.62*39":
				if(a762_39 >= (cur.max_bullet - cur.bullet_num)) {
					a762_39 -= (cur.max_bullet - cur.bullet_num);
					cur.bullet_num = cur.max_bullet;
				}
				else {
					cur.bullet_num += a762_39;
					a762_39 = 0;
				}
				break;
			case"5.45*39":
				if(a545_39 >= (cur.max_bullet - cur.bullet_num)) {
					a545_39 -= (cur.max_bullet - cur.bullet_num);
					cur.bullet_num = cur.max_bullet;
				}
				else {
					cur.bullet_num += a545_39;
					a545_39 = 0;
				}
				break;
			}
			reload = false;
		}
		else {
			int x1 = 320, x2 = 640, y = 680, height = 100;
			int width = (int) (((double)delta_time/(double)cur.reloadTime)*320);
			switch(cur.bullet.name) {
			case"7.62*54R":
				if(a762_54r == 0) return;
				break;
			case"7.62*39":
				if(a762_39 == 0) return;
				break;
			case"5.45*39":
				if(a545_39 == 0) return;
				break;
			}
			g.setColor(Color.BLACK);
			if(no == 0)
			{
				if(cur_weapon == 1) g.drawRect(x1, y, width, height);
				if(cur_weapon == 2) g.drawRect(x2, y, width, height);
				reload_sound.playMusic(false);
			}

		}
	}
	public void drawHealling(Graphics g) {
		if(cur_medicine == 0) return;
		if(healling == false) return;
		Medicine cur = medicine_bag.get(cur_medicine-1);
		Calendar calendar = Calendar.getInstance();
		long cur_time = calendar.getTimeInMillis();
		long delta_time = Math.abs(cur_time - last_healling_time);
		if(delta_time > cur.use_time) {
			int heal = 0;
			if(cur.name == "medicine box") heal = 10000;
			else heal = Math.min(cur.healing_point, Math.abs(hp - 7500));
			this.hp = Math.min(10000, hp + heal);
			this.ep = Math.min(10000, ep + cur.power_point);
			healling = false;
			medicine_bag.remove(cur_medicine-1);
			if(medicine_bag.size() == 0) cur_medicine = 0;
			else cur_medicine = 1;
			speed = 3;
		}
		else {
			int x = 1150, y = 150 + (cur_medicine-1)*50, height = 50;
			int width = (int) (((double)delta_time/(double)cur.use_time)*50);
			g.setColor(Color.BLACK);
			if(no == 0){
				g.drawRect(x, y, width, height);
			}
			speed = 2;
		}
	}
	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
		if (hp <= 0 && this.live) {
			if(running_sound.isPlaying)
				running_sound.stopMusic();
			if(walking_sound.isPlaying)
				walking_sound.stopMusic();
			this.live = false;
			if (no != 0)
				client.alive--;
		}
	}

	public int getEp() {
		return ep;
	}

	public void setEp(int ep) {
		this.ep = ep;
	}
	/*
	 * 计算方位角的
	 */
	public static double azimuthAngle(double x1, double y1, double x2, double y2) {
        double dx, dy, angle = 0;
        dx = x2 - x1;
        dy = y2 - y1;
        if (x2 == x1) {
            angle = Math.PI / 2.0;
            if (y2 >= y1) {
                angle = 0.0;
            } else if (y2 < y1) {
                angle = Math.PI;
            }
        } else if (y2 == y1) {
        	if (x2 >= x1) {
        		angle = Math.PI / 2.0;
        	} else if (x2 < x1) {
        		angle = 3 * Math.PI / 2.0;
        	}
        } else if ((x2 > x1) && (y2 > y1)) {
            angle = Math.atan(dx / dy);
        } else if ((x2 > x1) && (y2 < y1)) {
            angle = Math.PI / 2 + Math.atan(-dy / dx);
        } else if ((x2 < x1) && (y2 < y1)) {
            angle = Math.PI + Math.atan(dx / dy);
        } else if ((x2 < x1) && (y2 > y1)) {
            angle = 3.0 * Math.PI / 2.0 + Math.atan(dy / -dx);
        }
        return angle;
    }
    public static double cnt_distance(double x, double y, double _x, double _y){
		return Math.sqrt((x-_x)*(x-_x) + (y-_y)*(y-_y));
	}
    
    //旋转图片
    public static BufferedImage create(BufferedImage image, double angle, GraphicsConfiguration gc) {
    	double sin = Math.abs(Math.sin(angle)), cos = Math.abs(Math.cos(angle));
    	int w = image.getWidth(), h = image.getHeight();
    	int neww = (int) Math.floor(w * cos + h * sin);
    	int newh = (int) Math.floor(h* cos + w * sin);
    	int transparency = image.getColorModel().getTransparency();
    	BufferedImage result = gc.createCompatibleImage(neww, newh, transparency);
    	Graphics2D g = result.createGraphics();
    	g.translate((neww - w) / 2, (newh - h) / 2);
    	g.rotate(angle, w / 2, h / 2);
    	g.drawRenderedImage(image, null);
    	return result;
    }


}
