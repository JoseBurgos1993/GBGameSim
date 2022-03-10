package GBGame.Engine;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

import GBGame.TileEditor.JsonReadWrite;

public class GameManager {
	// This will be run at the launch. It contains all the information for the current game.
	// My hope is that most everything else can be used for any type of game for this style,
	// But classes like this one load the specific information for that particular game.
	// For example, one might be for a Mario-like game, another for a RPG.

	private JsonReadWrite jsonReadWrite = new JsonReadWrite();
	
	// Game Data \\
	private Tile tileSet[] = new Tile[256]; // All tiles. Panel will have all of these just because it would be a pain to do otherwise.
	//private Entity entitySet[] = new Entity[100]; // Contains all entities in the game. The panel holds copies of these (can have multiple). Panel doesn't hold entities it doesn't currently need;
	//private Entity windowSet[] = new Entity[100]; // Contains all the window entities in the game. I could bunch this in with entitySet, but I'd rather keep them separate, unless something comes up.
	
	// TEMP \\
	private byte BackgroundLayer[] = new byte[440];
	private Entity EntityLayer[] = new Entity[2];
	private Sprite SpriteLayer[] = new Sprite[256];
	private Sprite WindowLayer[] = new Sprite[256];
	
	public GameManager() {
		// TODO The constructor for the Game Manager should read from files, create tiles, create sprites, and whatever else is needed.
		// Then it needs to be available for the Game Panel to pull.
		
		// Read tileset
		createTiles();
		readFileData();
		// Read sprite data
		
		// Read entity data
		
		// Create and package it neatly
		
		// Return that to GamePanel? Or no, maybe this class can handle it. GamePanel can handle the graphics, sound, window, etc.
	}
	private void readFileData() {
		JSONObject json = jsonReadWrite.readFromFile("./tiles.json");
		Iterator<String> keys = json.keys();
		Tile tile = new Tile();
		JSONArray jsonArray = new JSONArray();
		int[] temp1;
		byte[] temp2;
		int n = 0;
		
		while(keys.hasNext()) {
			String key = keys.next();
			if(json.get(key) instanceof JSONArray) {
				//savedTileNames[numberOfSavedTiles] = (String) key;
				jsonArray = (JSONArray) json.get(key);
				
				temp1 = new int[64];
				temp2 = new byte[64];
				
				for(int i = 0; i < 64; i++) {
					temp1[i] = (int) jsonArray.get(i);
					temp2[i] = (byte) temp1[i];
				}
				
				tile = new Tile(temp2);
				//savedTiles[numberOfSavedTiles] = tile;
				tileSet[n] = tile;
				n++;
			}
		}
	}
	private void createTiles(){
		// Creates Background Tile (the smiley face)
		byte tile1[] = new byte[64];
		//Arrays.fill(tile1, 1); // Default filling the array with dark grey pixels. Followed by filling in specific elements with the colors to make the face.
		for(int i = 0; i < tile1.length; i++) {
			tile1[i] = 1;
		}
		
		tile1[10] = 3;
		tile1[13] = 3;
		tile1[18] = 3;
		tile1[21] = 3;

		tile1[33] = 0;
		tile1[38] = 0;
		tile1[42] = 0;
		tile1[45] = 0;
		tile1[51] = 0;
		tile1[52] = 0;
		
		tileSet[0] = new Tile(tile1); // Adds this tile to the tileSet for the system
		//Arrays.fill(BackgroundLayer, 0); // Fills the whole background layer with this tile.
		for(int i = 0; i < BackgroundLayer.length; i++) {
			BackgroundLayer[i] = 0;
		}
		// Creating pokemon boy. First 4 lines create 4 tiles with the appropriate colors.
		// Stand down *DONE
		byte pokTile0[] = new byte[] {4,4,4,4,4,0,0,0,  4,4,4,4,0,1,1,1,  4,4,4,0,1,1,1,1,  4,4,4,0,1,1,1,1,  4,4,0,0,0,1,2,2,  4,4,0,0,1,0,0,0,  4,0,2,0,2,2,2,2,  4,0,2,2,2,2,0,2};
		byte pokTile1[] = new byte[] {0,0,0,4,4,4,4,4,  1,1,1,0,4,4,4,4,  1,1,1,1,0,4,4,4,  1,1,1,1,0,4,4,4,  2,2,1,0,0,0,4,4,  0,0,0,1,0,0,4,4,  2,2,2,2,0,2,0,4,  2,0,2,2,2,2,0,4}; // Mirror of previous
		byte pokTile2[] = new byte[] {4,4,0,0,2,2,0,2,  4,4,0,0,0,2,2,1,  4,0,2,2,0,0,0,0,  4,0,2,2,0,0,0,0,  4,4,0,0,0,1,1,0,  4,4,4,0,1,0,0,1,  4,4,4,0,1,1,1,0,  4,4,4,4,0,0,0,4};
		byte pokTile3[] = new byte[] {2,0,2,2,0,0,4,4,  1,2,2,0,0,0,4,4,  0,0,0,0,2,2,0,4,  0,0,0,0,2,2,0,4,  0,1,1,0,0,0,4,4,  1,0,0,1,0,4,4,4,  0,1,1,1,0,4,4,4,  4,0,0,0,4,4,4,4}; // Mirror of previous
		
		// Adds these tiles to the tileSet
		tileSet[1] = new Tile(pokTile0);
		tileSet[2] = new Tile(pokTile1);
		tileSet[3] = new Tile(pokTile2);
		tileSet[4] = new Tile(pokTile3);
		
		// Creates the array that is used to pick the tiles used for the upcoming sprite/entity.
		int testArr[] = new int[] {1,2,3,4};
		
		// Creates the sprite and then adds it to a sprite array. This is because an entity can consist of multiple sprites (i.e. one for looking up, another for left, walking frame 1, walk frame 2, etc)
		Sprite pokerman1 = new Sprite(testArr,2,2);
		Sprite pokermanArr[] = new Sprite[6];
		pokermanArr[0] = pokerman1;
		///////////////////////////////////////////////////////////////
		// More SPRITES! \\
		// Walk down *DONE
		pokTile0 = new byte[] {4,4,4,4,4,4,4,4, 4,4,4,4,4,0,0,0, 4,4,4,4,0,1,1,1, 4,4,4,0,1,1,1,1, 4,4,4,0,1,1,1,1, 4,4,0,0,0,1,2,2, 4,4,0,0,0,0,0,0, 4,0,2,0,2,2,2,2};
		pokTile1 = new byte[] {4,4,4,4,4,4,4,4, 0,0,0,4,4,4,4,4, 1,1,1,0,4,4,4,4, 1,1,1,1,0,4,4,4, 1,1,1,1,0,4,4,4, 2,2,1,0,0,0,4,4, 0,0,0,0,0,0,4,4, 2,2,2,2,0,2,0,4}; // Mirror of previous
		pokTile2 = new byte[] {4,0,2,2,2,2,0,2, 4,0,0,0,2,2,0,2, 4,0,2,0,0,2,2,1, 4,4,0,0,0,0,0,0, 4,4,4,0,0,1,0,0, 4,4,4,4,0,0,0,1, 4,4,4,4,0,1,1,0, 4,4,4,4,4,0,0,0};
		pokTile3 = new byte[] {2,0,2,2,2,2,0,4, 2,0,2,2,0,0,4,4, 1,2,2,0,1,0,4,4, 0,0,0,0,2,0,4,4, 0,0,2,2,0,0,4,4, 1,0,2,2,0,4,4,4, 0,4,0,0,4,4,4,4, 4,4,4,4,4,4,4,4};

		tileSet[5] = new Tile(pokTile0);
		tileSet[6] = new Tile(pokTile1);
		tileSet[7] = new Tile(pokTile2);
		tileSet[8] = new Tile(pokTile3);

		testArr = new int[] {5,6,7,8};
		
		pokerman1 = new Sprite(testArr,2,2);
		pokermanArr[1] = pokerman1;
		
		// Stand left *DONE
		
		pokTile0 = new byte[] {4,4,4,4,4,0,0,0, 4,4,4,4,0,1,1,1, 4,4,4,0,1,1,1,1, 4,4,0,0,2,1,1,1, 4,0,2,2,2,2,1,1, 4,4,0,0,1,1,1,0, 4,4,4,0,2,0,2,2, 4,4,4,0,2,0,2,2};
		pokTile1 = new byte[] {0,0,0,4,4,4,4,4, 1,1,1,0,4,4,4,4, 1,1,1,1,0,4,4,4, 1,1,1,1,0,4,4,4, 1,1,1,0,0,0,4,4, 0,0,0,0,0,0,4,4, 0,0,0,0,0,0,4,4, 0,2,2,0,0,4,4,4};
		pokTile2 = new byte[] {4,4,4,0,2,2,2,2, 4,4,4,4,0,1,2,2, 4,4,4,4,4,0,0,0, 4,4,4,4,4,4,0,0, 4,4,4,4,4,4,0,0, 4,4,4,4,4,0,1,1, 4,4,4,4,4,0,1,1, 4,4,4,4,4,4,0,0};
		pokTile3 = new byte[] {2,2,2,0,4,4,4,4, 2,0,0,1,0,4,4,4, 0,0,1,1,0,4,4,4, 2,2,0,1,0,4,4,4, 2,2,0,1,0,4,4,4, 0,0,0,0,4,4,4,4, 1,1,0,4,4,4,4,4, 0,0,4,4,4,4,4,4};

		tileSet[9] = new Tile(pokTile0);
		tileSet[10] = new Tile(pokTile1);
		tileSet[11] = new Tile(pokTile2);
		tileSet[12] = new Tile(pokTile3);

		testArr = new int[] {9,10,11,12};
		
		pokerman1 = new Sprite(testArr,2,2);
		pokermanArr[2] = pokerman1;
		
		// Walk left *DONE
		pokTile0 = new byte[] {4,4,4,4,4,4,4,4, 4,4,4,4,4,0,0,0, 4,4,4,4,0,1,1,1, 4,4,4,0,1,1,1,1, 4,4,0,0,2,1,1,1, 4,0,2,2,2,2,1,1, 4,4,0,0,1,1,1,0, 4,4,4,0,2,0,2,2};
		pokTile1 = new byte[] {4,4,4,4,4,4,4,4, 0,0,0,4,4,4,4,4, 1,1,1,0,4,4,4,4, 1,1,1,1,0,4,4,4, 1,1,1,1,0,4,4,4, 1,1,1,0,0,0,4,4, 0,0,0,0,0,0,4,4, 0,0,0,0,0,0,4,4};
		pokTile2 = new byte[] {4,4,4,0,2,0,2,2, 4,4,4,0,2,2,2,2, 4,4,4,4,0,1,2,2, 4,4,4,4,4,0,0,0, 4,4,4,0,0,0,0,0, 4,4,0,1,1,0,1,1, 4,4,4,0,1,1,0,0, 4,4,4,4,0,0,0,4};
		pokTile3 = new byte[] {0,2,2,0,0,4,4,4, 2,2,2,0,4,4,4,4, 2,0,0,1,0,4,4,4, 0,0,0,1,0,4,4,4, 0,2,2,0,0,4,4,4, 0,2,2,0,1,0,4,4, 0,0,0,1,1,0,4,4, 4,4,4,0,0,4,4,4};

		tileSet[13] = new Tile(pokTile0);
		tileSet[14] = new Tile(pokTile1);
		tileSet[15] = new Tile(pokTile2);
		tileSet[16] = new Tile(pokTile3);

		testArr = new int[] {13,14,15,16};
		
		pokerman1 = new Sprite(testArr,2,2);
		pokermanArr[3] = pokerman1;
		
		// Stand north *DONE
		pokTile0 = new byte[] {4,4,4,4,4,0,0,0, 4,4,4,4,0,1,1,1, 4,4,4,0,1,1,1,1, 4,4,4,0,1,1,1,1, 4,4,0,0,1,1,1,1, 4,4,0,0,0,1,1,1, 4,0,2,0,0,0,0,0, 4,0,2,2,0,0,0,0};
		pokTile1 = new byte[] {0,0,0,4,4,4,4,4, 1,1,1,0,4,4,4,4, 1,1,1,1,0,4,4,4, 1,1,1,1,0,4,4,4, 1,1,1,1,0,0,4,4, 1,1,1,0,0,0,4,4, 0,0,0,0,0,2,0,4, 0,0,0,0,2,2,0,4}; // Mirror of previous
		pokTile2 = new byte[] {4,4,0,0,2,2,0,0, 4,4,0,0,0,0,1,1, 4,0,2,0,0,1,0,0, 4,0,2,0,0,1,1,2, 4,4,0,0,0,0,1,1, 4,4,4,0,1,0,0,0, 4,4,4,0,1,1,1,0, 4,4,4,4,0,0,0,4};
		pokTile3 = new byte[] {0,0,2,2,0,0,4,4, 1,1,0,0,0,0,4,4, 0,0,1,0,0,2,0,4, 2,1,1,0,0,2,0,4, 1,1,0,0,0,0,4,4, 0,0,0,1,0,4,4,4, 0,1,1,1,0,4,4,4, 4,0,0,0,4,4,4,4}; // Mirror of previous

		tileSet[17] = new Tile(pokTile0);
		tileSet[18] = new Tile(pokTile1);
		tileSet[19] = new Tile(pokTile2);
		tileSet[20] = new Tile(pokTile3);
		
		testArr = new int[] {17,18,19,20};
		
		pokerman1 = new Sprite(testArr,2,2);
		pokermanArr[4] = pokerman1;
		
		// Walk north * DONE
		pokTile0 = new byte[] {4,4,4,4,4,4,4,4, 4,4,4,4,4,0,0,0, 4,4,4,4,0,1,1,1, 4,4,4,0,1,1,1,1, 4,4,4,0,1,1,1,1, 4,4,0,0,1,1,1,1, 4,4,0,0,0,1,1,1, 4,0,2,0,0,0,0,0};
		pokTile1 = new byte[] {4,4,4,4,4,4,4,4, 0,0,0,4,4,4,4,4, 1,1,1,0,4,4,4,4, 1,1,1,1,0,4,4,4, 1,1,1,1,0,4,4,4, 1,1,1,1,0,0,4,4, 1,1,1,0,0,0,4,4, 0,0,0,0,0,2,0,4}; // Mirror of previous
		pokTile2 = new byte[] {4,0,2,2,0,0,0,0, 4,0,0,0,2,2,0,0, 4,0,2,0,0,0,1,1, 4,4,0,0,0,1,0,0, 4,4,4,0,0,1,1,2, 4,4,4,4,0,0,1,1, 4,4,4,4,0,1,0,0, 4,4,4,4,4,0,0,0};
		pokTile3 = new byte[] {0,0,0,0,2,2,0,4, 0,0,2,2,0,0,4,4, 1,1,0,0,0,0,4,4, 0,0,1,0,2,2,0,4, 2,1,1,0,2,2,0,4, 1,1,0,0,0,0,4,4, 0,0,4,4,4,4,4,4, 4,4,4,4,4,4,4,4};

		tileSet[21] = new Tile(pokTile0);
		tileSet[22] = new Tile(pokTile1);
		tileSet[23] = new Tile(pokTile2);
		tileSet[24] = new Tile(pokTile3);

		testArr = new int[] {21,22,23,24};
		
		pokerman1 = new Sprite(testArr,2,2);
		pokermanArr[5] = pokerman1;
		
		
		
		
		
		///////////////////////////////////////////////////////////////
		// Creates the entity. The second line is just a duplicate to make sure it worked.
		Entity pokerman = new Entity("Player", pokermanArr);
		Entity pokermanDos = new Entity("Player", pokermanArr);
		
		// Changing spawn location of the second one.
		pokermanDos.setLocation(90, 25);
		
		// Adds the entities to the entity layer. Later, there should just be a list of all entities in the game and the entity layer should just grab from that when needed.
		EntityLayer[0] = pokerman;
		EntityLayer[1] = pokermanDos;
	}
	
	public Tile[] getTileSet() {
		return tileSet;
	}
	
	public byte[] getBackgroundLayer() {
		return BackgroundLayer;
	}
	public Entity[] getEntityLayer() {
		return EntityLayer;
	}
	public Sprite[] getSpriteLayer(){
		return SpriteLayer;
	}
	public Sprite[] getWindowLayer() {
		return WindowLayer;
	}
}
