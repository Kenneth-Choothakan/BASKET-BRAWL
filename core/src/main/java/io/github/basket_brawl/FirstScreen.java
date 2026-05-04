package io.github.basket_brawl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;

/** First screen of the application. Displayed after the application is created. */
public class FirstScreen implements Screen {
    private Player player;
    private Batch batch;
    private ShapeRenderer shapeRenderer;
    private static final float CHARGE_RATE = 1.5f;
    private static final float BAR_HOLD_TIME = 1.0f;
    private static final float BAR_WIDTH = 48f;
    private static final float BAR_HEIGHT = 220f;
    private static final float BAR_MARGIN = 32f;
    //                                                           Green           Yellow           Yellow 
    private static final float[][] shootingMargin2Pointer = {{0.45f,0.625f}, {0.35f, 0.45f}, {0.625f, 0.75f}};
    private static final float[][] shootingMargin3Pointer = {{0.675f,0.749f}, {0.575f, 0.675f}, {0.749f, 0.852f}};
    private final OrthographicCamera camera = new OrthographicCamera();
    private SpriteBatch spriteBatch;
    private Texture basketballTexture;
    private Texture hoopTexture;
    
    private float chargeAmount;
    private float timeSinceRelease = BAR_HOLD_TIME;
    private boolean wasHolding;
    private float transparency = 1f;
    private boolean canShoot = true;
    private final float distanceFromBasket = 45f;
    private float[][] shootingMargins;
    
    // Trajectory tracking variables
    private boolean isTrajectoryActive = false;
    private float trajectoryTime = 0f;
    private static final float TRAJECTORY_DURATION = 1.5f;
    private float ballStartX;
    private float ballStartY;
    private boolean shouldMakeShot = false;
    private boolean missHigh = false;
    private float shotPeakHeight = 200f;
    public FirstScreen() {
        // Create player at center of screen
        player = new Player(272, 172);
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
    }
    
    @Override
    public void show() {
        // Prepare your screen here.
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        
        // Load sprites
        spriteBatch = new SpriteBatch();
        basketballTexture = new Texture("BasketBall.png");
        hoopTexture = new Texture("BasketBallHoop.png");
    }

