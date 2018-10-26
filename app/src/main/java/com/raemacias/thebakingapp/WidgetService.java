package com.raemacias.thebakingapp;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import java.util.ArrayList;

public class WidgetService extends IntentService {

    public static final String RECIPE_NAME = "Recipe Name";
    public static final String CURRENT_INGREDIENTS = "Ingredients";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public WidgetService() {
        super("WidgetService");
    }

    public static void startWidgetService(Context context, ArrayList<String> currentIngredients) {
        Intent intent = new Intent(context, WidgetService.class);
        intent.putExtra(CURRENT_INGREDIENTS, currentIngredients);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            ArrayList<String> currentIngredients = intent.getExtras().getStringArrayList(CURRENT_INGREDIENTS);
            handleWidgetService(currentIngredients);

        }
    }

    private void handleWidgetService(ArrayList<String> currentIngredients) {
    }
}
