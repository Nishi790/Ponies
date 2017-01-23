package main;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

import inventory.Item;
import quests.Quest;

public class MainChar {
	Sprite avatar;
	boolean moveLeft, moveRight, moveUp, moveDown;
	private double[] stats=new double[]{10,10,10,10};//sleep, hunger, fun, hygiene
	private double[] points=new double[]{100,100,100}; //special ability (fly/spells/earth pony???);HP;MP
	private int gold=100;
	OrthoCamera camera;
	ArrayList<Quest> activeQuests;
	private ArrayList<Item> inventory;
	
	public MainChar(Sprite av, OrthoCamera camera){
		avatar=av;
		moveLeft=false;
		moveRight=false;
		moveUp=false;
		moveDown=false;
		this.camera=camera;
		avatar.setSize(2, 2);
		avatar.setCenter(camera.viewportWidth/2, camera.viewportHeight/2);
		activeQuests=new ArrayList<Quest>();
		inventory=(new ArrayList<Item>());
	}
	
	//wrapper for draw function
	public void draw(Batch b){avatar.draw(b);}
		
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
	}
	

}
