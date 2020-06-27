package com.example.inf0251atrabalho2.testmovecircle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Toast;

import com.example.inf0251atrabalho2.R;

public class MoveCircleSensorActivity extends AppCompatActivity {

    private DrawCircleView drawCircleView;
    private SensorManager sensorManager;
    private Sensor acelerometro;
    private SensorEventListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_circle_sensor);

        ConstraintLayout rootLayout = findViewById(R.id.screen);

        drawCircleView = new DrawCircleView(this);

        rootLayout.addView(drawCircleView);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        acelerometro = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (acelerometro == null) {
            Toast.makeText(this, getString(R.string.accelerometer_warning), Toast.LENGTH_LONG).show();
            finish();
        }

        listener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float x = event.values[0];
                float y = event.values[1];
                //float z = event.values[2];
                moveCircle(x, y);
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };
    }

    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(listener, acelerometro,
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(listener);
    }

    private void moveCircle(float x, float y) {
        int xInt = (int) x;
        int yInt = (int) y;
        drawCircleView.setCurrX(drawCircleView.getCurrX() - (xInt * xInt * xInt));
        drawCircleView.setCurrY(drawCircleView.getCurrY() + (yInt * yInt * yInt));
        drawCircleView.invalidate();
    }
}
