package unical.master.computerscience.yellit.graphic.Activities;


import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Debug;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import butterknife.ButterKnife;
import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import siclo.com.ezphotopicker.api.EZPhotoPick;
import siclo.com.ezphotopicker.api.EZPhotoPickStorage;
import siclo.com.ezphotopicker.api.models.EZPhotoPickConfig;
import siclo.com.ezphotopicker.api.models.PhotoSource;
import siclo.com.ezphotopicker.models.PhotoIntentException;
import unical.master.computerscience.yellit.MainActivity;
import unical.master.computerscience.yellit.R;
import unical.master.computerscience.yellit.connection.LoginService;
import unical.master.computerscience.yellit.connection.SigninService;
import unical.master.computerscience.yellit.logic.InfoManager;
import unical.master.computerscience.yellit.logic.objects.User;
import unical.master.computerscience.yellit.utilities.BaseURL;
import unical.master.computerscience.yellit.utilities.PrefManager;

import static android.app.Activity.RESULT_OK;

public class SignUpFragment extends Fragment {

    private static final String DEMO_PHOTO_PATH = "MyDemoPhotoDir";
    private static final String TAG = "SignupFragment";

    private Dialog choosePhotoDialog;
    private String currentPhotoPath;
    private CallbackManager callbackManager;
    private EZPhotoPickStorage ezPhotoPickStorage;

