package com.tatteam.popthecamera;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by dongc_000 on 9/29/2015.
 */
public class PopTheCamera extends Game {

//    public Stage stage;
//    private OrthographicCamera fitCamera;
//    public Viewport fitViewport;
//    public Stage flashStage;
//    private OrthographicCamera screenCamera;
//    public Viewport screenViewport;

    public SpriteBatch batch;
    private Screen previousScreen;

    @Override
    public void create() {
        AssetsLoader.getInstance().init();
        SoundHelper.getInstance().initSound();

//        fitCamera = new OrthographicCamera();
//        fitViewport = new FitViewport(AssetsLoader.getInstance().getViewPortSize().getWidth(), AssetsLoader.getInstance().getViewPortSize().getHeight(), fitCamera);
//        fitCamera.position.set(fitCamera.viewportWidth / 2, fitCamera.viewportHeight / 2, 0);
//        fitCamera.update();
//        stage = new Stage();
//        stage.setViewport(fitViewport);
//
//        screenCamera = new OrthographicCamera();
//        screenViewport = new ScreenViewport(screenCamera);
//        screenCamera.position.set(screenCamera.viewportWidth / 2, screenCamera.viewportHeight / 2, 0);
//        screenCamera.update();
//        stage = new Stage();
//        stage.setViewport(fitViewport);

        batch = new SpriteBatch();
        this.setScreen(new MainMenuScreen(this));
//        Gdx.input.setCatchBackKey(true);
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
