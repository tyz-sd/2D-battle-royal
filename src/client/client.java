package client;

import java.awt.*;
import map.GameMap;
import map.MiniMap;
import ui.BasicPanel;
import ui.EndPanel;
import ui.PausePanel;

import java.awt.event.*;
import java.util.Random;
import java.util.Vector;

import javax.swing.SwingUtilities;

import java.lang.Math.*;
import java.util.ArrayList;
import java.awt.event.*;
import java.util.Iterator;
import main.*;
import Weapon.*;
import effect.*;
import java.util.Calendar;
import sound.musicStaff;

public class client extends MyFrame{
	static String last_man_standing_filepath = "./resources/sound/lastman.wav";
	static musicStaff last_battle_sound = new musicStaff(last_man_standing_filepath);
	public static GameMap gameMap;
	public static Player me;
	public PausePanel pausePanel;
	public EndPanel endPanel;
	public static int alive;
	public static ArrayList<Weapon> weapon = new ArrayList<Weapon>();
	public static ArrayList<Weapon> bag = new ArrayList<Weapon>();
	public static ArrayList<Medicine> medicine_bag = new ArrayList<Medicine>();
	public static ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	public static ArrayList<Bullet> bullet = new ArrayList<Bullet>();
	public static ArrayList<Armor> armor = new ArrayList<Armor>();
	public static Weapon nearest_weapon = new Weapon();
	public static Bullet nearest_bullet = new Bullet(0,0,-1);
	public static Medicine nearest_medicine = new Medicine(0,0,0);
	public static Armor nearest_armor = new Armor(0,0,0);
	public static Vector<Effect> effectList = new Vector<>();
	final static int block_size = 32;
	boolean init = false;
	public static int ring;
	public static int center_x, center_y;
	public static long start_time;
	public static ArrayList<Player> allPlayers = new ArrayList<Player>();
	long gameEndTimer = -1;
	MiniMap miniMap;
	boolean over;
	public client(String mapName){
		weapon.clear();
		bag.clear();
		bullets.clear();
		bullet.clear();
		nearest_weapon = new Weapon();
		nearest_bullet = new Bullet(0,0,-1);
		nearest_medicine = new Medicine(0,0,0);
		nearest_armor = new Armor(0,0,0);
		effectList = new Vector<>();
		init = false;
		over = false;
		allPlayers.clear();
		alive = 5;
		gameMap = new GameMap(mapName, MyFrame.Window_Width, MyFrame.Window_Height);
		start_time = -1;
		ring = (int)((double)(Math.max(gameMap.getHeight(), gameMap.getWidth())) * 1.1);
		double w = (double) gameMap.getWidth();
		double h = (double) gameMap.getHeight();
		center_x = (int) (w/4 + Math.random()*w/2);
		center_y = (int) (h/4 + Math.random()*h/2);
		
		int x,y;
		do {
			x = (int)(Math.random()*gameMap.getWidth());
			y = (int)(Math.random()*gameMap.getHeight());
		}while(gameMap.getPlayerPassableMap()[x/block_size][y/block_size]!=1);
		me = new Player(x,y,0,gameMap);
		allPlayers.add(me);
		
		for (int i = 1;i<=5;i++) {
			do {
				x = (int)(Math.random()*gameMap.getWidth());
				y = (int)(Math.random()*gameMap.getHeight());
			}while(gameMap.getPlayerPassableMap()[x/block_size][y/block_size]!=1);
			allPlayers.add(new Bot(x,y,i,gameMap));
		}
		gameEndTimer = -1;
		miniMap = new MiniMap(me, gameMap);
	}
	@Override
	public void loadFrame() {
		super.loadFrame();
		//添加键盘监听器，处理键盘按下事件
		this.setFocusable(true); // 加上这一句下边listener才管用
		this.requestFocus();
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				me.keyPressed(e);//委托给me
				client.this.escPressed(e);
			}
			@Override
			public void keyReleased(KeyEvent e) {
				me.keyReleased(e);
			}
		});
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				me.mouseDragged(e);
			}
			@Override
			public void mouseDragged(MouseEvent e) {
				me.mouseDragged(e);
			}
		});
		addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
				me.mousePressed(e);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				me.mouseReleased(e);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}
		});
	}

	void escPressed(KeyEvent e){
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
			SwingUtilities.invokeLater(()->{
				this.setVisible(false);
				pausePanel.setVisible(true);
			});
		}
	}


	/**
	 * 绘制界面
	 */
	@Override
	public void paint(Graphics g) {
		if(!init){
			init_random_weapon(g);
			init_random_bullet(g);
			init_random_medicine(g);
			init_random_armor(g);
			init = true;
		}
		gameMap.render(me.x, me.y, g);
		if (start_time == -1) {
			Calendar calendar = Calendar.getInstance();
			start_time = calendar.getTimeInMillis();
		}
		else {
			Calendar calendar = Calendar.getInstance();
			long cur_time = calendar.getTimeInMillis() - start_time;
			ring = Math.max((int)((double)(Math.max(gameMap.getHeight(), gameMap.getWidth())) * 1.1 - cur_time/20),1500);  
		}
		
		gameMap.drawEntity(new Ring(ring), center_x, center_y, me.x, me.y, g);
		if(me.live){//
			gameMap.drawEntity(me, me.x, me.y, me.x, me.y, g);
		}else{//
			last_battle_sound.stopMusic();
			for (int botn = 1;botn < allPlayers.size();botn++) {
				Bot bot = (Bot)allPlayers.get(botn);
				if(bot.running_sound.myclip.isRunning())
					bot.running_sound.stopMusic();
				if(bot.walking_sound.myclip.isRunning())
					bot.walking_sound.stopMusic();
			}
			if(me.running_sound.myclip.isRunning())
				me.running_sound.stopMusic();
			if(me.walking_sound.myclip.isRunning())
				me.walking_sound.stopMusic();
			over = true;
			int endInterval = 3000;
			if(gameEndTimer < 0) gameEndTimer = Calendar.getInstance().getTimeInMillis();
			else if(Calendar.getInstance().getTimeInMillis() - gameEndTimer > endInterval) {
				SwingUtilities.invokeLater(()->{
					this.setVisible(false);
					endPanel.setVisible(true);
					endPanel.setLose();
				});
			}
			//g.drawImage(fail, (background.getWidth(null)-fail.getWidth(null))/2, (background.getHeight(null)-fail.getHeight(null))/2, null);
		}
		if (alive > 0) {
			for (int botn = 1;botn < allPlayers.size();botn++) {
				Bot bot = (Bot)allPlayers.get(botn);
				if(bot.live) {
					double da=Math.abs(Player.azimuthAngle(me.x, me.y, bot.x, bot.y) - me.direction);
					if(((Player.cnt_distance(bot.x, bot.y, me.x, me.y) < me.v_distance) &&
						((da < me.sight/2) || (da > (2 * Math.PI - me.sight/2))))) {
					bot.draw = true;
				}
				else
					bot.draw = false;
				gameMap.drawEntity(bot, bot.x, bot.y, me.x,me.y, g);
				}
			}
			if(alive == 1) {
				last_battle_sound.playMusic(true);
			}
		}
		else {
			last_battle_sound.stopMusic();
			for (int botn = 1;botn < allPlayers.size();botn++) {
				Bot bot = (Bot)allPlayers.get(botn);
				if(bot.running_sound.myclip.isRunning()) {
					bot.running_sound.stopMusic();
				}
				if(bot.walking_sound.myclip.isRunning())
					bot.walking_sound.stopMusic();
			}
			if(me.running_sound.myclip.isRunning())
				me.running_sound.stopMusic();
			if(me.walking_sound.myclip.isRunning())
				me.walking_sound.stopMusic();
			over = true;
			int endInterval = 3000;
			if(gameEndTimer < 0) gameEndTimer = Calendar.getInstance().getTimeInMillis();
			else if(Calendar.getInstance().getTimeInMillis() - gameEndTimer > endInterval) {
				SwingUtilities.invokeLater(()->{
					this.setVisible(false);
					endPanel.setVisible(true);
					endPanel.setWin();
				});
			}
		}
		
		for (Player p:allPlayers) {
			if (Player.cnt_distance(center_x, center_y, p.x, p.y) > ring) {
				if (p.inRingTime < 10) {
					p.inRingTime++;
				}
				else{
					p.setHp(p.getHp() - 500);
					p.inRingTime = 0;
				}
			}
			else {
				p.inRingTime = 0;
			}
		}
		draw_bag(g);
		drawReload(g);
		draw_random_weapon(g);
		draw_flying_bullet(g);
		drawEffect(g);
		drawKillcount(g);//绘制分数
		drawHealling(g);
		miniMap.draw(MyFrame.Window_Width-180, MyFrame.Window_Height-160, g);
	}
	private void drawReload(Graphics g) {
		for (Player p:allPlayers)
			p.drawReload(g);
	}
	private void drawHealling(Graphics g) {
		me.drawHealling(g);
	}
	/**
	 * 绘制分数
	 * @param g
	 */
	public void draw_random_weapon(Graphics g){
		g.setFont(new Font("Courier New", Font.BOLD, 10));
		double near_pos = 100;
		Weapon tmp_weapon = new Weapon();
		Bullet tmp_bullet = new Bullet(0,0,-1);
		Medicine tmp_medicine = new Medicine(0,0,0);
		Armor tmp_armor = new Armor(0,0,0);
		int Size = bullet.size();
		for(int i = 0 ; i < Size ; i ++) {
			Bullet bullet_i = bullet.get(i);
			double da=Math.abs(Player.azimuthAngle(me.x, me.y, bullet_i.pos_x, bullet_i.pos_y) - me.direction);
			if(((Player.cnt_distance(bullet_i.pos_x, bullet_i.pos_y, me.x, me.y) < me.v_distance) &&
					((da < me.sight/2) || (da > (2 * Math.PI - me.sight/2))))) {
				gameMap.drawEntity(bullet_i, bullet_i.pos_x, bullet_i.pos_y, me.x, me.y, g);
			}
			if((Player.cnt_distance(bullet_i.pos_x, bullet_i.pos_y, me.x, me.y) < near_pos) &&(
					(da < me.sight/2) || (da > (2 * Math.PI - me.sight/2)))) {
				tmp_bullet = bullet_i;
				near_pos =  Player.cnt_distance(bullet_i.pos_x, bullet_i.pos_y, me.x, me.y);
			}
		}
		double nearer_pos = near_pos;
		Size = weapon.size();
		for(int i = 0 ; i < Size ; i++) {
			Weapon weapon_i = weapon.get(i);
			if(weapon_i.Picked) {continue;}
			double da=Math.abs(Player.azimuthAngle(me.x, me.y, weapon_i.pos_x, weapon_i.pos_y) - me.direction);
			if(((Player.cnt_distance(weapon_i.pos_x, weapon_i.pos_y, me.x, me.y) < me.v_distance) &&
					((da < me.sight/2) || (da > (2 * Math.PI - me.sight/2))))) {
				gameMap.drawEntity(weapon_i, weapon_i.pos_x, weapon_i.pos_y, me.x, me.y, g);
			}
			if((Player.cnt_distance(weapon_i.pos_x, weapon_i.pos_y, me.x, me.y) < near_pos) &&(
					(da < me.sight/2) || (da > (2 * Math.PI - me.sight/2)))) {
				tmp_weapon = weapon_i;
				near_pos =  Player.cnt_distance(weapon_i.pos_x, weapon_i.pos_y, me.x, me.y);
			}
		}
		double nearerer_pos = near_pos;
		Size = medicine_bag.size();
		for(int i = 0 ; i < Size ; i++) {
			Medicine medicine_i = medicine_bag.get(i);
			if(medicine_i.Picked) {continue;}
			double da=Math.abs(Player.azimuthAngle(me.x, me.y, medicine_i.pos_x, medicine_i.pos_y) - me.direction);
			if(((Player.cnt_distance(medicine_i.pos_x, medicine_i.pos_y, me.x, me.y) < me.v_distance) &&
					((da < me.sight/2) || (da > (2 * Math.PI - me.sight/2))))) {
				gameMap.drawEntity(medicine_i, medicine_i.pos_x, medicine_i.pos_y, me.x, me.y, g);
			}
			if((Player.cnt_distance(medicine_i.pos_x, medicine_i.pos_y, me.x, me.y) < near_pos) &&(
					(da < me.sight/2) || (da > (2 * Math.PI - me.sight/2)))) {
				tmp_medicine = medicine_i;
				near_pos =  Player.cnt_distance(medicine_i.pos_x, medicine_i.pos_y, me.x, me.y);
			}
		}
		double nearererer_pos = near_pos;
		Size = armor.size();
		for(int i = 0 ; i < Size ; i++) {
			Armor armor_i = armor.get(i);
			if(armor_i.Picked) {continue;}
			double da=Math.abs(Player.azimuthAngle(me.x, me.y, armor_i.pos_x, armor_i.pos_y) - me.direction);
			if(((Player.cnt_distance(armor_i.pos_x, armor_i.pos_y, me.x, me.y) < me.v_distance) &&
					((da < me.sight/2) || (da > (2 * Math.PI - me.sight/2))))) {
				gameMap.drawEntity(armor_i, armor_i.pos_x, armor_i.pos_y, me.x, me.y, g);
			}
			if((Player.cnt_distance(armor_i.pos_x, armor_i.pos_y, me.x, me.y) < near_pos) &&(
					(da < me.sight/2) || (da > (2 * Math.PI - me.sight/2)))) {
				tmp_armor = armor_i;
				near_pos =  Player.cnt_distance(armor_i.pos_x, armor_i.pos_y, me.x, me.y);
			}
		}
		if(near_pos != nearererer_pos) {
			nearest_weapon = null;
			nearest_medicine = null;
			nearest_bullet = null;
			nearest_armor = tmp_armor;
			gameMap.drawString("Press F to pick up.", nearest_armor.pos_x, nearest_armor.pos_y,me.x, me.y, g);
		}
		else if(near_pos != nearerer_pos) {
			nearest_weapon = null;
			nearest_medicine = tmp_medicine;
			nearest_bullet = null;
			nearest_armor = null;
			gameMap.drawString("Press F to pick up.", nearest_medicine.pos_x, nearest_medicine.pos_y,me.x, me.y, g);
		}
		else if(near_pos != nearer_pos) {
			nearest_weapon = tmp_weapon;
			nearest_bullet = null;
			nearest_medicine = null;
			nearest_armor = null;
			gameMap.drawString("Press F to pick up.", nearest_weapon.pos_x, nearest_weapon.pos_y,me.x, me.y, g);
		}
		else if(nearer_pos != 20){
			nearest_bullet = tmp_bullet;
			nearest_weapon = null;
			nearest_medicine = null;
			nearest_armor = null;
			gameMap.drawString("Press F to pick up.", nearest_bullet.pos_x, nearest_bullet.pos_y,me.x, me.y, g);
		}
		else {
			nearest_weapon = null;
			nearest_bullet = null;
			nearest_medicine = null;
			nearest_armor = null;
		}
	}
	public void draw_bag(Graphics g) {
		me.draw_bag(g);
		int Size = me.bag.size();
		if(Size != 0) {
			for(int i = 0; i < Size; i++)
				me.bag.get(i).draw(410+320*i, MyFrame.Window_Height-90, g);
			g.setFont(new Font("consolas", Font.PLAIN, 14));
			for(int i = 0 ; i < Size ; i++)
				g.drawString(me.bag.get(i).name,500+320*i,MyFrame.Window_Height-100);
			for(int i = 0 ; i < Size ; i ++) {
				g.drawString(me.bag.get(i).bullet.name,420+320*i,MyFrame.Window_Height-50);
				switch(me.bag.get(i).bullet.name) {
				case"7.62*54R":
					g.drawString(":"+me.bag.get(i).bullet_num+"/"+me.a762_54r,500+320*i,MyFrame.Window_Height-50);
					break;
				case"7.62*39":
					g.drawString(":"+me.bag.get(i).bullet_num+"/"+me.a762_39,500+320*i,MyFrame.Window_Height-50);
					break;
				case"5.45*39":
					g.drawString(":"+me.bag.get(i).bullet_num+"/"+me.a545_39,500+320*i,MyFrame.Window_Height-50);
					break;
				}
			}	
		}
		Size = me.medicine_bag.size();
		if(Size != 0) {
			for(int i = 0; i < Size; i++)
				me.medicine_bag.get(i).draw(1217, 180+i*50, g);
		}
		Size = me.armor_bag.size();
		if(Size != 0) me.armor_bag.get(0).draw(310, 740, g);
	}
	public void draw_flying_bullet(Graphics g) {
		int Size = bullets.size();
		Iterator<Bullet> iterator = bullets.iterator();
	    while (iterator.hasNext()){
	    	Bullet tmp_bullet = iterator.next();
			int nx = (int) (tmp_bullet.pos_x + tmp_bullet.speed*Math.sin(tmp_bullet.angle));
			int ny = (int) (tmp_bullet.pos_y + tmp_bullet.speed*Math.cos(tmp_bullet.angle));
			if(gameMap.bulletPassable(nx, ny) != 1) {//撞墙
				iterator.remove();
				effectList.add(new HitEffect(nx, ny));
				continue;
			}
			else {//击中bot，显然要改成判断是否击中所有bot以及是否击中玩家
				for (Player i:allPlayers) {
					boolean con = false;
					if (i.no == tmp_bullet.no)
						continue;
					if(hit(i.x, i.y, nx, ny, tmp_bullet.pos_x, tmp_bullet.pos_y) < 30 && Player.cnt_distance(nx, ny, i.x, i.y) < 16) {
						boolean pre_live = i.live;
						if (!over) {
							double protect = 1.0;
							if(i.armor_bag.size() != 0) protect = i.armor_bag.get(0).protect;
							int damage = (int) (((double)tmp_bullet.damage) * protect);
							i.setHp(i.getHp()-damage);
							if(i.armor_bag.size() != 0) i.armor_bag.get(0).durability -= damage/90;
						}
						if(i.armor_bag.size() != 0 && i.armor_bag.get(0).durability <= 0) {
							i.armor_bag.get(0).durability = 0;
							i.armor_bag.get(0).protect = 0.9;
						}
						if(i.getHp() <= 0) { 
							effectList.add(new KillEffect(i.x, i.y));
						}
						if (tmp_bullet.no == 0 && pre_live && !i.live)
								me.killcount++;
						iterator.remove();
						if(i.running_sound.myclip.isRunning())
							i.running_sound.stopMusic();
						if(i.walking_sound.myclip.isRunning())
							i.walking_sound.stopMusic();
						effectList.add(new HitEffect(nx, ny));
						con = true;
						break;
					}
					if (con)
						continue;
				}
			}
			tmp_bullet.pos_x = nx;
			tmp_bullet.pos_y = ny;
			gameMap.drawEntity(tmp_bullet, tmp_bullet.pos_x, tmp_bullet.pos_y, me.x, me.y, g);
	    }
	}
	public void init_random_bullet(Graphics g){
		g.setFont(new Font("Courier New", Font.BOLD, 40));
		g.setColor(Color.BLACK);
		for(int i = 0 ; i < 80 ; i++) {
			int _x, _y;
			do {
				_x = (int) (Math.random() * (gameMap.getHeight()-160)+160);
				_y = (int) (Math.random() * (gameMap.getWidth()-160)+160);
			}while(gameMap.playerPassable(_x, _y) != 1);
			int w_type = (int) (Math.random() * 5);
			Bullet b = new Bullet(0,1,-1);
			b.pos_x = _x;
			b.pos_y = _y;
			if(w_type < 2 ) b.name = "7.62*39";
			else if(w_type < 4 ) b.name = "5.45*39";
			else b.name = "7.62*54R";
			bullet.add(b);
			g.drawRect(_x-3, _y-3, 6, 6);
		}
	}
	public void init_random_weapon(Graphics g){
		g.setFont(new Font("Courier New", Font.BOLD, 40));
		g.setColor(Color.BLACK);
		for(int i = 0 ; i < 40 ; i++) {
			int _x, _y;
			do {
				_x = (int) (Math.random() * (gameMap.getHeight()-160)+160);
				_y = (int) (Math.random() * (gameMap.getWidth()-160)+160);
			}while(gameMap.playerPassable(_x, _y) != 1);
			int w_type = (int) (Math.random() * 7);
			if(w_type<2) weapon.add(new AK(_x, _y));
			else if(w_type<4) weapon.add(new AKS(_x, _y));
			else if(w_type<5) weapon.add(new PKM(_x, _y));
			else if(w_type<6) weapon.add(new SVD(_x, _y));
			else weapon.add(new Mosin(_x, _y));
			g.drawRect(_x-3, _y-3, 6, 6);
		}
	}
	public void init_random_medicine(Graphics g){
		g.setFont(new Font("Courier New", Font.BOLD, 40));
		g.setColor(Color.BLACK);
		for(int i = 0 ; i < 40 ; i++) {
			int _x, _y;
			do {
				_x = (int) (Math.random() * (gameMap.getHeight()-160)+160);
				_y = (int) (Math.random() * (gameMap.getWidth()-160)+160);
			}while(gameMap.playerPassable(_x, _y) != 1);
			int w_type = (int) (Math.random() * 20);
			if(w_type<7) medicine_bag.add(new Medicine(0,_x,_y));
			else if(w_type<15) medicine_bag.add(new Medicine(1,_x,_y));
			else if(w_type<19) medicine_bag.add(new Medicine(2,_x,_y));
			else medicine_bag.add(new Medicine(3,_x,_y));
			g.drawRect(_x-3, _y-3, 6, 6);
		}
	}
	public void init_random_armor(Graphics g){
		g.setFont(new Font("Courier New", Font.BOLD, 40));
		g.setColor(Color.BLACK);
		for(int i = 0 ; i < 20 ; i++) {
			int _x, _y;
			do {
				_x = (int) (Math.random() * (gameMap.getHeight()-160)+160);
				_y = (int) (Math.random() * (gameMap.getWidth()-160)+160);
			}while(gameMap.playerPassable(_x, _y) != 1);
			int w_type = (int) (Math.random() * 7);
			if(w_type < 4 ) armor.add(new Armor(0,_x,_y));
			else if(w_type < 6 ) armor.add(new Armor(1,_x,_y));
			else armor.add(new Armor(2,_x,_y));
			g.drawRect(_x-3, _y-3, 6, 6);
		}
	}
	public void drawKillcount(Graphics g){
		g.setFont(new Font("Courier New", Font.BOLD, 40));
		g.setColor(Color.BLACK);
		g.drawString("Kill:"+me.killcount,MyFrame.Window_Width - 200, 100);
		g.drawString("Enemy:"+alive,MyFrame.Window_Width - 200, 200);
	}
	public double hit(double x_0, double y_0, double x_1, double y_1, double x_2, double y_2) {//其实是在判断x_0,y_0到直线的距离，并且x_0,y_0要在两点中间
		double s_1 = Player.cnt_distance(x_0, y_0, me.x, me.y);
		double s_2 = Player.cnt_distance(x_1, y_1, me.x, me.y);
		if(s_2 <= s_1) return 10;//任意大于10的数
		double a_1 = Player.cnt_distance(x_0, y_0, x_1, y_1);
		double a_2 = Player.cnt_distance(x_0, y_0, x_2, y_2);
		double a_3 = Player.cnt_distance(x_1, y_1, x_2, y_2);
		double p = (a_1 + a_2 + a_3)/2;
		double S = Math.sqrt(p * (p-a_1) * (p-a_2) * (p-a_3));
		return (S*2) / a_3;
	}
	void drawEffect(Graphics g){
		Iterator<Effect> it = effectList.iterator();
		while(it.hasNext()){
			Effect ef = it.next();
			if(ef.expired()) it.remove();
			else gameMap.drawEntity(ef, ef.x, ef.y, me.x, me.y, g);
		}
	}
}
