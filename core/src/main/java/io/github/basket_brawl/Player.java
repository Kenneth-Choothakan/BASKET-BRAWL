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
    private float speed = 400f; // pixels per second (can be modified by stats)
    private boolean defenseMode;
    private final String characterSkin;
    private CharacterStats stats;
    
    private Rectangle bounds;
    private Sprite playerSprite;
    private float skinScale = 1f;
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
    private float adjustedBlockJumpHeight = BLOCK_JUMP_HEIGHT;
    private float minX = Float.NEGATIVE_INFINITY;
    private float maxX = Float.POSITIVE_INFINITY;
    private float shootingAnimationTime = 0;
    private float shootingHoldTime = 0;
    private static final float SHOOT_HOLD_DURATION = 0.15f; // Briefly freeze on the last shot frame before returning to dribble
    private static final float SHOOT_TRANSITION_SMOOTHING = 0.95f; // Slight easing for shooting time progression
    
    public Player(float startX, float startY) {
        this(startX, startY, false, "Steph", null);
    }

    public Player(float startX, float startY, boolean defenseMode) {
        this(startX, startY, defenseMode, "Steph", null);
    }

    public Player(float startX, float startY, boolean defenseMode, String characterSkin) {
        this(startX, startY, defenseMode, characterSkin, null);
    }

    public Player(float startX, float startY, boolean defenseMode, String characterSkin, CharacterStats stats) {
        this.x = startX;
        this.y = startY;
        this.baseY = startY;
        this.defenseMode = defenseMode;
        this.characterSkin = normalizeSkin(characterSkin);
        this.stats = stats;
        
        // Apply speed multiplier from stats
        if (this.stats != null) {
            this.speed = 400f * this.stats.speed;
            this.adjustedBlockJumpHeight = BLOCK_JUMP_HEIGHT * this.stats.jumpHeight;
        } else {
            this.speed = 400f;
            this.adjustedBlockJumpHeight = BLOCK_JUMP_HEIGHT;
        }
        
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
            String skinRoot = characterSkin;
            switch (skinRoot) {
                case "LeBron":
                    offenseDribbleFrames = loadFrames(
                        "LeBron/Dribble/1.png",
                        "LeBron/Dribble/2.png",
                        "LeBron/Dribble/3.png"
                    );
                    defenseDribbleFrames = loadFrames(
                        "LeBron/Defense/1.png",
                        "LeBron/Defense/2.png",
                        "LeBron/Defense/1.png"
                    );
                    idleFrame = new TextureRegion(new Texture("LeBron/Defense/1.png"));
                    blockFrames = loadFrames(
                        "LeBron/Block/2.png",
                        "LeBron/Block/3.png",
                        "LeBron/Block/2.png",
                        "LeBron/Block/3.png",
                        "LeBron/Block/2.png"
                    );
                    shootingAnimation = new Animation<>(SHOOT_FRAME_DURATION, loadFrames(
                        "LeBron/Shoot/1.png",
                        "LeBron/Shoot/2.png",
                        "LeBron/Shoot/3.png"
                    ));
                    break;
                case "Brandon":
                    offenseDribbleFrames = loadFrames(
                        "Brandon/Dribble/1.png",
                        "Brandon/Dribble/2.png",
                        "Brandon/Dribble/3.png"
                    );
                    defenseDribbleFrames = loadFrames(
                        "Brandon/Defense/1.png",
                        "Brandon/Defense/2.png",
                        "Brandon/Defense/1.png"
                    );
                    idleFrame = new TextureRegion(new Texture("Brandon/Defense/1.png"));
                    blockFrames = loadFrames(
                        "Brandon/Block/BranShoot2.png",
                        "Brandon/Block/BranShoot3.png",
                        "Brandon/Block/BranShoot2.png",
                        "Brandon/Block/BranShoot3.png",
                        "Brandon/Block/BranShoot2.png"
                    );
                    shootingAnimation = new Animation<>(SHOOT_FRAME_DURATION, loadFrames(
                        "Brandon/Shoot/1.png",
                        "Brandon/Shoot/2.png",
                        "Brandon/Shoot/3.png"
                    ));
                    break;
                case "Manvir":
                    offenseDribbleFrames = loadFrames(
                        "Manvir/Dribble/1.png",
                        "Manvir/Dribble/2.png",
                        "Manvir/Dribble/3.png"
                    );
                    defenseDribbleFrames = loadFrames(
                        "Manvir/Defense/1.png",
                        "Manvir/Defense/2.png",
                        "Manvir/Defense/1.png"
                    );
                    idleFrame = new TextureRegion(new Texture("Manvir/Defense/1.png"));
                    blockFrames = loadFrames(
                        "Manvir/Block/2.png",
                        "Manvir/Block/4.png",
                        "Manvir/Block/2.png",
                        "Manvir/Block/4.png",
                        "Manvir/Block/2.png"
                    );
                    shootingAnimation = new Animation<>(SHOOT_FRAME_DURATION, loadFrames(
                        "Manvir/Shoot/1.png",
                        "Manvir/Shoot/2.png",
                        "Manvir/Shoot/4.png"
                    ));
                    break;
                case "Vishal":
                    offenseDribbleFrames = loadFrames(
                        "Vishal/Dribble/1.png",
                        "Vishal/Dribble/2.png",
                        "Vishal/Dribble/3.png"
                    );
                    defenseDribbleFrames = loadFrames(
                        "Vishal/Defense/1.png",
                        "Vishal/Defense/2.png",
                        "Vishal/Defense/1.png"
                    );
                    idleFrame = new TextureRegion(new Texture("Vishal/Defense/1.png"));
                    blockFrames = loadFrames(
                        "Vishal/Block/1.png",
                        "Vishal/Block/2.png",
                        "Vishal/Block/1.png",
                        "Vishal/Block/2.png",
                        "Vishal/Block/1.png"
                    );
                    shootingAnimation = new Animation<>(SHOOT_FRAME_DURATION, loadFrames(
                        "Vishal/Shoot/1.png",
                        "Vishal/Shoot/2.png",
                        "Vishal/Shoot/3.png"
                    ));
                    break;
                case "Steph":
                default:
                    offenseDribbleFrames = loadFrames(
                        "Steph/Dribble/drib 1.png",
                        "Steph/Dribble/drib 2.png",
                        "Steph/Dribble/drib 3.png"
                    );

                    defenseDribbleFrames = loadFrames(
                        "Steph/Defense/shuffle 2.png",
                        "Steph/Defense/shuffle 3.png",
                        "Steph/Defense/shuffle 5.png"
                    );

                    idleFrame = new TextureRegion(new Texture("Steph/Defense/idle.png"));
                    blockFrames = loadFrames(
                        "Steph/Block/block 1.png",
                        "Steph/Block/block 2.png",
                        "Steph/Block/block 3.png",
                        "Steph/Block/block 4.png",
                        "Steph/Block/block 5.png"
                    );
                    shootingAnimation = new Animation<>(SHOOT_FRAME_DURATION, loadFrames(
                        "Steph/Shoot/shoot 1.png",
                        "Steph/Shoot/shoot 2.png",
                        "Steph/Shoot/shoot 3.png"
                    ));
                    break;
            }
            
            // Create dribble animation with 0.15 second per frame
            offenseDribbleAnimation = new Animation<>(FRAME_DURATION, offenseDribbleFrames);
            defenseDribbleAnimation = new Animation<>(FRAME_DURATION, defenseDribbleFrames);

            playerSprite = defenseMode ? new Sprite(idleFrame) : new Sprite(offenseDribbleFrames[0]);
            playerSprite.setSize(256, 256);
            // Simplified scaling: make every skin except Steph larger by a fixed multiplier
            if ("Steph".equals(characterSkin)) {
                skinScale = 1f;
            } else {
                skinScale = 2f; // larger so non-Steph characters appear closer in size to Steph
            }

            System.out.println("Animations loaded successfully! (skinScale=" + skinScale + ")");
        } catch (Exception e) {
            System.out.println("Error loading animations: " + e.getMessage());
        }
    }

    private String normalizeSkin(String skinName) {
        if (skinName == null) {
            return "Steph";
        }

        switch (skinName) {
            case "LeBron":
            case "Brandon":
            case "Manvir":
            case "Vishal":
                return skinName;
            case "Steph":
            default:
                return "Steph";
        }
    }

    private TextureRegion[] loadFrames(String... paths) {
        TextureRegion[] frames = new TextureRegion[paths.length];
        for (int index = 0; index < paths.length; index++) {
            frames[index] = new TextureRegion(new Texture(paths[index]));
        }
        return frames;
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
            y = blockJumpStartY + (float) Math.sin(progress * Math.PI) * adjustedBlockJumpHeight;

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
            float frameWidth = currentFrame.getRegionWidth();
            float frameHeight = currentFrame.getRegionHeight();
            if (frameWidth <= 0 || frameHeight <= 0) {
                frameWidth = 256f;
                frameHeight = 256f;
            }
            float scale = Math.min(width / frameWidth, height / frameHeight);
            // Apply special-case downscaling for Brandon during jump, dribble, and shoot
            float extraAnimScale = 1f;
            if ("Brandon".equals(characterSkin)) {
                boolean isShootingAnim = (isShooting && shootingAnimation != null) || (shootingFinished && shootingAnimation != null);
                boolean isJumpAnim = (defenseMode && (blockJumpWindupActive || blockJumpActive) && blockFrames != null);
                boolean isDribbleAnim = (!defenseMode && offenseDribbleAnimation != null);
                if (isShootingAnim || isJumpAnim || isDribbleAnim) {
                    extraAnimScale = 0.5f; // reduce Brandon's animation size for these actions
                }
            }

            float drawWidth = frameWidth * scale * skinScale * extraAnimScale;
            float drawHeight = frameHeight * scale * skinScale * extraAnimScale;
            float drawX = x + (width - drawWidth) / 2f;
            float drawY = y + (height - drawHeight) / 2f;
            // Use a Sprite so we can flip safely without mutating the shared TextureRegion
            Sprite sprite = new Sprite(currentFrame);
            sprite.setSize(drawWidth, drawHeight);
            sprite.setPosition(drawX, drawY);
            if (faceRight && !sprite.isFlipX()) sprite.flip(true, false);
            if (!faceRight && sprite.isFlipX()) sprite.flip(true, false);
            sprite.draw(batch);
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