package com.example.earthquake_info;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

public class settingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public static class EarthquakeprefrenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.setting_main);

            Preference minmag=findPreference(getString(R.string.settings_min_magnitude_key));
            bindPreferenceSummaryToValue(minmag);

            Preference orderby=findPreference(getString(R.string.settings_order_by_key));
            bindPreferenceSummaryToValue(orderby);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();
            if(preference instanceof ListPreference)
            {
                ListPreference listPreference=(ListPreference) preference;
                int prefIndex=listPreference.findIndexOfValue(stringValue);

                if(prefIndex>=0)
                {
                    CharSequence[] lables=listPreference.getEntries();
                    preference.setSummary(lables[prefIndex]);
                }
            }
            else
            {
                preference.setSummary(stringValue);
            }
            return true;
        }

        private void bindPreferenceSummaryToValue(Preference preference)
        {
            preference.setOnPreferenceChangeListener(this);
            SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String preferencestring=sharedPreferences.getString(preference.getKey(),"");
            onPreferenceChange(preference,preferencestring);
        }
    }
}