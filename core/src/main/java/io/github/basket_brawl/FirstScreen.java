package io.github.basket_brawl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;

/** First screen of the application. Displayed after the application is created. */
public class FirstScreen implements Screen {
    private Player player;
    private Batch batch;
    private ShapeRenderer shapeRenderer;
    
    public FirstScreen() {
        // Create player at center of screen (approximate)
        player = new Player(384, 256);
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
    }
    
    @Override
    public void show() {
        // Prepare your screen here.
    }

    @Override
    public void render(float delta) {
        // Clear screen with black background
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        // Handle WASD input
        Input input = Gdx.input;
        boolean left = input.isKeyPressed(Input.Keys.A);
        boolean right = input.isKeyPressed(Input.Keys.D);
        
        // Update player position
        player.update(delta, left, right);
        
        // Draw player using ShapeRenderer
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(player.getX(), player.getY(), player.getWidth(), player.getHeight());
        shapeRenderer.end();
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
    }
}