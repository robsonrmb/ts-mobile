package br.com.topspin.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import br.com.topspin.config.ConfiguracaoFirebase;

@IgnoreExtraProperties
public class ResultadoJogo extends Model implements Serializable {

    private String idDoUsuarioLogado;
    private String data;
    private String resultado;               // Vitória ou derrota
    private String placar;                  // 2a0 ou 2a1
    private String tipo;                    // Torneio, barragem, contra, outros
    private String qtdTieBreakVencidos;
    private String qtdTieBreakPerdidos;
    private String adversario;
    private String qtdVitoriasParaEstatisticas;
    private String qtdDerrotasParaEstatisticas;
    private String qtdTieVencidosParaEstatisticas;
    private String qtdTiePerdidosParaEstatisticas;
    private String resultadoUltimosJogosParaEstatisticas;
    private String qtdJogosParaEstatisticas;

    public ResultadoJogo() {}

    public String validar() {
        String retorno = null;
        if (data == null || data.isEmpty()) {
            retorno = "Informe a data do jogo.";

        }else if (resultado == null || resultado.isEmpty() || resultado.toUpperCase().equals("RESULTADO")) {
            retorno = "Informe o resultado do jogo.";

        }else if (placar == null || placar.isEmpty() || placar.toUpperCase().equals("PLACAR")) {
            retorno = "Informe o placar do jogo.";

        }else{
            int qtdVencidos = 0;
            int qtdPerdidos = 0;
            try {
                qtdVencidos = Integer.parseInt(qtdTieBreakVencidos);
            }catch (Exception e) {
                retorno = "Valores de tiebreaks incorretos.";
            }

            try {
                qtdPerdidos = Integer.parseInt(qtdTieBreakPerdidos);
            }catch (Exception e) {
                retorno = "Valores de tiebreaks incorretos.";
            }

            if (retorno == null) {
                String placarNaoConfereComTieBreak = "Quantidade de tiebreaks não conferem com o placar do jogo.";
                if ("DERROTA".equals(resultado)) {
                    if ("2 a 0".equals(placar)) {
                        if (qtdVencidos > 0) {
                            retorno = placarNaoConfereComTieBreak;
                        } else if (qtdPerdidos > 2) {
                            retorno = placarNaoConfereComTieBreak;
                        }

                    } else if ("2 a 1".equals(placar)) {
                        if (qtdVencidos > 1) {
                            retorno = placarNaoConfereComTieBreak;
                        } else if (qtdPerdidos > 2) {
                            retorno = placarNaoConfereComTieBreak;
                        }

                    } else if ("3 a 0".equals(placar)) {
                        if (qtdVencidos > 0) {
                            retorno = placarNaoConfereComTieBreak;
                        } else if (qtdPerdidos > 3) {
                            retorno = placarNaoConfereComTieBreak;
                        }

                    } else if ("3 a 1".equals(placar)) {
                        if (qtdVencidos > 1) {
                            retorno = placarNaoConfereComTieBreak;
                        } else if (qtdPerdidos > 3) {
                            retorno = placarNaoConfereComTieBreak;
                        }

                    } else if ("3 a 2".equals(placar)) {
                        if (qtdVencidos > 2) {
                            retorno = placarNaoConfereComTieBreak;
                        } else if (qtdPerdidos > 3) {
                            retorno = placarNaoConfereComTieBreak;
                        }
                    }

                }else{
                    if ("2 a 0".equals(placar)) {
                        if (qtdVencidos > 2) {
                            retorno = placarNaoConfereComTieBreak;
                        } else if (qtdPerdidos > 0) {
                            retorno = placarNaoConfereComTieBreak;
                        }

                    } else if ("2 a 1".equals(placar)) {
                        if (qtdVencidos > 2) {
                            retorno = placarNaoConfereComTieBreak;
                        } else if (qtdPerdidos > 1) {
                            retorno = placarNaoConfereComTieBreak;
                        }

                    } else if ("3 a 0".equals(placar)) {
                        if (qtdVencidos > 3) {
                            retorno = placarNaoConfereComTieBreak;
                        } else if (qtdPerdidos > 0) {
                            retorno = placarNaoConfereComTieBreak;
                        }

                    } else if ("3 a 1".equals(placar)) {
                        if (qtdVencidos > 3) {
                            retorno = placarNaoConfereComTieBreak;
                        } else if (qtdPerdidos > 1) {
                            retorno = placarNaoConfereComTieBreak;
                        }

                    } else if ("3 a 2".equals(placar)) {
                        if (qtdVencidos > 3) {
                            retorno = placarNaoConfereComTieBreak;
                        } else if (qtdPerdidos > 2) {
                            retorno = placarNaoConfereComTieBreak;
                        }
                    }
                }
            }
        }
        return retorno;
    }

