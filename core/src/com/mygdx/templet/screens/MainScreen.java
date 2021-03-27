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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.templet.objects.Platform;
import com.mygdx.templet.objects.aliveObjects.enemies.Dummy;
import com.mygdx.templet.objects.aliveObjects.enemies.Enemy;
import com.mygdx.templet.objects.aliveObjects.Hero;
import com.mygdx.templet.objects.aliveObjects.Pet;
import com.mygdx.templet.objects.aliveObjects.WalkingNPC;
import com.mygdx.templet.objects.staticObjects.TalkingNPC;
import com.mygdx.templet.objects.staticObjects.Spike;
import com.mygdx.templet.screens.textures.MainScreenTextures;
import com.mygdx.templet.tools.DebugRendering;
import com.mygdx.templet.main.Hunter;
import com.mygdx.templet.tools.MusicControl;
import com.mygdx.templet.tools.TextAlignment;
import com.mygdx.templet.tools.TiledSetUp;

import static com.mygdx.templet.Const.HERO_X_VELOCITY;
import static com.mygdx.templet.Const.NUM_BUTTONS_MAIN_SCREEN;
import static com.mygdx.templet.Const.MENU_BUTTON_HEIGHT;
import static com.mygdx.templet.Const.MENU_BUTTON_WIDTH;
import static com.mygdx.templet.Const.MENU_BUTTON_Y_START;
import static com.mygdx.templet.Const.WORLD_HEIGHT;
import static com.mygdx.templet.Const.WORLD_WIDTH;

class MainScreen extends ScreenAdapter {

    //=========================================== Variable =========================================

    //================================== Image processing ===========================================
    private Viewport viewport;			                     //The screen where we display things
    private Camera camera;				                     //The camera viewing the viewport
    private final SpriteBatch batch = new SpriteBatch();	 //Batch that holds all of the textures

    //=================================== Buttons ==================================================

    //===================================== Tools ==================================================
    private final Hunter hunter;      //Game object that holds the settings
    private DebugRendering debugRendering;        //Draws debug hit-boxes
    private MusicControl musicControl;            //Plays Music
    private final TextAlignment textAlignment = new TextAlignment();

    //=========================================== Text =============================================
    //Font used for the user interaction
    private BitmapFont bitmapFont = new BitmapFont();             //Font used for the user interaction
    private final BitmapFont bitmapFontDeveloper = new BitmapFont();    //Font for viewing phone stats in developer mode
    private MainScreenTextures mainScreenTextures;
    private TiledSetUp tiledSetUp;               //Takes all the data from tiled

    //============================================= Flags ==========================================
    private boolean developerMode = false;      //Developer mode shows hit boxes and phone data
    private boolean pausedFlag = false;         //Stops the game from updating
    private boolean endFlag = false;            //Tells us game has been lost
    private boolean helpFlag = false;           //Tells us if help flag is on or off
    private float xCameraDelta = 0;             //Keeps track of how far the camera has moved (to update menus)
    private float yCameraDelta = 0;             //Keeps track of how far the camera has moved (to update menus)
    private int buttonIndex = 0;                //Tells us which button we're currently looking at in the main menu
    private boolean isTouchingNPC = false;      //Tells is if we're near an NPC
    private boolean isTalking = false;          //Tells us if player is talking to an NPC
    private boolean letGoJump = true;           //Tells us that the player can still jump few sec after falling
    private boolean cameraPan = false;          //Tells us the camera is paning and player can't move

    //=================================== Miscellaneous Vars =======================================
    private final String[] menuButtonText = new String[]{"Controls", "Skins", "Sound Off", "Main Menu", "Back", "Sound On"};
    private Array<String> levelNames = new Array<>(); //Names of all the lvls in order
    private int tiledSelection;                       //Which tiled map is loaded in

    //Timer counting down until player can be hit again
    private static final float POST_FALL_TIME = 0.3F;       //Counter for grace time to jump after fall
    private float postFallTime = POST_FALL_TIME;            //Counter for grace time to jump after fall
    private boolean canPostFallJump = true;                 //Flag to let player jump after falling

    //================================ Set Up ======================================================

    //============================== Player ============================
    private Hero hero;

    //=========================== Physical Objects =====================
    private final Array<Platform> platforms = new Array<>();
    private final Array<Spike> spikes = new Array<>();

