package com.kiri.hackjak;

import java.util.List;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class Navigation {	
	public static String ACTION_PREV = "com.kiri.hackjak.ACTION_PREV";
	public static String ACTION_STOP = "com.kiri.hackjak.ACTION_STOP";
	public static String ACTION_NEXT = "com.kiri.hackjak.ACTION_NEXT";
	
	private static int NOTIFICATION_ID = 0;
	
	private static Navigation mInstance;
	
	private Context mContext;
	private List<FormattedResult> mRoutes;
	private int mRouteIndex;
	
	private NotificationManager notificationManager;
	private NotificationCompat.Builder notifyBuilder;
	
	public static Navigation getInstance() {
		return (mInstance == null ? (mInstance = new Navigation()) : mInstance);
	}
	
	public void initiateRoutes(Context context, List<FormattedResult> routes) {
		mContext = context;
		mRoutes = routes;
		mRouteIndex = 0;
		
		notificationManager = (NotificationManager) 
				  mContext.getSystemService(Context.NOTIFICATION_SERVICE);
		notifyBuilder = new NotificationCompat.Builder(mContext);
	}
	
	public FormattedResult actionPrev() {
		if(mRouteIndex > 0)  mRouteIndex--;
		return mRoutes.get(mRouteIndex);
	}
	
	public FormattedResult actionNext() {
		if(mRouteIndex < mRoutes.size() - 1)  mRouteIndex++;
		return mRoutes.get(mRouteIndex);
	}
	
	public FormattedResult getCurrentRoute() {
		return mRoutes.get(mRouteIndex);
	}
	
	public void actionStop() {
		notificationManager.cancel(NOTIFICATION_ID);;
	}
	
	public void redraw() {		
		Intent intentPrev = new Intent(mContext, NotificationReceiverService.class);
		intentPrev.setAction(ACTION_PREV);
		PendingIntent pIntentPrev = PendingIntent.getBroadcast(mContext, 0, intentPrev, PendingIntent.FLAG_UPDATE_CURRENT);
		
		Intent intentNext = new Intent(mContext, NotificationReceiverService.class);
		intentNext.setAction(ACTION_NEXT);
		PendingIntent pIntentNext = PendingIntent.getBroadcast(mContext, 1, intentNext, PendingIntent.FLAG_UPDATE_CURRENT);
		
		Intent intentStop = new Intent(mContext, NotificationReceiverService.class);
		intentStop.setAction(ACTION_STOP);
		PendingIntent pIntentStop = PendingIntent.getBroadcast(mContext, 2, intentStop, PendingIntent.FLAG_UPDATE_CURRENT);
		
		notifyBuilder
			//.setContentTitle("Navigasi : " + Integer.toString(mRouteIndex))
			.setContentTitle(getCurrentRoute().getTitle())
			.setContentText(getCurrentRoute().getDetail())
			.setOngoing(true)
			.setSmallIcon(android.R.drawable.ic_menu_directions)
			.addAction(android.R.drawable.ic_media_rew, "Prev", pIntentPrev)
			.addAction(R.drawable.ic_action_stop, "Stop", pIntentStop)
			.addAction(android.R.drawable.ic_media_ff, "Next", pIntentNext);
		
		notificationManager.notify(NOTIFICATION_ID, notifyBuilder.build());
	}
}
