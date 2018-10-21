package com.raemacias.thebakingapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.raemacias.thebakingapp.R;
import com.raemacias.thebakingapp.RecipeDetailActivity;
import com.raemacias.thebakingapp.RecipeListActivity;
//import com.raemacias.thebakingapp.content.RecipeContent;
import com.raemacias.thebakingapp.models.Ingredient;
import com.raemacias.thebakingapp.models.Recipe;
import com.raemacias.thebakingapp.models.Step;


import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.MyViewHolder> {

    private Context mContext;
    private List<Recipe> mRecipeList;
    private final RecipeListActivity mParentActivity = new RecipeListActivity();
    private ListItemClickListener mOnClickListener;
    private boolean mTwoPane;
    private static final String INGREDIENTS = "Ingredients";
    private static final String RECIPE = "RecipeName";
    private static final String STEPS = "Steps";


    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public RecipeAdapter(Context mContext, List<Recipe> mRecipeList) {
        this.mRecipeList = mRecipeList;
        this.mContext = mContext;
        this.mOnClickListener = mOnClickListener;
        this.mTwoPane = mTwoPane;


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView mServings;

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_card, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Recipe recipe = mRecipeList.get(position);
        holder.title.setText(recipe.getName());
//        holder.mServings.setText(recipe.getServings());

        //Need to find what to load here
//        Picasso.get()
//                .load("baking.json")
//                .into((ImageView) holder.itemView);

    }


    @Override
    public int getItemCount() {
        return mRecipeList.size();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        ImageView image;
//        TextView mServings;

        MyViewHolder(final View view) {

            super(view);
            title = view.findViewById(R.id.item_title);
            image = view.findViewById(R.id.recipeImage);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        Recipe clickedDataItem = mRecipeList.get(pos);

                        Intent intent = new Intent(mContext, RecipeDetailActivity.class);
                        intent.putExtra("Recipe", clickedDataItem);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

//                        intent.putParcelableArrayListExtra(INGREDIENTS,
//                                new ArrayList<Ingredient>(mRecipeList.get(pos).getIngredients()));
//                        intent.putParcelableArrayListExtra(STEPS,
//                                new ArrayList<Step>(mRecipeList.get(pos).getSteps()));
                        mContext.startActivity(intent);
                        Toast.makeText(v.getContext(), "You clicked " + clickedDataItem.getName(), Toast.LENGTH_SHORT).show();
                    }

//                    RecipeContent.Recipe item = (RecipeContent.Recipe) view.getTag();


//                    if (mTwoPane) {
//                        Bundle arguments = new Bundle();
//                        arguments.putString(RecipeDetailFragment.INGREDIENTS, STEPS);
//                        RecipeDetailFragment fragment = new RecipeDetailFragment();
//                        fragment.setArguments(arguments);
//                        mParentActivity.getSupportFragmentManager().beginTransaction()
//                                .replace(R.id.recipe_detail_container, fragment)
//                                .commit();
//                    } else {
//                        Context context = view.getContext();
//                        Intent intent = new Intent(context, RecipeDetailActivity.class);
//                        intent.putParcelableArrayListExtra(INGREDIENTS,
//                                new ArrayList<Ingredient>(mRecipeList.get(pos).getIngredients()));
//                        intent.putParcelableArrayListExtra(STEPS,
//                                new ArrayList<Step>(mRecipeList.get(pos).getSteps()));
//
//                        context.startActivity(intent);
//                    }
                }
            });
        }
    }
}
