package main;

import java.awt.Graphics;
import map.GameMap;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.*;

import AStar.*;
import Weapon.Bullet;
import client.client;

public class Bot extends Player{
	private int des_x;
	private int des_y;	//当前正在前往的目标点
	private LinkedList<Point> path;
	public AStar astar = new AStar(this.gameMap);
	boolean run = false;
	public boolean draw = true;
	public Bot(int x,int y,int no,GameMap gameMap) {
		super(x,y,no,gameMap);
		des_x = x;
		des_y = y;		
	}
	@Override
	public void keyPressed(KeyEvent e) {}
	@Override
	public void keyReleased(KeyEvent e) {}
	@Override
	public void mouseDragged(MouseEvent e) {}
	@Override
	public void draw(Graphics g) {}
	@Override
	public void draw(int x,int y,Graphics g) {
		moveTo(des_x,des_y);
		move();//移动
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		GraphicsConfiguration gc = gd.getDefaultConfiguration();
		BufferedImage rotateImg = create(newImgSnakeHead[current_Image],Math.PI-this.direction,gc);
		int rotate_height = rotateImg.getHeight()/5;
		int rotate_width = rotateImg.getWidth()/5;
		
		if (draw)
			g.drawImage(rotateImg, x-rotate_width/2, y-rotate_height/2, rotate_width,rotate_height,null);//绘制
		
		set_direction();
	}
	
	public Player nearestPlayer() {
		double min_dis = 10000000;
		Player p = null;
		for(Player i:client.allPlayers) {
			if (i.no == this.no)
				continue;
			double dis = Math.sqrt((double)((this.x - i.x)*(this.x - i.x) 
					+ (this.y - i.y)*(this.y -  i.y)));
			//System.out.println(i.name + " " + dis);
			if ( dis < min_dis && i.live) {
				min_dis = dis;
				p = i;
			}
		}
		return p;
	}
	
	
	
	public Weapon notice_weapon(double dist) {
		double min_dis = dist;
		Weapon p = null;
		for(Weapon i:client.weapon) {
			double dis = Math.sqrt((double)((this.x - i.pos_x)*(this.x - i.pos_x) 
					+ (this.y - i.pos_y)*(this.y -  i.pos_y)));
			//System.out.println(i.name + " " + dis);
			if ( dis < min_dis &&  !i.Picked) {
				min_dis = dis;
				p = i;
			}
		}
		return p;
	}
	
	public Bullet notice_bullet(double dist) {
		double min_dis = dist;
		Bullet p = null;
		for(Bullet i:client.bullet) {
			double dis = Math.sqrt((double)((this.x - i.pos_x)*(this.x - i.pos_x) 
					+ (this.y - i.pos_y)*(this.y -  i.pos_y)));
			//System.out.println(i.name + " " + dis);
			if ( dis < min_dis ) {
				min_dis = dis;
				p = i;
			}
		}
		return p;
	}
	
	public Medicine notice_medicine(double dist) {
		double min_dis = dist;
		Medicine p = null;
		for(Medicine i:client.medicine_bag) {
			double dis = Math.sqrt((double)((this.x - i.pos_x)*(this.x - i.pos_x) 
					+ (this.y - i.pos_y)*(this.y -  i.pos_y)));

			if ( dis < min_dis ) {
				min_dis = dis;
				p = i;
			}
		}
		return p;
	}
	
	public Armor notice_armor(double dist) {
		double min_dis = dist;
		int dur = armor_bag.size()==0?0:armor_bag.get(0).durability;
		Armor p = null;
		for(Armor i:client.armor) {
			double dis = Math.sqrt((double)((this.x - i.pos_x)*(this.x - i.pos_x) 
					+ (this.y - i.pos_y)*(this.y -  i.pos_y)));

			if ( dis < min_dis && i.durability > dur) {
				dur = i.durability;
				min_dis = dis;
				p = i;
			}
		}
		return p;
	}
	
