package sound;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class musicStaff {
	public Clip myclip;
	File musicPath = new File("");
	public boolean isPlaying = false;
	public musicStaff(String fileinput){
		musicPath = new File(fileinput);
		try
		{
			if(musicPath.exists())
			{
				AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
				Clip clip = AudioSystem.getClip();
				myclip = clip;
				myclip.open(audioInput);
			}
			else
			{
				
			}
		}
		catch(Exception ex)
		{
			System.out.println(this.musicPath);
			ex.printStackTrace();
		}
	}
	public void playMusic( boolean loop)
	{
		try
		{
			if(!myclip.isRunning()) {
				myclip.close();
				AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
				myclip.open(audioInput);
				myclip.start();
				if(loop)
					myclip.loop(Clip.LOOP_CONTINUOUSLY);
				else
					myclip.loop(0);
			}
			else {
				
			}
		}
		catch(Exception ex)
		{
			System.out.println(this.musicPath);
			ex.printStackTrace();
		}
	}
	public void stopMusic() {
		if(myclip != null) {
			isPlaying = false;
			myclip.stop();
		}
	}
}
