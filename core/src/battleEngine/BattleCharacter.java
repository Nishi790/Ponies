package battleEngine;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;

import main.LoadScript;
import main.MainChar;

public class BattleCharacter extends Actor implements Comparable<BattleCharacter> {
	
	String name;
	int maxhp,maxmp;
	double currenthp, currentmp;
	ArrayList<Attack> moves;
	Sprite restingState;
	Random generator;
	int initiative;
	int speed;
	int defence;
	int[] size;
	Attack currentAttack=null;
	boolean attacking=false;
	boolean defeated=false;
	BitmapFont labeller;
	private LoadScript script;
	
	
	public BattleCharacter(String name, int x, int y){
		this.name=name;
		moves=new ArrayList<Attack>();
		script=new LoadScript(name+".lua", this);
		script.executeInit();
		setXpos(x);
		setYpos(y);
		currenthp=maxhp;
		currentmp=maxmp;
		generator=new Random();
		labeller=new BitmapFont();
		initiative=generator.nextInt(21)+speed;
	}
	
	public void setMaxHp(int hp){maxhp=hp;}
	public void setMaxMp(int mp){maxmp=mp;}
	public void setCurrentHp(float hp){currenthp=hp;}
	public void setCurrentMp(float mp){currentmp=mp;}
	public void setDefence(int def){defence=def;}
	public void setSpeed(int spd){speed=spd;}
	public void setsize(int width, int height){
		size=new int[]{width,height};
		restingState.setSize(width, height);
		this.setSize(width, height);
	}
	public void setRestingState(String filename){
		restingState=new Sprite(new Texture(Gdx.files.internal("data/"+filename)));
	}
	public void setXpos(float x){
		restingState.setX(x);
		this.setX(x);
		}
	public void setYpos(float y){
		restingState.setY(y);
		this.setY(y);
		}
	public void createAttack(String file, BattleCharacter b){
		moves.add(new Attack(Gdx.files.internal("data/Monsters/"+file), b));
	}
	
	public String getname(){return name;}
	
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
		generator=new Random();
		initiative=generator.nextInt(21)+speed;
		labeller=new BitmapFont();
		
	}
	
	public void draw(Batch b){
		if(attacking){
			b.draw(currentAttack.currentFrame,this.getX(),this.getY(), restingState.getWidth(),restingState.getHeight());
			labeller.draw(b, currenthp+"/"+maxhp+" HP", this.getX(),this.getY()+restingState.getHeight()+5);
		}
		else{ 
			restingState.setPosition(this.getX(), this.getY());
			restingState.draw(b);
			labeller.draw(b, currenthp+"/"+maxhp+" HP", this.getX(),this.getY()+restingState.getHeight()+5);
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
