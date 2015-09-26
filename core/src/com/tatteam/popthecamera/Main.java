package com.tatteam.popthecamera;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.tatteam.popthecamera.actors.AlphaRectangle;
import com.tatteam.popthecamera.actors.Background;
import com.tatteam.popthecamera.actors.CameraButton;
import com.tatteam.popthecamera.actors.Dot;
import com.tatteam.popthecamera.actors.Indicator;
import com.tatteam.popthecamera.actors.Lens;
import com.tatteam.popthecamera.actors.TextView;

import java.util.Random;

public class Main extends ApplicationAdapter implements InputProcessor, ActorGroup.OnShakeCompleteListener, CameraButton.OnPressFinishListener {

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
    private int number;
    private int currentIndex;
    private double indicatorBeta;
    private double dotBeta;
    private float radius;
    private Random random;
    private boolean playAgain = false;
    private boolean nextLevel = false;
    private TextView level;
    private TextView index;
    private AlphaRectangle rectangle;
    private Sound successSound;
    private Sound failSound;
    private Sound finishLevelSound;

    @Override
    public void create() {
        BaseLog.enableLog = true;

        random = new Random();

        rectangle = new AlphaRectangle();

        stage = new Stage(new FitViewport(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT));
        stage.getViewport().apply();

        atlas = new TextureAtlas(Gdx.files.internal("images/large/pop_the_camera.pack"));

        init();

        initDotRotation();

        stage.addActor(cameraGroup.getActors());
        number = currentIndex = 1;

        level = new TextView(Gdx.files.internal("fonts/liberation_serif.ttf"));
        index = new TextView(Gdx.files.internal("fonts/liberation_serif.ttf"));
        level.setText("" + number);
        index.setText("" + index);

        stage.addActor(level);
        stage.addActor(index);

        if (dot.getRotation() >= 0 && dot.getRotation() <= 180) {
            indicator.clockwise = false;
        } else if (dot.getRotation() > 180 && dot.getRotation() < 360) {
            indicator.clockwise = true;
        }

        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0f, 96f/255f, 100/255f, 1);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        checkOver();

