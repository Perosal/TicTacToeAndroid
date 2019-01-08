package com.frozenbrain.tictactoe;

import android.content.Context;
import android.media.MediaPlayer;

import java.util.Random;

public class AppBehavior {
    public boolean NORMAL_MODE = true;
    private Context context;
    private MediaPlayer Normal_Sound;
    private MediaPlayer Retard_Sound_1;
    private MediaPlayer Retard_Sound_2;
    private MediaPlayer Retard_Sound_3;
    private MediaPlayer Retard_Sound_4;
    private MediaPlayer Retard_Sound_5;
    private MediaPlayer Retard_Sound_6;
    Random random = new Random();


    public void declare(Context context)
    {
        this.context = context.getApplicationContext();
        Normal_Sound = MediaPlayer.create(context, R.raw.s_sip_regular_standard);
        Retard_Sound_1 = MediaPlayer.create(context, R.raw.wrong_sound);
        Retard_Sound_2 = MediaPlayer.create(context, R.raw.s_sip_regular_standard);
        Retard_Sound_3 = MediaPlayer.create(context, R.raw.wrong_sound);
        Retard_Sound_4 = MediaPlayer.create(context, R.raw.burp_sound);
        Retard_Sound_5 = MediaPlayer.create(context, R.raw.fart_soud);
        Retard_Sound_6 = MediaPlayer.create(context, R.raw.you_suck_sound);
    }
    public void makesound() {
        int n = random.nextInt(1000)%5 + 1;

        if (NORMAL_MODE)
            Normal_Sound.start();
        else {
            switch (n)
            {
                case 1:
                    Retard_Sound_1.start();
                    break;
                case 2:
                    Retard_Sound_2.start();
                    break;
                case 3:
                    Retard_Sound_3.start();
                    break;
                case 4:
                    Retard_Sound_4.start();
                    break;
                case 5:
                    Retard_Sound_5.start();
                    break;
                default:
                    Normal_Sound.start();
                    break;

            }
        }

    }
    public void normal_click_sound()
    {
        Normal_Sound.start();
    }
    public void lose()
    {
        Retard_Sound_6.start();
    }



}
