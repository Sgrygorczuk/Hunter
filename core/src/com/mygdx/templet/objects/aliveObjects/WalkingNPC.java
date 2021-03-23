package com.mygdx.templet.objects.aliveObjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class WalkingNPC extends AliveObjects{

    float startX;
    float endX;

    public WalkingNPC(float x, float y, float width, TextureRegion[][] spriteSheet) {
        super(x, y, spriteSheet);
        hitBox.width = spriteSheet[0][0].getRegionWidth();
        hitBox.height = spriteSheet[0][0].getRegionHeight();

        startX = x;
        endX = x + width;

        animationFrameTime = 4;
    }

    public void update(float delta){

        if(!isFacingRight){ velocity.x = 1; }
        else{ velocity.x = -1; }

        animationRightTime += delta;
        animationLeftTime += delta;

        hitBox.x += velocity.x;

        if(hitBox.x + hitBox.width >= endX || hitBox.x <= startX){ isFacingRight = !isFacingRight; }


    }
}
