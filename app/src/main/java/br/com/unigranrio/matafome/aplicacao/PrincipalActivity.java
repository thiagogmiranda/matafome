package br.com.unigranrio.matafome.aplicacao;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import br.com.unigranrio.matafome.R;

public class PrincipalActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_principal);

        Button btn = (Button)findViewById(R.id.dummy_button);

        final Context btnContext = this;

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(btnContext, LoginActivity.class);

                startActivity(intent);
            }
        });
    }
}
