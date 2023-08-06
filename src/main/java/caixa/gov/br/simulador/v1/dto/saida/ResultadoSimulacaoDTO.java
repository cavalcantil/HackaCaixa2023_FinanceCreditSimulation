package caixa.gov.br.simulador.v1.dto.saida;

import java.util.List;

public class ResultadoSimulacaoDTO {
    private String tipo;
    private List<ParcelaDTO> parcelas;

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public List<ParcelaDTO> getParcelas() {
        return parcelas;
    }

    public void setParcelas(List<ParcelaDTO> parcelas) {
        this.parcelas = parcelas;
    }
}