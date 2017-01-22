package main;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import dialogEngine.DialogBox;
import interactable.Building;
import inventory.Inventory;

public class GameScreen implements Screen, InputProcessor {
	final Sim game;
	final MainChar main;
	int screenHeight;
	int screenWidth;
	OrthoCamera camera;
	TiledMap current;
	TiledMapTileLayer base;
	OrthogonalTiledMapRenderer tiledMapRenderer;
	com.badlogic.gdx.files.FileHandle[] npcData;
	public ArrayList<NPC> npcs;
	private dialogEngine.Dialog currentDialog;
	public boolean inDialog=false;
	Inventory invi;
	DialogBox display;
	BitmapFont font;
	HUD hud;
	ArrayList<Building> buildings;
	
	
	public GameScreen(final Sim game, Sprite pony){
		this.game=game;
		screenHeight=Gdx.graphics.getHeight();
		screenWidth=Gdx.graphics.getWidth();
		
		//load map
		current=new TmxMapLoader().load("data/Maps/map.tmx");
		buildings=new ArrayList<Building>();
		String[] temporary=Gdx.files.internal("data/Maps/mapBuildings.txt").readString().split(":");
		for(String s:temporary){
			Building b=new Building(Gdx.files.internal("data/Buildings/"+s));
			buildings.add(b);
		}
		float scale=1/16f;
		tiledMapRenderer = new OrthogonalTiledMapRenderer(current,scale);
		base=(TiledMapTileLayer)current.getLayers().get(0);
		
		//set up camera
		camera=new OrthoCamera(base);
		camera.setToOrtho(false,screenWidth/32,screenHeight/32);
		camera.position.set(camera.viewportWidth/2f, camera.viewportHeight/2f, 0);
		camera.update();
		
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
		hud=new HUD(main,this.game, this);
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
		
		//update necessary things
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
		if(!buildings.isEmpty()){
			for(Building b: buildings){
				b.getSprite().draw(game.batch);
			}
		}
		
		game.batch.end();
		if(inDialog){
			display.getChat().show(display.getStage());
			if(display.getPrompt()=="close"){
				int j=main.getActiveQuests().size();
				for(int i=0;i<j;i++){
					if(!main.getActiveQuests().get(i).isComplete()){
						main.getActiveQuests().get(i).checkComplete();
					}
				}
				System.out.println("Dispose of Stage");
				display.dispose();
				display=null;
				inDialog=false;
			}
			else{
				display.getStage().draw();
			}
		}
		hud.act();
		hud.draw();
		if(invi!=null){
			invi.getStage().draw();
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
		case Input.Keys.I:
			invi=new Inventory(main, game);
			invi.getDisplay().show(invi.getStage());
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
				currentDialog.setSpeaker(n);
				display=new DialogBox(currentDialog, currentDialog.getSpeaker(), game);
			}	
		}
		if(!buildings.isEmpty()){
			for(Building b:buildings){
				if(main.avatar.getBoundingRectangle().overlaps(b.getSprite().getBoundingRectangle())){
					if(main.moveDown){
						main.setMoveDown(false);
						main.avatar.setY(main.avatar.getY()+10*Gdx.graphics.getDeltaTime());
					
					}
					if(main.moveUp){
						main.setMoveUp(false);
						main.avatar.setY(main.avatar.getY()-10*Gdx.graphics.getDeltaTime());
						b.enter(main, game);
					
					}
					if(main.moveLeft){
						main.setMoveLeft(false);
						main.avatar.setX(main.avatar.getX()+10*Gdx.graphics.getDeltaTime());
					}
					if(main.moveRight){
						main.setMoveRight(false);
						main.avatar.setX(main.avatar.getX()-10*Gdx.graphics.getDeltaTime());
					}
				}
			}
		}
	}


	public dialogEngine.Dialog getCurrentDialog() {
		return currentDialog;
	}


	public void setCurrentDialog(dialogEngine.Dialog currentDialog) {
		this.currentDialog = currentDialog;
	}

}
