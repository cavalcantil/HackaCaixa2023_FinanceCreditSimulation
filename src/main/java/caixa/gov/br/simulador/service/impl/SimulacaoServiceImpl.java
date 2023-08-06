package caixa.gov.br.simulador.service.impl;

import caixa.gov.br.simulador.v1.dto.entrada.EntradaSimulacaoDTO;
import caixa.gov.br.simulador.v1.dto.saida.SimulacaoDTO;

public interface SimulacaoServiceImpl {
    SimulacaoDTO simular(EntradaSimulacaoDTO entrada);
}