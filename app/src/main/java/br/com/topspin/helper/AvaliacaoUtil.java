package br.com.topspin.helper;

public class AvaliacaoUtil {

    public static String montaSequenciaComQuatroRespostas(String sequencia, String avaliacao) {

        //SEQUENCIA COM 4 RESPOSTAS
        if (sequencia != null) {
            String[] array = sequencia.split("#");
            if (avaliacao.equals("RUIM")) {
                array[0] = ((Integer.parseInt(array[0]) + 1) + "");
            } else if (avaliacao.equals("REGULAR")) {
                array[1] = ((Integer.parseInt(array[1]) + 1) + "");
            } else if (avaliacao.equals("BOM")) {
                array[2] = ((Integer.parseInt(array[2]) + 1) + "");
            } else if (avaliacao.equals("OTIMO")) {
                array[3] = ((Integer.parseInt(array[3]) + 1) + "");
            }
            return array[0] + "#" + array[1] + "#" + array[2] + "#" + array[3];

        }else{
            if (avaliacao.equals("RUIM")) {
                return "1#0#0#0";
            } else if (avaliacao.equals("REGULAR")) {
                return "0#1#0#0";
            } else if (avaliacao.equals("BOM")) {
                return "0#0#1#0";
            } else if (avaliacao.equals("OTIMO")) {
                return "0#0#0#1";
            } else {
                return "0#0#0#0";
            }
        }
    }

    public static String montaSequenciaComTresRespostas(String sequencia, String avaliacao) {

        //SEQUENCIA COM 3 RESPOSTAS
        if (sequencia != null) {
            String[] array = sequencia.split("#");
            if (avaliacao.equals("POUCO")) {
                array[0] = ((Integer.parseInt(array[0]) + 1) + "");
            } else if (avaliacao.equals("NORMAL")) {
                array[1] = ((Integer.parseInt(array[1]) + 1) + "");
            } else if (avaliacao.equals("MUITO")) {
                array[2] = ((Integer.parseInt(array[2]) + 1) + "");
            }
            return array[0] + "#" + array[1] + "#" + array[2];

        }else{
            if (avaliacao.equals("POUCO")) {
                return "1#0#0";
            } else if (avaliacao.equals("NORMAL")) {
                return "0#1#0";
            } else if (avaliacao.equals("MUITO")) {
                return "0#0#1";
            } else{
                return "0#0#0";
            }
        }
    }

    public static String salvaAvaliacaoRecebida(String qtdAvaliacoesRecebida) {
        if (qtdAvaliacoesRecebida == null) {
            return "1";
        }else {
            return (Integer.parseInt(qtdAvaliacoesRecebida) + 1) + "";
        }
    }

    public static String salvaAvaliacaoPendente(String qtdAvaliacoesPendentes) {
        if (qtdAvaliacoesPendentes == null) {
            return "1";
        }else {
            return (Integer.parseInt(qtdAvaliacoesPendentes) + 1) + "";
        }
    }

    public static String confirmaAvaliacaoPendente(String qtdAvaliacoesPendentes) {
        return (Integer.parseInt(qtdAvaliacoesPendentes) - 1) + "";
    }

}
