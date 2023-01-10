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
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.aueb.microwave.databinding.ActivityFoodBinding;

import java.util.ArrayList;

public class FoodActivity extends AppCompatActivity {
    ActivityFoodBinding binding;
    String mAnswer;
    TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_food);
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
        }
        else{
            startSpeechRecognizer();
            tts=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    tts.speak("Θα θέλατε να αποψύξετε, να ζεστάνετε, ή να μαγειρέψετε το φαγητό σας; " +
                            "Απαντήστε με απόψυξη, ή ζέσταμα, ή μαγείρεμα."+
                            "Αν χρειάζεστε βοήθεια, πείτε βοήθεια.", TextToSpeech.QUEUE_FLUSH,null,null);
                }
            });
        }
    }

    private void setUpClickListeners() {
        binding.DefrostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FoodActivity.this, DefrostActivity.class);
                mAnswer = "όχι";
                intent.putExtra("ANSWER",mAnswer);
                startActivity(intent);
            }
        });
        binding.WarmUpFoodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FoodActivity.this, WarmUpFoodActivity.class);
                mAnswer = "όχι";
                intent.putExtra("ANSWER",mAnswer);
                startActivity(intent);
            }
        });
        binding.CookingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FoodActivity.this, CookingActivity.class);
                mAnswer = "όχι";
                intent.putExtra("ANSWER",mAnswer);
                startActivity(intent);
            }
        });
        binding.information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {

                    }
                };
                DialogWindow.showDialog(view.getContext(),
                        "Πληροφορίες",
                        "Απόψυξη: \nΓια κατεψυγμένα προϊόντα, όπως ψάρια, κρέας και κοτόπουλο.\nΖέσταμα: \nΓια ζέσταμα" +
                                " οποιουδήποτε προμαγειρεμένου φαγητού.\nΜαγείρεμα: \nΓια μαγείρεμα λαχανικών, ψαριών, ρυζιού και μπριζόλας.",
                        "ΕΝΤΑΞΕΙ",
                        runnable);
            }
        });
        binding.backBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FoodActivity.this, FoodOrDrinkActivity.class);
                String mAnswer = "όχι";
                intent.putExtra("ANSWER",mAnswer);
                startActivity(intent);
                //finish();
            }
        });
    }


    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(FoodActivity.this, FoodOrDrinkActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
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
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Θα θέλατε να αποψύξετε, να ζεστάνετε ή να μαγειρέψετε το φαγητό σας;");
                startActivityForResult(intent, REQUEST_SPEECH_RECOGNIZER);
            }
        }, 15000);
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
                case "απόψυξη":
                    answer_id=1;
                    intent = new Intent(FoodActivity.this, DefrostActivity.class);
                    intent.putExtra("ANSWER", mAnswer);
                    startActivity(intent);
                    break;
                case "ζέσταμα" :
                    answer_id=2;
                    intent = new Intent(FoodActivity.this, WarmUpFoodActivity.class);
                    intent.putExtra("ANSWER", mAnswer);
                    startActivity(intent);
                    break;
                case "μαγείρεμα" :
                    answer_id=3;
                    intent = new Intent(FoodActivity.this,CookingActivity.class);
                    intent.putExtra("ANSWER", mAnswer);
                    startActivity(intent);
                    break;
                case "βοήθεια":
                    tts.speak("Πληροφορίες: Απόψυξη: Για κατεψυγμένα προϊόντα, όπως ψάρια, κρέας και κοτόπουλο. Ζέσταμα: Για ζέσταμα" +
                                    " οποιουδήποτε προμαγειρεμένου φαγητού. Μαγείρεμα: Για μαγείρεμα λαχανικών, ψαριών, ρυζιού και μπριζόλας.", TextToSpeech.QUEUE_FLUSH,null,null);

                    (new Handler()).postDelayed(this::restart, 13000);
                case "πίσω":
                    answer_id=3;
                    mAnswer = "ναι";
                    intent = new Intent(FoodActivity.this, FoodOrDrinkActivity.class);
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
