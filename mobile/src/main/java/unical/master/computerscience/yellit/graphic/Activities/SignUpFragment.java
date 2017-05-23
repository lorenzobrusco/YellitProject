package unical.master.computerscience.yellit.graphic.Activities;


import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import java.io.IOException;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.Bind;
import siclo.com.ezphotopicker.api.EZPhotoPick;
import siclo.com.ezphotopicker.api.EZPhotoPickStorage;
import siclo.com.ezphotopicker.api.models.EZPhotoPickConfig;
import siclo.com.ezphotopicker.api.models.PhotoSource;
import siclo.com.ezphotopicker.models.PhotoIntentException;
import unical.master.computerscience.yellit.MainActivity;
import unical.master.computerscience.yellit.R;

import static android.app.Activity.RESULT_OK;

public class SignUpFragment extends Fragment {

    private static final String DEMO_PHOTO_PATH = "MyDemoPhotoDir";
    private static final String TAG = "SignupActivity";
    private static final int REQUEST_CODE = 1;
    private static final int TAKE_PICTURE = 1;

    private EZPhotoPickStorage ezPhotoPickStorage;

    @Bind(R.id.profile_image_signup)
    ImageView _profileImage;
    @Bind(R.id.input_name_signup)
    EditText _nameText;
    @Bind(R.id.input_email_signup)
    EditText _emailText;
    @Bind(R.id.input_password_signup)
    EditText _passwordText;
    @Bind(R.id.input_password_again_signup)
    EditText _reEnterPasswordText;
    @Bind(R.id.btn_login_signup)
    Button _signupButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_signup, container, false);
        ButterKnife.bind(this, view);
        ButterKnife.bind(this, view);

        ezPhotoPickStorage = new EZPhotoPickStorage(getActivity());

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "open camera", Toast.LENGTH_LONG).show();

                try {
                    EZPhotoPickConfig config = new EZPhotoPickConfig();
                    config.photoSource = PhotoSource.CAMERA;
                    config.storageDir = DEMO_PHOTO_PATH;
                    config.needToAddToGallery = true;
                    config.exportingSize = 1000;
                    EZPhotoPick.startPhotoPickActivity(SignUpFragment.this, config);

                } catch (PhotoIntentException e) {
                    e.printStackTrace();
                }
            }
        });
        return view;
    }


    public void signup() {

        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(getContext(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        // TODO: Implement your own signup logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        getActivity().setResult(RESULT_OK, null);
        startActivity(new Intent(getContext(), MainActivity.class));
        getActivity().finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getContext(), "Login failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError("Password Do not match");
            valid = false;
        } else {
            _reEnterPasswordText.setError(null);
        }

        return valid;
    }

    public void pickImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != Activity.RESULT_OK) {
            Toast.makeText(this.getContext(), "Sono in questo IF improbabile", Toast.LENGTH_SHORT).show();
            return;
        }

        if(requestCode == EZPhotoPick.PHOTO_PICK_CAMERA_REQUEST_CODE)
        {
            try {
                ArrayList<String> pickedPhotoNames = data.getStringArrayListExtra(EZPhotoPick.PICKED_PHOTO_NAMES_KEY);
                Bitmap pickedPhoto = ezPhotoPickStorage.loadLatestStoredPhotoBitmap(300);

                _profileImage.setImageBitmap(pickedPhoto);

                String profPhoto = ezPhotoPickStorage.getAbsolutePathOfStoredPhoto(DEMO_PHOTO_PATH, pickedPhotoNames.get(0));

                Toast.makeText(this.getContext(), "IMMA " + profPhoto, Toast.LENGTH_LONG).show();

                //setupImagePicker(ezPhotoPickStorage.getAbsolutePathOfStoredPhoto(DEMO_PHOTO_PATH, pickedPhotoNames.get(0)));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}