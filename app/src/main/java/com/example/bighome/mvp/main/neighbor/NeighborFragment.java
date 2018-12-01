package com.example.bighome.mvp.main.neighbor;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bighome.R;
import com.example.bighome.mvp.main.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class NeighborFragment extends Fragment {

    private MainActivity mainActivity;

    @BindView(R.id.btnJoin)
    Button btnJoin;
    @BindView(R.id.name)
    EditText txtName;

    private Unbinder unbinder;

    public static NeighborFragment newInstance() {
        return new NeighborFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_neighbor, container, false);
        unbinder=ButterKnife.bind(this,view);

        btnJoin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (txtName.getText().toString().trim().length() > 0) {

                    String name = txtName.getText().toString().trim();
                    Intent intent = new Intent(mainActivity,
                            ChatActivity.class);
                    intent.putExtra("name", name);

                    startActivity(intent);

                } else {
                    Toast.makeText(mainActivity,
                            "Please enter your name", Toast.LENGTH_LONG).show();
                }
            }
        });
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity=(MainActivity) getActivity();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
