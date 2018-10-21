package com.raemacias.thebakingapp.network;

import com.raemacias.thebakingapp.models.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GetRecipes {

    @GET("baking.json")
    Call<List<Recipe>> getAllRecipes();
}
