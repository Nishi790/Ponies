package battleEngine;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.utils.Array;

public class Attack extends Action {
	int accuracy;
	int minDamage;
	int damageRange;
	int criticalRange;
	int critDamage;
	boolean critical;
	boolean inProgress;
	Animation<TextureRegion> attack;
	TextureRegion currentFrame;
	float animationTime=0f;
	Random dice;
	String name;
	BattleCharacter attacker;
	String type;
	int range=1;
	
	
	public Attack(FileHandle f, BattleCharacter b){
		super();
		attacker=b;
		String[] tempFileRead=f.readString().split(";");
		accuracy=Integer.parseInt(tempFileRead[0]);
		minDamage=Integer.parseInt(tempFileRead[1]);
		damageRange=Integer.parseInt(tempFileRead[2]);
		critDamage=Integer.parseInt(tempFileRead[3]);
		criticalRange=Integer.parseInt(tempFileRead[4]);
		name=tempFileRead[5];
		inProgress=false;
		critical=false;
		TextureRegion base=new TextureRegion(new Texture(Gdx.files.internal("data/Monsters/"+tempFileRead[6])));
		TextureRegion[][] frames=base.split(Integer.parseInt(tempFileRead[7]), Integer.parseInt(tempFileRead[8]));
		Array<TextureRegion> orderedFrames=new Array<TextureRegion>();
		for(int i=0;i<frames.length; i++){
			for(int j=0;j<frames[i].length;j++){
				orderedFrames.add(frames[i][j]);
			}
		}
		attack=new Animation<TextureRegion>(0.1f,orderedFrames,PlayMode.NORMAL);
		dice=new Random();
		type=tempFileRead[9];
		range=Integer.parseInt(tempFileRead[10]);
		
		
	}
	
	//method to calculate damage done to the target
	public int calculateDamage(){
		int damage=0;
		if(critical){
			damage=dice.nextInt(criticalRange)+critDamage;
		}
		else{
			damage=dice.nextInt(damageRange)+minDamage;
		}
		return damage;
	}
	
	//method to apply damage to target if hit
	public void attack(BattleCharacter target){
		int hit=dice.nextInt(20);
		int damage;
		if(hit>=18){
			critical=true;
			damage=calculateDamage();
			critical=false;
		}
		else if(hit+accuracy>target.defence){
			damage=calculateDamage();
		}
		else damage=0;
		target.currenthp=target.currenthp-damage;
		inProgress=true;
		
		currentFrame=attack.getKeyFrame(0);
		act(Gdx.graphics.getDeltaTime());
	}
	
	//update animation
	public boolean act(float delta, BattleScreen current) {
		if (inProgress){
			//draw next frame of the attack animation
			animationTime=animationTime+delta;
			currentFrame=attack.getKeyFrame(animationTime);
			
			if(attack.isAnimationFinished(animationTime)){
				inProgress=false;
				current.activeCharacter.attacking=false;
				current.activeCharacter.currentAttack=null;
				current.toNextAttacker();
				animationTime=0f;
				
			}
			
			
		}
		return false;
	}
	
	public boolean getInProgress(){return inProgress;}
	public void setInProgress(boolean progress){inProgress=progress;}

	@Override
	public boolean act(float delta) {
		// TODO Auto-generated method stub
		return false;
	}

}
