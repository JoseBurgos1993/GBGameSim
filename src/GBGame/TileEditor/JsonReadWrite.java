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
		System.out.println("This is the Json constructor.");
	}
	
	JSONObject readJsonfromFile(final String filename) {
		JSONObject contents = null;
		return contents;
	}
	
	JSONObject readFromFile() {
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
	
	void writeToFile(final JSONObject json) {
		System.out.println("Attempting to write to tiles.json.");
		try(FileWriter file = new FileWriter("./tiles.json")){
			file.write(json.toString());
			file.flush();
			System.out.println("Success.");
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	void writeJsonObjectToFile(final JSONObject json) {
		JSONObject obj = new JSONObject();
		
		//JSONObject class creates a json object,
		//provides a put function to insert the details into json object
		obj.put("name", "Abhishek Sharma");
		obj.put("department","B.E");
		obj.put("branch", "C.S.E");
		obj.put("year", 3);

		JSONArray list = new JSONArray();
		
		//This is a JSON Array List , 
		//It creates an array and then add the values in it  
		list.put("remark 1");
		list.put("remark 2");
		list.put("remark 3");

		obj.put("remarks", list);//adding the list to our JSON Object

		try (FileWriter file = new FileWriter("./newfile.json")) {
			//File Writer creates a file in write mode at the given location 
			file.write(obj.toString());

			//write function is use to write in file,
			//here we write the Json object in the file
			file.flush();

		}
		catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println(obj);
		//to print our JSon object
	}
}