    @Override
    public void render(float delta) {
        ballStartX = player.getPlayerX();
        ballStartY = player.getPlayerY();
        shootListener(delta);
        // Clear screen with white background
        Gdx.gl.glClearColor(0.08f, 0.09f, 0.12f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        renderChargeBar(Gdx.input.isKeyPressed(Input.Keys.W), transparency);
        renderBasketballShot();
        // Handle WASD input
        Input input = Gdx.input;
        boolean left = input.isKeyPressed(Input.Keys.A);
        boolean right = input.isKeyPressed(Input.Keys.D);
        boolean shoot = input.isKeyJustPressed(Input.Keys.W);
        // Update player position
        player.update(delta, left, right);
        
        // Start shooting animation if W is pressed
        if (shoot) {
            player.startShooting();
        }
        
        // Draw player using sprite
        batch.begin();
        player.render(batch);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        // If the window is minimized on a desktop (LWJGL3) platform, width and height are 0, which causes problems.
        // In that case, we don't resize anything, and wait for the window to be a normal size before updating.
        if(width <= 0 || height <= 0) return;

        // Set up projection matrix for shape renderer (orthographic, origin bottom-left)
        Matrix4 projection = new Matrix4();
        projection.setToOrtho2D(0, 0, width, height);
        shapeRenderer.setProjectionMatrix(projection);
        batch.setProjectionMatrix(projection);
        
        // Update camera to match viewport
        camera.setToOrtho(false, width, height);
        camera.update();
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
    }

    @Override
    public void dispose() {
        // Destroy screen's assets here.
        shapeRenderer.dispose();
        if (spriteBatch != null) {
            spriteBatch.dispose();
        }
        if (basketballTexture != null) {
            basketballTexture.dispose();
        }
        if (hoopTexture != null) {
            hoopTexture.dispose();
        }
    }
    public String getShotResult(float[][] shootingMargins, float chargeAmount) {
        if (chargeAmount >= shootingMargins[0][0] && chargeAmount <= shootingMargins[0][1]) {
            return "Green";
        } 
        else if (chargeAmount >= shootingMargins[1][0] && chargeAmount <= shootingMargins[1][1]) {
            return "Yellow";
        } 
        else if (chargeAmount >= shootingMargins[2][0] && chargeAmount <= shootingMargins[2][1]) {
            return "Yellow";
        }
        else {
            return "Miss";
        }
    }

    private void renderChargeBar(boolean isHolding, float transparency) {
        if (timeSinceRelease >= BAR_HOLD_TIME && !isHolding) {
            return;
        }

        float barX = BAR_MARGIN;
        float barY = BAR_MARGIN;
        float fillHeight = BAR_HEIGHT * chargeAmount;

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(0.15f, 0.15f, 0.18f, transparency);
        shapeRenderer.rect(barX, barY, BAR_WIDTH, BAR_HEIGHT);

        shapeRenderer.setColor(1f, 1f, 1f, transparency);
        shapeRenderer.rect(barX, barY, BAR_WIDTH, fillHeight);

        shapeRenderer.setColor(0f, 0f, 0f, 1f);
        shapeRenderer.rectLine(barX, barY, barX + BAR_WIDTH, barY, transparency);
        shapeRenderer.rectLine(barX, barY + BAR_HEIGHT, barX + BAR_WIDTH, barY + BAR_HEIGHT, transparency);
        shapeRenderer.rectLine(barX, barY, barX, barY + BAR_HEIGHT, transparency);
        shapeRenderer.rectLine(barX + BAR_WIDTH, barY, barX + BAR_WIDTH, barY + BAR_HEIGHT, transparency);

        // 3 pointer
        if (distanceFromBasket > 10 && distanceFromBasket < 50) {
            shootingMargins = shootingMargin3Pointer;
            shapeRenderer.setColor(0.2f, 0.85f, 0.45f, transparency-0.5f);
            shapeRenderer.rect(barX, barY + 150f, BAR_WIDTH, 15f);
            shapeRenderer.setColor(1f, 1f, 0f, transparency-0.5f);
            shapeRenderer.rect(barX, barY + 165f, BAR_WIDTH, 25f);
            shapeRenderer.setColor(1f, 1f, 0f, transparency-0.5f);
            shapeRenderer.rect(barX, barY + 125f, BAR_WIDTH, 25f);
        }

        // 2 pointer
        if (distanceFromBasket > 0 && distanceFromBasket < 10) {
            shootingMargins = shootingMargin2Pointer;
            shapeRenderer.setColor(0.2f, 0.85f, 0.45f, transparency-0.5f);
            shapeRenderer.rect(barX, barY + 100f, BAR_WIDTH, 40f);
            shapeRenderer.setColor(1f, 1f, 0f, transparency-0.5f);
            shapeRenderer.rect(barX, barY + 140f, BAR_WIDTH, 25f);
            shapeRenderer.setColor(1f, 1f, 0f, transparency-0.5f);
            shapeRenderer.rect(barX, barY + 75f, BAR_WIDTH, 25f);
        }

        shapeRenderer.end();
    }

    private void renderBasketballShot() {
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        spriteBatch.setColor(1f, 1f, 1f, 1f);

        float hoopX = camera.viewportWidth - 500f;
        float hoopY = camera.viewportHeight / 2f - 300f;
        spriteBatch.draw(hoopTexture, hoopX + 500f, hoopY-20f, -500f, 500f);

        if (isTrajectoryActive) {
            float t = trajectoryTime / TRAJECTORY_DURATION;
            float targetHoopX = hoopX + 220f; // Center of hoop
            float ballX = ballStartX + (targetHoopX - ballStartX) * t;
            float targetY = hoopY + 250f;

            float ballY;
            if (shouldMakeShot) {
                ballY = ballStartY + (targetY - ballStartY) * t + MathUtils.sin(t * MathUtils.PI) * shotPeakHeight;
            } else if (missHigh) {
                ballY = ballStartY + (targetY + 150f - ballStartY) * t + MathUtils.sin(t * MathUtils.PI) * (shotPeakHeight + 80f);
            } else {
                ballY = ballStartY + (targetY - 150f - ballStartY) * t + MathUtils.sin(t * MathUtils.PI) * (shotPeakHeight + 40f);
            }

            float ballSize = 50f;
            spriteBatch.draw(basketballTexture, ballX - ballSize / 2f, ballY - ballSize / 2f, ballSize, ballSize);
        }

        spriteBatch.end();
    }

    private void shootListener(float delta) {
        boolean isHolding = Gdx.input.isKeyPressed(Input.Keys.W);

        if (isHolding && canShoot == true) {
            chargeAmount = MathUtils.clamp(chargeAmount + delta * CHARGE_RATE, 0f, 1f);
            timeSinceRelease = 0f;
            transparency = 1f;
        } else if (wasHolding && canShoot == true) {
            timeSinceRelease = 0f;
            transparency = 1f;
            String shotResult = getShotResult(shootingMargins, chargeAmount);
            System.out.println(shotResult);

            // Start trajectory animation
            isTrajectoryActive = true;
            trajectoryTime = 0f;
            ballStartX = 50f; // Starting position on left side
            ballStartY = camera.viewportHeight / 2f + 50f;

            // Determine shot outcome once on release so the trajectory stays stable.
            boolean isGreenShot = shotResult.equals("Green");
            boolean isYellowShot = shotResult.equals("Yellow");

            if (isGreenShot) {
                shouldMakeShot = true;
                missHigh = false;
            } else if (isYellowShot) {
                shouldMakeShot = MathUtils.random() < 0.5f;
                missHigh = chargeAmount >= shootingMargins[2][0];
            } else {
                shouldMakeShot = false;
                if (chargeAmount > shootingMargins[2][1]) {
                    missHigh = true;
                } else if (chargeAmount < shootingMargins[1][0]) {
                    missHigh = false;
                } else {
                    missHigh = MathUtils.random() < 0.5f;
                }
            }
            shotPeakHeight = MathUtils.random(400f, 600f);
        } else if (timeSinceRelease < BAR_HOLD_TIME) {
            timeSinceRelease += delta;
            transparency = MathUtils.clamp(transparency - delta, 0f, 1f);
            canShoot = false;
        }
        if (timeSinceRelease > BAR_HOLD_TIME) {
            chargeAmount = 0f;
            transparency = 1f;
            canShoot = true;
        }

        wasHolding = isHolding;

        if (isTrajectoryActive) {
            trajectoryTime += delta;
            if (trajectoryTime >= TRAJECTORY_DURATION) {
                isTrajectoryActive = false;
                trajectoryTime = 0f;
            }
        }
    }
}