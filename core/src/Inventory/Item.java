package inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Item {

	int itemID;
	FileHandle data;
	int value;//in Gp
	public String name;//string
	private String description;
	Image icon;
	
	
	public Item(FileHandle f){
		data=f;
		itemID=Integer.parseInt(f.nameWithoutExtension().replace("Item", ""));
		parseData();
	}
	
	//read data file to determine item parameters
	private void parseData(){
		String[] temp=data.readString().split(":");
		name=temp[0];
		value=Integer.parseInt(temp[1].trim());
		setDescription(temp[2]);
		icon=new Image(new Texture(Gdx.files.internal("data/Items/"+name+itemID+".png")));
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
