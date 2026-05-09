package io.github.basket_brawl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import static com.badlogic.gdx.utils.ScreenUtils.clear;
import com.badlogic.gdx.utils.viewport.FitViewport;

/** First screen of the application. Displayed after the application is created. */
public class StartScreen implements Screen {
    Texture backgroundTexture;
    Texture startTexture;
    Texture selectionTexture;
    SpriteBatch spriteBatch;
    FitViewport viewport;
    Skin skin;
    Stage stage;
    Table table;
    ImageButton startButton;
    ImageButton selectionButton;
    TextureRegionDrawable startDrawable;
    TextureRegionDrawable selectionDrawable; 
    Main game;
    Texture startHoverTex;
    Texture selectHoverTex;
    ImageButton.ImageButtonStyle startStyle;
    ImageButton.ImageButtonStyle selectStyle;


    
public StartScreen(Main game) {
    this.game = game;
    
    backgroundTexture = new Texture(Gdx.files.internal("newStartScreen.jpg"));
    startTexture = new Texture(Gdx.files.internal("button_centered_2.png"));
    selectionTexture = new Texture(Gdx.files.internal("button_centered_4.png"));
    Texture startHoverTex = new Texture(Gdx.files.internal("button_centered_1.png"));
    Texture selectHoverTex = new Texture(Gdx.files.internal("button_centered_3.png"));

    startDrawable = new TextureRegionDrawable(new TextureRegion(startTexture));
    selectionDrawable = new TextureRegionDrawable(new TextureRegion(selectionTexture));

    ImageButton.ImageButtonStyle startStyle = new ImageButton.ImageButtonStyle();
    startStyle.imageUp = startDrawable;
    startStyle.imageOver = new TextureRegionDrawable(new TextureRegion(startHoverTex));

    ImageButton.ImageButtonStyle selectStyle = new ImageButton.ImageButtonStyle();
    selectStyle.imageUp = selectionDrawable;
    selectStyle.imageOver = new TextureRegionDrawable(new TextureRegion(selectHoverTex));

    startButton = new ImageButton(startStyle);
    selectionButton = new ImageButton(selectStyle);

    spriteBatch = new SpriteBatch();
    viewport = new FitViewport(8, 5);
    stage = new Stage(viewport); 
    Gdx.input.setInputProcessor(stage); 

    table = new Table();
    table.setFillParent(true);
    table.center();
    // push the centered row slightly to the right
    table.padTop(3f);
    table.padLeft(2f);
    stage.addActor(table);
    // Place buttons in one horizontal row centered in the table
    table.defaults().pad(0.25f);
    table.add(startButton).size(3f, 2f).center().padRight(2f);
    table.add(selectionButton).size(3f, 2f).center();

    startButton.addListener(new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            game.play();
        }
    });

    selectionButton.addListener(new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            game.Selection();
        }
    });
    
    // Buttons are positioned by the table; remove manual positioning/actions


    }
    
    @Override
    public void show() {
        // Prepare your screen here.
    }

    @Override
    public void render(float delta) {
        input();
        logic();
        draw();
        stage.act(delta);
        stage.draw();
    }

    private void input() {

    }

    private void logic() {}

    private void draw() {
        clear(Color.BLACK);
        viewport.apply();
    
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();
        spriteBatch.draw(backgroundTexture, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        spriteBatch.end();

        stage.act();
        stage.draw();
    }
    
    @Override
    public void resize(int width, int height) {
        // If the window is minimized on a desktop (LWJGL3) platform, width and height are 0, which causes problems.
        // In that case, we don't resize anything, and wait for the window to be a normal size before updating.
        if(width <= 0 || height <= 0) return;
        viewport.update(width, height, true);
        // Resize your screen here. The parameters represent the new window size.
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