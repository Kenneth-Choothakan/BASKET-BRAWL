package io.github.basket_brawl;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
    Music song31;
    Music song32;
    Music song33;
    Music song34;
    Music song35;
    Music song36;
    Music song37;
    Music song38;
    Music song39;
    Music song40;
    Music song41;
    Music song42;
    Music song43;
    Music song44;
    Music song45;
    Music song46;
    Music song47;
    Music song48;

    boolean musicStarted;
    Music[] songArray;
    private String player1Selected = "Stepth Curry";
    private String player2Selected = "Stepth Curry";
    private CharacterStats player1Stats = CharacterStats.getStatsForCharacter("Stepth Curry");
    private CharacterStats player2Stats = CharacterStats.getStatsForCharacter("Stepth Curry");

    @Override
    public void create() {
        start();

        song1 = Gdx.audio.newMusic(Gdx.files.internal("Music/4X4.mp3"));
        song2 = Gdx.audio.newMusic(Gdx.files.internal("Music/5TO10.mp3"));
        song3 = Gdx.audio.newMusic(Gdx.files.internal("Music/CALL BACK.mp3"));
        song4 = Gdx.audio.newMusic(Gdx.files.internal("Music/CARDIGAN.mp3"));
        song5 = Gdx.audio.newMusic(Gdx.files.internal("Music/LASTLAUGH.mp3"));
        song6 = Gdx.audio.newMusic(Gdx.files.internal("Music/LMM.mp3"));
        song7 = Gdx.audio.newMusic(Gdx.files.internal("Music/LOH.mp3"));
        song8 = Gdx.audio.newMusic(Gdx.files.internal("Music/SECONDHAND.mp3"));
        song9 = Gdx.audio.newMusic(Gdx.files.internal("Music/LIE.mp3"));
        song10 = Gdx.audio.newMusic(Gdx.files.internal("Music/CRY.mp3"));
        song11 = Gdx.audio.newMusic(Gdx.files.internal("Music/REMINDER.mp3"));
        song12 = Gdx.audio.newMusic(Gdx.files.internal("Music/COMING.mp3"));
        song13 = Gdx.audio.newMusic(Gdx.files.internal("Music/SOMEONE.mp3"));
        song14 = Gdx.audio.newMusic(Gdx.files.internal("Music/ORDINARY.mp3"));
        song15 = Gdx.audio.newMusic(Gdx.files.internal("Music/HEARTLESS.mp3"));
        song16 = Gdx.audio.newMusic(Gdx.files.internal("Music/HOB.mp3"));
        song17 = Gdx.audio.newMusic(Gdx.files.internal("Music/ENJOY.mp3"));
        song18 = Gdx.audio.newMusic(Gdx.files.internal("Music/NOKIA.mp3"));
        song19 = Gdx.audio.newMusic(Gdx.files.internal("Music/FT.mp3"));
        song20 = Gdx.audio.newMusic(Gdx.files.internal("Music/CV.mp3"));
        song21 = Gdx.audio.newMusic(Gdx.files.internal("Music/OW.mp3"));
        song22 = Gdx.audio.newMusic(Gdx.files.internal("Music/NB.mp3"));
        song23 = Gdx.audio.newMusic(Gdx.files.internal("Music/SND.mp3"));
        song24 = Gdx.audio.newMusic(Gdx.files.internal("Music/POWER.mp3"));
        song25 = Gdx.audio.newMusic(Gdx.files.internal("Music/RUNNIN.mp3"));
        song26 = Gdx.audio.newMusic(Gdx.files.internal("Music/SLIDIN.mp3"));
        song27 = Gdx.audio.newMusic(Gdx.files.internal("Music/MD.mp3"));
        song28 = Gdx.audio.newMusic(Gdx.files.internal("Music/JC.mp3"));
        song29 = Gdx.audio.newMusic(Gdx.files.internal("Music/BOB.mp3"));
        song30 = Gdx.audio.newMusic(Gdx.files.internal("Music/PM.mp3"));

        song31 = Gdx.audio.newMusic(Gdx.files.internal("Music/Chinese.mp3"));
        song32 = Gdx.audio.newMusic(Gdx.files.internal("Music/Luther.mp3"));
        song33 = Gdx.audio.newMusic(Gdx.files.internal("Music/Stars.mp3"));
        song34 = Gdx.audio.newMusic(Gdx.files.internal("Music/ATS.mp3"));
        song35 = Gdx.audio.newMusic(Gdx.files.internal("Music/Redevous.mp3"));
        song36 = Gdx.audio.newMusic(Gdx.files.internal("Music/2AM.mp3"));
        song37 = Gdx.audio.newMusic(Gdx.files.internal("Music/E85.mp3"));
        song38 = Gdx.audio.newMusic(Gdx.files.internal("Music/ATM.mp3"));
        song39 = Gdx.audio.newMusic(Gdx.files.internal("Music/LYM.mp3"));
        song40 = Gdx.audio.newMusic(Gdx.files.internal("Music/Company.mp3"));
        song41 = Gdx.audio.newMusic(Gdx.files.internal("Music/FWU.mp3"));
        song42 = Gdx.audio.newMusic(Gdx.files.internal("Music/Time.mp3"));
        song43 = Gdx.audio.newMusic(Gdx.files.internal("Music/HIS.mp3"));
        song44 = Gdx.audio.newMusic(Gdx.files.internal("Music/You.mp3"));
        song45 = Gdx.audio.newMusic(Gdx.files.internal("Music/Kryptonite.mp3"));
        song46 = Gdx.audio.newMusic(Gdx.files.internal("Music/SPACE.mp3"));
        song47 = Gdx.audio.newMusic(Gdx.files.internal("Music/FAMILY.mp3"));
        song48 = Gdx.audio.newMusic(Gdx.files.internal("Music/GLOCK.mp3"));  

        songArray = new Music[] {song1, song2, song3, song4, song5, song6, song7, song8, song9, song10, song11, song12, song14, song15, song16, song17, song18, song19, song20, song21, song22, song23, song24, song25, song26, song27, song28, song29, song30, song31, song32, song33, song34, song35, song36, song37, song38, song39, song40, song41, song42, song43, song44, song45, song46, song47, song48};
        //songSelection = (int)(Math.random() * songArray.length);
        songSelection = (int)(Math.random() * songArray.length);
        music = songArray[songSelection];
        music.play();
    }

    @Override
    public void render() {
        playMusic();

        super.render(); 
    }

    private void playMusic(){
        if(!(music.isPlaying())){
            songSelection = (int)(Math.random() * songArray.length);
            music = songArray[songSelection];
            music.play();
        }
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            music.stop();
            songSelection = (int)(Math.random() * songArray.length);
            music = songArray[songSelection];
            music.play();
        }
    }

    public void start(){
        setScreen(new StartScreen(this));
    }

    public void Selection(){
        setScreen(new SelectionScreen(this));
    }

    public void play(){
        setScreen(new FirstScreen(this, player1Selected, player2Selected));
    }

    public String getPlayer1Selected() {
        return player1Selected;
    }

    public void setPlayer1Selected(String player1Selected) {
        this.player1Selected = player1Selected;
        this.player1Stats = CharacterStats.getStatsForCharacter(player1Selected);
    }

    public String getPlayer2Selected() {
        return player2Selected;
    }

    public void setPlayer2Selected(String player2Selected) {
        this.player2Selected = player2Selected;
        this.player2Stats = CharacterStats.getStatsForCharacter(player2Selected);
    }

    public CharacterStats getPlayer1Stats() {
        return player1Stats;
    }

    public CharacterStats getPlayer2Stats() {
        return player2Stats;
    }
}