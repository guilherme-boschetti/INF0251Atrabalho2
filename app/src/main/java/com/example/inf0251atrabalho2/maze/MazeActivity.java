package com.example.inf0251atrabalho2.maze;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.inf0251atrabalho2.R;

import java.math.BigDecimal;

public class MazeActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private SensorEventListener listener;
    private MazeView mazeView;
    private float lastX;
    private float lastY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometer == null) {
            Toast.makeText(this, R.string.accelerometer_warning, Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        int level = getIntent().getIntExtra("level", 1);

        setTitle(getString(R.string.level, level));

        mazeView = new MazeView(this, level);

        setContentView(mazeView);

        listener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                //float x = event.values[0];
                //float y = event.values[1];
                //float z = event.values[2];

                BigDecimal bdx = new BigDecimal(String.valueOf(event.values[0]));
                BigDecimal bdy = new BigDecimal(String.valueOf(event.values[1]));
                bdx = bdx.setScale(1, BigDecimal.ROUND_HALF_UP);
                bdy = bdy.setScale(1, BigDecimal.ROUND_HALF_UP);
                //float x = bdx.floatValue();
                //float y = bdy.floatValue();
                float x = bdx.intValue();
                float y = bdy.intValue();

                if(x > lastX)
                    mazeView.left();
                else if (x < lastX)
                    mazeView.right();
                if(y > lastY)
                    mazeView.down();
                else if (y < lastY)
                    mazeView.up();
                mazeView.invalidate();

                //lastX = x;
                //lastY = y;
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(listener, accelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(listener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.restart_level) {
            restartLevel();
        } else if (id == R.id.back_to_home) {
            backToHome();
        }
        return super.onOptionsItemSelected(item);
    }

    public void showAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (getIntent().getIntExtra("level", 1) != 3) {
            builder.setPositiveButton(getString(R.string.next_level), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    nextLevel();
                }
            });
        }
        builder.setNegativeButton(getString(R.string.restart_level), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                restartLevel();
            }
        });
        builder.setNeutralButton(getString(R.string.back_to_home), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                backToHome();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    private void nextLevel() {
        Intent it = getIntent();
        int level = it.getIntExtra("level", 1);
        it.putExtra("level", level+1);
        recreate();
    }

    private void restartLevel() {
        recreate();
    }

    private void backToHome() {
        finish();
    }
}
