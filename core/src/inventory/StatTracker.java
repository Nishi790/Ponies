package inventory;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import main.MainChar;
import main.Sim;

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
			statValue=main.getStats()[index];
		}
		if(index>3&&index<7){
			statValue=main.getPoints()[index-4];
		}
		if(index==7){
			statValue=main.getGold();
		}
	}
	@Override
	public boolean act(float delta) {
		if(index<=3){
			statValue=main.getStats()[index]-0.001;
			main.getStats()[index]=statValue;
			label.setText(java.lang.Math.round(statValue) +"/10");
		}
		if(index>3&&index<7){
			statValue=main.getPoints()[index-4];
			label.setText(java.lang.Math.round(statValue) +"/100");
		}
		
		if(index==7){
			statValue=main.getGold();
			label.setText("Gold: "+java.lang.Math.round(statValue));
		}
		
		return false;
	}
	
	

}
