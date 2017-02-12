package battleEngine;

import java.util.ArrayList;
import java.util.Collections;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeType.Bitmap;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;

import main.MainChar;
import main.Sim;

public class BattleScreen implements Screen{

	final Sim game;
	final MainChar main;
	
	BattleCharacter playerCharacter;
	Stage stage;
	ArrayList<BattleCharacter> party;
	ArrayList<Monster> monsters;
	TiledMap map;
	OrthogonalTiledMapRenderer tiledMapRenderer;
	OrthographicCamera camera;
	ImageButton[] movetargets;
	String[] mainChoices=new String[]{"Move", "Attack(Melee)", "Attack(Ranged)", "Magic"};
	String[] moveChoices=new String[]{"Left 1", "Left 2", "Right 1", "Right 2", "Up 1", "Up 2", "Down 1", "Down 2"};
	Table leftTable;
	Table[] lowerTables;
	Table messageTable;
	Table defeat;
	Table flee;
	Table win;
	ArrayList<Button> targets;
	boolean ranged;
	boolean[] tables=new boolean[]{false,false,false,false};
	boolean playerTurn;
	TextButton[] mainbuttons;
	BattleCharacter activeCharacter;
	ArrayList<BattleCharacter> initiativeOrder;

	
	public BattleScreen(Sim g, MainChar m, String setting, Monster mon, String map){
		//load battle map
		this.map=new TmxMapLoader().load(map);
		tiledMapRenderer = new OrthogonalTiledMapRenderer(this.map);
		
		//store main character and game
		game=g;
		main=m;
		
		//create stage and set Camera to look below and left of map, in order to make space for menus
		stage=new Stage();
		camera=(OrthographicCamera)stage.getCamera();
		camera.position.set(288,16,0);
		
		//create party, store main character
		party=new ArrayList<BattleCharacter>();
		playerCharacter=new BattleCharacter(main,0);
		party.add(playerCharacter);
		
		//add party members if available
		for(int i=0; i<main.getPartyMembers().length;i++){
			if(main.getPartyMembers()[i]!=null){
				party.add(new BattleCharacter(main.getPartyMembers()[i],i+1));
			}
			
		}
		
		//create initiative order array
		initiativeOrder=new ArrayList<BattleCharacter>();
		
		//create arrayList of monsters
		monsters=new ArrayList<Monster>();
		monsters.add(mon);
		
		//add party members and monsters to initiative order
		for(BattleCharacter b:party){
			initiativeOrder.add(b);
		}
		for(Monster s:monsters){
			initiativeOrder.add(s);
		}
		
		//sort all players by initiative
		Collections.sort(initiativeOrder);
		
		//call make tables
		makeTables();
		
		//set input processor to take input from stage
		Gdx.input.setInputProcessor(stage);
		
		targets=new ArrayList<Button>();
		
		//set the current active Character to the first character in initiative order
		activeCharacter=initiativeOrder.get(0);
		
	}
	
	//generates the interactable left table and the sub tables
	private void makeTables(){
		//create the left side menu table to select type of action
		leftTable=new Table(game.skin);
		leftTable.setPosition(-224, -100);
		leftTable.setSize(224, 500);
		
		//lowerTables is the set of tables that appears depending on left menu selection
		lowerTables=new Table[4];
		
		//create buttons for each menu item
		for(int i=0;i<mainChoices.length;i++){
			TextButton b=new TextButton(mainChoices[i], new TextButton.TextButtonStyle(game.skin.newDrawable("highlight", Color.BLACK), 
					game.skin.newDrawable("highlight", Color.BLACK), game.skin.getDrawable("window-c"), 
					game.skin.getFont("giygas")));
			
			if(i==0){
				//create basic table for movement
				Table t=new Table(game.skin);
				t.setPosition(-224, -368);
				t.setSize(1024, 268);
				t.setBackground(game.skin.getDrawable("window-player-c"));
				movementTable(t);
				lowerTables[i]=t;
			}
			
			final int j=i;
			b.addListener(new ClickListener(){
				
				//if the table is visible, set invisible, if invisible, set visible
				@Override
				public void clicked(InputEvent event, float x, float y) {
					stage.addActor(lowerTables[j]);
					for(int i=0; i<tables.length;i++){
						if(i!=j&&lowerTables[i]!=null){
							lowerTables[i].remove();
							((Button)leftTable.getCells().get(i).getActor()).setChecked(false);
						}
					}
				}
				
			});
			
			//add buttons to the left table
			leftTable.add(b);
			Cell<TextButton> c=leftTable.getCell(b);
			c.pad(20, 0, 20, 0);
			//move to new table row
			leftTable.row();

		}
		leftTable.setBackground(game.skin.getDrawable("window-c"));
		
		//add left menu to the stage to be drawn and interacted with
		stage.addActor(leftTable);
		
		//create table overtop of battlefield for displaying messages
		messageTable=new Table(game.skin);
		messageTable.setSize(800, 400);
		messageTable.setPosition(0, 0);
		
		//add message table to stage
		stage.addActor(messageTable);
		
		//create table to be displayed in the event that all party members leave the battlefield
		flee=new Table();
		Label fled=new Label("You ran away",game.skin.get("giygas", Label.LabelStyle.class));
		
		//add button to return player to main map
		TextButton toGame=new TextButton("Return to game", game.skin);
		toGame.pad(20, 0, 20, 0);
		toGame.addListener(new ChangeListener(){

			//return player to main map upon fleeing
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				main.setHP(playerCharacter.currenthp);
				game.setScreen(game.getMainScreen());
				Gdx.input.setInputProcessor(game.getMainScreen());
				
			}
			
		});
		flee.add(fled);
		flee.row();
		flee.add(toGame);
		
