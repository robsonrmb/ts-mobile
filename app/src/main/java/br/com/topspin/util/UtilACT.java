package br.com.topspin.util;

import br.com.topspin.constantes.Constantes;

public class UtilACT {

    public static String retornaApelidoPadraoAPartirDoNome(String nome) {
        String[] str = nome.split(" ");
        return str[0];
    }

    public static int buscaTipo(String tipo) {
        if ("DESTRO".equals(tipo)) {
            return 1;
        }else if ("CANHOTO".equals(tipo)) {
            return 2;
        }else{
            return 0;
        }
    }

    public static int buscaNivel(String nivel) {
        if ("INICIANTE".equals(nivel)) {
            return 1;
        }else if ("INTERMEDIARIO".equals(nivel)) {
            return 2;
        }else if ("AVANCADO".equals(nivel)) {
            return 3;
        }else{
            return 0;
        }
    }

    public static int buscaIndiceDoEstado(String estado) {
        if ("AC".equals(estado)) {
            return 1;
        }else if ("AL".equals(estado)) {
            return 2;
        }else if ("AP".equals(estado)) {
            return 3;
        }else if ("AP".equals(estado)) {
            return 3;
        }else if ("AM".equals(estado)) {
            return 4;
        }else if ("BA".equals(estado)) {
            return 5;
        }else if ("CE".equals(estado)) {
            return 6;
        }else if ("DF".equals(estado)) {
            return 7;
        }else if ("ES".equals(estado)) {
            return 8;
        }else if ("GO".equals(estado)) {
            return 9;
        }else if ("MA".equals(estado)) {
            return 10;
        }else if ("MT".equals(estado)) {
            return 11;
        }else if ("MS".equals(estado)) {
            return 12;
        }else if ("MG".equals(estado)) {
            return 13;
        }else if ("PA".equals(estado)) {
            return 14;
        }else if ("PB".equals(estado)) {
            return 15;
        }else if ("PR".equals(estado)) {
            return 16;
        }else if ("PE".equals(estado)) {
            return 17;
        }else if ("PI".equals(estado)) {
            return 18;
        }else if ("RJ".equals(estado)) {
            return 19;
        }else if ("RN".equals(estado)) {
            return 20;
        }else if ("RS".equals(estado)) {
            return 21;
        }else if ("RO".equals(estado)) {
            return 22;
        }else if ("RR".equals(estado)) {
            return 23;
        }else if ("SC".equals(estado)) {
            return 24;
        }else if ("SP".equals(estado)) {
            return 25;
        }else if ("SE".equals(estado)) {
            return 26;
        }else if ("TO".equals(estado)) {
            return 27;
        }else{
            return 0;
        }
    }

    public static String buscaEstadoDoIndice(long indice) {
        int i = Integer.parseInt(String.valueOf(indice));
        return Constantes.ESTADOS[i];
    }

    public static String retornaDadoFormatadoParaEstado(String estado, boolean retornaIndiceZero) {
        int retorno = UtilACT.buscaIndiceDoEstado(estado);
        if (retorno == 0) {
            if (retornaIndiceZero) {
                return Constantes.ESTADOS[retorno];
            }else{
                return "";
            }
        }else{
            return Constantes.ESTADOS[retorno];
        }
    }

    public static String retornaDadoFormatado(String valor, String complemento, String retornoPadrao) {
        if (valor == null) {
            if (retornoPadrao == null) {
                return "";
            }else{
                return retornoPadrao;
            }
        }else{
            if (complemento == null) {
                return valor;
            }else{
                return valor + complemento;
            }
        }
    }

    public static String retornaDadoFormatadoParaDoisValores(String valor1, String valor2, String retornoPadrao) {
        if ("".equals(valor1)) {
            valor1 = null;
        }
        if ("".equals(valor2)) {
            valor2 = null;
        }
        if (valor1 == null && valor2 == null) {
            if (retornoPadrao == null) {
                return "";
            }else{
                return retornoPadrao;
            }
        }else if (valor1 != null && valor2 == null) {
            return valor1;
        }else if (valor1 == null && valor2 != null) {
            return valor2;
        }else{
            return valor1 + " - " + valor2;
        }
    }

}
