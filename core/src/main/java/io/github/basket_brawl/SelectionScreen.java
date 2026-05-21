package io.github.basket_brawl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import static com.badlogic.gdx.utils.ScreenUtils.clear;
import com.badlogic.gdx.utils.viewport.FitViewport;

/** First screen of the application. Displayed after the application is created. */
public class SelectionScreen implements Screen {
    Texture backgroundTexture;
    SpriteBatch spriteBatch;
    FitViewport viewport;
    Stage stage; // Use a Stage to manage UI
    Main game;

    Texture mysteryTexture;


    //----
    Texture homeTexture;
    ImageButton home;
    TextureRegionDrawable homeDrawable;

    Texture KDTexture;
    ImageButton KD;
    TextureRegionDrawable KDDrawable;

    Texture SCTexture;
    ImageButton SC;
    TextureRegionDrawable SCDrawable;

    Texture LBJTexture;
    ImageButton LBJ;
    TextureRegionDrawable LBJDrawable;

    Texture JTTexture;
    ImageButton JT;
    TextureRegionDrawable JTDrawable;

    Texture ManvirTexture;
    ImageButton Manvir;
    TextureRegionDrawable ManvirDrawable;

    Texture BrandonTexture;
    ImageButton Brandon;
    TextureRegionDrawable BrandonDrawable;

    Texture VishalTexture;
    ImageButton Vishal;
    TextureRegionDrawable VishalDrawable;

    Texture ButlerTexture;
    ImageButton Butler;
    TextureRegionDrawable ButlerDrawable;

    Texture CurrySelection;
    Texture ButlerSelection;
    Texture LebronSelection;
    Texture DurantSelection;
    Texture TatumSelection;
    Texture VishalSelection;
    Texture BrandonSelection;
    Texture ManvirSelection;

    boolean Currybool;
    boolean Brandonbool;
    boolean Manvirbool;
    boolean Vishalbool;
    boolean Tatumbool;
    boolean Durantbool;
    boolean Butlerbool;
    boolean Lebronbool;

    Texture Player1Texture;
    ImageButton Player1;
    TextureRegionDrawable Player1Drawable;

    Texture Player2Texture;
    ImageButton Player2;
    TextureRegionDrawable Player2Drawable;

    Sound sound;
    private BitmapFont statsFont;

