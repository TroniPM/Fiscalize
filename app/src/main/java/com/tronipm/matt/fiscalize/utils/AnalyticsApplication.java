package com.tronipm.matt.fiscalize.utils;

/*
 * Copyright Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.app.Application;
import android.util.Log;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.tronipm.matt.fiscalize.R;

/**
 * This is a subclass of {@link Application} used to provide shared objects for this app, such as
 * the {@link Tracker}.
 */
public class AnalyticsApplication extends Application {

    private static GoogleAnalytics sAnalytics;
    private static Tracker sTracker;
    public static String TAG_ACTION = "Action";
    public static String TAG_BUTTON = "Button";

    @Override
    public void onCreate() {
        super.onCreate();

        sAnalytics = GoogleAnalytics.getInstance(this);
    }

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     *
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
        //Comente este if para remover o erro de xml não encontrado
        if (sTracker == null) {
            sTracker = sAnalytics.newTracker(R.xml.global_tracker);
        }

        return sTracker;
    }

    public void screen(String name) {
        getDefaultTracker();

        if (sTracker != null) {
            Log.i(AnalyticsApplication.class.getSimpleName(), "New screen: " + name);
            sTracker.setScreenName("Image~" + name);
            sTracker.send(new HitBuilders.ScreenViewBuilder().build());
        } else {
            Log.i(AnalyticsApplication.class.getSimpleName(), "New screen ERROR: tracker is null");
        }
    }

    public void action(String event) {
        getDefaultTracker();

        if (sTracker != null) {
            Log.i(AnalyticsApplication.class.getSimpleName(), "New action: " + event);
            sTracker.send(new HitBuilders.EventBuilder()
                    .setCategory(TAG_ACTION)
                    .setAction(event)
                    .build());
        } else {
            Log.i(AnalyticsApplication.class.getSimpleName(), "New action ERROR: tracker is null");
        }
    }

    public void button(String event) {
        getDefaultTracker();

        if (sTracker != null) {
            Log.i(AnalyticsApplication.class.getSimpleName(), "New action(button): " + event);
            sTracker.send(new HitBuilders.EventBuilder()
                    .setCategory(TAG_BUTTON)
                    .setAction(event)
                    .build());
        } else {
            Log.i(AnalyticsApplication.class.getSimpleName(), "New action(button) ERROR: tracker is null");
        }
    }
}