    //========================== NPCs ==================================
    private final Array<TalkingNPC> talkingNPCS = new Array<>();
    private final Array<WalkingNPC> walkingNPCS = new Array<>();
    private final Array<Pet> pets = new Array<>();
    private TalkingNPC focusedOnNPC;                //NPC that player is near and who can be talked to

    //========================= Enemies =================================
    private final Array<Enemy> enemies = new Array<>();


    /**
     * Purpose: Grabs the info from main screen that holds asset manager
     * Input: BasicTemplet
    */
    MainScreen(Hunter hunter, int tiledSelection) {
        this.hunter = hunter;

        this.tiledSelection = tiledSelection;
        levelNames.add("Tiled/Town.tmx");
    }


    /**
    Purpose: Updates the dimensions of the screen
    Input: The width and height of the screen
    */
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    //===================================== Show ===================================================

    /**
     * Purpose: Central function for initializing the screen
     */
    @Override
    public void show() {
        showCamera();       //Set up the camera
        showObjects();      //Sets up player and font
        mainScreenTextures = new MainScreenTextures();
        showTiled();
        musicControl.showMusic(0);
        if(developerMode){debugRendering.showRender();}    //If in developer mode sets up the redners
    }


    /**
     * Purpose: Sets up all the objects imported from tiled
     */
    private void showTiled() {
        tiledSetUp = new TiledSetUp(hunter.getAssetManager(), batch, levelNames.get(tiledSelection));

        //======================================== Hero =========================================
        Array<Vector2> heroPosition = tiledSetUp.getLayerCoordinates("Hero");
        hero = new Hero(heroPosition.get(0).x, heroPosition.get(0).y, mainScreenTextures.heroSpriteSheet);

        //================================= Platforms =======================================
        Array<Vector2> platformsPositions = tiledSetUp.getLayerCoordinates("Platforms");
        Array<Vector2> platformsDimensions = tiledSetUp.getLayerDimensions("Platforms");
        for(int i = 0; i < platformsPositions.size; i++){
            platforms.add(new Platform(platformsPositions.get(i).x, platformsPositions.get(i).y, platformsDimensions.get(i).x, platformsDimensions.get(i).y));
        }

        //================================= Spikes =======================================
        Array<Vector2> spikesPositions = tiledSetUp.getLayerCoordinates("Spikes");
        Array<Vector2> spikesDimensions = tiledSetUp.getLayerDimensions("Spikes");
        for(int i = 0; i < spikesPositions.size; i++){
            spikes.add(new Spike(spikesPositions.get(i).x, spikesPositions.get(i).y,
                    spikesDimensions.get(i).x, mainScreenTextures.spikeTexture));
        }

        //================================= NPCs =============================================
        Array<Vector2> talkingNPCSPositions = tiledSetUp.getLayerCoordinates("TalkingNPC");
        Array<String> talkingNPCSNames = tiledSetUp.getLayerNames("TalkingNPC");
        Array<Object> talkingNPCSDialogues = tiledSetUp.getLayerDialogue("TalkingNPC");
        for(int i = 0; i < talkingNPCSPositions.size; i++){
            addNPC(talkingNPCSNames.get(i), talkingNPCSPositions.get(i), talkingNPCSDialogues.get(i));
        }

        Array<Vector2> walkingNPCSPositions = tiledSetUp.getLayerCoordinates("WalkingNPC");
        Array<Vector2> walkingNPCSDimensions = tiledSetUp.getLayerDimensions("WalkingNPC");
        Array<String> walkingNPCSNames = tiledSetUp.getLayerNames("WalkingNPC");
        for(int i = 0; i < walkingNPCSPositions.size; i++){
            addWalkingNPC(walkingNPCSNames.get(i), walkingNPCSPositions.get(i), walkingNPCSDimensions.get(i));
        }

        //================================ Enemies =================================================
        Array<Vector2> enemyPositions = tiledSetUp.getLayerCoordinates("Enemy");
        Array<String> enemyNames = tiledSetUp.getLayerNames("Enemy");
        for(int i = 0; i < enemyPositions.size; i++){
            addEnemyType(enemyNames.get(i), enemyPositions.get(i));
        }

    }

