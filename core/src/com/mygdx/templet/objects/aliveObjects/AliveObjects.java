package com.mygdx.templet.objects.aliveObjects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.templet.objects.GenericObjects;

public class AliveObjects extends GenericObjects {

    int currentHealth;
    int maxHealth;

    protected Vector2 velocity;

    float animationFrameTime = 8;

    protected Animation<TextureRegion> walkRightAnimation;
    protected Animation<TextureRegion> walkLeftAnimation;

    protected boolean isFacingRight = false; //Tells us which way the person is facing

    protected float animationRightTime = 0;
    protected float animationLeftTime = 0;

    protected TextureRegion[][] spriteSheet;

    public AliveObjects(float x, float y, TextureRegion[][] spriteSheet) {
        super(x, y);
        this.spriteSheet = spriteSheet;
        velocity = new Vector2(0,0);
        setUpWalkingAnimations();
    }

    /**
     * Purpose: Sets up the animation loops in all of the directions
     */
    protected void setUpWalkingAnimations() {
        walkRightAnimation = setUpAnimation(spriteSheet, 1/animationFrameTime, 0, Animation.PlayMode.LOOP);
        walkLeftAnimation = setUpAnimation(spriteSheet, 1/animationFrameTime, 0, Animation.PlayMode.LOOP_REVERSED);
    }

    protected Animation<TextureRegion> setUpAnimation(TextureRegion[][] textureRegion, float duration, int row, Animation.PlayMode playMode){
        Animation<TextureRegion> animation = new Animation<>(duration, textureRegion[row]);
        animation.setPlayMode(playMode);
        return animation;
    }

    public void takeDamage(int damage){
        currentHealth -= damage;
    }

    public int getMaxHealth(){return maxHealth;}
    public int getCurrentHealth(){return currentHealth;}

}
