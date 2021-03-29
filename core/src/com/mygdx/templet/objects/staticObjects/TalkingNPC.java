package com.mygdx.templet.objects.staticObjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.templet.tools.TextAlignment;

import java.util.ArrayList;

public class TalkingNPC extends StaticObjects {

    private final Texture profileTexture;       //Texture in menu dialogue
    private final ArrayList<String> dialogue;   //The dialogue broken into chunks that will fit in menu
    private int linePlace = 0;                  //Current place in the conversation
    private final String name;                  //Name of the player
    TextAlignment textAlignment = new TextAlignment();  //Used for text alignment

    /**
     * A NPC that will stand in a place and will chat with the player if interacted with
     * @param x position
     * @param y position
     * @param texture unit texture
     * @param profileTexture dialogue menu texture
     * @param dialogue conversation with player
     * @param name name of unit
     */
    public TalkingNPC(float x, float y, Texture texture, Texture profileTexture, String dialogue, String name) {
        super(x, y, texture);
        hitBox.width = texture.getWidth();
        hitBox.height = texture.getHeight();
        this.profileTexture = profileTexture;
        this.dialogue = textAlignment.sentences(dialogue, 40);
        this.name = name;
    }

    /**
     * Moves the conversation along
     */
    public void updateDialogue(){ if(linePlace < dialogue.size()){ linePlace++; } }

    /**
     * Checks if player reached on of conversation
     * @return are we at the end of the conversation array
     */
    public boolean isDialogueDone() { return linePlace == dialogue.size() - 1; }

    /**
     * Once player is done talking set the conversation back to the start
     */
    public void restartDialogue(){linePlace = 0;}

    /**
     * Draws the menu details
     * @param batch where its drawn
     * @param bitmapFont where the text is drawn
     * @param xPosition placement on the screen
     * @param yPosition placements on the screen
     */
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
