package com.mygdx.templet.objects.aliveObjects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.templet.objects.GenericObjects;

import static com.mygdx.templet.Const.FEET_HEAD_HEIGHT;
import static com.mygdx.templet.Const.GRAVITY;
import static com.mygdx.templet.Const.JUMP_INC;
import static com.mygdx.templet.Const.JUMP_PEAK;

public class AliveObjects extends GenericObjects {

    //==================================== Health Management =========================
    protected int currentHealth;
    protected  int maxHealth;

    //=============================================== Visuals ========================
    protected TextureRegion[][] spriteSheet;

    protected float animationFrameTime = 8;
    protected Animation<TextureRegion> walkRightAnimation;
    protected Animation<TextureRegion> walkLeftAnimation;
    protected float animationRightTime = 0;
    protected float animationLeftTime = 0;

    protected boolean isFacingRight = false; //Tells us which way the person is facing

    /* =========================== Movement Variables =========================== */
    protected float timer = 0.75f;                  //Used for gravity acceleration upwards
    protected float peak;                   //Tells us where the peak of the jump will be (differs on how long user holds button)
    protected float initialY;               //Where the jump starts from

    protected final Rectangle feetBox;      //Used for collisions with floors
    protected final Rectangle headBox;      //Used for collisions with ceiling's

    protected boolean isJumping = false;    //Used to tell that it's not touching a platform
    protected boolean isFalling = false;    //Used to tell if Cole if falling off a platform
    protected boolean isRising = false;     //Used to create a arc for the jump

    protected Vector2 velocity;             //Speed that the unit is moving currently, x and y
    protected float relativeGravity;        //Gravity going downwards

    /**
     * Generic object that has a walking animation and can be hurt
     * @param x position
     * @param y position
     * @param spriteSheet texture
     */
    public AliveObjects(float x, float y, TextureRegion[][] spriteSheet) {
        super(x, y);                        //HitBox
        this.spriteSheet = spriteSheet;     //SpriteSheet
        setUpWalkingAnimations();            //Walking animations

        //Feet and Head for collision
        feetBox = new Rectangle(hitBox.x + hitBox.width + 0.15f, hitBox.y - FEET_HEAD_HEIGHT, hitBox.width * 0.7f, FEET_HEAD_HEIGHT);
        headBox = new Rectangle(hitBox.x, hitBox.y + hitBox.height + FEET_HEAD_HEIGHT, hitBox.width/2f, FEET_HEAD_HEIGHT);

        relativeGravity = GRAVITY;           //Falling Gravity
        velocity = new Vector2(0,0);   //Speed
    }

    //=============================== Movement =============================================


    /**
     * Moves the unit horizontally
     * @param velocity new horizontal velocity
     */
    public void moveHorizontally(float velocity){ this.velocity.x = velocity; }

    /**
     * Purpose: Initiate the unit jump action
     */
    public void jump(){
        isJumping = true;       //Tells us we're jumping
        isRising = true;        //Tells us we're going up
        initialY = hitBox.y;    //Grabs the initial place where we started so we can find the jump peak
        peak = JUMP_INC;        //Makes it the lowest jump
    }

    /**
     * Extends the jump till it reaches peak, for player if button is held
     */
    public void jumpExt(){
        if(peak <= JUMP_PEAK){ peak += JUMP_INC; }
    }

    /**
     * Purpose: Turns isFalling true whenever Cole doesn't have ground below him
     */
    public void setFalling(boolean falling){isFalling = falling;}
    public boolean getIsFalling(){return isFalling;}

    /**
     * Purpose: To tell MainScreen that the use can click Jump
     * @return returns isJumping state
     */
    public boolean getIsJumping(){return isJumping;}
    public void setIsJumping(boolean isJumping){this.isJumping = isJumping;}


    /**
     Purpose: Performs the jumping/falling action
     */
    protected void updateVelocityY(){
        //=================== Player Initiated Jumping============
        if(isJumping) {
            //Is rising towards peak
            if (isRising && hitBox.y < initialY + peak) {
                velocity.y = (float) (10 * timer);
                timer += timer * .1f;
            }
            //Checks if we reached peaked or Cole hit something above him
            else if (isRising) {
                isRising = false;
                timer = 0.75f;
                peak = 0;
            }
            //Starts falling back down
            else if (velocity.y > -relativeGravity) { velocity.y -= (relativeGravity + velocity.y * 0.1f); }
        }
        //================== Player Walked Off A Platform ===========
        else if(isFalling) {
            if (velocity.y > -relativeGravity) { velocity.y -= (relativeGravity + velocity.y * 0.1f); }
        }
        //==================== Is Standing on a Platform =============
        else{
            velocity.y = 0;
            peak = 0;
        }
    }

