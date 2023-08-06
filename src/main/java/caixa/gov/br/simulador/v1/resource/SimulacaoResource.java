package caixa.gov.br.simulador.v1.resource;

import caixa.gov.br.simulador.service.SimulacaoService;
import caixa.gov.br.simulador.v1.dto.entrada.EntradaSimulacaoDTO;
import caixa.gov.br.simulador.v1.dto.saida.SimulacaoDTO;
import caixa.gov.br.simulador.v1.resource.impl.SimulacaoResourceImpl;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("v1/simulacao")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SimulacaoResource implements SimulacaoResourceImpl {

    @Inject
    SimulacaoService simulacaoService;

    @POST
    public SimulacaoDTO simular(EntradaSimulacaoDTO entrada) {
        return simulacaoService.simular(entrada);
    }
}