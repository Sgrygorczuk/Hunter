package com.mygdx.templet.screens.textures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MainScreenTextures {

    //============================================= Textures =======================================
    public Texture menuBackgroundTexture;   //Pop up menu to show menu buttons and Help screen
    public Texture controlsTexture;
    public Texture eTexture;
    public Texture qTexture;

    //=================== Physical Objects ==================
    public Texture spikeTexture;
    public Texture momTexture;
    public Texture momPortTexture;
    public Texture manTexture;
    public Texture manPortTexture;

    //=================================== Background ===============================================
    public Texture backgroundColor;
    public Texture backgroundBack;
    public Texture backgroundMid;
    public Texture backgroundFront;

    //============================= Enemy =================================================
    public TextureRegion[][] dummySpriteSheet;

    public TextureRegion[][] buttonSpriteSheet;

    public TextureRegion[][] heroSpriteSheet;

    public TextureRegion[][] npcSpriteSheet;
    public TextureRegion[][] childSpriteSheet;
    public TextureRegion[][] petSpriteSheet;

    public MainScreenTextures(){ showTextures(); }

    /**
     * Purpose: Sets up all of the textures
     */
    private void showTextures(){
        //=================================== UI ==========================================
        menuBackgroundTexture = new Texture(Gdx.files.internal("UI/BoarderBox.png"));
        controlsTexture = new Texture(Gdx.files.internal("UI/Instructions.png"));
        eTexture = new Texture(Gdx.files.internal("Sprites/E.png"));
        qTexture = new Texture(Gdx.files.internal("Sprites/Q.png"));

        //======================= Physical Objects ========================================
        spikeTexture = new Texture(Gdx.files.internal("Sprites/Spikes.png"));

        //========================= NPCs =================================================
        momTexture = new Texture(Gdx.files.internal("Sprites/NPC/Mom.png"));
        momPortTexture = new Texture(Gdx.files.internal("Sprites/NPC/MomPort.png"));
        manTexture = new Texture(Gdx.files.internal("Sprites/NPC/Man.png"));
        manPortTexture = new Texture(Gdx.files.internal("Sprites/NPC/ManPort.png"));

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
        Texture heroTexturePath = new Texture(Gdx.files.internal("Sprites/Hero.png"));
        heroSpriteSheet = new TextureRegion(heroTexturePath).split(
                heroTexturePath.getWidth()/4, heroTexturePath.getHeight()/2);


        //================================== NPCs ==================================================
        Texture npcTexturePath = new Texture(Gdx.files.internal("Sprites/NPC.png"));
        npcSpriteSheet = new TextureRegion(npcTexturePath).split(
                npcTexturePath.getWidth()/4, npcTexturePath.getHeight()/2);

        Texture childTexturePath = new Texture(Gdx.files.internal("Sprites/Child.png"));
        childSpriteSheet = new TextureRegion(childTexturePath).split(
                childTexturePath.getWidth()/4, childTexturePath.getHeight());

        Texture petTexturePath = new Texture(Gdx.files.internal("Sprites/Pet.png"));
        petSpriteSheet = new TextureRegion(petTexturePath).split(
                petTexturePath.getWidth()/2, petTexturePath.getHeight()/4);

        //========================== Enemy ====================================
        Texture dummyTexturePath = new Texture(Gdx.files.internal("Sprites/Dummy.png"));
        dummySpriteSheet = new TextureRegion(dummyTexturePath).split(
                dummyTexturePath.getWidth()/3, dummyTexturePath.getHeight());
    }

}
