package caixa.gov.br.simulador;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class SimulacaoServiceTest {

    @Test
    public void testMockito() {
        // Criando o mock da dependência
        List<String> mockedList = Mockito.mock(List.class);

        // Definindo o comportamento esperado do mock
        when(mockedList.size()).thenReturn(10);

        // Testando a lógica com o mock
        int size = mockedList.size();

        // Verificando o resultado
        assertEquals(10, size);
    }
}
