package com.tatteam.popthecamera;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Created by dongc_000 on 9/27/2015.
 */
public class Button {

    private ImageButton button;
    private Skin buttonSkin;

    public Button(TextureAtlas buttonTexturePacker, String imageName) {
        buttonSkin = new Skin();
        buttonSkin.addRegions(buttonTexturePacker);
        button = new ImageButton(buttonSkin.getDrawable(imageName));

        button.setWidth(buttonTexturePacker.findRegion(imageName).getRegionWidth());
        button.setWidth(buttonTexturePacker.findRegion(imageName).getRegionHeight());

    }

    public void addTo(Stage stage) {
        stage.addActor(button);
    }

    public void setPosition(float x, float y) {
        button.setPosition(x, y);
    }

    public void setImage(String imageName) {
        button.getStyle().imageUp = buttonSkin.getDrawable(imageName);
    }

    public void setButtonSkin(TextureAtlas buttonSkin) {
        this.buttonSkin.addRegions(buttonSkin);
    }

    public void setWidth(float width) {
        button.setWidth(width);
    }

    public void setHeight(float height) {
        button.setHeight(height);
    }

    public float getX() {
        return button.getX();
    }

    public float getY() {
        return button.getY();
    }

    public float getWidth() {
        return button.getWidth();
    }

    public float getHeight() {
        return button.getHeight();
    }

    public void dispose() {
        buttonSkin.dispose();
    }
}

