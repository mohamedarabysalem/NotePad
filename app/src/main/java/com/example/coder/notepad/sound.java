package com.example.coder.notepad;

import android.content.ActivityNotFoundException;
import android.content.ClipboardManager;
import android.content.Intent;
import android.database.Cursor;
import android.speech.RecognizerIntent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class sound extends ActionBarActivity implements AdapterView.OnItemSelectedListener
{


    protected static final int RESULT_SPEECH = 1;

    private EditText Text;
    private Button Copy, Share, Speak;
    String copy_content;
    public static String curDate = "";
    private Cursor note;
    EditText mTitleText,Title;
    public String item="ar";
    ArrayList<String> arrayList = new ArrayList<String>();


    private NotesDbAdapter mDbHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound);

        Text = (EditText) findViewById(R.id.txtText);
        Speak = (Button) findViewById(R.id.Speak);
        Copy = (Button) findViewById(R.id.copy);
        Share = (Button) findViewById(R.id.share);
        Title=(EditText)findViewById(R.id.title);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);


        spinner.setOnItemSelectedListener(this);

        List<String> categories = new ArrayList<String>();
        for (Locale locale : Locale.getAvailableLocales()) {
            categories.add( locale.getLanguage() + "_" + locale.getCountry() + " [" + locale.getDisplayName() + "]");
        }
        for (Locale locale : Locale.getAvailableLocales())
        {
            arrayList.add(locale.getLanguage());
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(dataAdapter);


        Speak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, item);
                try
                {
                    startActivityForResult(intent, RESULT_SPEECH);
                    Text.setText("");
                } catch (ActivityNotFoundException a) {
                    Toast t = Toast.makeText(getApplicationContext(), "Unfortunately, this device does not support talk",
                            Toast.LENGTH_SHORT);
                    t.show();
                }
            }
        });

        Copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = Title.getText().toString();
                String body = Text.getText().toString();
                long msTime = System.currentTimeMillis();
                Date curDateTime = new Date(msTime);

                SimpleDateFormat formatter = new SimpleDateFormat("d'/'M'/'y");
                curDate = formatter.format(curDateTime);
                mDbHelper = new NotesDbAdapter(getApplicationContext());
                mDbHelper.open();

                long number = mDbHelper.createNote(title,body,curDate);
                if (number < 0)
                    Toast.makeText(getApplicationContext(), number + "unsuccesful", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), "successful", Toast.LENGTH_SHORT).show();
                Intent intent= new Intent(sound.this,NoteList.class);
                startActivity(intent);



            }
        });

        Share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copy_content = Text.getText().toString();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, copy_content);
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "Select the application:"));
            }

        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_SPEECH: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> text = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    Text.setText(text.get(0));
                }
                break;
            }

        }
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
         item=arrayList.get(position);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
