package com.app.utilities.utilitiesActivity;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.app.utilities.R;
import com.app.utilities.settings.Preferenze;
import com.app.utilities.utility.Utils;

public class InfoActivity extends AppCompatActivity {
    private final Utils utils = new Utils();
    protected Configuration mPrevConfig;
    Preferenze pref;

    public static boolean isOnDarkMode(Configuration configuration) {
        return (configuration.uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = utils.loadData(this, new Preferenze());
        if (!pref.getPredBool()) {
            if (pref.getThemeText().equals("LightTheme") || pref.getThemeText().equals("LightThemeSelected"))
                utils.changeThemeSelected(this, 0);
            else
                utils.changeThemeSelected(this, 1);
        } else {
            switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
                case Configuration.UI_MODE_NIGHT_UNDEFINED:
                case Configuration.UI_MODE_NIGHT_NO:
                    utils.changeTheme(this, 0);
                    break;
                case Configuration.UI_MODE_NIGHT_YES:
                    utils.changeTheme(this, 1);
                    break;
            }
        }
        setContentView(R.layout.activity_info);
        ImageButton back = findViewById(R.id.back);
        back.setOnClickListener(view -> utils.goToMainActivity(InfoActivity.this));
        mPrevConfig = new Configuration(getResources().getConfiguration());
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        configurationChanged(newConfig);
        mPrevConfig = new Configuration(newConfig);
    }

    protected void configurationChanged(Configuration newConfig) {
        if (isNightConfigChanged(newConfig) && pref.getPredBool()) {
            utils.refreshActivity(this);
        }
    }

    protected boolean isNightConfigChanged(Configuration newConfig) {
        return (newConfig.diff(mPrevConfig) & ActivityInfo.CONFIG_UI_MODE) != 0 && isOnDarkMode(newConfig) != isOnDarkMode(mPrevConfig);
    }
}