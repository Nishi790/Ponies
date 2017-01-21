package main;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;

import inventory.StatTracker;

public class HUD {
	Stage stage;
	Table overall;
	final Sim game;
	final MainChar main;
	final GameScreen screen;
	Table[] current;
	Table needTable;
	Label gold;
	
	
	public HUD(MainChar main, Sim game, GameScreen s){
		this.main=main;
		this.game=game;
		this.screen=s;
		stage=new Stage(new ScalingViewport(Scaling.none, 1024, 768),game.batch);
		current=new Table[main.getStats().length+main.getPoints().length];
		buildStatMonitors();
		needTable=new Table(this.game.skin);
		constructNeedTable();
		needTable.pad(20);
		needTable.left();
		needTable.moveBy(0, 670);
		stage.addActor(needTable);

	}
	
	public void act(){
		stage.act();


	}
	
	public void draw(){
		
		stage.draw();
	}
	
	private void constructNeedTable(){
		Table top=new Table(game.skin);
		needTable.add(top);
		top.add(current[0]);
		top.getCell(current[0]).left();
		top.add(current[1]);
		top.row();
		top.add(current[2]);
		top.getCell(current[2]).left();
		top.add(current[3]);
		top.row();
		top.add(current[4]);
		top.getCell(current[4]).left();
		top.add(current[5]);
		needTable.row();
		needTable.add(current[6]);
		needTable.getCell(current[6]).left();
		needTable.row();
		needTable.add(gold);
		needTable.getCell(gold).left();
		
		
	}
	private void buildStatMonitors(){
		float delayTime=5f;
		current[0]=new Table(this.game.skin);
		current[0].add("Sleep ");
		Label temp=new Label(main.getStats()[0]+"/10", this.game.skin);
		Action t=Actions.sequence((
				new StatTracker(temp, 0, this.game, this.main)), 
				Actions.delay(delayTime));
		temp.addAction(t);
		current[0].add(temp);
		
		current[1]=new Table(this.game.skin);
		current[1].add("Hunger ");
		temp=new Label(main.getStats()[1]+"/10", this.game.skin);
		t=Actions.sequence((
				new StatTracker(temp, 1, this.game, this.main)), 
				Actions.delay(delayTime));
		temp.addAction(t);
		current[1].add(temp);
		
		current[2]=new Table(this.game.skin);
		current[2].add("Fun ");
		temp=new Label(main.getStats()[2]+"/10", this.game.skin);
		t=Actions.sequence((
				new StatTracker(temp, 2, this.game, this.main)), 
				Actions.delay(delayTime));
		temp.addAction(t);
		current[2].add(temp);
		
		current[3]=new Table(this.game.skin);
		current[3].add("Hygiene ");
		temp=new Label(main.getStats()[3]+"/10", this.game.skin);
		t=Actions.sequence((
				new StatTracker(temp, 3, this.game, this.main)), 
				Actions.delay(delayTime));
		temp.addAction(t);
		current[3].add(temp);
		
		current[4]=new Table(this.game.skin);
		current[4].add("HP ");
		temp=new Label(main.getPoints()[0]+"/100", this.game.skin);
		t=Actions.sequence((
				new StatTracker(temp, 4, this.game, this.main)), 
				Actions.delay(delayTime));
		temp.addAction(t);
		current[4].add(temp);
		
		current[5]=new Table(this.game.skin);
		current[5].add("MP ");
		temp=new Label(main.getPoints()[1]+"/100", this.game.skin);
		t=Actions.sequence((
				new StatTracker(temp, 5, this.game, this.main)), 
				Actions.delay(delayTime));
		temp.addAction(t);
		current[5].add(temp);
		
		current[6]=new Table(this.game.skin);
		current[6].add("Special Ability ");
		temp=new Label(main.getPoints()[2]+"/100", this.game.skin);
		t=Actions.sequence((
				new StatTracker(temp, 6, this.game, this.main)), 
				Actions.delay(delayTime));
		temp.addAction(t);
		current[6].add(temp);
		
		gold=new Label(String.valueOf(main.getGold()), this.game.skin);
		t=Actions.sequence(
				new StatTracker(gold,7,this.game, this.main),
				Actions.delay(delayTime));
		gold.addAction(t);
	}
	
	
}
