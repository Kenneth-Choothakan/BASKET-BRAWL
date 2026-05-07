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
    private final Player player;
    private final Player player2;
    private final Batch batch;
    private final ShapeRenderer shapeRenderer;
    private boolean player1HasBall = true;
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
    private Texture hoopTextureLeft;
    
    // Player 1 shot variables
    private float chargeAmount;
    private float timeSinceRelease = BAR_HOLD_TIME;
    private boolean wasHolding;
    private float transparency = 1f;
    private boolean canShoot = true;
    private float[][] shootingMargins;
    
    // Player 2 shot variables
    private float chargeAmount2;
    private float timeSinceRelease2 = BAR_HOLD_TIME;
    private boolean wasHolding2;
    private float transparency2 = 1f;
    private boolean canShoot2 = true;
    private float[][] shootingMargins2;
    
    // Trajectory tracking variables for Player 1
    private boolean isTrajectoryActive = false;
    private float trajectoryTime = 0f;
    private static final float TRAJECTORY_DURATION = 1.5f;
    private float ballStartX;
    private float ballStartY;
    private boolean shouldMakeShot = false;
    private boolean missHigh = false;
    private float shotPeakHeight = 200f;
    private boolean madeShotFalling = false;
    private float madeShotFallTime = 0f;
    private float madeShotFallX;
    private float madeShotFallY;
    private static final float MADE_SHOT_FALL_DURATION = 1f;
    
    // Trajectory tracking variables for Player 2
    private boolean isTrajectoryActive2 = false;
    private float trajectoryTime2 = 0f;
    private float ballStartX2;
    private float ballStartY2;
    private boolean shouldMakeShot2 = false;
    private boolean missHigh2 = false;
    private float shotPeakHeight2 = 200f;
    private boolean madeShotFalling2 = false;
    private float madeShotFallTime2 = 0f;
    private float madeShotFallX2;
    private float madeShotFallY2;
    private static final float CATCH_HORIZONTAL_PADDING = 30f;
    private static final float CATCH_VERTICAL_PADDING = 40f;
    private int pendingPossession = 0; // 0 = none, 1 = to player1, 2 = to player2
    private boolean cancelShotPending1 = false;
    private boolean cancelShotPending2 = false;

    public FirstScreen() {
        // Create player 1 at left-center of screen
        player = new Player(100, 172);
        // Create player 2 at right-center of screen
        player2 = new Player(444, 172, true);
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        syncPlayerModes();
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
        hoopTextureLeft = new Texture("BasketBallHoop.png");
    }

    @Override
    public void render(float delta) {
        syncPlayerModes();
        // Update ball start positions for both players
        ballStartX = player.getPlayerX() + 60f;
        ballStartY = player.getPlayerY() + 40f;
        ballStartX2 = player2.getPlayerX() + 60f;
        ballStartY2 = player2.getPlayerY() + 40f;
        
        if (player1HasBall) {
            shootListener(delta);
        } else {
            shootListener2(delta);
        }
        
        // Clear screen with white background
        Gdx.gl.glClearColor(0.08f, 0.09f, 0.12f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        
        Input input = Gdx.input;
        renderChargeBar(player1HasBall && input.isKeyPressed(Input.Keys.W) && !cancelShotPending1, transparency, true);
        renderChargeBar(!player1HasBall && input.isKeyPressed(Input.Keys.UP) && !cancelShotPending2, transparency2, false);
        renderBasketballShot(delta);
        
        // Handle Player inputs with dynamic bindings depending on possession/mode
        boolean left1 = input.isKeyPressed(Input.Keys.A);
        boolean right1 = input.isKeyPressed(Input.Keys.D);
        boolean left2 = input.isKeyPressed(Input.Keys.LEFT);
        boolean right2 = input.isKeyPressed(Input.Keys.RIGHT);

        // Shooting hold keys: player1 -> W, player2 -> UP when they have the ball
        boolean shootingHeld1 = input.isKeyPressed(Input.Keys.W);
        boolean shootingHeld2 = input.isKeyPressed(Input.Keys.SPACE);
        if (!player1HasBall) shootingHeld2 = input.isKeyPressed(Input.Keys.UP);

        boolean shoot1 = player1HasBall && input.isKeyJustPressed(Input.Keys.W);
        boolean shoot2 = !player1HasBall && input.isKeyJustPressed(Input.Keys.UP);

        // Block/jump: bound to defender's key (W if player1 is defender, UP if player2 is defender)
        Player offense = getOffensePlayer();
        Player defense = getDefensePlayer();
        if (defense == player) {
            if (input.isKeyPressed(Input.Keys.W)) {
                player.startBlockJumpToward(offense.getPlayerX());
            }
        } else {
            if (input.isKeyPressed(Input.Keys.UP)) {
                player2.startBlockJumpToward(offense.getPlayerX());
            }
        }

        // Update both players; shootingHeld only applies to the player who has the ball
        player.update(delta, left1, right1, player1HasBall && shootingHeld1);
        player2.update(delta, left2, right2, !player1HasBall && shootingHeld2);

        // Apply pending possession switches only after the defender has landed
        if (pendingPossession == 2 && !player2.isBlockJumpActive()) {
            switchPossessionToPlayer2();
            pendingPossession = 0;
        } else if (pendingPossession == 1 && !player.isBlockJumpActive()) {
            switchPossessionToPlayer1();
            pendingPossession = 0;
        }

        // Start shooting animation for the player who has the ball
        if (shoot1) player.startShooting();
        if (shoot2) player2.startShooting();
        
        // Draw both players using sprite
        batch.begin();
        player.render(batch, false);
        player2.render(batch, true);
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
        if (hoopTextureLeft != null) {
            hoopTextureLeft.dispose();
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

    private void renderChargeBar(boolean isHolding, float transparency, boolean isPlayer1) {
        float chargeAmt = isPlayer1 ? this.chargeAmount : this.chargeAmount2;
        float timeSinceRel = isPlayer1 ? this.timeSinceRelease : this.timeSinceRelease2;
        if (timeSinceRel >= BAR_HOLD_TIME && !isHolding) {
            return;
        }

        float barX = isPlayer1 ? player.getPlayerX() + 40f : player2.getPlayerX() + 40f;
        float barY = BAR_MARGIN+200f;
        float fillHeight = BAR_HEIGHT * chargeAmt;

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

        Player playerRef = isPlayer1 ? player : player2;
        
        // 3 pointer
        if (playerRef.getPlayerX() < 600f) {
            shapeRenderer.setColor(0.2f, 0.85f, 0.45f, transparency-0.5f);
            shapeRenderer.rect(barX, barY + 150f, BAR_WIDTH, 15f);
            shapeRenderer.setColor(1f, 1f, 0f, transparency-0.5f);
            shapeRenderer.rect(barX, barY + 165f, BAR_WIDTH, 25f);
            shapeRenderer.setColor(1f, 1f, 0f, transparency-0.5f);
            shapeRenderer.rect(barX, barY + 125f, BAR_WIDTH, 25f);
        }

        // 2 pointer
        if (playerRef.getPlayerX() > 600f) {
            shapeRenderer.setColor(0.2f, 0.85f, 0.45f, transparency-0.5f);
            shapeRenderer.rect(barX, barY + 100f, BAR_WIDTH, 40f);
            shapeRenderer.setColor(1f, 1f, 0f, transparency-0.5f);
            shapeRenderer.rect(barX, barY + 140f, BAR_WIDTH, 25f);
            shapeRenderer.setColor(1f, 1f, 0f, transparency-0.5f);
            shapeRenderer.rect(barX, barY + 75f, BAR_WIDTH, 25f);
        }

        shapeRenderer.end();
    }

    private void renderBasketballShot(float delta) {
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        spriteBatch.setColor(1f, 1f, 1f, 1f);

        // Draw both hoops
        float hoopXRight = camera.viewportWidth - 500f;
        float hoopY = camera.viewportHeight / 2f - 300f;
        spriteBatch.draw(hoopTexture, hoopXRight + 650f, hoopY-20f, -500f, 500f);

        float hoopXLeft = -150;
        spriteBatch.draw(hoopTextureLeft, hoopXLeft, hoopY-20f, 500f, 500f);

        // If possession is pending from a mid-air catch, attach the ball to the pending defender
        if (pendingPossession == 2) {
            float handX = player2.getPlayerX() + 60f;
            float handY = player2.getPlayerY() + 140f;
            float ballSize = 50f;
            spriteBatch.draw(basketballTexture, handX - ballSize / 2f, handY - ballSize / 2f, ballSize, ballSize);
            spriteBatch.end();
            return;
        } else if (pendingPossession == 1) {
            float handX = player.getPlayerX() + 60f;
            float handY = player.getPlayerY() + 140f;
            float ballSize = 50f;
            spriteBatch.draw(basketballTexture, handX - ballSize / 2f, handY - ballSize / 2f, ballSize, ballSize);
            spriteBatch.end();
            return;
        }

        float ballSize = 50f;

        // Player 1 trajectory (shoots at right hoop)
        if (isTrajectoryActive) {
            float t = trajectoryTime / TRAJECTORY_DURATION;
            float targetHoopX = hoopXRight + 300f; // Center of hoop
            if (missHigh && !shouldMakeShot) {
                targetHoopX += 600f;
            }
            float ballX = ballStartX + (targetHoopX - ballStartX) * t;
            float targetY = hoopY + 250f;

            float ballY;
            if (shouldMakeShot) {
                ballY = ballStartY + (targetY - ballStartY) * t + MathUtils.sin(t * MathUtils.PI) * shotPeakHeight;
            } else if (missHigh) {
                ballY = ballStartY + (targetY + 150f - ballStartY) * t + MathUtils.sin(t * MathUtils.PI) * (shotPeakHeight + 80f);
            } else {
                ballY = ballStartY + (targetY - 200f - ballStartY) * t + MathUtils.sin(t * MathUtils.PI) * (shotPeakHeight + 40f);
            }

            spriteBatch.draw(basketballTexture, ballX - ballSize / 2f, ballY - ballSize / 2f, ballSize, ballSize);
        } else if (madeShotFalling && madeShotFallTime < MADE_SHOT_FALL_DURATION) {
            float fallProgress = MathUtils.clamp(madeShotFallTime / MADE_SHOT_FALL_DURATION, 0f, 1f);
            float floorY = 0f;
            float ballY = madeShotFallY - (madeShotFallY - floorY) * (fallProgress * fallProgress);
            spriteBatch.draw(basketballTexture, madeShotFallX - ballSize / 2f, ballY - ballSize / 2f, ballSize, ballSize);
        }

        // Player 2 trajectory (shoots at left hoop)
        if (isTrajectoryActive2) {
            float t = trajectoryTime2 / TRAJECTORY_DURATION;
            float targetHoopX = hoopXLeft + 300f; // Center of left hoop
            if (missHigh2 && !shouldMakeShot2) {
                targetHoopX -= 600f;
            }
            float ballX = ballStartX2 + (targetHoopX - ballStartX2) * t;
            float targetY = hoopY + 250f;
            System.out.println("ball x: " + ballX);
            float ballY;
            if (shouldMakeShot2) {
                ballY = ballStartY2 + (targetY - ballStartY2) * t + MathUtils.sin(t * MathUtils.PI) * shotPeakHeight2;
            } else if (missHigh2) {
                ballY = ballStartY2 + (targetY + 150f - ballStartY2) * t + MathUtils.sin(t * MathUtils.PI) * (shotPeakHeight2 + 80f);
            } else {
                ballY = ballStartY2 + (targetY - 200f - ballStartY2) * t + MathUtils.sin(t * MathUtils.PI) * (shotPeakHeight2 + 40f);
            }

            spriteBatch.draw(basketballTexture, ballX - ballSize / 2f, ballY - ballSize / 2f, ballSize, ballSize);
        } else if (madeShotFalling2 && madeShotFallTime2 < MADE_SHOT_FALL_DURATION) {
            float fallProgress = MathUtils.clamp(madeShotFallTime2 / MADE_SHOT_FALL_DURATION, 0f, 1f);
            float floorY = 0f;
            float ballY = madeShotFallY2 - (madeShotFallY2 - floorY) * (fallProgress * fallProgress);
            spriteBatch.draw(basketballTexture, madeShotFallX2 - ballSize / 2f, ballY - ballSize / 2f, ballSize, ballSize);
        }

        spriteBatch.end();
    }

    private void shootListener(float delta) {
        if (!player1HasBall) {
            return;
        }

        boolean isHolding = Gdx.input.isKeyPressed(Input.Keys.W);
        boolean cancelPressed = Gdx.input.isKeyJustPressed(Input.Keys.S);

        // Cancel shot if S is pressed while holding W
        if (isHolding && cancelPressed) {
            chargeAmount = 0f;
            wasHolding = false;
            timeSinceRelease = BAR_HOLD_TIME;
            transparency = 1f;
            canShoot = true;
            cancelShotPending1 = true;
            player.cancelShooting();
            return;
        }

        // While cancel is pending, require the player to release W before allowing any new charge
        if (cancelShotPending1) {
            if (isHolding) {
                // still holding W: keep pending and don't progress charge
                wasHolding = false;
                return;
            } else {
                // W released: clear the pending cancel and allow charging again
                cancelShotPending1 = false;
            }
        }

        if (isHolding && canShoot == true) {
            chargeAmount = MathUtils.clamp(chargeAmount + delta * CHARGE_RATE, 0f, 1f);
            timeSinceRelease = 0f;
            transparency = 1f;
        } else if (wasHolding && canShoot == true) {
            timeSinceRelease = 0f;
            transparency = 1f;
            madeShotFalling = false;
            madeShotFallTime = 0f;
            
            // Determine shooting margins for player 1
            if (player.getPlayerX() < 600f) {
                shootingMargins = shootingMargin3Pointer;
            } else {
                shootingMargins = shootingMargin2Pointer;
            }
            
            String shotResult = getShotResult(shootingMargins, chargeAmount);
            System.out.println("Player 1: " + shotResult);

            // Start trajectory animation
            isTrajectoryActive = true;
            trajectoryTime = 0f;
            ballStartX = player.getPlayerX() + 60f;
            ballStartY = player.getPlayerY() + 40f;

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
            float t = MathUtils.clamp(trajectoryTime / TRAJECTORY_DURATION, 0f, 1f);
            float targetHoopX = camera.viewportWidth - 500f + 150f;
            if (missHigh && !shouldMakeShot) {
                targetHoopX += 600f;
            }
            float targetY = camera.viewportHeight / 2f - 300f + 250f;
            float ballX = ballStartX + (targetHoopX - ballStartX) * t;
            float ballY;
            if (shouldMakeShot) {
                ballY = ballStartY + (targetY - ballStartY) * t + MathUtils.sin(t * MathUtils.PI) * shotPeakHeight;
            } else if (missHigh) {
                ballY = ballStartY + (targetY + 150f - ballStartY) * t + MathUtils.sin(t * MathUtils.PI) * (shotPeakHeight + 80f);
            } else {
                ballY = ballStartY + (targetY - 200f - ballStartY) * t + MathUtils.sin(t * MathUtils.PI) * (shotPeakHeight + 40f);
            }

            if (didDefenderCatchBall(player2, ballX, ballY)) {
                // Defender caught the ball mid-air — defer switching until they land
                System.out.println("player x: " + player2.getPlayerX());
            System.out.println("Ball x: " + ballX);
                pendingPossession = 2;
                isTrajectoryActive = false;
                trajectoryTime = 0f;
                return;
            }

            if (trajectoryTime >= TRAJECTORY_DURATION) {
                isTrajectoryActive = false;
                trajectoryTime = 0f;
                if (shouldMakeShot) {
                    madeShotFalling = true;
                    madeShotFallTime = 0f;
                    madeShotFallX = camera.viewportWidth - 500f + 300f;
                    madeShotFallY = camera.viewportHeight / 2f - 300f + 250f;
                } else {
                    restartAfterMissedShot();
                }
            }
        }
        if (madeShotFalling) {
            madeShotFallTime += delta;
            if (madeShotFallTime >= MADE_SHOT_FALL_DURATION) {
                madeShotFalling = false;
                madeShotFallTime = 0f;
                    // After shot falls (score), restart with swapped possession
                    restartAfterMissedShot();
            
            }
        }
    }

    private void shootListener2(float delta) {
        if (player1HasBall) {
            return;
        }

        boolean isHolding = Gdx.input.isKeyPressed(Input.Keys.UP);
        boolean cancelPressed = Gdx.input.isKeyJustPressed(Input.Keys.DOWN);

        // Cancel shot if DOWN is pressed while holding UP
        if (isHolding && cancelPressed) {
            chargeAmount2 = 0f;
            wasHolding2 = false;
            timeSinceRelease2 = BAR_HOLD_TIME;
            transparency2 = 1f;
            canShoot2 = true;
            cancelShotPending2 = true;
            player2.cancelShooting();
            return;
        }

        // While cancel is pending, require the player to release UP before allowing any new charge
        if (cancelShotPending2) {
            if (isHolding) {
                wasHolding2 = false;
                return;
            } else {
                cancelShotPending2 = false;
            }
        }

        if (isHolding && canShoot2 == true) {
            chargeAmount2 = MathUtils.clamp(chargeAmount2 + delta * CHARGE_RATE, 0f, 1f);
            timeSinceRelease2 = 0f;
            transparency2 = 1f;
        } else if (wasHolding2 && canShoot2 == true) {
            timeSinceRelease2 = 0f;
            transparency2 = 1f;
            madeShotFalling2 = false;
            madeShotFallTime2 = 0f;
            
            // Determine shooting margins for player 2
            if (player2.getPlayerX() < 600f) {
                shootingMargins2 = shootingMargin3Pointer;
            } else {
                shootingMargins2 = shootingMargin2Pointer;
            }
            
            String shotResult = getShotResult(shootingMargins2, chargeAmount2);
            System.out.println("Player 2: " + shotResult);

            // Start trajectory animation
            isTrajectoryActive2 = true;
            trajectoryTime2 = 0f;
            ballStartX2 = player2.getPlayerX() + 60f;
            ballStartY2 = player2.getPlayerY() + 40f;

            // Determine shot outcome once on release so the trajectory stays stable.
            boolean isGreenShot = shotResult.equals("Green");
            boolean isYellowShot = shotResult.equals("Yellow");

            if (isGreenShot) {
                shouldMakeShot2 = true;
                missHigh2 = false;
            } else if (isYellowShot) {
                shouldMakeShot2 = MathUtils.random() < 0.5f;
                missHigh2 = chargeAmount2 >= shootingMargins2[2][0];
            } else {
                shouldMakeShot2 = false;
                if (chargeAmount2 > shootingMargins2[2][1]) {
                    missHigh2 = true;
                } else if (chargeAmount2 < shootingMargins2[1][0]) {
                    missHigh2 = false;
                } else {
                    missHigh2 = MathUtils.random() < 0.5f;
                }
            }
            shotPeakHeight2 = MathUtils.random(400f, 600f);
        } else if (timeSinceRelease2 < BAR_HOLD_TIME) {
            timeSinceRelease2 += delta;
            transparency2 = MathUtils.clamp(transparency2 - delta, 0f, 1f);
            canShoot2 = false;
        }
        if (timeSinceRelease2 > BAR_HOLD_TIME) {
            chargeAmount2 = 0f;
            transparency2 = 1f;
            canShoot2 = true;
        }

        wasHolding2 = isHolding;

        if (isTrajectoryActive2) {
            trajectoryTime2 += delta;
            float t = MathUtils.clamp(trajectoryTime2 / TRAJECTORY_DURATION, 0f, 1f);
            float targetHoopX = 300f;
            if (missHigh2 && !shouldMakeShot2) {
                targetHoopX -= 600f;
            }
            float targetY = camera.viewportHeight / 2f - 300f + 250f;
            float ballX = ballStartX2 + (targetHoopX - ballStartX2) * t;
            float ballY;
            if (shouldMakeShot2) {
                ballY = ballStartY2 + (targetY - ballStartY2) * t + MathUtils.sin(t * MathUtils.PI) * shotPeakHeight2;
            } else if (missHigh2) {
                ballY = ballStartY2 + (targetY + 150f - ballStartY2) * t + MathUtils.sin(t * MathUtils.PI) * (shotPeakHeight2 + 80f);
            } else {
                ballY = ballStartY2 + (targetY - 200f - ballStartY2) * t + MathUtils.sin(t * MathUtils.PI) * (shotPeakHeight2 + 40f);
            }
            
            if (didDefenderCatchBall(player, ballX, ballY)) {
                System.out.println("player x: " + player.getPlayerX());
            System.out.println("Ball x: " + ballX);
                // Defender caught the ball mid-air — defer switching until they land
                pendingPossession = 1;
                isTrajectoryActive2 = false;
                trajectoryTime2 = 0f;
                return;
            }

            if (trajectoryTime2 >= TRAJECTORY_DURATION) {
                isTrajectoryActive2 = false;
                trajectoryTime2 = 0f;
                if (shouldMakeShot2) {
                    madeShotFalling2 = true;
                    madeShotFallTime2 = 0f;
                    madeShotFallX2 = 250f;
                    madeShotFallY2 = camera.viewportHeight / 2f - 300f + 250f;
                } else {
                    restartAfterMissedShot();
                }
            }
        }

        if (madeShotFalling2) {
            madeShotFallTime2 += delta;
            if (madeShotFallTime2 >= MADE_SHOT_FALL_DURATION) {
                madeShotFalling2 = false;
                madeShotFallTime2 = 0f;
                // After shot falls (score), restart with swapped possession
                restartAfterMissedShot();
            }
        }
    }

    private void syncPlayerModes() {
        player.setDefenseMode(!player1HasBall);
        player2.setDefenseMode(player1HasBall);
    }

    private Player getOffensePlayer() {
        return player1HasBall ? player : player2;
    }

    private Player getDefensePlayer() {
        return player1HasBall ? player2 : player;
    }

    private void switchPossessionToPlayer1() {
        player1HasBall = true;
        resetShotState();
        syncPlayerModes();
    }

    private void switchPossessionToPlayer2() {
        player1HasBall = false;
        resetShotState();
        syncPlayerModes();
    }

    private void resetShotState() {
        chargeAmount = 0f;
        timeSinceRelease = BAR_HOLD_TIME;
        wasHolding = false;
        transparency = 1f;
        canShoot = true;
        isTrajectoryActive = false;
        trajectoryTime = 0f;
        shouldMakeShot = false;
        missHigh = false;
        shotPeakHeight = 200f;
        madeShotFalling = false;
        madeShotFallTime = 0f;

        chargeAmount2 = 0f;
        timeSinceRelease2 = BAR_HOLD_TIME;
        wasHolding2 = false;
        transparency2 = 1f;
        canShoot2 = true;
        isTrajectoryActive2 = false;
        trajectoryTime2 = 0f;
        shouldMakeShot2 = false;
        missHigh2 = false;
        shotPeakHeight2 = 200f;
        madeShotFalling2 = false;
        madeShotFallTime2 = 0f;
    }

    private boolean didDefenderCatchBall(Player defender, float ballX, float ballY) {
        if (!defender.isBlockJumpActive()) {
            return false;
        }
        float defenderLeft = defender.getPlayerX() - CATCH_HORIZONTAL_PADDING;
        float defenderRight = defender.getPlayerX() + defender.getWidth() + CATCH_HORIZONTAL_PADDING;
        float defenderBottom = defender.getPlayerY() + CATCH_VERTICAL_PADDING;
        float defenderTop = defender.getPlayerY() + defender.getHeight() + CATCH_VERTICAL_PADDING;
        System.out.println("Defender Left: " + defenderLeft);
        return ballX >= defenderLeft && ballX <= defenderRight && ballY >= defenderBottom && ballY <= defenderTop;
    }

    private void restartAfterMissedShot() {
        // Swap possession
        player1HasBall = !player1HasBall;

        // Reset both players to center
        player.setPosition(600, 172);
        player2.setPosition(1000, 172);

        // Reset all shot state
        resetShotState();

        // Sync player modes based on new possession
        syncPlayerModes();
    }
}