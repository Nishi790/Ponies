package battleEngine;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;

import main.MainChar;

public class BattleCharacter extends Actor implements Comparable<BattleCharacter> {
	
	String name;
	int maxhp,maxmp;
	double currenthp, currentmp;
	ArrayList<Attack> moves;
	Sprite restingState;
	FileHandle data;
	Random generator;
	int initiative;
	int speed;
	int defence=10;
	Attack currentAttack=null;
	boolean attacking=false;
	boolean defeated=false;
	
	
	public BattleCharacter(FileHandle f){
		data=f;
		String[] info=f.readString().split(";");
		name=info[0];
		restingState=new Sprite(new Texture(Gdx.files.internal("data/Monsters/"+info[1])));
		restingState.setSize(Float.parseFloat(info[2]), Float.parseFloat(info[3]));
		this.setPosition(Float.parseFloat(info[4]), Float.parseFloat(info[5]));
		generator=new Random();
		//generates random hp value between the given min and max values
		maxhp=generator.nextInt(Integer.parseInt(info[7])-Integer.parseInt(info[6]))+Integer.parseInt(info[6]); 
		//generates random mp total between the given min and max values
		if(Integer.parseInt(info[9])-Integer.parseInt(info[8])>0){
			maxmp=generator.nextInt()+Integer.parseInt(info[8]);
		}
		else maxmp=0;
		currenthp=maxhp;
		currentmp=maxmp;
		initiative=generator.nextInt(21);
		moves=new ArrayList<Attack>();
		for(int i=10; i<info.length;i++){
			if(info[i].contains("Attack")){
				Attack a=new Attack(Gdx.files.internal("data/Monsters/"+info[i]), this);
				moves.add(a);
			}
			else if (info[i].contains("Spell")){
				Spell s=new Spell(Gdx.files.internal("data/Monsters/"+info[i]), this);
				moves.add(s);
			}
		}
	}
	
	public BattleCharacter(MainChar main, int index){
		name=main.name;
		maxhp=100;
		maxmp=100;
		currenthp=main.getPoints()[0];
		currentmp=main.getPoints()[1];
		restingState=new Sprite(main.getAvatar());
		restingState.setSize(96,96);
		restingState.setPosition(0,100*index);
		moves=main.getMoves();
		
		//temporary move
		Attack temp=new Attack(Gdx.files.internal("data/Monsters/Attack2.txt"), this);
		moves.add(temp);
		
		speed=5;
		Random r=new Random();
		initiative=r.nextInt(21)+speed;
		
	}
	
	public void draw(Batch b){
		if(attacking){
			b.draw(currentAttack.currentFrame,this.getX(),this.getY(), restingState.getWidth(),restingState.getHeight());
		}
		else{ 
			restingState.setPosition(this.getX(), this.getY());
			restingState.draw(b);
		}

	}


	

	@Override
	public int compareTo(BattleCharacter o) {
		if(this.initiative>o.initiative) return 1;
		else if(this.initiative<o.initiative) return -1;
		else return 0;
	}

	public ArrayList<Attack> getMoves() {
		return moves;
		
	}
	
	

}
