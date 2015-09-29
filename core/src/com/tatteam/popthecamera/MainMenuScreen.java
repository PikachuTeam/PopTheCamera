package com.tatteam.popthecamera;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 * Created by dongc_000 on 9/29/2015.
 */
public class MainMenuScreen implements Screen {

    private TextButton button;
    private TextButton.TextButtonStyle buttonStyle;
    private Stage stage;
    final PopTheCamera game;

    public MainMenuScreen(final PopTheCamera game) {
        stage = new Stage();

        this.game = game;

        buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = new BitmapFont();

        button = new TextButton("Classic", buttonStyle);

        stage.addActor(button);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();

        if (Gdx.input.isTouched()) {
            game.setScreen(new ClassicGameScreen());
//            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
