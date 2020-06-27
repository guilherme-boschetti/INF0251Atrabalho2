package com.example.inf0251atrabalho2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.inf0251atrabalho2.maze.MazeActivity;
import com.example.inf0251atrabalho2.testmovecircle.MoveCircleSensorActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void maze(View view) {
        Intent intent = new Intent(this, MazeActivity.class);
        startActivity(intent);
    }

    public void moveCircleSensor(View view) {
        Intent intent = new Intent(this, MoveCircleSensorActivity.class);
        startActivity(intent);
    }

    public void exitGame(View view) {
        finish();
    }
}
