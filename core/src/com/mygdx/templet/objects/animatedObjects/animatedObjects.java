package com.mygdx.templet.objects.animatedObjects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.templet.objects.GenericObjects;

public class animatedObjects extends GenericObjects {

    protected TextureRegion[][] spriteSheet;
    protected Animation<TextureRegion> animation;
    float animationFrameTime = 4f;
    float animationTime = 0;

    boolean isFacingRight = false;

    public animatedObjects(float x, float y, TextureRegion[][] spriteSheet) {
        super(x, y);
        this.spriteSheet = spriteSheet;
        setUpAnimations();
    }

    /**
     * Purpose: Sets up the animation loops in all of the directions
     */
    protected void setUpAnimations() {
        animation = setUpAnimation(spriteSheet, 1/animationFrameTime, 0, Animation.PlayMode.LOOP);
    }

    /**
     * Animates a row of the texture region
     * @param textureRegion, the sprite sheet
     * @param duration, how long each frame lasts
     * @param row, which row of the sprite sheet
     * @param playMode, how will the animation play out
     * @return full animation set
     */
    protected Animation<TextureRegion> setUpAnimation(TextureRegion[][] textureRegion, float duration, int row, Animation.PlayMode playMode){
        Animation<TextureRegion> animation = new Animation<>(duration, textureRegion[row]);
        animation.setPlayMode(playMode);
        return animation;
    }

    public void update(float delta) {
        animationTime += delta;
    }

    /**
     * Draws the animations
     * @param batch where the animation will be drawn
     */
    public void drawAnimations(SpriteBatch batch){
        TextureRegion currentFrame = spriteSheet[0][0];

        currentFrame = animation.getKeyFrame(animationTime);

        batch.draw(currentFrame, isFacingRight ? hitBox.x + currentFrame.getRegionWidth() : hitBox.x, hitBox.y, isFacingRight ? -currentFrame.getRegionWidth() : currentFrame.getRegionWidth(), currentFrame.getRegionHeight());
    }

}
