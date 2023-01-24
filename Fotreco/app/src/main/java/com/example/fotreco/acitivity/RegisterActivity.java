package com.example.fotreco.acitivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
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
import com.example.fotreco.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class RegisterActivity extends AppCompatActivity {

    private EditText edit_email_reg, edit_senha_reg, edit_confi_senha, nome_reg, sobrenome_reg;
    private Button btn_registrar_reg, btn_login_reg;
    private CheckBox mostra_senha_regi;
    private ProgressBar regProgressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        nome_reg = (EditText) findViewById(R.id.nome_reg);
        sobrenome_reg = (EditText) findViewById(R.id.sobrenome_reg);

        edit_email_reg = (EditText) findViewById(R.id.edit_email_reg);
        edit_senha_reg = (EditText) findViewById(R.id.edit_senha_reg);
        edit_confi_senha = (EditText) findViewById(R.id.edit_confi_senha);
        btn_registrar_reg = (Button) findViewById(R.id.btn_registrar_reg);
        btn_login_reg = (Button) findViewById(R.id.btn_login_reg);
        mostra_senha_regi = (CheckBox) findViewById(R.id.mostra_senha_regi);
        regProgressBar = (ProgressBar) findViewById(R.id.regProgressBar);

        mostra_senha_regi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    edit_senha_reg.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    edit_confi_senha.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    edit_senha_reg.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    edit_confi_senha.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        btn_registrar_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                User user = new User();

                user.setEmail(edit_email_reg.getText().toString());
                user.setNome(nome_reg.getText().toString());
                user.setSobrenome(sobrenome_reg.getText().toString());

                String registerSenha = edit_senha_reg.getText().toString();
                String registerSenhaConfir = edit_confi_senha.getText().toString();

                if(!TextUtils.isEmpty(user.getNome()) && !TextUtils.isEmpty(user.getSobrenome()) && !TextUtils.isEmpty(user.getEmail()) && !TextUtils.isEmpty(registerSenha) && !TextUtils.isEmpty(registerSenhaConfir)){
                    if(registerSenha.equals(registerSenhaConfir)){
                        regProgressBar.setVisibility(View.VISIBLE);

                        mAuth.createUserWithEmailAndPassword(user.getEmail(), registerSenha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    user.setId(mAuth.getUid());
                                    user.salvar();
                                    abrirTelaPrincipal();
                                } else {
                                    String error;
                                    try {
                                        throw task.getException();
                                    } catch (FirebaseAuthWeakPasswordException e) {
                                        error = "A senha deve conter no mínimo 6 caracteres.";

                                    } catch (FirebaseAuthInvalidCredentialsException e) {
                                        error = "E-mail inválido.";

                                    } catch (FirebaseAuthUserCollisionException e) {
                                        error = "Usuário já cadastrado.";

                                    } catch (Exception e) {
                                        error = "Erro ao efetuar o cadastro.";
                                        e.printStackTrace();
                                    }
                                    Toast.makeText(RegisterActivity.this, ""+error, Toast.LENGTH_SHORT).show();
                                }

                                regProgressBar.setVisibility(View.INVISIBLE);

                            }
                        });
                    } else {
                        Toast.makeText(RegisterActivity.this, "As senhas devem ser iguais.",Toast.LENGTH_SHORT).show();

                    }

                } else {
                    Toast.makeText(RegisterActivity.this, "Preencha todos os campos.",Toast.LENGTH_SHORT).show();

                }

            }
        });

        btn_login_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void abrirTelaPrincipal() {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
