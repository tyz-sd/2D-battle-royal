/**
 * 画地图的类
 */

package ui;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import map.Atlas;

import javax.swing.*;

public class MapBuilder extends BasicPanel{

    int nrow = 160, ncol = 160;
    int[][] tiles = new int[nrow][ncol];
    int curTile;
    int penSize = 1;
    int tileSize = 32;

    MapPanel mapPanel = new MapPanel();
    JTextField mapNameField = new JTextField("new_map", 30);
    MyButton saveButton = new MyButton("Save");
    MyButton landButton = new MyButton("Land");
    MyButton waterButton = new MyButton("Water");
    MyButton wallButton = new MyButton("Wall");
    MyButton bigButton = new MyButton("bigger");
    MyButton smallButton = new MyButton("smaller");
    MyButton backButton = new MyButton("back");
    JSlider penSizeSlider = new JSlider(1, 5, 1);
    JLabel penSizeLabel = new JLabel("Brush size:");

    public MapBuilder(){
        super("./resources/ui/blackgun.jpg");
        initMaps();
        this.setLayout(new BorderLayout());
        
        penSizeLabel.setForeground(Color.WHITE);
        penSizeLabel.setFont(new Font("新宋", Font.BOLD, 28));
        
        
        JPanel topPanel = new JPanel();
        topPanel.setOpaque(false);
        topPanel.add(mapNameField);
        topPanel.add(saveButton);
        topPanel.add(backButton);
        add(topPanel, BorderLayout.NORTH);

        saveButton.addActionListener((e)->{
            saveMap();
        });

        add(mapPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.add(landButton);
        bottomPanel.add(waterButton);
        bottomPanel.add(wallButton);
        bottomPanel.add(penSizeLabel);
        bottomPanel.add(penSizeSlider);
        add(bottomPanel, BorderLayout.SOUTH);

        landButton.addActionListener((e)->{
            curTile = Atlas.tileId.get("land");
        });
        waterButton.addActionListener((e)->{
            curTile = Atlas.tileId.get("water");
        });
        wallButton.addActionListener((e)->{
            curTile = Atlas.tileId.get("wall");
        });
        penSizeSlider.addChangeListener((e)->{
        	penSize = penSizeSlider.getValue();
        });
        penSizeSlider.setOpaque(false);

        //setSize(800, 800);
        setVisible(true);
    }

    void initMaps(){
        curTile = Atlas.tileId.get("land");

        for(int i = 0; i < nrow; i++)
            for(int j = 0; j < ncol; j++){
                tiles[i][j] = Atlas.tileId.get("land");
            }
    }

    void saveMap(){
    	renderWaterEdge();
    	
        String filename = mapNameField.getText();
        try{
            BufferedWriter out = new BufferedWriter(new FileWriter("./resources/maps/" + filename));
            out.write("" + nrow + " " + ncol + " " + tileSize + "\n");
            for(int i = 0; i < nrow; i++){
                for(int j = 0; j < ncol; j++)
                    out.write("" + tiles[i][j] + " ");
                out.write("\n");
            }
            out.close();
                
        }
        catch(IOException e){
            e.printStackTrace();
        }

    }
    
    void renderWaterEdge() {
    	for(int i = 1; i < nrow-1; i++)
    		for(int j = 1; j < ncol-1; j++) {
    			if(!isWater(tiles[i][j]))
    				continue;
    			int status = 0;
    			int[] di = {-1, 0, 0, 1};
    			int[] dj = {0, -1, 1, 0};
    			for(int k = 0; k < 4; k++) {
    				if(!isWater(tiles[i + di[k]][j + dj[k]]))
    					status |= 0b1000 >> k;
    			}
    			
    			String newTileName = "waterEdge";
    			switch(status) {
    			case 0b1100:
    				newTileName += 0;
    				break;
    			case 0b1000:
    			case 0b1110:
    				newTileName += 1;
    				break;
    			case 0b1010:
    				newTileName += 2;
    				break;
    			case 0b0100:
    			case 0b1101:
    				newTileName += 3;
    				break;
    			case 0b0010:
    			case 0b1011:
    				newTileName += 5;
    				break;
    			case 0b0101:
    				newTileName += 6;
    				break;
    			case 0b0001:
    			case 0b0111:
    				newTileName += 7;
    				break;
    			case 0b0011:
    				newTileName += 8;
    				break;
    			case 0b0000:
        			int status2 = 0;
        			int[] di2 = {-1, -1, 1, 1};
        			int[] dj2 = {-1, 1, -1, 1};
        			for(int l = 0; l < 4; l++) {
        				if(!isWater(tiles[i + di2[l]][j + dj2[l]]))
        					status2 |= 0b1000 >> l;
        			}
        			switch(status2) {
        			case 0b1000:
        				newTileName += "Concave3";
        				break;
        			case 0b0100:
        				newTileName += "Concave2";
        				break;
        			case 0b0010:
        				newTileName += "Concave1";
        				break;
        			case 0b0001:
        				newTileName += "Concave0";
        				break;
        			default:
        				newTileName = "water";
        			}
        			break;
    			default:
    				newTileName = "water";
    			}
    			try {
    				tiles[i][j] = Atlas.tileId.get(newTileName);
    			}
    			catch(Exception e) {
    				System.out.println(newTileName);
    				e.printStackTrace();
    			}
    		}
    }
    
    boolean isWater(int x) {
    	return !(x == Atlas.tileId.get("land") || x == Atlas.tileId.get("wall"));
    }

    class MapPanel extends JPanel{
        final int padding = 20;
        final int gridLen = 4;
        int mouseX = 0, mouseY = 0;
        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            for(int i = 0; i < nrow; i++)
                for(int j = 0; j < ncol; j++){
                    int x = padding + j * gridLen;
                    int y = padding + i * gridLen;
                    g.setColor(Atlas.tileColor.get(tiles[i][j]));
                    g.fillRect(x, y, gridLen, gridLen);
                }
                paintMouse(g);
        }
        void paintMouse(Graphics g){
            int x = mouseX - (penSize-1) * gridLen, y = mouseY - (penSize-1) * gridLen;
            int width = gridLen * (penSize * 2 - 1);
            g.setColor(Atlas.tileColor.get(curTile));
            g.fillRect(x, y, width, width);
        }
        MapPanel(){
            this.setOpaque(false);
            addMouseMotionListener(new MouseMotionAdapter(){
                @Override
                public void mouseMoved(MouseEvent e){
                    mouseX = e.getX();
                    mouseY = e.getY();
                    SwingUtilities.invokeLater(()->{
                        repaint();
                    });
                }
                @Override
                public void mouseDragged(MouseEvent e){
                    int tempX = window2grid(mouseX);
                	int tempY = window2grid(mouseY);
                    mouseX = e.getX();
                    mouseY = e.getY();
                    int j = window2grid(e.getX());
                    int i = window2grid(e.getY());
                    if (i == tempY) {
                    	for (int t = Math.min(tempX,j);t <= Math.max(tempX, j);t++) {
                    		for(int ii = i - (penSize-1); ii <= i + (penSize-1); ii++) {
                    			for(int jj = t - (penSize-1); jj <= t + (penSize-1); jj++){
                    				if(ii < 0 || ii >= nrow || jj < 0 || jj >= ncol) 
                    					continue;
                    				tiles[ii][jj] = curTile;
                    			}
                    		}
                    	}
                    }
                    else {
                    	float k1 = (float)(j-tempX)/(i-tempY), k2 = 1;
                    	if (k1 > 0) {
                    		if (k1 > 1) {
                    			k2 = k2/k1;
                    			k1 = 1;
                    		}
                    		for (float t1 = Math.min(tempX,j), t2 = Math.min(tempY, i);
                    				t1 <= Math.max(tempX, j) && t2 <= Math.max(tempY, i);t1 += k1,t2 += k2) {
                    			for(int ii = (int)t2 - (penSize-1); ii <= (int)t2 + (penSize-1); ii++) {
                    				for(int jj = (int)t1 - (penSize-1); jj <= (int)t1 + (penSize-1); jj++){
                    					if(ii < 0 || ii >= nrow || jj < 0 || jj >= ncol) 
                    						continue;
                    					tiles[ii][jj] = curTile;
                    				}
                    			}
                    		}
                    	}
                    	else {
                    		if (k1 < -1) {
                        		k2 = -k2/k1;
                        		k1 = -1;
                        	}
                        	for (float t1 = Math.max(tempX,j), t2 = Math.min(tempY, i);
                        			t1 >= Math.min(tempX, j) && t2 <= Math.max(tempY, i);t1 += k1,t2 += k2) {
                        		for(int ii = (int)t2 - (penSize-1); ii <= (int)t2 + (penSize-1); ii++) {
                        			for(int jj = (int)t1 - (penSize-1); jj <= (int)t1 + (penSize-1); jj++){
                        				if(ii < 0 || ii >= nrow || jj < 0 || jj >= ncol) 
                        					continue;
                        				tiles[ii][jj] = curTile;
                        			}
                        		}
                        	}
                    	}
                    }
                    SwingUtilities.invokeLater(()->{
                        repaint();
                    });
                }
            });
        }
        int window2grid(int x){
            return (x - padding) / gridLen;
        }
    }
}
