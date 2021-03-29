package com.mygdx.templet.objects.staticObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Collectible extends StaticObjects {

    private final String name;
    private boolean hasBeenCollected;
    private boolean canBeCollected;

    /**
     * Generic static object that has a single texture and doesn't move
     *
     * @param x       position
     * @param y       position
     * @param texture image
     */
    public Collectible(float x, float y, Texture texture, String name, boolean hasBeenCollected) {
        super(x, y, texture);
        this.name = name;
        this.hasBeenCollected = hasBeenCollected;
        hitBox.width = texture.getWidth();
        hitBox.height = texture.getHeight();
        canBeCollected = name.equals("Basic");
    }

    public String  getName(){ return name;}

    public void setCanBeCollected(){canBeCollected = true;}

    public void setCollected(){hasBeenCollected = true;}

    @Override
    public boolean isColliding(Rectangle other) {
        return super.isColliding(other) && !hasBeenCollected;
    }

    @Override
    public void draw(SpriteBatch batch) {
        if(canBeCollected && !hasBeenCollected) { super.draw(batch); }
    }
}
