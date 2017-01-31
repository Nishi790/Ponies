package main;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;

import battleEngine.BattleScreen;
import battleEngine.Monster;
import dialogEngine.DialogBox;
import interactable.Building;
import interactable.Entrance;
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
	public ArrayList<NPC> npcs;
	private dialogEngine.Dialog currentDialog;
	public boolean inDialog=false;
	Inventory invi;
	DialogBox display;
	BitmapFont font;
	HUD hud;
	ArrayList<Building> buildings;
	ArrayList<Rectangle> collisions;
	ArrayList<Entrance> entrances;
	Animation<TextureRegion> ani;
	float animationtime=0;
	
	
	public GameScreen(final Sim game, Texture pony, String name){
		this.game=game;
		screenHeight=Gdx.graphics.getHeight();
		screenWidth=Gdx.graphics.getWidth();
		
		//load map
		current=new TmxMapLoader().load("data/Maps/map.tmx");
		buildings=new ArrayList<Building>();
		
		float scale=1/32f;
		tiledMapRenderer = new OrthogonalTiledMapRenderer(current,scale);
		base=(TiledMapTileLayer)current.getLayers().get(1);
		

		//set up camera
		camera=new OrthoCamera(base, scale);
		camera.setToOrtho(false,screenWidth/32,screenHeight/32);
		camera.position.set(camera.viewportWidth/2f, camera.viewportHeight/2f, 0);
		camera.update();
		
		collisions=new ArrayList<Rectangle>();
		entrances=new ArrayList<Entrance>();
		TiledMapTileLayer build=(TiledMapTileLayer)current.getLayers().get(3);
		TiledMapTileLayer coll=(TiledMapTileLayer)current.getLayers().get(0);
		for(int i=0; i<build.getWidth();i++){
			for(int j=0;j<build.getHeight();j++){
				Cell cur=build.getCell(i, j);
				if (cur!=null){
					TiledMapTile a=cur.getTile();
					if(a!=null){
						Object buil=a.getProperties().get("building");
						if(buil!=null){
							Rectangle temp=new Rectangle(i,j+1,build.getTileWidth()*scale,build.getTileHeight()*scale);
							collisions.add(temp);
						}
						
						Object buildName=a.getProperties().get("buildingName");
						if(buildName!=null){
							Building b=new Building(Gdx.files.internal("data/Buildings/"+buildName+".txt"));
							buildings.add(b);
							Object enter=a.getProperties().get("Entrance");
							if(enter!=null){
								Rectangle temp=new Rectangle(i,j+1,build.getTileWidth()*scale,build.getTileHeight()*scale);
								Entrance e=new Entrance(b,temp);
								entrances.add(e);
							}
						}
					}
				}
			}
		}
		for(int i=0; i<coll.getWidth();i++){
			for(int j=0;j<coll.getHeight();j++){
				Cell cur=coll.getCell(i, j);
				if (cur!=null){
					TiledMapTile a=cur.getTile();
					if(a!=null){
						Object buil=a.getProperties().get("blocked");
						if(buil!=null){
							Rectangle temp=new Rectangle(i,j+1,coll.getTileWidth()*scale,coll.getTileHeight()*scale);
							collisions.add(temp);
						}
					}
				}
			}
		}


		
		//create characters
		main=new MainChar(pony, name);
		npcs=new ArrayList<NPC>();
		String[] s=Gdx.files.internal("data/Maps/NPCs.txt").readString().split(";");
		for(String t:s){
			try {
				NPC n=new NPC(Gdx.files.internal("data/NPCs/"+t), main, this);
				npcs.add(n);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		//Temporary animation test
		TextureRegion sheet=new TextureRegion(new Texture(Gdx.files.internal("data/Sprites/proing.png")),128,768);
		TextureRegion[] sprites=new TextureRegion[]{new TextureRegion(sheet,0,0,128,128),new TextureRegion(sheet,0,128,128,128), 
				new TextureRegion(sheet,0,256,128,128), new TextureRegion(sheet,0,384,128,128), new TextureRegion(sheet,0,512,128,128)};
		ani=new Animation<TextureRegion>(0.1f,sprites);
		ani.setPlayMode(PlayMode.LOOP);
		
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
		
		//draw NPCs
		if (!npcs.isEmpty()){
			for(NPC n: npcs){
				n.avatar.draw(game.batch);
			}
		}
		
		//temporary animation tester
		animationtime=animationtime+Gdx.graphics.getDeltaTime();
		TextureRegion animation=ani.getKeyFrame(animationtime);
		game.batch.draw(animation, 5, 5, 4, 4);
		
		game.batch.end();
		
		//draw chat if inDialog
		if(inDialog){
			display.getChat().show(display.getStage());
			if(display.getPrompt()=="close"){
				int j=main.getActiveQuests().size();
				for(int i=0;i<j;i++){
					if(!main.getActiveQuests().get(i).isComplete()){
						main.getActiveQuests().get(i).checkComplete();
					}
				}
				display.dispose();
				display=null;
				inDialog=false;
			}
			else{
				display.getStage().draw();
			}
		}
		
		//draw HUD
		hud.act();
		hud.draw();
		
		//Draw inventory
		if(invi!=null){
			invi.getStage().draw();
		}
		
	}

	@Override
	public void resize(int width, int height) {
		camera.setToOrtho(false,width/32,height/32);
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
		
		//Set movement direction
		case Input.Keys.W: 
			main.setMoveUp(true);
			main.setMoveDown(false);
			break;
			
		case Input.Keys.S:
			main.setMoveDown(true);
			main.setMoveUp(false);
			break;
			
		case Input.Keys.A: 
			main.setMoveLeft(true);
			main.setMoveRight(false);
			break;
			
		case Input.Keys.D: 
			main.setMoveRight(true);
			main.setMoveLeft(false);
			break;
			
		//exit game
		case Input.Keys.ESCAPE:
			dispose();
			game.dispose();
			break;
		
		// display inventory	
		case Input.Keys.I:
			invi=new Inventory(main, game);
			invi.getDisplay().show(invi.getStage());
			break;
		
		//temporary, to allow easy testing of battle screen
		case Input.Keys.B:
			game.setScreen(new BattleScreen(game, main, "TempBG", new Monster(Gdx.files.internal("data/Monsters/Slime.txt"))));
			break;
		}

		return false;
	}


	@Override
	public boolean keyUp(int keycode) {
		//stop movement if key is released
		switch(keycode){
		
		case Input.Keys.W:
			main.setMoveUp(false);
		
		case Input.Keys.S:
			main.setMoveDown(false);
		
		case Input.Keys.A:
			main.setMoveLeft(false);
		
		case Input.Keys.D:
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
	
	
	//check collisions with NPCs, stop from walking through them; if colliding with NPC, enter dialog with them
	//Check collisions with buildings; if colliding from the bottom, enter building
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
		for(Rectangle b:collisions){
			if(main.avatar.getBoundingRectangle().overlaps(b)){
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
			}
		}
		for(Entrance e: entrances){
			if(main.avatar.getBoundingRectangle().overlaps(e.getEntrance())){
				e.enter(main, game);
			}
		}
	}

//Getters and Setters
	
	public dialogEngine.Dialog getCurrentDialog() {
		return currentDialog;
	}


	public void setCurrentDialog(dialogEngine.Dialog currentDialog) {
		this.currentDialog = currentDialog;
	}

}
