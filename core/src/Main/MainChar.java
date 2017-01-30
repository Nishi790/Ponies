package main;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;

import inventory.Item;
import quests.Quest;

public class MainChar {
	public String name;
	Sprite avatar;
	Animation<TextureRegion>[] movement;
	boolean moveLeft, moveRight, moveUp, moveDown;
	String lastDir;
	private double[] stats=new double[]{50,50,50,50};//sleep, hunger, fun, hygiene
	private double[] points=new double[]{100,100,100}; //special ability (fly/spells/earth pony???);HP;MP
	private int gold=100;
	int minDamage;
	int maxDamage;
	int accuracy;
	ArrayList<Quest> activeQuests;
	private ArrayList<Item> inventory;
	ArrayList<Action> moves;
	private MainChar[] partyMembers;
	
	float time=0;
	
	
	public MainChar(Texture av, String n){
		partyMembers=new MainChar[2];
		name=n;
		avatar=new Sprite(new TextureRegion(av,128,0,128,128));
		TextureRegion[][] all=TextureRegion.split(av, 128, 128);
		TextureRegion[] left=new TextureRegion[12];
		for(int i=0; i<left.length;i++){
			left[i]=all[i][0];
		}
		TextureRegion[] right=new TextureRegion[12];
		for(int i=0; i<right.length;i++){
			right[i]=all[i][1];
		}
		TextureRegion[] down=new TextureRegion[8];
		for(int i=0;i<down.length;i++){
			down[i]=all[i][2];
		}
		TextureRegion[] up=new TextureRegion[8];
		for(int i=0;i<up.length;i++){
			up[i]=all[i][3];
		}
		movement=new Animation[]{new Animation<TextureRegion>(0.05f, left),
			new Animation<TextureRegion>(0.05f,right), new Animation<TextureRegion>(0.075f,up), new Animation<TextureRegion>(0.075f, down)};
		for(Animation<TextureRegion> s:movement){
			s.setPlayMode(PlayMode.LOOP);
		}
		moveLeft=false;
		moveRight=false;
		moveUp=false;
		moveDown=false;
		avatar.setSize(2, 2);
		avatar.setCenter(20,5);
		activeQuests=new ArrayList<Quest>();
		inventory=(new ArrayList<Item>());
		accuracy=70;
		minDamage=5;
		maxDamage=15;
		moves=new ArrayList<Action>();
		
	}
	
	
	//wrapper for draw function
	public void draw(Batch b){
		if(moveLeft){
			TextureRegion s=movement[0].getKeyFrame(time);
			b.draw(s, avatar.getX(), avatar.getY(), avatar.getWidth(), avatar.getHeight());
		}
		else if(moveRight){
			TextureRegion s=movement[1].getKeyFrame(time);
			b.draw(s, avatar.getX(), avatar.getY(), avatar.getWidth(), avatar.getHeight());
		}
		else if(moveUp){
			TextureRegion s=movement[2].getKeyFrame(time);
			b.draw(s, avatar.getX(), avatar.getY(), avatar.getWidth(), avatar.getHeight());
		}
		else if(moveDown){
			TextureRegion s=movement[3].getKeyFrame(time);
			b.draw(s, avatar.getX(), avatar.getY(), avatar.getWidth(), avatar.getHeight());
		}
		else{
			if (lastDir=="right"){
				TextureRegion s=movement[1].getKeyFrame(0);
				b.draw(s, avatar.getX(), avatar.getY(), avatar.getWidth(), avatar.getHeight());
			}
			else if (lastDir=="up"){
				TextureRegion s=movement[2].getKeyFrame(0);
				b.draw(s, avatar.getX(), avatar.getY(), avatar.getWidth(), avatar.getHeight());
			}
			else if(lastDir=="down"){
				TextureRegion s=movement[3].getKeyFrame(0);
				b.draw(s, avatar.getX(), avatar.getY(), avatar.getWidth(), avatar.getHeight());
			}
			else if(lastDir=="left"){
				TextureRegion s=movement[0].getKeyFrame(0);
				b.draw(s, avatar.getX(), avatar.getY(), avatar.getWidth(), avatar.getHeight());
			}
			else avatar.draw(b);
		}
	}
		
	//Getters and Setters as needed
	
	public ArrayList<Quest> getActiveQuests(){return activeQuests;}

	public ArrayList<Item> getInventory() {return inventory;}

	public double[] getStats() {return stats;}
	
	public void setStats(int stat, double value){stats[stat]=value;}

	public double[] getPoints() {return points;}

	public void setPoints(int stat, double value) {points[stat]=value;}

	public int getGold() {return gold;}

	//add gold to current total, use negative int to subtract gold
	public void addGold(int a){gold=(getGold()+a);}

	public Sprite getAvatar() {return avatar;}
	
	public void setMoveUp(boolean b) {moveUp=b;}

	public void setMoveDown(boolean b){moveDown=b;}

	public void setMoveLeft(boolean b){moveLeft=b;}
	
	public void setMoveRight(boolean b){moveRight=b;}
	
	public void setHP(double i){points[0]=i;}
	
	public double getHP(){return points[0];}
	
	public ArrayList<Action> getMoves(){
		return moves;
	}
	
	
	public void setLastDir(){
		if(moveLeft){
			lastDir="left";
		}
		else if(moveRight){
			lastDir="right";
		}
		else if(moveUp){
			lastDir="up";
		}
		else if(moveDown){
			lastDir="down";
		}
	}
	//move player based on current direction of movement and time since last frame
	public void updatePos(){
		if(moveUp) {
			avatar.setY(avatar.getY()+10*Gdx.graphics.getDeltaTime());
		}
		if(moveDown){
			avatar.setY(avatar.getY()-10*Gdx.graphics.getDeltaTime());
		}
		if(moveLeft) {
			avatar.setX(avatar.getX()-10*Gdx.graphics.getDeltaTime());
		}
		if(moveRight) {
			avatar.setX(avatar.getX()+10*Gdx.graphics.getDeltaTime());
		}
		setLastDir();
		time=time+Gdx.graphics.getDeltaTime();
	}


	public MainChar[] getPartyMembers() {
		return partyMembers;
	}


	public void setPartyMembers(MainChar[] partyMembers) {
		this.partyMembers = partyMembers;
	}
	

}
