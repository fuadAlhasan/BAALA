package com.example.motherscaremod;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.motherscaremod.databinding.ActivityLoginBinding;
import com.google.android.material.tabs.TabLayout;


public class LoginActivity extends AppCompatActivity
{

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        setUpTabLayoutLogInSignUp();
        setUpListeners();
    }

    @Override
    public void finish() {
        binding.tabLayoutLogInSignUp.clearOnTabSelectedListeners();
        binding.tabLayoutUserRole.clearOnTabSelectedListeners();

        super.finish();
    }


    void setUpTabLayoutLogInSignUp()
    {
        binding.tabLayoutLogInSignUp.clearOnTabSelectedListeners();
        binding.tabLayoutLogInSignUp.addOnTabSelectedListener(new MyTabLayoutOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);

                if (tab.getTag() != null)
                    binding.setMode((LogInPageMode) tab.getTag());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                super.onTabReselected(tab);

                if (tab.getTag() != null)
                    binding.setMode((LogInPageMode) tab.getTag());
            }
        });

        TabLayout.Tab logInTab = binding.tabLayoutLogInSignUp.getTabAt(0);
        TabLayout.Tab signUpTab = binding.tabLayoutLogInSignUp.getTabAt(1);

        if (logInTab != null) {
            logInTab.setTag(LogInPageMode.LOG_IN);
            logInTab.select();
        }

        if (signUpTab != null)
            signUpTab.setTag(LogInPageMode.SIGN_UP);
    }

    void setUpListeners()
    {
        binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (areAllInfoValid()) {
                    // Uncomment the lines below to verify sample users
                    /*if ((binding.editTextName.getText() == null)
                            || (binding.editTextPassword.getText() == null)) {
                        Toast.makeText(LoginActivity.this, R.string.error_unknown,
                                Toast.LENGTH_LONG).show();
                        return;
                    }

                    String name = binding.editTextName.getText().toString();
                    String password = binding.editTextPassword.getText().toString();

                    if (binding.getMode() == LogInPageMode.LOG_IN) {
                        for (User user : SampleData.sampleUsers) {
                            if (user.name.equals(name) && user.password.equals(password)) {
                                startActivity(
                                        new Intent(LoginActivity.this,
                                                MainActivity.class)
                                );
                                finish();
                                return;
                            }
                        }

                        Toast.makeText(LoginActivity.this, R.string.error_userNotFound,
                                Toast.LENGTH_LONG).show();

                    } else if (binding.getMode() == LogInPageMode.SIGN_UP) {*/
                    startActivity(
                            new Intent(LoginActivity.this, MainActivity.class)
                    );
                    finish();
                    //}
                }
            }
        });
    }

    boolean areAllInfoValid()
    {
        boolean valid = true;

        if (TextUtils.isEmpty(binding.editTextName.getText())) {
            binding.textInputLayoutName.setError(
                    getString(R.string.error_nameFieldNecessary)
            );
            valid = false;

        } else {
            binding.textInputLayoutName.setError(null);
        }

        if (TextUtils.isEmpty(binding.editTextPassword.getText())) {
            binding.textInputLayoutPassword.setError(
                    getString(R.string.error_passwordFieldNecessary)
            );
            valid = false;

        } else {
            binding.textInputLayoutPassword.setError(null);
        }

        return valid;
    }

}
