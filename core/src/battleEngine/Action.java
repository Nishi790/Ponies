package battleEngine;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import main.MainChar;

public class Action {
	
	int minDamage;
	int maxDamage;
	int accuracy;
	Animation<TextureRegion> animation;
	String name;
	Random dice;
	boolean attacking=false;
	Monster actor;
	float animationTime=0f;
	
	public Action(Monster m,FileHandle f){
		actor=m;
		dice=new Random();
		String[] temp=f.readString().split(";");
		minDamage=Integer.parseInt(temp[0]);
		maxDamage=Integer.parseInt(temp[1]);
		accuracy=Integer.parseInt(temp[2]);
		name=temp[3];
		Texture spritesheet=new Texture(Gdx.files.internal("data/Monsters/"+temp[4]));
		TextureRegion[][] tmp=TextureRegion.split(spritesheet, Integer.parseInt(temp[5]), Integer.parseInt(temp[6]));
		Array<TextureRegion> animationFrames=new Array<TextureRegion>();
		for(int i=0; i<tmp.length;i++){
			for(int j=0;j<tmp[i].length;j++){
				animationFrames.add(tmp[i][j]);
			}
		}
		animation=new Animation<TextureRegion>(0.1f,animationFrames);
		animation.setPlayMode(PlayMode.NORMAL);
		
		
	}
	
	public void perform(MainChar target){
		int damage=dice.nextInt(maxDamage-minDamage)+minDamage;
		target.setPoints(0, target.getPoints()[0]-damage);
		attacking=true;
		actor.attacking=true;
		
	}
}
