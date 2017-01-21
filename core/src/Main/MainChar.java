package Main;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

import Inventory.Item;
import Quests.Quest;

public class MainChar {
	Sprite avatar;
	boolean moveLeft, moveRight, moveUp, moveDown;
	int[] stats=new int[]{10,10,10,10};//sleep, hunger, fun, hygiene
	int points=100; //special ability (fly/spells/earth pony???)
	int gold=100;
	ArrayList<NPC> npcsEncountered;
	OrthoCamera camera;
	ArrayList<Quest> activeQuests;
	private ArrayList<Item> inventory;
	
	public MainChar(Sprite av, OrthoCamera camera){
		avatar=av;
		moveLeft=false;
		moveRight=false;
		moveUp=false;
		moveDown=false;
		npcsEncountered=new ArrayList<NPC>();
		this.camera=camera;
		avatar.setCenter(camera.viewportWidth/2, camera.viewportHeight/2);
		avatar.scale(-23/24f);
		activeQuests=new ArrayList<Quest>();
		setInventory(new ArrayList<Item>());
	}
	
	public ArrayList<Quest> getActiveQuests(){
		return activeQuests;
	}
	
	public void draw(Batch b){
		avatar.draw(b);
	}
	
	public void addGold(int a){gold=gold+a;}

	public void setMoveUp(boolean b) {
		moveUp=b;		
	}
	public void setMoveDown(boolean b){
		moveDown=b;
	}
	public void setMoveLeft(boolean b){
		moveLeft=b;
	}
	public void setMoveRight(boolean b){
		moveRight=b;
	}
	
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
	

	public ArrayList<Item> getInventory() {
		return inventory;
	}

	public void setInventory(ArrayList<Item> inventory) {
		this.inventory = inventory;
	}
}
