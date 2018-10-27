package com.raemacias.thebakingapp;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class RecipeDetailActivityTest {


    // http://www.vogella.com/tutorials/AndroidTestingEspresso/article.html
    @Rule
    public ActivityTestRule<RecipeDetailActivity> rule = new ActivityTestRule<>(RecipeDetailActivity.class);


    public static final String BROWNIES = "Brownies";

    @Test
    public void selectDessertGetIngredients() {
        //when you tap on dessert, ingredients list comes up
        onView(withId(R.id.recipe_list))
                .perform(click());
        onView(withText(BROWNIES)).check(matches(isDisplayed()));
    }
}