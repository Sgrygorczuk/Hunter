package com.mygdx.templet.objects.aliveObjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import static com.mygdx.templet.Const.ACCELERATION;
import static com.mygdx.templet.Const.FRICTION;
import static com.mygdx.templet.Const.GRAVITY;
import static com.mygdx.templet.Const.HERO_HEIGHT;
import static com.mygdx.templet.Const.HERO_WIDTH;
import static com.mygdx.templet.Const.JUMP_PEAK;
import static com.mygdx.templet.Const.MAX_VELOCITY;

public class Hero extends AliveObjects{
    private boolean isJumping = false;    //Used to tell that it's not touching a platform
    private boolean isFalling = false;    //Used to tell if Cole if falling off a platform
    private boolean isRising = false;     //Used to create a arc for the jump
    private boolean isDucking = false;    //Tells us if cole is ducking
    private boolean invincibilityFlag = false; //Tells us if he's invincible
    protected boolean flashing = false;      //Tells the animation to flash or not
    private float initialY;               //Where the jump starts from

    private float lastTouchedGroundX;
    private float lastTouchedGroundY;

    private float relativeGravity;
    protected boolean touchedCeiling;


    //Timer counting down until player can be hit again
    private static final float INVINCIBILITY_TIME = 1.5F;
    private float invincibilityTimer = INVINCIBILITY_TIME;

    //Timer counting down until we turn the draw function on/Off
    private static final float FLASHING_TIME = 0.1F;
    private float flashingTimer = FLASHING_TIME;

    public boolean landedFlag = false;

    /* =========================== Movement Variables =========================== */

    protected float yAccel;     //value of jump speed
    protected float xAccel;     //value of increased speed for chosen direction
    protected float xDecel;     //value of decreased speed for chosen direction
    protected float xMaxVel;    //maximum x velocity allowed

    //============================ Constructor ========================================

    public Hero(float x, float y, TextureRegion[][] spriteSheet) {
        super(x, y, spriteSheet);

        hitBox.width = HERO_WIDTH;
        hitBox.height = HERO_HEIGHT;

        relativeGravity = GRAVITY;
        velocity.y = -relativeGravity;
        yAccel = relativeGravity;
        xAccel = ACCELERATION;
        xDecel = FRICTION;
        xMaxVel = MAX_VELOCITY;
    }

    /**
     * Purpose: Central Update function for Cole all continuous updates come through here
     * @param levelWidth the end of the level
     */
    public void update(float levelWidth, float levelHeight, float delta){
        assertWorldBound(levelWidth, levelHeight);
        updateVelocityY();
        decelerate();

        if(velocity.x > 0){
            animationLeftTime += delta;
            isFacingRight = false;
        }
        if(velocity.x < 0){
            animationRightTime += delta;
            isFacingRight = true;
        }

        updateDucking();

        hitBox.y += velocity.y;
        hitBox.x += velocity.x;
    }

    //=============================== Movement =============================================

    /**
     Purpose: Performs the jumping/falling action
     */
    private void updateVelocityY(){
        //=================== Player Initiated Jumping============
        if(isJumping) {
            //Is rising towards peak
            if (isRising && hitBox.y < initialY + JUMP_PEAK) { velocity.y += relativeGravity/2f; }
            //Checks if we reached peaked or Cole hit something above him
            else if (isRising) { isRising = false; }
            //Starts falling back down
            else if (velocity.y > -relativeGravity) { velocity.y = -relativeGravity; }
        }
        //================== Player Walked Off A Platform ===========
        else if(isFalling) {
            if (velocity.y > -relativeGravity) { velocity.y = -relativeGravity; }
        }
        //==================== Is Standing on a Platform =============
        else{
            velocity.y = 0;
        }
    }

    private void decelerate(){
        float deceleration = xDecel;

        if (velocity.x > deceleration)
            velocity.x = velocity.x - deceleration;
        else if (velocity.x < -deceleration)
            velocity.x = velocity.x + deceleration;
        else
            velocity.x = 0;

    }

    public void moveHorizontally(int direction){
        if ((velocity.x < xMaxVel) && (velocity.x > -xMaxVel))
            velocity.x += direction*xAccel;
    }

