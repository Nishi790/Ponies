package DialogEngine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;

import Main.NPC;

public class DialogBox {
	Skin skin;
	DialogEngine.Dialog current;
	private com.badlogic.gdx.scenes.scene2d.ui.Dialog chat;
	private Stage stage;
	NPC speaker;
	private String prompt;
	TextButton button1;
	TextButton button2;
	Screen screen;
	TextureAtlas atlas;
	
	public DialogBox(Dialog currentDialog, NPC n, Screen s, Batch game){
		setStage(new Stage(new ScalingViewport(Scaling.stretch, 1024, 768),game));
		speaker=n;
		screen=s;
		current=currentDialog;
		atlas=new TextureAtlas(Gdx.files.internal("data/skin/terra-mother-ui.atlas"));
		skin=new Skin(Gdx.files.internal("data/skin/terra-mother-ui.json"),atlas);
		
		setChat(new com.badlogic.gdx.scenes.scene2d.ui.Dialog(n.getName(), skin));
		getChat().getContentTable().add(current.displayPrompt());
		addAvatar();
		generateButtons();
		getChat().pad(50);
		getChat().show(getStage());
		Gdx.input.setInputProcessor(getStage());
	}
	

	private void generateButtons(){
		Table buttons=getChat().getButtonTable();
		int j=0;
		for(int i:current.possibleResponses){
			final int d=j;
			TextButton button=new TextButton(current.displayResponses()[j],skin);
			button.addListener(new ChangeListener(){
				@Override
				public void changed(ChangeEvent event, Actor actor){
					current.nextPrompt(d);
					updateDialog();
				}
			});
			buttons.add(button);
			buttons.row();
			j++;
		}
		buttons.pad(20);
	}
	private void updateDialog(){
		Table text=getChat().getContentTable();
		text.clear();
		getChat().text(current.displayPrompt());
		Table buttons=getChat().getButtonTable();
		buttons.clear();
		generateButtons();
		setPrompt(current.displayPrompt());
		addAvatar();
		
	}
	private void addAvatar(){
		Image temp=new Image(new Texture(Gdx.files.internal("data/Sprites/"+speaker.getName().toLowerCase()+".png")));
		Table avatar=new Table(skin);
		getChat().getContentTable().add(avatar);
		avatar.add(temp);
		avatar.getCell(temp).minHeight(64);
		avatar.getCell(temp).minWidth(64);
		avatar.row();
		avatar.add("Friendship: "+speaker.getCurrentFriendship()+"/100");
	}
	
	public void dispose(){
		getStage().clear();
		Gdx.input.setInputProcessor((InputProcessor) screen);
		getStage().dispose();
	}


	public Stage getStage() {
		return stage;
	}


	public void setStage(Stage stage) {
		this.stage = stage;
	}


	public com.badlogic.gdx.scenes.scene2d.ui.Dialog getChat() {
		return chat;
	}


	public void setChat(com.badlogic.gdx.scenes.scene2d.ui.Dialog chat) {
		this.chat = chat;
	}


	public String getPrompt() {
		return prompt;
	}


	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}
	
}
