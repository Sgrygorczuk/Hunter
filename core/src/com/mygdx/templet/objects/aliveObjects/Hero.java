package com.mygdx.templet.objects.aliveObjects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import static com.mygdx.templet.Const.ACCELERATION;
import static com.mygdx.templet.Const.FEET_HEAD_HEIGHT;
import static com.mygdx.templet.Const.FRICTION;
import static com.mygdx.templet.Const.GRAVITY;
import static com.mygdx.templet.Const.HERO_HEIGHT;
import static com.mygdx.templet.Const.HERO_WIDTH;
import static com.mygdx.templet.Const.JUMP_INC;
import static com.mygdx.templet.Const.JUMP_PEAK;
import static com.mygdx.templet.Const.MAX_VELOCITY;

public class Hero extends AliveObjects{

    private float lastTouchedGroundX;
    private float lastTouchedGroundY;

    protected boolean touchedCeiling;

    private Rectangle attackHitBox;


    //Timer counting down until player can be hit again
    private static final float INVINCIBILITY_TIME = 1.5F;
    private float invincibilityTimer = INVINCIBILITY_TIME;

    //Timer counting down until we turn the draw function on/Off
    private static final float FLASHING_TIME = 0.1F;
    private float flashingTimer = FLASHING_TIME;

    //=============================== Flags ====================================
    private boolean isAttacking = false;
    private boolean isDucking = false;    //Tells us if cole is ducking
    private boolean invincibilityFlag = false; //Tells us if he's invincible
    private boolean flashing = false;      //Tells the animation to flash or not


    private Animation<TextureRegion> attackAnimation;
    //Timer counting down until we turn the draw function on/Off
    private static final float ATTACK_TIME = 3/8f;
    private float attackTime = ATTACK_TIME;
    private float animationAttackTime = 0;


    //============================ Constructor ========================================

    public Hero(float x, float y, TextureRegion[][] spriteSheet) {
        super(x, y, spriteSheet);

        hitBox.width = HERO_WIDTH;
        hitBox.height = 32;

        attackHitBox = new Rectangle(hitBox.x, hitBox.y + hitBox.height/2f, hitBox.width*2, hitBox.y);
        setUpAttackAnimation();
    }

    protected void setUpAttackAnimation(){
        attackAnimation =new Animation<TextureRegion>(1/8f, spriteSheet[1][0], spriteSheet[1][1], spriteSheet[1][2]);
        attackAnimation.setPlayMode(Animation.PlayMode.LOOP);
    }

    /**
     * Purpose: Central Update function for Cole all continuous updates come through here
     * @param levelWidth the end of the level
     */
    public void update(float levelWidth, float levelHeight, float delta){
        assertWorldBound(levelWidth, levelHeight);
        updateVelocityY();

        if(velocity.x > 0){
            animationLeftTime += delta;
            isFacingRight = false;
        }
        if(velocity.x < 0){
            animationRightTime += delta;
            isFacingRight = true;
        }

        if(isAttacking) {
            animationAttackTime += delta;
            attackTime -= delta;
            if(attackTime < 0){
                attackTime = ATTACK_TIME;
                isAttacking = false;
            }
        }


        updateDucking();

        hitBox.y += velocity.y;
        hitBox.x += velocity.x;

        attackHitBox.x = hitBox.x;
        attackHitBox.y = hitBox.y + hitBox.height/2f;
    }

    //================================== Respawn ========================================

    public void setLastTouchedGround(){
        lastTouchedGroundX = hitBox.x;
        lastTouchedGroundY = hitBox.y;
    }

    public void touchedBadObject(int damage){
        takeDamage(damage);
        hitBox.x = lastTouchedGroundX;
        hitBox.y = lastTouchedGroundY;
    }

    //============================= Duck ================================================

    /**
     * Purpose: Have cole be at either full height or 2/3 height depending on if he's ducking
     */
    private void updateDucking(){
        if(!isDucking){ hitBox.height = HERO_HEIGHT; }
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

    public void attack(){
        isAttacking = true;
    }

    public boolean isAttacking(){ return isAttacking; }

    public Rectangle getAttackHitBox(){return attackHitBox;}

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

    //========================================= Drawing ============================================


    @Override
    public void drawDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(hitBox.x, hitBox.y, hitBox.width, hitBox.height);
        shapeRenderer.rect(feetBox.x, feetBox.y, feetBox.width, feetBox.height);
        shapeRenderer.rect(headBox.x, headBox.y, headBox.width, headBox.height);
        //shapeRenderer.rect(attackHitBox.x, attackHitBox.y, attackHitBox.width, attackHitBox.height);
    }

    public void drawAnimations(SpriteBatch batch){
        TextureRegion currentFrame = spriteSheet[0][0];
        if(!flashing) {
            //=========================== Cole ============================================
            if(isAttacking){
                currentFrame = attackAnimation.getKeyFrame(animationAttackTime);
            }
            else if (isFacingRight) {
                if (velocity.x != 0) {
                    currentFrame = walkRightAnimation.getKeyFrame(animationRightTime);
                }
            } else if (!isFacingRight) {
                if (velocity.x != 0) {
                    currentFrame = walkLeftAnimation.getKeyFrame(animationLeftTime);
                }
            }

            batch.draw(currentFrame, isFacingRight ? hitBox.x + 2*currentFrame.getRegionWidth()/3f: hitBox.x, hitBox.y, isFacingRight ? -currentFrame.getRegionWidth() : currentFrame.getRegionWidth(), currentFrame.getRegionHeight());

            //========================== Particle Effects =================================
            TextureRegion currentParticleFrame;
        }
    }
}
