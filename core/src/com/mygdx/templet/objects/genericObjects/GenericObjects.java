package com.mygdx.templet.objects.genericObjects;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import static com.mygdx.templet.Const.FEET_HEAD_HEIGHT;
import static com.mygdx.templet.Const.TILED_HEIGHT;
import static com.mygdx.templet.Const.TILED_WIDTH;

public class GenericObjects {
    //HitBox that is used for position and display
    protected Rectangle hitBox;

    /**
     * The basic building block that holds the hitBox data
     * @param x position
     * @param y position
     */
    public GenericObjects(float x , float y){ this.hitBox = new Rectangle(x, y, TILED_WIDTH, TILED_HEIGHT); }

    /**
     * @return hitBox
     */
    public Rectangle getHitBox(){return hitBox;}

    //============================ X ===============================
    public float getX(){return hitBox.x;}
    public void setX(float x){hitBox.x = x;}

    //============================ Y ===========================
    public float getY(){return hitBox.y;}
    public void setY(float y){hitBox.y = y;}

    //========================= Width ===========================
    public void setWidth(float width){hitBox.width = width;}
    public float getWidth(){return hitBox.width;}

    //========================= Height =============================
    public void setHeight(float height){hitBox.height = height;}
    public float getHeight(){return hitBox.height;}


    /**
     * Tells us if the two hitBoxes are touching
     * @param other hitBox
     * @return if they are touching
     */
    public boolean isColliding(Rectangle other) { return this.hitBox.overlaps(other); }

    /**
     * Purpose: Draws the circle on the screen using render
     */
    public void drawDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(hitBox.x, hitBox.y, hitBox.width, hitBox.height);
    }
}
