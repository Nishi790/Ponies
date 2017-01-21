package Main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class Sim extends Game {
	public SpriteBatch batch;
	BitmapFont font;
	AssetManager manager;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		font=new BitmapFont();
		manager=new AssetManager();
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
