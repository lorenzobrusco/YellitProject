package unical.master.computerscience.yellit.graphic.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;


import android.util.Log;
import android.widget.EditText;
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
import unical.master.computerscience.yellit.R;
import unical.master.computerscience.yellit.connection.LoginService;
import unical.master.computerscience.yellit.connection.SigninService;
import unical.master.computerscience.yellit.logic.InfoManager;
import unical.master.computerscience.yellit.logic.objects.User;
import unical.master.computerscience.yellit.utilities.BaseURL;
import unical.master.computerscience.yellit.utilities.PrefManager;

public class LoginFragment extends Fragment {

    private static final String TAG = "LoginFragment";
    private static final int REQUEST_SIGNUP = 0;
    private CallbackManager callbackManager;
    @Bind(R.id.input_email)
    protected EditText _emailText;
    @Bind(R.id.input_password)
    protected EditText _passwordText;
    @Bind(R.id.btn_login)
    protected Button _loginButton;
    @Bind(R.id.btn_fb_login_signup)
    protected LoginButton _facebookSignButton;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_login, container, false);
        ButterKnife.bind(this, view);
        progressDialog = new ProgressDialog(LoginFragment.this.getContext());
        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (validate()) {
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Authenticating...");
                    progressDialog.show();
                    login();
                }
            }
        });

        callbackManager = CallbackManager.Factory.create();

        _facebookSignButton.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends"));
        _facebookSignButton.setFragment(this);

        _facebookSignButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>()

        {
            @Override
            public void onSuccess(LoginResult loginResult) {

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {

                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());

                                try {

                                    final String email = object.getString("email");

                                    Retrofit retrofit = new Retrofit.Builder()
                                            .baseUrl(BaseURL.URL)
                                            .addConverterFactory(GsonConverterFactory.create())
                                            .build();
                                    final LoginService loginService = retrofit.create(LoginService.class);
                                    Call<User> call = loginService.getProfile(email, "");
                                    call.enqueue(new Callback<User>() {
                                        @Override
                                        public void onResponse(Call<User> call, Response<User> response) {

                                            User profile = response.body();
                                            InfoManager.getInstance().setmUser(profile);
                                            if (profile.getEmail() == null) {
                                                LoginFragment.this.onLoginFailed();
                                                Log.d("retrofit", "email o password errati");
                                            } else {
                                                Log.d("nick", profile.getNickname());
                                                LoginFragment.this.onLoginSuccess(email);
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<User> call, Throwable t) {
                                            Log.e("Facebook signin", "errore di signin");
                                        }
                                    });

                                } catch (JSONException e) {
                                    e.printStackTrace();
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
                Log.e("LOGINFACEBOOKERROR", "vabbè");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.e("LOGINFACEBOOKERROR", "mannaia");
            }
        });

        return view;
    }

    public boolean login() {
        Log.d(TAG, "Login");

        final String email = _emailText.getText().toString();
        final String password = _passwordText.getText().toString();

        boolean result = this.login(email, password);
        return result;

    }

    public void onLoginSuccess(String name) {
        progressDialog.dismiss();
        PrefManager.getInstace(getContext()).setUser(_emailText.getText().toString());
        startActivity(new Intent(getContext(), WelcomeActivity.class));
        getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        getActivity().finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getContext(), "Login failed", Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        //TODO fix this crash
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty()) {
            _passwordText.setError("enter a password");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    private boolean login(String email, String password) {
        /** the method required this array*/
        final boolean[] correct = {false};
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseURL.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        LoginService loginService = retrofit.create(LoginService.class);
        Call<User> call = loginService.getProfile(email, password);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                User profile = response.body();
                InfoManager.getInstance().setmUser(profile);
                Toast.makeText(getContext(), profile.getEmail(), Toast.LENGTH_LONG).show();
                if (profile.getEmail() == null) {
                    LoginFragment.this.buildErrorDialog();
                    Log.d("retrofit", "email o password errati");
                    buildErrorDialog();
                    correct[0] = false;
                } else {
                    onLoginSuccess(null);
                    Log.d("nick", profile.getNickname());
                    correct[0] = true;
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("retrofit", "errore di log");
                correct[0] = false;
            }
        });
        return correct[0];
    }

    private void buildErrorDialog() {
        progressDialog.dismiss();
        new AlertDialog.Builder(getContext())
                .setTitle("Login failed")
                .setMessage("email or password are wrong, if you don't have an account create it")
                .setCancelable(false)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
        this._emailText.setText("");
        this._passwordText.setText("");
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

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
    }
}
