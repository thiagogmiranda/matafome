package br.com.unigranrio.matafome.aplicacao;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
        btn.setVisibility(View.INVISIBLE);

        final Context btnContext = this;

        SharedPreferences prefs = getSharedPreferences("mata_fome", 0);
        boolean jaLogou = prefs.getBoolean("tem_usuario_logado", false);

        if(jaLogou){
            btn.setVisibility(View.INVISIBLE);

            Intent intent = new Intent();
            intent.setClass(this, AtividadesActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(intent);
            finish();
        } else {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(btnContext, LoginActivity.class);

                    startActivity(intent);
                }
            });
            btn.setVisibility(View.VISIBLE);
        }
    }
}
