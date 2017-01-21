package Main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class MainMenuScreen implements Screen {
	final Sim game;
	Texture texture;
	Texture aj;
	OrthographicCamera camera;
	Sprite[] sprites;
	
	public MainMenuScreen(final Sim game){
		this.game=game;
		camera=new OrthographicCamera();
		camera.setToOrtho(false);
		texture= new Texture(Gdx.files.internal("data/Sprites/sprite_Pinkie pie1.png"));
		aj=new Texture (Gdx.files.internal("data/Sprites/sprite_AppleJack1.png"));
		Sprite applejack=new Sprite(aj);
		Sprite pinkie= new Sprite(texture);
		sprites=new Sprite[]{applejack, pinkie};
	}
	
	@Override
	public void render(float delta){
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		sprites[0].setPosition(100, 100);
		sprites[1].setPosition(200, 100);
		
		camera.update();
		
		game.batch.begin();
		game.font.draw(game.batch, "Welcome to PonyLife", 
				camera.viewportWidth/2-75, camera.viewportHeight/2+50);
		game.font.draw(game.batch, "Select a pony to continue", 
				camera.viewportWidth/2-75, camera.viewportHeight/2-50);
		for(Sprite sprite:sprites){
			sprite.draw(game.batch);
		}
		game.batch.end();
		
		if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
			if(containsCursor(sprites[0].getBoundingRectangle())){
				game.setScreen(new GameScreen(game,sprites[0]));
				dispose();
			}
			if(containsCursor(sprites[1].getBoundingRectangle())){
				game.setScreen(new GameScreen(game, sprites[1]));
				dispose();
			}
		}
		
	}
	//returns true if provided rectangle contains the current cursor position
	public boolean containsCursor(Rectangle r){
		if(r.contains(Gdx.input.getX(), Gdx.graphics.getHeight()-Gdx.input.getY()))
			return true;
		else return false;
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
		texture.dispose();
		
	}
	
}
