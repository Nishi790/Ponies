package inventory;

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
	private Dialog display;
	private Stage stage;
	Skin skin;
	
	public Inventory(MainChar m, Sim g){
		main=m;
		game=g;
		skin=game.skin;
		setStage(new Stage());
		setDisplay(new Dialog("Inventory", skin));
		for(int i=0;i<main.getInventory().size();i++){
			Table content=getDisplay().getContentTable();
			Item temp=main.getInventory().get(i);
			content.add(temp.name);
			content.add(temp.icon);
			content.row();
		}
		display.padTop(50);
		display.padLeft(20);
		display.padRight(20);
		TextButton button=new TextButton("Return to Game", skin);
		button.addListener(new ChangeListener(){

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				dispose();	
			}
			
		});
		getDisplay().button(button);
		Gdx.input.setInputProcessor(getStage());
	}
	
	public void draw(){
		getDisplay().draw(getStage().getBatch(), 1);
	}
	
	public void dispose(){
		Gdx.input.setInputProcessor((InputProcessor) game.getScreen());
		stage.dispose();
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public Dialog getDisplay() {
		return display;
	}

	public void setDisplay(Dialog display) {
		this.display = display;
	}
}
