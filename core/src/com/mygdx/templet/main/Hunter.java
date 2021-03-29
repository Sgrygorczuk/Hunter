package com.mygdx.templet.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.mygdx.templet.screens.LoadingScreen;

public class Hunter extends Game {

	//Holds the UI images and sound files
	private final AssetManager assetManager = new AssetManager();
	//Holds all the saved data
	private final GeneralData generalData = new GeneralData();

	/**
	 * Purpose: Tells the game what controls/information it should provide
	*/
	public Hunter(){}

	/**
	 * 	Purpose: Returns asset manager with all its data
	 * 	Output: Asset Manager
	*/
	public AssetManager getAssetManager() { return assetManager; }

	/**
	 * Purpose: Return the current state of the game
	 * @return gets all data that may have been changed by player
	 */
	public GeneralData getGeneralData() { return  generalData; }

	/**
	Purpose: Starts the app
	*/
	@Override
	public void create () {
		assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
		setScreen(new LoadingScreen(this));
	}

}
