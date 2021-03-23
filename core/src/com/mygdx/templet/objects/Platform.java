package com.mygdx.templet.objects;

public class Platform extends GenericObjects{

    public Platform(float x, float y, float width, float height) {
        super(x, y);
        hitBox.width = width;
        hitBox.height = height;
    }
}
