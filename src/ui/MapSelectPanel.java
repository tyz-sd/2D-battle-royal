package ui;

import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.*;

import main.MyFrame;
import map.Atlas;
import map.GameMap;

public class MapSelectPanel extends BasicPanel{
    JList<String> mapList;
    MapThumbnail mapThumbnail;
    GameMap gameMap;
    String mapName;
    MyButton createButton = new MyButton("Create New");
    MyButton goButton = new MyButton("GO!");
    MyButton backButton = new MyButton("Back");

    MapSelectPanel(){
        super("./resources/ui/blackgun.jpg");
        this.setLayout(null);

        Vector<String> mapNames = getFile("./resources/maps");
        mapList = new JList<String>(mapNames);
        mapList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mapList.setBounds(100, 200, 200, 500);
        mapList.setBackground(Color.BLACK);
        mapList.setForeground(Color.white);
        mapList.setFont(new Font("consolas", Font.PLAIN, 20));
        mapList.addListSelectionListener((e)->{
            //System.out.println("Selected!!");
            mapName = mapList.getSelectedValue();
            if(mapName == null) return;
            gameMap = new GameMap(mapName, MyFrame.Window_Width, MyFrame.Window_Height);
            if(mapThumbnail != null) this.remove(mapThumbnail);
            mapThumbnail = new MapThumbnail(gameMap, 3, 350, 200);
            this.add(mapThumbnail);
            repaint();
        });
        mapList.setSelectedIndex(0);
        this.add(mapList);

        createButton.setBounds(900, 300, 200, 50);
        this.add(createButton);
        goButton.setBounds(900, 400, 200, 50);
        this.add(goButton);
        backButton.setBounds(900, 500, 200, 50);
        this.add(backButton);
    }

    Vector<String> getFile(String path){
        Vector<String> res = new Vector<>();
        File[] files = new File(path).listFiles();

        for(int i = 0; i < files.length; i++){
            if(files[i].isFile()){
                res.add(files[i].toString().substring(17)); //cut leading "./resources/maps/"
            }
        }
        return res;
    }

    class MapThumbnail extends JPanel{
        GameMap gameMap;
        int[][] tiles;
        int pixelSize, nrow, ncol;
        MapThumbnail(GameMap gameMap, int pixelSize, int x, int y){
            this.gameMap = gameMap;
            this.pixelSize = pixelSize;
            tiles = gameMap.getTiles();
            nrow = tiles.length;
            ncol = tiles[0].length;
            this.setBounds(x, y, ncol * pixelSize, nrow * pixelSize);
        }

        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            int gridLen = pixelSize;
            
            for(int i = 0; i < nrow; i++)
                for(int j = 0; j < ncol; j++){
                    int x = j * gridLen;
                    int y = i * gridLen;
                    g.setColor(Atlas.tileColor.get(tiles[i][j]));
                    g.fillRect(x, y, gridLen, gridLen);
                }
        }
    }
}
