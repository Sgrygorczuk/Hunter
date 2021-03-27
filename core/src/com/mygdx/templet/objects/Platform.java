package com.mygdx.templet.objects;

public class Platform extends GenericObjects{

    /**
     * Big rectangles that if collided tell user to stand or push against them
     * @param x position
     * @param y position
     * @param width dimension
     * @param height dimension
     */
    public Platform(float x, float y, float width, float height) {
        super(x, y);
        hitBox.width = width;
        hitBox.height = height;
    }
}
