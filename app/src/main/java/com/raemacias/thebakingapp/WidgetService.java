package com.raemacias.thebakingapp;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

public class WidgetService extends IntentService {

    public static final String RECIPE_NAME = "Recipe Name";
    public static final String INGREDIENTS = "Ingredients";
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public WidgetService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}
