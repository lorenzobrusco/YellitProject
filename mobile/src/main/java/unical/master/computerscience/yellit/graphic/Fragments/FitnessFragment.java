package unical.master.computerscience.yellit.graphic.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import unical.master.computerscience.yellit.R;

/**
 * Created by Lorenzo on 14/03/2017.
 */

public class FitnessFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fitness, container, false);
        return view;
    }

}
