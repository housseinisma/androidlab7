package com.example.starwarsencyclopedia;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    ArrayList<String> characterNames;
    ArrayList<JSONObject> characterData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        characterNames = new ArrayList<>();
        characterData = new ArrayList<>();

        // Start AsyncTask to fetch data
        new FetchDataTask().execute();

        listView.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            JSONObject character = characterData.get(position);

            // Check for tablet layout
            View frameLayout = findViewById(R.id.frameLayout);
            if (frameLayout != null) { // Tablet layout
                Bundle bundle = new Bundle();
                bundle.putString("characterData", character.toString());
                DetailsFragment fragment = new DetailsFragment();
                fragment.setArguments(bundle);

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, fragment)
                        .commit();
            } else { // Phone layout
                Intent intent = new Intent(MainActivity.this, EmptyActivity.class);
                intent.putExtra("characterData", character.toString());
                startActivity(intent);
            }
        });
    }

    private class FetchDataTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            StringBuilder result = new StringBuilder();
            try {
                URL url = new URL("https://swapi.dev/api/people/?format=json");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result.toString();
        }

        @Override
        protected void onPostExecute(String responseText) {
            try {
                JSONArray characters = new JSONObject(responseText).getJSONArray("results");
                for (int i = 0; i < characters.length(); i++) {
                    JSONObject character = characters.getJSONObject(i);
                    characterNames.add(character.getString("name"));
                    characterData.add(character);
                }
                CharacterAdapter adapter = new CharacterAdapter(MainActivity.this, characterNames);
                listView.setAdapter(adapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
