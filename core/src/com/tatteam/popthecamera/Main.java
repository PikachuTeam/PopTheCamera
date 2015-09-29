package com.tatteam.popthecamera;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tatteam.popthecamera.actors.AlphaRectangle;
import com.tatteam.popthecamera.actors.Background;
import com.tatteam.popthecamera.actors.CameraButton;
import com.tatteam.popthecamera.actors.Dot;
import com.tatteam.popthecamera.actors.Indicator;
import com.tatteam.popthecamera.actors.Lens;
import com.tatteam.popthecamera.actors.TextView;

public class Main extends ApplicationAdapter implements InputProcessor, ActorGroup.OnShakeCompleteListener, CameraButton.OnPressFinishListener, AlphaRectangle.OnDisappearListener, Dot.OnFadeCompleteListener {

    private OrthographicCamera camera;
    private Camera splashCamera;
    private Viewport fitViewport;
    private Viewport screenViewport;
    private Indicator indicator;
    private Dot dot;
    private Background background;
    private Stage stage;
    private Stage splashStage;
    private TextureAtlas atlas;
    private ActorGroup cameraGroup;
    private ActorGroup lensGroup;
    private Lens lens1;
    private Lens lens2;
    private Lens lens3;
    private Lens lens4;
    private CameraButton cameraButton;
    private int number;
    private int currentIndex;
    private double indicatorBeta;
    private double dotBeta;
    private boolean playAgain = false;
    private TextView level;
    private TextView index;
    private AlphaRectangle rectangle;
    private double e;
    private Color currentBackgroundColor;
    private Button soundButton;
    private Button vibrationButton;
    private Vector3 touchPoint;
    private Preferences preferences;
    private boolean currentOrientation = true;

    public static boolean touchable = true;
    private static boolean checkable = true;

