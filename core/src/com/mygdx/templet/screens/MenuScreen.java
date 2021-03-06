package com.mygdx.templet.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.templet.main.Hunter;
import com.mygdx.templet.screens.textures.MenuScreenTextures;
import com.mygdx.templet.tools.MusicControl;
import com.mygdx.templet.tools.TextAlignment;

import static com.mygdx.templet.Const.LVL_COUNT;
import static com.mygdx.templet.Const.MENU_BUTTON_FONT;
import static com.mygdx.templet.Const.MENU_BUTTON_HEIGHT;
import static com.mygdx.templet.Const.MENU_BUTTON_WIDTH;
import static com.mygdx.templet.Const.NUM_BUTTONS_MENU_SCREEN;
import static com.mygdx.templet.Const.WORLD_HEIGHT;
import static com.mygdx.templet.Const.WORLD_WIDTH;

public class MenuScreen extends ScreenAdapter{

    //=========================================== Variable =========================================

    //================================== Image processing ===========================================
    private SpriteBatch batch = new SpriteBatch();
    private Viewport viewport;
    private Camera camera;

    //===================================== Tools ==================================================
    private MusicControl musicControl;
    private MenuScreenTextures menuScreenTextures;
    private final Hunter hunter;
    private final TextAlignment textAlignment = new TextAlignment();

    //=========================================== Text =============================================
    //Font used for the user interaction
    private BitmapFont bitmapFont = new BitmapFont();

    //============================================= Flags ==========================================
    private boolean helpFlag;      //Tells if help menu is up or not
    private boolean levelSelectFlag;   //Tells if credits menu is up or not
    private int levelIndex = 0;
    private boolean levelExit = false;

    //=================================== Miscellaneous Vars =======================================
    //String used on the buttons
    private final String[] buttonText = new String[]{"Play", "Level Select", "Controls"};
    private float backButtonY = 10;
    private int buttonIndex = 0;    //Tells us which button we're currently looking at

    //================================ Set Up ======================================================

    /**
     * Purpose: Grabs the info from main screen that holds asset manager
     * Input: Infamous
     */
    MenuScreen(Hunter hunter) { this.hunter = hunter; }

    /**
     Purpose: Updates the dimensions of the screen
     Input: The width and height of the screen
     */
    @Override
    public void resize(int width, int height) { viewport.update(width, height); }

    //===================================== Show ===================================================

    /**
     * Purpose: Central function for initializing the screen
     */
    @Override
    public void show() {
        showCamera();           //Sets up camera through which objects are draw through
        menuScreenTextures = new MenuScreenTextures();
        showObjects();          //Sets up the font
        musicControl.showMusic(0);
    }

    /**
     * Purpose: Sets up the camera through which all the objects are view through
     */
    private void showCamera(){
        camera = new OrthographicCamera();
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        camera.update();
        viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
    }

    /**
     * Purpose: Sets up objects such as debugger, musicControl, fonts and others
     */
    private void showObjects(){
        musicControl = new MusicControl(hunter.getAssetManager());

        if(hunter.getAssetManager().isLoaded("Fonts/Font.fnt")){bitmapFont = hunter.getAssetManager().get("Fonts/Font.fnt");}
        bitmapFont.getData().setScale(0.6f);
        bitmapFont.setColor(Color.BLACK);
    }

    //=================================== Execute Time Methods =====================================

    /**
     Purpose: Central function for the game, everything that updates run through this function
     */
    @Override
    public void render(float delta) {
        update();       //Update the variables
        draw();
    }

    /**
     Purpose: Updates all the moving components and game variables
     */
    private void update() { inputHandling();}

