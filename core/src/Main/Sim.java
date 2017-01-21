package main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Sim extends Game {
	public SpriteBatch batch;
	BitmapFont font;
	AssetManager manager;
	public Skin skin;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		font=new BitmapFont();
		manager=new AssetManager();
		TextureAtlas atlas=new TextureAtlas(Gdx.files.internal("data/skin/terra-mother-ui.atlas"));
		skin=new Skin(Gdx.files.internal("data/skin/terra-mother-ui.json"),atlas);
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
}
