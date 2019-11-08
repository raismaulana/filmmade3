package com.dicoding.filmfinal.networking;


import com.dicoding.filmfinal.db.models.MovieItems;
import com.dicoding.filmfinal.db.models.TVItems;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FilmApi {
    @GET("discover/movie")
    Call<MovieItems> getMovieList(@Query("api_key") String api_key,
                                  @Query("language") String language);

    @GET("discover/tv")
    Call<TVItems> getTVList(@Query("api_key") String api_key,
                            @Query("language") String language);

    @GET("search/movie")
    Call<MovieItems> searchMovie(@Query("api_key") String api_key,
                                 @Query("language") String language,
                                 @Query("query") String query);

    @GET("search/tv")
    Call<TVItems> searchTV(@Query("api_key") String api_key,
                           @Query("language") String language,
                           @Query("query") String query);

    @GET("discover/movie")
    Call<MovieItems> getNewReleaseMovie(@Query("api_key") String api_key,
                                        @Query("primary_release_date.gte") String date_gte,
                                        @Query("primary_release_date.lte") String date_lte);
}
