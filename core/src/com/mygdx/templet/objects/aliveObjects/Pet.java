package com.mygdx.templet.objects.aliveObjects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class Pet extends AliveObjects{

    private final Rectangle awarenessHitBox;
    private boolean sensePlayer = false;

    public Pet(float x, float y, TextureRegion[][] spriteSheet) {
        super(x, y, spriteSheet);

        hitBox.width = spriteSheet[0][0].getRegionWidth();
        hitBox.height = spriteSheet[0][0].getRegionHeight();

        animationFrameTime = 4;
        int random = MathUtils.random(3);
        walkRightAnimation = setUpAnimation(spriteSheet, 1/animationFrameTime, random, Animation.PlayMode.LOOP);
        walkLeftAnimation = setUpAnimation(spriteSheet, 1/animationFrameTime, random, Animation.PlayMode.LOOP_REVERSED);


        awarenessHitBox = new Rectangle(hitBox.x - 30, hitBox.y - 30, 60, 60);
    }

    public void isSensing(Rectangle playerHitBox){
        sensePlayer = this.awarenessHitBox.overlaps(playerHitBox);
    }

    @Override
    public void drawDebug(ShapeRenderer shapeRenderer) {
        super.drawDebug(shapeRenderer);
        shapeRenderer.rect(awarenessHitBox.x, awarenessHitBox.y, awarenessHitBox.width, awarenessHitBox.height);
    }

    public void update(float delta, Rectangle player){
        animationRightTime += delta;
        animationLeftTime += delta;

        if(sensePlayer){
            if(isColliding(player)){
                int random = MathUtils.random(-1, 1);
                velocity.x = random;
            }
            else if(Math.round(hitBox.x) < Math.round(player.x)){ velocity.x = 2f; }
            else{ velocity.x = -2f; }

            hitBox.x += velocity.x;
            awarenessHitBox.x += velocity.x;
        }
    }

    @Override
    public void drawAnimations(SpriteBatch batch) {
        TextureRegion currentFrame = spriteSheet[0][0];
        //=========================== Cole ============================================
        if (isFacingRight) {
            currentFrame = walkRightAnimation.getKeyFrame(animationRightTime);
        }
        else if (!isFacingRight) {
            currentFrame = walkLeftAnimation.getKeyFrame(animationLeftTime);
        }

        batch.draw(currentFrame, isFacingRight ? hitBox.x + currentFrame.getRegionWidth() : hitBox.x, hitBox.y, isFacingRight ? -currentFrame.getRegionWidth() : currentFrame.getRegionWidth(), currentFrame.getRegionHeight());

    }
}
