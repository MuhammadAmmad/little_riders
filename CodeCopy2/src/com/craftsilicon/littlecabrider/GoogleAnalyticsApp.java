package com.craftsilicon.littlecabrider;
import android.app.Application;

import com.craftsilicon.littlecabrider.utils.AndyUtils;
import com.craftsilicon.littlecabrider.utils.Const;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.Tracker;
 
/**
 * Created by Ravi on 13/08/15.
 */
public class GoogleAnalyticsApp extends Application {
    public static final String TAG = GoogleAnalyticsApp.class
            .getSimpleName();
 
    private static GoogleAnalyticsApp mInstance;
 
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
 
        AnalyticsTrackers.initialize(this);
        AnalyticsTrackers.getInstance().get(AnalyticsTrackers.Target.APP);
        
     // Setup handler for uncaught exceptions.
        if(!Const.isLogging){
        	Thread.setDefaultUncaughtExceptionHandler (new Thread.UncaughtExceptionHandler()
            {
              @Override
              public void uncaughtException (Thread thread, Throwable e)
              {
                handleUncaughtException (thread, e);
            	  
             }
            });	
        }
        
    }
    public void handleUncaughtException (Thread thread, Throwable e)
    {
      //e.printStackTrace(); // not all Android versions will print the stack trace automatically
      /*if(Const.isLogging){
    	  AndyUtils.appendLog2("testallexceptions", e.getMessage().toString()); 
      }*/
    	
      trackException(e);      
      System.exit(1); // kill off the crashed app
    }
 
    public static synchronized GoogleAnalyticsApp getInstance() {
        return mInstance;
    }
 
    public synchronized Tracker getGoogleAnalyticsTracker() {
        AnalyticsTrackers analyticsTrackers = AnalyticsTrackers.getInstance();
        return analyticsTrackers.get(AnalyticsTrackers.Target.APP);
    }
 
    /***
     * Tracking screen view
     *
     * @param screenName screen name to be displayed on GA dashboard
     */
    public void trackScreenView(String screenName) {
        Tracker t = getGoogleAnalyticsTracker(); 
        // Set screen name.
        t.setScreenName(screenName); 
        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build()); 
        GoogleAnalytics.getInstance(this).dispatchLocalHits();
    }
 
    /***
     * Tracking exception
     *
     * @param e exception to be tracked
     */
    public void trackException(Throwable e) {
        if (e != null) {
            Tracker t = getGoogleAnalyticsTracker();
            t.enableAutoActivityTracking(true);
      	  	t.enableExceptionReporting(true);
            t.send(new HitBuilders.ExceptionBuilder()
                            .setDescription(
                                    new StandardExceptionParser(this, null).getDescription(Thread.currentThread().getName(), e))
                            .setFatal(false)
                            .build()
            );
        }
    }
 
    /***
     * Tracking event
     *
     * @param category event category
     * @param action   action of the event
     * @param label    label
     */
    public void trackEvent(String category, String action, String label) {
        Tracker t = getGoogleAnalyticsTracker();
 
        // Build and send an Event.
        t.send(new HitBuilders.EventBuilder().setCategory(category).setAction(action).setLabel(label).build());
    }
 
}