	private void moveWhileFighting() {
		int bin = (int)(this.direction/Math.PI*4);
		this.up = false;
		this.down = false;
		this.right = false;
		this.left = false;
		switch(bin) {
		case 0:right = true;break;
		case 1:down = true;left = true;break;
		case 2:up = true;break;
		case 3:up = true;left = true;break;
		case 4:left = true;
		case 5:up = true;right = true;
		case 6:down = true;break;
		case 7:down = true;right = true;break;
		default:break;
		}
		super.move();
	}
	
	private double shoot_angle(Player player) {
		double dist = Player.cnt_distance(player.x, player.y, this.x, this.y);
		int next_x = player.x,next_y = player.y;
		double cur_speed = ((player.up ^ player.down) & (player.left ^ player.right)) ? player.speed/1.4 : player.speed;
		double bullet_speed = this.bag.get(cur_weapon-1).bullet.speed;
		if (player.fast_moving) cur_speed *= 1.5;
		if (player.up^player.down) {
			if (player.up)
				next_y -= dist/bullet_speed*cur_speed;
			else
				next_y += dist/bullet_speed*cur_speed;
		}
		if (player.left^player.right) {
			if (player.left)
				next_x -= dist/bullet_speed*cur_speed;
			else
				next_x += dist/bullet_speed*cur_speed;
		}
		return Player.azimuthAngle(this.x, this.y, next_x, next_y);
	}
	
