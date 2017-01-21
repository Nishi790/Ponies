package Quests;

import java.util.ArrayList;

import com.badlogic.gdx.files.FileHandle;

import Inventory.Item;
import Main.GameScreen;
import Main.NPC;

public class Quest {
	private String description;
	boolean active;
	private boolean complete;
	int number;
	int friendshipReward;
	ArrayList<Item> itemReward;
	int goldReward;
	FileHandle reference;
	NPC target;
	boolean talkedTo;
	Item needed;
	boolean inInventory;
	final GameScreen screen;
	NPC questgiver;
	
	public Quest(FileHandle ref, GameScreen screen){
		this.screen=screen;
		reference=ref;
		active=true;
		setComplete(false);
		parseRef();
	}
	
	private void parseRef(){
		String ref=reference.readString();
		String[]temp=ref.split(":");
		number=Integer.parseInt(temp[0]);
		setDescription(temp[1]);
		friendshipReward=Integer.parseInt(temp[2]);
		goldReward=Integer.parseInt(temp[3]);
		if(temp[4].contains("Talk")){
			for(NPC n:screen.npcs){
				if(n.getName().contains(temp[5].trim())) target=n;
			}
			talkedTo=false;
			target.setCurrentTask(target.getTaskNumber()+1);
		}
		else talkedTo=true;
		if(temp[4].contains("Get")){
			//
		}
		else inInventory=true;
		for(NPC n: screen.npcs){
			System.out.println(temp[6].trim());
			System.out.println(n.getName());
			if(n.getName().contains(temp[6].trim()))questgiver=n;
		}
	}
	
	public void checkDialog(){
		System.out.println("current speaker "+screen.getCurrentDialog().getSpeaker().getName());
		System.out.println("target "+target.getName());
		if(screen.getCurrentDialog().getSpeaker().getName()==target.getName()){
			talkedTo=true;
		}
	}
	
	public void checkComplete(){
		if(talkedTo && inInventory){
			setComplete(true);
			questgiver.setCurrentTask(questgiver.getTaskNumber()+1);
		}
		
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}
	
	
}
