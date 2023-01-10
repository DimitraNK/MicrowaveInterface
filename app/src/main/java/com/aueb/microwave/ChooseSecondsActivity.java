package com.aueb.microwave;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.aueb.microwave.databinding.ActivityChooseSecondsBinding;

import java.util.ArrayList;
import java.util.Arrays;

public class ChooseSecondsActivity extends AppCompatActivity{
    ActivityChooseSecondsBinding binding;
    private TextView timerText;
    String mAnswer;
    TextToSpeech tts;
    String timeMin;
    String timeSec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_choose_seconds);
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
        mAnswer= extras.getString("ANSWER");
        timerText.setText(timeMin+" : 00");

        startSpeechRecognizer();
        tts=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                tts.speak("Πείτε τον αριθμό των δευτερολέπτων."+
                        "Αν θέλετε να επιστρέψετε στις επιλογές φαγητού πείτε φαγητό."+
                        "Αν θέλετε να επιστρέψετε στις επιλογές του ροφήματος πείτε ρόφημα.", TextToSpeech.QUEUE_FLUSH,null,null);
            }});


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
        }, 10000);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("DEMO-REQUESTCODE", Integer.toString(requestCode));
        Log.i("DEMO-RESULTCODE", Integer.toString(resultCode));
        if (requestCode == 3000 && resultCode == Activity.RESULT_OK && data != null) {
            ArrayList<String> text = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            mAnswer = text.get(0);
            Log.i("DEMO-ANSWER", text.get(0));
            int answer_id = 0;
            Intent intent;
            switch (mAnswer.toLowerCase()) {
                case "φαγητό":
                    answer_id = 1;
                    mAnswer = "ναι";
                    intent = new Intent(ChooseSecondsActivity.this, FoodActivity.class);
                    intent.putExtra("ANSWER", mAnswer);
                    startActivity(intent);
                    break;
                case "ρόφημα":
                    answer_id = 2;
                    mAnswer = "ναι";
                    intent = new Intent(ChooseSecondsActivity.this, DrinkActivity.class);
                    intent.putExtra("ANSWER", mAnswer);
                    startActivity(intent);
                    break;
                default:
                    mAnswer = "ναι";
                    timeSec = arrayIndex(text.get(0));
                    answer_id = 0;
                    intent = new Intent(ChooseSecondsActivity.this, TimerActivity.class);

                    if (timeSec == "λάθος") {
                        tts.speak("Απαντήστε μόνο με τις λέξεις που αναφέρονται. Προσπαθήστε ξανά.", TextToSpeech.QUEUE_FLUSH, null, null);
                        (new Handler()).postDelayed(this::restart, 6000);
                    } else {
                        intent.putExtra("TIME_MIN", timeMin);
                        intent.putExtra("TIME_SEC", timeSec);
                        intent.putExtra("ANSWER", mAnswer);
                        startActivity(intent);
                    }
                    Log.i("DEMO_ID", Integer.toString(answer_id));
            }
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
    @RequiresApi(api = Build.VERSION_CODES.N)
    private String arrayIndex(String input){
        String[] numbers = new String[]{"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10",
                "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30",
                "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50",
                "51", "52", "53", "54", "55", "56", "57", "58", "59", "60"};
        String[] numbers_optimized = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
                "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30",
                "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50",
                "51", "52", "53", "54", "55", "56", "57", "58", "59", "60"};
        String[] strings = {"μηδέν", "ένα","δύο","τρία","τέσσερα","πέντε","έξι","εφτά","οχτώ","εννέα","δέκα","ένδεκα","δώδεκα","δεκατρία","δεκατέσσερα","δεκαπέντε","δεκαέξι","δεκαεπτά","δεκαοκτώ","δεκαεννιά","είκοσι","Είκοσι ένα",
                "Είκοσι δύο","Είκοσι τρία","Είκοσι τέσσερα","Είκοσι πέντε","Είκοσι έξι","Είκοσι επτά","Είκοσι οκτώ","Είκοσι εννέα","τριάντα","Τριάντα ένα",
                "Τριάντα δύο","Τριάντα τρία","Τριάντα τέσσερα","Τριάντα πέντε","Τριάντα έξι","Τριάντα επτά","Τριάντα οκτώ",
                "Τριάντα εννέα","σαράντα","Σαράντα ένα","Σαράντα δύο","Σαράντα τρεις","Σαράντα τέσσερα","Σαράντα πέντε",
                "Σαράντα έξι","Σαράντα επτά","Σαράντα οκτώ","Σαράντα εννέα","πενήντα", "Πενήντα ένα","Πενήντα δύο",
                "Πενήντα τρία","Πενήντα τέσσερις","Πενήντα πέντε","Πενήντα έξι","Πενήντα επτά","Πενήντα οκτώ",
                "Πενήντα εννέα","εξήντα"};
        if (Arrays.asList(strings).contains(input)) {
            int index = Arrays.asList(strings).indexOf(input);
            return numbers[index];
        }
        else if (Arrays.asList(numbers).contains(input)){
            int index = Arrays.asList(numbers).indexOf(input);
            return numbers[index];
        }
        else if (Arrays.asList(numbers_optimized).contains(input)) {
            int index = Arrays.asList(numbers_optimized).indexOf(input);
            return numbers[index];
        }
        return "λάθος";
    }

}



