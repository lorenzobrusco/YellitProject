package unical.master.computerscience.yellit.graphic.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;


import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.zip.Inflater;

import javax.xml.transform.TransformerConfigurationException;

import butterknife.ButterKnife;
import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import unical.master.computerscience.yellit.R;
import unical.master.computerscience.yellit.connection.LoginService;
import unical.master.computerscience.yellit.logic.InfoManager;
import unical.master.computerscience.yellit.logic.objects.User;
import unical.master.computerscience.yellit.utilities.PrefManager;

import static android.app.Activity.RESULT_OK;

public class LoginActivity extends Fragment {
    private static final String TAG = "LoginActivity";
    private static final String BASEURL = "http://10.0.2.2:8080/YellitServer/";
    private static final int REQUEST_SIGNUP = 0;

    @Bind(R.id.input_email)
    EditText _emailText;
    @Bind(R.id.input_password)
    EditText _passwordText;
    @Bind(R.id.btn_login)
    Button _loginButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_login, container, false);
        ButterKnife.bind(this, view);
        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //TODO replace this code with login();
                // login();
                onLoginSuccess();
            }
        });

        return view;
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);


        final String email = _emailText.getText().toString();
        final String password = _passwordText.getText().toString();
       /* final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();*/
        this.login(email, password);
        //onLoginSuccess();
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
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

        if (email.isEmpty()) {// || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
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

    private void login(String email, String password) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        LoginService loginService = retrofit.create(LoginService.class);
        Call<User> call = loginService.getProfile(email, password);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                User profile = response.body();
                InfoManager.getInstance().setmUser(profile);
                if (profile.getEmail() == null) {
                    LoginActivity.this.buildErrorDialog();
                    Log.d("retrofit", "email o password errati");
                } else {
                    Log.d("nick", profile.getNickname());
                    onLoginSuccess();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("retrofit", "errore di log");
            }
        });
    }

    private void buildErrorDialog() {
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
}
