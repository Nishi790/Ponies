package main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;

public class MainMenuScreen implements Screen {
	final Sim game;
	Stage stage;
	Label welcome;
	Label instructions;
	ImageButton[] ponySelection;
	Skin skin;
	Table layout;
	Table buttons;
	Texture[] chars;
	
	public MainMenuScreen(final Sim game){
		this.game=game;
		stage=new Stage(new ScalingViewport(Scaling.stretch, 640,480));
		stage.getViewport().getCamera().position.x=0;
		stage.getViewport().getCamera().position.y=0;
		skin=new Skin(Gdx.files.internal("data/skin/terra-mother-ui.json"),
				new TextureAtlas(Gdx.files.internal("data/skin/terra-mother-ui.atlas")));
		
		chars=new Texture[4];
		chars[0]=new Texture(Gdx.files.internal("data/Sprites/VinylScratch.png"));
		chars[1]=new Texture(Gdx.files.internal("data/Sprites/base walk.png"));
		chars[2]=new Texture(Gdx.files.internal("data/Sprites/Drizzle.png"));
		chars[3]=new Texture(Gdx.files.internal("data/Sprites/EarthPony.png"));
		skin.add("unicorn", 
				new TextureRegion(chars[0],128,128));
		
		skin.add("pegasus", 
				new TextureRegion(chars[1],128,128));
		
		skin.add("pegasusWalk", 
				new TextureRegion(chars[2],128,128));
		
		skin.add("pony", 
				new TextureRegion(chars[3],128,128));

		
		welcome=new Label("Welcome to PonyLife!", skin);
		
		instructions=new Label("Select a pony to begin", skin);
		
		ponySelection=new ImageButton[4];
		
		//create image buttons for each possible character
		ponySelection[0]=new ImageButton(new TextureRegionDrawable(skin.getRegion("unicorn")));
		ponySelection[0].addListener(new ChangeListener(){

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				game.main=new GameScreen(game,chars[0], "Vinyl Scratch");
				game.setScreen(game.main);
			}
		});
		ponySelection[0].left();
		ponySelection[1]=new ImageButton(new TextureRegionDrawable(skin.getRegion("pegasus")));
		ponySelection[1].addListener(new ChangeListener(){

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				game.main=new GameScreen(game,chars[1], "Derpy");
				game.setScreen(game.main);
			}
		});
		ponySelection[1].pad(10);
		ponySelection[2]=new ImageButton(new TextureRegionDrawable(skin.getRegion("pegasusWalk")));
		ponySelection[2].addListener(new ChangeListener(){

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				game.main=new GameScreen(game,chars[2], "Drizzle");
				game.setScreen(game.main);
			}
		});
		ponySelection[2].pad(10);
		ponySelection[3]=new ImageButton(new TextureRegionDrawable(skin.getRegion("pony")));
		ponySelection[3].addListener(new ChangeListener(){

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				game.main=new GameScreen(game,chars[3], "SoccerPony");
				game.setScreen(game.main);
			}
		});
		ponySelection[3].pad(10);
		
		//create table to organize image buttons
		buttons=new Table(skin);
		
		//create table to hold labels and button table
		layout=new Table(skin);
		
		//organize table
		createTable();
		
		//set input processor to accept input
		Gdx.input.setInputProcessor(stage);
		
		//add table to the stage to make interactable and drawable
		stage.addActor(layout);
		
	}
	
	//fill table with appropriate layout
	private void createTable(){
		layout.add(welcome);
		Cell<Label> a=layout.getCell(welcome);
		a.center();
		a.padBottom(5);
		layout.row();
		layout.add(instructions);
		a=layout.getCell(instructions);
		a.center();
		a.padBottom(5);
		layout.row();
		layout.add(buttons);
		buttons.add(ponySelection[0]);
		buttons.add(ponySelection[1]);
		buttons.add(ponySelection[2]);
		buttons.add(ponySelection[3]);
	}
	
	@Override
	public void render(float delta){
		//clear screen
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		//layout table
		layout.layout();

		//draw everything
		stage.draw();

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
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
		stage.dispose();
		
		
	}
	
}
