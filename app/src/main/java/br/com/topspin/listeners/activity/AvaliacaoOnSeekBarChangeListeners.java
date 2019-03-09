package br.com.topspin.listeners.activity;

import android.graphics.Color;
import android.widget.SeekBar;
import android.widget.TextView;

import br.com.topspin.constantes.Constantes;

public class AvaliacaoOnSeekBarChangeListeners implements SeekBar.OnSeekBarChangeListener {

    private SeekBar skb;
    private TextView txtRuim;
    private TextView txtRegular;
    private TextView txtBom;
    private TextView txtOtimo;

    private TextView txtPouco;
    private TextView txtNormal;
    private TextView txtMuito;

    private int qtdRespostas;

    public AvaliacaoOnSeekBarChangeListeners() {}
    public AvaliacaoOnSeekBarChangeListeners(SeekBar skb, TextView txtRuim, TextView txtRegular, TextView txtBom, TextView txtOtimo, int qtdRespostas) {
        this.skb = skb;
        this.txtRuim = txtRuim;
        this.txtRegular = txtRegular;
        this.txtBom = txtBom;
        this.txtOtimo = txtOtimo;
        this.qtdRespostas = qtdRespostas;
    }

    public AvaliacaoOnSeekBarChangeListeners(SeekBar skb, TextView txtPouco, TextView txtNormal, TextView txtMuito, int qtdRespostas) {
        this.skb = skb;
        this.txtPouco = txtPouco;
        this.txtNormal = txtNormal;
        this.txtMuito = txtMuito;
        this.qtdRespostas = qtdRespostas;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        //Log.v("SeekBar: " + seekBar.getMax() + " - " + seekBar.getId(), "");
        if (qtdRespostas == 3) {
            setarResultadoAvaliativoComTresRespostas(progress);
        }else if (qtdRespostas == 4) {
            setarResultadoAvaliativoComQuatroRespostas(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}


    public void setarResultadoAvaliativoComQuatroRespostas(int progress) {
        txtRuim.setTextColor(Color.parseColor(Constantes.C_PADRAO));
        txtRegular.setTextColor(Color.parseColor(Constantes.C_PADRAO));
        txtBom.setTextColor(Color.parseColor(Constantes.C_PADRAO));
        txtOtimo.setTextColor(Color.parseColor(Constantes.C_PADRAO));

        if (progress == 0) {
            txtRuim.setTextColor(Color.parseColor(Constantes.C_RUIM));
        }else if (progress == 1) {
            txtRegular.setTextColor(Color.parseColor(Constantes.C_REGULAR));
        }else if (progress == 2) {
            txtBom.setTextColor(Color.parseColor(Constantes.C_BOM));
        }else if (progress == 3) {
            txtOtimo.setTextColor(Color.parseColor(Constantes.C_OTIMO));
        }
    }

    public void setarResultadoAvaliativoComTresRespostas(int progress) {
        txtPouco.setTextColor(Color.parseColor(Constantes.C_PADRAO));
        txtNormal.setTextColor(Color.parseColor(Constantes.C_PADRAO));
        txtMuito.setTextColor(Color.parseColor(Constantes.C_PADRAO));

        if (progress == 0) {
            txtPouco.setTextColor(Color.parseColor(Constantes.C_POUCO));
        }else if (progress == 1) {
            txtNormal.setTextColor(Color.parseColor(Constantes.C_NORMAL));
        }else if (progress == 2) {
            txtMuito.setTextColor(Color.parseColor(Constantes.C_MUITO));
        }
    }
}