		//create table to be displayed if all party members are defeated
		defeat=new Table();
		Label defeated=new Label("You have been defeated!", game.skin.get("giygas", Label.LabelStyle.class));
		
		//create button to return to central area of main map (the town)
		TextButton toTown=new TextButton("Return to Town", game.skin);
		toTown.pad(20,0,20,0);
		toTown.addListener(new ChangeListener(){
			
			@Override
			public void changed(ChangeEvent event, Actor actor){
				main.getAvatar().setPosition(50, 45);
				main.setHP(party.get(0).currenthp);
				game.setScreen(game.getMainScreen());
				Gdx.input.setInputProcessor(game.getMainScreen());

			}
		});
		defeat.add(defeated);
		defeat.row();
		defeat.add(toTown);
		
		//create table to be displayed if all monsters are defeated
		win=new Table();
		Label winner=new Label("You have defeated your enemies!", game.skin.get("giygas", Label.LabelStyle.class));
			
		//create button to return to central area of main map (the town)
		TextButton map=new TextButton("Return to Map", game.skin);
		map.pad(20,0,20,0);
		map.addListener(new ChangeListener(){
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				main.setHP(playerCharacter.currenthp);
				game.setScreen(game.getMainScreen());
				Gdx.input.setInputProcessor(game.getMainScreen());
			}
		});
		win.add(winner);
		win.row();
		win.add(map);
	
	}
	
	private void flee(){
		messageTable.add(flee);
	}
	
	
	private void lose(){
		messageTable.add(defeat);
	}
	
	private void win(){
		messageTable.add(win);
	}
	
	
	private void movementTable(Table t){
		
		for(int i=0;i<moveChoices.length;i++){
			
			//create button for each possible type of movement
			final TextButton b=new TextButton(moveChoices[i], 
					new TextButton.TextButtonStyle(game.skin.newDrawable("highlight", Color.BLACK), game.skin.newDrawable("highlight",Color.BLACK), 
					game.skin.getDrawable("window-c"), game.skin.getFont("year199x")));
			final int j=i;
			b.addListener(new ClickListener(){
				
				//code to cause movement on button press
				@Override
				public void clicked(InputEvent event, float x, float y) {
					String[] temp=moveChoices[j].split(" ");
					
					//move the active character based on the selected option
					if(temp[0].equalsIgnoreCase("left")){
						activeCharacter.setX(activeCharacter.getX()-100*Integer.parseInt(temp[1]));
						b.setChecked(false);
						((Button) leftTable.getCells().get(0).getActor()).setChecked(false);
					}
					else if(temp[0].equalsIgnoreCase("right")){
						activeCharacter.setX(activeCharacter.getX()+100*Integer.parseInt(temp[1]));
						b.setChecked(false);
						((Button) leftTable.getCells().get(0).getActor()).setChecked(false);
					}
					else if(temp[0].equalsIgnoreCase("up")){
						activeCharacter.setY(activeCharacter.getY()+100*Integer.parseInt(temp[1]));
						b.setChecked(false);
						((Button) leftTable.getCells().get(0).getActor()).setChecked(false);
					}
					else if(temp[0].equalsIgnoreCase("down")){
						activeCharacter.setY(activeCharacter.getY()-100*Integer.parseInt(temp[1]));
						b.setChecked(false);
						((Button) leftTable.getCells().get(0).getActor()).setChecked(false);
					}
					
					//switch initiative to next character
					toNextAttacker();
					
				}
				
			});
			b.pad(5, 30, 5, 30);
			t.add(b);
			
			if(i==3){
				t.row();
			}
		}
	}
	
	//create table with all ranged attacks of the current attacker
	private void rangedTable(BattleCharacter current){
		lowerTables[2]=new Table();
		lowerTables[2].setPosition(-224, -368);
		lowerTables[2].setSize(1024, 268);
		lowerTables[2].setBackground(game.skin.getDrawable("window-player-c"));
		
		//add all ranged moves to the table
		for(Attack a:current.moves){
			if(a.type.equalsIgnoreCase("range")){
				final TextButton b=new TextButton(a.name,new TextButton.TextButtonStyle(game.skin.newDrawable("highlight", Color.BLACK), game.skin.newDrawable("highlight", Color.BLACK), 
					game.skin.getDrawable("window-c"), game.skin.getFont("year199x")));
				final Attack j=a;
				b.addListener(new ClickListener(){
					
					@Override
					public void clicked(InputEvent event, float x, float y){
						ranged=true;
						for(Monster m:monsters){
							if(Math.abs(m.getX()-activeCharacter.getX())<=100*j.range&&m.getY()==activeCharacter.getY()){
								final Monster mon=m;
								
								//create bright square around targetable enemies
								Button target=new Button(game.skin.newDrawable("highlight", 1,1,1,0.3f));
								target.setPosition(m.getX(), m.getY());
								target.setSize(100,100);
								target.addListener(new ClickListener(){
									//if square is clicked, attack the monster in the square, set ranged to false, and move on to next attacker
									@Override
									public void clicked(InputEvent event, float x, float y){
										j.attack(mon);
										ranged=false;
										lowerTables[2].remove();
										((Button)leftTable.getCells().get(2).getActor()).setChecked(false);
										b.setChecked(false);
										toNextAttacker();
										for (int i=0;i<targets.size();i++){
											targets.get(i).remove();
											targets.remove(i);
										}
									}
								});
								targets.add(target);
								
							}
						}
					}
				});
				b.pad(5, 30, 5, 30);
				lowerTables[2].add(b);
				lowerTables[2].row();
			}
			
		}
		for(Cell c: lowerTables[2].getCells()){
			((Button) c.getActor()).setChecked(false);
		}
	}
	
	//method to move initiative to the next attacker
	public void toNextAttacker(){
		
		//get init of next character
		int next=initiativeOrder.indexOf(activeCharacter)+1;
		
		//if next is larger than the size of the init arrayList, set next to 0
		if(next>initiativeOrder.size()-1)next=0;
		activeCharacter=initiativeOrder.get(next);
		
		//if the active character has been defeated, get next character in initiative
		if(activeCharacter.defeated)toNextAttacker();
				
		//if the new active character is player controlled, set player turn to true
		if(party.contains(activeCharacter))playerTurn=true;
		else {
			playerTurn=false;
			lowerTables[1]=null;
			lowerTables[2]=null;
			lowerTables[3]=null;
		}
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		
		if(party.isEmpty()){
			flee();	
		}
		
		//create int, if it decrements to 0 (because all party members are defeated), call lose 
		//tests if all party members are defeated
		int defeat=party.size();
		for(BattleCharacter b:party){
			if(b.defeated)defeat--;
		}
		
		if(defeat==0){
			lose();
		}
		
		//create int equivalent to number of monsters, decrement
		//test if all monsters are dead, if so, call win
		
		int kill=monsters.size();
		
		for(Monster m: monsters){
			if(m.defeated)kill--;
		}
		
		if(kill==0){
			win();
		}
		
		//clear screen
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		//render battlegrid
		tiledMapRenderer.setView(camera);
		tiledMapRenderer.render();
		
		//if player turn, generate the relevant tables
		if(playerTurn){
			if(lowerTables[2]==null)rangedTable(activeCharacter);
		}
		
		
		//draw in camera coordinates
		game.batch.setProjectionMatrix(camera.combined);
		game.batch.begin();
		
		//for each party member, if they have left the map, remove from party
		//if they have less than 0 hp, set as defeated
		//else draw
		int j=party.size();
		for(int i=0;i<j;i++){
			BattleCharacter b=party.get(i);
			if(b.getX()<0){
				party.remove(b);
			}
			else if(b.currenthp<=0){
				b.defeated=true;
			}
			else b.draw(game.batch);
			
		}
		
		//draw each monster
		for(Monster s:monsters){
			if(s.currenthp>0){
				s.draw(game.batch);
			}
			//if have 0 hp, set as defeated
			else{
				s.defeated=true;
			}
		}

		game.batch.end();
		
		if(ranged){
			for(Button b:targets){
				stage.addActor(b);
				b.toFront();
			}
		}
		else{
			for(Button b: targets){
				b.remove();
			}
		}
		//draw menus on stage
		stage.draw();
		
		if (!playerTurn){
			//call AI
			//Temporarily, just attack
			if(!party.isEmpty()){
				if(activeCharacter.getX()-party.get(0).getX()<=208&&activeCharacter.getY()==party.get(0).getY()){
					Attack a=(Attack)activeCharacter.getMoves().get(0);
					if(!a.inProgress) {
						activeCharacter.currentAttack=a;
						activeCharacter.attacking=true;
						a.attack(party.get(0));
					}
					else {
						a.act(Gdx.graphics.getDeltaTime(),this);
					}
				}
				//if more than 2 squares away, move closer
				else if(activeCharacter.getX()-party.get(0).getX()>=208){
					activeCharacter.setX(activeCharacter.getX()-100);
					toNextAttacker();
				}
				//if above player character, move down
				else if(activeCharacter.getY()>party.get(0).getY()){
					activeCharacter.setY(activeCharacter.getY()-100);
					toNextAttacker();
				}
				//if below player character, move up
				else if(activeCharacter.getY()<party.get(0).getY()){
					activeCharacter.setY(activeCharacter.getY()+100);
					toNextAttacker();
				}
			}
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	
}