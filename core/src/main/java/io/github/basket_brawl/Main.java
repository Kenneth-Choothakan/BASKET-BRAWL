package io.github.basket_brawl;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;


/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    int songSelection;
    Music music;
    Music song1;
    Music song2;
    Music song3;
    Music song4;
    Music song5;
    Music song6;
    Music song7;
    Music song8;
    Music song9;
    Music song10;
    Music song11;
    Music song12;
    Music song13;
    Music song14;
    Music song15;
    Music song16;
    Music song17;
    Music song18;
    Music song19;
    Music song20;
    Music song21;
    Music song22;
    Music song23;
    Music song24;
    Music song25;
    Music song26;
    Music song27;
    Music song28;
    Music song29;
    Music song30;
    Music[] songArray;

    @Override
    public void create() {
        start();

        song1 = Gdx.audio.newMusic(Gdx.files.internal("4X4.mp3"));
        song2 = Gdx.audio.newMusic(Gdx.files.internal("5TO10.mp3"));
        song3 = Gdx.audio.newMusic(Gdx.files.internal("CALL BACK.mp3"));
        song4 = Gdx.audio.newMusic(Gdx.files.internal("CARDIGAN.mp3"));
        song5 = Gdx.audio.newMusic(Gdx.files.internal("LASTLAUGH.mp3"));
        song6 = Gdx.audio.newMusic(Gdx.files.internal("LMM.mp3"));
        song7 = Gdx.audio.newMusic(Gdx.files.internal("LOH.mp3"));
        song8 = Gdx.audio.newMusic(Gdx.files.internal("SECONDHAND.mp3"));
        song9 = Gdx.audio.newMusic(Gdx.files.internal("LIE.mp3"));
        song10 = Gdx.audio.newMusic(Gdx.files.internal("CRY.mp3"));
        song11 = Gdx.audio.newMusic(Gdx.files.internal("REMINDER.mp3"));
        song12 = Gdx.audio.newMusic(Gdx.files.internal("COMING.mp3"));
        song13 = Gdx.audio.newMusic(Gdx.files.internal("SOMEONE.mp3"));
        song14 = Gdx.audio.newMusic(Gdx.files.internal("ORDINARY.mp3"));
        song15 = Gdx.audio.newMusic(Gdx.files.internal("HEARTLESS.mp3"));
        song16 = Gdx.audio.newMusic(Gdx.files.internal("HOB.mp3"));
        song17 = Gdx.audio.newMusic(Gdx.files.internal("ENJOY.mp3"));
        song18 = Gdx.audio.newMusic(Gdx.files.internal("NOKIA.mp3"));
        song19 = Gdx.audio.newMusic(Gdx.files.internal("FT.mp3"));
        song20 = Gdx.audio.newMusic(Gdx.files.internal("CV.mp3"));
        song21 = Gdx.audio.newMusic(Gdx.files.internal("OW.mp3"));
        song22 = Gdx.audio.newMusic(Gdx.files.internal("NB.mp3"));
        song23 = Gdx.audio.newMusic(Gdx.files.internal("SND.mp3"));
        song24 = Gdx.audio.newMusic(Gdx.files.internal("POWER.mp3"));
        song25 = Gdx.audio.newMusic(Gdx.files.internal("RUNNIN.mp3"));
        song26 = Gdx.audio.newMusic(Gdx.files.internal("SLIDIN.mp3"));
        song27 = Gdx.audio.newMusic(Gdx.files.internal("MD.mp3"));
        song28 = Gdx.audio.newMusic(Gdx.files.internal("JC.mp3"));
        song29 = Gdx.audio.newMusic(Gdx.files.internal("BOB.mp3"));
        song30 = Gdx.audio.newMusic(Gdx.files.internal("PM.mp3"));
        songArray = new Music[] {song1, song2, song3, song4, song5, song6, song7, song8, song9, song10, song11, song12, song14, song15, song16, song17, song18, song19, song20, song21, song22, song23, song24, song25, song26, song27, song28, song29, song30};
        songSelection = (int)(Math.random() * songArray.length);
        music = songArray[songSelection];
        //music.play();
    }

    @Override
    public void render() {
        playMusic();

        super.render(); 
    }

    private void playMusic(){
        // if(!(music.isPlaying())){
        //     songSelection = (int)(Math.random() * songArray.length);
        //     music = songArray[songSelection];
        //     music.play();
        // }
        // if(Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
        //     music.stop();
        //     songSelection = (int)(Math.random() * songArray.length);
        //     music = songArray[songSelection];
        //     music.play();
        // }
    }

    public void start(){
        setScreen(new StartScreen(this));
    }

    public void Selection(){
        setScreen(new SelectionScreen());
    }

    public void play(){
        setScreen(new FirstScreen());
    }
}