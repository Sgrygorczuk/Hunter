package com.mygdx.templet.objects.genericObjects;

public class Door extends GenericObjects {

    private final String pathName;

    public Door(float x, float y, String pathName) {
        super(x, y);
        this.pathName = pathName;
    }

    public String getPathName(){ return pathName; }

}