    String [] selectionList = {"Stepth Curry", "Kevin Durant", "Lebron James", "Jimmy Butler", "Mavir", "Brandon", "Vishal", "Jayson Tatum"};
    String selected;
    String player1Selected;
    String player2Selected;


    
    public SelectionScreen(Main game) {
        this.game = game;
        selected = selectionList[0];
        //player1Selected = selectionList[0];
        //player2Selected = selectionList[0];
        sound = Gdx.audio.newSound(Gdx.files.internal("Music/button.wav"));
        backgroundTexture = new Texture(Gdx.files.internal("SelectionBackground.png"));
        spriteBatch = new SpriteBatch();
        viewport = new FitViewport(800, 500);
        stage = new Stage(viewport, spriteBatch);
        Gdx.input.setInputProcessor(this.stage);

        mysteryTexture = new Texture(Gdx.files.internal("6.jpg"));
        CurrySelection = new Texture(Gdx.files.internal("7.jpg"));
        BrandonSelection = new Texture(Gdx.files.internal("13.jpg"));
        ManvirSelection = new Texture(Gdx.files.internal("12.jpg"));
        VishalSelection = new Texture(Gdx.files.internal("14.jpg"));
        TatumSelection = new Texture(Gdx.files.internal("10.jpg"));
        DurantSelection = new Texture(Gdx.files.internal("11.jpg"));
        ButlerSelection = new Texture(Gdx.files.internal("8.jpg"));
        LebronSelection = new Texture(Gdx.files.internal("9.jpg"));

        Currybool = false;
        Brandonbool = false;
        Manvirbool = false;
        Vishalbool = false;
        Tatumbool = false;
        Durantbool = false;
        Butlerbool = false;
        Lebronbool = false;

        //----
        homeTexture = new Texture(Gdx.files.internal("homebutton.png"));
        homeDrawable = new TextureRegionDrawable(new TextureRegion(homeTexture));
        home = new ImageButton(homeDrawable); 
        home.setSize(80, 80);
        stage.addActor(home);
        home.addListener(new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            game.start();
            sound.play(1.5f);
        }
        });

        //----
        KDTexture = new Texture(Gdx.files.internal("KD1.png"));
        KDDrawable = new TextureRegionDrawable(new TextureRegion(KDTexture));
        KD = new ImageButton(KDDrawable);
        KD.setTransform(true);
        KD.setSize(180, 180);
        stage.addActor(KD);
        KD.addListener(new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
        Currybool = false;
        Brandonbool = false;
        Manvirbool = false;
        Vishalbool = false;
        Tatumbool = false;
        Durantbool = true;
        Butlerbool = false;
        Lebronbool = false;
        sound.play(1.5f);
        selected = selectionList[1];
            }
        });

        SCTexture = new Texture(Gdx.files.internal("Curry1.png"));
        SCDrawable = new TextureRegionDrawable(new TextureRegion(SCTexture));
        SC = new ImageButton(SCDrawable);
        SC.setTransform(true);
        SC.setSize(180, 180);
        stage.addActor(SC);
        SC.addListener(new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
        Currybool = true;
        Brandonbool = false;
        Manvirbool = false;
        Vishalbool = false;
        Tatumbool = false;
        Durantbool = false;
        Butlerbool = false;
        Lebronbool = false;
        sound.play(1.5f);
        selected = selectionList[0];
            }
        });

        LBJTexture = new Texture(Gdx.files.internal("LBJ1.png"));
        LBJDrawable = new TextureRegionDrawable(new TextureRegion(LBJTexture));
        LBJ = new ImageButton(LBJDrawable);
        LBJ.setTransform(true);
        LBJ.setSize(180, 180);
        stage.addActor(LBJ);
        LBJ.addListener(new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
        Currybool = false;
        Brandonbool = false;
        Manvirbool = false;
        Vishalbool = false;
        Tatumbool = false;
        Durantbool = false;
        Butlerbool = false;
        Lebronbool = true; 
        sound.play(1.5f);    
        selected = selectionList[2];   
    }
        });

        JTTexture = new Texture(Gdx.files.internal("Tatum1.png"));
        JTDrawable = new TextureRegionDrawable(new TextureRegion(JTTexture));
        JT = new ImageButton(JTDrawable);
        JT.setTransform(true);
        JT.setSize(180, 180);
        stage.addActor(JT);
        JT.addListener(new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
        Currybool = false;
        Brandonbool = false;
        Manvirbool = false;
        Vishalbool = false;
        Tatumbool = true;
        Durantbool = false;
        Butlerbool = false;
        Lebronbool = false;
        sound.play(1.5f);
        selected = selectionList[7];
            }
        });
        //--------------
        ButlerTexture = new Texture(Gdx.files.internal("Butler1.png"));
        ButlerDrawable = new TextureRegionDrawable(new TextureRegion(ButlerTexture));
        Butler = new ImageButton(ButlerDrawable);
        Butler.setTransform(true);
        Butler.setSize(180, 180);
        stage.addActor(Butler);
        Butler.addListener(new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
        Currybool = false;
        Brandonbool = false;
        Manvirbool = false;
        Vishalbool = false;
        Tatumbool = false;
        Durantbool = false;
        Butlerbool = true;
        Lebronbool = false;
        sound.play(1.5f);   
        selected = selectionList[3];     
        }
        });

        ManvirTexture = new Texture(Gdx.files.internal("Manvir1.png"));
        ManvirDrawable = new TextureRegionDrawable(new TextureRegion(ManvirTexture));
        Manvir = new ImageButton(ManvirDrawable);
        Manvir.setTransform(true);
        Manvir.setSize(180, 180);
        stage.addActor(Manvir);
        Manvir.addListener(new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
        Currybool = false;
        Brandonbool = false;
        Manvirbool = true;
        Vishalbool = false;
        Tatumbool = false;
        Durantbool = false;
        Butlerbool = false;
        Lebronbool = false;
    sound.play(1.5f);        
        selected = selectionList[4];

}
        });

        VishalTexture = new Texture(Gdx.files.internal("Vishal1.png"));
        VishalDrawable = new TextureRegionDrawable(new TextureRegion(VishalTexture));
        Vishal = new ImageButton(VishalDrawable);
        Vishal.setTransform(true);
        Vishal.setSize(180, 180);
        stage.addActor(Vishal);
        Vishal.addListener(new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
        Currybool = false;
        Brandonbool = false;
        Manvirbool = false;
        Vishalbool = true;
        Tatumbool = false;
        Durantbool = false;
        Butlerbool = false;
        Lebronbool = false;
    sound.play(1.5f);        
        selected = selectionList[6];

}
        });

        BrandonTexture = new Texture(Gdx.files.internal("Brandon1.png"));
        BrandonDrawable = new TextureRegionDrawable(new TextureRegion(BrandonTexture));
        Brandon = new ImageButton(BrandonDrawable);
        Brandon.setTransform(true);
        Brandon.setSize(180, 180);
        stage.addActor(Brandon);
        Brandon.addListener(new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
        Currybool = false;
        Brandonbool = true;
        Manvirbool = false;
        Vishalbool = false;
        Tatumbool = false;
        Durantbool = false;
        Butlerbool = false;
        Lebronbool = false;
    sound.play(1.5f);        
        selected = selectionList[5];

}
        });



        Player1Texture = new Texture(Gdx.files.internal("Player1.png"));
        Player1Drawable = new TextureRegionDrawable(new TextureRegion(Player1Texture));
        Player1 = new ImageButton(Player1Drawable);
        Player1.setTransform(true);
        Player1.setSize(160, 160);
        stage.addActor(Player1);
        Player1.addListener(new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
        sound.play(1.5f);
        player1Selected = selected;
        game.setPlayer1Selected(player1Selected);
        System.out.println("Player 1: " + player1Selected);
        }
        });



        Player2Texture = new Texture(Gdx.files.internal("Player2.png"));
        Player2Drawable = new TextureRegionDrawable(new TextureRegion(Player2Texture));
        Player2 = new ImageButton(Player2Drawable);
        Player2.setTransform(true);
        Player2.setSize(160, 160);
        stage.addActor(Player2);
        Player2.addListener(new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
        sound.play(1.5f);
        player2Selected = selected;
        game.setPlayer2Selected(player2Selected);
        System.out.println("Player 2: " + player2Selected);
        }
        });
        Gdx.input.setInputProcessor(stage);


    }
    
    @Override
    public void show() {
        // Prepare your screen here.
        statsFont = new BitmapFont();
        statsFont.getData().setScale(2f);
    }

    @Override
    public void render(float delta) {
        clear(Color.BLACK);


        // Let the stage draw and update the button
        stage.act(delta);
        stage.draw();
        draw();
        logic();
    }

    private void input() {

    }

    private void logic() {
        KD.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1) { // Only on mouse hover
                KD.addAction(Actions.scaleTo(1.05f, 1.05f, 0.05f, Interpolation.fade));
            }
        }
        @Override
        public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
            if (pointer == -1) {
                KD.addAction(Actions.scaleTo(1.0f, 1.0f, 0.1f, Interpolation.fade));
            }
        }
        });
        //----------
        SC.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1) { // Only on mouse hover
                SC.addAction(Actions.scaleTo(1.05f, 1.05f, 0.05f, Interpolation.fade));
            }
        }
        @Override
        public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
            if (pointer == -1) {
                SC.addAction(Actions.scaleTo(1.0f, 1.0f, 0.1f, Interpolation.fade));
            }
        }
        });
        //----------
        //----------
        LBJ.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1) { // Only on mouse hover
                LBJ.addAction(Actions.scaleTo(1.05f, 1.05f, 0.05f, Interpolation.fade));
            }
        }
        @Override
        public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
            if (pointer == -1) {
                LBJ.addAction(Actions.scaleTo(1.0f, 1.0f, 0.1f, Interpolation.fade));
            }
        }
        });
        //----------
        //----------
        JT.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1) { // Only on mouse hover
                JT.addAction(Actions.scaleTo(1.05f, 1.05f, 0.05f, Interpolation.fade));
            }
        }
        @Override
        public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
            if (pointer == -1) {
                JT.addAction(Actions.scaleTo(1.0f, 1.0f, 0.1f, Interpolation.fade));
            }
        }
        });
        //----------
        //----------
        Butler.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1) { // Only on mouse hover
                Butler.addAction(Actions.scaleTo(1.05f, 1.05f, 0.05f, Interpolation.fade));
            }
        }
        @Override
        public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
            if (pointer == -1) {
                Butler.addAction(Actions.scaleTo(1.0f, 1.0f, 0.1f, Interpolation.fade));
            }
        }
        });
        //----------
        //----------
        Manvir.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1) { // Only on mouse hover
                Manvir.addAction(Actions.scaleTo(1.05f, 1.05f, 0.05f, Interpolation.fade));
            }
        }
        @Override
        public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
            if (pointer == -1) {
                Manvir.addAction(Actions.scaleTo(1.0f, 1.0f, 0.1f, Interpolation.fade));
            }
        }
        });
        //----------
        //----------
        Brandon.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1) { // Only on mouse hover
                Brandon.addAction(Actions.scaleTo(1.05f, 1.05f, 0.05f, Interpolation.fade));
            }
        }
        @Override
        public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
            if (pointer == -1) {
                Brandon.addAction(Actions.scaleTo(1.0f, 1.0f, 0.1f, Interpolation.fade));
            }
        }
        });
        //----------
        //----------
        Vishal.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1) { // Only on mouse hover
                Vishal.addAction(Actions.scaleTo(1.05f, 1.05f, 0.05f, Interpolation.fade));
            }
        }
        @Override
        public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
            if (pointer == -1) {
                Vishal.addAction(Actions.scaleTo(1.0f, 1.0f, 0.1f, Interpolation.fade));
            }
        }
        });
        //----------
        //----------
        //----------

        Player1.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1) { // Only on mouse hover
                Player1.addAction(Actions.scaleTo(1.05f, 1.05f, 0.05f, Interpolation.fade));
            }
        }
        @Override
        public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
            if (pointer == -1) {
                Player1.addAction(Actions.scaleTo(1.0f, 1.0f, 0.1f, Interpolation.fade));
            }
        }
        });
        //---------
        Player2.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1) { // Only on mouse hover
                Player2.addAction(Actions.scaleTo(1.05f, 1.05f, 0.05f, Interpolation.fade));
            }
        }
        @Override
        public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
            if (pointer == -1) {
                Player2.addAction(Actions.scaleTo(1.0f, 1.0f, 0.1f, Interpolation.fade));
            }
        }
        });

    }

    private void draw() {
        clear(Color.BLACK);
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();
        spriteBatch.draw(backgroundTexture, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        spriteBatch.draw(mysteryTexture, 545, 0, 255, 500);

        if(Currybool) {
            spriteBatch.draw(CurrySelection, 545, 0, 255, 500);
        }
        if(Brandonbool) {
            spriteBatch.draw(BrandonSelection, 545, 0, 255, 500);
        }
        if(Manvirbool) {
            spriteBatch.draw(ManvirSelection, 545, 0, 255, 500);
        }
        if(Vishalbool) {
            spriteBatch.draw(VishalSelection, 545, 0, 255, 500);
        }
        if(Tatumbool) {
            spriteBatch.draw(TatumSelection, 545, 0, 255, 500);
        }
        if(Durantbool) {
            spriteBatch.draw(DurantSelection, 545, 0, 255, 500);
        }
        if(Butlerbool) {
            spriteBatch.draw(ButlerSelection, 545, 0, 255, 500);
        }
        if(Lebronbool) {
            spriteBatch.draw(LebronSelection, 545, 0, 255, 500);
        }   
        
        //----
        home.setPosition(10, viewport.getWorldHeight() - 90f);
        home.draw(spriteBatch, 1);

        Player1.draw(spriteBatch, 1);
        Player1.setPosition(300, -50);

        Player2.draw(spriteBatch, 1);
        Player2.setPosition(85, -50);

        KD.draw(spriteBatch, 1);
        KD.setPosition(-10f, 260);

        SC.draw(spriteBatch, 1);
        SC.setPosition(120, 260);

        LBJ.draw(spriteBatch, 1);
        LBJ.setPosition(250, 260);

        JT.draw(spriteBatch, 1);
        JT.setPosition(380, 260);

        Manvir.draw(spriteBatch, 1);
        Manvir.setPosition(-10f, 60);

        Vishal.draw(spriteBatch, 1);
        Vishal.setPosition(120, 60);

        Butler.draw(spriteBatch, 1);
        Butler.setPosition(250, 60);

        Brandon.draw(spriteBatch, 1);
        Brandon.setPosition(380, 60);

        spriteBatch.end();
    }

    private void drawCharacterStats() {
        if (selected == null) return;
        
        CharacterStats stats = CharacterStats.getStatsForCharacter(selected);
        float statsX = 560;
        float statsY = 400;
        
        spriteBatch.end(); // End batch for font rendering
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();
        
        statsFont.draw(spriteBatch, selected, statsX, statsY);
        statsY -= 40;
        
        statsFont.draw(spriteBatch, "Speed: " + String.format("%.1f", stats.speed), statsX, statsY);
        statsY -= 35;
        statsFont.draw(spriteBatch, "3PT: " + String.format("%.2f", stats.threePointAccuracy), statsX, statsY);
        statsY -= 35;
        statsFont.draw(spriteBatch, "Mid: " + String.format("%.2f", stats.midRangeAccuracy), statsX, statsY);
        statsY -= 35;
        statsFont.draw(spriteBatch, "Jump: " + String.format("%.1f", stats.jumpHeight), statsX, statsY);
        
        spriteBatch.end();
        spriteBatch.begin(); // Resume spritebatch for UI elements
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
        backgroundTexture.dispose();
        mysteryTexture.dispose();
        CurrySelection.dispose();
        BrandonSelection.dispose();
        ManvirSelection.dispose();
        VishalSelection.dispose();
        TatumSelection.dispose();
        DurantSelection.dispose();
        ButlerSelection.dispose();
        LebronSelection.dispose();
        homeTexture.dispose();
        KDTexture.dispose();
        SCTexture.dispose();
        LBJTexture.dispose();
        JTTexture.dispose();
        if (statsFont != null) {
            statsFont.dispose();
        }

    }
}