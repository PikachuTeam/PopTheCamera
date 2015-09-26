package com.tatteam.popthecamera;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.tatteam.popthecamera.actors.Background;
import com.tatteam.popthecamera.actors.CameraButton;
import com.tatteam.popthecamera.actors.Dot;
import com.tatteam.popthecamera.actors.Indicator;
import com.tatteam.popthecamera.actors.Lens;
import com.tatteam.popthecamera.actors.TextView;

import java.util.Random;

public class Main extends ApplicationAdapter implements InputProcessor, ActorGroup.OnShakeCompleteListener {

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

    @Override
    public void create() {
        BaseLog.enableLog = true;

        random = new Random();

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
        Gdx.gl.glClearColor(0.5f, 0.9f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        checkOver();

        stage.act();
        stage.draw();
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }

    private void init() {
        // Set up camera group. Camera group includes camera_button, background, lens1, lensGroup.
        cameraGroup = new ActorGroup();

        background = new Background(atlas.findRegion("camera"));
        cameraButton = new CameraButton(atlas.findRegion("camera_button"));
        lens1 = new Lens(atlas.findRegion("lens1"));
        initLens();

        cameraGroup.setSize(background.getWidth(), background.getHeight());
        cameraGroup.setPosition(Constants.VIEWPORT_WIDTH / 2 - cameraGroup.getWidth() / 2, Constants.VIEWPORT_HEIGHT / 2 - cameraGroup.getHeight() / 2);
        cameraGroup.setOrigin(cameraGroup.getWidth() / 2, cameraGroup.getHeight() / 2);

        lens1.setCenterPosition(cameraGroup.getWidth(), cameraGroup.getHeight());
        cameraButton.setPosition(cameraButton.getWidth(), cameraGroup.getHeight() - 110f);
        lensGroup.setPosition(cameraGroup.getWidth() / 2 - lensGroup.getWidth() / 2, cameraGroup.getHeight() / 2 - lensGroup.getHeight() / 2);

        cameraGroup.addActor(cameraButton);
        cameraGroup.addActor(background);
        cameraGroup.addActor(lens1);
        cameraGroup.addActor(lensGroup.getActors());

        radius = lens3.getHeight() / 2;

        indicatorBeta = Math.toDegrees(Math.acos((2 * radius * radius - indicator.getWidth() * indicator.getWidth()) / (2 * radius * radius)));
        dotBeta = Math.toDegrees(Math.acos((2 * radius * radius - dot.getWidth() * dot.getWidth()) / (2 * radius * radius)));

        cameraGroup.setOnShakeCompleteListener(this);
    }

    // Set up lens group
    // Lens group includes indicator, dot, lens2, lens 3, lens4.
    private void initLens() {
        lensGroup = new ActorGroup();

        lens2 = new Lens(atlas.findRegion("lens2"));
        lens3 = new Lens(atlas.findRegion("lens3"));
        lens4 = new Lens(atlas.findRegion("lens4"));

        lensGroup.setSize(lens2.getWidth(), lens2.getHeight());
        lensGroup.setPosition(cameraGroup.getWidth() / 2 - lensGroup.getWidth() / 2, cameraGroup.getHeight() / 2 - lensGroup.getHeight() / 2);

        lens2.setCenterPosition(lensGroup.getWidth(), lensGroup.getHeight());
        lens3.setCenterPosition(lensGroup.getWidth(), lensGroup.getHeight());
        lens4.setCenterPosition(lensGroup.getWidth(), lensGroup.getHeight());

        indicator = new Indicator(atlas.findRegion("indicator"));
        indicator.setPosition(lens2.getWidth() / 2 - indicator.getWidth() / 2, lens2.getHeight() - indicator.getHeight());
        indicator.setCenterOrigin(lens3.getHeight() / 2);
        dot = new Dot(atlas.findRegion("dot"));
        dot.setPosition(lens2.getWidth() / 2 - dot.getWidth() / 2, lens2.getHeight() - dot.getHeight() - 10f);
        dot.setCenterOrigin(lens3.getHeight() / 2);

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
                double deltaIndicatorRotation;
                double bound1 = dot.getRotation() - dotBeta / 2;
                double bound2 = dot.getRotation() + dotBeta / 2;
//                if (bound1 < 0) {
//                    double tmp = bound2;
//                    bound2 = bound1 + 360;
//                    bound1 = tmp;
//                }
//                if (bound2 >= 360) {
//                    double tmp = bound1;
//                    bound1 = bound2 - 360;
//                    bound2 = tmp;
//                }

                deltaIndicatorRotation = recalculateAngleIfNeed(indicator.getRotation());
//                if (indicator.getRotation() < 0) {
//                    deltaIndicatorRotation = 360 + indicator.getRotation();
//                } else {
//                    deltaIndicatorRotation = indicator.getRotation();
//                }

                if (deltaIndicatorRotation < dot.getRotation()) {
                    deltaIndicatorRotation += indicatorBeta / 2;
                } else {
                    deltaIndicatorRotation -= indicatorBeta / 2;
                }

                deltaIndicatorRotation = recalculateAngleIfNeed(deltaIndicatorRotation);

                BaseLog.printString("Touch.");
                BaseLog.checkRotation("Indicator Rotation", indicator.getRotation());
                BaseLog.checkRotation("Dot Rotation", dot.getRotation());
                BaseLog.checkRotation("Delta Indicator Rotation", deltaIndicatorRotation);
                BaseLog.checkRotation("Bound 1", bound1);
                BaseLog.checkRotation("Bound 2", bound2);

                if (deltaIndicatorRotation >= bound1 && deltaIndicatorRotation <= bound2) {
                    currentIndex--;
                    if (currentIndex != 0) {
                        SoundHelper.getInstance().makeSound(Gdx.files.internal("sounds/bleep.mp3"));
                        SoundHelper.getInstance().playSound();
                        dot.setRotation(randomRotation(dot.getRotation()));
                        if (indicator.clockwise) {
                            indicator.clockwise = false;
                        } else {
                            indicator.clockwise = true;
                        }
                    } else {
                        stopGame(1);
                    }
                    BaseLog.tingTing();
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
        int rnd = random.nextInt(30) + 50;
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
                AlphaAction alphaAction = new AlphaAction();
                alphaAction.setAlpha(2f);
                alphaAction.setDuration(0.5f);
                stage.addAction(alphaAction);
                cameraButton.press();
                break;
            case 2:
                playAgain = true;
                SoundHelper.getInstance().makeSound(Gdx.files.internal("sounds/fail.wav"));
                SoundHelper.getInstance().playSound();
                cameraGroup.shake();
                break;
        }
        currentIndex = number;
        indicator.isMoving = false;
    }

    private void checkOver() {
        if (indicator.isMoving) {
            double deltaIndicatorRotation1;
            double deltaIndicatorRotation2;
            double bound1 = dot.getRotation() - dotBeta / 2;
            double bound2 = dot.getRotation() + dotBeta / 2;
            boolean isSwap = false;

            if (bound1 < 0) {
                double tmp = bound2;
                bound2 = bound1 + 360;
                bound1 = tmp;
                isSwap = true;
            }
            if (bound2 >= 360) {
                double tmp = bound1;
                bound1 = bound2 - 360;
                bound2 = tmp;
                isSwap = true;
            }

            deltaIndicatorRotation1 = recalculateAngleIfNeed(indicator.getRotation());
            deltaIndicatorRotation2 = recalculateAngleIfNeed(indicator.getRotation());

            if (indicator.clockwise) {
                deltaIndicatorRotation1 -= indicatorBeta / 2;
                deltaIndicatorRotation1 = recalculateAngleIfNeed(deltaIndicatorRotation1);
//                if (isSameSide(deltaIndicatorRotation1, dot.getRotation())) {

//                if (isSwap) {
//                    if (bound2 - deltaIndicatorRotation1 > 0) {
//                        BaseLog.printString("Check over 1.");
//                        BaseLog.checkRotation("Indicator Rotation", indicator.getRotation());
//                        BaseLog.checkRotation("Dot Rotation", dot.getRotation());
//                        BaseLog.checkRotation("Delta Indicator Rotation 1", deltaIndicatorRotation1);
//                        BaseLog.checkRotation("Delta Indicator Rotation 2", deltaIndicatorRotation2);
//                        BaseLog.checkRotation("Bound 1", bound1);
//                        BaseLog.checkRotation("Bound 2", bound2);
//                        BaseLog.checkSwap(isSwap);
//                        if (deltaIndicatorRotation2 + dot.getRotation() > 360) {
//                            if (deltaIndicatorRotation2 < bound2) {
//                                stopGame(2);
//                            }
//                        }
//                    }
//                } else {
//                    if (isSameSide(deltaIndicatorRotation1, dot.getRotation())) {
//                        if (deltaIndicatorRotation1 < dot.getRotation()) {
//                            deltaIndicatorRotation2 += indicatorBeta / 2;
////                    deltaIndicatorRotation2 = recalculateAngleIfNeed(deltaIndicatorRotation2);
//                            BaseLog.printString("Check over 1.");
//                            BaseLog.checkRotation("Indicator Rotation", indicator.getRotation());
//                            BaseLog.checkRotation("Dot Rotation", dot.getRotation());
//                            BaseLog.checkRotation("Delta Indicator Rotation 1", deltaIndicatorRotation1);
//                            BaseLog.checkRotation("Delta Indicator Rotation 2", deltaIndicatorRotation2);
//                            BaseLog.checkRotation("Bound 1", bound1);
//                            BaseLog.checkRotation("Bound 2", bound2);
//                            BaseLog.checkSwap(isSwap);
////                        if (isSwap) {
////                            if (deltaIndicatorRotation2 + dot.getRotation() > 360) {
////                                if (deltaIndicatorRotation2 < bound2) {
////                                    stopGame(2);
////                                }
////                            }
////                        } else {
//                            if (deltaIndicatorRotation2 < bound1) {
//                                stopGame(2);
//                            }
////                        }
////                    }
//                        }
//                    }
//                }

            } else {
                deltaIndicatorRotation1 += indicatorBeta / 2;
                deltaIndicatorRotation1 = recalculateAngleIfNeed(deltaIndicatorRotation1);
                if (Math.abs(indicator.getRotation() - dot.getRotation()) < indicatorBeta + dotBeta / 2) {

                }
//                if (isSameSide(deltaIndicatorRotation1, dot.getRotation()))
//                    if (Math.abs(deltaIndicatorRotation1 - dot.getRotation()) > dotBeta / 2) {
////                    if (deltaIndicatorRotation1 > dot.getRotation()) {
//                        deltaIndicatorRotation2 -= indicatorBeta / 2;
//                        BaseLog.printString("Check over 2.");
//                        BaseLog.checkRotation("Indicator Rotation", indicator.getRotation());
//                        BaseLog.checkRotation("Dot Rotation", dot.getRotation());
//                        BaseLog.checkRotation("Delta Indicator Rotation 1", deltaIndicatorRotation1);
//                        BaseLog.checkRotation("Delta Indicator Rotation 2", deltaIndicatorRotation2);
//                        BaseLog.checkRotation("Bound 1", bound1);
//                        BaseLog.checkRotation("Bound 2", bound2);
//                        if (isSameSide(deltaIndicatorRotation2, bound2)) {
//                            if (deltaIndicatorRotation2 > bound2) {
//                                stopGame(2);
//                            }
//                        } else {
//                            if (deltaIndicatorRotation1 > bound2) {
//                                stopGame(2);
//                            }
//                        }
//                    }
            }
        }
    }

    private boolean isSameSide(double angle1, double angle2) {
        return ((angle1 >= 0 && angle2 >= 0 && angle1 <= 180 && angle2 <= 180) || (angle1 > 180 && angle2 > 180 && angle1 < 360 && angle2 < 360));
    }

    private boolean isDifferentSide(double angle1, float angle2) {
        return ((angle1 > 180 && angle2 >= 0 && angle1 < 360 && angle2 <= 180) || (angle1 >= 0 && angle2 > 180 && angle1 <= 180 && angle2 < 360));
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
}
