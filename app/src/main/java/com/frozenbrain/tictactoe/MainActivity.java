package com.frozenbrain.tictactoe;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import hotchemi.android.rate.AppRate;

import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity{
    // region GLOBAL DECLARATION
    public AppBehavior app = new AppBehavior();
    public Board b = new Board();
    //
    private ImageView imageView00;
    private ImageView imageView01;
    private ImageView imageView02;
    private ImageView imageView10;
    private ImageView imageView11;
    private ImageView imageView12;
    private ImageView imageView20;
    private ImageView imageView21;
    private ImageView imageView22;
    //INITIAL STATE FOR PLAYING WITH A HUMMAN OR A GIRL
    private boolean PLAY_WITH_AI = false;
    private String PLAYER_1 = "PLAYER 1";
    private String PLAYER_2 = "PLAYER 2";
    //THE CONTOR FOR KNOWIND WICH PLAYER MOVE k = true P1 else P2
    private boolean k = true;
    //
    private Button RESET;
    private Button PLAYER;
    private Button AI;
    private Button RETARD_MODE;
    private Button NORMAL_MODE;
    private Button GET_IMAGE_PLAYER_1;
    private Button GET_IMAGE_PLAYER_2;
    public ImageView image;

    private Bitmap PLAYER_1_IMAGE;
    private Bitmap PLAYER_2_IMAGE;

    private boolean GET_P1_BTN_PRESSED = false;


    // endregion
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // region Some Stuff
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // region DECLARATION

        RESET = findViewById(R.id.Reset);
        PLAYER = findViewById(R.id.Player);
        AI = findViewById(R.id.AI);
        RETARD_MODE = findViewById(R.id.Retard_Mode);
        NORMAL_MODE = findViewById(R.id.Normal_Mode);
        GET_IMAGE_PLAYER_1 = findViewById(R.id.get_image_player_1);
        GET_IMAGE_PLAYER_2 = findViewById(R.id.get_image_player_2);

        imageView00 = findViewById(R.id.image00);
        imageView01 = findViewById(R.id.image01);
        imageView02 = findViewById(R.id.image02);
        imageView10 = findViewById(R.id.image10);
        imageView11 = findViewById(R.id.image11);
        imageView12 = findViewById(R.id.image12);
        imageView20 = findViewById(R.id.image20);
        imageView21 = findViewById(R.id.image21);
        imageView22 = findViewById(R.id.image22);

        app.declare(this);

        PLAYER_1_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.trump);
        PLAYER_2_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.putin);

        // endregion

        //KEEP SCREEN ON
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // region GET_PLAYER_NAME
        //endregion

        // region PLAYER BUTTON
        PLAYER.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();
                PLAY_WITH_AI = false;
                app.NORMAL_MODE = true;
                app.normal_click_sound();

        }
        });
        // endregion

        // region RESET BUTTON
        RESET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();
                if(PLAY_WITH_AI)
                    aiMove();
                else
                    app.normal_click_sound();


            }
        });
        // endregion

        // region AI BUTTON
        AI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //FIRST BEGIN AI
                resetGame();
                PLAY_WITH_AI = true;
                aiMove();
            }
        });
        // endregion

        // region RETARD_MODE BUTTON
        RETARD_MODE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();
                PLAY_WITH_AI = false;
                app.normal_click_sound();
                app.NORMAL_MODE = false;
                GET_IMAGE_PLAYER_1.setVisibility(View.VISIBLE);
                GET_IMAGE_PLAYER_2.setVisibility(View.VISIBLE);
            //make changes for retard mode
            }
        });
        //endregion

        // region NORMAL MODE
        NORMAL_MODE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();
                PLAY_WITH_AI = false;
                app.NORMAL_MODE = true;
                app.normal_click_sound();
                GET_IMAGE_PLAYER_1.setVisibility(View.GONE);
                GET_IMAGE_PLAYER_2.setVisibility(View.GONE);
            }
        });

        // endregion

        // region GET IMAGE P1
        GET_IMAGE_PLAYER_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GET_P1_BTN_PRESSED = true;
                checkAndroidVersion();
            }
        });
        // endregion

        // region GET IMAGE P2
        GET_IMAGE_PLAYER_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });
        // endregion


        //App Rate
        AppRate.with(this)
                .setInstallDays(0)
                .setLaunchTimes(3)
                .setRemindInterval(2)
                .monitor();

        AppRate.showRateDialogIfMeetsConditions(this);
    }

    public void getText(View view){
        String player_1 = ((EditText)findViewById(R.id.Player_One)).getText().toString();
        String player_2 = ((EditText)findViewById(R.id.Player_Two)).getText().toString();
        if(player_1.length()>0)
            PLAYER_1 = player_1;
        else
            PLAYER_1 = "PLAYER 1";
        if(player_2.length()>0)
            PLAYER_2 = player_2;
        else
            PLAYER_2 = "PLAYER 2";
    }

    // region THE WHOLE MESS

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 555 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            pickImage();
        else
            checkAndroidVersion();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        //RESULT FROM SELECTED IMAGE
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);
            cropRequest(imageUri);
        }

        //RESULT FROM CROPPING ACTIVITY
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), result.getUri());

                    //((ImageView)findViewById(R.id.imageTest)).setImageBitmap(bitmap);

                    if(GET_P1_BTN_PRESSED) {
                        PLAYER_1_IMAGE = bitmap;
                        GET_P1_BTN_PRESSED = false;
                    }
                    else
                        PLAYER_2_IMAGE = bitmap;
                    resetGame();


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void pickImage() {
        CropImage.startPickImageActivity(this);
    }

    private void cropRequest(Uri imageUri){
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .setAspectRatio(1, 1)
                .start(this);
    }

    private void checkAndroidVersion(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 555);
            } catch (Exception e){
                e.printStackTrace();
            }
        } else {
            pickImage();
        }
    }

    // endregion
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case R.id.about:
                openAbout();
                break;
            default:
                return super.onOptionsItemSelected(item);

        }

        return super.onOptionsItemSelected(item);
    }


    public void openAbout()
    {
        Intent intent = new Intent(this,AboutActivity.class);
        startActivity(intent);
    }

    public void MakeAMove(View view)
    {

        String id = view.getResources().getResourceEntryName(view.getId());
        int x = Character.getNumericValue(id.charAt(4));
        int y = Character.getNumericValue(id.charAt(5));
        //DECLARE A POINT FOR MOVE
        Point point = new Point(x, y);
        //I DECIDE TO MAKE USER VS USER SEPARATELY ----
        if(b.board[x][y]==0&&!b.isGameOver()&&PLAY_WITH_AI) {
            //PLAYER MOVE
            b.placeAMove(point, Board.PLAYER_0);
            app.makesound();
            //PUT X
            if(app.NORMAL_MODE)
                Glide.with(this).load(R.drawable.o).into(findImageId(x, y));
            else
                Glide.with(this).load(PLAYER_2_IMAGE).into(findImageId(x, y));

            //HERE ONLY WHEN USER PUTS HIS MOVE AND THE GAME IS NOT OVER "aiMove()" IS CALLED
            if(PLAY_WITH_AI&&!b.isGameOver()) {
                aiMove();
            }
            else if(PLAY_WITH_AI){
                Toast.makeText(this, "You Win", Toast.LENGTH_SHORT).show();
            }
        }


        //HERE I MAKE USER VS USER SEPARATELY
        if(!PLAY_WITH_AI&&!b.isGameOver()&&b.board[x][y]==0)
        {


            if(k) {
                b.placeAMove(point, Board.PLAYER_0);
                app.makesound();
                if(app.NORMAL_MODE)
                    Glide.with(this).load(R.drawable.x).into(findImageId(x, y));
                else
                    Glide.with(this).load(PLAYER_1_IMAGE).into(findImageId(x, y));
                k=false;
            }
            else
            {
                b.placeAMove(point, Board.PLAYER_X);
                app.makesound();
                if(app.NORMAL_MODE)
                    Glide.with(this).load(R.drawable.o).into(findImageId(x, y));
                else
                    Glide.with(this).load(PLAYER_2_IMAGE).into(findImageId(x, y));
                k=true;
            }
            //view.setStateListAnimator(AnimatorInflater.loadStateListAnimator(MainActivity.this, R.animator.animate_red));

            if(b.isGameOver())
            {
                 if(b.hasPlayerWon(Board.PLAYER_X)){
                     if(!PLAY_WITH_AI&&!app.NORMAL_MODE)
                         app.lose();
                    Toast.makeText(this, PLAYER_2 + " Win", Toast.LENGTH_SHORT).show();
                }
                else if(b.hasPlayerWon(Board.PLAYER_0)) {
                     Toast.makeText(this, PLAYER_1 + " Win", Toast.LENGTH_SHORT).show();
                     if (!PLAY_WITH_AI&&!app.NORMAL_MODE)
                         app.lose();
                 }
                else
                     Toast.makeText(this, "DRAW", Toast.LENGTH_SHORT).show();

            }

        }



    }
    //==--Find Image ID Based On The CardView Position
    private ImageView findImageId(int x,int y)
    {
        switch (x)
        {
            case 0:
                switch (y)
                {
                    case 0:
                        return imageView00;
                    case 1:
                        return imageView01;
                    case 2:
                        return imageView02;
                    default:
                        break;
                }
                break;
            case 1:
                switch (y)
                {
                    case 0:
                        return imageView10;
                    case 1:
                        return imageView11;
                    case 2:
                        return imageView12;
                    default:
                        break;
                }
                break;
            case 2:
                switch (y)
                {
                    case 0:
                        return imageView20;
                    case 1:
                        return imageView21;
                    case 2:
                        return imageView22;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
        return image;

    }
    //==--Find Card ID Based On The CardView Position
    private void aiMove()
    {
        b.minimax(0, Board.PLAYER_X);

        int x = b.computerMove.x;
        int y = b.computerMove.y;

        b.placeAMove(b.computerMove, Board.PLAYER_X);
        if(PLAY_WITH_AI){
            if(app.NORMAL_MODE)
                Glide.with(this).load(R.drawable.x).into(findImageId(x, y));
            else
                Glide.with(this).load(PLAYER_1_IMAGE).into(findImageId(x, y));
        }
        else {
            if(app.NORMAL_MODE)
                Glide.with(this).load(R.drawable.o).into(findImageId(x, y));
            else
                Glide.with(this).load(PLAYER_2_IMAGE).into(findImageId(x, y));
        }

        if(b.isGameOver())
        {
            if(!b.hasPlayerWon(Board.PLAYER_X)&&!b.hasPlayerWon(Board.PLAYER_0))
            {
                Toast.makeText(this, "DRAW", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this, "You Lose", Toast.LENGTH_SHORT).show();
            }
        }

        app.makesound();

    }

    private void resetGame()
    {

        k = true;
        b.resetBoard();
        imageView00.setImageDrawable(null);
        imageView01.setImageDrawable(null);
        imageView02.setImageDrawable(null);
        imageView10.setImageDrawable(null);
        imageView11.setImageDrawable(null);
        imageView12.setImageDrawable(null);
        imageView20.setImageDrawable(null);
        imageView21.setImageDrawable(null);
        imageView22.setImageDrawable(null);


    }

}