    @Bind(R.id.profile_image_signup)
    protected ImageView _profileImage;
    @Bind(R.id.input_name_signup)
    protected EditText _nameText;
    @Bind(R.id.input_email_signup)
    protected EditText _emailText;
    @Bind(R.id.input_password_signup)
    protected EditText _passwordText;
    @Bind(R.id.input_password_again_signup)
    protected EditText _reEnterPasswordText;
    @Bind(R.id.btn_login_signup)
    protected Button _signupButton;
    @Bind(R.id.btn_fb_login_signup)
    protected LoginButton _facebookSignButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_signup, container, false);
        ButterKnife.bind(this, view);
        ButterKnife.bind(this, view);
        currentPhotoPath = "";
        ezPhotoPickStorage = new EZPhotoPickStorage(getActivity());
        choosePhotoDialog = buildDialogFilter();
        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });
        /** The below code is used to login with facebook */
        callbackManager = CallbackManager.Factory.create();
        _facebookSignButton.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends"));
        _facebookSignButton.setFragment(this);
        _facebookSignButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                final GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {

                                try {

                                    final String email = object.getString("email");
                                    String nameFirst = object.getString("name"); // 01/31/1980 format

                                    Retrofit retrofit = new Retrofit.Builder()
                                            .baseUrl(BaseURL.URL)
                                            .addConverterFactory(GsonConverterFactory.create())
                                            .build();
                                    SigninService signinService = retrofit.create(SigninService.class);
                                    Call<User> call = signinService.createProfile(nameFirst, email, "");
                                    call.enqueue(new Callback<User>() {
                                        @Override
                                        public void onResponse(Call<User> call, Response<User> response) {
                                            final User profile = response.body();
                                            InfoManager.getInstance().setmUser(profile);
                                            PrefManager.getInstace(SignUpFragment.this.getContext()).setUser(email + "#" + "NULL");
                                            if (profile.getEmail() == null) {
                                                SignUpFragment.this.onSignupFailed();
                                                Toast.makeText(getContext(),"Error during load facebook info",Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getContext(),"Error during load facebook info",Toast.LENGTH_SHORT).show();
                                                onSignupSuccess(email);
                                            }
                                        }
                                        @Override
                                        public void onFailure(Call<User> call, Throwable t) {
                                            Toast.makeText(getContext(),"Error during load facebook info",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } catch (JSONException e) {
                                    Toast.makeText(getContext(),"Error during load facebook info",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }
            @Override
            public void onCancel() {
                Log.e("CANCEL", "vabbè");
            }
            @Override
            public void onError(FacebookException exception) {
                Log.e("ERRORE", "mannaia");
            }
        });
        _profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhotoDialog.show();
            }
        });
        return view;
    }

    /**
     * @return dialog where user can choose how to get an image
     * either from gallery or camera
     */
    private Dialog buildDialogFilter() {
        //TODO Salvatore fix it because when we get an image and sign in it ignores the image
        final Dialog dialog = new Dialog(getContext());
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_signin);
        dialog.setTitle("Load photo from..");
        final Button camButton = (Button) dialog.findViewById(R.id.signin_cam_button);
        camButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final EZPhotoPickConfig config = new EZPhotoPickConfig();
                    config.photoSource = PhotoSource.CAMERA;
                    config.storageDir = DEMO_PHOTO_PATH;
                    config.needToAddToGallery = true;
                    config.exportingSize = 1000;
                    EZPhotoPick.startPhotoPickActivity(SignUpFragment.this, config);
                } catch (PhotoIntentException e) {
                    Toast.makeText(getContext(), "Error during load photo", Toast.LENGTH_SHORT).show();
                }

            }
        });
        final Button gallButton = (Button) dialog.findViewById(R.id.signin_gall_button);
        gallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final EZPhotoPickConfig config = new EZPhotoPickConfig();
                    config.photoSource = PhotoSource.GALERY;
                    config.needToExportThumbnail = true;
                    config.isAllowMultipleSelect = true;
                    config.storageDir = DEMO_PHOTO_PATH;
                    config.exportingThumbSize = 200;
                    config.exportingSize = 1000;
                    EZPhotoPick.startPhotoPickActivity(SignUpFragment.this, config);
                } catch (PhotoIntentException e) {
                    Toast.makeText(getContext(), "Error during get photo from gallery", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return dialog;
    }

    /**
     * It used to take the credentials and calls the query to add user in the database
     */
    private void signup() {
        if (!validate()) {
            onSignupFailed();
            return;
        }
        _signupButton.setEnabled(false);
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();
        final String name = _nameText.getText().toString();
        final String email = _emailText.getText().toString();
        final String password = _passwordText.getText().toString();
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseURL.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final SigninService signinService = retrofit.create(SigninService.class);
        final Call<User> call = signinService.createProfile(name, email, password);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                final User profile = response.body();
                InfoManager.getInstance().setmUser(profile);
                if (profile.getEmail() == null) {
                    SignUpFragment.this.onSignupFailed();
                } else {
                    PrefManager.getInstace(getContext()).setUser(email + "#" + password);
                    onSignupSuccess(null);
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                buildDialogFilter();
            }
        });
    }

    /**
     * It used to start the next activity because the sign in is successed
     */
    private void onSignupSuccess(String name) {
        _signupButton.setEnabled(true);
        getActivity().setResult(RESULT_OK, null);
        PrefManager.getInstace(getContext()).setUser(name == null ? _emailText.getText().toString() : name);
        startActivity(new Intent(getContext(), MainActivity.class));
        getActivity().finish();
    }

    /**
     * It used when the sign in is failed and notify the user
     */
    public void onSignupFailed() {
        Toast.makeText(getContext(), "Signin failed", Toast.LENGTH_LONG).show();
        _signupButton.setEnabled(true);
    }

    /**
     * It used to chack if the input is correct or not
     */
    //TODO fix validate
    public boolean validate() {
        boolean valid = true;
        final String name = _nameText.getText().toString();
        final String email = _emailText.getText().toString();
        final String password = _passwordText.getText().toString();
        final String reEnterPassword = _reEnterPasswordText.getText().toString();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != Activity.RESULT_OK) {
            Toast.makeText(this.getContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (FacebookSdk.isFacebookRequestCode(requestCode)) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
        if (requestCode == EZPhotoPick.PHOTO_PICK_CAMERA_REQUEST_CODE) {
            try {
                final ArrayList<String> pickedPhotoNames = data.getStringArrayListExtra(EZPhotoPick.PICKED_PHOTO_NAMES_KEY);
                final Bitmap pickedPhoto = ezPhotoPickStorage.loadLatestStoredPhotoBitmap(300);
                _profileImage.setImageBitmap(pickedPhoto);
                currentPhotoPath = ezPhotoPickStorage.getAbsolutePathOfStoredPhoto(DEMO_PHOTO_PATH, pickedPhotoNames.get(0));
            } catch (IOException e) {
                Toast.makeText(getContext(), "Error during load photo", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == EZPhotoPick.PHOTO_PICK_GALERY_REQUEST_CODE) {
            try {
                final ArrayList<String> pickedPhotoNames = data.getStringArrayListExtra(EZPhotoPick.PICKED_PHOTO_NAMES_KEY);
                for (final String photoName : pickedPhotoNames) {
                    final Bitmap pickedPhoto = ezPhotoPickStorage.loadStoredPhotoBitmap(DEMO_PHOTO_PATH, photoName, 300);
                    _profileImage.setImageBitmap(pickedPhoto);
                    currentPhotoPath = ezPhotoPickStorage.getAbsolutePathOfStoredPhoto(DEMO_PHOTO_PATH, photoName);
                }
            } catch (IOException e) {
                Toast.makeText(getContext(), "Error during load photo", Toast.LENGTH_SHORT).show();
            }
        }
        choosePhotoDialog.dismiss();
    }
}