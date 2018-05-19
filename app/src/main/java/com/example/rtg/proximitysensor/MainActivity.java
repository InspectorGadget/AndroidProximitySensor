package com.example.rtg.proximitysensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.hardware.SensorEventListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView proximity;
    Button proxbtn;
    private int count;
    private float distance;
    private SensorManager sensorManager;
    private Sensor proximitySensor;
    private SensorEventListener proximityEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        count = 0;

        proximity = (TextView) findViewById(R.id.proximity);
        proxbtn = (Button) findViewById(R.id.proxbtn);
        proxbtn.setOnClickListener(this);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        if (proximitySensor == null) {
            Toast.makeText(this, "Not available!", Toast.LENGTH_LONG).show();
            proxbtn.setText("Your device isn't supported!");
            proxbtn.setEnabled(false);
            finish();
        }

        proximityEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if (sensorEvent.values[0] < proximitySensor.getMaximumRange()) {
                    if (count == 0) {
                        proximity.setText("STATUS: OFF");
                    } else {
                        proximity.setText("Near!");
                    }
                } else {
                    if (count == 0) {
                        proximity.setText("STATUS: OFF");
                    } else {
                        proximity.setText("Nothing is near you!");
                    }
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
        sensorManager.registerListener(proximityEventListener, proximitySensor, 2 * 1000 * 1000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(proximityEventListener);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.proxbtn:
                if (count < 1) {
                    count = 1;
                    proxbtn.setText("Click to disable");
                } else {
                    count = 0;
                    proxbtn.setText("Click to enable!");
                }
                break;
        }
    }

}
