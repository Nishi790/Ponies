package interactable;

import com.badlogic.gdx.math.Rectangle;

import main.MainChar;
import main.Sim;

public class Entrance {
	Building enteree;
	private Rectangle entrancearea;
	
	public Entrance(Building b, Rectangle r){
		enteree=b;
		setEntrance(r);
	}
	
	public void enter(MainChar main, Sim game){
		enteree.enter(main, game);
	}

	public Rectangle getEntrance() {
		return entrancearea;
	}

	public void setEntrance(Rectangle entrancearea) {
		this.entrancearea = entrancearea;
	}
}
