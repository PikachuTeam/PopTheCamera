package com.tatteam.popthecamera;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.tatteam.popthecamera.actors.Background;
import com.tatteam.popthecamera.actors.CameraButton;
import com.tatteam.popthecamera.actors.Dot;
import com.tatteam.popthecamera.actors.Indicator;
import com.tatteam.popthecamera.actors.Lens;

public class Main extends ApplicationAdapter implements InputProcessor, ActorGroup.OnShakeCompleteListener {

    private OrthographicCamera camera;
    private Indicator indicator;
    private Dot dot;
    private Background background;
    private Stage stage;
    private TextureAtlas atlas;
    private ActorGroup cameraGroup;
    private ActorGroup lensGroup;
    private Lens lens1;
    private Lens lens2;
    private Lens lens3;
    private Lens lens4;
    private CameraButton cameraButton;

    @Override
    public void create() {
//        camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        stage = new Stage(new FitViewport(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT));
        stage.getViewport().apply();
//        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);

        atlas = new TextureAtlas(Gdx.files.internal("images/pop_the_camera.pack"));

        init();

        stage.addActor(cameraGroup.getActors());

        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.5f, 0.9f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
//        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0f);
    }

    private void init() {
        // Set up camera group. Camera group includes camera_button, background, lens1, lensGroup.
        cameraGroup = new ActorGroup();
        cameraGroup.setSize(Constants.BACKGROUND_WIDTH, Constants.BACKGROUND_HEIGHT);
        cameraGroup.setPosition(Constants.VIEWPORT_WIDTH / 2 - cameraGroup.getWidth() / 2, Constants.BACKGROUND_HEIGHT / 2 + cameraGroup.getHeight() / 4);
        cameraGroup.setOrigin(cameraGroup.getWidth() / 2, cameraGroup.getHeight() / 2);

        background = new Background(atlas.findRegion("camera"), Constants.BACKGROUND_WIDTH, Constants.BACKGROUND_HEIGHT, 0, 0);
        cameraButton = new CameraButton(atlas.findRegion("camera_button"), Constants.CAMERA_BUTTON_WIDTH, Constants.CAMERA_BUTTON_HEIGHT, Constants.CAMERA_BUTTON_X, Constants.CAMERA_BUTTON_Y);
        lens1 = new Lens(atlas.findRegion("lens1"), Constants.LENS1_WIDTH, Constants.LENS1_HEIGHT, Constants.LENS1_X, Constants.LENS1_Y);
        initLens();

        cameraGroup.addActor(cameraButton);
        cameraGroup.addActor(background);
        cameraGroup.addActor(lens1);
        cameraGroup.addActor(lensGroup.getActors());

        cameraGroup.setOnShakeCompleteListener(this);
    }

    // Set up lens group
    // Lens group includes indicator, dot, lens2, lens 3, lens4.
    private void initLens() {
        lensGroup = new ActorGroup();
        lensGroup.setSize(Constants.LENS2_WIDTH, Constants.LENS2_HEIGHT);
        lensGroup.setPosition(Constants.GROUP_LENS_X, Constants.GROUP_LENS_Y);

        lens2 = new Lens(atlas.findRegion("lens2"), Constants.LENS2_WIDTH, Constants.LENS2_HEIGHT, Constants.LEN2_X, Constants.LEN2_Y);
        lens3 = new Lens(atlas.findRegion("lens3"), Constants.LENS3_WIDTH, Constants.LENS3_HEIGHT, Constants.LENS3_X, Constants.LENS3_Y);
        lens4 = new Lens(atlas.findRegion("lens4"), Constants.LENS4_WIDTH, Constants.LENS4_HEIGHT, Constants.LENS4_X, Constants.LENS4_Y);
        indicator = new Indicator(atlas.findRegion("indicator"), Constants.INDICATOR_WIDTH, Constants.INDICATOR_HEIGHT, Constants.INDICATOR_X, Constants.INDICATOR_Y, Constants.INDICATOR_ORIGIN_X, Constants.INDICATOR_ORIGIN_Y);
        dot = new Dot(atlas.findRegion("dot"), Constants.DOT_WIDTH, Constants.DOT_HEIGHT, Constants.DOT_X, Constants.DOT_Y, Constants.DOT_ORIGIN_X, Constants.DOT_ORIGIN_Y);

        lensGroup.addActor(lens2);
        lensGroup.addActor(lens3);
        lensGroup.addActor(lens4);
        lensGroup.addActor(dot);
        lensGroup.addActor(indicator);
        dot.setRotation(30);
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        cameraGroup.shake();
        cameraButton.press();
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    public void onShakeComplete() {
        Gdx.app.log(Constants.APP_TITLE, "Complete");
    }
}
