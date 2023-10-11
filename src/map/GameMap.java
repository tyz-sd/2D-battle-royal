package map;
import java.awt.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import main.Player;

import java.io.*;

public class GameMap {
    String name;
    int[][] tiles;
    int nrow, ncol, tileSize, tileNum;
    int mapWidth, mapHeight, windowWidth, windowHeight;
    Image atlas;
    final int atlasSize = 1024;
    
    final int[][] playerPassableGrid;
    final int[][] bulletPassableGrid;

    public GameMap(String name, int windowWidth, int windowHeight){
        try{
            atlas = ImageIO.read(new File("./resources/Atlas/terrain_atlas.png"));
            Scanner input = new Scanner(new FileInputStream("./resources/maps/"+name));
            nrow = input.nextInt();
            ncol = input.nextInt();
            tileSize = input.nextInt();
            mapWidth = ncol * tileSize;
            mapHeight = nrow * tileSize;
            this.windowWidth = windowWidth;
            this.windowHeight = windowHeight;

            tiles = new int[nrow][ncol];
            for(int i = 0; i < nrow; i++)
                for(int j = 0; j < ncol; j++){
                    tiles[i][j] = input.nextInt();
                }

            input.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        
        playerPassableGrid = initPlayerPassableMap();
        bulletPassableGrid = initBulletPassableMap();
    }
    public void render(int cameraX, int cameraY, Graphics g){
        int width = windowWidth, height = windowHeight;
        int x2 = windowWidth, y2 = windowHeight;
        // 相机放到左上角
        cameraX = cameraX - width/2;
        cameraY = cameraY - height/2;

        int penX = cameraX - cameraX % tileSize - tileSize;
        int penY = cameraY - cameraY % tileSize - tileSize;

        for(int x = penX; x < x2 + cameraX; x += tileSize){
            for(int y = penY; y < y2 + cameraY; y += tileSize){
                try{
                    drawTile(tiles[y/tileSize][x/tileSize], x - cameraX, y - cameraY, g);
                }
                catch(IndexOutOfBoundsException e){
                    g.setColor(Color.gray);
                    g.fillRect(x-cameraX, y-cameraY, tileSize, tileSize);
                }
            }
        }        
    }
    public void drawEntity(Drawable entity, int x, int y, int cameraX, int cameraY, Graphics g){
        cameraX = cameraX - windowWidth/2;
        cameraY = cameraY - windowHeight/2;
        entity.draw(x - cameraX, y - cameraY, g);
    }
    private void drawTile(int tile, int x, int y, Graphics g){
        int tx = tile / atlasSize, ty = tile % atlasSize;
        g.drawImage(atlas, x, y, x + tileSize, y + tileSize,
            tx, ty, tx + tileSize, ty + tileSize, null);
    }
    public void drawString(String s, int x, int y, int cameraX, int cameraY, Graphics g) {
        cameraX = cameraX - windowWidth/2;
        cameraY = cameraY - windowHeight/2;
    	g.drawString(s, x-cameraX , y-cameraY);
    }
    public int[][] getPlayerPassableMap(){
    	return this.playerPassableGrid;
    }
    public int[][] getBulletPassableMap(){
    	return this.bulletPassableGrid;
    }
    private int[][] initPlayerPassableMap(){
        int[][] res = new int[ncol][nrow];
        for(int i = 0; i < nrow; i++)
            for(int j = 0; j < ncol; j++){
                res[j][i] = Atlas.playerPass.get(tiles[i][j]);
            }
        return res;
    }
    private int[][] initBulletPassableMap(){
        int[][] res = new int[ncol][nrow];
        for(int i = 0; i < nrow; i++)
            for(int j = 0; j < ncol; j++){
                res[j][i] = Atlas.bulletPass.get(tiles[i][j]);
            }
        return res;
    }

    public int screenToWorldX(int sx, int cameraX){
        cameraX = cameraX - windowWidth/2;
        return sx + cameraX;
    }
    public int screenToWorldY(int sy, int cameraY){
        cameraY = cameraY - windowHeight/2;
        return sy + cameraY;
    }

    public int getWidth(){
        return mapWidth;
    }
    public int getHeight(){
        return mapHeight;
    }

    public int playerPassable(int x, int y){
    	if(x < 0 || y < 0)return 0;
        int i = y / tileSize;
        int j = x / tileSize;
        int passable = 1;
        try{
            passable = Atlas.playerPass.get(tiles[i][j]);
        }
        catch(IndexOutOfBoundsException e){
            passable = 0;
        }
        return passable;
    }

    public int bulletPassable(int x, int y){
        if(x < 0 || y < 0) return 0;
        int i = y / tileSize;
        int j = x / tileSize;
        int passable = 1;
        try{
            passable = Atlas.bulletPass.get(tiles[i][j]);
        }
        catch(IndexOutOfBoundsException e){
            passable = 0;
        }
        return passable;
    }

    public int[][] getTiles(){
        return tiles.clone();
    }
    

    //测试用
    /*
    @SuppressWarnings("serial")
	class MapFrame extends JFrame{
        MapFrame(){
            setTitle("test map");
            setSize(800, 600);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setVisible(true);
        }
        @Override
        public void paint(Graphics g){
            render(0, 0, g);
            //drawTile(tiles[0][0], 20, 20, g);
            for(int i = 0; i < 4; i++)
            	drawTile(Atlas.tileId.get("waterEdgeConcave" + i), 20 + 35*i, 200, g);
        }
    }
    // for test
    public static void main(String[] args) {
    	MapFrame frame = new GameMap("testWater", 800, 600).new MapFrame();
    }
    */
}