    //========================== Platform Collision ===========================================

    /**
     * Purpose: Check if Cole is touching any platform
     * @param rectangle the platform we're checking against
     * @return tells us if there is any platform below Cole
     */
    public boolean updateCollision(Rectangle rectangle){
        feetBox.x = hitBox.x + hitBox.width * 0.15f;
        feetBox.y = hitBox.y - FEET_HEAD_HEIGHT;

        headBox.x = hitBox.x + hitBox.width/4f;
        headBox.y = hitBox.y + hitBox.height + FEET_HEAD_HEIGHT;

        //Vertical
        //==================== Floor Collision ======================
        if(feetBox.overlaps(rectangle)){
            this.hitBox.y = rectangle.y + rectangle.height;
            isJumping = false;  //Can jump again
            isFalling = false;  //Is no longer falling
            velocity.y = 0;
        }

        //===================== Ceiling Collision =====================
        if(headBox.overlaps(rectangle)){
            this.hitBox.y = rectangle.y - this.hitBox.height;
            isFalling = true;
            isJumping = false;
            isRising = false;
            velocity.y = 0;
        }

        //Horizontal
        //====================== On the LEft of colliding Platform ==================
        if(hitBox.overlaps(rectangle) && !headBox.overlaps(rectangle)) {
            if (this.hitBox.x + this.hitBox.width >= rectangle.x
                    && hitBox.x < rectangle.x
                    && !(this.hitBox.y >= rectangle.y + rectangle.height)
                    && this.hitBox.y >= rectangle.y) {
                this.hitBox.x = rectangle.x - this.hitBox.width;
                velocity.x = 0; //Stops movement
            }
            //=============== On the Right of the Colliding Platform ====================
            else if (this.hitBox.x <= rectangle.x + rectangle.width
                    && this.hitBox.x > rectangle.x
                    && !(this.hitBox.y >= rectangle.y + rectangle.height)
                    && this.hitBox.y >= rectangle.y) {
                this.hitBox.x = rectangle.x + rectangle.width;
                velocity.x = 0; //Stop movement
            }
        }
        //Tells us if the user is standing on ground (used for respawn)
        return feetBox.overlaps(rectangle);
    }

    //============================================= Health ====================================
    public int getMaxHealth(){return maxHealth;}
    public int getCurrentHealth(){return currentHealth;}

    /**
     *
     * @param damage
     */
    public void takeDamage(int damage){
        currentHealth -= damage;
    }

    //============================ Drawing =========================================

    /**
     * Purpose: Sets up the animation loops in all of the directions
     */
    protected void setUpWalkingAnimations() {
        walkRightAnimation = setUpAnimation(spriteSheet, 1/animationFrameTime, 0, Animation.PlayMode.LOOP);
        walkLeftAnimation = setUpAnimation(spriteSheet, 1/animationFrameTime, 0, Animation.PlayMode.LOOP_REVERSED);
    }

    /**
     * Generic Animation set up that will take a row of a sprite sheet and turn it into an animation
     * @param textureRegion the sprite sheet
     * @param duration  how long each frame lasts
     * @param row which from from sprite sheet
     * @param playMode how should the animation be played
     * @return a full animation
     */
    protected Animation<TextureRegion> setUpAnimation(TextureRegion[][] textureRegion, float duration, int row, Animation.PlayMode playMode){
        Animation<TextureRegion> animation = new Animation<>(duration, textureRegion[row]);
        animation.setPlayMode(playMode);
        return animation;
    }


    /**
     * Draws the walking generic walking animation
     * @param batch where it's drawn
     */
    public void drawAnimations(SpriteBatch batch){
        TextureRegion currentFrame = spriteSheet[0][0];
        if (isFacingRight) {
            if (velocity.x != 0) { currentFrame = walkRightAnimation.getKeyFrame(animationRightTime); }
        }
        else if (!isFacingRight) {
            if (velocity.x != 0) { currentFrame = walkLeftAnimation.getKeyFrame(animationLeftTime); }
        }

        batch.draw(currentFrame, isFacingRight ? hitBox.x + currentFrame.getRegionWidth() : hitBox.x, hitBox.y, isFacingRight ? -currentFrame.getRegionWidth() : currentFrame.getRegionWidth(), currentFrame.getRegionHeight());
    }

}