	@Override
	public void move() {
		shoot = false;
		Player player = nearestPlayer();
		if (player == null)
			return;
		if (run)
			fast_moving = true;
		else
			fast_moving = false;
		Bullet b = notice_bullet(2*block_size);
		if (b != null) {
			if(b.name == "7.62*39") {
				a762_39 += 30;
			}
			else if(b.name == "5.45*39") {
				a545_39 += 30;
			}
			else if(b.name == "7.62*54R") {
				a762_54r += 30;
			}
			client.bullet.remove(b);
		}
		if (medicine_bag.size()<6) {
			Medicine m = notice_medicine(2*block_size);
			if (m != null) {
				if(cur_medicine == 0) {
					cur_medicine = 1;
					medicine_bag.add(m);
					m.Picked = true;
				}
				else {
					medicine_bag.add(m);
					m.Picked = true;
				}
			}
		}
		if (bag.size()<2) {
			Weapon w = this.notice_weapon(2*block_size);
			if (w != null) {
				if(cur_weapon == 0) {
					cur_weapon = 1;
					bag.add(w);
					w.Picked = true;
				}
				else {
					bag.add(w);
					w.Picked = true;
				}
			}
		}
		
		Armor a = this.notice_armor(2*block_size);
		if (a != null) {
			if(armor_bag.size() == 0)
				if(cur_armor == 0) {
					cur_armor = 1;
					armor_bag.add(a);
					a.Picked = true;
				}
				else {}
			else {
				armor_bag.get(cur_armor-1).Picked = false;
				armor_bag.get(cur_armor-1).pos_x = this.x;
				armor_bag.get(cur_armor-1).pos_y = this.y;
				armor_bag.remove(cur_armor-1);
				armor_bag.add(cur_armor-1,a);
				a.Picked = true;
			}
		}
		
		if (Player.cnt_distance(gameMap.getWidth()/2, gameMap.getHeight()/2, this.x, this.y) > client.ring - 200) {
			do {
				des_x = (int)Math.random()*gameMap.getWidth();
				des_y = (int)Math.random()*gameMap.getHeight();
				
			}while(gameMap.playerPassable(des_x, des_y)==1 
					&& Player.cnt_distance(gameMap.getWidth()/2, gameMap.getHeight()/2, des_x, des_y) < client.ring-500);
			moveTo(des_x,des_y);
			super.move();
			return;
		}
		
		
		if (Player.cnt_distance(x, y, player.x, player.y) > 1000 && medicine_bag.size() > 0) {
			this.cur_medicine = 1;
			healling();
		}
		int notice_dist = run? 800:500;
		if (Player.cnt_distance(x, y, player.x, player.y) > notice_dist) {	
				run = false;
				if (bag.size()<2) {
					if (bag.size()==1) {
							cur_weapon = 1;
							Weapon w = bag.get(cur_weapon-1);
							if (w.bullet_num <= 2.0/3*w.max_bullet)
								reload();
					}
					Weapon notice_w = this.notice_weapon(300);
					if (notice_w != null) {
						if (Player.cnt_distance(notice_w.pos_x, notice_w.pos_y, x, y) <= 2*block_size) {
							if(cur_weapon == 0) {
								cur_weapon = 1;
								bag.add(notice_w);
							notice_w.Picked = true;
							}
							else {
								bag.add(notice_w);
								notice_w.Picked = true;
							}
						}
						else {	
							moveTo(notice_w.pos_x,notice_w.pos_y);
						}
						Bullet notice_b = notice_bullet(100);
						if (notice_b != null) {
							if (Player.cnt_distance(notice_b.pos_x, notice_b.pos_y, x, y) <= 2*block_size) {
								if(notice_b.name == "7.62*39") {
									a762_39 += 30;
								}
								else if(notice_b.name == "5.45*39") {
									a545_39 += 30;
								}
								else if(notice_b.name == "7.62*54R") {
									a762_54r += 30;
								}
								client.bullet.remove(notice_b);
							}
							else {
								moveTo(notice_b.pos_x,notice_b.pos_y);
							}
						}
					}
					else {
						Bullet notice_b = notice_bullet(300);
						if (notice_b != null) {
							if (Player.cnt_distance(notice_b.pos_x, notice_b.pos_y, x, y) <= block_size) {
								if(notice_b.name == "7.62*39") {
									a762_39 += 30;
								}
								else if(notice_b.name == "5.45*39") {
									a545_39 += 30;
								}
								else if(notice_b.name == "7.62*54R") {
									a762_54r += 30;
								}
								client.bullet.remove(notice_b);
							}
							else {
								moveTo(notice_b.pos_x,notice_b.pos_y);
							}
						}
					}
				}
				else {
					cur_weapon = 1;
					Weapon w = bag.get(cur_weapon-1);
					if (w.bullet_num < 2.0/3*w.max_bullet) {
						reload();
					}else {
						cur_weapon = 2;
						reload();
					}
					Bullet notice_b = notice_bullet(300);
					if (notice_b != null) {
						if (Player.cnt_distance(notice_b.pos_x, notice_b.pos_y, x, y) <= block_size) {
							if(notice_b.name == "7.62*39") {
								a762_39 += 30;
							}
							else if(notice_b.name == "5.45*39") {
								a545_39 += 30;
							}
							else if(notice_b.name == "7.62*54R") {
								a762_54r += 30;
							}
							client.bullet.remove(notice_b);
						}
						else {
							moveTo(notice_b.pos_x,notice_b.pos_y);
						}
					}
					Armor notice_a = this.notice_armor(250);
					if (notice_a != null) {
						moveTo(notice_a.pos_x,notice_a.pos_y);
					}
				}
		
				super.move();
			}
			else {
				boolean fight = false;
				cur_weapon = 1;
				while(cur_weapon <= bag.size()) {
					//reload();
					Weapon cur = bag.get(cur_weapon-1);
					//System.out.println("fight " + cur.name +" "+ cur.bullet_num + " " + this.direction + " " + player.x +" "+ player.y);
					if(cur.bullet_num>0) {
						fight = true;
						this.direction = shoot_angle(player)+(Math.random()-0.5)*0.2;
						//this.direction = Player.azimuthAngle(x, y, player.x, player.y)+(Math.random()-0.5)*0.3;
						moveWhileFighting();
						shoot = true;
						shoot();
						
						break;
					}
					cur_weapon++;
				}
				if (!fight) {
					//System.out.println("run");
					run = true;
					
					while(des_x>=this.gameMap.getWidth()||des_y>=this.gameMap.getHeight()
							||gameMap.getPlayerPassableMap()[des_x/block_size][des_y/block_size] == 0
							||Player.cnt_distance(des_x, des_y, player.x, player.y) <= Player.cnt_distance(x, y, player.x, player.y)) {
						des_x = (int)(this.gameMap.getWidth()*Math.random());
						des_y = (int)(this.gameMap.getHeight()*Math.random());
					}
					//System.out.println("run "+des_x+" "+des_y);
					moveTo(des_x,des_y);
					cur_weapon = 1;
					reload();
					super.move();
				}
			}
		}
	
	
	//判断两点是否在同一块内或8邻域的相邻块
	public boolean inSame(int x1,int y1,int x2,int y2) {
		int x_1 = x1/block_size;
		int y_1 = y1/block_size;
		int x_2 = x2/block_size;
		int y_2 = y2/block_size;
		return Math.abs(x_1-x_2) <=1 && Math.abs(y_1-y_2) <= 1;
	}
	
