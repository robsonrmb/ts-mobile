package br.com.topspin.graficos;


import android.content.Context;
import android.graphics.Color;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class GraficoAvaliacoes {

    public static void setPieChartAvaliacoesComQuatroRespostas(Context context, PieChart pieChart, String sequencia, String descricao) {

        try {
            int ruim = Integer.parseInt(sequencia.substring(0,1));
            int regular = Integer.parseInt(sequencia.substring(1,2));
            int bom = Integer.parseInt(sequencia.substring(2,3));
            int otimo = Integer.parseInt(sequencia.substring(3));
            int somaTotal = ruim + regular + bom + otimo;

            int percentualRuim = ((ruim * 100) / (somaTotal));
            int percentualRegular = ((regular * 100) / (somaTotal));
            int percentualBom = ((bom * 100) / (somaTotal));
            int percentualOtimo = ((otimo * 100) / (somaTotal));
            int somaPercentualTotal = percentualRuim + percentualRegular + percentualBom + percentualOtimo;

            if (somaPercentualTotal != 100) {
                if (somaPercentualTotal < 100) {
                    percentualBom = percentualBom + (100 - somaPercentualTotal);

                }else if (somaPercentualTotal > 100) {
                    percentualRuim = percentualRuim - (somaPercentualTotal - 100);
                }
            }

            List<PieEntry> entries = new ArrayList<>();
            if (ruim != 0) {
                entries.add(new PieEntry(percentualRuim, "Ruim"));
            }
            if (regular != 0) {
                entries.add(new PieEntry(percentualRegular, "Regular"));
            }
            if (bom != 0) {
                entries.add(new PieEntry(percentualBom, "Bom"));
            }
            if (otimo != 0) {
                entries.add(new PieEntry(percentualOtimo, "Ótimo"));
            }

            PieDataSet set = new PieDataSet(entries, "");
            set.setColors(ColorTemplate.MATERIAL_COLORS);
            //set.setColors(new int[] { R.color.gfcRuim, R.color.gfcRegular, R.color.gfcBom, R.color.gfcOtimo});
            PieData data = new PieData(set);
            pieChart.setData(data);

            Legend legend = pieChart.getLegend();
            legend.setEnabled(false);

            Description d = new Description();
            d.setText(descricao);
            d.setTextColor(Color.parseColor("#ffffff"));
            pieChart.setDescription(d);

            pieChart.setEntryLabelColor(Color.BLACK);
            pieChart.invalidate();

        }catch (Exception e) {

        }
    }

    public static void setPieChartAvaliacoesComTresRespostas(Context context, PieChart pieChart, String sequencia, String descricao) {

        try {
            int pouco = Integer.parseInt(sequencia.substring(0,1));
            int normal = Integer.parseInt(sequencia.substring(1,2));
            int muito = Integer.parseInt(sequencia.substring(2));
            int somaTotal = pouco + normal + muito;

            int percentualPouco = ((pouco * 100) / (somaTotal));
            int percentualNormal = ((normal * 100) / (somaTotal));
            int percentualMuito = ((muito * 100) / (somaTotal));
            int somaPercentualTotal = percentualPouco + percentualNormal + percentualMuito;

            if (somaPercentualTotal != 100) {
                if (somaPercentualTotal < 100) {
                    percentualNormal = percentualNormal + (100 - somaPercentualTotal);

                }else if (somaPercentualTotal > 100) {
                    percentualNormal = percentualNormal - (somaPercentualTotal - 100);
                }
            }

            List<PieEntry> entries = new ArrayList<>();
            if (pouco != 0) {
                entries.add(new PieEntry(percentualPouco, "Pouco"));
            }
            if (normal != 0) {
                entries.add(new PieEntry(percentualNormal, "Normal"));
            }
            if (muito != 0) {
                entries.add(new PieEntry(percentualMuito, "Muito"));
            }

            PieDataSet set = new PieDataSet(entries, "");
            set.setColors(ColorTemplate.MATERIAL_COLORS);
            PieData data = new PieData(set);
            pieChart.setData(data);

            Legend legend = pieChart.getLegend();
            legend.setEnabled(false);

            Description d = new Description();
            d.setText(descricao);
            d.setTextColor(Color.parseColor("#ffffff"));
            pieChart.setDescription(d);
            pieChart.setEntryLabelColor(Color.BLACK);
            pieChart.invalidate();

        }catch (Exception e) {

        }
    }

}
