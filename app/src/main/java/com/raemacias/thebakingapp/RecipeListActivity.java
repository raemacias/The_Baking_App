package com.raemacias.thebakingapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.raemacias.thebakingapp.adapters.RecipeAdapter;
import com.raemacias.thebakingapp.models.Recipe;
import com.raemacias.thebakingapp.network.GetRecipes;
import com.raemacias.thebakingapp.network.Retrofit;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.raemacias.thebakingapp.adapters.RecipeAdapter.*;


/**
 * An activity representing a list of Recipes. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RecipeDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RecipeListActivity extends AppCompatActivity implements ListItemClickListener {

    private static final String TAG = "RecipeListActivity";
    private List<Recipe> mRecipeList = new ArrayList<>();
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    public static final String STEP = "Step";
    public static final String STEP_BUNDLE = "StepBundle";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        setTitle("The Baking App");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        if (findViewById(R.id.recipe_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        final RecyclerView mRecyclerView = findViewById(R.id.recipe_list);
        mRecyclerView.setHasFixedSize(true);
        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);
        GetRecipes getRecipes = Retrofit.getRetrofitInstance().create(GetRecipes.class);

//        setupRecyclerView(mRecyclerView);


//        GetRecipes getRecipes = Retrofit.getRetrofitInstance().create(GetRecipes.class);
        Call<List<Recipe>> call = getRecipes.getAllRecipes();

        call.enqueue(new Callback<List<Recipe>>()  {
            @Override
            public void onResponse(@NonNull Call < List<Recipe >> call, @NonNull Response<List<Recipe>> response){
            if (response.message().contentEquals("OK")) {
                mRecipeList = response.body();
                mRecyclerView.setLayoutManager(new LinearLayoutManager(RecipeListActivity.this));
                mRecyclerView.setAdapter(new RecipeAdapter(RecipeListActivity.this, mRecipeList));
            }
        }

            @Override
            public void onFailure (@NonNull Call < List < Recipe >> call, @NonNull Throwable t){
            Log.i(TAG, t.toString());
        }

        });
    }

    public void onListItemClick(int clickedItemIndex) {

    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

}
