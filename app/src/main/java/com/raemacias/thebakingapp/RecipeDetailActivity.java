package com.raemacias.thebakingapp;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.raemacias.thebakingapp.fragments.RecipeStepFragment;
import com.raemacias.thebakingapp.models.Ingredient;
import com.raemacias.thebakingapp.models.Recipe;
import com.raemacias.thebakingapp.models.Step;

import java.util.ArrayList;
import java.util.List;

//Most of this code came from doing the tutorial from Delaroy Studios on YouTube
//https://www.youtube.com/watch?v=edLw_iaOEWU&t=842s
/**
 * An activity representing a single Recipe detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link RecipeListActivity}.
 */
public class RecipeDetailActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private List<Ingredient> mIngredientList;
    private List<Ingredient> mQuantity;
    private List<Step> mStepList;
    public String recipeName;
    public ArrayList<Object> recipeObjects;
    public Recipe mRecipe;
    private boolean mTwoPane;
    private List<Ingredient> mWidgetIngredient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recipeObjects = new ArrayList<>();

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        if (intent.hasExtra("Recipe")) {
            mRecipe = getIntent().getParcelableExtra("Recipe");
            mIngredientList = getIntent().getParcelableArrayListExtra("Ingredients");
            mIngredientList = mRecipe.getIngredients();
            mStepList = mRecipe.getSteps();
            recipeName = mRecipe.getName();
            recipeObjects.addAll(mIngredientList);
            recipeObjects.addAll(mStepList);

            setTitle(recipeName);

        } else {
            Toast.makeText(this, "Info not available", Toast.LENGTH_SHORT).show();
        }

        SharedPreferences myPrefs;

