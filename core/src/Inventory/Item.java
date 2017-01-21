package Inventory;

import com.badlogic.gdx.files.FileHandle;

public class Item {

	int itemID;
	FileHandle data;
	int value;//in Gp
	public String name;//string
	private String description;
	
	
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
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
