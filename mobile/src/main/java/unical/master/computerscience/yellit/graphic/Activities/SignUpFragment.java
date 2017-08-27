package unical.master.computerscience.yellit.graphic.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
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
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import butterknife.ButterKnife;
import butterknife.Bind;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
import unical.master.computerscience.yellit.connection.SigninService;
import unical.master.computerscience.yellit.connection.UsersService;
import unical.master.computerscience.yellit.logic.InfoManager;
import unical.master.computerscience.yellit.logic.objects.User;
import unical.master.computerscience.yellit.utilities.BaseURL;
import unical.master.computerscience.yellit.utilities.PrefManager;
import unical.master.computerscience.yellit.utilities.UpdateGoogleInfo;

import static android.app.Activity.RESULT_OK;

public class SignUpFragment extends Fragment {

    private static final String DEMO_PHOTO_PATH = "MyDemoPhotoDir";

    private Dialog choosePhotoDialog;
    private String currentPhotoPath;
    private CallbackManager callbackManager;
    private EZPhotoPickStorage ezPhotoPickStorage;
    private ProgressDialog mProgressDialog;
    @Bind(R.id.profile_image_signup)
    protected ImageView _profileImage;
    @Bind(R.id.input_name_signup)
    protected TextInputEditText _nameText;
    @Bind(R.id.input_email_signup)
    protected TextInputEditText _emailText;
    @Bind(R.id.input_password_signup)
    protected TextInputEditText _passwordText;
    @Bind(R.id.input_password_again_signup)
    protected TextInputEditText _reEnterPasswordText;
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
        mProgressDialog = new ProgressDialog(getContext());
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
                                    final String nameFirst = object.getString("name");
                                    final String id = object.getString("id");

                                    /** To get user image from facebook*/
                                    final String filenameImage = "https://graph.facebook.com/" + id + "/picture?type=large";
                                    Retrofit retrofit = new Retrofit.Builder()
                                            .baseUrl(BaseURL.URL)
                                            .addConverterFactory(GsonConverterFactory.create())
                                            .build();
                                    SigninService signinService = retrofit.create(SigninService.class);
                                    Call<User> call = signinService.createProfileWithoutFile(filenameImage, nameFirst, email, email, "");
                                    call.enqueue(new Callback<User>() {
                                        @Override
                                        public void onResponse(Call<User> call, Response<User> response) {
                                            final User profile = response.body();
                                            if (profile != null && profile.getEmail() != null) {
                                                InfoManager.getInstance().setmUser(profile);
                                                PrefManager.getInstace(SignUpFragment.this.getContext()).setUser(email + "#" + "NULL");
                                                onSignupSuccess();
                                            } else {
                                                SignUpFragment.this.onSignupFailed();
                                                Toast.makeText(getContext(), "Error during load facebook info", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<User> call, Throwable t) {
                                            Toast.makeText(getContext(), "Error during load facebook info", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } catch (JSONException e) {
                                    Toast.makeText(getContext(), "Error during load facebook info", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getContext(), "Error during load facebook info", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(getContext(), "Error during load facebook info", Toast.LENGTH_SHORT).show();
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
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setMessage("Creating Account...");
        mProgressDialog.show();
        final String name = _nameText.getText().toString();
        final String email = _emailText.getText().toString();
        final String password = _passwordText.getText().toString();
        if (!currentPhotoPath.equals("")) {
            final File file = new File(currentPhotoPath);
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
            RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
            RequestBody newName = RequestBody.create(MediaType.parse("text/plain"), name);
            RequestBody newEmail = RequestBody.create(MediaType.parse("text/plain"), email);
            RequestBody newPassword = RequestBody.create(MediaType.parse("text/plain"), password);
            final Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BaseURL.URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            final SigninService signinService = retrofit.create(SigninService.class);
            final Call<User> call = signinService.createProfile(fileToUpload, filename, newName, newEmail, newPassword);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    final User profile = response.body();
                    if (profile != null) {
                        InfoManager.getInstance().setmUser(profile);
                        PrefManager.getInstace(getContext()).setUser(email + "#" + password);
                        mProgressDialog.dismiss();
                        onSignupSuccess();
                    } else {
                        buildDialogFilter();
                    }

                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    buildDialogFilter();
                }
            });
        } else {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BaseURL.URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            SigninService signinService = retrofit.create(SigninService.class);
            Call<User> call = signinService.createProfileWithoutFile("", name, "", email, password);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    final User profile = response.body();
                    if (profile != null && profile.getEmail() != null) {
                        InfoManager.getInstance().setmUser(profile);
                        PrefManager.getInstace(SignUpFragment.this.getContext()).setUser(email + "#" + password);
                        onSignupSuccess();
                    } else {
                        SignUpFragment.this.onSignupFailed();
                        Toast.makeText(getContext(), "Error during load facebook info", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(getContext(), "Error during load facebook info", Toast.LENGTH_SHORT).show();
                }
            });
        }


    }

    /**
     * It used to start the next activity because the sign in is successed
     */
    private void onSignupSuccess() {
        UpdateGoogleInfo.update((AppCompatActivity) getActivity());
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseURL.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final UsersService loginService = retrofit.create(UsersService.class);
        Call<User[]> call = loginService.getAllUsers("allUsers");
        call.enqueue(new Callback<User[]>() {
            @Override
            public void onResponse(Call<User[]> call, Response<User[]> response) {
                User[] users = response.body();
                List<User> usersList = new ArrayList<>();
                for (User user : users) {
                    usersList.add(user);
                }
                InfoManager.getInstance().setmAllUsers(usersList);
                mProgressDialog.dismiss();
                getActivity().setResult(RESULT_OK, null);
                startActivity(new Intent(getContext(), WelcomeActivity.class));
                getActivity().finish();
            }

            @Override
            public void onFailure(Call<User[]> call, Throwable t) {
                mProgressDialog.dismiss();
                getActivity().setResult(RESULT_OK, null);
                startActivity(new Intent(getContext(), MainActivity.class));
                getActivity().finish();
            }
        });

    }

    /**
     * It used when the sign in is failed and notify the user
     */
    public void onSignupFailed() {
        buildErrorDialog();
        Toast.makeText(getContext(), "Signin failed", Toast.LENGTH_LONG).show();
    }

    /**
     * It used to check if the input is correct or not
     */
    public boolean validate() {
        boolean valid = true;
        final String name = _nameText.getText().toString();
        final String email = _emailText.getText().toString();
        final String password = _passwordText.getText().toString();
        final String reEnterPassword = _reEnterPasswordText.getText().toString();
        if (name.isEmpty() || name.length() < 3) {
            valid = false;
        }
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            valid = false;
        }
        if (password.isEmpty()) {
            valid = false;
        }
        if (!(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError("Password Do not match");
            Toast.makeText(getContext(), "Password Do not match", Toast.LENGTH_SHORT).show();
            valid = false;
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
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setMessage("Getting info from Facebook");
            mProgressDialog.show();
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
                    Toast.makeText(getContext(), currentPhotoPath, Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                Toast.makeText(getContext(), "Error during load photo", Toast.LENGTH_SHORT).show();
            }
        }
        choosePhotoDialog.dismiss();
    }


    /**
     * It called to create a dialog that notify to the user an error incontrato during the login
     */
    private void buildErrorDialog() {
        mProgressDialog.dismiss();
        new AlertDialog.Builder(getContext())
                .setTitle("Sign in  failed")
                .setMessage(R.string.signin_constraint)
                .setCancelable(false)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
        this._emailText.setText("");
        this._passwordText.setText("");
        final InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

    }


}