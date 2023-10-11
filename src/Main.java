import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class Main extends JFrame implements KeyListener, MouseMotionListener {
    public Main(){
        this.setSize(1080,960);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("Game");
        this.setVisible(true);
    }
    public static void main(String []args){
        Main test = new Main();
        test.addKeyListener(test);
        test.addMouseMotionListener(test);
        
        jg=test.getGraphics();
        
        while(true) {
        	test.paint(jg);
        }
    }
    
    private static Graphics jg;
    
    static int eagle = 40;
    static warrior you = new warrior();
    public void paint(Graphics g){
        g.drawRect(you.posx, you.posy, 10, 10);
    }
    public void keyTyped(KeyEvent e){

    }
    public void keyPressed(KeyEvent e){
        int code = e.getKeyCode();
        //System.out.println(code);
        if(code==87) {
        	you.moveUp();
        }
        if(code==83) {
        	you.moveDown();
        }
        if(code==65) {
        	you.moveLeft();
        }
        if(code==68) {
        	you.moveRight();
        }
    }
    public void keyReleased(KeyEvent e){

    }
    public void mouseDragged(MouseEvent e){

    }
    public void mouseMoved(MouseEvent e){
        int _x = e.getX();
        int _y = e.getY();
    }
}
//*****************************************************//
class warrior{
    public int health;
    public int armor;
    public weapon Wep;
    public int posx;
    public int posy;
    public warrior(){
    	this.posx=512;
    	this.posy=256;
    }
    public void moveUp() {
    	this.posy--;
    }
    public void moveDown() {
    	this.posy++;
    }
    public void moveLeft() {
    	this.posx--;
    }
    public void moveRight() {
    	this.posx++;
    }
}
class weapon{
    int damage;
    int range;
    int load;
}
class xxx_weapon extends weapon{

}