package Quests;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import Inventory.Item;
import Main.GameScreen;
import Main.MainChar;
import Main.NPC;

public class Quest {
	private String description;
	boolean active;
	private boolean complete;
	private int number;
	private int friendshipReward;
	private ArrayList<Item> itemReward;
	private int goldReward;
	FileHandle reference;
	NPC target;
	boolean talkedTo;
	Item needed;
	boolean inInventory;
	boolean given;
	NPC recipient;
	Item toBeGiven;
	final GameScreen screen;
	NPC questgiver;
	final MainChar main;
	
	public Quest(FileHandle ref, GameScreen screen, MainChar m){
		this.screen=screen;
		main=m;
		reference=ref;
		active=true;
		itemReward=new ArrayList<Item>();
		setComplete(false);
		parseRef();
		System.out.println(questgiver.getName()+" - "+description);
	}
	
	private void parseRef(){
		String ref=reference.readString();
		String[]temp=ref.split(":");
		setNumber(Integer.parseInt(temp[0]));
		setDescription(temp[1]);
		setFriendshipReward(Integer.parseInt(temp[2]));
		setGoldReward(Integer.parseInt(temp[3]));
		//if talk to NPC quest
		if(temp[4].contains("Talk")){
			for(NPC n:screen.npcs){
				if(n.getName().contains(temp[5].trim())) target=n;
				if(n.getName().contains(temp[6].trim()))questgiver=n;
			}
			talkedTo=false;
			target.setCurrentTask(target.getTaskNumber()+1);
			
		}
		else talkedTo=true;
		//if find object quest
		if(temp[4].contains("Get")){
			//
		}
		//if Giving quest
		if(temp[4].contains("Give")){
			for(NPC n:screen.npcs){
				if(n.getName().contains(temp[5].trim()))recipient=n;
				if(n.getName().contains(temp[7].trim()))questgiver=n;
			}
			toBeGiven=new Item(Gdx.files.internal("data/Items/Item"+temp[6].trim()+".txt"));
			main.getInventory().add(toBeGiven);
			recipient.setCurrentTask(recipient.getTaskNumber()+1);
		}
		else given=true;
		
	}
	
	public void checkDialog(){
		if(!talkedTo){
			if(screen.getCurrentDialog().getSpeaker().getName().contains(target.getName())){
				talkedTo=true;
			}
		}
	}
	
	public void give(){
		if(!given){
			if(screen.getCurrentDialog().getSpeaker().getName().contains(recipient.getName())){
				main.getInventory().remove(toBeGiven);
				given=true;
			}
		}
		
	}
	
	public void checkComplete(){
		checkDialog();
		give();
		if(talkedTo||given){
			setComplete(true);
			questgiver.setCurrentTask(questgiver.getTaskNumber()+1);
			System.out.println(questgiver.getName()+"'s quest complete");
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

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getGoldReward() {
		return goldReward;
	}

	public void setGoldReward(int goldReward) {
		this.goldReward = goldReward;
	}

	public int getFriendshipReward() {
		return friendshipReward;
	}

	public void setFriendshipReward(int friendshipReward) {
		this.friendshipReward = friendshipReward;
	}

	public ArrayList<Item> getItemReward() {
		return itemReward;
	}

	public void setItemReward(ArrayList<Item> itemReward) {
		this.itemReward = itemReward;
	}
	
	
}
