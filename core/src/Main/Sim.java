package main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Sim extends Game {
	public SpriteBatch batch;
	public BitmapFont font;
	AssetManager manager;
	public Skin skin;
	GameScreen main;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		font=new BitmapFont();
		manager=new AssetManager();
		TextureAtlas atlas=new TextureAtlas(Gdx.files.internal("data/skin/terra-mother-ui.atlas"));
		skin=new Skin(Gdx.files.internal("data/skin/terra-mother-ui.json"),atlas);
		Pixmap pixmap = new Pixmap(80, 80, Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
		pixmap.fill();
		skin.add("highlight", new Texture(pixmap));
		this.setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render (){ 
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		font.dispose();
	}
	
	public GameScreen getMainScreen(){return main;}

	public void setMainScreen(GameScreen gameScreen) {
		main=gameScreen;
		
	}
}
