package com.example.starwarsencyclopedia;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import org.json.JSONObject;

public class DetailsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        TextView nameTextView = view.findViewById(R.id.nameTextView);
        TextView heightTextView = view.findViewById(R.id.heightTextView);
        TextView birthYearTextView = view.findViewById(R.id.birthYearTextView);

        Bundle bundle = getArguments();
        if (bundle != null) {
            try {
                JSONObject characterData = new JSONObject(bundle.getString("characterData"));
                nameTextView.setText(characterData.getString("name"));
                heightTextView.setText("Height: " + characterData.getString("height"));
                birthYearTextView.setText("Birth Year: " + characterData.getString("birth_year"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return view;
    }
}
