package io.github.basket_brawl;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

/** Player class that handles movement with A and D keys. */
public class Player {
    private float x;
    private float y;
    private float baseY;
    private final float width = 256;
    private final float height = 256;
    private final float speed = 400f; // pixels per second
    private boolean defenseMode;
    
    private Rectangle bounds;
    private Sprite playerSprite;
    private TextureRegion idleFrame;
    private TextureRegion[] offenseDribbleFrames;
    private TextureRegion[] defenseDribbleFrames;
    private TextureRegion[] blockFrames;
    private Animation<TextureRegion> offenseDribbleAnimation;
    private Animation<TextureRegion> defenseDribbleAnimation;
    private Animation<TextureRegion> shootingAnimation;
    private float animationTime = 0;
    private static final float FRAME_DURATION = 0.1f; // Dribble frame duration
    private static final float SHOOT_FRAME_DURATION = 0.18f; // Slower shooting animation frames for smoother motion
    private boolean isShooting = false;
    private boolean shootingFinished = false;
    private boolean isMoving = false;
    private boolean blockJumpActive = false;
    private boolean blockJumpWindupActive = false;
    private float blockJumpTime = 0f;
    private float blockJumpWindupTime = 0f;
    private float blockJumpStartX = 0f;
    private float blockJumpStartY = 0f;
    private float blockJumpTargetX = 0f;
    private float blockJumpQueuedTargetX = 0f;
    private static final float BLOCK_JUMP_WINDUP_DURATION = 0.12f;
    private static final float BLOCK_JUMP_DURATION = 1.05f;
    private static final float BLOCK_JUMP_HEIGHT = 400f;
    private static final float BLOCK_JUMP_DISTANCE = 680f;
    private float minX = Float.NEGATIVE_INFINITY;
    private float maxX = Float.POSITIVE_INFINITY;
    private float shootingAnimationTime = 0;
    private float shootingHoldTime = 0;
    private static final float SHOOT_HOLD_DURATION = 0.15f; // Briefly freeze on the last shot frame before returning to dribble
    private static final float SHOOT_TRANSITION_SMOOTHING = 0.95f; // Slight easing for shooting time progression
    
    public Player(float startX, float startY) {
        this(startX, startY, false);
    }

    public Player(float startX, float startY, boolean defenseMode) {
        this.x = startX;
        this.y = startY;
        this.baseY = startY;
        this.defenseMode = defenseMode;
        this.bounds = new Rectangle(x, y, width, height);

        // Load dribbling animation frames
        loadAnimations();
    }
    public float getPlayerX() {
        return x;
    }
    public float getPlayerY() {
        return y;
    }
    private void loadAnimations() {
        try {
            // Load all walk frames from the appropriate folder
            offenseDribbleFrames = new TextureRegion[3];
            offenseDribbleFrames[0] = new TextureRegion(new Texture("Steph/Dribble/drib 1.png"));
            offenseDribbleFrames[1] = new TextureRegion(new Texture("Steph/Dribble/drib 2.png"));
            offenseDribbleFrames[2] = new TextureRegion(new Texture("Steph/Dribble/drib 3.png"));

            defenseDribbleFrames = new TextureRegion[3];
            defenseDribbleFrames[0] = new TextureRegion(new Texture("Steph/Defense/shuffle 2.png"));
            defenseDribbleFrames[1] = new TextureRegion(new Texture("Steph/Defense/shuffle 3.png"));
            defenseDribbleFrames[2] = new TextureRegion(new Texture("Steph/Defense/shuffle 5.png"));

            idleFrame = new TextureRegion(new Texture("Steph/Defense/idle.png"));
            blockFrames = new TextureRegion[5];
            blockFrames[0] = new TextureRegion(new Texture("Steph/Block/block 1.png"));
            blockFrames[1] = new TextureRegion(new Texture("Steph/Block/block 2.png"));
            blockFrames[2] = new TextureRegion(new Texture("Steph/Block/block 3.png"));
            blockFrames[3] = new TextureRegion(new Texture("Steph/Block/block 4.png"));
            blockFrames[4] = new TextureRegion(new Texture("Steph/Block/block 5.png"));
            
            // Create dribble animation with 0.15 second per frame
            offenseDribbleAnimation = new Animation<>(FRAME_DURATION, offenseDribbleFrames);
            defenseDribbleAnimation = new Animation<>(FRAME_DURATION, defenseDribbleFrames);
            
            // Load all 3 shooting frames
            TextureRegion[] shootingFrames = new TextureRegion[3];
            shootingFrames[0] = new TextureRegion(new Texture("Steph/Shoot/shoot 1.png"));
            shootingFrames[1] = new TextureRegion(new Texture("Steph/Shoot/shoot 2.png"));
            shootingFrames[2] = new TextureRegion(new Texture("Steph/Shoot/shoot 3.png"));
            
            // Create shooting animation with a slower frame rate than dribble
            shootingAnimation = new Animation<>(SHOOT_FRAME_DURATION, shootingFrames);

            playerSprite = defenseMode ? new Sprite(idleFrame) : new Sprite(offenseDribbleFrames[0]);
            playerSprite.setSize(256, 256);
            
            System.out.println("Animations loaded successfully!");
        } catch (Exception e) {
            System.out.println("Error loading animations: " + e.getMessage());
        }
    }
    
