package ui;

import sound.musicStaff;
import javax.swing.JFrame;
import java.applet.*;
import javax.swing.SwingUtilities;
import java.awt.*;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import client.client;

public class TopFrame extends JFrame {
	public static void main(String args[]) {
		TopFrame topFrame = new TopFrame();
		topFrame.setVisible(true);
		start_sound.playMusic(true);
	}

	static String start_filepath = "./resources/sound/Start.wav";
	static musicStaff start_sound = new musicStaff(start_filepath);
	static String click_filepath = "./resources/sound/click.wav";
	static musicStaff click_sound = new musicStaff(click_filepath);
	StartPanel startPanel = new StartPanel();
	EndPanel endPanel = new EndPanel();
	HelpPanel helpPanel = new HelpPanel();
	MapSelectPanel mapPanel = new MapSelectPanel();
	MapBuilder mapBuilder = new MapBuilder(); 
	PausePanel pausePanel = new PausePanel();
	client cl;
	
	@SuppressWarnings("deprecation")
	public TopFrame(){
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("2D Battleroyal");
		this.setLayout(new CardLayout());

		this.add(startPanel);
		this.add(mapPanel);
		this.add(endPanel);
		this.add(helpPanel);
		this.add(mapBuilder);
		this.add(pausePanel);
		this.mapPanel.setVisible(false);
		this.endPanel.setVisible(false);
		this.helpPanel.setVisible(false);
		this.pausePanel.setVisible(false);
		mapBuilder.setVisible(false);
		

		startPanel.startButton.addActionListener((e)->{
			SwingUtilities.invokeLater(()->{
				click_sound.playMusic(false);
				this.startPanel.setVisible(false);
				this.mapPanel.setVisible(true);
			});
		});
		startPanel.helpButton.addActionListener((e)->{
			SwingUtilities.invokeLater(()->{
				click_sound.playMusic(false);
				this.startPanel.setVisible(false);
				this.helpPanel.setVisible(true);
				//this.endPanel.setVisible(true);
				//this.endPanel.setWin();
			});
		});

		mapPanel.createButton.addActionListener((e)->{
			SwingUtilities.invokeLater(()->{
				click_sound.playMusic(false);
				mapPanel.setVisible(false);
				mapBuilder.setVisible(true);
			});
		});
		mapPanel.goButton.addActionListener((e)->{
			SwingUtilities.invokeLater(()->{
				start_sound.stopMusic();
				click_sound.playMusic(false);
				cl = new client(mapPanel.mapName);
				cl.pausePanel = pausePanel;
				cl.endPanel = endPanel;
				Cursor cur = new Cursor(Cursor.CROSSHAIR_CURSOR);
				cl.setCursor(cur);
				this.add(cl);
				mapPanel.setVisible(false);
				cl.setVisible(true);
				cl.loadFrame();
			});
		});
		mapPanel.backButton.addActionListener((e)->{
			SwingUtilities.invokeLater(()->{
				click_sound.playMusic(false);
				mapPanel.setVisible(false);
				startPanel.setVisible(true);
			});
		});

		mapBuilder.backButton.addActionListener((e)->{
			SwingUtilities.invokeLater(()->{
				click_sound.playMusic(false);
				mapBuilder.setVisible(false);
				mapPanel.setVisible(true);
				mapPanel.mapList.setListData(mapPanel.getFile("./resources/maps/"));
				mapPanel.mapList.setSelectedIndex(0);
			});;
		});

		pausePanel.continueButton.addActionListener((e)->{
			SwingUtilities.invokeLater(()->{
				click_sound.playMusic(false);
				pausePanel.setVisible(false);
				cl.setVisible(true);
				cl.requestFocus();
			});
		});
		pausePanel.escButton.addActionListener((e)->{
			SwingUtilities.invokeLater(()->{
				click_sound.playMusic(false);
				pausePanel.setVisible(false);
				mapPanel.setVisible(true);
				this.remove(cl);
				cl = null;
			});
		});

		helpPanel.backButton.addActionListener((e)->{
			SwingUtilities.invokeLater(()->{
				click_sound.playMusic(false);
				helpPanel.setVisible(false);
				startPanel.setVisible(true);
			});
		});

		endPanel.backButton.addActionListener((e)->{
			SwingUtilities.invokeLater(()->{
				start_sound.playMusic(true);
				click_sound.playMusic(false);
				endPanel.setVisible(false);
				startPanel.setVisible(true);
			});
		});

		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
}