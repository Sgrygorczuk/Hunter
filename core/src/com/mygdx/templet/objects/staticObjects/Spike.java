package com.mygdx.templet.objects.staticObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import static com.mygdx.templet.Const.TILED_HEIGHT;
import static com.mygdx.templet.Const.TILED_WIDTH;

public class Spike extends StaticObjects {

    /**
     * Point thing that hurt player and sends them back to last platform
     * @param x position
     * @param y position
     * @param width dimension
     * @param texture dimension
     */
    public Spike(float x, float y, float width, Texture texture) {
        super(x, y, texture);
        hitBox.height = TILED_HEIGHT/2f;
        hitBox.width = width;
        this.texture = texture;
    }


    /**
     * Overwrite the basic draw function to draw multiple spike instances based on the given width
     * @param batch where it's drawn
     */
    @Override
    public void draw(SpriteBatch batch) {
        for(int i = 0; i < hitBox.width/TILED_WIDTH; i++){
            batch.draw(texture, hitBox.x + TILED_WIDTH * i, hitBox.y);
        }
    }
}