    public void update(float delta, boolean left, boolean right, boolean shootingHeld) {
        if (blockJumpWindupActive) {
            blockJumpWindupTime += delta/1.5;
            x = blockJumpStartX;
            y = baseY;

            if (blockJumpWindupTime >= BLOCK_JUMP_WINDUP_DURATION) {
                blockJumpWindupActive = false;
                blockJumpActive = true;
                blockJumpTime = 0f;
                blockJumpStartX = x;
                blockJumpStartY = baseY;
                blockJumpTargetX = blockJumpQueuedTargetX;
            }

            clampToHorizontalBounds();

            bounds.set(x, y, width, height);
            return;
        }

        if (blockJumpActive) {
            blockJumpTime += delta;
            float progress = Math.min(blockJumpTime / BLOCK_JUMP_DURATION, 1f);
            float easedProgress = progress * progress * (3f - 2f * progress);
            x = blockJumpStartX + (blockJumpTargetX - blockJumpStartX) * easedProgress;
            y = blockJumpStartY + (float) Math.sin(progress * Math.PI) * BLOCK_JUMP_HEIGHT;

            clampToHorizontalBounds();

            if (progress >= 1f) {
                blockJumpActive = false;
                x = blockJumpTargetX;
                y = baseY;
            }

            bounds.set(x, y, width, height);
            return;
        }

        // Calculate movement - only allow if not shooting or holding after shooting
        float moveX = 0;
        
        if (!isShooting && !shootingFinished) {
            if (left) moveX -= speed * delta;
            if (right) moveX += speed * delta;
        }
        
        // Apply movement
        x += moveX;
        y = baseY;

        clampToHorizontalBounds();
        
        isMoving = left || right;

        // Only advance the shuffle/dribble animation while moving so stationary defense uses the idle frame.
        if (isMoving || !defenseMode) {
            animationTime += delta;
        }
        
        // Keep the first shooting frame visible while W is held.
        // Once W is released, advance the rest of the shooting animation.
        if (isShooting && shootingHeld) {
            shootingAnimationTime = 0f;
        }

        // Update shooting animation if active
        if (isShooting && !shootingHeld) {
            shootingAnimationTime += delta * SHOOT_TRANSITION_SMOOTHING;
            if (shootingAnimation.isAnimationFinished(shootingAnimationTime)) {
                isShooting = false;
                shootingFinished = true;
                shootingHoldTime = 0;
                shootingAnimationTime = shootingAnimation.getAnimationDuration();
            }
        }
        
        // Hold on the last shooting frame briefly, then return to dribbling
        if (shootingFinished) {
            shootingHoldTime += delta;
            if (shootingHoldTime >= SHOOT_HOLD_DURATION) {
                shootingFinished = false;
                shootingHoldTime = 0;
            }
        }
        
        // Update bounds 
        bounds.set(x, y, width, height);
    }
    
