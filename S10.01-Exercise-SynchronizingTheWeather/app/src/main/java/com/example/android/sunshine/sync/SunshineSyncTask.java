package com.example.android.sunshine.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.example.android.sunshine.data.WeatherContract;
import com.example.android.sunshine.utilities.NetworkUtils;
import com.example.android.sunshine.utilities.OpenWeatherJsonUtils;

import java.net.URL;

//  COMPLETED (1) Create a class called SunshineSyncTask
public class SunshineSyncTask {

    private static final String TAG = SunshineSyncTask.class.getSimpleName();

//  COMPLETED (2) Within SunshineSyncTask, create a synchronized public static void method called syncWeather
    public synchronized static void syncWeather(final Context context) {
        try {
//      COMPLETED (3) Within syncWeather, fetch new weather data
            final URL url = NetworkUtils.getUrl(context);
            final String response = NetworkUtils.getResponseFromHttpUrl(url);
            final ContentValues[] items = OpenWeatherJsonUtils
                    .getWeatherContentValuesFromJson(context, response);
//      COMPLETED (4) If we have valid results, delete the old data and insert the new
            if (items != null && items.length > 0) {
                final ContentResolver contentResolver = context.getContentResolver();
                contentResolver.delete(WeatherContract.WeatherEntry.CONTENT_URI, null, null);
                contentResolver.bulkInsert(WeatherContract.WeatherEntry.CONTENT_URI, items);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }
}