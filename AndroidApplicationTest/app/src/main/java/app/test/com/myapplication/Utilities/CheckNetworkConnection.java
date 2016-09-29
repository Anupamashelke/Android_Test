package app.test.com.myapplication.Utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
/**
 * Created by anupama.shelke on 9/29/2016.
 * This class used for Network connection checking.
 *
 */
public class CheckNetworkConnection {

	public static boolean isConnectionAvailable(Context context) {

		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivityManager != null) {
			NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
			if (netInfo != null && netInfo.isConnected()
					&& netInfo.isConnectedOrConnecting()
					&& netInfo.isAvailable()) {
			return true;
			}
		}
		return false;
	}
	

}
