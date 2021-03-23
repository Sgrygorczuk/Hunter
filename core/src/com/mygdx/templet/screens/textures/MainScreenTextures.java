package com.mygdx.templet.screens.textures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MainScreenTextures {

    //============================================= Textures =======================================
    public Texture menuBackgroundTexture;   //Pop up menu to show menu buttons and Help screen
    public Texture controlsTexture;

    public Texture spikeTexutre;

    //=================================== Background ===============================================
    public Texture backgroundColor;
    public Texture backgroundBack;
    public Texture backgroundMid;
    public Texture backgroundFront;

    public TextureRegion[][] buttonSpriteSheet;

    public TextureRegion[][] heroSpriteSheet;

    public TextureRegion[][] npcSpriteSheet;

    public MainScreenTextures(){ showTextures(); }

    /**
     * Purpose: Sets up all of the textures
     */
    private void showTextures(){
        menuBackgroundTexture = new Texture(Gdx.files.internal("UI/BoarderBox.png"));
        controlsTexture = new Texture(Gdx.files.internal("UI/Instructions.png"));

        spikeTexutre = new Texture(Gdx.files.internal("Sprites/Spikes.png"));

        //================================== Background ============================================
        backgroundColor = new Texture(Gdx.files.internal("Sprites/BackgroundColor.png"));
        backgroundBack = new Texture(Gdx.files.internal("Sprites/BackgroundBack.png"));
        backgroundMid = new Texture(Gdx.files.internal("Sprites/BackgroundMiddle.png"));
        backgroundFront = new Texture(Gdx.files.internal("Sprites/BackgroundFront.png"));

        //================================ Button ================================================
        Texture menuButtonTexturePath = new Texture(Gdx.files.internal("UI/Button.png"));
        buttonSpriteSheet = new TextureRegion(menuButtonTexturePath).split(
                menuButtonTexturePath.getWidth()/2, menuButtonTexturePath.getHeight());

        //=============================== Hero ======================================================
        Texture heroTexturePath = new Texture(Gdx.files.internal("Sprites/Orc.png"));
        heroSpriteSheet = new TextureRegion(heroTexturePath).split(
                heroTexturePath.getWidth()/4, heroTexturePath.getHeight());

        Texture npcTexturePath = new Texture(Gdx.files.internal("Sprites/NPC.png"));
        npcSpriteSheet = new TextureRegion(npcTexturePath).split(
                npcTexturePath.getWidth()/4, npcTexturePath.getHeight()/2);

    }

}
