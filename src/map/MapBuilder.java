package map;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.*;

@SuppressWarnings("serial")
public class MapBuilder extends JFrame{
    public static void main(String[] args) {
        SwingUtilities.invokeLater(()->{
            MapBuilder mapBuilder = new MapBuilder();
            mapBuilder.setVisible(true);
        });
    }

    Map<String, Integer> tileId = new ConcurrentHashMap<>();
    Map<Integer, Integer> playerPass = new ConcurrentHashMap<>();
    Map<Integer, Integer> bulletPass = new ConcurrentHashMap<>();
    Map<Integer, Color> tileColor = new ConcurrentHashMap<>();

    int nrow = 160, ncol = 160;
    int[][] tiles = new int[nrow][ncol];
    int curTile;
    int penSize = 1;
    int tileSize = 32, tileNum = 3;

    MapPanel mapPanel = new MapPanel();
    JTextField mapNameField = new JTextField("new_map", 30);
    JButton saveButton = new JButton("Save");
    JButton landButton = new JButton("Land");
    JButton waterButton = new JButton("Water");
    JButton wallButton = new JButton("Wall");
    JButton bigButton = new JButton("bigger");
    JButton smallButton = new JButton("smaller");

    MapBuilder(){
        initMaps();
        setTitle("Map Builder");

        JPanel topPanel = new JPanel();
        topPanel.add(mapNameField);
        topPanel.add(saveButton);
        add(topPanel, BorderLayout.NORTH);

        saveButton.addActionListener((e)->{
            saveMap();
        });

        add(mapPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(landButton);
        bottomPanel.add(waterButton);
        bottomPanel.add(wallButton);
        bottomPanel.add(bigButton);
        bottomPanel.add(smallButton);
        add(bottomPanel, BorderLayout.SOUTH);

        landButton.addActionListener((e)->{
            curTile = tileId.get("land");
        });
        waterButton.addActionListener((e)->{
            curTile = tileId.get("water");
        });
        wallButton.addActionListener((e)->{
            curTile = tileId.get("wall");
        });
        bigButton.addActionListener((e)->{
            penSize += 1;
            if(penSize > 5) penSize = 5;
        });
        smallButton.addActionListener((e)->{
            penSize -= 1;
            if(penSize < 1) penSize = 1;
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 800);
        setVisible(true);
    }

    void initMaps(){
        tileId.put("land", 544*1024+160);
        tileId.put("water", 672*1024+544);
        tileId.put("wall", 448*1024+480);
        curTile = tileId.get("land");

        playerPass.put(tileId.get("land"), 1);
        playerPass.put(tileId.get("water"), 0);
        playerPass.put(tileId.get("wall"), 0);

        bulletPass.put(tileId.get("land"), 1);
        bulletPass.put(tileId.get("water"), 1);
        bulletPass.put(tileId.get("wall"), 0);

        tileColor.put(tileId.get("land"), Color.green);
        tileColor.put(tileId.get("water"), Color.blue);
        tileColor.put(tileId.get("wall"), Color.gray);

        for(int i = 0; i < nrow; i++)
            for(int j = 0; j < ncol; j++){
                tiles[i][j] = tileId.get("land");
            }
    }

    void saveMap(){
        String filename = mapNameField.getText();
        try{
            BufferedWriter out = new BufferedWriter(new FileWriter(filename));
            out.write("" + nrow + " " + ncol + " " + tileSize + " " + tileNum + "\n");
            for(int id : tileId.values()){
                out.write("" + id + " " + playerPass.get(id) + " " + bulletPass.get(id) + "\n");
            }
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
                    g.setColor(tileColor.get(tiles[i][j]));
                    g.fillRect(x, y, gridLen, gridLen);
                }
                paintMouse(g);
        }
        void paintMouse(Graphics g){
            int x = mouseX - (penSize-1) * gridLen, y = mouseY - (penSize-1) * gridLen;
            int width = gridLen * (penSize * 2 - 1);
            g.setColor(tileColor.get(curTile));
            g.fillRect(x, y, width, width);
        }
        MapPanel(){
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
                    mouseX = e.getX();
                    mouseY = e.getY();
                    int j = window2grid(e.getX());
                    int i = window2grid(e.getY());
                    for(int ii = i - (penSize-1); ii < i + (penSize-1); ii++)
                        for(int jj = j - (penSize-1); jj < j + (penSize-1); jj++){
                            if(ii < 0 || ii >= nrow || jj < 0 || jj >= ncol) continue;
                            tiles[ii][jj] = curTile;
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