    public void salvar() {

        DatabaseReference databaseReference = ConfiguracaoFirebase.getDatabase();
        //databaseReference.child(getNoPrincipal()).child(getIdDoUsuarioLogado()).child("resultadoJogos").push().setValue(this);
        String key = databaseReference.child(getNoPrincipal()).child(getIdDoUsuarioLogado()).child("resultadoJogos").push().getKey();

        Map<String, Object> postJogo = this.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/"+ getNoPrincipal() +"/" + idDoUsuarioLogado + "/resultadoJogos/" + key, postJogo);
        if (getQtdVitoriasParaEstatisticas() != null && !"".equals(getQtdVitoriasParaEstatisticas())) {
            childUpdates.put("/"+ getNoPrincipal() +"/" + idDoUsuarioLogado + "/estatisticas/qtdVitorias", getQtdVitoriasParaEstatisticas());
        }
        if (getQtdDerrotasParaEstatisticas() != null && !"".equals(getQtdDerrotasParaEstatisticas())) {
            childUpdates.put("/"+ getNoPrincipal() +"/" + idDoUsuarioLogado + "/estatisticas/qtdDerrotas", getQtdDerrotasParaEstatisticas());
        }
        childUpdates.put("/"+ getNoPrincipal() +"/" + idDoUsuarioLogado + "/estatisticas/qtdTieVencidos", getQtdTieVencidosParaEstatisticas());
        childUpdates.put("/"+ getNoPrincipal() +"/" + idDoUsuarioLogado + "/estatisticas/qtdTiePerdidos", getQtdTiePerdidosParaEstatisticas());
        childUpdates.put("/"+ getNoPrincipal() +"/" + idDoUsuarioLogado + "/estatisticas/ultimosJogos", getResultadoUltimosJogosParaEstatisticas());
        childUpdates.put("/"+ getNoPrincipal() +"/" + idDoUsuarioLogado + "/estatisticas/qtdJogos", getQtdJogosParaEstatisticas());

        databaseReference.updateChildren(childUpdates);
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("data", data);
        result.put("resultado", resultado);
        result.put("placar", placar);
        result.put("tipo", tipo);
        result.put("qtdTieBreakVencidos", qtdTieBreakVencidos);
        result.put("qtdTieBreakPerdidos", qtdTieBreakPerdidos);
        result.put("adversario", adversario);
        return result;
    }

    @Exclude
    public String getIdDoUsuarioLogado() {
        return idDoUsuarioLogado;
    }

    public void setIdDoUsuarioLogado(String idDoUsuarioLogado) {
        this.idDoUsuarioLogado = idDoUsuarioLogado;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public String getPlacar() {
        return placar;
    }

    public void setPlacar(String placar) {
        this.placar = placar;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getAdversario() {
        return adversario;
    }

    public void setAdversario(String adversario) {
        this.adversario = adversario;
    }

    public String getQtdTieBreakVencidos() {
        return qtdTieBreakVencidos;
    }

    public void setQtdTieBreakVencidos(String qtdTieBreakVencidos) {
        this.qtdTieBreakVencidos = qtdTieBreakVencidos;
    }

    public String getQtdTieBreakPerdidos() {
        return qtdTieBreakPerdidos;
    }

    public void setQtdTieBreakPerdidos(String qtdTieBreakPerdidos) {
        this.qtdTieBreakPerdidos = qtdTieBreakPerdidos;
    }

    @Exclude
    public String getQtdVitoriasParaEstatisticas() {
        return qtdVitoriasParaEstatisticas;
    }

    public void setQtdVitoriasParaEstatisticas(String qtdVitoriasParaEstatisticas) {
        this.qtdVitoriasParaEstatisticas = qtdVitoriasParaEstatisticas;
    }

    @Exclude
    public String getQtdDerrotasParaEstatisticas() {
        return qtdDerrotasParaEstatisticas;
    }

    public void setQtdDerrotasParaEstatisticas(String qtdDerrotasParaEstatisticas) {
        this.qtdDerrotasParaEstatisticas = qtdDerrotasParaEstatisticas;
    }

    @Exclude
    public String getQtdTieVencidosParaEstatisticas() {
        return qtdTieVencidosParaEstatisticas;
    }

    public void setQtdTieVencidosParaEstatisticas(String qtdTieVencidosParaEstatisticas) {
        this.qtdTieVencidosParaEstatisticas = qtdTieVencidosParaEstatisticas;
    }

    @Exclude
    public String getQtdTiePerdidosParaEstatisticas() {
        return qtdTiePerdidosParaEstatisticas;
    }

    public void setQtdTiePerdidosParaEstatisticas(String qtdTiePerdidosParaEstatisticas) {
        this.qtdTiePerdidosParaEstatisticas = qtdTiePerdidosParaEstatisticas;
    }

    @Exclude
    public String getResultadoUltimosJogosParaEstatisticas() {
        return resultadoUltimosJogosParaEstatisticas;
    }

    public void setResultadoUltimosJogosParaEstatisticas(String resultadoUltimosJogosParaEstatisticas) {
        this.resultadoUltimosJogosParaEstatisticas = resultadoUltimosJogosParaEstatisticas;
    }

    @Exclude
    public String getQtdJogosParaEstatisticas() {
        return qtdJogosParaEstatisticas;
    }

    public void setQtdJogosParaEstatisticas(String qtdJogosParaEstatisticas) {
        this.qtdJogosParaEstatisticas = qtdJogosParaEstatisticas;
    }
}
