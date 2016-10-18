package com.connollyed.dictionaryfilegame;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Dict> dictionary; //Complete list of words and definitions from textfile
    private ArrayList<Dict> randomdefs; //5 random words and definitions
    private int chosen = 0;             //This is the correct answer from the randomdefs
    private ListView defs;
    private ArrayList<String> definition_list;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(dictionary == null) {
            dictionary = new ArrayList<>();
            readDictionaryFile();
        }

        getRandomDefinitions();
        chooseWord();
        addToListView();
        setupListviewListener();
    }

    private void setupListviewListener() {
        defs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(chosen == position ){
                    Toast.makeText(getApplicationContext(),"Correct",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(),"Wrong", Toast.LENGTH_SHORT).show();
                }

                getRandomDefinitions();
                chooseWord();
                definition_list.clear();
                addToListView();
                arrayAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * Add randomdefs to ListView
     */
    private void addToListView() {
        defs = (ListView) findViewById(R.id.definitions);

        definition_list = new ArrayList<String>();
        for(int i=0;i<randomdefs.size();i++)
            definition_list.add(randomdefs.get(i).def);

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, definition_list);
        defs.setAdapter(arrayAdapter);
    }

    /**
     * Randomly choose a word from the randomdefs that the user has to select the definition
     */
    private void chooseWord(){
        Random gen = new Random();
        chosen = gen.nextInt(5);

        Log.i("rand",randomdefs.get(0).word);
//        Dict chosen_entry = new Dict(randomdefs.get(chosen_entry).word, randomdefs.get(chosen_entry).def);
        Dict chosen_entry = randomdefs.get(chosen);
        TextView word_text = (TextView) findViewById(R.id.word);
        word_text.setText(chosen_entry.word);
    }

    /**
     * Shuffles arraylist of dictionary items to get 5 random definitions
     * @return array
     */
    private void getRandomDefinitions() {
        Collections.shuffle(dictionary);

        randomdefs = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            randomdefs.add(dictionary.get(i));
            //Log.i("RANDOMDEF",dictionary.get(i).word);
        }

//        return randomdefs
    }

    /**
     * Reads the file into a List called dictionary
     */
    private void readDictionaryFile() {
        try {
            Scanner scan = new Scanner(getResources().openRawResource(R.raw.words));

            String line = null;
            while(scan.hasNext()){
                // Get line and split on "=" sign and clean strings removing whitespace
                line = scan.nextLine();

                String params[] = line.split("=");
                String str_word = params[0].trim();
                String str_def = params[1].trim();
                Log.i("FILE", line);

                //Add to List, word is param[0] and definition is param[1]
                dictionary.add(new Dict(str_word, str_def));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

/**
 * Dictionary Entity that stores the word and its corresponding definition
 */
class Dict{
    String word;
    String def;

    public Dict(String word, String def){
        this.word = word;
        this.def = def;
    }
}
