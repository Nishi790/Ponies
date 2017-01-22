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
	
	public MainMenuScreen(final Sim game){
		this.game=game;
		stage=new Stage(new ScalingViewport(Scaling.stretch, 640,480));
		stage.getViewport().getCamera().position.x=0;
		stage.getViewport().getCamera().position.y=0;
		skin=new Skin(Gdx.files.internal("data/skin/terra-mother-ui.json"),
				new TextureAtlas(Gdx.files.internal("data/skin/terra-mother-ui.atlas")));
		skin.add("unicorn", 
				new TextureRegion(new Texture(Gdx.files.internal("data/Sprites/VinylScratch.png"))));
		skin.add("pegasus", 
				new TextureRegion(new Texture(Gdx.files.internal("data/Sprites/Derpy.png"))));
		skin.add("pegasusWalk", 
				new TextureRegion(new Texture(Gdx.files.internal("data/Sprites/Drizzle.png"))));
		skin.add("pony", 
				new TextureRegion(new Texture(Gdx.files.internal("data/Sprites/EarthPony.png"))));

		welcome=new Label("Welcome to PonyLife!", skin);
		instructions=new Label("Select a pony to begin", skin);
		ponySelection=new ImageButton[4];
		ponySelection[0]=new ImageButton(new TextureRegionDrawable(skin.getRegion("unicorn")));
		ponySelection[0].addListener(new ChangeListener(){

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				game.main=new GameScreen(game,new Sprite(skin.getRegion("unicorn")));
				game.setScreen(game.main);
			}
		});
		ponySelection[0].left();
		ponySelection[1]=new ImageButton(new TextureRegionDrawable(skin.getRegion("pegasus")));
		ponySelection[1].addListener(new ChangeListener(){

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				game.main=new GameScreen(game,new Sprite(skin.getRegion("pegasus")));
				game.setScreen(game.main);
			}
		});
		ponySelection[1].pad(10);
		ponySelection[2]=new ImageButton(new TextureRegionDrawable(skin.getRegion("pegasusWalk")));
		ponySelection[2].addListener(new ChangeListener(){

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				game.main=new GameScreen(game,new Sprite(skin.getRegion("pegasusWalk")));
				game.setScreen(game.main);
			}
		});
		ponySelection[2].pad(10);
		ponySelection[3]=new ImageButton(new TextureRegionDrawable(skin.getRegion("pony")));
		ponySelection[3].addListener(new ChangeListener(){

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				game.main=new GameScreen(game,new Sprite(skin.getRegion("pony")));
				game.setScreen(game.main);
			}
		});
		ponySelection[3].pad(10);
		buttons=new Table(skin);
		layout=new Table(skin);
		createTable();
		Gdx.input.setInputProcessor(stage);
		stage.addActor(layout);
		
	}
	
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
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		layout.layout();

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
