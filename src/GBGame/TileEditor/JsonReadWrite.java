package GBGame.TileEditor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonReadWrite {

	public JsonReadWrite() {
		// TODO Auto-generated constructor stub
	}
	
	public JSONObject readFromFile(final String filename) {
		JSONObject json = new JSONObject();;
		System.out.println("Attempting to read from tiles.json.");
		
		try(BufferedReader file = new BufferedReader(new FileReader("./tiles.json"))){
			//json.pa
			StringBuilder sb = new StringBuilder();
		    String line;
		    while ((line = file.readLine()) != null) {
		        sb.append(line);
		    }
		    json = new JSONObject(sb.toString());
		} catch(IOException e) {
			e.printStackTrace();
		}
		return json;
	}
	
	public void writeToFile(final JSONObject json, final String filename) {
		System.out.println("Attempting to write to tiles.json.");
		try(FileWriter file = new FileWriter("./tiles.json")){
			file.write(json.toString());
			file.flush();
			System.out.println("Success.");
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
