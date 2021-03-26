package com.mygdx.templet.objects.staticObjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.templet.tools.TextAlignment;

import java.util.ArrayList;

public class TalkingNPC extends staticObjects{

    private final Texture profileTexture;
    private ArrayList<String> dialogue = new ArrayList<>();
    private int linePlace = 0;
    private final String name;
    TextAlignment textAlignment = new TextAlignment();

    public TalkingNPC(float x, float y, Texture texture, Texture profileTexture, String dialogue, String name) {
        super(x, y, texture);
        hitBox.width = texture.getWidth();
        hitBox.height = texture.getHeight();
        this.profileTexture = profileTexture;
        this.dialogue = textAlignment.sentences(dialogue, 40);
        this.name = name;
    }

    public void updateDialogue(){
        if(linePlace < dialogue.size()){ linePlace++; }
    }

    public boolean isDialogueDone() { return linePlace == dialogue.size() - 1; }

    public void restartDialogue(){linePlace = 0;}

    public void drawSpeech(SpriteBatch batch, BitmapFont bitmapFont, float xPosition, float yPosition){
        //Draws Profile Pic
        batch.draw(profileTexture, 20 + xPosition, 187.5f + yPosition);

        //Draws the Dialogue
        bitmapFont.getData().setScale(0.25f);
        bitmapFont.setColor(Color.WHITE);
        GlyphLayout glyphLayout = new GlyphLayout();
        glyphLayout.setText(bitmapFont, dialogue.get(linePlace));
        bitmapFont.draw(batch, dialogue.get(linePlace), xPosition + 70, yPosition + 235);

        //Draws Name
        textAlignment.centerText(batch, bitmapFont, name, 20 + xPosition + profileTexture.getWidth()/2f, 182.5f);
    }
}
