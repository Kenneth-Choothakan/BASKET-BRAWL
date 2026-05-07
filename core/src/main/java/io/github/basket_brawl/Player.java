package io.github.basket_brawl;

import com.badlogic.gdx.graphics.Color;
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
    private float width = 256;
    private float height = 256;
    private float speed = 200f; // pixels per second
    
    private Rectangle bounds;
    private Sprite playerSprite;
    private Animation<TextureRegion> dribbleAnimation;
    private Animation<TextureRegion> shootingAnimation;
    private float animationTime = 0;
    private static final float FRAME_DURATION = 0.1f; // Dribble frame duration
    private static final float SHOOT_FRAME_DURATION = 0.18f; // Slower shooting animation frames for smoother motion
    private boolean isShooting = false;
    private boolean shootingFinished = false;
    private float shootingAnimationTime = 0;
    private float shootingHoldTime = 0;
    private static final float SHOOT_HOLD_DURATION = 0.15f; // Briefly freeze on the last shot frame before returning to dribble
    private static final float SHOOT_TRANSITION_SMOOTHING = 0.95f; // Slight easing for shooting time progression
    
    public Player(float startX, float startY) {
        this.x = startX;
        this.y = startY;
        this.bounds = new Rectangle(x, y, width, height);
        
        // Load the player image
        try {
            Texture texture = new Texture("player.png");
            playerSprite = new Sprite(texture);
            playerSprite.setSize(256, 256);
            System.out.println("Player image loaded successfully!");
        } catch (Exception e) {
            System.out.println("Error loading player image: " + e.getMessage());
            e.printStackTrace();
        }
        
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
            // Load all 3 dribble frames
            TextureRegion[] dribbleFrames = new TextureRegion[3];
            dribbleFrames[0] = new TextureRegion(new Texture("drib 1.png"));
            dribbleFrames[1] = new TextureRegion(new Texture("drib 2.png"));
            dribbleFrames[2] = new TextureRegion(new Texture("drib 3.png"));
            
            // Create dribble animation with 0.15 second per frame
            dribbleAnimation = new Animation<TextureRegion>(FRAME_DURATION, dribbleFrames);
            
            // Load all 3 shooting frames
            TextureRegion[] shootingFrames = new TextureRegion[3];
            shootingFrames[0] = new TextureRegion(new Texture("shoot 1.png"));
            shootingFrames[1] = new TextureRegion(new Texture("shoot 2.png"));
            shootingFrames[2] = new TextureRegion(new Texture("shoot 3.png"));
            
            // Create shooting animation with a slower frame rate than dribble
            shootingAnimation = new Animation<TextureRegion>(SHOOT_FRAME_DURATION, shootingFrames);
            
            System.out.println("Animations loaded successfully!");
        } catch (Exception e) {
            System.out.println("Error loading animations: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void update(float delta, boolean left, boolean right, boolean shootingHeld) {
        // Calculate movement - only allow if not shooting or holding after shooting
        float moveX = 0;
        
        if (!isShooting && !shootingFinished) {
            if (left) moveX -= speed * delta;
            if (right) moveX += speed * delta;
        }
        
        // Apply movement
        x += moveX;
        
        // Always update dribble animation time so it resumes smoothly later
        animationTime += delta;
        
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
    
    public void render(Batch batch) {
        TextureRegion currentFrame = null;
        
        if (isShooting && shootingAnimation != null) {
            // Use shooting animation while shooting
            currentFrame = shootingAnimation.getKeyFrame(shootingAnimationTime, false);
        } else if (shootingFinished && shootingAnimation != null) {
            // Show last frame of shooting animation while holding
            currentFrame = shootingAnimation.getKeyFrame(shootingAnimationTime, false);
        } else if (dribbleAnimation != null) {
            // Use dribble animation when not shooting
            currentFrame = dribbleAnimation.getKeyFrame(animationTime, true);
        }
        
        if (currentFrame != null) {
            // Ensure consistent sprite size
            currentFrame.setRegionWidth((int) width);
            currentFrame.setRegionHeight((int) height);
            currentFrame.setU(0);
            currentFrame.setV(0);
            currentFrame.setU2(1);
            currentFrame.setV2(1);
            batch.draw(currentFrame, x, y, width, height);
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
    
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        bounds.set(x, y, width, height);
    }
    
    public void startShooting() {
        if (!isShooting && !shootingFinished) {
            isShooting = true;
            shootingAnimationTime = 0;
        }
    }
}