    @Override
    public void create() {
        Log.enableLog = false;
        loadData();

        AssetsLoader.getInstance().init();
        SoundHelper.getInstance().initSound();

        stage = new Stage();
        splashStage = new Stage();

        camera = new OrthographicCamera();
        fitViewport = new FitViewport(AssetsLoader.getInstance().getViewPortSize().getWidth(), AssetsLoader.getInstance().getViewPortSize().getHeight(), camera);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        stage.setViewport(fitViewport);

        splashCamera = new OrthographicCamera(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        screenViewport = new ScreenViewport(splashCamera);
        splashCamera.position.set(splashCamera.viewportWidth / 2, splashCamera.viewportHeight / 2, 0);
        splashStage.setViewport(screenViewport);

        rectangle = new AlphaRectangle();
        rectangle.setOnDisappearListener(this);

        atlas = new TextureAtlas(Gdx.files.internal(AssetsLoader.getInstance().getImagePath() + "pop_the_camera.pack"));

        ColorHelper.getInstance().initColor();
        currentBackgroundColor = ColorHelper.getInstance().getNormalColor(0);

        init();

        dot.initPosition();

        stage.addActor(cameraGroup.getActors());

        if (dot.getRotation() >= 0 && dot.getRotation() <= 180) {
            indicator.clockwise = false;
        } else if (dot.getRotation() > 180 && dot.getRotation() < 360) {
            indicator.clockwise = true;
        }

        initButton();

        touchPoint = new Vector3();

        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(currentBackgroundColor.r, currentBackgroundColor.g, currentBackgroundColor.b, currentBackgroundColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        checkOver();

        fitViewport.apply();
        stage.act();
        stage.draw();

        screenViewport.apply();
        splashStage.act();
        splashStage.draw();
    }

    @Override
    public void dispose() {
        saveData();
        stage.dispose();
        ColorHelper.getInstance().dispose();
        SoundHelper.getInstance().dispose();
        soundButton.dispose();
        atlas.dispose();
        super.dispose();
    }

    @Override
    public void resize(int width, int height) {
        fitViewport.update(width, height);
        screenViewport.update(width, height);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        splashCamera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
    }

    private void init() {
        // Set up camera group. Camera group includes camera_button, background, lensGroup.
        cameraGroup = new ActorGroup();

        background = new Background(atlas.findRegion("camera"));
        cameraButton = new CameraButton(atlas.findRegion("camera_button"));
        cameraButton.setOnPressFinishListener(this);

        cameraGroup.setSize(background.getWidth(), background.getHeight());
        cameraGroup.setPosition(fitViewport.getWorldWidth() / 2 - cameraGroup.getWidth() / 2, fitViewport.getWorldHeight() / 2 - cameraGroup.getHeight() / 2);
        cameraGroup.setOrigin(cameraGroup.getWidth() / 2, cameraGroup.getHeight() / 2);

        cameraButton.setPosition(cameraButton.getWidth(), cameraGroup.getHeight() - cameraButton.getHeight() * 3.85f);

        initLens();
        lensGroup.setPosition(cameraGroup.getWidth() / 2 - lensGroup.getWidth() / 2, cameraGroup.getHeight() / 2 - lensGroup.getHeight() / 2 - 30);

        cameraGroup.addActor(cameraButton);
        cameraGroup.addActor(background);
        cameraGroup.addActor(lensGroup.getActors());
        cameraGroup.setOnShakeCompleteListener(this);

        indicator.setAccelerator(Constants.DOT_ROTATION_ACCELERATOR);

        initTextView();
    }

    // Set up lens group
    // Lens group includes indicator, dot, lens1, lens2, lens 3, lens4.
    private void initLens() {
        lensGroup = new ActorGroup();

        lens1 = new Lens(atlas.findRegion("lens1"));
        lens2 = new Lens(atlas.findRegion("lens2"));
        lens3 = new Lens(atlas.findRegion("lens3"));
        lens4 = new Lens(atlas.findRegion("lens4"));

        lensGroup.setSize(lens1.getWidth(), lens1.getHeight());

        lens1.setPosition(0, 0);
        lens2.setCenterPosition(lensGroup.getWidth(), lensGroup.getHeight());
        lens3.setCenterPosition(lensGroup.getWidth(), lensGroup.getHeight());
        lens4.setCenterPosition(lensGroup.getWidth(), lensGroup.getHeight());

        indicator = new Indicator(atlas.findRegion("indicator"));
        float indicatorOffset = (lens2.getHeight() - lens3.getHeight()) / 2 - indicator.getHeight();
        indicator.setPosition(lens1.getWidth() / 2 - indicator.getWidth() / 2, (lens1.getHeight() - lens2.getHeight()) / 2 + lens2.getHeight() - indicator.getHeight() - indicatorOffset / 2);
        indicator.setOrigin(indicator.getWidth() / 2f, -(lens3.getHeight() / 2f) - indicatorOffset / 2f);

        dot = new Dot(atlas.findRegion("dot"));
        float dotOffset = (lens2.getHeight() - lens3.getHeight()) / 2 - dot.getHeight();
        dot.setPosition(lens1.getWidth() / 2 - dot.getWidth() / 2, (lens1.getHeight() - lens2.getHeight()) / 2 + lens2.getHeight() - dot.getHeight() - dotOffset / 2);
        dot.setOrigin(dot.getWidth() / 2, -(lens3.getHeight() / 2) - dotOffset / 2);
        dot.setOnFadeCompleteListener(this);

        float dotRadius = lens3.getHeight() / 2 + dotOffset / 2;
        float indicatorRadius = lens3.getHeight() / 2 + indicatorOffset / 2;
        indicatorBeta = Math.toDegrees(Math.acos((2 * indicatorRadius * indicatorRadius - indicator.getWidth() * indicator.getWidth()) / (2 * indicatorRadius * indicatorRadius)));
        dotBeta = Math.toDegrees(Math.acos((2 * dotRadius * dotRadius - dot.getWidth() * dot.getWidth()) / (2 * dotRadius * dotRadius)));

        // set color
        ColorHelper.getInstance().setColor(lens2, lens3, lens4);

        lensGroup.addActor(lens1);
        lensGroup.addActor(lens2);
        lensGroup.addActor(lens3);
        lensGroup.addActor(lens4);
        lensGroup.addActor(dot);
        lensGroup.addActor(indicator);

        e = indicatorBeta / 4;
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
        if (touchable) {
            camera.unproject(touchPoint.set(screenX, screenY, 0));
            if (touchPoint.x >= soundButton.getX() && touchPoint.x <= soundButton.getX() + soundButton.getWidth() && touchPoint.y >= soundButton.getY() && touchPoint.y <= soundButton.getY() + soundButton.getHeight()) {
                soundButton.setImage("press_sound");
            } else if (touchPoint.x >= vibrationButton.getX() && touchPoint.x <= vibrationButton.getX() + vibrationButton.getWidth() && touchPoint.y >= vibrationButton.getY() && touchPoint.y <= vibrationButton.getY() + vibrationButton.getHeight()) {
                vibrationButton.setImage("press_vibrate");
            } else if (playAgain) {
                dot.fadeOut(1);
                indicator.fadeOut();
                updateTextView(2);
                checkable = false;
            } else {
                if (!indicator.isMoving) {
                    indicator.isMoving = true;
                } else {
                    double indicationRotation = recalculateAngleIfNeed(indicator.getRotation());
                    double delta;
                    if (indicator.clockwise) {
                        if (indicationRotation == 0) {
                            indicationRotation = 360;
                        }
                    }
                    if (isSameSide(indicationRotation, dot.getRotation())) {
                        if (indicationRotation >= dot.getRotation()) {
                            delta = indicationRotation - dot.getRotation();
                        } else {
                            delta = -indicationRotation + dot.getRotation();
                        }
                    } else {
                        if (indicationRotation >= dot.getRotation()) {
                            delta = 360 - indicationRotation + dot.getRotation();
                        } else {
                            delta = 360 - dot.getRotation() + indicationRotation;
                        }
                    }
                    delta -= e;
                    if (delta <= dotBeta / 2) {
                        currentIndex--;
                        updateTextView(2);
                        Log.writeLog("Check touch");
                        Log.writeLog("Indicator Rotation", "" + indicationRotation);
                        Log.writeLog("Dot Rotation", "" + dot.getRotation());
                        Log.writeLog("Delta 2", "" + delta);
                        Log.writeLog("Dot beta", "" + dotBeta / 2);
                        Log.writeLog("Indicator beta", "" + indicatorBeta / 8);
                        Log.writeLog("Ting ting.");
                        SoundHelper.getInstance().playSuccessSound();
                        if (currentIndex != 0) {
                            currentOrientation = indicator.clockwise;
                            dot.fadeOut(2);
                            if (indicator.clockwise) {
                                indicator.clockwise = false;
                            } else {
                                indicator.clockwise = true;
                            }
                            checkable = false;
                        } else {
                            stopGame(1);
                        }
                    } else {
                        Log.writeLog("Check touch");
                        Log.writeLog("Indicator Rotation", "" + indicationRotation);
                        Log.writeLog("Dot Rotation", "" + dot.getRotation());
                        Log.writeLog("Delta 2", "" + delta);
                        Log.writeLog("Dot beta", "" + dotBeta / 2);
                        Log.writeLog("Indicator beta", "" + indicatorBeta / 8);
                        Log.writeLog("Tach.");
                        stopGame(2);
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        camera.unproject(touchPoint.set(screenX, screenY, 0));
        if (touchPoint.x >= soundButton.getX() && touchPoint.x <= soundButton.getX() + soundButton.getWidth() && touchPoint.y >= soundButton.getY() && touchPoint.y <= soundButton.getY() + soundButton.getHeight()) {
            if (SoundHelper.enableSound) {
                soundButton.setImage("off_sound");
                SoundHelper.enableSound = false;
            } else {
                SoundHelper.enableSound = true;
                soundButton.setImage("on_sound");
            }
        } else if (touchPoint.x >= vibrationButton.getX() && touchPoint.x <= vibrationButton.getX() + vibrationButton.getWidth() && touchPoint.y >= vibrationButton.getY() && touchPoint.y <= vibrationButton.getY() + vibrationButton.getHeight()) {
            if (VibrationHelper.enableVibration) {
                vibrationButton.setImage("off_vibrate");
                VibrationHelper.enableVibration = false;
            } else {
                vibrationButton.setImage("on_vibrate");
                VibrationHelper.enableVibration = true;
            }
        }
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
        touchable = true;
    }

    // situation: 1-win; 2-lose
    private void stopGame(int situation) {
        switch (situation) {
            case 1:
                number++;
                cameraButton.press();
                break;
            case 2:
                playAgain = true;
                cameraGroup.shake();
                SoundHelper.getInstance().playFailSound();
                touchable = false;
                VibrationHelper.vibrate(1);
                currentBackgroundColor = ColorHelper.FAIL_COLOR;
                break;
        }
        currentIndex = number;
        indicator.isMoving = false;
    }

    private void checkOver() {
        if (checkable) {
            if (indicator.isMoving) {
                double indicationRotation = recalculateAngleIfNeed(indicator.getRotation());
                double delta;

                if (indicator.clockwise) {
                    if (indicationRotation == 0) {
                        indicationRotation = 360;
                    }
                }

                if (isSameSide(indicationRotation, dot.getRotation())) {
                    if (indicationRotation >= dot.getRotation()) {
                        delta = indicationRotation - dot.getRotation();
                    } else {
                        delta = -indicationRotation + dot.getRotation();
                    }
                } else {
                    if (indicationRotation >= dot.getRotation()) {
                        delta = 360 - indicationRotation + dot.getRotation();
                    } else {
                        delta = 360 - dot.getRotation() + indicationRotation;
                    }
                }
                delta -= e;
                if (indicator.clockwise) {
                    if (isSameSide(indicationRotation, dot.getRotation())) {
                        if (indicationRotation < dot.getRotation()) {
                            if (delta > dotBeta / 2) {
                                Log.writeLog("Check over 1");
                                Log.writeLog("Indicator Rotation", "" + indicationRotation);
                                Log.writeLog("Dot Rotation", "" + dot.getRotation());
                                Log.writeLog("Delta 2", "" + delta);
                                Log.writeLog("Dot beta", "" + dotBeta / 2);
                                Log.writeLog("Indicator beta", "" + indicatorBeta / 8);
                                Log.writeLog("Tach.");
                                stopGame(2);
                            }
                        }
                    } else {
                        if (dot.getRotation() >= 0 && dot.getRotation() <= 180) {
                            if (delta > dotBeta / 2) {
                                Log.writeLog("Check over 2");
                                Log.writeLog("Indicator Rotation", "" + indicationRotation);
                                Log.writeLog("Dot Rotation", "" + dot.getRotation());
                                Log.writeLog("Delta 2", "" + delta);
                                Log.writeLog("Dot beta", "" + dotBeta / 2);
                                Log.writeLog("Indicator beta", "" + indicatorBeta / 8);
                                Log.writeLog("Tach.");
                                stopGame(2);
                            }
                        }
                    }
                } else {
                    if (isSameSide(indicationRotation, dot.getRotation())) {
                        if (indicationRotation > dot.getRotation()) {
                            if (delta > dotBeta / 2) {
                                Log.writeLog("Check over 3");
                                Log.writeLog("Indicator Rotation", "" + indicationRotation);
                                Log.writeLog("Dot Rotation", "" + dot.getRotation());
                                Log.writeLog("Delta 2", "" + delta);
                                Log.writeLog("Dot beta", "" + dotBeta / 2);
                                Log.writeLog("Indicator beta", "" + indicatorBeta / 8);
                                Log.writeLog("Tach.");
                                stopGame(2);
                            }
                        }
                    } else {
                        if (dot.getRotation() >= 180 && dot.getRotation() <= 360) {
                            if (delta > dotBeta / 2) {
                                Log.writeLog("Check over 4");
                                Log.writeLog("Indicator Rotation", "" + indicationRotation);
                                Log.writeLog("Dot Rotation", "" + dot.getRotation());
                                Log.writeLog("Delta 2", "" + delta);
                                Log.writeLog("Dot beta", "" + dotBeta / 2);
                                Log.writeLog("Indicator beta", "" + indicatorBeta / 8);
                                Log.writeLog("Tach.");
                                stopGame(2);
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean isSameSide(double angle1, double angle2) {
        return ((angle1 >= 0 && angle2 >= 0 && angle1 <= 180 && angle2 <= 180) || (angle1 >= 180 && angle2 >= 180 && angle1 <= 360 && angle2 <= 360));
    }

    private double recalculateAngleIfNeed(double angle) {
        double tmp = angle;
        if (angle < 0) {
            tmp = 360 + angle;
        }
        if (angle >= 360) {
            tmp = angle - 360;
        }
        return tmp;
    }

    @Override
    public void onPressFinish() {
        SoundHelper.getInstance().playFinishLevelSound();
        Color color = ColorHelper.getInstance().getNormalColor(ColorHelper.getInstance().getIndex() - 1);
        rectangle.setColor(color.r, color.g, color.b, color.a * 0.5f);
        rectangle.appear(splashStage);
        indicator.fadeOut();
        dot.fadeOut(1);
    }

    @Override
    public void onDisappear(AlphaRectangle alphaRectangle) {
        touchable = true;
        alphaRectangle.getParent().removeActor(alphaRectangle);
        updateTextView(1);
        updateTextView(2);
        playAgain = false;
        currentBackgroundColor = ColorHelper.getInstance().getNormalColor(ColorHelper.getInstance().getIndex());
        ColorHelper.getInstance().setColor(lens2, lens3, lens4);
    }

    private void initTextView() {
        level = new TextView(Gdx.files.internal(AssetsLoader.getInstance().getFontPath() + "calibri.fnt"), Gdx.files.internal(AssetsLoader.getInstance().getFontPath() + "calibri.png"));
        index = new TextView(Gdx.files.internal(AssetsLoader.getInstance().getFontPath() + "calibri_index.fnt"), Gdx.files.internal(AssetsLoader.getInstance().getFontPath() + "calibri_index.png"));
        level.setText("Level " + number);
        index.setText("" + currentIndex);

        stage.addActor(level);
        stage.addActor(index);

        level.setPosition(stage.getViewport().getWorldWidth() / 2 - level.getWidth() / 2, stage.getViewport().getWorldHeight() / 4 - level.getHeight());
        index.setPosition(stage.getViewport().getWorldWidth() / 2 - index.getWidth() / 2, 3 * stage.getViewport().getWorldHeight() / 4 + 1.75f * index.getHeight());
    }

    private void initButton() {
        if (SoundHelper.enableSound) {
            soundButton = new Button(atlas, "on_sound");
        } else {
            soundButton = new Button(atlas, "off_sound");
        }
        soundButton.setPosition(fitViewport.getWorldWidth() - soundButton.getWidth() * 1.25f, fitViewport.getWorldHeight() - soundButton.getHeight() * 1.25f);
        soundButton.addTo(stage);
        if (VibrationHelper.enableVibration) {
            vibrationButton = new Button(atlas, "on_vibrate");
        } else {
            vibrationButton = new Button(atlas, "off_vibrate");
        }
        vibrationButton.setPosition(soundButton.getX() - 5 * vibrationButton.getWidth() / 4, soundButton.getY());
        vibrationButton.addTo(stage);
    }

    private void loadData() {
        preferences = Gdx.app.getPreferences(Constants.APP_TITLE);
        number = preferences.getInteger("number", 1);
        currentIndex = number;
        SoundHelper.enableSound = preferences.getBoolean("sound", true);
        VibrationHelper.enableVibration = preferences.getBoolean("vibration", true);
    }

    private void saveData() {
        preferences = Gdx.app.getPreferences(Constants.APP_TITLE);
        preferences = Gdx.app.getPreferences(Constants.APP_TITLE);
        preferences.putInteger("number", number);
        preferences.putBoolean("sound", SoundHelper.enableSound);
        preferences.putBoolean("vibration", VibrationHelper.enableVibration);
        preferences.flush();
    }

    @Override
    public void onFadeOutComplete(int type) {
        switch (type) {
            case 1:
                dot.initPosition();
                break;
            case 2:
                dot.randomPosition(currentOrientation);
                break;
        }
        dot.fadeIn(type);
    }

    @Override
    public void onFadeInComplete(int type) {
        if (type == 1) {
            indicator.setRotation(0);
            playAgain = false;
            currentBackgroundColor = ColorHelper.getInstance().getNormalColor(ColorHelper.getInstance().getIndex() - 1);
            if (dot.getRotation() >= 0 && dot.getRotation() <= 180) {
                indicator.clockwise = false;
            } else if (dot.getRotation() > 180 && dot.getRotation() < 360) {
                indicator.clockwise = true;
            }
        }
        checkable = true;
    }

    public void updateTextView(int type) {
        switch (type) {
            case 1:
                level.setText("Level " + number);
                if (number % 10 == 0) {
                    level.setX(stage.getViewport().getWorldWidth() / 2 - level.getWidth() / 2);
                }
                break;
            case 2:
                index.setText("" + currentIndex);
                if ((currentIndex + 1) % 10 == 0 || currentIndex == number) {
                    index.setX(stage.getViewport().getWorldWidth() / 2 - index.getWidth() / 2);
                }
                break;
        }
    }
}
