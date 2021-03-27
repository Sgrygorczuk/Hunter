package com.mygdx.templet.objects.aliveObjects.enemies;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.templet.objects.aliveObjects.enemies.Enemy;


public class Dummy extends Enemy {
    /**
     * Dummy is a basic enemy that stands still, deals no damage and only dies
     * @param x position
     * @param y position
     * @param spriteSheet texture
     */
    public Dummy(float x, float y, TextureRegion[][] spriteSheet) {
        super(x, y, spriteSheet);
        maxHealth = currentHealth = 15;                     //Set up health
        hitBox.height = spriteSheet[0][0].getRegionHeight();
        hitBox.width = spriteSheet[0][0].getRegionWidth();
    }

    /**
     * Overwrites the walking animation since it only stands still and has extra frame of death
     * @param batch where it's drawn
     */
    @Override
    public void drawAnimations(SpriteBatch batch) {
        TextureRegion currentFrame;

        //Dead
        if(currentHealth < 0){ currentFrame = spriteSheet[0][2]; }
        //Standing Still
        else{ currentFrame = spriteSheet[0][0]; }

        batch.draw(currentFrame, isFacingRight ? hitBox.x + currentFrame.getRegionWidth() : hitBox.x, hitBox.y, isFacingRight ? -currentFrame.getRegionWidth() : currentFrame.getRegionWidth(), currentFrame.getRegionHeight());
    }
}

