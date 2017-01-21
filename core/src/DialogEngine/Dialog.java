package dialogEngine;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import inventory.Item;
import main.GameScreen;
import main.MainChar;
import main.NPC;
import quests.Quest;

public class Dialog {
	ArrayList<String[]> prompts;
	ArrayList<String[]> responses;
	int nextprompt=0;
	ArrayList<Integer> possibleResponses;
	final MainChar main;
	private NPC speaker;
	final GameScreen screen;
	
	
	public Dialog(FileHandle f, MainChar main, NPC speaker, GameScreen screen){
		this.main=main;
		this.setSpeaker(speaker);
		this.screen=screen;
		prompts=new ArrayList<String[]>();
		responses=new ArrayList<String[]>();
		String temp=f.extension();
		if(temp.contains("txt")){
			String lines=f.readString();
			String[]all=lines.split(";");
			for(String s: all){
				if(s.contains("Prompt")){
					String[] options=s.split(":");
					prompts.add(options);
				}
				if(s.contains("Response")){
					String[] options=s.split(":");
					responses.add(options);
				}
			}
			possibleResponses=new ArrayList<Integer>();
		}

	}
	//return the NPC's next piece of dialog
	public String displayPrompt(){
		//if you hit goodbye
		if(nextprompt==-1){
			return "close";
		}
		else{
			String[] options=prompts.get(nextprompt);
			String[] temp=options[3].split(",");
			possibleResponses.clear();
			for(int i=0;i<=temp.length-1;i++){
				possibleResponses.add(i,Integer.parseInt(temp[i].trim()));
			}
		return options[1];
		}
	}
	
	//return Array of responses
	public String[] displayResponses(){
		String[] resps;
		if(possibleResponses.size()>=2){
			resps=new String[possibleResponses.size()];
			int index=0;
			for(int i:possibleResponses){
				String[]x=responses.get(i);
				resps[index]=x[1];
				index++;
			}
		}
		else{
			resps=new String[1];
			resps[0]=responses.get(possibleResponses.get(0))[1];
		}
		return resps;
		
	}
	
	//find next prompt, given selected response
	public void nextPrompt(int b){
		String[][]resps=new String[possibleResponses.size()][];
		for(int i=0;i<possibleResponses.size();i++){
			resps[i]=responses.get(possibleResponses.get(i));
		}
		String[] temp=resps[b];
		//check if this response triggers quest creation or adds an item to inventory
		if(temp.length>=4){
			for(int i=3; i<temp.length;i++){
				if(temp[i].contains("Quest")){
					main.getActiveQuests().add(new Quest(Gdx.files.internal("data/Quests/"+temp[i].trim()+".txt"),screen, main));
					getSpeaker().setCurrentTask(getSpeaker().getTaskNumber()+1);
				}
				if(temp[i].contains("Item")){
					main.getInventory().add(new Item(Gdx.files.internal("data/Items/"+temp[i].trim()+".txt")));
					System.out.println(temp[i]+" gained");
				}
				if(temp[i].contains("Reward")){
					for(Quest q:main.getActiveQuests()){
						if(q.getNumber()==Integer.parseInt(temp[i].replace("Reward", ""))){
							main.addGold(q.getGoldReward());
							speaker.setCurrentFriendship(speaker.getCurrentFriendship()+q.getFriendshipReward());
							if(!q.getItemReward().isEmpty()){
								for(Item item:q.getItemReward()){
									main.getInventory().add(item);
								
								}
							}
							speaker.setCurrentTask(speaker.getTaskNumber()+1);
						}
					}
				}
			}
		}
		
		int index=Integer.parseInt(temp[2].trim());
		nextprompt=index-1;
	}
	
	public NPC getSpeaker() {
		return speaker;
	}
	public void setSpeaker(NPC speaker) {
		this.speaker = speaker;
	}
}
