package com.mygdx.templet.objects.staticObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import static com.mygdx.templet.Const.TILED_HEIGHT;
import static com.mygdx.templet.Const.TILED_WIDTH;

public class Spike extends staticObjects{

    public Spike(float x, float y, float width, Texture texture) {
        super(x, y, texture);
        hitBox.height = TILED_HEIGHT/2f;
        hitBox.width = width;
        this.texture = texture;
    }


    @Override
    public void draw(SpriteBatch batch) {
        for(int i = 0; i < hitBox.width/TILED_WIDTH; i++){
            batch.draw(texture, hitBox.x + TILED_WIDTH * i, hitBox.y);
        }
    }
}