//        public void saveArrayList(ArrayList<String> mIngredientList, String key){
//            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
//            SharedPreferences.Editor editor = prefs.edit();
//            Gson gson = new Gson();
//            String json = gson.toJson(mIngredientList);
//            editor.putString(key, json);
//            editor.apply();     // This line is IMPORTANT !!!
//            editor.commit();
//        }




        //This came from the tutorial at https://appsandbiscuits.com/saving-data-with-sharedpreferences-android-9-9fecae19896a
        myPrefs = getSharedPreferences (getString(R.string.appwidget_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = myPrefs.edit();

//        //The name key is the identifier for the item being stored.
//        //Bruce the Hoon is the value.
        editor.putString(getString(R.string.appwidget_recipe_name), mRecipe.getName());
        editor.putString(getString(R.string.appwidget_ingredients), getAndFormatIngredients());
        editor.apply();
        editor.commit();

        //This code came from https://stackoverflow.com/questions/3455123/programmatically-update-widget-from-activity-service-receiver
        Intent intent1 = new Intent(this, BakingAppWidget.class);
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
        int ids[] = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), BakingAppWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
        sendBroadcast(intent1);

        //Example:
//        editor.putString("nameKey", nameEditText.getText().toString());
//        editor.putInt("ageKey", Integer.parseInt(ageEditText.getText().toString()));



        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //

        if (findViewById(R.id.recipe_detail_container) != null) {
            mTwoPane = true;
        }

        View mRecyclerView = findViewById(R.id.recipe_list);
        assert mRecyclerView != null;
        setupRecyclerView((RecyclerView) mRecyclerView);
    }

    private String getAndFormatIngredients() {
        List<Ingredient> recipeIngredients = mRecipe.getIngredients();
        String[] ingredients = new String[recipeIngredients.size()];
        for (int i = 0; i < ingredients.length; i++) {
            ingredients[i] = recipeIngredients.get(i).getQuantityUnitNameString();
        }
        return TextUtils.join("\n", ingredients);

    }

    private void setupRecyclerView(@NonNull RecyclerView mRecyclerView) {
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(new RecipeAndStepAdapter(recipeObjects, mTwoPane));
    }

    //inner class adapter
    public class RecipeAndStepAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        //What we need to display in our RV
        private List<Object> dataSet;
        private static final int INGREDIENT = 0;
        private static final int STEP = 1;
        public boolean isTwoPane;

        public RecipeAndStepAdapter(List<Object> dataSet, boolean isTwoPane) {
            this.dataSet = dataSet;
            this.isTwoPane = isTwoPane;
        }

        @Override
        public int getItemViewType(int position) {
            if (dataSet.get(position) instanceof Ingredient) {
                return INGREDIENT;
            } else if (dataSet.get(position) instanceof Step) {
                return STEP;
            }
            return -1;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            RecyclerView.ViewHolder viewHolder;
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

            switch (viewType) {
                case INGREDIENT:
                    View userView = inflater.inflate(R.layout.layout_viewholder_ingredient, viewGroup, false);
                    viewHolder = new IngredientViewHolder(userView);
                    break;
                case STEP:
                    View imageView = inflater.inflate(R.layout.layout_viewholder_step, viewGroup, false);
                    viewHolder = new StepViewHolder(imageView);
                    break;
                default:
                    View view = inflater.inflate(R.layout.layout_viewholder_step, viewGroup, false);
                    viewHolder = new StepViewHolder(view);
            }
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
            switch (viewHolder.getItemViewType()) {
                case INGREDIENT:
                    IngredientViewHolder ingredientViewHolder = (IngredientViewHolder) viewHolder;
                    configureIngredientViewHolder(ingredientViewHolder, position);
                    break;
                case STEP:
                    StepViewHolder stepViewHolder = (StepViewHolder) viewHolder;
                    configureStepViewHolder(stepViewHolder, position);
                    break;
                default:
                    StepViewHolder defaultVh = (StepViewHolder) viewHolder;
                    configureStepViewHolder(defaultVh, position);
                    break;
            }
        }

        private void configureIngredientViewHolder(IngredientViewHolder ingredientViewHolder, int position) {
            Ingredient ingredient = (Ingredient) dataSet.get(position);
            if (ingredient != null) {
                ingredientViewHolder.getIngredient().setText(ingredient.getIngredient());
            }
        }

        private void configureStepViewHolder(StepViewHolder stepViewHolder, int position) {
            Step step = (Step) dataSet.get(position);
            if (step != null) {
                stepViewHolder.getShortDesc().setText(step.getShortDescription());
            }
        }

        @Override
        public int getItemCount() {
            return this.dataSet.size();
        }

        public class StepViewHolder extends RecyclerView.ViewHolder {

            public TextView shortDesc, desc, videoUrl;

            public StepViewHolder(View view) {
                super(view);

                shortDesc = view.findViewById(R.id.short_description);
                desc = view.findViewById(R.id.description);
                videoUrl = view.findViewById(R.id.video_url);

                view.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION){

                            //if twopane, call the bundle
                            if (isTwoPane) {
                                Step clickedDataItem = (Step)dataSet.get(pos);
                                Bundle arguments = new Bundle();
                                arguments.putParcelable("Steps", clickedDataItem);

                                //may need to make this into RecipeStepFragment instead
                                RecipeStepFragment stepFragment = new RecipeStepFragment();
                                stepFragment.setArguments(arguments);
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.recipe_detail_container, stepFragment)
                                        .commit();
                            } else {
                                Context context = v.getContext();
                                //still need to cast to step object
                                Step clickedDataItem = (Step)dataSet.get(pos);
                                Intent intent = new Intent(context, RecipeStepActivity.class);
                                intent.putExtra("Steps", clickedDataItem);
                                context.startActivity(intent);
                            }
                        }
                    }
                });
            }
            public TextView getShortDesc() {
                return this.shortDesc;
            }

            public void setShortDesc(TextView shortDesc) {
                this.shortDesc = shortDesc;
            }

            public TextView getDesc() {
                return this.desc;
            }

            public void setDesc(TextView desc) {
                this.desc = desc;
            }

            public TextView getVideoUrl() {
                return this.videoUrl;
            }

            public void setVideoUrl(TextView videoUrl) {
                this.videoUrl = videoUrl;
            }
        }

        public class IngredientViewHolder extends RecyclerView.ViewHolder {
            public TextView ingredient, recipe;

            public IngredientViewHolder(View view) {
                super(view);
                ingredient = view.findViewById(R.id.recipe_ingredients);
            }

            public TextView getIngredient() {
                return this.ingredient;
            }

            public void setIngredient(TextView ingredient) {
                this.ingredient = ingredient;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(new Intent(this, RecipeListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
