package main;

import java.io.FileNotFoundException;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class NPC {
	Sprite avatar;
	int maxFriendship;
	private int currentFriendship;
	int xPos, yPos;
	String data;
	FileHandle tasks;
	int currentTask;
	String[] taskList;
	String dialog;
	private String name;
	final MainChar main;
	final GameScreen screen;
	
	public NPC(FileHandle f, MainChar main, GameScreen screen) throws FileNotFoundException{
		this.main=main;
		this.screen=screen;
		maxFriendship=100;
		data=f.readString();
		parseData();
		currentTask=0;
		taskList=readTasks();
		setName(f.nameWithoutExtension());
	}
	
	public int getTaskNumber(){return currentTask;}
	public void setCurrentTask(int a){currentTask=a;}
	
	//parse the data file providing the name of the task file, the position, avatar, and starting friendship levels
	public void parseData() throws FileNotFoundException{
		String[]input=data.split(";");
		String taskfile=input[0];
		tasks=Gdx.files.internal("data/Tasks/"+taskfile);
		xPos=Integer.parseInt(input[1].trim());
		yPos=Integer.parseInt(input[2].trim());
		Texture sprite=new Texture(Gdx.files.internal("data/Sprites/"+input[3].trim()));
		avatar=new Sprite(sprite);
		avatar.setCenter(xPos, yPos);
		avatar.scale(-23/24f);
		setCurrentFriendship(Integer.parseInt(input[4].trim()));
		
	}
	public String[] readTasks(){
		String encounters=tasks.readString();
		String[] list=encounters.split(";");	
		return list;
	}
	public dialogEngine.Dialog getCurrentDialog(){
		String task=taskList[currentTask];
		dialogEngine.Dialog talk=new dialogEngine.Dialog(Gdx.files.internal("data/Dialog/"+task), main, this,screen);
		return talk;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCurrentFriendship() {
		return currentFriendship;
	}

	public void setCurrentFriendship(int currentFriendship) {
		this.currentFriendship = currentFriendship;
	}
	
}
