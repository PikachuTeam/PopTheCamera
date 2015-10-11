package com.tatteam.popthecamera;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tatteam.popthecamera.actors.Background;
import com.tatteam.popthecamera.actors.Camera;
import com.tatteam.popthecamera.actors.CameraButton;
import com.tatteam.popthecamera.actors.Dot;
import com.tatteam.popthecamera.actors.Flash;
import com.tatteam.popthecamera.actors.Indicator;
import com.tatteam.popthecamera.actors.Lens;
import com.tatteam.popthecamera.actors.TextView;

public class GDXGameLauncher extends ApplicationAdapter implements InputProcessor, ActorGroup.OnShakeCompleteListener, CameraButton.OnPressFinishListener, Flash.OnDisappearListener, Dot.OnFadeCompleteListener {

    private Constants.GameMode gameMode = Constants.GameMode.CLASSIC_SLOW;
    private final int CLASSIC_COLOR_STEP = 3;
    private final int UNLIMITED_COLOR_STEP = 5;

    private Viewport backgroundViewport;
    private Viewport fitViewport;
    private Viewport screenViewport;
    private Indicator indicator;
    private Dot dot;
    private Camera camera;
    private Stage stage;
    private Stage splashStage;
    private Stage backgroundStage;
    private TextureAtlas atlas;
    private ActorGroup cameraGroup;
    private ActorGroup lensGroup;
    private Lens lens1;
    private Lens lens2;
    private Lens lens3;
    private Lens lens4;
    private CameraButton cameraButton;
    private int classicLevel;
    private int classicScore;
    private double indicatorBeta;
    private double dotBeta;
    private boolean playAgain = false;
    private TextView level;
    private TextView index;
    private TextView classicType;
    private Flash flash;
    private double e;
    private Color currentBackgroundColor;
    private Button soundButton;
    private Button vibrationButton;
    private Button backButton;
    private Vector3 touchPoint;
    private Preferences preferences;
    private boolean currentOrientation = true;

    public static boolean touchable = true;
    private static boolean checkable = true;

    private OnGameListener onGameListener;
    private int unlimitedScore = 0;
    private int unlimitedBestScore = 0;
    private int unlimitedColorIndex = 0;
    private Background background;

    private int classicColorIndex = 0;
    private float touchOffset;

