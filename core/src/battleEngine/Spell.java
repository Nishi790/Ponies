package battleEngine;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Action;

public class Spell extends Attack {
	
	public Spell(FileHandle f, BattleCharacter attacker){
		super(f, attacker);
	}

	@Override
	public boolean act(float delta) {
		// TODO Auto-generated method stub
		return false;
	}

}
