package caixa.gov.br.simulador.service;

import caixa.gov.br.simulador.repository.entity.Produto;
import caixa.gov.br.simulador.repository.ProdutoRepository;
import caixa.gov.br.simulador.v1.dto.entrada.EntradaSimulacaoDTO;
import caixa.gov.br.simulador.v1.dto.saida.ParcelaDTO;
import caixa.gov.br.simulador.v1.dto.saida.ResultadoSimulacaoDTO;
import caixa.gov.br.simulador.v1.dto.saida.SimulacaoDTO;
import com.azure.messaging.eventhubs.*;
import com.azure.messaging.eventhubs.models.CreateBatchOptions;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.time.Duration;
import reactor.core.publisher.Mono;



@ApplicationScoped
public class SimulacaoService {

    @Inject
    ProdutoRepository produtoRepository;

    public SimulacaoDTO simular(EntradaSimulacaoDTO entrada) {
        SimulacaoDTO simulacao = new SimulacaoDTO();

        List<Produto> produtos = produtoRepository.buscaListProduto(entrada);

        if (produtos.isEmpty()) {
            throw new WebApplicationException(
                    Response.status(Response.Status.NOT_FOUND)
                            .entity("Nenhum produto encontrado para os parâmetros informados.")
                            .build());
        }

        Produto produto = produtos.get(0);

        simulacao.setCodigoProduto(produto.getCodigoProduto());
        simulacao.setDescricaoProduto(produto.getNomeProduto());
        simulacao.setTaxaJuros(produto.getTaxaJuros());

        List<ResultadoSimulacaoDTO> resultados = new ArrayList<>();

        // CALCULO SAC [INICIO]

        ResultadoSimulacaoDTO resultadoSAC = simulaSAC(entrada, simulacao);

        // CALCULO SAC [FIM]

        resultados.add(resultadoSAC);

        // CALCULO PRICE [INICIO]

        ResultadoSimulacaoDTO resultadoPRICE = simulaPRICE(entrada, simulacao);

        // CALCULO PRICE [FIM]
        resultados.add(resultadoPRICE);

        simulacao.setResultadoSimulacao(resultados);

        // Gravar o envelope JSON no Event Hub
        gravacaoEventHub(simulacao);

        return simulacao;
    }

    private static void gravacaoEventHub(SimulacaoDTO simulacao) {
        try {
            String connectionString = "Endpoint=sb://eventhack.servicebus.windows.net/;SharedAccessKeyName=hack;SharedAccessKey=HeHeVaVqyVkntO2FnjQcs2Ilh/4MUDo4y+AEhKp8z+g=;EntityPath=simulacoes";
            String eventHubName = "simulacoes";

            EventHubProducerAsyncClient producer = new EventHubClientBuilder()
                    .connectionString(connectionString, eventHubName)
                    .buildAsyncProducerClient();

            CreateBatchOptions options = new CreateBatchOptions()
                    .setPartitionId("0");

            EventDataBatch eventDataBatch = producer.createBatch(options).block(Duration.ofSeconds(10));

            eventDataBatch.tryAdd(new EventData(simulacao.toString().getBytes(StandardCharsets.UTF_8)));

            Mono<Void> sendResult = producer.send(eventDataBatch);

            sendResult.doOnSuccess(v -> {
                System.out.println("Evento enviado e gravado com sucesso no Event Hub.");
            }).doOnError(error -> {
                System.out.println("Falha ao enviar o evento para o Event Hub: " + error.getMessage());
            }).block();

            producer.close();
        } catch (Exception e) {
            e.printStackTrace();
            // Lida com a exceção de envio do evento para o Event Hub
        }
    }

    private ResultadoSimulacaoDTO simulaPRICE(EntradaSimulacaoDTO entrada, SimulacaoDTO simulacao) {
        ResultadoSimulacaoDTO resultadoPRICE = new ResultadoSimulacaoDTO();
        resultadoPRICE.setTipo("PRICE");
        resultadoPRICE.setParcelas(new ArrayList<>());

        BigDecimal taxaJurosPRICE = simulacao.getTaxaJuros();
        BigDecimal prestacaoPRICE = entrada.getValorDesejado()
                .multiply(taxaJurosPRICE)
                .divide(BigDecimal.ONE.subtract(BigDecimal.ONE.divide(BigDecimal.ONE.add(taxaJurosPRICE).pow(entrada.getPrazo()), 8, RoundingMode.HALF_UP)), 2, RoundingMode.HALF_UP);

        BigDecimal saldoDevedorPRICE = entrada.getValorDesejado();
        for (int i = 1; i <= entrada.getPrazo(); i++) {
            ParcelaDTO parcela = new ParcelaDTO();
            parcela.setNumero(i);
            parcela.setValorAmortizacao(prestacaoPRICE.subtract(saldoDevedorPRICE.multiply(taxaJurosPRICE)).setScale(2, RoundingMode.HALF_UP));
            parcela.setValorJuros(saldoDevedorPRICE.multiply(taxaJurosPRICE).setScale(2, RoundingMode.HALF_UP));
            parcela.setValorPrestacao(prestacaoPRICE);
            resultadoPRICE.getParcelas().add(parcela);
            saldoDevedorPRICE = saldoDevedorPRICE.subtract(parcela.getValorAmortizacao());
        }
        return resultadoPRICE;
    }

    private ResultadoSimulacaoDTO simulaSAC(EntradaSimulacaoDTO entrada, SimulacaoDTO simulacao) {
        ResultadoSimulacaoDTO resultadoSAC = new ResultadoSimulacaoDTO();
        resultadoSAC.setTipo("SAC");
        resultadoSAC.setParcelas(new ArrayList<>());

        BigDecimal valorAmortizacaoSAC = entrada.getValorDesejado().divide(BigDecimal.valueOf(entrada.getPrazo()), 2, RoundingMode.HALF_UP);
        BigDecimal saldoDevedorSAC = entrada.getValorDesejado();

        for (int i = 1; i <= entrada.getPrazo(); i++) {
            ParcelaDTO parcela = new ParcelaDTO();
            parcela.setNumero(i);
            parcela.setValorAmortizacao(valorAmortizacaoSAC);
            parcela.setValorJuros(saldoDevedorSAC.multiply(simulacao.getTaxaJuros()).setScale(2, RoundingMode.HALF_UP));
            parcela.setValorPrestacao(parcela.getValorAmortizacao().add(parcela.getValorJuros()).setScale(2, RoundingMode.HALF_UP));
            resultadoSAC.getParcelas().add(parcela);
            saldoDevedorSAC = saldoDevedorSAC.subtract(valorAmortizacaoSAC);
        }
        return resultadoSAC;
    }
}
