package com.mygdx.templet.objects.aliveObjects.enemies;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.templet.objects.aliveObjects.AliveObjects;

public class Enemy extends AliveObjects {
    /**
     * Generic Object that can encapsulate all enemy units
     * @param x position
     * @param y position
     * @param spriteSheet texture
     */
    public Enemy(float x, float y, TextureRegion[][] spriteSheet) {
        super(x, y, spriteSheet);
    }

    public void setDead(){ currentHealth = -1; }

}
