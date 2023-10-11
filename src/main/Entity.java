package main;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

public abstract class Entity {
	int x;//横坐标
	int y;//纵坐标
	Image img;//图片
	int width;//图片宽度
	int height;//图片高度
	String name;//实体名称
	double direction;//实体面对方向
	public boolean live;//死亡/存活
	
	public abstract void draw(Graphics g);
	/**
	 * 获取图片对应的矩形
	 * 
	 * @return
	 */
	public Rectangle getRectangle() {
		return new Rectangle(x, y, width, height);
	}
}
