package unical.master.computerscience.yellit.graphic.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import unical.master.computerscience.yellit.R;
import unical.master.computerscience.yellit.connection.LoginService;
import unical.master.computerscience.yellit.connection.UsersService;
import unical.master.computerscience.yellit.logic.InfoManager;
import unical.master.computerscience.yellit.logic.objects.User;
import unical.master.computerscience.yellit.utilities.BaseURL;
import unical.master.computerscience.yellit.utilities.PrefManager;

/**
 * Fragment to login
 */
public class LoginFragment extends Fragment {

    private CallbackManager callbackManager;
    private ProgressDialog progressDialog;
    @Bind(R.id.input_email)
    protected EditText _emailText;
    @Bind(R.id.input_password)
    protected EditText _passwordText;
    @Bind(R.id.btn_login)
    protected Button _loginButton;
    @Bind(R.id.btn_fb_login_signup)
    protected LoginButton _facebookSignButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_login, container, false);
        ButterKnife.bind(this, view);
        progressDialog = new ProgressDialog(LoginFragment.this.getContext());
        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                progressDialog.setIndeterminate(true);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Authenticating...");
                progressDialog.show();
                login(false);

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
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
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
                                    if (profile != null && profile.getEmail() != null) {
                                        InfoManager.getInstance().setmUser(profile);
                                        PrefManager.getInstace(LoginFragment.this.getContext()).setUser(email + "#" + "NULL");
                                        LoginFragment.this.onLoginSuccess();
                                    } else {
                                        buildErrorDialog();
                                    }
                                }

                                @Override
                                public void onFailure(Call<User> call, Throwable t) {
                                    Toast.makeText(getContext(), "facebook failure", Toast.LENGTH_SHORT).show();
                                    buildErrorDialog();
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
                buildErrorDialog();
            }

            @Override
            public void onError(FacebookException exception) {
                buildErrorDialog();
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != Activity.RESULT_OK) {
            Toast.makeText(this.getContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (FacebookSdk.isFacebookRequestCode(requestCode)) {
            progressDialog.setIndeterminate(true);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Authenticating...");
            progressDialog.show();
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }


    /**
     * It used to take the credentials and calls the query to check
     * whether user exists in the database
     *
     * @return true if the login is correct otherwise false
     */
    private boolean login(boolean facebook) {
        if (facebook)
            if (!validate()) {
                onLoginFailed();
                return false;
            }
        final String email = _emailText.getText().toString();
        final String password = _passwordText.getText().toString();
        return this.login(email, password);
    }

    /**
     * It used to start the next activity because the login is successed
     */
    private void onLoginSuccess() {
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
                List<User> usersList = new ArrayList<User>();
                for (User user : users) {
                    usersList.add(user);
                }
                InfoManager.getInstance().setmAllUsers(usersList);
                progressDialog.dismiss();
                startActivity(new Intent(getContext(), WelcomeActivity.class));
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                getActivity().finish();
            }

            @Override
            public void onFailure(Call<User[]> call, Throwable t) {
                progressDialog.dismiss();
                startActivity(new Intent(getContext(), WelcomeActivity.class));
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                getActivity().finish();
            }
        });
    }

    /**
     * It used when the login is failed and notify the user
     */
    public void onLoginFailed() {
        Toast.makeText(getContext(), "Login failed", Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(true);
        _facebookSignButton.setEnabled(true);
    }

    /**
     * It used to chack if the input is correct or not
     */
    //TODO Fix valide
    private boolean validate() {

        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    /**
     * @param email    user's email
     * @param password user's passwords
     * @return true if login is correct otherwise false
     */
    private boolean login(final String email, final String password) {
        /** the method required this array */
        final boolean[] correct = {false};
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseURL.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final LoginService loginService = retrofit.create(LoginService.class);
        Call<User> call = loginService.getProfile(email, password);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                final User profile = response.body();
                if (profile != null) {
                    if (profile.getEmail() == null) {
                        buildErrorDialog();
                    } else {
                        onLoginSuccess();
                        InfoManager.getInstance().setmUser(profile);
                        PrefManager.getInstace(getContext()).setUser(email + "#" + password);
                        correct[0] = true;
                    }
                } else {
                    buildErrorDialog();
                    correct[0] = false;
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                correct[0] = false;
            }
        });
        return correct[0];
    }

    /**
     * It called to create a dialog that notify to the user an error incontrato during the login
     */
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
        final InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

    }

}
