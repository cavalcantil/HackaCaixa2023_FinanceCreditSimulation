package caixa.gov.br.simulador.repository.impl;

import caixa.gov.br.simulador.repository.entity.Produto;
import caixa.gov.br.simulador.v1.dto.entrada.EntradaSimulacaoDTO;
import java.util.List;

public interface ProdutoRepositoryImpl {
    List<Produto> buscaListProduto(EntradaSimulacaoDTO entrada);
}