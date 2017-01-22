package main;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class OrthoCamera extends OrthographicCamera{
	TiledMapTileLayer main;
	
	public OrthoCamera(TiledMapTileLayer t){
		super();
		main=t;
		
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
