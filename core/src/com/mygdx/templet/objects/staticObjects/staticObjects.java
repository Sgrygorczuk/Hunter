package com.mygdx.templet.objects.staticObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.templet.objects.GenericObjects;

public class staticObjects extends GenericObjects {

    protected Texture texture;

    public staticObjects(float x, float y, Texture texture) {
        super(x, y);
        this.texture = texture;
    }

    public void draw(SpriteBatch batch){
        batch.draw(texture, hitBox.x, hitBox.y, hitBox.width, hitBox.height);
    }


}
