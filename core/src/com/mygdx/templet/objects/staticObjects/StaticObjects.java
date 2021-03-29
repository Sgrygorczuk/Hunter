package com.mygdx.templet.objects.staticObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.templet.objects.genericObjects.GenericObjects;

public class StaticObjects extends GenericObjects {

    //Texture displayed
    protected Texture texture;

    /**
     * Generic static object that has a single texture and doesn't move
     * @param x position
     * @param y position
     * @param texture image
     */
    public StaticObjects(float x, float y, Texture texture) {
        super(x, y);
        this.texture = texture;
    }

    /**
     * Draws the texture
     * @param batch where it's drawn
     */
    public void draw(SpriteBatch batch){
        batch.draw(texture, hitBox.x, hitBox.y, hitBox.width, hitBox.height);
    }


}
