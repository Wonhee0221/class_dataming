package com.example.fooddiary;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MeasureFragment extends Fragment {
    private static final String API_KEY = "a9411a84ed604f16b7ad";
    private static final String API_URL = "http://openapi.foodsafetykorea.go.kr/api/" + API_KEY + "/I2790/json/1/1000/DESC_KOR=가자미구이";

    public MeasureFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_measure, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // API 호출
        new FetchFoodDataTask().execute(API_URL);
    }

    private class FetchFoodDataTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                // API 응답 데이터 읽기
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();
                return response.toString();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);

            if (response != null) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray foodDataArray = jsonObject.getJSONArray("FoodData");

                    StringBuilder proteinData = new StringBuilder();

                    for (int i = 0; i < foodDataArray.length(); i++) {
                        JSONObject foodDataObject = foodDataArray.getJSONObject(i);
                        String protein = foodDataObject.getString("protein");
                        proteinData.append(protein).append("\n");
                    }

                    TextView protein = getView().findViewById(R.id.protein);
                    protein.setText(proteinData.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