    public void render(Batch batch, boolean faceRight) {
        TextureRegion currentFrame = null;
        
        if (isShooting && shootingAnimation != null) {
            // Use shooting animation while shooting
            currentFrame = shootingAnimation.getKeyFrame(shootingAnimationTime, false);
        } else if (shootingFinished && shootingAnimation != null) {
            // Show last frame of shooting animation while holding
            currentFrame = shootingAnimation.getKeyFrame(shootingAnimationTime, false);
        } else if (defenseMode && blockJumpWindupActive && blockFrames != null) {
            currentFrame = blockFrames[0];
        } else if (defenseMode && blockJumpActive && blockFrames != null) {
            float jumpProgress = Math.min(blockJumpTime / BLOCK_JUMP_DURATION, 1f);
            if (jumpProgress < 0.15f) {
                currentFrame = blockFrames[0];
            } else if (jumpProgress < 0.3f) {
                currentFrame = blockFrames[1];
            } else if (jumpProgress < 0.45f) {
                currentFrame = blockFrames[2];
            } else if (jumpProgress < 0.9f) {
                currentFrame = blockFrames[3];
            } else {
                currentFrame = blockFrames[4];
            }
        } else if (defenseMode && !isMoving && idleFrame != null) {
            currentFrame = idleFrame;
        } else if (defenseMode && defenseDribbleAnimation != null) {
            currentFrame = defenseDribbleAnimation.getKeyFrame(animationTime, true);
        } else if (offenseDribbleAnimation != null) {
            // Use dribble animation when not shooting
            currentFrame = offenseDribbleAnimation.getKeyFrame(animationTime, true);
        }
        
        if (currentFrame != null) {
            currentFrame.setU(0);
            currentFrame.setV(0);
            currentFrame.setU2(1);
            currentFrame.setV2(1);
                    float frameWidth = 256; // Fixed size for consistent scaling
                    float frameHeight = 256;
                    float scale = Math.min(width / frameWidth, height / frameHeight);
                    float drawWidth = frameWidth * scale;
                    float drawHeight = frameHeight * scale;
                    if (faceRight) {
                        currentFrame.flip(true, false);
                    }
                    float drawX = x + (width - drawWidth) / 2f;
                    float drawY = y + (height - drawHeight) / 2f;
                    batch.draw(currentFrame, drawX, drawY, drawWidth, drawHeight);
        } else if (playerSprite != null) {
            playerSprite.setPosition(x, y);
            playerSprite.draw(batch);
        } else {
            System.out.println("Player sprite is null!");
        }
    }
    
    public float getX() {
        return x;
    }
    
    public float getY() {
        return y;
    }
    
    public float getWidth() {
        return width;
    }
    
    public float getHeight() {
        return height;
    }
    
    public Rectangle getBounds() {
        return bounds;
    }

    public boolean isDefenseMode() {
        return defenseMode;
    }

    public boolean isBlockJumpActive() {
        return blockJumpActive;
    }

    public void setDefenseMode(boolean defenseMode) {
        if (this.defenseMode == defenseMode) {
            return;
        }

        this.defenseMode = defenseMode;
        blockJumpWindupActive = false;
        blockJumpActive = false;
        blockJumpWindupTime = 0f;
        blockJumpTime = 0f;
        isShooting = false;
        shootingFinished = false;
        shootingAnimationTime = 0f;
        shootingHoldTime = 0f;
        animationTime = 0f;
    }
    
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        this.baseY = y;
        clampToHorizontalBounds();
        bounds.set(x, y, width, height);
    }

    public void setHorizontalBounds(float minX, float maxX) {
        this.minX = minX;
        this.maxX = maxX;
        clampToHorizontalBounds();
        bounds.set(x, y, width, height);
    }

    private void clampToHorizontalBounds() {
        x = Math.max(minX, Math.min(x, maxX));
    }

    public void startBlockJumpToward(float targetX) {
        if (!defenseMode || blockJumpActive || blockJumpWindupActive) {
            return;
        }

        float clampedTargetX = Math.max(minX, Math.min(targetX, maxX));
        float deltaX = clampedTargetX - x;
        float jumpDirection = deltaX < 0f ? -1f : 1f;
        blockJumpTargetX = Math.max(minX, Math.min(x + jumpDirection * BLOCK_JUMP_DISTANCE, maxX));
        blockJumpQueuedTargetX = blockJumpTargetX;
        blockJumpStartX = x;
        blockJumpStartY = baseY;
        blockJumpWindupTime = 0f;
        blockJumpWindupActive = true;

        isShooting = false;
        shootingFinished = false;
        shootingAnimationTime = 0f;
        shootingHoldTime = 0f;
        animationTime = 0f;
    }
    
    public void startShooting() {
        if (!defenseMode && !isShooting && !shootingFinished) {
            isShooting = true;
            shootingAnimationTime = 0;
        }
    }

    public void cancelShooting() {
        isShooting = false;
        shootingFinished = false;
        shootingAnimationTime = 0f;
        shootingHoldTime = 0f;
    }
}