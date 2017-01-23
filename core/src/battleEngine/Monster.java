package battleEngine;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import main.MainChar;

public class Monster {
	Sprite avatar;
	String name;
	int hp;
	Action[] possibleActions;
	boolean attacking;
	Random r;
	Action currentAction;
	int width,height;
	
	public Monster(FileHandle f){
		String[] temp=f.readString().split(";");
		name=temp[0];
		avatar=new Sprite(new Texture(Gdx.files.internal("data/Monsters/"+temp[1])));
		width=Integer.parseInt(temp[2]);
		height=Integer.parseInt(temp[3]);
		avatar.setSize(width, height);//width;height
		avatar.setPosition(Integer.parseInt(temp[4]), Integer.parseInt(temp[5]));//x-coord; y-coord
		int maxHP=Integer.parseInt(temp[6]);
		r=new Random();
		hp=r.nextInt(maxHP+1);
		possibleActions=new Action[Integer.parseInt(temp[7])];
		for(int i=0;i<possibleActions.length;i++){
			Action act=new Action(this,Gdx.files.internal("data/Monsters/Action"+temp[i+8]));
			possibleActions[i]=act;
		}
		
	}
	
	public void act(MainChar target){
		int action=r.nextInt(possibleActions.length);
		currentAction=possibleActions[action];
		currentAction.perform(target);
	}
	
	public void draw(Batch b){
		if(currentAction!=null&&currentAction.attacking){
			//get current animation frame
			TextureRegion frame=currentAction.animation.getKeyFrame(currentAction.animationTime);
			b.draw(frame, avatar.getX(),avatar.getY(), width, height);
					
			//add time to prepare for next animation frame
			currentAction.animationTime=currentAction.animationTime+Gdx.graphics.getDeltaTime();
					
			//if animation is finished, stop rendering
			if(currentAction.animation.isAnimationFinished(currentAction.animationTime)){
				currentAction.attacking=false;
				attacking=false;
				currentAction.animationTime=0;
			}
		}
		else avatar.draw(b);
	}
	
	
	
}
