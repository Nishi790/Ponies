package main;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import dialogEngine.DialogBox;
import interactable.Furniture;
import inventory.Inventory;

public class InteriorScreen implements Screen, InputProcessor {
	float xEntrance, yEntrance;
	int screenHeight, screenWidth;
	float origX,origY;
	public final Sim game;
	final TiledMap current;
	OrthogonalTiledMapRenderer tiledMapRenderer;
	TiledMapTileLayer base;
	OrthoCamera camera;
	private final MainChar main;
	ArrayList<NPC> npcs;
	DialogBox display;
	private HUD hud;
	boolean inDialog;
	Inventory invi;
	ArrayList<Furniture> furnishing;
	private boolean alert;
	private Furniture interactee;

	
	public InteriorScreen(final Sim game, MainChar main, TiledMap map, ArrayList<String[]> inside){
		this.game=game;
		screenHeight=Gdx.graphics.getHeight();
		screenWidth=Gdx.graphics.getWidth();
		furnishing=new ArrayList<Furniture>();
		for(String[] s:inside){
			furnishing.add(new Furniture(this, Gdx.files.internal(s[0]),s[1],s[2]));
		}
		
		//load map
		current=map;
		float scale=1/16f;
		tiledMapRenderer = new OrthogonalTiledMapRenderer(current,scale);
		base=(TiledMapTileLayer)current.getLayers().get(0);
		
		//set up camera
		camera=new OrthoCamera(base,scale);
		camera.setToOrtho(false,screenWidth/32,screenHeight/32);
		camera.position.set(camera.viewportWidth/2f-6.5f, camera.viewportHeight/2f-1f, 0);
		camera.update();
		
		//create characters
		this.main=main;
		origX=main.avatar.getX();
		origY=main.avatar.getY();
		main.avatar.setPosition(5, 2);
		main.avatar.setSize(4, 4);
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
		setHud(new HUD(main,this.game, this));
	}


	@Override
	public boolean keyDown(int keycode) {
		switch(keycode){
		
		//set direction of movement
		
		case Input.Keys.W: 
			getMain().setMoveUp(true);
			getMain().setMoveDown(false);
			break;
			
		case Input.Keys.S:
			getMain().setMoveDown(true);
			getMain().setMoveUp(false);
			break;
			
		case Input.Keys.A: 
			getMain().setMoveLeft(true);
			getMain().setMoveRight(false);
			break;
			
		case Input.Keys.D: 
			getMain().setMoveRight(true);
			getMain().setMoveLeft(false);
			break;
		
		//exit game
		case Input.Keys.ESCAPE:
			dispose();
			game.dispose();
			break;
			
		//display inventory by pressing i
		case Input.Keys.I:
			invi=new Inventory(getMain(), game);
			invi.getDisplay().show(invi.getStage());
			
		//Exit building by pressing e
		case Input.Keys.E:
			getMain().avatar.setPosition(origX, origY-3);
			getMain().avatar.setSize(2, 2);
			game.setScreen(game.getMainScreen());
			break;
			
		}
		return false;
	}


	@Override
	public boolean keyUp(int keycode) {
		//stop movement if key lifted
		switch(keycode){
		
		case Input.Keys.W:
			getMain().setMoveUp(false);
		
		case Input.Keys.S:
			getMain().setMoveDown(false);
		
		case Input.Keys.A:
			getMain().setMoveLeft(false);
		
		case Input.Keys.D:
			getMain().setMoveRight(false);
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
		System.out.println("Show room");
		
	}


	@Override
	public void render(float delta) {
		//clear screen
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		//update necessary things
		getMain().updatePos();
		clamp();
		block();
		camera.update();
		
		//render map
		tiledMapRenderer.setView(camera);
		tiledMapRenderer.render();
				
		//render in Camera's coordinate system
		game.batch.setProjectionMatrix(camera.combined);
				
		//actual drawing
		game.batch.begin();
		getMain().draw(game.batch);
		
		//draw NPCs
		if (!npcs.isEmpty()){
			for(NPC n: npcs){
				n.avatar.draw(game.batch);
			}
		}
		
		//draw Furnishings
		if (!furnishing.isEmpty()){
			for(Furniture f: furnishing){
				f.getImage().draw(game.batch);
			}
		}
		game.batch.end();
		
		//draw dialog
		if(inDialog){
			display.getChat().show(display.getStage());
			if(display.getPrompt()=="close"){
				int j=getMain().getActiveQuests().size();
				//check for any completed quests
				for(int i=0;i<j;i++){
					if(!getMain().getActiveQuests().get(i).isComplete()){
						getMain().getActiveQuests().get(i).checkComplete();
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
		
		//draw interaction message if interacting with an object
		if(isAlert()){
			getInteractee().getStage().draw();
		}
		
		//draw HUD
		getHud().act();
		getHud().draw();
		
		//draw Inventory
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

		
	}


	@Override
	public void dispose() {
		getHud().dispose();
		tiledMapRenderer.dispose();
		current.dispose();
		invi.dispose();
		
	}
	
	//Keep avatar on screen;
	public void clamp(){
		if(getMain().avatar.getX()>18){
			getMain().avatar.setX(18);
		}
		if(getMain().avatar.getX()<0){
			getMain().avatar.setX(0);
		}
		if(getMain().avatar.getY()<0){
			getMain().avatar.setY(0);
		}
		if(getMain().avatar.getY()>18){
			getMain().avatar.setY(18);
		}
	}
	
	//keep from walking through furniture, displays interaction message
	public void block(){
		for(Furniture n: furnishing){
			if(n.getImage().getBoundingRectangle().overlaps(main.avatar.getBoundingRectangle())){
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
				setAlert(true);
				setInteractee(n);
				n.interactWith();
			}	
		}
	}


	public HUD getHud() {
		return hud;
	}


	public void setHud(HUD hud) {
		this.hud = hud;
	}


	public MainChar getMain() {
		return main;
	}


	public Furniture getInteractee() {
		return interactee;
	}


	public void setInteractee(Furniture interactee) {
		this.interactee = interactee;
	}


	public boolean isAlert() {
		return alert;
	}


	public void setAlert(boolean alert) {
		this.alert = alert;
	}
	
}
