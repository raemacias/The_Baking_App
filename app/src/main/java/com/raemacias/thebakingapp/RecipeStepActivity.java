package com.raemacias.thebakingapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.raemacias.thebakingapp.fragments.RecipeStepFragment;

//This was created by watching the video tutorial from Delaroy Studios.

public class RecipeStepActivity extends AppCompatActivity {

    public static final String STEP = "Step";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_recipe_step);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Recipe Video");


        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {

            Bundle arguments = new Bundle();
            arguments.putParcelable("Steps", getIntent().getParcelableExtra("Steps"));

            RecipeStepFragment stepFragment = new RecipeStepFragment();
            stepFragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.recipe_detail_container, stepFragment)
                    .commit();

        }

    }

}