	//移动至指定位置
	public void moveTo(int toX, int toY) {
		
		this.up = false;
		this.down = false;
		this.left = false;
		this.right = false;
		if ((this.x/block_size == toX/block_size && this.y/block_size == toY/block_size)
				||astar.graph[toX/block_size][toY/block_size] == 0) {
			//System.out.println(this.x +" "+this.y+" "+toX+" "+toY);
			if (path == null || path.isEmpty()) {
				do {
					des_x = (int)(Math.random()*gameMap.getWidth());
					des_y = (int)(Math.random()*gameMap.getHeight());
					//System.out.println(this.x +" "+this.y+" "+des_x+" "+des_y +" "+this.gameMap.getPlayerPassableMap()[des_x/block_size][des_y/block_size]);
				}while(this.gameMap.playerPassable(des_x, des_y) != 1);
				//System.out.println(this.x +" "+this.y+" "+des_x+" "+des_y);
				moveTo(des_x,des_y);
			}
			else {
				Point p = path.poll();
				des_x = p.x * block_size;
				des_y = p.y * block_size;
				moveTo(des_x,des_y);
			}
			return;
		}
		//System.out.println(this.x +" "+this.y+" "+des_x+" "+des_y);
		if (inSame(this.x,this.y,toX,toY)) {
			if (this.y < toY) 
				this.down = true;
			if (this.y > toY)
				this.up = true;
			if (this.x > toX)
				this.left = true;
			if (this.x < toX)
				this.right = true;
		}
		else {
			//System.out.println(this.x +" "+this.y+" "+des_x+" "+des_y);
			//System.out.println(toX+" "+toY);
			path = astar.search(this.x/block_size, this.y/block_size, toX/block_size, toY/block_size);
			Point p = path.poll();
			des_x = p.x * block_size;
			des_y = p.y * block_size;
			//System.out.println(this.x+" "+this.y+" "+des_x+" "+des_y+gameMap.playerPassable(des_x, des_y));
			moveTo(des_x,des_y);
		}
	}
	
	//根据移动方向调整朝向
	public void set_direction() {
		if (this.up && this.right)
			this.direction = 0.75 *Math.PI;
		else if (this.up && this.left)
			this.direction = 1.25 * Math.PI;
		else if (this.up)
			this.direction = Math.PI;
		else if (this.down && this.right)
			this.direction = 0.25 * Math.PI;
		else if (this.down && this.left)
			this.direction = 1.75 * Math.PI;
		else if (this.down)
			this.direction = 0;
		else if (this.left)
			this.direction = 1.5 * Math.PI;
		else if (this.right)
			this.direction = 0.5 * Math.PI;
	}
}