    /**
     * Purpose: Initiate the Player jump action
     */
    public void jump(){
        isJumping = true;       //Tells us we're jumping
        isRising = true;        //Tells us we're going up
        initialY = hitBox.y;    //Grabs the initial place where we started so we can find the jump peak
    }

    /**
     * Purpose: Turns isFalling true whenever Cole dosen't have ground below him
     */
    public void setFalling(boolean falling){isFalling = falling;}

    public boolean getIsFalling(){return isFalling;}

    /**
     * Purpose: To tell MainScreen that the use can click Jump
     * @return returns isJumping state
     */
    public boolean getIsJumping(){return isJumping;}

    public void setIsJumping(boolean isJumping){this.isJumping = isJumping;}

    //================================== Respawn ========================================

    public void setLastTouchedGround(){
        lastTouchedGroundX = hitBox.x;
        lastTouchedGroundY = hitBox.y;
    }

    public void touchedWater(){
        takeDamage(20);

        hitBox.x = lastTouchedGroundX;
        hitBox.y = lastTouchedGroundY;
    }

    //============================= Duck ================================================

    /**
     * Purpose: Have cole be at either full height or 2/3 height depending on if he's ducking
     */
    private void updateDucking(){
        if(!isDucking){ hitBox.height = HERO_WIDTH; }
        else{ hitBox.height = 2 * HERO_HEIGHT / 3f; }
    }


    public boolean getIsDucking(){return !isDucking;}

    /**
     * Purpose: Allow use to change Cole's stance
     * @param isDucking sets if he's ducking or not
     */
    public void setDucking(boolean isDucking){this.isDucking = isDucking;}

    /* ============================ Combat Functions =========================== */

    /**
     Input: Float delta
     Purpose: Ticks down to turn off invincibility
     */
    public void invincibilityTimer(float delta){
        invincibilityTimer -= delta;
        flashingTimer -= delta;

        if (flashingTimer <= 0) {
            flashingTimer = FLASHING_TIME;
            flashing = !flashing;
        }

        if (invincibilityTimer <= 0) {
            invincibilityTimer = INVINCIBILITY_TIME;
            invincibilityFlag = false;
            flashing = false;
        }
    }

    public boolean getInvincibility(){return invincibilityFlag;}

    public void setInvincibility(boolean invincibility){this.invincibilityFlag = invincibility;}

    /* ============================ Utility Functions =========================== */

    /**
     * Purpose: Keeps Object within the level
     * @param levelWidth tells where the map ends
     */
    protected void checkIfWorldBound(float levelWidth, float levelHeight) {
        //Makes sure we're bound by x
        if (hitBox.x < 0) {
            hitBox.x = 0;
            velocity.x = 0;
        }
        else if (hitBox.x + hitBox.width > levelWidth) {
            hitBox.x = (int) (levelWidth - hitBox.width);
            velocity.x = 0;
        }

        //Makes sure that we stop moving down when we hit the ground
        if (hitBox.y < 0) {
            hitBox.y = 0;
            velocity.y = 0;
        }
        else if (hitBox.y + hitBox.height > levelHeight){
            hitBox.y = levelHeight - hitBox.height;
            touchedCeiling = true;
        }
    }

    /**
     * Purpose: Keeps Cole within the level
     * @param levelWidth tells where the map ends
     */
    private void assertWorldBound(float levelWidth, float levelHeight) {
        checkIfWorldBound(levelWidth, levelHeight);
        if (touchedCeiling) {isFalling = true; touchedCeiling = false;}
    }

