package main;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;

import dialogEngine.DialogBox;
import interactable.Building;
import inventory.Inventory;

public class InteriorScreen implements Screen, InputProcessor {
	float xEntrance, yEntrance;
	int screenHeight, screenWidth;
	float origX,origY;
	final Sim game;
	final TiledMap current;
	OrthogonalTiledMapRenderer tiledMapRenderer;
	TiledMapTileLayer base;
	OrthoCamera camera;
	final MainChar main;
	ArrayList<NPC> npcs;
	DialogBox display;
	HUD hud;
	boolean inDialog;
	Inventory invi;

	
	public InteriorScreen(final Sim game, MainChar main, TiledMap map){
		this.game=game;
		screenHeight=Gdx.graphics.getHeight();
		screenWidth=Gdx.graphics.getWidth();
		
		//load map
		current=map;
		float scale=1/16f;
		tiledMapRenderer = new OrthogonalTiledMapRenderer(current,scale);
		base=(TiledMapTileLayer)current.getLayers().get(0);
		
		//set up camera
		camera=new OrthoCamera(base);
		camera.setToOrtho(false,screenWidth/32,screenHeight/32);
		camera.position.set(camera.viewportWidth/2f-6.5f, camera.viewportHeight/2f-1f, 0);
		camera.update();
		
		//create characters
		this.main=main;
		origX=main.avatar.getX();
		origY=main.avatar.getY();
		main.avatar.setPosition(-camera.viewportWidth/2f+7.5f, -camera.viewportHeight/2f-3f);
		npcs=new ArrayList<NPC>();
		
		//npcData=new com.badlogic.gdx.files.FileHandle[]{Gdx.files.internal("data/NPCs/Pinkie.txt"), 
		//		Gdx.files.internal("data/NPCs/Spike.txt"), Gdx.files.internal("data/NPCs/Twilight.txt")};
		//for(com.badlogic.gdx.files.FileHandle f: npcData){
			//try {
				//NPC n=new NPC(f.readString(),f, main, this);
				//npcs.add(n);
		//	} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
	//			e.printStackTrace();
		//	}	
		//}
		//create input processor
		Gdx.input.setInputProcessor(this);
		hud=new HUD(main,this.game, this);
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
		case Input.Keys.E:
			main.avatar.setPosition(origX, origY);
			game.setScreen(game.main);
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
		clamp();
		//block();
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
		hud.dispose();
		tiledMapRenderer.dispose();
		current.dispose();
		
	}


	@Override
	public void dispose() {
		
		
	}
	
	public void clamp(){
		if(main.avatar.getX()>2*base.getWidth()-camera.viewportWidth/2){
			main.avatar.setX(2f*base.getWidth()-camera.viewportWidth/2);
		}
		if(main.avatar.getX()<-(camera.viewportWidth/2)){
			main.avatar.setX(-(camera.viewportWidth/2));
		}
		if(main.avatar.getY()<-camera.viewportHeight/2f-4f){
			main.avatar.setY(-camera.viewportHeight/2f-4f);
		}
		if(main.avatar.getY()>2*base.getHeight()-main.avatar.getHeight()/2){
			main.avatar.setY(2*base.getHeight()-main.avatar.getHeight()/2);
		}
	}
	
}
