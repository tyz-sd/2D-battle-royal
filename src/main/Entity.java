package main;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

public abstract class Entity {
	int x;//������
	int y;//������
	Image img;//ͼƬ
	int width;//ͼƬ���
	int height;//ͼƬ�߶�
	String name;//ʵ������
	double direction;//ʵ����Է���
	public boolean live;//����/���
	
	public abstract void draw(Graphics g);
	/**
	 * ��ȡͼƬ��Ӧ�ľ���
	 * 
	 * @return
	 */
	public Rectangle getRectangle() {
		return new Rectangle(x, y, width, height);
	}
}
