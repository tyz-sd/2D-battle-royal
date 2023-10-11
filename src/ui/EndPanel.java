package ui;

import java.awt.Color;
import client.client;
import java.awt.Font;

import javax.swing.JLabel;

import main.Bot;
import sound.musicStaff;
public class EndPanel extends BasicPanel{
	static String winning_filepath = "./resources/sound/win.wav";
	static musicStaff winning_sound = new musicStaff(winning_filepath);
	static String losing_filepath = "./resources/sound/dead.wav";
	static musicStaff losing_sound = new musicStaff(losing_filepath);
    MyButton backButton = new MyButton("Back");
    JLabel winLabel1 = new JLabel("Winner Winner");
    JLabel winLabel2 = new JLabel("Chicken Dinner");
    JLabel loseLabel = new JLabel("DEFEAT");
    EndPanel(){
        super("./resources/ui/dust.jpeg");
        backButton.setBounds(600, 700, 200, 50);
        add(backButton);
        winLabel1.setForeground(Color.orange);
        winLabel1.setFont(new Font("consolas", Font.BOLD, 90));
        winLabel1.setBounds(300, 100, 900, 400);
        this.add(winLabel1);
        winLabel2.setForeground(Color.orange);
        winLabel2.setFont(new Font("consolas", Font.BOLD, 90));
        winLabel2.setBounds(300, 300, 900, 400);
        this.add(winLabel2);
        loseLabel.setForeground(Color.blue);
        loseLabel.setFont(new Font("consolas", Font.BOLD, 140));
        loseLabel.setBounds(400, 200, 800, 400);
        this.add(loseLabel);
    }
    public void setWin(){
        for (int botn = 1;botn < client.allPlayers.size();botn++) {
			Bot bot = (Bot)client.allPlayers.get(botn);
			if(bot.running_sound.isPlaying)
				bot.running_sound.stopMusic();
			if(bot.walking_sound.isPlaying)
				bot.walking_sound.stopMusic();
		}
		if(client.me.running_sound.isPlaying)
			client.me.running_sound.stopMusic();
		if(client.me.walking_sound.isPlaying)
			client.me.walking_sound.stopMusic();
    	winning_sound.playMusic(false);
        winLabel1.setVisible(true);
        winLabel2.setVisible(true);
        loseLabel.setVisible(false);
    }
    public void setLose(){
        for (int botn = 1;botn < client.allPlayers.size();botn++) {
			Bot bot = (Bot)client.allPlayers.get(botn);
			if(bot.running_sound.isPlaying)
				bot.running_sound.stopMusic();
			if(bot.walking_sound.isPlaying)
				bot.walking_sound.stopMusic();
		}
		if(client.me.running_sound.isPlaying)
			client.me.running_sound.stopMusic();
		if(client.me.walking_sound.isPlaying)
			client.me.walking_sound.stopMusic();
    	losing_sound.playMusic(false);
        winLabel1.setVisible(false);
        winLabel2.setVisible(false);
        loseLabel.setVisible(true);
    }
}
