package com.aueb.microwave;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.aueb.microwave.databinding.ActivityTimerBinding;

import java.util.ArrayList;
import java.util.Locale;

import static java.lang.Long.parseLong;

public class TimerActivity extends AppCompatActivity {
    ActivityTimerBinding binding;
    private CountDownTimer countDownTimer;
    private TextView timerText;
    private boolean timeRunning;
    private long TimeUntillFinished;
    String mAnswer;
    TextToSpeech tts;
    String timeMin;
    String timeSec;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_timer);
        ActionBar actionBar;
        actionBar = getSupportActionBar();

        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#F37C7B"));

        actionBar.setBackgroundDrawable(colorDrawable);
        //actionBar.setDisplayHomeAsUpEnabled(true);

        timerText =findViewById(R.id.timerText);
        Bundle extras = getIntent().getExtras();
        timeMin = extras.getString("TIME_MIN");
        timeSec = extras.getString("TIME_SEC");
        Long min=parseLong(timeMin)*60000;
        Long sec=parseLong(timeSec)*1000;
        TimeUntillFinished=min+sec;
        mAnswer= extras.getString("ANSWER");

        if (mAnswer.equals("όχι")){
            setUpClickListeners();
        }
        else{
            startSpeechRecognizer();
            tts=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    tts.speak("Αν θέλετε να σταματήσετε την λειτουργία του φούρνου μικροκυμάτων, πείτε ακύρωση, αλλιώς πείτε συνέχεια.", TextToSpeech.QUEUE_FLUSH,null,null);
                }
            });
        }

        countDownTimer = new CountDownTimer(TimeUntillFinished,1000) {
            @Override
            public void onTick(long l) {
                TimeUntillFinished=l;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timeRunning = false;

                Intent intent = new Intent(TimerActivity.this, ReadyActivity.class);
                if (!mAnswer.equals("συνέχεια")) {
                    mAnswer = "όχι";
                }
                intent.putExtra("ANSWER",mAnswer);
                startActivity(intent);
            }
        }.start();
        timeRunning = true;
        updateCountDownText();
    }
    /*private void startTimer(){
        countDownTimer = new CountDownTimer(TimeUntillFinished,1000) {
            @Override
            public void onTick(long l) {
                TimeUntillFinished=l;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timeRunning = false;
            }
        }.start();
        timeRunning = true;
    }*/

    private void updateCountDownText() {
        int minutes = (int) (TimeUntillFinished / 1000) / 60;
        int seconds = (int) (TimeUntillFinished / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        timerText.setText(timeLeftFormatted);
    }

    private void setUpClickListeners() {
        binding.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(TimerActivity.this)
                        .setTitle("ΑΚΥΡΩΣΗ").setMessage("Είστε σίγουροι ότι θέλετε να σταματήσετε την λειτουργία του φούρνου μικροκυμάτων;")
                        .setPositiveButton("ΝΑΙ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(TimerActivity.this, FoodOrDrinkActivity.class);
                                mAnswer = "όχι";
                                intent.putExtra("ANSWER",mAnswer);
                                startActivity(intent);
                                Toast.makeText(TimerActivity.this, "Διακοπή λειτουργίας",Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton("ΟΧΙ", null).show();

            }
        });
    }


    private void startSpeechRecognizer() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int REQUEST_SPEECH_RECOGNIZER = 3000;
                Intent intent = new Intent (RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "el");
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "el");
                intent.putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, "el");
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Αν θέλετε να σταματήσετε τον φούρνο μικροκυμάτων, πείτε ακύρωση, αλλιώς πείτε συνέχεια");
                startActivityForResult(intent, REQUEST_SPEECH_RECOGNIZER);
            }
        }, 6500);
    }
    private void endTimer(){
        countDownTimer.cancel();
    }
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("DEMO-REQUESTCODE", Integer.toString(requestCode));
        Log.i("DEMO-RESULTCODE", Integer.toString(resultCode));
        if (requestCode == 3000 && resultCode == Activity.RESULT_OK && data != null){
            ArrayList<String> text = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            mAnswer = text.get(0);
            Log.i("DEMO-ANSWER", text.get(0));
            int answer_id = 0;
            Intent intent;
            switch (mAnswer.toLowerCase()){
                case "ακύρωση" :
                    answer_id=1;
                    mAnswer = "ναι";
                    intent = new Intent(TimerActivity.this, FoodOrDrinkActivity.class);
                    intent.putExtra("ANSWER", mAnswer);
                    startActivity(intent);
                    break;
                case "συνέχεια" :
                    mAnswer = "συνέχεια";
                    answer_id=1;
                    break;
                default:
                    mAnswer = "συνέχεια";
                    answer_id=0;
                    tts.speak("Απαντήστε μόνο με τις λέξεις που αναφέρονται. Προσπαθήστε ξανά.", TextToSpeech.QUEUE_FLUSH,null,null);
                    (new Handler()).postDelayed(this::restart, 6000);
            }
            Log.i("DEMO_ID", Integer.toString(answer_id));
        }
        else{
            System.out.println("Recognizer API error");
        }
    }

    public void restart(){
        Intent intent;
        intent = getIntent();
        startActivity(intent);
    }

    public void onDestroy() {
        super.onDestroy();
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onPause();
    }
}
