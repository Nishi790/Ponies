package DialogEngine;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import Main.NPC;

public class Messages extends com.badlogic.gdx.scenes.scene2d.ui.Dialog{
	DialogEngine.Dialog currentDialog;
	
	public Messages(DialogEngine.Dialog current,NPC n, Skin skin){
		super(n.getName(),skin);
		currentDialog=current;
		addListener(new ClickListener());
		text(current.displayPrompt());
		for(String response:current.displayResponses()){
			button(new TextButton(response, skin));
		}
	}
	
	
	
	

}
