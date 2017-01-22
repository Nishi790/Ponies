package interactable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import main.InteriorScreen;

public class Furniture {

	private Sprite image;
	int stat;
	int improvement;
	InteriorScreen location;
	String message;
	String name;
	int cost;
	private Stage stage;
	
	public Furniture(InteriorScreen loc, FileHandle f){
		setStage(new Stage());
		location=loc;
		String[] info=f.readString().split(":");
		name=info[0];
		stat=Integer.parseInt(info[1]);
		improvement=Integer.parseInt(info[2]);
		setImage(new Sprite(new Texture(Gdx.files.internal("data/Furniture/"+info[3]))));
		message=info[4];
		cost=Integer.parseInt(info[5]);
		image.setPosition(Float.parseFloat(info[6]),Float.parseFloat(info[7]));
		image.setSize(image.getWidth()/32,image.getHeight()/32);
	}
	
	public void interactWith(){
		final Dialog display=new Dialog(name,location.game.skin);
		display.pad(50);
		display.text(message);
		TextButton button=new TextButton("Yes", location.game.skin);
		button.addListener(new ChangeListener(){

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				double relevant=location.getMain().getStats()[stat];
				if(relevant+improvement>10){
					relevant=10;
				}
				else {
					relevant=relevant+improvement;
				}
				location.getMain().setStats(stat, relevant);
				location.getMain().addGold(-cost);
				Gdx.input.setInputProcessor(location);
				location.setInteractee(null);
				location.setAlert(false);
				display.hide();
				
			}
			
		});
		display.button(button);
		button=new TextButton("No", location.game.skin);
		button.addListener(new ChangeListener(){

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Gdx.input.setInputProcessor(location);
				location.setInteractee(null);
				location.setAlert(false);
				display.hide();
				
			}
			
		});
		display.button(button);
		Gdx.input.setInputProcessor(getStage());
		display.show(getStage());
		
	}

	public Sprite getImage() {
		return image;
	}

	public void setImage(Sprite image) {
		this.image = image;
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}
	
}
