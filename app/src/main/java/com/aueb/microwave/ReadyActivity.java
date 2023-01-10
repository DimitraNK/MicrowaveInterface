package com.aueb.microwave;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.aueb.microwave.databinding.ActivityReadyBinding;

import java.util.ArrayList;

public class ReadyActivity extends AppCompatActivity {
    ActivityReadyBinding binding;
    String mAnswer;
    TextToSpeech tts;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_ready);
        ActionBar actionBar;
        actionBar = getSupportActionBar();

        // Define ColorDrawable object and parse color
        // using parseColor method
        // with color hash code as its parameter
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#F37C7B"));

        // Set BackgroundDrawable
        actionBar.setBackgroundDrawable(colorDrawable);
        //actionBar.setHomeButtonEnabled(true);
        Bundle extras = getIntent().getExtras();
        mAnswer= extras.getString("ANSWER");
        if (mAnswer.equals("όχι")){
            setUpClickListeners();
        }
        else{
            startSpeechRecognizer();
            tts=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    tts.speak("Το φαγητό είναι έτοιμο. Αν θέλετε να επιστρέψετε στην αρχική, απαντήστε με επιστροφή", TextToSpeech.QUEUE_FLUSH,null,null);
                }
            });
        }

    }

    private void setUpClickListeners() {
        binding.ReturnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReadyActivity.this, FoodOrDrinkActivity.class);
                mAnswer = "όχι";
                intent.putExtra("ANSWER",mAnswer);
                startActivity(intent);
                //finish();
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
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Το φαγητό είναι έτοιμο. Αν θέλετε να επιστρέψετε στην αρχική, απαντήστε με επιστροφή");
                startActivityForResult(intent, REQUEST_SPEECH_RECOGNIZER);
            }
        }, 8000);
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
                case "επιστροφή":
                    answer_id=1;
                    mAnswer="ναι";
                    intent = new Intent(ReadyActivity.this, FoodOrDrinkActivity.class);
                    intent.putExtra("ANSWER", mAnswer);
                    startActivity(intent);
                    break;
                default:
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
