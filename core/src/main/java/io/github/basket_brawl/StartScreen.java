package io.github.basket_brawl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import static com.badlogic.gdx.utils.ScreenUtils.clear;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

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
    //int songSelection;
    // Music music;
    // Music song1;
    // Music song2;
    // Music song3;
    // Music song4;
    // Music song5;
    // Music song6;
    // Music song7;
    // Music song8;
    // Music song9;
    // Music song10;
    // Music song11;
    // Music song12;
    // Music song13;
    // Music song14;
    // Music song15;
    // Music song16;
    // Music song17;
    // Music song18;
    // Music song19;
    // Music song20;
    // Music song21;
    // Music song22;
    // Music song23;
    // Music song24;
    // Music song25;
    // Music song26;
    // Music song27;
    // Music song28;
    // Music song29;
    // Music song30;

    // Music[] songArray;

    
    public StartScreen(Main game) {
        this.game = game;
        backgroundTexture = new Texture(Gdx.files.internal("start_screen.png"));
        startTexture = new Texture(Gdx.files.internal("start_one.png"));
        selectionTexture = new Texture(Gdx.files.internal("selection_one.png"));
        spriteBatch = new SpriteBatch();
        viewport = new FitViewport(8, 5);

        stage = new Stage(viewport); 
        Gdx.input.setInputProcessor(stage); 
        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        //startButton = new ImageButton(skin);
        //table.add(startButton).size(200, 100).pad(10);
        //selectionButton = new ImageButton(skin);
        //table.add(selectionButton).size(200, 100).pad(10);

        startDrawable = new TextureRegionDrawable(new TextureRegion(startTexture));
        selectionDrawable = new TextureRegionDrawable(new TextureRegion(selectionTexture));

        startButton = new ImageButton(startDrawable);
        selectionButton = new ImageButton(selectionDrawable);


        table.add(startButton).size(2f, 1f).padRight(2f).padBottom(-2f);
        table.add(selectionButton).size(2f, 1f).padLeft(2f).padBottom(-2f);

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

        // song1 = Gdx.audio.newMusic(Gdx.files.internal("4X4.mp3"));
        // song2 = Gdx.audio.newMusic(Gdx.files.internal("5TO10.mp3"));
        // song3 = Gdx.audio.newMusic(Gdx.files.internal("CALL BACK.mp3"));
        // song4 = Gdx.audio.newMusic(Gdx.files.internal("CARDIGAN.mp3"));
        // song5 = Gdx.audio.newMusic(Gdx.files.internal("LASTLAUGH.mp3"));
        // song6 = Gdx.audio.newMusic(Gdx.files.internal("LMM.mp3"));
        // song7 = Gdx.audio.newMusic(Gdx.files.internal("LOH.mp3"));
        // song8 = Gdx.audio.newMusic(Gdx.files.internal("SECONDHAND.mp3"));
        // song9 = Gdx.audio.newMusic(Gdx.files.internal("LIE.mp3"));
        // song10 = Gdx.audio.newMusic(Gdx.files.internal("CRY.mp3"));
        // song11 = Gdx.audio.newMusic(Gdx.files.internal("REMINDER.mp3"));
        // song12 = Gdx.audio.newMusic(Gdx.files.internal("COMING.mp3"));
        // song13 = Gdx.audio.newMusic(Gdx.files.internal("SOMEONE.mp3"));
        // song14 = Gdx.audio.newMusic(Gdx.files.internal("ORDINARY.mp3"));
        // song15 = Gdx.audio.newMusic(Gdx.files.internal("HEARTLESS.mp3"));
        // song16 = Gdx.audio.newMusic(Gdx.files.internal("HOB.mp3"));
        // song17 = Gdx.audio.newMusic(Gdx.files.internal("ENJOY.mp3"));
        // song18 = Gdx.audio.newMusic(Gdx.files.internal("NOKIA.mp3"));
        // song19 = Gdx.audio.newMusic(Gdx.files.internal("FT.mp3"));
        // song20 = Gdx.audio.newMusic(Gdx.files.internal("CV.mp3"));
        // song21 = Gdx.audio.newMusic(Gdx.files.internal("OW.mp3"));
        // song22 = Gdx.audio.newMusic(Gdx.files.internal("NB.mp3"));
        // song23 = Gdx.audio.newMusic(Gdx.files.internal("SND.mp3"));
        // song24 = Gdx.audio.newMusic(Gdx.files.internal("POWER.mp3"));
        // song25 = Gdx.audio.newMusic(Gdx.files.internal("RUNNIN.mp3"));
        // song26 = Gdx.audio.newMusic(Gdx.files.internal("SLIDIN.mp3"));
        // song27 = Gdx.audio.newMusic(Gdx.files.internal("MD.mp3"));
        // song28 = Gdx.audio.newMusic(Gdx.files.internal("JC.mp3"));
        // song29 = Gdx.audio.newMusic(Gdx.files.internal("BOB.mp3"));
        // song30 = Gdx.audio.newMusic(Gdx.files.internal("PM.mp3"));


        // songArray = new Music[] {song1, song2, song3, song4, song5, song6, song7, song8, song9, song10, song11, song12, song14, song15, song16, song17, song18, song19, song20, song21, song22, song23, song24, song25, song26, song27, song28, song29, song30};
        // songSelection = (int)(Math.random() * songArray.length);
        // music = songArray[songSelection];
        // music.play();
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
        //playMusic();
    }

    // private void playMusic(){
    //     if(!(music.isPlaying())){
    //         songSelection = (int)(Math.random() * songArray.length);
    //         music = songArray[songSelection];
    //         music.play();
    //     }
    //     if(Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
    //         music.stop();
    //         songSelection = (int)(Math.random() * songArray.length);
    //         music = songArray[songSelection];
    //         music.play();
    //     }
    // }

    private void input() {

    }

    private void logic() {

    }

   /*private void draw() {
        clear(Color.BLACK);
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();
        
        spriteBatch.draw(backgroundTexture, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        
        //spriteBatch.draw(selectionTexture, 0.5f, 0, 2, 1);
        //spriteBatch.draw(startTexture, 5.5f, 0, 2, 1);
        
        spriteBatch.end();
    }*/

    private void draw() {
    clear(Color.BLACK);
    viewport.apply();
    
    // 1. Draw ONLY the background manually
    spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
    spriteBatch.begin();
    spriteBatch.draw(backgroundTexture, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
    spriteBatch.end();

    // 2. Let the stage handle the buttons
    stage.act(); // Updates logic/animations
    stage.draw(); // Draws the buttons at the positions defined in the table
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