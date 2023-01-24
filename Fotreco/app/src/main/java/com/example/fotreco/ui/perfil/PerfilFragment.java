package com.example.fotreco.ui.perfil;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.fotreco.R;
import com.example.fotreco.acitivity.LoginActivity;
import com.example.fotreco.databinding.FragmentPerfilBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PerfilFragment extends Fragment {

    private FragmentPerfilBinding binding;
    private TextView user_email, user_nome;
    private Button btn_logout;

    private DatabaseReference mDatabaseRef;
    private FirebaseAuth mAuth;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentPerfilBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        btn_logout = (Button) root.findViewById(R.id.btn_logout);
        user_email = (TextView) root.findViewById(R.id.user_email);
        user_nome = (TextView) root.findViewById(R.id.user_nome);

        mAuth = FirebaseAuth.getInstance();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Usuarios/"+mAuth.getUid());

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String nome = snapshot.child("nome").getValue().toString();
                String email = snapshot.child("email").getValue().toString();

                user_nome.setText(nome.toUpperCase());
                user_email.setText(email);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Toast toast10 = Toast.makeText(getActivity(), "Logout efetuado!", Toast.LENGTH_SHORT);
                toast10.setGravity(Gravity.CENTER, 0, 0);
                toast10.show();

                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        return root;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