        stage.act();
        stage.draw();
    }

    @Override
    public void dispose() {
        disposeSound();
        stage.dispose();
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

        initSound();
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
        BaseLog.checkRotation("Indicator offset", offset1);
        indicator.setPosition(lens1.getWidth() / 2 - indicator.getWidth() / 2, (lens1.getHeight() - lens2.getHeight()) / 2 + lens2.getHeight() - indicator.getHeight() - offset1 / 2);
        indicator.setOrigin(indicator.getWidth() / 2f, -(lens3.getHeight() / 2f) - offset1 / 2f);
        dot = new Dot(atlas.findRegion("dot"));
        float offset2 = (lens2.getHeight() - lens3.getHeight()) / 2 - dot.getHeight();
        BaseLog.checkRotation("Dot offset", offset2);
        dot.setPosition(lens1.getWidth() / 2 - dot.getWidth() / 2, (lens1.getHeight() - lens2.getHeight()) / 2 + lens2.getHeight() - dot.getHeight() - offset2 / 2);
        dot.setOrigin(dot.getWidth() / 2, -(lens3.getHeight() / 2) - offset2 / 2);

        lensGroup.addActor(lens1);
        lensGroup.addActor(lens2);
        lensGroup.addActor(lens3);
        lensGroup.addActor(lens4);
        lensGroup.addActor(dot);
        lensGroup.addActor(indicator);
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
        if (nextLevel || playAgain) {
            indicator.setRotation(0);
            initDotRotation();
            nextLevel = false;
            playAgain = false;
            indicator.resetAngle();
            if (dot.getRotation() >= 0 && dot.getRotation() <= 180) {
                indicator.clockwise = false;
            } else if (dot.getRotation() > 180 && dot.getRotation() < 360) {
                indicator.clockwise = true;
            }
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
                delta -= indicatorBeta / 6;
                if (delta <= dotBeta / 2) {
                    currentIndex--;
                    successSound.play(0.5f);
                    if (currentIndex != 0) {
                        dot.setRotation(randomRotation(dot.getRotation()));
                        if (indicator.clockwise) {
                            indicator.clockwise = false;
                        } else {
                            indicator.clockwise = true;
                        }
                    } else {
                        stopGame(1);
                    }
                } else {
                    stopGame(2);
                }
            }
        }
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

    }

    private void initDotRotation() {
        int type = random.nextInt(2);
        if (type == 0) {
            dot.setRotation(random.nextInt(60) + 30);
        } else {
            dot.setRotation(random.nextInt(60) + 270);
        }
    }

    private float randomRotation(float currentRotation) {
        float tmp;
        int rnd = random.nextInt(50) + 30;
        if (indicator.clockwise) {
            tmp = currentRotation + rnd;
        } else {
            tmp = currentRotation - rnd;
        }
        if (tmp < 0) {
            tmp += 360;
        } else if (tmp > 360) {
            tmp -= 360;
        }
        return tmp;
    }

    // situation: 1-win; 2-lose
    private void stopGame(int situation) {
        switch (situation) {
            case 1:
                number++;
                nextLevel = true;
                cameraButton.press();
                break;
            case 2:
                playAgain = true;
                cameraGroup.shake();
                failSound.play(1f);
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
            delta -= indicatorBeta / 6;

            if (indicator.clockwise) {
                if (isSameSide(indicationRotation, dot.getRotation())) {
                    if (indicationRotation < dot.getRotation()) {
                        if (delta > dotBeta / 2) {
                            BaseLog.printString("Check over 1");
                            BaseLog.checkRotation("Indicator Rotation", indicationRotation);
                            BaseLog.checkRotation("Dot Rotation", dot.getRotation());
                            BaseLog.checkRotation("Delta", delta);
                            BaseLog.checkRotation("Dot beta", dotBeta);
                            BaseLog.checkRotation("Indicator beta", indicatorBeta);
                            BaseLog.printString("Tach.");
                            stopGame(2);
                        }
                    }
                } else {
                    if (dot.getRotation() >= 0 && dot.getRotation() <= 180) {
                        if (delta > dotBeta / 2) {
                            BaseLog.printString("Check over 2");
                            BaseLog.checkRotation("Indicator Rotation", indicationRotation);
                            BaseLog.checkRotation("Dot Rotation", dot.getRotation());
                            BaseLog.checkRotation("Delta", delta);
                            BaseLog.checkRotation("Dot beta", dotBeta);
                            BaseLog.checkRotation("Indicator beta", indicatorBeta);
                            BaseLog.printString("Tach.");
                            stopGame(2);
                        }
                    }
                }
            } else {
                if (isSameSide(indicationRotation, dot.getRotation())) {
                    if (indicationRotation > dot.getRotation()) {
                        if (delta > dotBeta / 2) {
                            BaseLog.printString("Check over 3");
                            BaseLog.checkRotation("Indicator Rotation", indicationRotation);
                            BaseLog.checkRotation("Dot Rotation", dot.getRotation());
                            BaseLog.checkRotation("Delta", delta);
                            BaseLog.checkRotation("Dot beta", dotBeta);
                            BaseLog.checkRotation("Indicator beta", indicatorBeta);
                            BaseLog.printString("Tach.");
                            stopGame(2);
                        }
                    }
                } else {
                    if (dot.getRotation() >= 180 && dot.getRotation() <= 360) {
                        if (delta > dotBeta / 2) {
                            BaseLog.printString("Check over 4");
                            BaseLog.checkRotation("Indicator Rotation", indicationRotation);
                            BaseLog.checkRotation("Dot Rotation", dot.getRotation());
                            BaseLog.checkRotation("Delta", delta);
                            BaseLog.checkRotation("Dot beta", dotBeta);
                            BaseLog.checkRotation("Indicator beta", indicatorBeta);
                            BaseLog.printString("Tach.");
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

    private void initSound() {
        successSound = Gdx.audio.newSound(Gdx.files.internal("sounds/success.wav"));
        failSound = Gdx.audio.newSound(Gdx.files.internal("sounds/fail.wav"));
        finishLevelSound = Gdx.audio.newSound(Gdx.files.internal("sounds/finish_level.wav"));
    }

    private void disposeSound() {
        successSound.dispose();
        failSound.dispose();
        finishLevelSound.dispose();
    }

    @Override
    public void onPressFinish() {
        finishLevelSound.play(0.2f);
        rectangle.appear(stage);
    }
}
