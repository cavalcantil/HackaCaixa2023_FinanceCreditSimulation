package caixa.gov.br.simulador.v1.resource.impl;

import caixa.gov.br.simulador.v1.dto.entrada.EntradaSimulacaoDTO;
import caixa.gov.br.simulador.v1.dto.saida.SimulacaoDTO;

public interface SimulacaoResourceImpl {
    SimulacaoDTO simular(EntradaSimulacaoDTO entrada);
}