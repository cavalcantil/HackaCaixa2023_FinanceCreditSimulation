package caixa.gov.br.simulador.v1.dto.entrada;

import java.math.BigDecimal;

public class EntradaSimulacaoDTO {
    private BigDecimal valorDesejado;
    private int prazo;

    public BigDecimal getValorDesejado() {
        return valorDesejado;
    }

    public void setValorDesejado(BigDecimal valorDesejado) {
        this.valorDesejado = valorDesejado;
    }

    public int getPrazo() {
        return prazo;
    }

    public void setPrazo(int prazo) {
        this.prazo = prazo;
    }
}