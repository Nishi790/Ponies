package Main;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import DialogEngine.DialogBox;
import Inventory.Item;
import Quests.Quest;

public class GameScreen implements Screen, InputProcessor {
	final Sim game;
	final MainChar main;
	OrthoCamera camera;
	ScreenViewport view;
	TiledMap current;
	TiledMapTileLayer base;
	OrthogonalTiledMapRenderer tiledMapRenderer;
	com.badlogic.gdx.files.FileHandle[] npcData;
	public ArrayList<NPC> npcs;
	private DialogEngine.Dialog currentDialog;
	public boolean inDialog=false;
	DialogBox display;
	BitmapFont font;
	
	public GameScreen(final Sim game, Sprite pony){
		this.game=game;
		//load map
		current=new TmxMapLoader().load("data/Maps/map.tmx");
		float scale=1/16f;
		tiledMapRenderer = new OrthogonalTiledMapRenderer(current,scale);
		base=(TiledMapTileLayer)current.getLayers().get(0);
		//set up camera
		camera=new OrthoCamera(current);
		camera.setToOrtho(false,25,25);
		view=new ScreenViewport();
		view.setUnitsPerPixel(1/16);
		view.setScreenSize(25,25);
		//create characters
		main=new MainChar(pony, camera);
		npcs=new ArrayList<NPC>();
		npcData=new com.badlogic.gdx.files.FileHandle[]{Gdx.files.internal("data/NPCs/Pinkie.txt"), 
				Gdx.files.internal("data/NPCs/Spike.txt"), Gdx.files.internal("data/NPCs/Twilight.txt")};
		for(com.badlogic.gdx.files.FileHandle f: npcData){
			try {
				NPC n=new NPC(f.readString(),f, main, this);
				npcs.add(n);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
		//create input processor
		Gdx.input.setInputProcessor(this);
	}
	
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		//clear screen
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		//update necessary things
		main.updateQuests();
		main.updatePos();
		block();
		camera.position.set(main.avatar.getX()+main.avatar.getWidth()/2, main.avatar.getY()+main.avatar.getHeight()/2, 0);
		camera.clamp();
		camera.update();
		
		//render map
		tiledMapRenderer.setView(camera);
		tiledMapRenderer.render();
		
		//render in Camera's coordinate system
		game.batch.setProjectionMatrix(camera.combined);
		
		//actual drawing
		game.batch.begin();
		main.draw(game.batch);
		if (!npcs.isEmpty()){
			for(NPC n: npcs){
				n.avatar.draw(game.batch);
			}
		}
		
		game.batch.end();
		if(inDialog){
			display.getChat().show(display.getStage());
			if(display.getPrompt()=="close"){
				for(Quest q: main.getActiveQuests()){
					q.checkDialog();
				}
				display.dispose();
				display=null;
				
				inDialog=false;
			}
			else{
				display.getStage().draw();
			}
		}
		
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void hide() {
		
	}

	@Override
	public void dispose() {
		current.dispose();
		tiledMapRenderer.dispose();
	}


	@Override
	public boolean keyDown(int keycode) {
		switch(keycode){
		case Input.Keys.UP: 
			main.setMoveUp(true);
			main.setMoveDown(false);
			break;
		case Input.Keys.DOWN:
			main.setMoveDown(true);
			main.setMoveUp(false);
			break;
		case Input.Keys.LEFT: 
			main.setMoveLeft(true);
			main.setMoveRight(false);
			break;
		case Input.Keys.RIGHT: 
			main.setMoveRight(true);
			main.setMoveLeft(false);
			break;
		case Input.Keys.ESCAPE:
			dispose();
			game.dispose();
			break;
		}
		return false;
	}


	@Override
	public boolean keyUp(int keycode) {
		switch(keycode){
		case Input.Keys.UP:
			main.setMoveUp(false);
		case Input.Keys.DOWN:
			main.setMoveDown(false);
		case Input.Keys.LEFT:
			main.setMoveLeft(false);
		case Input.Keys.RIGHT:
			main.setMoveRight(false);
		}
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
	
	public void block(){
		for(NPC n: npcs){
			if(main.avatar.getBoundingRectangle().overlaps(n.avatar.getBoundingRectangle())){
				if(main.moveDown){
					main.setMoveDown(false);
					main.avatar.setY(main.avatar.getY()+10*Gdx.graphics.getDeltaTime());
					
				}
				if(main.moveUp){
					main.setMoveUp(false);
					main.avatar.setY(main.avatar.getY()-10*Gdx.graphics.getDeltaTime());
					
				}
				if(main.moveLeft){
					main.setMoveLeft(false);
					main.avatar.setX(main.avatar.getX()+10*Gdx.graphics.getDeltaTime());
				}
				if(main.moveRight){
					main.setMoveRight(false);
					main.avatar.setX(main.avatar.getX()-10*Gdx.graphics.getDeltaTime());
				}
				setCurrentDialog(n.getCurrentDialog());
				inDialog=true;
				display=new DialogBox(getCurrentDialog(), getCurrentDialog().getSpeaker(), this, game.batch);
			}	
		}
	}


	public DialogEngine.Dialog getCurrentDialog() {
		return currentDialog;
	}


	public void setCurrentDialog(DialogEngine.Dialog currentDialog) {
		this.currentDialog = currentDialog;
	}

}
