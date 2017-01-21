package inventory;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import main.MainChar;
import main.Sim;

public class Inventory {
	final MainChar main;
	final Sim game;
	Dialog display;
	Stage stage;
	Skin skin;
	
	public Inventory(MainChar m, Sim g, Stage s){
		main=m;
		game=g;
		skin=game.skin;
		stage=s;
		display=new Dialog("Inventory", skin);
		display.pad(20);
		for(Item i:main.getInventory()){
			Table content=display.getContentTable();
			content.add(i.name);
			content.add(i.icon);
			content.row();
		}
		TextButton button=new TextButton("Return to Game", skin);
		button.addListener(new ChangeListener(){

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				dispose();	
			}
			
		});
		display.button(button);
		Gdx.input.setInputProcessor(stage);
	}
	
	public void draw(){
		display.draw(stage.getBatch(), 1);
	}
	
	public void dispose(){
		Gdx.input.setInputProcessor((InputProcessor) game.getScreen());
	}
}
