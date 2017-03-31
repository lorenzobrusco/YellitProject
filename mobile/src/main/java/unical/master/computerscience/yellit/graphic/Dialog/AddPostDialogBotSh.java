package unical.master.computerscience.yellit.graphic.Dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import unical.master.computerscience.yellit.R;

/**
 * Created by Lorenzo on 15/03/2017.
 */

public class CustomDialogBottomSheet extends BottomSheetDialogFragment {

    //TODO replace this class
    String mString;

    public static CustomDialogBottomSheet newInstance(String string) {
        CustomDialogBottomSheet f = new CustomDialogBottomSheet();
        Bundle args = new Bundle();
        args.putString("string", string);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mString = getArguments().getString("string");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_post_modal, container, false);
        TextView tv = (TextView) v.findViewById(R.id.text);
        return v;
    }
}