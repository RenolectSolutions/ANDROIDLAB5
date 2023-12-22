package com.example.androidlab5;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private ListView listView;
    private CustomArrayAdapter adapter;
    private EditText filterEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        filterEditText = findViewById(R.id.filterEditText);

        adapter = new CustomArrayAdapter(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);

        filterEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String filter = charSequence.toString().toUpperCase();
                adapter.getFilter().filter(filter);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        DataLoader dataLoader = new DataLoader();
        dataLoader.execute();
    }

    private class DataLoader extends AsyncTask<Void, Void, List<CurrencyItem>> {

        protected List<CurrencyItem> doInBackground(Void... voids) {
            String dataUrl = "https://www.floatrates.com/daily/usd.xml";
            List<CurrencyItem> result = new ArrayList<>();

            try {
                URL url = new URL(dataUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                // Parse the XML data and extract the TargetCurrency and ExchangeRate
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser parser = factory.newPullParser();
                parser.setInput(reader);

                int eventType = parser.getEventType();

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG && parser.getName().equals("item")) {
                        String title = parser.nextText();
                        String[] parts = title.split(" ");
                        String exchangeRate = parts[4];
                        String targetCurrency = parts[5];

                        CurrencyItem currencyItem = new CurrencyItem(targetCurrency, exchangeRate);
                        result.add(currencyItem);
                    }

                    eventType = parser.next();
                }
            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
            }

            for (CurrencyItem item : result) {
                Log.d("CurrencyData", item.getTargetCurrency() + " - " + item.getExchangeRate());
            }

            return result;
        }
        @Override
        protected void onPostExecute(List<CurrencyItem> result) {
            adapter.addAll(result);
            adapter.notifyDataSetChanged();
        }
    }
}