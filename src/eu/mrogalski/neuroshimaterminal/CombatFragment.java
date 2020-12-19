package eu.mrogalski.neuroshimaterminal;

import com.actionbarsherlock.app.SherlockFragment;

import eu.mrogalski.neuroshimaterminal.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CombatFragment extends SherlockFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	View v = inflater.inflate(R.layout.combat, container, false);
        return v;
    }
}

