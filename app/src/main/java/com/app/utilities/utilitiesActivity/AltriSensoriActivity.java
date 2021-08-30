package com.app.utilities.utilitiesActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.app.utilities.R;
import com.app.utilities.utility.Preferences;
import com.app.utilities.utility.Utils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

public class AltriSensoriActivity extends AppCompatActivity implements SensorEventListener {
    private final Utils utils = new Utils();
    @SuppressWarnings("unused")
    protected Configuration mPrevConfig;
    TextView light, pressure, temperature, humidity;
    Preferences pref;
    Spinner speedLig, speedPre, speedTem, speedHum;
    SensorManager sensorManager;
    Sensor mLight, mPressure, mTemperature, mHumidity;
    LinearLayout otherSensorsLayout;
    int ligSimpRate, preSimpRate, temSimpRate, humSimpRate;
    private Thread thread;
    private boolean plotData = true;
    private LineChart chartLight, chartPressure, chartTemperature, chartHumidity;

    public static boolean isOnDarkMode(@NonNull Configuration configuration) {
        return (configuration.uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = utils.loadData(this, new Preferences());
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
        setContentView(R.layout.activity_altri_sensori);

        speedLig = findViewById(R.id.speedLig);
        speedPre = findViewById(R.id.speedPre);
        speedTem = findViewById(R.id.speedTem);
        speedHum = findViewById(R.id.speedHum);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.speed));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        speedLig.setAdapter(arrayAdapter);
        speedPre.setAdapter(arrayAdapter);
        speedTem.setAdapter(arrayAdapter);
        speedHum.setAdapter(arrayAdapter);

        otherSensorsLayout = findViewById(R.id.otherSensorsLayout);

