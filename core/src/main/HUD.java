package main;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;

public class HUD {
	Stage stage;
	final Sim game;
	final MainChar main;
	final GameScreen screen;
	Table[] current;
	Table needTable;
	
	
	public HUD(MainChar main, Sim game, GameScreen s){
		this.main=main;
		this.game=game;
		this.screen=s;
		stage=new Stage(new ScalingViewport(Scaling.none, 1024, 768),game.batch);
		current=new Table[main.stats.length+main.points.length];
		buildStatMonitors();
		needTable=new Table(this.game.skin);
		constructNeedTable();	
		stage.addActor(needTable);
	}
	
	public void act(){
		stage.act();


	}
	
	public void draw(){
		needTable.setPosition(stage.getWidth(), stage.getHeight()/2);
		stage.draw();
	}
	
	private void constructNeedTable(){
		needTable.add(current[0]);
		needTable.add(current[1]);
		needTable.row();
		needTable.add(current[2]);
		needTable.add(current[3]);
		needTable.row();
		needTable.add(current[4]);
		needTable.add(current[5]);
		needTable.row();
		needTable.add(current[6]);
	}
	private void buildStatMonitors(){
		current[0]=new Table(this.game.skin);
		current[0].add("Sleep ");
		Label temp=new Label(main.stats[0]+"/10", this.game.skin);
		Action t=Actions.sequence((
				new StatTracker(temp, 0, this.game, this.main)), 
				Actions.delay(3f));
		temp.addAction(t);
		current[0].add(temp);
		
		current[1]=new Table(this.game.skin);
		current[1].add("Hunger ");
		temp=new Label(main.stats[1]+"/10", this.game.skin);
		t=Actions.sequence((
				new StatTracker(temp, 1, this.game, this.main)), 
				Actions.delay(3f));
		temp.addAction(t);
		current[1].add(temp);
		
		current[2]=new Table(this.game.skin);
		current[2].add("Fun ");
		temp=new Label(main.stats[2]+"/10", this.game.skin);
		t=Actions.sequence((
				new StatTracker(temp, 2, this.game, this.main)), 
				Actions.delay(3f));
		temp.addAction(t);
		current[2].add(temp);
		
		current[3]=new Table(this.game.skin);
		current[3].add("Hygiene ");
		temp=new Label(main.stats[3]+"/10", this.game.skin);
		t=Actions.sequence((
				new StatTracker(temp, 3, this.game, this.main)), 
				Actions.delay(3f));
		temp.addAction(t);
		current[3].add(temp);
		
		current[4]=new Table(this.game.skin);
		current[4].add("HP ");
		temp=new Label(main.points[0]+"/100", this.game.skin);
		t=Actions.sequence((
				new StatTracker(temp, 4, this.game, this.main)), 
				Actions.delay(3f));
		temp.addAction(t);
		current[4].add(temp);
		
		current[5]=new Table(this.game.skin);
		current[5].add("MP ");
		temp=new Label(main.points[1]+"/100", this.game.skin);
		t=Actions.sequence((
				new StatTracker(temp, 5, this.game, this.main)), 
				Actions.delay(3f));
		temp.addAction(t);
		current[5].add(temp);
		
		current[6]=new Table(this.game.skin);
		current[6].add("Special Ability ");
		temp=new Label(main.points[2]+"/100", this.game.skin);
		t=Actions.sequence((
				new StatTracker(temp, 6, this.game, this.main)), 
				Actions.delay(3f));
		temp.addAction(t);
		current[6].add(temp);
	}
	
	
}
