package com.mygdx.templet.screens.textures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MainScreenTextures {

    //============================================= Textures =======================================
    public Texture menuBackgroundTexture;   //Pop up menu to show menu buttons and Help screen
    public Texture controlsTexture;

    public TextureRegion[][] buttonSpriteSheet;

    public TextureRegion[][] heroSpriteSheet;

    public MainScreenTextures(){ showTextures(); }

    /**
     * Purpose: Sets up all of the textures
     */
    private void showTextures(){
        menuBackgroundTexture = new Texture(Gdx.files.internal("UI/BoarderBox.png"));
        controlsTexture = new Texture(Gdx.files.internal("UI/Instructions.png"));

        //================================ Button ================================================
        Texture menuButtonTexturePath = new Texture(Gdx.files.internal("UI/Button.png"));
        buttonSpriteSheet = new TextureRegion(menuButtonTexturePath).split(
                menuButtonTexturePath.getWidth()/2, menuButtonTexturePath.getHeight());

        //=============================== Hero ======================================================
        Texture heroTexturePath = new Texture(Gdx.files.internal("Sprites/Orc.png"));
        heroSpriteSheet = new TextureRegion(heroTexturePath).split(
                heroTexturePath.getWidth()/4, heroTexturePath.getHeight());
    }

}
