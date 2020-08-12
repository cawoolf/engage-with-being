package com.example.connectionwithbeing;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.prush.typedtextview.TypedTextView;

import model.Exercise;

 /*
 Self = 1
 Others = 2
 Nature = 3
 Society = 4
*/

public class ExerciseActivity extends AppCompatActivity {

    private LinearLayout mStartQuestions;
    private ImageView mHomeButton, mExerciseImage, mToDoButton;
    private TypedTextView mTypedTextView;
    private TextView mPlaceHolderTextView;
    private TextView mQuoteTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_linear);

        //Set Actionbar color
        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.nature_primary_dark)));

        //Get Intents all coming from the menu activity.
        Bundle extras = getIntent().getExtras();
        int exerciseImage = (int) extras.get(ExerciseMenuActivity.exerciseImageViewKey);
        int exerciseText = (int) extras.get(ExerciseMenuActivity.exerciseTextViewKey);
        int exerciseNumber = (int) extras.get(ExerciseMenuActivity.exerciseNumberKey);
        int exerciseType = (int) extras.get(ExerciseMenuActivity.exerciseCategoryKey);
//        int exerciseTitle = (int) extras.get(ExerciseMenuActivity.exerciseTitleKey);

        //Declare Views, and Set resources from extras.

//        setTitle(exerciseTitle);
//        mQuoteTextView = findViewById(R.id.ExerciseActivityQuote_TextView);
//        mQuoteTextView.setText(R.string.society_e6_quote);

        mExerciseImage = findViewById(R.id.ExerciseActivity_ImageView);
        mExerciseImage.setImageResource(exerciseImage);
//        mExerciseImage.setImageResource(R.drawable.guistar); //Just a test image to see if all the strings are working

        mTypedTextView = findViewById(R.id.ExerciseActivity_TypedTextView);
        mTypedTextView.setTypedText(exerciseText);

        mPlaceHolderTextView = findViewById(R.id.ExerciseActivity_TextView);
        mPlaceHolderTextView.setText(exerciseText);

        //Reflections Button
        mStartQuestions = findViewById(R.id.ExerciseStartQuestions_LinearLayout);
        mHomeButton = findViewById(R.id.ExerciseActivityHomeButton);
//        mToDoButton = findViewById(R.id.ExerciseActivityToDoButton);

        setExerciseTitle(exerciseType, exerciseNumber);
        setOnClickListeners(exerciseText, exerciseNumber, exerciseType);
        bottomNavButtonsListeners();

    }

    private void setOnClickListeners(int exerciseText, final int exerciseNumber, final int exerciseType) {


        mStartQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(ExerciseActivity.this)
                        .setTitle("Continue to reflections..")
                        .setMessage("Have you completed the exercise?")

                        .setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                //Need intents here with extras for the questions!
                                Intent startQuestions = new Intent(ExerciseActivity.this, QuestionActivity.class);
                                startQuestions.putExtra(ExerciseMenuActivity.exerciseNumberKey, exerciseNumber);
                                startQuestions.putExtra(ExerciseMenuActivity.exerciseCategoryKey, exerciseType);
                                startActivity(startQuestions);
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton("Take more time", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mStartQuestions.clearAnimation();

                            }
                        })
                        .setIcon(R.drawable.guistar)
                        .show();

            }
        });

        String mExerciseString = getString(exerciseText);
        final int mIndex = mExerciseString.length() -1;

        mTypedTextView.bringToFront(); //Needed for formatting the typed text. Otherwise gets lost in the background text.

        //Used for triggering actions when the text is done typing.
        mTypedTextView.setOnCharacterTypedListener(new TypedTextView.OnCharacterTypedListener() {
            @Override
            public void onCharacterTyped(char character, int index) {
                if(index == mIndex) {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            playBlinkAnimation();
//                            mQuoteTextView.setVisibility(View.VISIBLE);  //Used for causing the quote to appear on the exercise activity. Not really needed
                        }
                    }, 1750);

                }
            }
        });

    }

    private void setExerciseTitle(int exerciseType, int exerciseNumber) {

        switch(exerciseType) {
            case 1:
                setTitle(Exercise.selfExerciseTitles[exerciseNumber-1]);
                break;
            case 2:
                setTitle(Exercise.othersExerciseTitles[exerciseNumber-1]);
                break;
            case 3:
                setTitle(Exercise.natureExerciseTitles[exerciseNumber-1]);
                break;
            case 4:
                setTitle(Exercise.societyExerciseTitles[exerciseNumber-1]);
                break;
        }

    }

    private void playBlinkAnimation(){

        Animation animation1 =
                AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.blink);
        mStartQuestions.startAnimation(animation1);
    }

    private void bottomNavButtonsListeners() {
        mHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent returnHome = new Intent(ExerciseActivity.this, MainActivity.class);
                startActivity(returnHome);

            }
        });


    }
    //When back button on actionbar is pressed, returns to the previous activity which has not been destroyed.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Toast.makeText(getApplicationContext(),"Back button clicked", Toast.LENGTH_SHORT).show();
                super.onBackPressed(); //This replicates the hard back button on the phone. Could just replace this with an Intent
                break;

            case R.id.infoItem:
                Toast.makeText(getApplicationContext(), "Exercise Category: \n" + "Exercise Number: ", Toast.LENGTH_LONG).show();
        }
        return true;
    }

    //  Creates the right hand menu.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.exercise_menu, menu);
//        android.app.ActionBar actionBar = getActionBar();
//        actionBar.setIcon(R.drawable.guistar);



        return true;
    }

}

