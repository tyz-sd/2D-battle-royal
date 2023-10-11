package map;
import map.Atlas;
import client.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Player;

public class MiniMap {
	Player player;
	GameMap gameMap;
	int[][] tiles;
	int nrow, ncol;
	BufferedImage image;
	
	public MiniMap(Player p, GameMap g){
		player = p;
		gameMap = g;
		tiles = g.getTiles();
        nrow = tiles.length;
        ncol = tiles[0].length;
        
        image = new BufferedImage(ncol, nrow, BufferedImage.TYPE_INT_RGB);
        for(int i = 0; i < nrow; i++)
            for(int j = 0; j < ncol; j++){
            	image.setRGB(j, i, Atlas.tileColor.get(tiles[i][j]).getRGB());
            }
	}
	
	public void draw(int x, int y, Graphics g) {
        g.drawImage(image, x, y, null);
        
        int px = x + player.x / gameMap.tileSize;
        int py = y + player.y / gameMap.tileSize;
        int r = 4;
        g.setColor(Color.RED);
        g.fillOval(px-r, py-r, r*2, r*2);
        g.setClip(x, y, gameMap.ncol, gameMap.nrow);
        g.drawOval(x+2*client.center_x/gameMap.tileSize/2 - client.ring/gameMap.tileSize,
        		   y+2*client.center_y/gameMap.tileSize/2 - client.ring/gameMap.tileSize,
        		       client.ring*2/gameMap.tileSize,		client.ring*2/gameMap.tileSize);
        g.setClip(null);
	}
}
