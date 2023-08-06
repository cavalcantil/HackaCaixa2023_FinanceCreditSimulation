package caixa.gov.br.simulador.repository.impl;

import java.math.BigDecimal;

public interface ProdutoImpl {
    int getCodigoProduto();

    void setCodigoProduto(int codigoProduto);

    String getNomeProduto();

    void setNomeProduto(String nomeProduto);

    BigDecimal getTaxaJuros();

    void setTaxaJuros(BigDecimal taxaJuros);

    int getMinimoMeses();

    void setMinimoMeses(int minimoMeses);

    Integer getMaximoMeses();

    void setMaximoMeses(Integer maximoMeses);

    BigDecimal getValorMinimo();

    void setValorMinimo(BigDecimal valorMinimo);

    BigDecimal getValorMaximo();

    void setValorMaximo(BigDecimal valorMaximo);
}