    /**
     * Purpose: Allow user to navigate the menus
     */
    private void inputHandling(){
        if(!helpFlag && !levelSelectFlag) {
            //Movement Vertically
            if (Gdx.input.isKeyJustPressed(Input.Keys.W) || Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
                buttonIndex--;
                //playSFX("Menu SFX");
                if (buttonIndex <= -1) {
                    buttonIndex = NUM_BUTTONS_MENU_SCREEN - 1;
                }
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.S) || Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
                buttonIndex++;
                //playSFX("Menu SFX");
                if (buttonIndex >= NUM_BUTTONS_MENU_SCREEN) {
                    buttonIndex = 0;
                }
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                //Launches the game
                //playSFX("Menu Confirm");
                if (buttonIndex == 0) {
                    musicControl.stopMusic();
                    hunter.setScreen(new LoadingScreen(hunter, 1, 0));
                }
                else if(buttonIndex == 1){
                    levelSelectFlag = true;
                }
                //Turns on the help menu
                else if (buttonIndex == 2) {
                    helpFlag = true;
                }
            }
        }
        else if(helpFlag){
            if (Gdx.input.isKeyJustPressed(Input.Keys.E) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                //playSFX("Menu Deconfirm");
                helpFlag = false;
            }
        }
        else if(levelSelectFlag){
            if ( Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                //playSFX("Menu Deconfirm");
                levelSelectFlag = false;
                levelIndex = 0;
            }
            if(levelExit){
                if (Gdx.input.isKeyJustPressed(Input.Keys.W) || Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
                    //playSFX("Menu SFX");
                    levelExit = false;
                }
                else if((Gdx.input.isKeyJustPressed(Input.Keys.E))){
                    //playSFX("Menu Confirm");
                    levelSelectFlag = false;
                    levelExit = false;
                    levelIndex = 0;
                }
            }
            else{
                if (Gdx.input.isKeyJustPressed(Input.Keys.S) || Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
                    //playSFX("Menu SFX");
                    levelExit = true;
                }
                else if(levelIndex < LVL_COUNT - 1 && (Gdx.input.isKeyJustPressed(Input.Keys.D) || Gdx.input.isKeyJustPressed(Input.Keys.RIGHT))){
                    //playSFX("Menu SFX");
                    levelIndex ++;
                }
                else if(levelIndex > 0 && (Gdx.input.isKeyJustPressed(Input.Keys.A) || Gdx.input.isKeyJustPressed(Input.Keys.LEFT))){
                    //playSFX("Menu SFX");
                    levelIndex --;
                }

                if(Gdx.input.isKeyJustPressed(Input.Keys.E)){
                    //playSFX("Menu Confirm");
                    musicControl.stopMusic();
                    hunter.setScreen(new LoadingScreen(hunter, 1, levelIndex));
                }
            }
        }
    }

    //========================================== Drawing ===========================================

    /**
     * Purpose: Central drawing function, from here everything gets drawn
     */
    private void draw() {
        clearScreen();
        //Viewport/Camera projection
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
        //Batch setting up texture before drawing buttons
        batch.begin();
        batch.draw(menuScreenTextures.backgroundTexture, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);
        //Draw the pop up menu
        //Draws the Play|Help|Credits text on buttons
        if(!helpFlag && !levelSelectFlag){
            drawMainButtons();
            drawButtonText();
        }
        else if(helpFlag){
            drawInstructions();
        }
        else if(levelSelectFlag){
            drawLevelSelect();
        }

        batch.end();
    }

    /**
     *  Purpose: Clear the screen
     */
    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    /**
     * Purpose: Draws the main buttons on the screen
     */
    private void drawMainButtons(){
        for(int i = 0; i < NUM_BUTTONS_MENU_SCREEN; i++){
            if(i == buttonIndex){
                batch.draw(menuScreenTextures.buttonSpriteSheet[0][1], 10, 2*WORLD_HEIGHT/3 - 15 - (MENU_BUTTON_HEIGHT + 15) * i, MENU_BUTTON_WIDTH, MENU_BUTTON_HEIGHT);
            }
            else{
                batch.draw(menuScreenTextures.buttonSpriteSheet[0][0], 10, 2*WORLD_HEIGHT/3 - 15 - (MENU_BUTTON_HEIGHT + 15) * i, MENU_BUTTON_WIDTH, MENU_BUTTON_HEIGHT);
            }
        }
    }

    /**
     * Purpose: Draws the text on the Play|Help|Credits buttons
     */
    private void drawButtonText(){
        bitmapFont.getData().setScale(MENU_BUTTON_FONT);
        for(int i = 0; i < NUM_BUTTONS_MENU_SCREEN; i ++) {
            textAlignment.centerText(batch, bitmapFont, buttonText[i], 10 + MENU_BUTTON_WIDTH/2f,
                    2*WORLD_HEIGHT/3 + 0.65f * MENU_BUTTON_HEIGHT - 15 - (MENU_BUTTON_HEIGHT + 15) * i);
        }
    }

    private void drawLevelSelect() {
        bitmapFont.setColor(Color.WHITE);
        bitmapFont.getData().setScale(0.5f);

        batch.draw(menuScreenTextures.menuBackgroundTexture, 0, 0);

        textAlignment.centerText(batch, bitmapFont, "Level Select",   WORLD_WIDTH/2f, WORLD_HEIGHT - 30);


        //============================= Draws the boxes Cole in them with different colors =========
        bitmapFont.getData().setScale(0.4f);
        /*
        for (int i = 0; i < 5; i++) {
            if (levelIndex == i) {
                batch.draw(menuScreenTextures.buttonSpriteSheet[0][1], 30 + (30 + 10) * i, WORLD_HEIGHT / 2f, 30, 30);
            } else {
                batch.draw(menuScreenTextures.buttonSpriteSheet[0][0], 30 + (30 + 10) * i,  WORLD_HEIGHT / 2f, 30, 30);
            }
            if(infamous.getCollected(i)){
                batch.draw(menuScreenTextures.collectibleSpriteSheet[0][0], 30 + (30 + 10) * i + 15, WORLD_HEIGHT / 2f + 40, 5, 5);
            }
            if(infamous.getHealed(i)){
                batch.draw(menuScreenTextures.peopleUpSpriteSheet[0][0], 30 + (30 + 10) * i + 5, WORLD_HEIGHT / 2f + 40, 5, 5);
            }
            if(infamous.getKilled(i)){
                batch.draw(menuScreenTextures.peopleUpSpriteSheet[0][4], 30 + (30 + 10) * i + 25, WORLD_HEIGHT / 2f + 40, 5, 5);
            }
            textAlignment.centerText(batch, bitmapFont, "" + (i+1), 46 + (30 + 10) * i,  WORLD_HEIGHT / 2f + 19);
        }


        for (int i = 5; i < 6; i++) {
            if (levelIndex == i) {
                batch.draw(menuScreenTextures.buttonSpriteSheet[0][1], 30 + (30 + 10) * (i - 5), WORLD_HEIGHT / 2f - 45, 30, 30);
            } else {
                batch.draw(menuScreenTextures.buttonSpriteSheet[0][0], 30 + (30 + 10) * (i - 5),  WORLD_HEIGHT / 2f - 45, 30, 30);
            }
            textAlignment.centerText(batch, bitmapFont, "" + (i+1), 46 + (30 + 10) * (i - 5),  WORLD_HEIGHT / 2f + 19 - 45);
            if(infamous.getCollected(i)){
                batch.draw(menuScreenTextures.collectibleSpriteSheet[0][0], 30 + (30 + 10) * (i - 5) + 15, WORLD_HEIGHT / 2f + 40 - 45, 5, 5);
            }
            if(infamous.getHealed(i)){
                batch.draw(menuScreenTextures.peopleUpSpriteSheet[0][0], 30 + (30 + 10) * (i - 5) + 5, WORLD_HEIGHT / 2f + 40 - 45, 5, 5);
            }
            if(infamous.getKilled(i)){
                batch.draw(menuScreenTextures.peopleUpSpriteSheet[0][4], 30 + (30 + 10) * (i - 5) + 25, WORLD_HEIGHT / 2f + 40 - 45, 5, 5);
            }
        }


        bitmapFont.getData().setScale(0.3f);
        if(levelExit){
            batch.draw(menuScreenTextures.buttonSpriteSheet[0][1], WORLD_WIDTH / 2f - MENU_BUTTON_WIDTH / 2f, 30, MENU_BUTTON_WIDTH, MENU_BUTTON_HEIGHT);
        }
        else{
            batch.draw(menuScreenTextures.buttonSpriteSheet[0][0], WORLD_WIDTH / 2f - MENU_BUTTON_WIDTH / 2f, 30, MENU_BUTTON_WIDTH, MENU_BUTTON_HEIGHT);
        }


        textAlignment.centerText(batch, bitmapFont, "Back", WORLD_WIDTH/2f, 47);
        */
    }

    /**
     * Purpose: Draws the text for instructions
     */
    private void drawInstructions() {
        batch.draw(menuScreenTextures.controlsTexture, 0, 0);
    }


    /**
     * Purpose: Gets rid of all visuals
     */
    @Override
    public void dispose() {
    }
}