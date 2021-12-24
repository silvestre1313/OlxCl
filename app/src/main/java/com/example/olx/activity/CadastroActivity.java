package com.example.olx.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.olx.R;
import com.example.olx.helper.ConfiguracaoFirebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class CadastroActivity extends AppCompatActivity {

    private Button botaoAcessar;
    private EditText campoEmail, campoSenha;
    private Switch tipoAcesso;

    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        inicializarComponentes();
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        botaoAcessar.setOnClickListener(v -> {

            String email = campoEmail.getText().toString();
            String senha = campoSenha.getText().toString();

            if ( !email.isEmpty() ){
                if (!senha.isEmpty()){

                    //Verifica o estado switch
                    if (tipoAcesso.isChecked()){//Cadastro

                        autenticacao.createUserWithEmailAndPassword(
                                email, senha
                        ).addOnCompleteListener(task -> {
                            if (task.isSuccessful()){
                                Toast.makeText(CadastroActivity.this, "Cadastro efetuado com sucesso", Toast.LENGTH_SHORT).show();
                            }else{

                                String erroExcecao = "";

                                try {
                                    throw task.getException();
                                } catch (FirebaseAuthWeakPasswordException e){
                                    erroExcecao = "Digite uma senha mais forte";
                                } catch (FirebaseAuthInvalidCredentialsException e){
                                    erroExcecao = "Digite um email valido";
                                } catch (FirebaseAuthUserCollisionException e){
                                    erroExcecao = "Essa conta ja foi cadastrada";
                                } catch (Exception e){
                                    erroExcecao = "Erro ao cadastrar usuario: " + e.getMessage();
                                    e.printStackTrace();
                                }

                                Toast.makeText(CadastroActivity.this, "Erro: " + erroExcecao, Toast.LENGTH_SHORT).show();

                            }
                        });

                    }else{ //Login

                        autenticacao.signInWithEmailAndPassword(
                                email, senha
                        ).addOnCompleteListener(task -> {
                            if (task.isSuccessful()){

                                Toast.makeText(CadastroActivity.this, "Logado com sucesso", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), AnunciosActivity.class));

                            } else{

                                Toast.makeText(CadastroActivity.this, "Erro ao fazer login", Toast.LENGTH_SHORT).show();

                            }
                        });

                    }

                }else {
                    Toast.makeText(CadastroActivity.this, "Preencha a senha", Toast.LENGTH_SHORT).show();
                }

            }else{
                Toast.makeText(CadastroActivity.this, "Preencha o email", Toast.LENGTH_SHORT).show();
            }

        });

    }

    public void inicializarComponentes(){
        botaoAcessar = findViewById(R.id.buttonAcesso);
        campoEmail = findViewById(R.id.editCadastroEmail);
        campoSenha = findViewById(R.id.editCadastroSenha);
        tipoAcesso = findViewById(R.id.switchAcesso);
    }

}