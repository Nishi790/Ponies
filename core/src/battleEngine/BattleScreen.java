package battleEngine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import main.MainChar;
import main.Sim;

public class BattleScreen implements Screen, InputProcessor {
	final Sim game;
	final MainChar main;
	Sprite background;
	OrthographicCamera camera;
	float prevX, prevY;
	Monster enemy;

	
	public BattleScreen(Sim g, MainChar m){
		game=g;
		main=m;
		Texture ground=new Texture(Gdx.files.internal("data/Backgrounds/SkyBG.png"));
		background=new Sprite(ground);
		background.setSize(1024, 768);
		prevX=main.getAvatar().getX();
		prevY=main.getAvatar().getY();
		main.getAvatar().setSize(128, 128);
		main.getAvatar().setPosition(128, 128);
		enemy=new Monster(Gdx.files.internal("data/Monsters/Slime.txt"));
		
		camera=new OrthographicCamera();
		camera.setToOrtho(false,1024, 768);
		
		Gdx.input.setInputProcessor(this);
		
	}
	
	
	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		game.batch.begin();
		background.draw(game.batch);
		main.draw(game.batch);
		
		enemy.draw(game.batch);
		
		game.batch.end();

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
		main.getAvatar().setPosition(prevX, prevY);
		main.getAvatar().setSize(2,2);

	}

	@Override
	public void dispose() {
		
		// TODO Auto-generated method stub

	}


	@Override
	public boolean keyDown(int keycode) {
		if(keycode==Input.Keys.A){
			enemy.act(main);
			System.out.println("Attacking="+enemy.attacking);
		}
		
		
		return false;
	}


	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
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

}
