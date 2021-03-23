package com.mygdx.templet.objects.staticObjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.templet.tools.TextAlignment;

import static com.mygdx.templet.Const.WORLD_WIDTH;

public class TalkingNPC extends staticObjects{

    private final Texture profileTexture;
    private final String dialogue;
    private final String name;

    public TalkingNPC(float x, float y, Texture texture, Texture profileTexture, String dialogue, String name) {
        super(x, y, texture);
        hitBox.width = texture.getWidth();
        hitBox.height = texture.getHeight();
        this.profileTexture = profileTexture;
        this.dialogue = dialogue;
        this.name = name;
    }

    public void drawSpeech(SpriteBatch batch, BitmapFont bitmapFont, float xPosition, float yPosition){
        batch.draw(profileTexture, 20 + xPosition, 187.5f + yPosition);
        TextAlignment textAlignment = new TextAlignment();
        bitmapFont.getData().setScale(0.25f);
        bitmapFont.setColor(Color.WHITE);
        GlyphLayout glyphLayout = new GlyphLayout();
        glyphLayout.setText(bitmapFont, textAlignment.addNewLine(dialogue, 40));
        bitmapFont.draw(batch, textAlignment.addNewLine(dialogue, 40), xPosition + 70, yPosition + 235);
        textAlignment.centerText(batch, bitmapFont, name, 20 + xPosition + profileTexture.getWidth()/2f, 182.5f);
    }
}
