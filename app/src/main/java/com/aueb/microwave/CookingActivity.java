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
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.aueb.microwave.databinding.ActivityCookingBinding;

import java.util.ArrayList;
import java.util.Arrays;

public class CookingActivity extends AppCompatActivity {
    ActivityCookingBinding binding;
    String mAnswer;
    TextToSpeech tts;
    String timeMin = "00";
    String timeSec = "00";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_cooking);
        ActionBar actionBar;
        actionBar = getSupportActionBar();

        // Define ColorDrawable object and parse color
        // using parseColor method
        // with color hash code as its parameter
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#F37C7B"));

        // Set BackgroundDrawable
        actionBar.setBackgroundDrawable(colorDrawable);
        //actionBar.setDisplayHomeAsUpEnabled(true);
        Bundle extras = getIntent().getExtras();
        mAnswer= extras.getString("ANSWER");
        if (mAnswer.equals("όχι")){
            setUpClickListeners();
            String m = spinnerMin();
            String s = spinnerSec();
            getTimes(m,s);
        }
        else{
            startSpeechRecognizer();
            tts=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    tts.speak("Τι θα θέλατε να μαγειρέψετε; Απαντήστε με λαχανικά ή ρύζι ή μπριζόλα ή ψάρι."+
                            "Αν θέλετε διαφορετικό χρόνο πείτε τον αριθμό των λεπτών", TextToSpeech.QUEUE_FLUSH,null,null);
                }
            });
        } //spinners
    }
    private void setUpClickListeners() {
        binding.CookMeatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CookingActivity.this, TimerActivity.class);
                timeMin = "13";
                timeSec = "00";
                mAnswer = "όχι";
                intent.putExtra("ANSWER",mAnswer);
                intent.putExtra("TIME_MIN", timeMin);
                intent.putExtra("TIME_SEC", timeSec);
                startActivity(intent);
                //finish();
            }
        });
        binding.CookFishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CookingActivity.this, TimerActivity.class);
                timeMin = "12";
                timeSec = "00";
                mAnswer = "όχι";
                intent.putExtra("ANSWER",mAnswer);
                intent.putExtra("TIME_MIN", timeMin);
                intent.putExtra("TIME_SEC", timeSec);
                startActivity(intent);
                //finish();
            }
        });
        binding.CookRiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CookingActivity.this, TimerActivity.class);
                timeMin = "10";
                timeSec = "00";
                mAnswer = "όχι";
                intent.putExtra("ANSWER",mAnswer);
                intent.putExtra("TIME_MIN", timeMin);
                intent.putExtra("TIME_SEC", timeSec);
                startActivity(intent);
                //finish();
            }
        });
        binding.CookVegetablesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CookingActivity.this, TimerActivity.class);
                timeMin = "04";
                timeSec = "00";
                mAnswer = "όχι";
                intent.putExtra("ANSWER",mAnswer);
                intent.putExtra("TIME_MIN", timeMin);
                intent.putExtra("TIME_SEC", timeSec);
                startActivity(intent);
                //finish();
            }
        });
        binding.timerTick.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CookingActivity.this, TimerActivity.class);
                mAnswer = "όχι";
                intent.putExtra("ANSWER",mAnswer);
                intent.putExtra("TIME_MIN", timeMin);
                intent.putExtra("TIME_SEC", timeSec);
                startActivity(intent);
                //finish();
            }
        });
        binding.backBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CookingActivity.this, FoodActivity.class);
                String mAnswer = "όχι";
                intent.putExtra("ANSWER",mAnswer);
                intent.putExtra("TIME_MIN", timeMin);
                intent.putExtra("TIME_SEC", timeSec);
                startActivity(intent);
                //finish();
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(CookingActivity.this, FoodActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

    private String spinnerMin() {
        Spinner dropdownMin = findViewById(R.id.spinnerMinutes);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.time,android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdownMin.setAdapter(adapter);
        dropdownMin.setOnItemSelectedListener ( new AdapterView.OnItemSelectedListener () {
            @Override
            public  void onItemSelected (AdapterView <?> Parent, View view, int position, long id) {
                timeMin = (String) Parent.getSelectedItem ();
            }

            @Override
            public  void onNothingSelected (AdapterView <?> Parent) {
                // dummy
            }
        });
        return timeMin;
    }
    private String spinnerSec() {
        Spinner dropdownSec = findViewById(R.id.spinnerSeconds);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.time,android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdownSec.setAdapter(adapter);
        dropdownSec.setOnItemSelectedListener ( new AdapterView.OnItemSelectedListener () {
            @Override
            public  void onItemSelected (AdapterView <?> Parent, View view, int position, long id) {
                timeSec = (String) Parent.getSelectedItem ();
            }

            @Override
            public  void onNothingSelected (AdapterView <?> Parent) {
                // dummy
            }
        });
        return timeSec;
    }

    public void getTimes(String timeMin, String timeSec){
        if ((timeMin != "00") && (timeSec!="00")) {
            Intent intent = new Intent(CookingActivity.this, TimerActivity.class);
            mAnswer = "όχι";
            intent.putExtra("ANSWER",mAnswer);
            intent.putExtra("TIME_MIN", timeMin);
            intent.putExtra("TIME_SEC", timeSec);
            startActivity(intent);
            //finish();
        }
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
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Τι θα θέλατε να μαγειρέψετε;");
                startActivityForResult(intent, REQUEST_SPEECH_RECOGNIZER);
            }
        }, 8000);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
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
                case "λαχανικά" :
                    answer_id=1;
                    timeMin = "04";
                    timeSec = "00";
                    intent = new Intent(CookingActivity.this, TimerActivity.class);
                    intent.putExtra("ANSWER", mAnswer);
                    intent.putExtra("TIME_MIN",timeMin);
                    intent.putExtra("TIME_SEC",timeSec);
                    startActivity(intent);
                    break;
                case "ρύζι" :
                    answer_id=2;
                    timeMin = "10";
                    timeSec = "00";
                    intent = new Intent(CookingActivity.this, TimerActivity.class);
                    intent.putExtra("ANSWER", mAnswer);
                    intent.putExtra("TIME_MIN",timeMin);
                    intent.putExtra("TIME_SEC",timeSec);
                    startActivity(intent);
                    break;
                case "μπριζόλα" :
                    answer_id=3;
                    timeMin = "13";
                    timeSec = "00";
                    intent = new Intent(CookingActivity.this, TimerActivity.class);
                    intent.putExtra("ANSWER", mAnswer);
                    intent.putExtra("TIME_MIN",timeMin);
                    intent.putExtra("TIME_SEC",timeSec);
                    startActivity(intent);
                    break;
                case "ψάρι" :
                    answer_id=4;
                    timeMin = "12";
                    timeSec = "00";
                    intent = new Intent(CookingActivity.this, TimerActivity.class);
                    intent.putExtra("ANSWER", mAnswer);
                    intent.putExtra("TIME_MIN",timeMin);
                    intent.putExtra("TIME_SEC",timeSec);
                    startActivity(intent);
                    break;
                case "πίσω":
                    answer_id=3;
                    mAnswer = "ναι";
                    intent = new Intent(CookingActivity.this, FoodActivity.class);
                    intent.putExtra("ANSWER", mAnswer);
                    startActivity(intent);
                    break;
                default:
                    timeMin = arrayIndex(text.get(0));
                    answer_id=0;
                    timeSec = "00";
                    intent = new Intent(CookingActivity.this, ChooseSecondsActivity.class);
                    if (timeMin == "λάθος"){
                        tts.speak("Απαντήστε μόνο με τις λέξεις που αναφέρονται. Προσπαθήστε ξανά.", TextToSpeech.QUEUE_FLUSH,null,null);
                        (new Handler()).postDelayed(this::restart, 6000);
                    }
                    else {
                        intent.putExtra("TIME_MIN",timeMin);
                        intent.putExtra("TIME_SEC",timeSec);
                        intent.putExtra("ANSWER", mAnswer);
                        startActivity(intent);
                    }
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
