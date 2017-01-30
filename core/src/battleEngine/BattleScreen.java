package battleEngine;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import main.MainChar;
import main.Sim;

public class BattleScreen implements Screen, InputProcessor{

	final Sim game;
	final MainChar main;
	Stage stage;
	ArrayList<BattleCharacter> party;
	ArrayList<Monster> monsters;
	Window background;
	
	public BattleScreen(Sim g, MainChar m, String setting, Monster mon){
		game=g;
		main=m;
		stage=new Stage();
		background=new Window("battle", new Window.WindowStyle(game.skin.getFont("font"),Color.BLACK,new TextureRegionDrawable(
				new TextureRegion(new Texture(Gdx.files.internal("data/Backgrounds/"+setting+".png"))))));
		background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		stage.addActor(background);
		party=new ArrayList<BattleCharacter>();
		party.add(new BattleCharacter(main,0));
		for(int i=0; i<main.getPartyMembers().length;i++){
			if(main.getPartyMembers()[i]!=null){
				party.add(new BattleCharacter(main.getPartyMembers()[i],i+1));
			}
			
		}
		monsters=new ArrayList<Monster>();
		monsters.add(mon);
		for(BattleCharacter b:party){
			stage.addActor(b);
		}
		for(Monster s:monsters){
			stage.addActor(s);
		}
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		//clear screen
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		stage.draw();
		
		game.batch.begin();
		for(BattleCharacter b:party){
			b.draw(game.batch);
		}
		for(Monster s:monsters){
			s.draw(game.batch);
		}
		game.batch.end();

		
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
}
