/**
 * 这个类包含了Atlas到图形的转换映射
 * 包括每一类Tile的图片、属性等 
 */

package map;
import java.awt.Color;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
public class Atlas {
    public static Map<String, Integer> tileId = new ConcurrentHashMap<>();
    public static Map<Integer, Integer> playerPass = new ConcurrentHashMap<>();
    public static Map<Integer, Integer> bulletPass = new ConcurrentHashMap<>();
    public static Map<Integer, Color> tileColor = new ConcurrentHashMap<>();
    static {
        tileId.put("land", 544*1024+160);
        tileId.put("water", 672*1024+544);
        tileId.put("wall", 448*1024+480);
        for(int i = 0; i < 3; i++)
        	for(int j = 0; j < 3; j++) {
        		tileId.put("waterEdge" + (i*3+j), (576+j*32)*1024 + 192+i*32);
        	}
        for(int i = 0; i < 2; i++)
        	for(int j = 0; j < 2; j++) {
        		tileId.put("waterEdgeConcave" + (i*2+j), (608+j*32)*1024 + 288+i*32);
        	}

        playerPass.put(tileId.get("land"), 1);
        playerPass.put(tileId.get("water"), 0);
        playerPass.put(tileId.get("wall"), 0);
        for(int i = 0; i < 9; i++)
        	playerPass.put(tileId.get("waterEdge"+i), 0);
        for(int i = 0; i < 4; i++)
        	playerPass.put(tileId.get("waterEdgeConcave" + i), 0);

        bulletPass.put(tileId.get("land"), 1);
        bulletPass.put(tileId.get("water"), 1);
        bulletPass.put(tileId.get("wall"), 0);
        for(int i = 0; i < 9; i++)
        	bulletPass.put(tileId.get("waterEdge"+i), 1);
        for(int i = 0; i < 4; i++)
        	bulletPass.put(tileId.get("waterEdgeConcave" + i), 1);

        tileColor.put(tileId.get("land"), Color.green);
        tileColor.put(tileId.get("water"), Color.blue);
        tileColor.put(tileId.get("wall"), Color.gray);
        for(int i = 0; i < 9; i++)
        	tileColor.put(tileId.get("waterEdge"+i), Color.blue);
        for(int i = 0; i < 4; i++)
        	tileColor.put(tileId.get("waterEdgeConcave" + i), Color.blue);
        
        //System.out.println("Initiated");
    }
}
