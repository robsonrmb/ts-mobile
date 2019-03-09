package br.com.topspin.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import br.com.topspin.R;
import br.com.topspin.constantes.Constantes;
import br.com.topspin.helper.Preferencias;

public class PassosActivity extends AppCompatActivity {

    private Preferencias preferencias;

    private Toolbar toolbar;
    private Button btnOK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passos);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("PRIMEIROS PASSOS");
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);

        preferencias = new Preferencias(PassosActivity.this);
        if ("0".equals(preferencias.getTema())) {
            toolbar.setBackgroundColor(Color.parseColor(Constantes.QUADRA_RAPIDA));
        }else if ("1".equals(preferencias.getTema())) {
            toolbar.setBackgroundColor(Color.parseColor(Constantes.SAIBRO));
        }else{
            toolbar.setBackgroundColor(Color.parseColor(Constantes.GRAMA));
        }

        btnOK = (Button) findViewById(R.id.btnOK);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(PassosActivity.this, MainActivity.class));
                finish();

            }
        });

    }
}
