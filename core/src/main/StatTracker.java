package main;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class StatTracker extends Action {
	final Sim game;
	final MainChar main;
	int index;
	double statValue;
	Label label;
	
	
	public StatTracker(Label l,int index, Sim g, MainChar m){
		game=g;
		main=m;
		this.index=index;
		label=l;
		if(index<=3){
			statValue=main.stats[index];
		}
		if(index>3){
			statValue=main.points[index-4];
		}
	}
	@Override
	public boolean act(float delta) {
		if(index<=3){
			statValue=main.stats[index]-0.001;
			main.stats[index]=statValue;
			label.setText(java.lang.Math.round(statValue) +"/10");
		}
		if(index>3){
			statValue=main.points[index-4];
			label.setText(java.lang.Math.round(statValue) +"/100");
		}
		
		return false;
	}
	
	

}
