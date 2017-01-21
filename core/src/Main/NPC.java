package Main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Scanner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;

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
	
	public NPC(String data, FileHandle f, MainChar main, GameScreen screen) throws FileNotFoundException{
		this.main=main;
		this.screen=screen;
		maxFriendship=100;
		this.data=data;
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
	public DialogEngine.Dialog getCurrentDialog(){
		String task=taskList[currentTask];
		DialogEngine.Dialog talk=new DialogEngine.Dialog(Gdx.files.internal("data/Dialog/"+task), main, this,screen);
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
