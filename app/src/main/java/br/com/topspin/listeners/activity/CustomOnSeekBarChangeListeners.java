package br.com.topspin.listeners.activity;

import android.graphics.Color;
import android.widget.SeekBar;
import android.widget.TextView;

import br.com.topspin.constantes.Constantes;

public class CustomOnSeekBarChangeListeners implements SeekBar.OnSeekBarChangeListener {

    private TextView txtResultado;
    private int qtdRespostas;

    public CustomOnSeekBarChangeListeners() {}
    public CustomOnSeekBarChangeListeners(TextView txtResultado, int qtdRespostas) {
        this.txtResultado = txtResultado;
        this.qtdRespostas = qtdRespostas;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        //Log.v("SeekBar: " + seekBar.getMax() + " - " + seekBar.getId(), "");
        if (qtdRespostas == 3) {
            setarResultadoAvaliativoComTresRespostas(progress, txtResultado);
        }else if (qtdRespostas == 4) {
            setarResultadoAvaliativoComQuatroRespostas(progress, txtResultado);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}


    public void setarResultadoAvaliativoComQuatroRespostas(int progress, TextView txtResultado) {
        if (progress == 0) {
            txtResultado.setText(Constantes.RUIM);
            txtResultado.setTextColor(Color.parseColor(Constantes.C_RUIM));
        }else if (progress == 1) {
            txtResultado.setText(Constantes.REGULAR);
            txtResultado.setTextColor(Color.parseColor(Constantes.C_REGULAR));
        }else if (progress == 2) {
            txtResultado.setText(Constantes.BOM);
            txtResultado.setTextColor(Color.parseColor(Constantes.C_BOM));
        }else if (progress == 3) {
            txtResultado.setText(Constantes.OTIMO);
            txtResultado.setTextColor(Color.parseColor(Constantes.C_OTIMO));
        }
    }

    public void setarResultadoAvaliativoComTresRespostas(int progress, TextView txtResultado) {
        if (progress == 0) {
            txtResultado.setText(Constantes.POUCO);
            txtResultado.setTextColor(Color.parseColor(Constantes.C_POUCO));
        }else if (progress == 1) {
            txtResultado.setText(Constantes.NORMAL);
            txtResultado.setTextColor(Color.parseColor(Constantes.C_NORMAL));
        }else if (progress == 2) {
            txtResultado.setText(Constantes.MUITO);
            txtResultado.setTextColor(Color.parseColor(Constantes.C_MUITO));
        }
    }
}
