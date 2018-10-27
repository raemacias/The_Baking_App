package com.raemacias.thebakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;
import android.widget.TextView;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {


        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);

        SharedPreferences myPrefs = context.getSharedPreferences(context.getString(R.string.appwidget_name), Context.MODE_PRIVATE);
        String mRecipe = myPrefs.getString(context.getString(R.string.appwidget_recipe_name), "Recipe Name");
        String ingredientsList = myPrefs.getString(context.getString(R.string.appwidget_ingredients), "");


        views.setTextViewText(R.id.widget_recipe_name, mRecipe);
        views.setTextViewText(R.id.widget_ingredients_list, ingredientsList);


        Intent intent = new Intent(context, RecipeListActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.widget_layout, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // called when the user selects a recipe in the app, to update the widget accordingly
        ComponentName bakingAppWidget = new ComponentName(context.getPackageName(), BakingAppWidget.class.getName());
        int[] appWidgetIds = AppWidgetManager.getInstance(context).getAppWidgetIds(bakingAppWidget);
        onUpdate(context, AppWidgetManager.getInstance(context), appWidgetIds);
        super.onReceive(context, intent);
    }
}

