package com.tatteam.popthecamera;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.tatteam.popthecamera.actors.AlphaRectangle;
import com.tatteam.popthecamera.actors.Background;
import com.tatteam.popthecamera.actors.CameraButton;
import com.tatteam.popthecamera.actors.Dot;
import com.tatteam.popthecamera.actors.Indicator;
import com.tatteam.popthecamera.actors.Lens;
import com.tatteam.popthecamera.actors.TextView;

public class Main extends ApplicationAdapter implements InputProcessor, ActorGroup.OnShakeCompleteListener, CameraButton.OnPressFinishListener, AlphaRectangle.OnDisappearListener, Dot.OnFadeCompleteListener {

    private OrthographicCamera camera;
    private Indicator indicator;
    private Dot dot;
    private Background background;
    private Stage stage;
    private Stage flashStage;
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
    private float radius;
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

    public static boolean touchable = true;

    @Override
    public void create() {
        Log.enableLog = true;
        loadData();

        camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);

        rectangle = new AlphaRectangle();
        rectangle.setOnDisappearListener(this);

        SoundHelper.getInstance().initSound();

        stage = new Stage(new FitViewport(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT, camera));

        flashStage = new Stage(new ExtendViewport(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT));
//        flashStage = new Stage(new FillViewport(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT));

        atlas = new TextureAtlas(Gdx.files.internal("images/large/pop_the_camera.pack"));

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

        stage.getViewport().apply();
        stage.act();
        stage.draw();

