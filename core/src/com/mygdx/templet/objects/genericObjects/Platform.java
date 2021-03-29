package com.mygdx.templet.objects.genericObjects;

public class Platform extends GenericObjects{

    private final int type;

    /**
     * Big rectangles that if collided tell user to stand or push against them
     * @param x position
     * @param y position
     * @param width dimension
     * @param height dimension
     */
    public Platform(float x, float y, float width, float height, int type) {
        super(x, y);
        hitBox.width = width;
        hitBox.height = height;
        this.type = type;
    }

    public int getType() { return type; }
}