    @Override
    public void create() {
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        touchable = true;
        loadData();

        AssetsLoader.getInstance().init();
        SoundHelper.getInstance().initSound();

        stage = new Stage();
        splashStage = new Stage();
        backgroundStage = new Stage();

        fitViewport = new FitViewport(AssetsLoader.getInstance().getViewPortSize().getWidth(), AssetsLoader.getInstance().getViewPortSize().getHeight());
        fitViewport.update(AssetsLoader.getInstance().getViewPortSize().getWidth(), AssetsLoader.getInstance().getViewPortSize().getHeight(), true);
        stage.setViewport(fitViewport);

        screenViewport = new ScreenViewport();
        screenViewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        splashStage.setViewport(screenViewport);

        backgroundViewport = new ScreenViewport();
        backgroundViewport.update(AssetsLoader.getInstance().getViewPortSize().getWidth(), AssetsLoader.getInstance().getViewPortSize().getHeight(), true);
        backgroundStage.setViewport(backgroundViewport);

        flash = new Flash(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        flash.setOnDisappearListener(this);

        atlas = new TextureAtlas(Gdx.files.internal(AssetsLoader.getInstance().getImagePath() + "pop_the_camera.pack"));

        if (gameMode == Constants.GameMode.UNLIMITED) {
            ColorHelper.getInstance().initColor();
            currentBackgroundColor = ColorHelper.getInstance().getBackGroundColorUnlimitedMode(unlimitedScore);
        } else {
            ColorHelper.getInstance().initColor();
            currentBackgroundColor = ColorHelper.getInstance().getBackgroundColor(classicColorIndex);
        }
        background = new Background(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        backgroundStage.addActor(background);

        init();

        stage.addActor(cameraGroup.getActors());

        dot.initPosition();
        if (dot.getRotation() >= 0 && dot.getRotation() <= 180) {
            indicator.clockwise = false;
        } else if (dot.getRotation() > 180 && dot.getRotation() < 360) {
            indicator.clockwise = true;
        }

        touchPoint = new Vector3();

        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        checkOver();

        backgroundViewport.apply();
        background.setColor(currentBackgroundColor);
        backgroundStage.act();
        backgroundStage.draw();

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
        fitViewport.update(width, height, true);
        screenViewport.update(width, height, true);
        backgroundViewport.update(width, height, true);
    }

    private void init() {
        // Set up camera group. Camera group includes camera_button, background, lensGroup.
        cameraGroup = new ActorGroup();

        camera = new Camera(atlas.findRegion("camera"));
        cameraButton = new CameraButton(atlas.findRegion("camera_button"));
        cameraButton.setOnPressFinishListener(this);

        cameraGroup.setSize(camera.getWidth(), camera.getHeight());
        cameraGroup.setPosition(fitViewport.getWorldWidth() / 2 - cameraGroup.getWidth() / 2, fitViewport.getWorldHeight() / 2 - cameraGroup.getHeight() / 2);
        cameraGroup.setOrigin(cameraGroup.getWidth() / 2, cameraGroup.getHeight() / 2);

        cameraButton.setPosition(cameraButton.getWidth(), cameraGroup.getHeight() - cameraButton.getHeight() * 4f);

        initLens();
        lensGroup.setPosition(cameraGroup.getWidth() / 2 - lensGroup.getWidth() / 2, cameraGroup.getHeight() / 2 - lensGroup.getHeight() / 2 - (lens1.getHeight() / 2 - lens2.getHeight() / 2) / 3);

        cameraGroup.addActor(cameraButton);
        cameraGroup.addActor(camera);
        cameraGroup.addActor(lensGroup.getActors());
        cameraGroup.setOnShakeCompleteListener(this);

        indicator.setSpeed(gameMode.getSpeed());

        initButton();
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

        float dotRadius = lens3.getHeight() / 2 + dotOffset / 2 + dot.getHeight() / 2;
        float indicatorRadius = lens3.getHeight() / 2 + indicatorOffset / 2 + indicator.getHeight() / 3;
        indicatorBeta = Math.toDegrees(Math.acos((2 * indicatorRadius * indicatorRadius - indicator.getWidth() * indicator.getWidth()) / (2 * indicatorRadius * indicatorRadius)));
        dotBeta = Math.toDegrees(Math.acos((2 * dotRadius * dotRadius - dot.getWidth() * dot.getWidth()) / (2 * dotRadius * dotRadius)));

        // set color
        if (gameMode == Constants.GameMode.UNLIMITED) {
            ColorHelper.getInstance().setColorUnlimitedMode(unlimitedColorIndex, lens2, lens3, lens4);
        } else {
            ColorHelper.getInstance().setColor(classicColorIndex, lens2, lens3, lens4);
        }

        lensGroup.addActor(lens1);
        lensGroup.addActor(lens2);
        lensGroup.addActor(lens3);
        lensGroup.addActor(lens4);
        lensGroup.addActor(dot);
        lensGroup.addActor(indicator);

        e = indicatorBeta / 2;
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
            touchPoint.set(screenX, screenY, 0);
            screenViewport.unproject(touchPoint);
            Log.writeLog("Check touch point", "" + touchPoint.x + " " + touchPoint.y);
            Log.writeLog("Sound button coordinate", "" + soundButton.getX() + " " + soundButton.getY());
            if (touchPoint.x >= soundButton.getX() - touchOffset && touchPoint.x <= soundButton.getX() + touchOffset + soundButton.getWidth() && touchPoint.y >= soundButton.getY() - touchOffset && touchPoint.y <= soundButton.getY() + soundButton.getHeight() + touchOffset) {
                soundButton.touched = true;
                soundButton.setImage("off_sound");
            } else if (touchPoint.x >= vibrationButton.getX() - touchOffset && touchPoint.x <= vibrationButton.getX() + vibrationButton.getWidth() + touchOffset && touchPoint.y >= vibrationButton.getY() - touchOffset && touchPoint.y <= vibrationButton.getY() + vibrationButton.getHeight() + touchOffset) {
                vibrationButton.touched = true;
                vibrationButton.setImage("off_vibrate");
            } else if (touchPoint.x >= backButton.getX()- touchOffset && touchPoint.x <= backButton.getX() + backButton.getWidth()+ touchOffset && touchPoint.y >= backButton.getY()- touchOffset && touchPoint.y <= backButton.getY() + backButton.getHeight()+ touchOffset) {
                backButton.touched = true;
                backButton.setImage("btn_back_pressed");
            } else if (playAgain) {
                dot.fadeOut(1);
                indicator.fadeOut();
                updateTextView(2);
                checkable = false;
                backButton.setVisible(false);
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
                        if (indicationRotation <= 90 || indicationRotation >= 270) {
                            if (indicationRotation >= dot.getRotation()) {
                                delta = 360 - indicationRotation + dot.getRotation();
                            } else {
                                delta = 360 - dot.getRotation() + indicationRotation;
                            }
                        } else {
                            if (indicationRotation >= dot.getRotation()) {
                                delta = indicationRotation - dot.getRotation();
                            } else {
                                delta = -indicationRotation + dot.getRotation();
                            }
                        }
                    }
                    delta -= e;
                    if (delta <= dotBeta / 2) {
                        classicScore--;
                        updateTextView(2);
                        Log.writeLog("Check touch");
                        Log.writeLog("Indicator Rotation", "" + indicationRotation);
                        Log.writeLog("Dot Rotation", "" + dot.getRotation());
                        Log.writeLog("Delta 2", "" + delta);
                        Log.writeLog("Dot beta", "" + dotBeta / 2);
                        Log.writeLog("Indicator beta", "" + indicatorBeta / 8);
                        Log.writeLog("Ting ting.");
                        SoundHelper.getInstance().playSuccessSound();
                        if (classicScore != 0 || gameMode == Constants.GameMode.UNLIMITED) {
                            increaseUnlimitedSeedIfNeeded();
                            currentOrientation = indicator.clockwise;
                            dot.fadeOut(2);
                            if (indicator.clockwise) {
                                indicator.clockwise = false;
                            } else {
                                indicator.clockwise = true;
                            }
                            checkable = false;

                            if (gameMode == Constants.GameMode.UNLIMITED && unlimitedScore % UNLIMITED_COLOR_STEP == 0) {
                                unlimitedColorIndex++;
                                ColorHelper.getInstance().setColorUnlimitedMode(unlimitedColorIndex, lens2, lens3, lens4);
                                currentBackgroundColor = ColorHelper.getInstance().getBackGroundColorUnlimitedMode(unlimitedColorIndex);
                            }
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
        touchPoint.set(screenX, screenY, 0);
        screenViewport.unproject(touchPoint);
        if (soundButton.touched) {
            if (SoundHelper.enableSound) {
                soundButton.setImage("press_sound");
                SoundHelper.enableSound = false;
            } else {
                SoundHelper.enableSound = true;
                soundButton.setImage("on_sound");
                SoundHelper.getInstance().playSuccessSound();
            }
            saveData();
            soundButton.reset();
        } else if (vibrationButton.touched) {
            if (VibrationHelper.enableVibration) {
                vibrationButton.setImage("press_vibrate");
                VibrationHelper.enableVibration = false;
            } else {
                vibrationButton.setImage("on_vibrate");
                VibrationHelper.enableVibration = true;
                VibrationHelper.vibrate(1);
            }
            saveData();
            vibrationButton.reset();
        } else if (backButton.touched) {
            backButton.reset();
            backButton.setImage("btn_back");
            if (onGameListener != null) {
                onGameListener.onGameBackPressed();
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
                classicLevel++;
                saveData();
                cameraButton.press();
                break;
            case 2:
                playAgain = true;
                cameraGroup.shake();
                backButton.setVisible(true);
                SoundHelper.getInstance().playFailSound();
                touchable = false;
                VibrationHelper.vibrate(1);
                currentBackgroundColor = ColorHelper.FAIL_COLOR;
                if (this.onGameListener != null) {
                    onGameListener.onLossGame(this, gameMode, classicLevel, classicScore);
                }
                resetScoreAtUnlimitedModeIfNeeded();
                break;
        }
        classicScore = classicLevel;
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
                    if (indicationRotation <= 90 || indicationRotation >= 270) {
                        if (indicationRotation >= dot.getRotation()) {
                            delta = 360 - indicationRotation + dot.getRotation();
                        } else {
                            delta = 360 - dot.getRotation() + indicationRotation;
                        }
                    } else {
                        if (indicationRotation >= dot.getRotation()) {
                            delta = indicationRotation - dot.getRotation();
                        } else {
                            delta = -indicationRotation + dot.getRotation();
                        }
                    }
                }
                delta -= e;
                if (indicator.clockwise) {
                    if (isSameSide(indicationRotation, dot.getRotation())) {
                        Log.writeLog("Aloha1");
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
                        Log.writeLog("Aloha2");
                        if (dot.getRotation() >= 180 && dot.getRotation() <= 270) {
                            if (indicationRotation <= dot.getRotation()) {
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
                        } else if (dot.getRotation() >= 0 && dot.getRotation() <= 90) {
                            if (indicationRotation >= dot.getRotation()) {
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
                } else {
                    if (isSameSide(indicationRotation, dot.getRotation())) {
                        Log.writeLog("Aloha3");
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
                        Log.writeLog("Aloha4");
                        if (dot.getRotation() >= 270 && dot.getRotation() <= 360) {
                            if (indicationRotation <= dot.getRotation()) {
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
                        } else if (dot.getRotation() >= 90 && dot.getRotation() < 180) {
                            if (indicationRotation >= dot.getRotation()) {
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
        if (gameMode != Constants.GameMode.UNLIMITED) {
            flash.appear(splashStage);
            indicator.fadeOut();
            dot.fadeOut(1);
        }
    }

    @Override
    public void onDisappear(Flash flash) {
        touchable = true;
        flash.isAppear = false;
        flash.getParent().removeActor(flash);
        updateTextView(1);
        updateTextView(2);
        playAgain = false;
        if (gameMode != Constants.GameMode.UNLIMITED) {
            if (classicLevel % CLASSIC_COLOR_STEP == 0) {
                classicColorIndex++;
                currentBackgroundColor = ColorHelper.getInstance().getBackgroundColor(classicColorIndex);
                ColorHelper.getInstance().setColor(classicColorIndex, lens2, lens3, lens4);
            }
        }
    }

    private void initTextView() {
        level = new TextView(Gdx.files.internal(AssetsLoader.getInstance().getFontPath() + "merlo_level.fnt"), Gdx.files.internal(AssetsLoader.getInstance().getFontPath() + "merlo_level.png"));
        index = new TextView(Gdx.files.internal(AssetsLoader.getInstance().getFontPath() + "merlo_index.fnt"), Gdx.files.internal(AssetsLoader.getInstance().getFontPath() + "merlo_index.png"));
        classicType = new TextView(Gdx.files.internal(AssetsLoader.getInstance().getFontPath() + "merlo_mode.fnt"), Gdx.files.internal(AssetsLoader.getInstance().getFontPath() + "merlo_mode.png"));

        updateTextView(1);
        updateTextView(2);
        updateTextView(3);

        stage.addActor(level);
        stage.addActor(index);
        splashStage.addActor(classicType);

        level.setPosition(stage.getViewport().getWorldWidth() / 2 - level.getWidth() / 2, stage.getViewport().getWorldHeight() / 3.5f - level.getHeight());
        index.setPosition(stage.getViewport().getWorldWidth() / 2 - index.getWidth() / 2, 3 * stage.getViewport().getWorldHeight() / 4 + 1.5f * index.getHeight());
    }

    private void initButton() {

        float widthAspectRatio = fitViewport.getWorldWidth() / screenViewport.getWorldWidth();
        float heightAspectRatio = fitViewport.getWorldHeight() / screenViewport.getWorldHeight();
        float aspectRatio = (widthAspectRatio > heightAspectRatio) ? widthAspectRatio : heightAspectRatio;

        if (SoundHelper.enableSound) {
            soundButton = new Button(atlas, "on_sound");
        } else {
            soundButton = new Button(atlas, "press_sound");
        }
        soundButton.setSize(soundButton.getWidth() / aspectRatio, soundButton.getHeight() / aspectRatio);
        soundButton.setPosition(screenViewport.getWorldWidth() - soundButton.getWidth() * 1.25f, screenViewport.getWorldHeight() - soundButton.getHeight() * 1.25f);
        soundButton.addTo(splashStage);

        if (VibrationHelper.enableVibration) {
            vibrationButton = new Button(atlas, "on_vibrate");
        } else {
            vibrationButton = new Button(atlas, "press_vibrate");
        }
        vibrationButton.setSize(vibrationButton.getWidth() / aspectRatio, vibrationButton.getHeight() / aspectRatio);
        vibrationButton.setPosition(soundButton.getX() - 5 * vibrationButton.getWidth() / 4, soundButton.getY());
        vibrationButton.addTo(splashStage);

        backButton = new Button(atlas, "btn_back");
        backButton.setSize(backButton.getWidth() / aspectRatio, backButton.getHeight() / aspectRatio);
        backButton.setPosition(backButton.getWidth() / 4, backButton.getHeight() / 4);
        backButton.addTo(splashStage);
        backButton.setVisible(false);

        touchOffset = soundButton.getWidth() / 4f;
    }

    private void loadData() {
        preferences = Gdx.app.getPreferences(Constants.APP_TITLE);

        SoundHelper.enableSound = preferences.getBoolean("sound", true);
        VibrationHelper.enableVibration = preferences.getBoolean("vibration", true);
        if (gameMode == Constants.GameMode.UNLIMITED) {
            unlimitedBestScore = preferences.getInteger("unlimited_best_score", 0);
        } else {
            if (gameMode == Constants.GameMode.CLASSIC_SLOW) {
                classicLevel = preferences.getInteger("classic_slow", 1);
            } else if (gameMode == Constants.GameMode.CLASSIC_MEDIUM) {
                classicLevel = preferences.getInteger("classic_medium", 1);
            } else if (gameMode == Constants.GameMode.CLASSIC_FAST) {
                classicLevel = preferences.getInteger("classic_fast", 1);
            } else if (gameMode == Constants.GameMode.CLASSIC_CRAZY) {
                classicLevel = preferences.getInteger("classic_crazy", 1);
            }
            classicScore = classicLevel;
            classicColorIndex = classicLevel / CLASSIC_COLOR_STEP;
        }
    }

    private void saveData() {
        preferences = Gdx.app.getPreferences(Constants.APP_TITLE);
        preferences.putBoolean("sound", SoundHelper.enableSound);
        preferences.putBoolean("vibration", VibrationHelper.enableVibration);
        if (gameMode == Constants.GameMode.UNLIMITED) {
            preferences.putInteger("unlimited_best_score", unlimitedBestScore);
        } else {
            if (gameMode == Constants.GameMode.CLASSIC_SLOW) {
                preferences.putInteger("classic_slow", classicLevel);
            } else if (gameMode == Constants.GameMode.CLASSIC_MEDIUM) {
                preferences.putInteger("classic_medium", classicLevel);
            } else if (gameMode == Constants.GameMode.CLASSIC_FAST) {
                preferences.putInteger("classic_fast", classicLevel);
            } else if (gameMode == Constants.GameMode.CLASSIC_CRAZY) {
                preferences.putInteger("classic_crazy", classicLevel);
            }
        }
        preferences.flush();
    }

    @Override
    public void onFadeOutComplete(int type) {
        switch (type) {
            case 1:
                dot.initPosition();
                playAgain = false;
                break;
            case 2:
                dot.randomPosition(currentOrientation);
                break;
        }
        if (!flash.isAppear) {
            touchable = true;
        }
        dot.fadeIn(type);
    }

    @Override
    public void onFadeInComplete(int type) {
        if (type == 1) {
            indicator.resetAngle();
            if (gameMode == Constants.GameMode.UNLIMITED) {
                ColorHelper.getInstance().setColorUnlimitedMode(unlimitedScore, lens2, lens3, lens4);
                currentBackgroundColor = ColorHelper.getInstance().getBackGroundColorUnlimitedMode(0);
            } else {
                currentBackgroundColor = ColorHelper.getInstance().getBackgroundColor(classicColorIndex);
            }
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
            case 1://update Level
                if (gameMode == Constants.GameMode.UNLIMITED) {
                    if (unlimitedBestScore <= 999) {
                        level.setText("My Best: " + unlimitedBestScore);
                    } else {
                        level.setText("My Best: 999+");
                    }
                    level.setX(stage.getViewport().getWorldWidth() / 2 - level.getWidth() / 2);
                } else {
                    if (classicLevel <= 999) {
                        level.setText("Level: " + classicLevel);
                    } else {
                        level.setText("Level: 999+");
                    }
                    level.setX(stage.getViewport().getWorldWidth() / 2 - level.getWidth() / 2);
                }
                break;
            case 2://update Index
                if (gameMode == Constants.GameMode.UNLIMITED) {
                    index.setText("" + unlimitedScore);
                    index.setX(stage.getViewport().getWorldWidth() / 2 - index.getWidth() / 2);
                    if (unlimitedScore >= unlimitedBestScore) {
                        unlimitedBestScore = unlimitedScore;
                        updateTextView(1);
                    }
                } else {
                    index.setText("" + classicScore);
                    index.setX(stage.getViewport().getWorldWidth() / 2 - index.getWidth() / 2);
                }
                break;
            case 3:
                if (gameMode == Constants.GameMode.UNLIMITED) {
                    classicType.setText("No limit");
                } else {
                    if (gameMode == Constants.GameMode.CLASSIC_SLOW) {
                        classicType.setText("Slow");
                    } else if (gameMode == Constants.GameMode.CLASSIC_MEDIUM) {
                        classicType.setText("Medium");
                    } else if (gameMode == Constants.GameMode.CLASSIC_FAST) {
                        classicType.setText("Fast");
                    } else {
                        classicType.setText("Crazy");
                    }
                }
                classicType.setPosition(screenViewport.getWorldWidth() - (soundButton.getX() + soundButton.getWidth()), screenViewport.getWorldHeight() - classicType.getHeight() / 2);
                break;
        }
    }

    public void setOnGameListener(OnGameListener onGameListener) {
        this.onGameListener = onGameListener;
    }

    public void setGameMode(Constants.GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public Constants.GameMode getGameMode() {
        return gameMode;
    }

    private void resetScoreAtUnlimitedModeIfNeeded() {
        if (gameMode == Constants.GameMode.UNLIMITED) {
            saveData();
            unlimitedScore = 0;
            unlimitedColorIndex = 0;
            gameMode.resetUnlimitedSpeed();
            indicator.setSpeed(gameMode.getSpeed());
        }
    }

    private void increaseUnlimitedSeedIfNeeded() {
        if (gameMode == Constants.GameMode.UNLIMITED) {
            unlimitedScore++;
            updateTextView(2);
            if (unlimitedScore % Constants.GameMode.UNLIMITED_INCREASING_POINT == 0) {
                indicator.setSpeed(gameMode.getUnlimitedNewSpeed());
            }
        }
    }

    public static interface OnGameListener {
        public void onLossGame(GDXGameLauncher gameLauncher, Constants.GameMode gameMode, int currentLevel, int score);

        void onGameBackPressed();
    }
}
