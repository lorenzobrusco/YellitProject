package unical.master.computerscience.yellit.graphic.Dialog;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import siclo.com.ezphotopicker.api.EZPhotoPick;
import siclo.com.ezphotopicker.api.EZPhotoPickStorage;
import siclo.com.ezphotopicker.api.models.EZPhotoPickConfig;
import siclo.com.ezphotopicker.api.models.PhotoSource;
import siclo.com.ezphotopicker.models.PhotoIntentException;
import unical.master.computerscience.yellit.R;

/**
 * Created by Lorenzo on 15/03/2017.
 */

public class AddPostDialogBotSh extends BottomSheetDialogFragment {

    private static final String DEMO_PHOTO_PATH = "MyDemoPhotoDir";

    @Bind(R.id.addpost_img)
    protected ImageView imageLoaded;

    @Bind(R.id.addpost_cam_button)
    protected Button camButton;

    @Bind(R.id.addpost_gall_button)
    protected Button galButton;

    private EZPhotoPickStorage ezPhotoPickStorage;

    public static AddPostDialogBotSh newInstance(String string) {
        AddPostDialogBotSh f = new AddPostDialogBotSh();
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_post_modal, container, false);
        ButterKnife.bind(this, v);

        ezPhotoPickStorage = new EZPhotoPickStorage(getActivity());

        camButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    EZPhotoPickConfig config = new EZPhotoPickConfig();
                    config.photoSource = PhotoSource.CAMERA;
                    config.storageDir = DEMO_PHOTO_PATH;
                    config.needToAddToGallery = true;
                    config.exportingSize = 1000;
                    EZPhotoPick.startPhotoPickActivity(AddPostDialogBotSh.this, config);

                } catch (PhotoIntentException e) {
                    e.printStackTrace();
                }
            }
        });

        galButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    EZPhotoPickConfig config = new EZPhotoPickConfig();
                    config.photoSource = PhotoSource.GALERY;
                    config.needToExportThumbnail = true;
                    config.isAllowMultipleSelect = true;
                    config.storageDir = DEMO_PHOTO_PATH;
                    config.exportingThumbSize = 200;
                    config.exportingSize = 1000;
                    EZPhotoPick.startPhotoPickActivity(AddPostDialogBotSh.this, config);

                } catch (PhotoIntentException e) {
                    e.printStackTrace();
                }
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == EZPhotoPick.PHOTO_PICK_GALERY_REQUEST_CODE) {

            try {
                ArrayList<String> pickedPhotoNames = data.getStringArrayListExtra(EZPhotoPick.PICKED_PHOTO_NAMES_KEY);

                for(String photoName: pickedPhotoNames){
                    Bitmap pickedPhoto = ezPhotoPickStorage.loadStoredPhotoBitmap(DEMO_PHOTO_PATH, photoName, 300);

                    imageLoaded.setImageBitmap(pickedPhoto);
                    imageLoaded.setScaleType(ImageView.ScaleType.FIT_CENTER);

                    imageLoaded.setVisibility(View.VISIBLE);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (requestCode == EZPhotoPick.PHOTO_PICK_CAMERA_REQUEST_CODE) {

            try {

                Bitmap pickedPhoto = ezPhotoPickStorage.loadLatestStoredPhotoBitmap(300);

                imageLoaded.setImageBitmap(pickedPhoto);
                imageLoaded.setScaleType(ImageView.ScaleType.FIT_CENTER);

                imageLoaded.setVisibility(View.VISIBLE);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}