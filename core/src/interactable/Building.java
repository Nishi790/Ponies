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
	private final Sprite sprite;
	TiledMap inside;
	FileHandle file;
	ArrayList<String> furniture;
	
	public Building(FileHandle info){
		file=info;
		String inf=file.readString();
		String[] temp=inf.split(";");
		sprite=new Sprite(new Texture(Gdx.files.internal("data/Buildings/"+temp[0])));
		getSprite().setPosition(Integer.parseInt(temp[1]), Integer.parseInt(temp[2]));//x,y position
		getSprite().scale(-15/16f);
		inside=new TmxMapLoader().load("data/Maps/"+temp[3]);//interior map
		furniture=new ArrayList<String>();
		for(int i=4; i<temp.length; i++){
			furniture.add("data/Furniture/"+temp[i]);
		}
	}
	
	public void enter(MainChar main, Sim game){
		game.setScreen(new InteriorScreen(game, main, inside, furniture));
	}
	

	public Sprite getSprite() {
		return sprite;
	}
	
	
}
