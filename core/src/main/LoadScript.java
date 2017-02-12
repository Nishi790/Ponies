package main;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.JsePlatform;

import com.badlogic.gdx.Gdx;

import battleEngine.BattleCharacter;

public class LoadScript implements IScript{

	private Globals globals=JsePlatform.standardGlobals();
	private LuaValue chunk;
	private boolean scriptFileExists;
	BattleCharacter monster;
	
	public LoadScript(final String fileName, BattleCharacter b){
		monster=b;
		if(!Gdx.files.internal("data/Monsters/"+fileName).exists()){
			scriptFileExists=false;
		}
		else{
			scriptFileExists=true;
		}
		chunk=globals.loadfile("data/Monsters/"+fileName);
		chunk.call();
	}
	
	@Override
	public boolean canExecute(){
		return scriptFileExists;
	}
	
	@Override
	public void executeInit(){
		if(!canExecute())return;
		LuaValue func=globals.get("create");
		func.invoke(new LuaValue[]
				{CoerceJavaToLua.coerce(monster)});
	}
	
	public void execute(String function, Object obj){
		if(!canExecute()) return;
		globals.get(function).invoke(new LuaValue[]
				{CoerceJavaToLua.coerce(obj)});
	}
	
}