        flashStage.getViewport().apply();
        flashStage.act();
        flashStage.draw();
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
        stage.getViewport().update(width, height);
    }

    private void init() {
        // Set up camera group. Camera group includes camera_button, background, lensGroup.
        cameraGroup = new ActorGroup();

        background = new Background(atlas.findRegion("camera"));
        cameraButton = new CameraButton(atlas.findRegion("camera_button"));
        cameraButton.setOnPressFinishListener(this);

        cameraGroup.setSize(background.getWidth(), background.getHeight());
        cameraGroup.setPosition(Constants.VIEWPORT_WIDTH / 2 - cameraGroup.getWidth() / 2, Constants.VIEWPORT_HEIGHT / 2 - cameraGroup.getHeight() / 2);
        cameraGroup.setOrigin(cameraGroup.getWidth() / 2, cameraGroup.getHeight() / 2);
        cameraButton.setPosition(cameraButton.getWidth(), cameraGroup.getHeight() - 110f);

        initLens();
        lensGroup.setPosition(cameraGroup.getWidth() / 2 - lensGroup.getWidth() / 2, cameraGroup.getHeight() / 2 - lensGroup.getHeight() / 2 - 30);

        cameraGroup.addActor(cameraButton);
        cameraGroup.addActor(background);
        cameraGroup.addActor(lensGroup.getActors());

        radius = lens3.getHeight() / 2;

        indicatorBeta = Math.toDegrees(Math.acos((2 * radius * radius - indicator.getWidth() * indicator.getWidth()) / (2 * radius * radius)));
        dotBeta = Math.toDegrees(Math.acos((2 * radius * radius - dot.getWidth() * dot.getWidth()) / (2 * radius * radius)));

        indicator.setAccelerator(2f);

        cameraGroup.setOnShakeCompleteListener(this);

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
        float offset1 = (lens2.getHeight() - lens3.getHeight()) / 2 - indicator.getHeight();
        indicator.setPosition(lens1.getWidth() / 2 - indicator.getWidth() / 2, (lens1.getHeight() - lens2.getHeight()) / 2 + lens2.getHeight() - indicator.getHeight() - offset1 / 2);
        indicator.setOrigin(indicator.getWidth() / 2f, -(lens3.getHeight() / 2f) - offset1 / 2f);
        dot = new Dot(atlas.findRegion("dot"));
        float offset2 = (lens2.getHeight() - lens3.getHeight()) / 2 - dot.getHeight();
        dot.setPosition(lens1.getWidth() / 2 - dot.getWidth() / 2, (lens1.getHeight() - lens2.getHeight()) / 2 + lens2.getHeight() - dot.getHeight() - offset2 / 2);
        dot.setOrigin(dot.getWidth() / 2, -(lens3.getHeight() / 2) - offset2 / 2);
        dot.setOnFadeCompleteListener(this);

        // set color
        ColorHelper.getInstance().setColor(lens2, lens3, lens4);

        lensGroup.addActor(lens1);
        lensGroup.addActor(lens2);
        lensGroup.addActor(lens3);
        lensGroup.addActor(lens4);
        lensGroup.addActor(dot);
        lensGroup.addActor(indicator);

        e = indicatorBeta / 7;
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
                indicator.isMoving = false;
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
                        index.setText("" + currentIndex);
                        Log.writeLog("Check touch");
                        Log.writeLog("Indicator Rotation", "" + indicationRotation);
                        Log.writeLog("Dot Rotation", "" + dot.getRotation());
                        Log.writeLog("Delta 2", "" + delta);
                        Log.writeLog("Dot beta", "" + dotBeta / 2);
                        Log.writeLog("Indicator beta", "" + indicatorBeta / 8);
                        Log.writeLog("Ting ting.");
                        SoundHelper.getInstance().playSuccessSound();
                        if (currentIndex != 0) {
                            dot.fadeOut(2);
                            indicator.isMoving = false;
                        } else {
                            stopGame(1);
                        }
                    } else {
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
            Log.writeLog("Release sound button");
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
        index.setText("" + currentIndex);
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
        rectangle.appear(flashStage);
    }

    @Override
    public void onDisappear(AlphaRectangle alphaRectangle) {
        touchable = true;
        alphaRectangle.getParent().removeActor(alphaRectangle);
        indicator.resetAngle();
        dot.initPosition();
        level.setText("Level " + number);
        index.setText("" + currentIndex);
        playAgain = false;
        currentBackgroundColor = ColorHelper.getInstance().getNormalColor(ColorHelper.getInstance().getIndex());
        ColorHelper.getInstance().setColor(lens2, lens3, lens4);
        if (dot.getRotation() >= 0 && dot.getRotation() <= 180) {
            indicator.clockwise = false;
        } else if (dot.getRotation() > 180 && dot.getRotation() < 360) {
            indicator.clockwise = true;
        }
    }

    private void initTextView() {
        level = new TextView(Gdx.files.internal("fonts/aloha.fnt"), Gdx.files.internal("fonts/aloha_00.png"));
        index = new TextView(Gdx.files.internal("fonts/aloha.fnt"), Gdx.files.internal("fonts/aloha_00.png"));
        level.setText("Level " + number);
        index.setText("" + currentIndex);

        stage.addActor(level);
        stage.addActor(index);

        level.setPosition(stage.getViewport().getWorldWidth() / 2 - level.getWidth() / 2, stage.getViewport().getWorldHeight() / 4 - level.getHeight() / 4);
        index.setPosition(stage.getViewport().getWorldWidth() / 2 - index.getWidth() / 3, 3 * stage.getViewport().getWorldHeight() / 4 + 1.2f * index.getHeight());
    }

    private void initButton() {
        if (SoundHelper.enableSound) {
            soundButton = new Button(atlas, "on_sound");
        } else {
            soundButton = new Button(atlas, "off_sound");
        }
        soundButton.setPosition(stage.getViewport().getWorldWidth() - 3 * soundButton.getWidth() / 2f, stage.getViewport().getWorldHeight() - 3 * soundButton.getHeight() / 2f);
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
//        number = preferences.getInteger("number", 1);
        number = 1;
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
                dot.randomPosition(indicator.clockwise);
                if (indicator.clockwise) {
                    indicator.clockwise = false;
                } else {
                    indicator.clockwise = true;
                }
                break;
        }
        dot.fadeIn(type);
    }

    @Override
    public void onFadeInComplete(int type) {
        switch (type) {
            case 1:
                indicator.setRotation(0);
                playAgain = false;
                currentBackgroundColor = ColorHelper.getInstance().getNormalColor(ColorHelper.getInstance().getIndex() - 1);
                indicator.resetAngle();
                if (dot.getRotation() >= 0 && dot.getRotation() <= 180) {
                    indicator.clockwise = false;
                } else if (dot.getRotation() > 180 && dot.getRotation() < 360) {
                    indicator.clockwise = true;
                }
                break;
            case 2:
                break;
        }
        indicator.isMoving = true;
    }
}
