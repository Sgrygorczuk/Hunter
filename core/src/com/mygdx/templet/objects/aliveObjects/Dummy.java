package com.mygdx.templet.objects.aliveObjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Dummy extends Enemy{
    public Dummy(float x, float y, TextureRegion[][] spriteSheet) {
        super(x, y, spriteSheet);
        maxHealth = currentHealth = 15;
        hitBox.height = spriteSheet[0][0].getRegionHeight();
        hitBox.width = spriteSheet[0][0].getRegionWidth();
    }

    @Override
    public void drawAnimations(SpriteBatch batch) {
        TextureRegion currentFrame;

        if(currentHealth < 0){ currentFrame = spriteSheet[0][2]; }
        else{ currentFrame = spriteSheet[0][0]; }

        batch.draw(currentFrame, isFacingRight ? hitBox.x + currentFrame.getRegionWidth() : hitBox.x, hitBox.y, isFacingRight ? -currentFrame.getRegionWidth() : currentFrame.getRegionWidth(), currentFrame.getRegionHeight());
    }
}