    /**
     * Adds in a NPC that's stationary and can be chatted with by the player
     * @param name Name of the NPC
     * @param position Where they stand
     * @param dialogue  What they say
     */
    private void addNPC(String name, Vector2 position, Object dialogue){
        switch (name){
            case "Mom":{
                talkingNPCS.add(new TalkingNPC(position.x, position.y, mainScreenTextures.momTexture,
                        mainScreenTextures.momPortTexture, (String) dialogue, name));
                break;
            }
            case "Man":{
                talkingNPCS.add(new TalkingNPC(position.x, position.y, mainScreenTextures.manTexture,
                        mainScreenTextures.manPortTexture, (String) dialogue, name));
                break;
            }
        }
    }


    /**
     * Adds in a walking NPC
     * @param name Name of NPC
     * @param position Placement
     * @param dimensions How far they can walk if they have preset path.
     */
    private void addWalkingNPC(String name, Vector2 position, Vector2 dimensions){
        switch (name){
            case "Child":{
                walkingNPCS.add(new WalkingNPC(position.x, position.y, dimensions.x, mainScreenTextures.childSpriteSheet));
                break;
            }
            case "Pet":{
                pets.add(new Pet(position.x, position.y, mainScreenTextures.petSpriteSheet));
                break;
            }
        }
    }

    /**
     * Sets up all the emmies
     * @param name name of enemy
     * @param position placement
     */
    private void addEnemyType(String name, Vector2 position){
        switch (name){
            case "Dummy":{
                enemies.add(new Dummy(position.x, position.y, mainScreenTextures.dummySpriteSheet));
            }
            default:{
                enemies.add(new Dummy(position.x, position.y, mainScreenTextures.dummySpriteSheet));
            }
        }
    }

