package main;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class OrthoCamera extends OrthographicCamera{
	boolean moveup, movedown, moveleft, moveright;
	TiledMap current;
	TiledMapTileLayer main;
	
	
	public OrthoCamera(TiledMap map){
		super();
		current=map;
		main=(TiledMapTileLayer) current.getLayers().get(0);
		moveup=false;
		movedown=false; 
		moveleft=false;
		moveright=false;
	}
	public void move(){
		if(moveup)translate(0,1);
		if(movedown)translate(0,-1);
		if(moveleft)translate(-1,0);
		if(moveright)translate(1,0);
	}
	
	public void setMoveDown(boolean b){
		movedown=b;
	}
	public void setMoveUp(boolean b){
		moveup=b;
	}
	public void setMoveLeft(boolean b){
		moveleft=b;
	}
	public void setMoveRight(boolean b){
		moveright=b;
	}
	public void clamp(){
		if(this.position.x<viewportWidth/2){
			position.x=this.viewportWidth/2;
		}
		if(position.x>main.getWidth()-this.viewportWidth/2){
			position.x=main.getWidth()-this.viewportWidth/2;
		}
		if(position.y>main.getHeight()-viewportHeight/2){
			position.y=main.getHeight()-viewportHeight/2;
		}
		if(position.y<viewportHeight/2){
			position.y=viewportHeight/2;
		}
	}

}
