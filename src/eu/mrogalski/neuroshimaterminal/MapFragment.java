package eu.mrogalski.neuroshimaterminal;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;

import eu.mrogalski.neuroshimaterminal.R;

public class MapFragment extends SherlockFragment {

    @SuppressLint("NewApi")
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	View v = inflater.inflate(R.layout.map, container, false);
    	HorizontallyScrollableWebView w = (HorizontallyScrollableWebView) v.findViewById(R.id.mapView);
    	if(w != null) {
	    	w.setInitialScale(100);
	    	w.setMapTrackballToArrowKeys(true);
	    	w.setHorizontalScrollBarEnabled(true);
	    	w.getSettings().setBuiltInZoomControls(true);
	    	w.getSettings().setSupportZoom(true);
	    	w.getSettings().setLoadWithOverviewMode(true);
	    	if(android.os.Build.VERSION.SDK_INT >= 11) {
	    		w.getSettings().setEnableSmoothTransition(true);
	    	}
	    	w.loadUrl("file:///android_asset/map.png");
    	}
        return v;
    }
}
