package com.mygdx.templet.objects.genericObjects;

public class CollectionZone extends GenericObjects {

    /**
     * The basic building block that holds the hitBox data
     *
     * @param x position
     * @param y position
     */
    public CollectionZone(float x, float y, float width, float height) {
        super(x, y);
        hitBox.width = width;
        hitBox.height = height;
    }


}
