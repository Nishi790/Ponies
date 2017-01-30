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
	ArrayList<String[]> furniture;
	
	public Building(FileHandle info){
		file=info;
		
		//parse file
		String inf=file.readString();
		String[] temp=inf.split(";");
		sprite=new Sprite(new Texture(Gdx.files.internal("data/Buildings/"+temp[0])));//image of building
		getSprite().setPosition(Integer.parseInt(temp[1]), Integer.parseInt(temp[2]));//x,y position
		getSprite().setSize(sprite.getWidth()/16, sprite.getHeight()/16);//scale building
		inside=new TmxMapLoader().load("data/Maps/"+temp[3]);//interior map
		
		//generate inside furniture
		furniture=new ArrayList<String[]>();
		for(int i=4; i<temp.length; i=i+3){
			furniture.add(new String[]{"data/Furniture/"+temp[i], temp[i+1], temp[i+2]});
		}
	}
	
	//enter the building
	public void enter(MainChar main, Sim game){
		game.setScreen(new InteriorScreen(game, main, inside, furniture));
	}
	

	public Sprite getSprite() {
		return sprite;
	}
	
	
}
