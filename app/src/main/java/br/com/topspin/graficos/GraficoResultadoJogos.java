package br.com.topspin.graficos;


import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class GraficoResultadoJogos {

    public static void setPieChartResultadoJogos(Context context, PieChart pieChart, int totalDeVitorias, int totalDeDerrotas, TextView txt) {

        if (totalDeVitorias == 0 && totalDeDerrotas == 0) {
            pieChart.setVisibility(View.INVISIBLE);
            txt.setVisibility(View.VISIBLE);

        }else {
            int valorPercentualVitorias = ((totalDeVitorias * 100) / (totalDeVitorias + totalDeDerrotas));
            int valorPercentualDerrotas = 100 - valorPercentualVitorias;

            List<PieEntry> entries = new ArrayList<>();
            if (totalDeVitorias != 0) {
                entries.add(new PieEntry(valorPercentualVitorias, "Vitórias"));
            }
            if (totalDeDerrotas != 0) {
                entries.add(new PieEntry(valorPercentualDerrotas, "Derrotas"));
            }

            /*
            int total = calcularPercentualTotal(totalDeVitorias + totalDeDerrotas);
            totalDeVitorias = calcularPercentualVitorias(total, totalDeVitorias);
            totalDeDerrotas = calcularPercentualDerrotas(total, totalDeDerrotas);
            */

            PieDataSet set = new PieDataSet(entries, "");
            set.setColors(ColorTemplate.MATERIAL_COLORS);
            PieData data = new PieData(set);
            pieChart.setData(data);

            Legend legend = pieChart.getLegend();
            legend.setEnabled(false);

            Description d = new Description();
            d.setText("Vitórias/derrotas");
            d.setTextColor(Color.parseColor("#ffffff"));
            pieChart.setDescription(d);
            pieChart.setEntryLabelColor(Color.BLACK);
            //pieChart.invalidate();
            pieChart.setVisibility(View.VISIBLE);
            txt.setVisibility(View.INVISIBLE);
        }
    }

    public static void setPieChartTieBreak(Context context, PieChart pieChart, int totalDeTieBreakVencidos, int totalDeTieBreakPerdidos, TextView txt) {

        if (totalDeTieBreakVencidos == 0 && totalDeTieBreakPerdidos == 0) {
            pieChart.setVisibility(View.INVISIBLE);
            txt.setVisibility(View.VISIBLE);

        }else {
            int valorPercentualVencidos = ((totalDeTieBreakVencidos * 100) / (totalDeTieBreakVencidos + totalDeTieBreakPerdidos));
            int valorPercentualPerdidos = 100 - valorPercentualVencidos;

            List<PieEntry> entries = new ArrayList<>();
            if (totalDeTieBreakVencidos != 0) {
                entries.add(new PieEntry(valorPercentualVencidos, "Vencidos"));
            }
            if (totalDeTieBreakPerdidos != 0) {
                entries.add(new PieEntry(totalDeTieBreakPerdidos, "Perdidos"));
            }

            /*
            int total = calcularPercentualTotal(totalDeTieBreakVencidos + totalDeTieBreakPerdidos);
            totalDeTieBreakVencidos = calcularPercentualTieVencidos(total, totalDeTieBreakVencidos);
            totalDeTieBreakPerdidos = calcularPercentualTiePerdidos(total, totalDeTieBreakPerdidos);
            */

            PieDataSet set = new PieDataSet(entries, "");
            set.setColors(ColorTemplate.MATERIAL_COLORS);
            PieData data = new PieData(set);
            pieChart.setData(data);

            Legend legend = pieChart.getLegend();
            legend.setEnabled(false);

            Description d = new Description();
            d.setText("TieBreaks");
            d.setTextColor(Color.parseColor("#ffffff"));
            pieChart.setDescription(d);
            pieChart.setEntryLabelColor(Color.BLACK);
            //pieChart.invalidate();
            pieChart.setVisibility(View.VISIBLE);
            txt.setVisibility(View.INVISIBLE);
        }
    }

}
