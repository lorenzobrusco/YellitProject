package unical.master.computerscience.yellit.graphic.Activities;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Handler;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import butterknife.Bind;
import butterknife.ButterKnife;
import unical.master.computerscience.yellit.MainActivity;
import unical.master.computerscience.yellit.R;
import unical.master.computerscience.yellit.utilities.PrefManager;

import static unical.master.computerscience.yellit.utilities.SystemUI.changeSystemBar;

/**
 * Activity used if user enable this mode and required a code to pass it,
 * either with fingerprint or a code, whether the device hasn't the fingerprint hardware it'll skipped
 */
public class SafeModeActivity extends AppCompatActivity {

    private static final String KEY_NAME = "key";
    @Bind(R.id.password_safe_mode_editText)
    protected EditText mPasswordSafeModeEditText;
    @Bind(R.id.confirm_fingerprint)
    protected FloatingActionButton mConfirmFingerPrintFloatingActionButton;

    /**
     * Fingerprint variables
     */
    private KeyguardManager mKeyguardManager;

    private FingerprintManager mFingerprintManager;

    private KeyStore mKeyStore;

    private KeyGenerator mKeyGenerator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe_mode);
        ButterKnife.bind(this);
        this.mPasswordSafeModeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("1111")) {
                    final View view = getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    final Animation expandIn = AnimationUtils.loadAnimation(SafeModeActivity.this, R.anim.expand_out);
                    mConfirmFingerPrintFloatingActionButton.startAnimation(expandIn);
                    mConfirmFingerPrintFloatingActionButton.setVisibility(View.VISIBLE);
                    final Handler handler = new Handler();
                    final Runnable runnable = new Runnable() {
                        public void run() {

                            startActivity(new Intent(getBaseContext(), WelcomeActivity.class));
                            finish();
                        }
                    };
                    handler.postDelayed(runnable, 1000);
                }
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
            changeSystemBar(this, false);
            this.mKeyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
            this.mFingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
            final FingerprintHandler mFingerprintHandler = new FingerprintHandler(mConfirmFingerPrintFloatingActionButton, 5);
            if (!this.checkFinger()) {
                startActivity(new Intent(getBaseContext(), LoginSignupActivity.class));
                finish();
            } else {
                try {

                    this.generateKey();
                    final Cipher mCipher = this.generateCipher();
                    final FingerprintManager.CryptoObject mCryptoObject = new FingerprintManager.CryptoObject(mCipher);
                    mFingerprintHandler.doAuth(mFingerprintManager, mCryptoObject);

                } catch (FingerprintException fpe) {
                    Toast.makeText(this,"Error with fingerprint, concatc the developers",Toast.LENGTH_SHORT).show();
                }
            }

        } else {
            startActivity(new Intent(getBaseContext(), LoginSignupActivity.class));
            finish();
        }

    }

    /**
     * @return true if and only if device has secure lock screen
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean checkFinger() {

        try {
            if (PrefManager.getInstace(getApplicationContext()).getUser() == null || !PrefManager.getInstace(getApplicationContext()).isSafeMode()) {
                return false;
            }
            if (!mFingerprintManager.isHardwareDetected()) {
                return false;
            }
            if (!mFingerprintManager.hasEnrolledFingerprints()) {
                return false;
            }
            if (!mKeyguardManager.isKeyguardSecure()) {
                return false;
            }

        } catch (SecurityException se) {
            se.printStackTrace();
        }
        return true;
    }

    /**
     * The above function  accesses to Android keystore and
     * generates key and also requires at least Android M
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void generateKey() throws FingerprintException {
        try {
            /** Get the reference to the key store */
            mKeyStore = KeyStore.getInstance("AndroidKeyStore");
            /** Key generator to generate the key */
            mKeyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES,
                    "AndroidKeyStore");
            mKeyStore.load(null);
            mKeyGenerator.init(new
                    KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            mKeyGenerator.generateKey();
        } catch (KeyStoreException
                | NoSuchAlgorithmException
                | NoSuchProviderException
                | InvalidAlgorithmParameterException
                | CertificateException
                | IOException exc) {
            exc.printStackTrace();
            throw new FingerprintException(exc);
        }
    }

    /**
     * @return
     */
    private Cipher generateCipher() throws FingerprintException {
        try {
            final Cipher cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                    + KeyProperties.BLOCK_MODE_CBC + "/"
                    + KeyProperties.ENCRYPTION_PADDING_PKCS7);
            final SecretKey key = (SecretKey) mKeyStore.getKey(KEY_NAME,
                    null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher;
        } catch (NoSuchAlgorithmException
                | NoSuchPaddingException
                | InvalidKeyException
                | UnrecoverableKeyException
                | KeyStoreException exc) {
            exc.printStackTrace();
            throw new FingerprintException(exc);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = MotionEventCompat.getActionMasked(event);

        switch (action) {
            case (MotionEvent.ACTION_DOWN):
                mPasswordSafeModeEditText.setVisibility(mPasswordSafeModeEditText.isShown() ? View.INVISIBLE : View.VISIBLE);
                return true;
            case (MotionEvent.ACTION_MOVE):
                return true;
            case (MotionEvent.ACTION_UP):
                return true;
            case (MotionEvent.ACTION_CANCEL):
                return true;
            case (MotionEvent.ACTION_OUTSIDE):
                return true;
            default:
                return super.onTouchEvent(event);
        }
    }

    /**
     * Fingerprint handler class
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private class FingerprintHandler extends FingerprintManager.AuthenticationCallback {

        private FloatingActionButton mFloatingActionButton;
        private int mCountAuthenticationError;

        private FingerprintHandler(final FloatingActionButton mFloatingActionButton, int mCountAuthenticationError) {
            this.mFloatingActionButton = mFloatingActionButton;
            this.mCountAuthenticationError = mCountAuthenticationError;
        }

        @Override
        public void onAuthenticationError(int errorCode, CharSequence errString) {
            super.onAuthenticationError(errorCode, errString);
        }

        @Override
        public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
            super.onAuthenticationHelp(helpCode, helpString);
        }

        @Override
        public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
            super.onAuthenticationSucceeded(result);
            Animation expandIn = AnimationUtils.loadAnimation(SafeModeActivity.this, R.anim.expand_out);
            mFloatingActionButton.startAnimation(expandIn);
            mFloatingActionButton.setVisibility(View.VISIBLE);
            final Handler handler = new Handler();
            final Runnable runnable = new Runnable() {
                public void run() {

                    startActivity(new Intent(getBaseContext(), MainActivity.class));
                    finish();
                }
            };
            handler.postDelayed(runnable, 1000);
        }

        @Override
        public void onAuthenticationFailed() {
            super.onAuthenticationFailed();
            Snackbar.make(SafeModeActivity.this.findViewById(R.id.layout_safe_mode), "You still have " + --mCountAuthenticationError + " attempts before the applications lock", Snackbar.LENGTH_LONG).show();
            if (mCountAuthenticationError <= 0) {
                //TODO future developments
                /** add a penalty if user wrong 5 times*/
            }
        }

        public void doAuth(FingerprintManager manager,
                           FingerprintManager.CryptoObject obj) {
            CancellationSignal signal = new CancellationSignal();
            try {
                manager.authenticate(obj, signal, 0, this, null);
            } catch (SecurityException sce) {
            }
        }
    }

    private class FingerprintException extends Exception {

        public FingerprintException(Exception e) {
            super(e);
        }

    }
}