    /**
     * Purpose: Check if Cole is touching any platform
     * @param rectangle the platform we're checking against
     * @return tells us if there is any platform below Cole
     */
    public boolean updateCollision(Rectangle rectangle){
        if(this.hitBox.overlaps(rectangle)){
            //Vertical hitBox collision happens first otherwise he'll get set to the other size of the map

            /* Breakdown of Collision
              this.hitBox.y < rectangle.y + rectangle.height - checks if we're dipping into the box from the top
              this.hitBox.y >= rectangle.y + rectangle.height * 0.8f - makes sure it's only the very top of the box we're going into
                       We do rectangle.y + rectangle.height * 0.8f not (rectangle.y + rectangle.height) * 0.8f because () will have a lower value on
                       lower platforms we only want to multiply the height so that the initial Y is still large
              hitBox.x + hitBox.width > rectangle.x && hitBox.x < rectangle.x + rectangle.width - makes sure to only interact with this platform
             */
            //=============== On Top Of the Colliding Platform ====================
            if(this.hitBox.y <= rectangle.y + rectangle.height
                    && this.hitBox.y >= rectangle.y + rectangle.height * 0.9f){
                landedFlag = true;
                this.hitBox.y = rectangle.y + rectangle.height;
                isJumping = false;  //Can jump again
                isFalling = false;  //Is no longer falling
                velocity.y = 0;
            }
            /* Breakdown of Collision
              this.hitBox.y + this.hitBox.height > rectangle.y- checks if we're dipping into the box from the bottom
              this.hitBox.y + this.hitBox.height * 0.8f < rectangle.y - makes sure that only on the bottom
              hitBox.x + hitBox.width > rectangle.x && hitBox.x < rectangle.x + rectangle.width - makes sure to only interact with this platform
             */
            //=============== Below the Colliding Platform ====================
            else if(this.hitBox.y + this.hitBox.height > rectangle.y
                    && this.hitBox.y < rectangle.y
                    && hitBox.x + hitBox.width >= rectangle.x + rectangle.width * 0.1f
                    && hitBox.x <= rectangle.x + rectangle.width * 0.9f){
                this.hitBox.y = rectangle.y - this.hitBox.height;
            }

            /* Breakdown of Collision
              this.hitBox.x + this.hitBox.width > rectangle.x - checks if we're dipping into the box from the left
              hitBox.x < rectangle.x - makes sures we're coming from the left
              !(this.hitBox.y >= rectangle.y + rectangle.height) - makes sure we don't do it when on top of the block
              hitBox.y >= rectangle.y - so he doesn't teleports to edge of platform if he touches it from below
             */
            //=============== On the Left of the Colliding Platform ====================
            if(this.hitBox.x + this.hitBox.width >= rectangle.x
                    && hitBox.x < rectangle.x
                    && !(this.hitBox.y >= rectangle.y + rectangle.height)
                    && this.hitBox.y >= rectangle.y){
                this.hitBox.x = rectangle.x - this.hitBox.width;
                velocity.x = 0; //Stops movement
            }
            //=============== On the Right of the Colliding Platform ====================
            else if(this.hitBox.x <= rectangle.x + rectangle.width
                    && this.hitBox.x > rectangle.x
                    && !(this.hitBox.y >= rectangle.y + rectangle.height)
                    && this.hitBox.y >= rectangle.y){
                this.hitBox.x = rectangle.x + rectangle.width;
                velocity.x = 0; //Stop movement
            }
        }
        //Creates a second rectangle that's a little below Cole to see if there is any
        //Platform below him
        Rectangle fallCheckBox = new Rectangle(hitBox.x, hitBox.y - 1, hitBox.width, hitBox.height * 0.1f);
        return fallCheckBox.overlaps(rectangle);
    }

    //========================================= Drawing ============================================

    public void drawAnimations(SpriteBatch batch){
        TextureRegion currentFrame = spriteSheet[0][0];
        if(!flashing) {
            //=========================== Cole ============================================
            if (isFacingRight) {
                if (velocity.x != 0) {
                    currentFrame = walkRightAnimation.getKeyFrame(animationRightTime);
                }
            } else if (!isFacingRight) {
                if (velocity.x != 0) {
                    currentFrame = walkLeftAnimation.getKeyFrame(animationLeftTime);
                }
            }

            batch.draw(currentFrame, isFacingRight ? hitBox.x + currentFrame.getRegionWidth() : hitBox.x, hitBox.y, isFacingRight ? -currentFrame.getRegionWidth() : currentFrame.getRegionWidth(), currentFrame.getRegionHeight());

            //========================== Particle Effects =================================
            TextureRegion currentParticleFrame;
        }
    }
}