    /**
     * Purpose: Sets up the camera through which all the objects are view through
    */
    private void showCamera(){
        camera = new OrthographicCamera();									//Sets a 2D view
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);	//Places the camera in the center of the view port
        camera.update();													//Updates the camera
        viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);  //Stretches the image to fit the screen
    }

    /**
     * Purpose: Sets up objects such as debugger, musicControl, fonts and others
    */
    private void showObjects(){
        debugRendering = new DebugRendering(camera);
        musicControl = new MusicControl(hunter.getAssetManager());

        if(hunter.getAssetManager().isLoaded("Fonts/Font.fnt")){bitmapFont = hunter.getAssetManager().get("Fonts/Font.fnt");}
        bitmapFont.getData().setScale(1f);
    }

    //=================================== Execute Time Methods =====================================

    /**
    Purpose: Central function for the game, everything that updates run through this function
    */
    @Override
    public void render(float delta) {
        if(!pausedFlag) { update(delta); }      //If the game is not paused update the variables
        else{ menuInputHandling(); }
        draw();                                 //Draws everything
        if (developerMode) { debugRender(); }   //If developer mode is on draws hit-boxes
    }

    //===================================== Debug ==================================================

    /**
     Purpose: Draws hit-boxes for all the objects
     */
    private void debugRender(){
        debugRendering.startEnemyRender();
        for(Spike spike : spikes){
            spike.drawDebug(debugRendering.getShapeRenderEnemy());
        }
        for(Enemy enemy : enemies){
            enemy.drawDebug(debugRendering.getShapeRenderEnemy());
        }
        debugRendering.endEnemyRender();

        debugRendering.startUserRender();
        hero.drawDebug(debugRendering.getShapeRendererUser());
        debugRendering.endUserRender();

        debugRendering.startBackgroundRender();
        for(Platform platform : platforms){
            platform.drawDebug(debugRendering.getShapeRendererBackground());
        }
        debugRendering.endBackgroundRender();

        debugRendering.startCollectibleRender();
        for(TalkingNPC talkingNPC : talkingNPCS){
            talkingNPC.drawDebug(debugRendering.getShapeRendererCollectible());
        }
        for(WalkingNPC walkingNPC : walkingNPCS){
            walkingNPC.drawDebug(debugRendering.getShapeRendererCollectible());
        }
        for(Pet pet : pets){
            pet.drawDebug(debugRendering.getShapeRendererCollectible());
        }
        debugRendering.endCollectibleRender();
    }

    /**
     * Purpose: Draws the info for dev to test the game
    */
    private void debugInfo(){
    }

    //=================================== Updating Methods =========================================

    /**
    Purpose: Updates all the moving components and game variables
    Input: @delta - timing variable
    */
    private void update(float delta){
        isColliding();          //Checks for everything touching everything else
        handleInput(delta);     //Checks user input
        updateCamera();         //Checks camera position changes
        updateUnits(delta);     //Update character positions and actions
    }

    //================================== Update Units ===========================================

    /**
     * Update character position and actions
     * @param delta timing var
     */
    private void updateUnits(float delta){
        //=============================== Player ================================
        hero.update(tiledSetUp.getLevelWidth(), tiledSetUp.getLevelHeight(), delta);
        if(hero.getIsFalling()){canPostFallJump(delta);}

        //=============================== Walking NPCS ========================
        for(WalkingNPC walkingNPC : walkingNPCS){ walkingNPC.update(delta); }
        for(Pet pet : pets){ pet.update(delta, hero.getHitBox()); }
    }

    //=================================== Input Handling ==========================================

    /**
     * Purpose: Central Input Handling function
     */
    private void handleInput(float delta) {
        //Pause and un-pause the game with ESC
        handlePause();
        //Allows user to turn on dev mode
        if (Gdx.input.isKeyJustPressed(Input.Keys.TAB)) { developerMode = !developerMode; }
        handleInputs(delta);
    }

    /**
     * Pauses and un-pauses the game with Esc key
     */
    private void handlePause(){
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            //playSFX("Menu Confirm");
            pausedFlag = !pausedFlag;
        }
    }

    /**
     * Purpose: Actions that can only be done in developer mode, used for testing
     */
    private void handleInputs(float delta){
        if(!isTalking && !cameraPan) {
            //======================== Movement Vertically ====================================
            if(Gdx.input.isKeyPressed(Input.Keys.UP)){
                //If he isn't jumping or falling jump
               if(!hero.getIsJumping() && (!hero.getIsFalling() || canPostFallJump) && letGoJump){ hero.jump(); }
               //If the button is held make the jump go higher
               else{ hero.jumpExt(); }
               //Make sure that the player won't instantly jump again when landed
                letGoJump = false;
            }
            //Allows to jump again after landing
            else{ letGoJump = true; }

            hero.setDucking(!hero.getIsJumping() && Gdx.input.isKeyPressed(Input.Keys.DOWN));

            //==================== Movement Horizontally ======================================
            if (hero.getIsDucking() && Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                hero.moveHorizontally(HERO_X_VELOCITY);
            } else if (hero.getIsDucking() && Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                hero.moveHorizontally(-HERO_X_VELOCITY);
            } else {
                hero.moveHorizontally(0);
            }

            //============================= Action =============================
            //Talks to NPC if near
            if (isTouchingNPC && Gdx.input.isKeyJustPressed(Input.Keys.E)){ isTalking = true; }
            //Attacks whatever is in front of player
            else if(Gdx.input.isKeyJustPressed(Input.Keys.E)){ hero.attack(); }
        }
        else{
            if (Gdx.input.isKeyJustPressed(Input.Keys.E)){
                //If the NPC no longer has any lines left exit the convo
                if(focusedOnNPC.isDialogueDone()){
                    focusedOnNPC.restartDialogue();
                    isTalking = false;
                }
                //Progress the conversation to next line
                else{ focusedOnNPC.updateDialogue(); }
            }
        }

    }

    /**
     * Purpose: allows user to control the menus
     */
    private void menuInputHandling(){
        //================================= General Menu ==========================
        if(!helpFlag && !endFlag) {
            //==================== Movement Vertically ====================================
            if (Gdx.input.isKeyJustPressed(Input.Keys.W) || Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
                buttonIndex--;
                //playSFX("Menu SFX");
                if (buttonIndex <= -1) {
                    buttonIndex = NUM_BUTTONS_MAIN_SCREEN - 1;
                }
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.S) || Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
                buttonIndex++;
                //playSFX("Menu SFX");
                if (buttonIndex >= NUM_BUTTONS_MAIN_SCREEN) {
                    buttonIndex = 0;
                }
            }

            //================================== Action Selection ======================
            if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                //Launches the game
                //playSFX("Menu Confirm");
                if (buttonIndex == 0) { helpFlag = true; }
                //Turns on the help menu
                else if (buttonIndex == 2) { musicControl.soundOnOff(); }
                //Turns on the credits menu
                else if(buttonIndex == 3){
                    musicControl.stopMusic();
                    hunter.setScreen(new LoadingScreen(hunter, 0));
                }
                else{
                    pausedFlag = false;
                    buttonIndex = 0;
                }
            }

            //===================================== Leaving ===========================
            if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
                //playSFX("Menu Deconfirm");
                pausedFlag = false;
                buttonIndex = 0;
            }
        }
        //========================= Controls ===============================
        else if(helpFlag){
            if (Gdx.input.isKeyJustPressed(Input.Keys.E) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                //playSFX("Menu Deconfirm");
                helpFlag = false; }
        }
        //================== Exit Menu ==================
        /*
        else if(endFlag){
            //Move between buttons
            if(exitIndex == 0 && (Gdx.input.isKeyJustPressed(Input.Keys.D) || Gdx.input.isKeyJustPressed(Input.Keys.RIGHT))){
                playSFX("Menu SFX");
                exitIndex++;
            }
            else if(exitIndex == 1 && (Gdx.input.isKeyJustPressed(Input.Keys.A) || Gdx.input.isKeyJustPressed(Input.Keys.LEFT))){
                playSFX("Menu SFX");
                exitIndex--;
            }

            //Select the action
            if (exitIndex == 0 && Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                playSFX("Menu Confirm");
                musicControl.stopMusic();
                infamous.setScreen(new LoadingScreen(infamous, 0));
            }
            else if(exitIndex == 1 && Gdx.input.isKeyJustPressed(Input.Keys.E)){
                playSFX("Menu Confirm");
                toNextLevel();
            }
        }
         */
    }

    //====================================== Collisions ============================================

    /**
     * Central function for updating collision
     */
    private void isColliding(){
        isCollidingPlatform();      //Update collision with platforms for player and any mobile unit
        isCollidingSpike();         //Checks for player collision with spikes
        isCollidingTalkingNPC();    //Checks for player being near a talk NPC
        isCollidingWithPets();      //Check for player being in range of Pet Awareness zone
        isCollidingAttack();        //Check if player or enemy hit target
    }

    /**
     * Purpose: Check if it's touching any platforms
     */
    private void isCollidingPlatform() {
        //Checks if there is ground below him
        boolean hasGround = false;
        for (int i = 0; i < platforms.size; i++) {
            if(hero.updateCollision(platforms.get(i).getHitBox())){
                hasGround = true;                //Tells us that he's standing
                if(hero.getX() >= platforms.get(i).getX()
                        && hero.getX() + hero.getWidth() <= platforms.get(i).getX() + platforms.get(i).getWidth()) {
                    hero.setLastTouchedGround();     //Saves that position for respawn
                    canPostFallJump = true;
                }
            }
            //Update pets to have collision with platforms
            for(int j = 0; j < pets.size; j++){
                boolean petGround = false;
                if(pets.get(j).updateCollision(platforms.get(i).getHitBox())){ petGround = true; }
                if(!petGround){pets.get(j).setFalling(true);}
            }
        }
        //If there is no ground below Cole he should fall
        if(!hasGround){hero.setFalling(true);}
    }

    /**
     * Purpose: Check if it's touching any spikes if they are send them back and give damage
     */
    private void isCollidingSpike() {
        for (int i = 0; i < spikes.size; i++) {
            if(spikes.get(i).isColliding(hero.getHitBox())){
                hero.touchedBadObject(-5);
                cameraPan = true;
            }
        }
    }

    /**
     * Turns off the ability to jump after few second after falling
     * @param delta timing var
     */
    private void canPostFallJump(float delta){
        postFallTime -= delta;
        if (postFallTime <= 0) {
            postFallTime = POST_FALL_TIME;
            canPostFallJump = false;
        }
    }

    /**
     * Purpose: Check if it's touching any platforms
     */
    private void isCollidingTalkingNPC() {
        isTouchingNPC = false;
        for (int i = 0; i < talkingNPCS.size; i++) {
            if(talkingNPCS.get(i).isColliding(hero.getHitBox())){
                isTouchingNPC = true;
                focusedOnNPC = talkingNPCS.get(i);
            }
        }
    }


    /**
     * Purpose: Check if it's touching any platforms
     */
    private void isCollidingWithPets() {
        for (int i = 0; i < pets.size; i++) { pets.get(i).isSensing(hero.getHitBox()); }
    }

    /**
     * Checks if player or enemy hit their target
     */
    private void isCollidingAttack(){
        if(hero.isAttacking()){
            for(Enemy enemy : enemies){
                if(enemy.isColliding(hero.getAttackHitBox())){
                    enemy.takeDamage(5);
                }
            }
        }
    }

    /**
     * Purpose: Resize the menuStage viewport in case the screen gets resized (Desktop)
     *          Moving the camera if that's part of the game
     */
    public void updateCamera() {
        //============================= Camera following player ============================
        if(!cameraPan) {
            //Updates Camera if the X positions has changed
            if ((hero.getX() > WORLD_WIDTH / 2f) && (hero.getX() < tiledSetUp.getLevelWidth() - WORLD_WIDTH / 2f)) {
                camera.position.set(hero.getX(), camera.position.y, camera.position.z);
                camera.update();
                tiledSetUp.updateCamera(camera);
            }
            //Updates the change of camera to keep the UI moving with the player
            xCameraDelta = camera.position.x - WORLD_WIDTH / 2f;
            yCameraDelta = camera.position.y - WORLD_HEIGHT / 2f;
        }
        //=========================== Camera panning to player after death ========================
        else{
            if(hero.getX() < camera.position.x){
                camera.position.set(camera.position.x - 2, camera.position.y, camera.position.z); }
            else { camera.position.set(camera.position.x + 2, camera.position.y, camera.position.z);}

            camera.update();
            tiledSetUp.updateCamera(camera);

            xCameraDelta = camera.position.x - WORLD_WIDTH / 2f;
            yCameraDelta = camera.position.y - WORLD_HEIGHT / 2f;

            //Once we reach roughly player position stop panning
            if(Math.round(hero.getX()) == Math.round(camera.position.x) ||
                Math.round(hero.getX()) == Math.round(camera.position.x - 1) ||
                Math.round(hero.getX()) == Math.round(camera.position.x + 1)
            )
            { cameraPan = false; }
        }
    }

    /**
    Purpose: Puts the game in end game state
    */
    private void endGame(){ endFlag = true; }

    /**
     Purpose: Restarts the game to it's basic settings
     */
    private void restart(){
    }

    //========================================== Drawing ===========================================

    /**
     * Purpose: Central drawing function, from here everything gets drawn
    */
    private void draw(){
        //================== Clear Screen =======================
        clearScreen();

        //==================== Set Up Camera =============================
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);

        //======================== Draws ==============================
        batch.begin();
        if(developerMode){debugInfo();}        //If dev mode is on draw hit boxes and phone stats
        drawBackground();
        batch.end();

        tiledSetUp.drawTiledMap();

        //======================== Draws ==============================
        batch.begin();
        if(developerMode){debugInfo();}        //If dev mode is on draw hit boxes and phone stats
        for(TalkingNPC talkingNPC : talkingNPCS){ talkingNPC.draw(batch); }
        for(WalkingNPC walkingNPC : walkingNPCS){ walkingNPC.drawAnimations(batch); }
        for(Enemy enemy : enemies){ enemy.drawAnimations(batch); }
        hero.drawAnimations(batch);
        for(Pet pet : pets){ pet.drawAnimations(batch);}
        drawAction();
        for(Spike spike : spikes){ spike.draw(batch); }

        if(isTalking){
            drawTalkingMenu();
        }
        batch.end();

        //=================== Draws the Menu Background =====================
        batch.begin();
        if(pausedFlag){
            drawPopUpMenu();
            if(!helpFlag) {
                drawMainButtons();
                drawButtonText();
            }
        }
        batch.end();

        //=================== Draws the Menu Buttons =========================

        //================= Draw Menu Button Text =============================
        batch.begin();
        batch.end();
    }

    /**
     * Purpose: Draws the parallax background
     */
    private void drawBackground(){
        batch.draw(mainScreenTextures.backgroundColor, xCameraDelta, yCameraDelta);
        for(int i = 0; i < tiledSetUp.getLevelWidth()/WORLD_WIDTH + 1; i++){
            batch.draw(mainScreenTextures.backgroundBack, xCameraDelta - xCameraDelta * 0.1f + WORLD_WIDTH *i, yCameraDelta);
            batch.draw(mainScreenTextures.backgroundMid, xCameraDelta - xCameraDelta * 0.2f + WORLD_WIDTH *i, yCameraDelta);
            batch.draw(mainScreenTextures.backgroundFront, xCameraDelta - xCameraDelta * 0.3f + WORLD_WIDTH *i, yCameraDelta);

        }
    }

    /**
     * Draws the action hero can take when near pole/ledge or a downed person 
     */
    private void drawAction(){
        if(isTouchingNPC){
            batch.draw(mainScreenTextures.eTexture, hero.getX() + mainScreenTextures.eTexture.getWidth()/2f,
                    hero.getY() + hero.getHeight() + 5 + mainScreenTextures.eTexture.getHeight()/2f  );
        }
    }

    //============================================== Draw Menus =====================================

    private void drawTalkingMenu(){
        batch.draw(mainScreenTextures.menuBackgroundTexture, xCameraDelta + 5, 170, WORLD_WIDTH - 10, 70);
        focusedOnNPC.drawSpeech(batch, bitmapFont, xCameraDelta, yCameraDelta);
        batch.draw(mainScreenTextures.eTexture, WORLD_WIDTH - 27 + xCameraDelta, 175);
    }


    /**
     * Purpose: Draws the menu background and instructions
     */
    private void drawPopUpMenu() {
        bitmapFont.getData().setScale(0.3f);
        if (pausedFlag || endFlag || helpFlag) {
            batch.draw(mainScreenTextures.menuBackgroundTexture, xCameraDelta + WORLD_WIDTH / 2f - WORLD_WIDTH / 4, yCameraDelta + 10, WORLD_WIDTH / 2, WORLD_HEIGHT - 20);
            if (helpFlag) { drawInstructions();}
        }
    }


    /**
     * Input: Void
     * Output: Void
     * Purpose: Draws the text for instructions
     */
    private void drawInstructions() {
        batch.draw(mainScreenTextures.controlsTexture, xCameraDelta, yCameraDelta);
    }


    /**
     * Purpose: Draws the main buttons on the screen
     */
    private void drawMainButtons(){
        for(int i = 0; i < NUM_BUTTONS_MAIN_SCREEN; i++){
            if(i == buttonIndex){
                batch.draw(mainScreenTextures.buttonSpriteSheet[0][1], xCameraDelta + WORLD_WIDTH / 2f - MENU_BUTTON_WIDTH / 2f, yCameraDelta +MENU_BUTTON_Y_START - 15 - (10 + MENU_BUTTON_HEIGHT) * i, MENU_BUTTON_WIDTH, MENU_BUTTON_HEIGHT);
            }
            else{
                batch.draw(mainScreenTextures.buttonSpriteSheet[0][0], xCameraDelta + WORLD_WIDTH / 2f - MENU_BUTTON_WIDTH / 2f, yCameraDelta + MENU_BUTTON_Y_START -  15 - (10 + MENU_BUTTON_HEIGHT) * i, MENU_BUTTON_WIDTH, MENU_BUTTON_HEIGHT);
            }
        }
    }

    /**
     Purpose: Draws text over the menu buttons, Restart, Help, Sound Off/On and Main Menu
     */
    private void drawButtonText() {
        bitmapFont.setColor(Color.BLACK);
        String string;
        for (int i = 0; i < menuButtonText.length - 1; i++) {
            string = menuButtonText[i];
            //If the volume is off draw Sound On else Sound off
            if (i == 2 && musicControl.getSFXVolume() == 0) { string = menuButtonText[menuButtonText.length - 1]; }
            textAlignment.centerText(batch, bitmapFont, string, xCameraDelta + WORLD_WIDTH / 2f,  yCameraDelta - 10 + MENU_BUTTON_Y_START + MENU_BUTTON_HEIGHT/2f - (10 + MENU_BUTTON_HEIGHT) * i);
        }
    }

    /**
     * Purpose: Set the screen to black so we can draw on top of it again
    */
    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a); //Sets color to black
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);										 //Sends it to the buffer
    }

    /**
     * Purpose: Destroys everything once we move onto the new screen
    */
    @Override
    public void dispose(){
    }
}