        light = findViewById(R.id.light);
        pressure = findViewById(R.id.pressure);
        temperature = findViewById(R.id.temperature);
        humidity = findViewById(R.id.humidity);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);


        chartLight = (LineChart) findViewById(R.id.chartLight);
        chartPressure = (LineChart) findViewById(R.id.chartPressure);
        chartTemperature = (LineChart) findViewById(R.id.chartTemperature);
        chartHumidity = (LineChart) findViewById(R.id.chartHumidity);

        ligSimpRate = SensorManager.SENSOR_DELAY_NORMAL;
        preSimpRate = SensorManager.SENSOR_DELAY_NORMAL;
        temSimpRate = SensorManager.SENSOR_DELAY_NORMAL;
        humSimpRate = SensorManager.SENSOR_DELAY_NORMAL;

        mLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if (mLight != null) {
            sensorManager.registerListener(this, mLight, ligSimpRate);
            config(chartLight, "luminosità");
        } else {
            light.setText(R.string.BrightnessNotSupported);
            otherSensorsLayout.removeView(chartLight);
            otherSensorsLayout.removeView(speedLig);
        }
        mPressure = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        if (mPressure != null) {
            sensorManager.registerListener(this, mPressure, preSimpRate);
            config(chartPressure, "peressione");
        } else {
            pressure.setText(R.string.PressureNotSupported);
            otherSensorsLayout.removeView(chartPressure);
            otherSensorsLayout.removeView(speedPre);
        }
        mTemperature = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        if (mTemperature != null) {
            sensorManager.registerListener(this, mTemperature, temSimpRate);
            config(chartTemperature, "temperatura");
        } else {
            temperature.setText(R.string.TemperatureNotSupported);
            otherSensorsLayout.removeView(chartTemperature);
            otherSensorsLayout.removeView(speedTem);
        }
        mHumidity = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        if (mHumidity != null) {
            sensorManager.registerListener(this, mHumidity, humSimpRate);
            config(chartHumidity, "umidità");
        } else {
            humidity.setText(R.string.HumidityNotSupported);
            otherSensorsLayout.removeView(chartHumidity);
            otherSensorsLayout.removeView(speedHum);
        }
        speedLig.setSelection(3);
        speedLig.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                switch (position) {
                    case 0:
                        ligSimpRate = SensorManager.SENSOR_DELAY_FASTEST;
                        break;
                    case 1:
                        ligSimpRate = SensorManager.SENSOR_DELAY_GAME;
                        break;
                    case 2:
                        ligSimpRate = SensorManager.SENSOR_DELAY_UI;
                        break;
                    case 3:
                    default:
                        ligSimpRate = SensorManager.SENSOR_DELAY_NORMAL;
                        break;
                }
                if (mLight != null) {
                    sensorManager.unregisterListener(AltriSensoriActivity.this, mLight);
                    sensorManager.registerListener(AltriSensoriActivity.this, mLight, ligSimpRate);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        speedPre.setSelection(3);
        speedPre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                switch (position) {
                    case 0:
                        preSimpRate = SensorManager.SENSOR_DELAY_FASTEST;
                        break;
                    case 1:
                        preSimpRate = SensorManager.SENSOR_DELAY_GAME;
                        break;
                    case 2:
                        preSimpRate = SensorManager.SENSOR_DELAY_UI;
                        break;
                    case 3:
                    default:
                        preSimpRate = SensorManager.SENSOR_DELAY_NORMAL;
                        break;
                }
                if (mPressure != null) {
                    sensorManager.unregisterListener(AltriSensoriActivity.this, mPressure);
                    sensorManager.registerListener(AltriSensoriActivity.this, mPressure, preSimpRate);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        speedTem.setSelection(3);
        speedTem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                switch (position) {
                    case 0:
                        temSimpRate = SensorManager.SENSOR_DELAY_FASTEST;
                        break;
                    case 1:
                        temSimpRate = SensorManager.SENSOR_DELAY_GAME;
                        break;
                    case 2:
                        temSimpRate = SensorManager.SENSOR_DELAY_UI;
                        break;
                    case 3:
                    default:
                        temSimpRate = SensorManager.SENSOR_DELAY_NORMAL;
                        break;
                }
                if (mTemperature != null) {
                    sensorManager.unregisterListener(AltriSensoriActivity.this, mTemperature);
                    sensorManager.registerListener(AltriSensoriActivity.this, mTemperature, temSimpRate);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        speedHum.setSelection(3);
        speedHum.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                switch (position) {
                    case 0:
                        humSimpRate = SensorManager.SENSOR_DELAY_FASTEST;
                        break;
                    case 1:
                        humSimpRate = SensorManager.SENSOR_DELAY_GAME;
                        break;
                    case 2:
                        humSimpRate = SensorManager.SENSOR_DELAY_UI;
                        break;
                    case 3:
                    default:
                        humSimpRate = SensorManager.SENSOR_DELAY_NORMAL;
                        break;
                }
                if (mHumidity != null) {
                    sensorManager.unregisterListener(AltriSensoriActivity.this, mHumidity);
                    sensorManager.registerListener(AltriSensoriActivity.this, mHumidity, humSimpRate);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        feedMultiple();
        ImageButton back = findViewById(R.id.back);
        back.setOnClickListener(v -> utils.goToMainActivity(this));
        mPrevConfig = new Configuration(getResources().getConfiguration());
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        if (sensor.getType() == Sensor.TYPE_LIGHT) {
            light.setText(Html.fromHtml("<b>Luminosità: </b>" + utils.roundAvoid(event.values[0], 2) + " [lx]"));
            if (plotData) {
                addEntry(event, "luminosità");
                plotData = false;
            }
        } else if (sensor.getType() == Sensor.TYPE_PRESSURE) {
            pressure.setText(Html.fromHtml("<b>Pressione: </b>" + utils.roundAvoid(event.values[0], 2) + " [hPa (mbar)]"));
            if (plotData) {
                addEntry(event, "pressione");
                plotData = false;
            }
        } else if (sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            temperature.setText(Html.fromHtml("<b>Temperatura: </b>" + utils.roundAvoid(event.values[0], 2) + " [°C]"));
            if (plotData) {
                addEntry(event, "temperatura");
                plotData = false;
            }
        } else if (sensor.getType() == Sensor.TYPE_RELATIVE_HUMIDITY) {
            humidity.setText(Html.fromHtml("<b>Umidità: </b>" + utils.roundAvoid(event.values[0], 2) + " [%]"));
            if (plotData) {
                addEntry(event, "umidità");
                plotData = false;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @SuppressWarnings("CommentedOutCode")
    private void config(LineChart chart, String sensor) {
        chart.getDescription().setEnabled(false);
        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setDrawGridBackground(false);
        chart.setPinchZoom(true);
        chart.setBackgroundColor(Color.WHITE);
        LineData data = new LineData();
        data.setValueTextColor(Color.BLACK);
        chart.setData(data);
        Legend l = chart.getLegend();
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.BLACK);
        XAxis xl = chart.getXAxis();
        xl.setTextColor(Color.BLACK);
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(false);
        xl.setEnabled(true);
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setDrawGridLines(false);
        switch (sensor) {
            case "luminosità":
                leftAxis.setAxisMinimum(0f);
                break;
            /*case "pressione":
                leftAxis.setAxisMaximum(mPressure.getMaximumRange());
                leftAxis.setAxisMinimum(-mPressure.getMaximumRange());
                break;
            case "temperatura":
                leftAxis.setAxisMaximum(mTemperature.getMaximumRange());
                leftAxis.setAxisMinimum(-mTemperature.getMaximumRange());
                break;
             */
            case "umnidità":
                //noinspection DuplicateBranchesInSwitch
                leftAxis.setAxisMinimum(0f);
                break;
        }
        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);
        chart.getAxisLeft().setDrawGridLines(true);
        chart.getXAxis().setDrawGridLines(true);
        chart.setDrawBorders(false);
    }

    private void addEntry(SensorEvent event, String sensor) {
        LineData data;
        switch (sensor) {
            case "luminosità":
                data = chartLight.getData();
                if (data != null) {
                    ILineDataSet set = data.getDataSetByIndex(0);
                    if (set == null) {
                        set = createSet("Lum.");
                        data.addDataSet(set);
                    }
                    data.addEntry(new Entry(set.getEntryCount(), event.values[0]), 0);
                    data.notifyDataChanged();
                    chartLight.notifyDataSetChanged();
                    chartLight.setVisibleXRangeMaximum(10);
                    chartLight.moveViewToX(data.getEntryCount());
                }
                break;
            case "pressione":
                data = chartPressure.getData();
                if (data != null) {
                    ILineDataSet set = data.getDataSetByIndex(0);
                    if (set == null) {
                        set = createSet("Pres.");
                        data.addDataSet(set);
                    }
                    data.addEntry(new Entry(set.getEntryCount(), event.values[0]), 0);
                    data.notifyDataChanged();
                    chartPressure.notifyDataSetChanged();
                    chartPressure.setVisibleXRangeMaximum(10);
                    chartPressure.moveViewToX(data.getEntryCount());
                }
                break;
            case "temperatura":
                data = chartTemperature.getData();
                if (data != null) {
                    ILineDataSet set = data.getDataSetByIndex(0);
                    if (set == null) {
                        set = createSet("Temp.");
                        data.addDataSet(set);
                    }
                    data.addEntry(new Entry(set.getEntryCount(), event.values[0] + 10), 0);
                    data.notifyDataChanged();
                    chartTemperature.notifyDataSetChanged();
                    chartTemperature.setVisibleXRangeMaximum(10);
                    chartTemperature.moveViewToX(data.getEntryCount());
                }
                break;
            case "umidità":
                data = chartHumidity.getData();
                if (data != null) {
                    ILineDataSet set = data.getDataSetByIndex(0);
                    if (set == null) {
                        set = createSet("Umid.");
                        data.addDataSet(set);
                    }
                    data.addEntry(new Entry(set.getEntryCount(), event.values[0]), 0);
                    data.notifyDataChanged();
                    chartHumidity.notifyDataSetChanged();
                    chartHumidity.setVisibleXRangeMaximum(10);
                    chartHumidity.moveViewToX(data.getEntryCount());
                }
                break;
        }

    }

    @NonNull
    private LineDataSet createSet(String data) {
        LineDataSet set = new LineDataSet(null, data);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setLineWidth(3f);
        set.setColor(Color.GREEN);
        set.setHighlightEnabled(false);
        set.setDrawValues(true);
        set.setDrawCircles(false);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setCubicIntensity(0.2f);
        return set;
    }

    private void feedMultiple() {
        if (thread != null) {
            thread.interrupt();
        }
        thread = new Thread(() -> {
            while (true) {
                plotData = true;
                try {
                    //noinspection BusyWait
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (thread != null) {
            thread.interrupt();
        }
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if (mLight != null) {
            sensorManager.registerListener(this, mLight, ligSimpRate);
        } else {
            light.setText(R.string.BrightnessNotSupported);
        }
        mPressure = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        if (mPressure != null) {
            sensorManager.registerListener(this, mPressure, preSimpRate);
        } else {
            pressure.setText(R.string.PressureNotSupported);
        }
        mTemperature = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        if (mTemperature != null) {
            sensorManager.registerListener(this, mTemperature, temSimpRate);
        } else {
            temperature.setText(R.string.TemperatureNotSupported);
        }
        mHumidity = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        if (mHumidity != null) {
            sensorManager.registerListener(this, mHumidity, humSimpRate);
        } else {
            humidity.setText(R.string.HumidityNotSupported);
        }
    }

    @Override
    public void onDestroy() {
        sensorManager.unregisterListener(this);
        thread.interrupt();
        super.onDestroy();
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