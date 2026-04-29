package io.github.basket_brawl;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

/** Player class that handles movement with WASD keys. */
public class Player {
    private float x;
    private float y;
    private float width = 32;
    private float height = 32;
    private float speed = 200f; // pixels per second
    
    private Rectangle bounds;
    private Color color = Color.RED;
    
    public Player(float startX, float startY) {
        this.x = startX;
        this.y = startY;
        this.bounds = new Rectangle(x, y, width, height);
    }
    
    public void update(float delta, boolean left, boolean right) {
        // Calculate movement
        float moveX = 0;
        
        if (left) moveX -= speed * delta;
        if (right) moveX += speed * delta;
        
        // Apply movement
        x += moveX;
        
        // Update bounds
        bounds.set(x, y, width, height);
    }
    
    public void render(Batch batch) {
        // Draw player as a simple rectangle
        // Using immediate mode drawing - in a real game you'd use a Sprite or Texture
        // For now, we'll just track the position
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
}