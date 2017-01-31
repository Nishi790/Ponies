package interactable;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import main.InteriorScreen;
import main.MainChar;
import main.Sim;

public class Building {
	TiledMap inside;
	FileHandle file;
	ArrayList<String[]> furniture;
	
	public Building(FileHandle info){
		file=info;
		
		//parse file
		String inf=file.readString();
		String[] temp=inf.split(";");
		inside=new TmxMapLoader().load("data/Maps/"+temp[0]);//interior map
		
		//generate inside furniture
		furniture=new ArrayList<String[]>();
		for(int i=1; i<temp.length; i=i+3){
			furniture.add(new String[]{"data/Furniture/"+temp[i], temp[i+1], temp[i+2]});
		}
	}
	
	//enter the building
	public void enter(MainChar main, Sim game){
		game.setScreen(new InteriorScreen(game, main, inside, furniture));
	}
	
	
	
}
