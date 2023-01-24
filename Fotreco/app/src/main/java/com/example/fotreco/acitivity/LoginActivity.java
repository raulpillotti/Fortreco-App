package com.example.fotreco.acitivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fotreco.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private ProgressBar loginProgressBar;
    private EditText edit_email, edit_senha;
    private Button btn_login, btn_registrar;
    private CheckBox mostra_senha;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        edit_email = (EditText) findViewById(R.id.edit_email);
        edit_senha = (EditText) findViewById(R.id.edit_senha);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_registrar = (Button) findViewById(R.id.btn_registrar);
        mostra_senha = (CheckBox) findViewById(R.id.mostra_senha);
        loginProgressBar = (ProgressBar) findViewById(R.id.loginProgressBar);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String loginEmail = edit_email.getText().toString();
                String loginSenha = edit_senha.getText().toString();

                if(!TextUtils.isEmpty(loginEmail) && !TextUtils.isEmpty(loginSenha)){
                    loginProgressBar.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(loginEmail, loginSenha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                abrirTelaPrincipal();
                            } else {
                                String error = task.getException().getMessage();
                                Toast.makeText(LoginActivity.this, ""+error, Toast.LENGTH_SHORT).show();
                                loginProgressBar.setVisibility(View.INVISIBLE);
                            }
                        }

                        private void abrirTelaPrincipal() {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            Toast toast11 = Toast.makeText(LoginActivity.this, "Login efetuado!", Toast.LENGTH_SHORT);
                            toast11.setGravity(Gravity.CENTER, 0, 0);
                            toast11.show();
                            startActivity(intent);
                            finish();
                        }
                    });

                }
            }
        });

        btn_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mostra_senha.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            if(isChecked){
                edit_senha.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                edit_senha.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

    }
}
