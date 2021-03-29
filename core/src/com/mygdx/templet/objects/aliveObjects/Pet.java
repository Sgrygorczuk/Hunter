package com.mygdx.templet.objects.aliveObjects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

import static com.mygdx.templet.Const.FEET_HEAD_HEIGHT;

public class Pet extends AliveObjects{

    private final Rectangle awarenessHitBox;    //Large box around the pet to see if player is near
    private final float boxSize = 120;          //Dimensions of the box
    private boolean sensePlayer = false;        //Tells us if player is close enough

    //Timer Counting unti the pet will wander
    private static final float WANDER_TIME = 0.5F;
    private float wanderTime = WANDER_TIME;
    private boolean isCollected = false;

    /**
     * A friendly alive object that follows the player around
     * @param x position
     * @param y position
     * @param spriteSheet spritesheet
     */
    public Pet(float x, float y, TextureRegion[][] spriteSheet) {
        super(x, y, spriteSheet);

        //Dimensions of hitBox
        hitBox.width = spriteSheet[0][0].getRegionWidth();
        hitBox.height = spriteSheet[0][0].getRegionHeight();

        //Redoing the animations, this time we have a random chance of getting one of 4 different colored pets
        animationFrameTime = 4;
        int random = MathUtils.random(3);
        walkRightAnimation = setUpAnimation(spriteSheet, 1/animationFrameTime, random, Animation.PlayMode.LOOP);
        walkLeftAnimation = setUpAnimation(spriteSheet, 1/animationFrameTime, random, Animation.PlayMode.LOOP_REVERSED);

        //Set up awareness box
        awarenessHitBox = new Rectangle(hitBox.x - boxSize/2f, hitBox.y - boxSize/2f, boxSize, boxSize);
    }

    /**
     * Checks if the player is with in a large range around the pet
     * @param playerHitBox players hitbox
     */
    public void isSensing(Rectangle playerHitBox){ sensePlayer = this.awarenessHitBox.overlaps(playerHitBox); }

    public void setIsCollected(){this.isCollected = true;}

    /**
     * General update function for Pets
     * @param delta timing var
     * @param player player hitBox
     */
    public void update(float delta, Rectangle player, float collectXMin, float collectXMax){
        //============ Animation Update =================
        animationRightTime += delta;
        animationLeftTime += delta;

        //============ Position Update ========================
        hitBox.y += velocity.y;       //Has to be before so he doesn't sink into the floor
        awarenessHitBox.x = hitBox.x - boxSize/2f;
        awarenessHitBox.y = hitBox.y - boxSize/2f;

        //================ Movement Update =====================
        updateVelocityY();
        if(isCollected){ updateHorizontalCollected(delta); }
        else{ updateHorizontal(delta, player); }

        //Has to be after so he doesn't smash into walls
        if(hitBox.x + hitBox.width + velocity.x >= collectXMax && isCollected){ hitBox.x = collectXMax - hitBox.width; }
        else if(hitBox.x + velocity.x <= collectXMin && isCollected){ hitBox.x = collectXMin; }
        else{ hitBox.x += velocity.x; }
    }

    private void updateHorizontalCollected(float delta){
        float random;   //Used to add random movements so the pet is "grazing"
        wanderTime -= delta;
        //Wander every once in a while
        if (wanderTime <= 0) {
            random = MathUtils.random(-3f, 3f);
            wanderTime = WANDER_TIME;
        }
        else{ random = 0; }
        velocity.x = random;
    }

    /**
     * Updates the horizontal movement
     * @param delta timing var
     * @param player player's hitbox
     */
    private void updateHorizontal(float delta, Rectangle player){
        float random;   //Used to add random movements so the pet is "grazing"
        //========================= Follows the Player ====================
        if(sensePlayer){
            //If is next to player just kind of wanders around or stays still
            if(isColliding(player)){
                wanderTime -= delta;
                //Wander every once in a while
                if (wanderTime <= 0) {
                    random = MathUtils.random(-3f, 3f);
                    wanderTime = WANDER_TIME;
                }
                //Stay still
                else{ random = 0; }
                velocity.x = random;
            }
            //If it's to the left of the player move right
            else if(Math.round(hitBox.x) < Math.round(player.x)){ velocity.x = 2f; }
            //Else go left
            else{ velocity.x = -2f; }
        }
        //=========================== Grazes ==========================
        else{
            wanderTime -= delta;
            //Graze every once in a while
            if (wanderTime <= 0) {
                random = MathUtils.random(-0.1f, 0.1f);
                wanderTime = WANDER_TIME;
                velocity.x = random;
            }
        }
    }

    /**
     * Draws the wire frame
     * @param shapeRenderer where its drawn
     */
    @Override
    public void drawDebug(ShapeRenderer shapeRenderer) {
        //super.drawDebug(shapeRenderer);
        shapeRenderer.rect(feetBox.x, feetBox.y, feetBox.width, feetBox.height);
        //shapeRenderer.rect(awarenessHitBox.x, awarenessHitBox.y, awarenessHitBox.width, awarenessHitBox.height);
    }

    /**
     * Overwrites to not have to use velocity to face in a specific directions
     * @param batch where it's drawn
     */
    @Override
    public void drawAnimations(SpriteBatch batch) {
        TextureRegion currentFrame = spriteSheet[0][0];
        if (isFacingRight) { currentFrame = walkRightAnimation.getKeyFrame(animationRightTime); }
        else if (!isFacingRight) { currentFrame = walkLeftAnimation.getKeyFrame(animationLeftTime); }

        batch.draw(currentFrame, isFacingRight ? hitBox.x + currentFrame.getRegionWidth() : hitBox.x, hitBox.y, isFacingRight ? -currentFrame.getRegionWidth() : currentFrame.getRegionWidth(), currentFrame.getRegionHeight());

    }
}
