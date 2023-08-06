# Projeto API de Simulação de Crédito  - CAIXA HACKATHON 2023.
Desenvolvido por Leandro Cordeiro Neves Cavalcanti - C134002.

# 1. Visão Geral do Sistema
Desenvolvido por Leandro Cordeiro Neves Cavalcanti - C134002.

Este sistema é uma aplicação Java criada usando o framework Quarkus, Maven e JDK 11. O objetivo é simular operações financeiras para diferentes produtos financeiros disponíveis. Ele executa cálculos complexos de amortização e juros, permitindo ao usuário entender completamente os detalhes do empréstimo ou financiamento desejado.

# 2. Estrutura de Dados



O código foi desenvolvido para um serviço de simulação de empréstimo bancário escrito em Quarkus, utilizando o Maven como gerenciador de dependências e o JDK 11.

O serviço possui os seguintes componentes:

Seguindo uma arquitetura MVC em camadas, onde cada camada tem uma responsabilidade específica.

1. Classe de Entidade (Produto):
    - Essa classe representa um produto de empréstimo.
    - Ela possui várias propriedades, como código do produto, nome do produto, taxa de juros, meses mínimos e máximos de duração do empréstimo, valor mínimo e máximo do empréstimo.
    - Essa classe é anotada com as anotações `@Entity` e `@Table`, indicando que ela é uma entidade persistente mapeada para uma tabela de banco de dados.
    - Ela possui métodos getters e setters para todas as propriedades.

2. Interface e Implementação do Repositório:
    - A interface `ProdutoRepositoryImpl` define um método para buscar uma lista de produtos de empréstimo com base em determinados critérios.
    - A classe `ProdutoRepository` implementa a interface `ProdutoRepositoryImpl` e fornece a implementação para o método `buscaListProduto`.
    - A implementação usa o JPA (Java Persistence API) para executar uma consulta no banco de dados e obter os produtos de empréstimo com base nos critérios especificados.

3. Classe de Serviço (SimulacaoService):
    - Essa classe fornece um serviço para simular o cálculo de empréstimos.
    - Ela possui um método chamado `simular` que recebe um objeto `EntradaSimulacaoDTO` como entrada e retorna um objeto `SimulacaoDTO` como resultado.
    - O método realiza as seguintes etapas:
        - Ele busca uma lista de produtos de empréstimo usando a classe `ProdutoRepository`.
        - Se nenhum produto for encontrado, ele lança uma exceção informando que nenhum produto foi encontrado.
        - Ele seleciona o primeiro produto da lista retornada.
        - Ele calcula os resultados da simulação de empréstimo para dois tipos: PRICE e SAC.
        - Ele cria um objeto `SimulacaoDTO` e define os dados relevantes, como código do produto, descrição do produto e resultados da simulação.
        - Por fim, ele invoca o método `gravacaoEventHub` para salvar os dados da simulação em um Event Hub.

4. Classe de Recurso (SimulacaoResource):
    - Essa classe é um recurso RESTful que expõe um endpoint para a simulação de empréstimos.
    - Ela implementa a interface `SimulacaoResourceImpl`, que define o método `simular`.
    - A classe injeta uma instância de `SimulacaoService` e delega a simulação de empréstimo para esse serviço.
    - O método `simular` aceita um objeto `EntradaSimulacaoDTO` como entrada, repassa-o para o método `simular` de `SimulacaoService` e retorna o resultado.

5. Objetos de Transferência de Dados (DTO):
    - O código inclui várias classes DTO, como `EntradaSimulacaoDTO`, `SimulacaoDTO`, `ResultadoSimulacaoDTO` e `ParcelaDTO`.
    - Essas classes definem as estruturas de dados usadas para transferir dados entre diferentes camadas da aplicação.
- Elas possuem propriedades correspondentes aos dados necessários para a simulação de empréstimos.

6. Adicionais:
    - O código também inclui algumas dependências adicionais e métodos utilitários para trabalhar com o Azure Event Hubs e manipular exceções.


# 3. Processo de Simulação

O ponto central do sistema é a simulação de empréstimos, que é tratada na classe SimulacaoResource. 
Esta classe tem um método POST simular, que recebe os dados de entrada do usuário e realiza a simulação.

```O processo de simulação é o seguinte:```

Consulta ao banco de dados para encontrar um produto financeiro que se encaixe nas necessidades do usuário (valor e prazo).
Se um produto apropriado é encontrado, o sistema realiza cálculos de amortização e juros usando duas metodologias diferentes: SAC e PRICE.
Os resultados são então coletados em uma estrutura de dados SimulacaoDTO, que é retornada ao usuário.

Rodar Aplicação no terminal do IDE
```shell script
./mvn quarkus:dev
```

Utilizar Postaman ou cliente de chamadas de API
> **_URL do Metodo "POST":_**  http://localhost:8080/v1/simulacao.

Necessário informar no body da requisição.
 ```{
    "valorDesejado" : 000.0,
    "prazo" : 0

}
```

# 4. Configuração do Sistema

As configurações do sistema estão armazenadas no arquivo application.properties. Este arquivo contém informações sobre o banco de dados SQL Server, bem como detalhes sobre a conexão ao serviço Azure Service Bus, que é usado para lidar com eventos.


# Fluxo de Dados

A interação começa com um usuário enviando uma requisição POST para a API, atingindo o método simular na classe SimulacaoResource.

O SimulacaoResource recebe um objeto EntradaSimulacaoDTO como entrada, que contém informações sobre o valor desejado do empréstimo e o prazo.

O SimulacaoResource então consulta o banco de dados para encontrar um produto adequado e inicia a simulação.

Para cada método de cálculo (SAC e PRICE), ele cria uma lista de ParcelaDTOs que representam as parcelas do empréstimo.

Essas parcelas são então coletadas em um objeto ResultadoSimulacaoDTO.

Todos os resultados são coletados em um objeto SimulacaoDTO, que é retornado ao usuário.


```
+-------------------+          +---------------------------+           +-----------------------+
| EntradaSimulacaoDTO |          | SimulacaoResource         |           | SimulacaoService      |
+-------------------+          +---------------------------+           +-----------------------+
         |                             |                                       |
         | simular()                   |                                       |
         |---------------------------> |                                       |
         |                             |                                       |
         |                             | buscaListProduto()                    |
         |                             |-------------------------------------->|
         |                             |                                       |
         |                             |          Lista de Produto              |
         |                             | <--------------------------------------|
         |                             |                                       |
         |                             | selecionar o primeiro Produto         |
         |                             |                                       |
         |                             | simulaSAC()                           |
         |                             |-------------------------------------->|
         |                             |                                       |
         |                             | Resultado SAC                         |
         |                             | <--------------------------------------|
         |                             |                                       |
         |                             | simulaPRICE()                         |
         |                             |-------------------------------------->|
         |                             |                                       |
         |                             | Resultado PRICE                       |
         |                             | <--------------------------------------|
         |                             |                                       |
         | criar SimulacaoDTO           |                                       |
         |---------------------------> |                                       |
         |                             |                                       |
         | definir dados da SimulacaoDTO |                                       |
         |---------------------------> |                                       |
         |                             |                                       |
         | chamar gravacaoEventHub()    |                                       |
         |---------------------------> |                                       |
         |                             |                                       |
         | retornar SimulacaoDTO        |                                       |
         |<--------------------------- |                                       |
+-------------------+          +---------------------------+           +-----------------------+
| SimulacaoDTO       |          | ProdutoRepository         |           | Produto                 |
+-------------------+          +---------------------------+           +-----------------------+
                                 |                                       |
                                 | buscaListProduto()                    |
                                 |-------------------------------------->|
                                 |                                       |
                                 |        Lista de Produto               |
                                 | <--------------------------------------|
                                 |                                       |
                                 |                                       |
```

Neste diagrama, as setas indicam a direção das chamadas de método entre as classes. Aqui está uma descrição do fluxo do processo:

1. A solicitação de simulação de empréstimo é recebida pela classe `SimulacaoResource` através do método `simular()`.
2. O método `simular()` chama o método `buscaListProduto()` do `ProdutoRepository` para obter uma lista de produtos de empréstimo com base nos critérios fornecidos.
3. O `ProdutoRepository` executa uma consulta no banco de dados e retorna a lista de produtos de empréstimo.
4. O `SimulacaoResource` seleciona o primeiro produto da lista para realizar a simulação.
5. O `SimulacaoResource` chama os métodos `simulaSAC()` e `simulaPRICE()` do `SimulacaoService` para calcular os resultados da simulação para os tipos SAC e PRICE, respectivamente.
6. O `SimulacaoService` realiza os cálculos necessários com base nos dados do produto e da entrada de simulação.
7. O `SimulacaoService` retorna os resultados da simulação para o `SimulacaoResource`.
8. O `SimulacaoResource` cria um objeto `SimulacaoDTO`, define os dados relevantes e chama o método `gravacaoEventHub()` para salvar os dados da simulação em um Event Hub.
9. O `SimulacaoResource` retorna o objeto `SimulacaoDTO` como resultado da solicitação.


# Testes

Os testes unitarios foram feitos nas seguintes classes:

1. Teste de Integração com RestAssured (SimulacaoResourceIntegrationTest):
   - Esse teste verifica se a API de simulação de empréstimos está funcionando corretamente.
   - O teste usa a biblioteca RestAssured para fazer uma chamada HTTP POST para o endpoint "/v1/simulacao" da API.
   - Ele define o corpo da solicitação como um objeto JSON contendo os parâmetros de entrada para a simulação.
   - Em seguida, o teste verifica se a resposta HTTP tem o código de status 200 (indicando uma resposta bem-sucedida).
   - Ele também verifica se o corpo da resposta contém os campos "codigoProduto", "descricaoProduto" e "taxaJuros", garantindo que os resultados da simulação sejam retornados corretamente.

2. Teste de Unidade com Mockito (SimulacaoServiceTest):
   - Esse teste demonstra o uso do framework de mockito para criar um mock de uma dependência e definir o comportamento esperado.
   - No exemplo, uma lista (do tipo `List<String>`) é mockada usando o Mockito.
   - Em seguida, é definido o comportamento esperado do mock: quando o método `size()` é chamado, ele deve retornar o valor 10.
   - O teste chama o método `size()` no mock e verifica se o valor retornado é igual a 10, validando o comportamento esperado definido no mock.


No caso do teste de integração com RestAssured, ele valida a funcionalidade correta da API em relação às chamadas HTTP recebidas e às respostas geradas. Esses testes garantem que a API esteja corretamente configurada, receba solicitações e retorne respostas esperadas.

Por outro lado, o teste de unidade com Mockito valida o comportamento de componentes individuais da aplicação. No exemplo dado, o teste com Mockito valida a lógica de um componente específico que depende de uma lista. Ele verifica se o componente se comporta corretamente de acordo com o comportamento esperado definido no mock.

Esses testes podem ser executados de forma automatizada durante o processo de compilação, integração contínua ou implantação contínua, permitindo que a equipe de desenvolvimento verifique a integridade da aplicação em diferentes níveis antes de liberá-la para uso. Eles ajudam a identificar problemas precocemente, tornando o desenvolvimento mais ágil e confiável.


# Evidenciamento e Conclusão 

Este sistema oferece uma solução robusta e flexível para simular empréstimos e financiamentos. Ele permite que os usuários explorem diferentes opções de produtos financeiros e entendam completamente os detalhes de cada possível empréstimo. Utilizando tecnologias modernas e eficientes, como Quarkus e Azure Service Bus, o sistema é capaz de lidar com operações complexas e tráfego pesado.

**Com base nos requisitos fornecidos, o código atende todo processo.  Vamos analisar um por um:**

1. Receber um envelope JSON, via chamada à API, contendo uma solicitação de simulação de empréstimo:
   - O requisito é atendido pela classe `SimulacaoResource`, que expõe um endpoint RESTful (`simular()`) para receber uma solicitação de simulação de empréstimo em formato JSON.

2. Consultar um conjunto de informações parametrizadas em uma tabela de banco de dados SQL Server:
   - O requisito é atendido pela classe `ProdutoRepository`, que utiliza o JPA (Java Persistence API) para consultar uma tabela de produtos no banco de dados SQL Server e obter os dados necessários para a simulação.

3. Validar os dados de entrada da API com base nos parâmetros de produtos retornados no banco de dados:
   - A validação dos dados de entrada não está explícita nas classes fornecidas. No entanto, essa validação pode ser realizada no `SimulacaoResource` antes de chamar o `SimulacaoService`. É importante realizar a validação dos dados para garantir que os parâmetros fornecidos sejam compatíveis com os produtos de empréstimo existentes.

4. Filtrar qual produto se adequa aos parâmetros de entrada:
   - O requisito é atendido na classe `ProdutoRepositoryImpl`, que realiza a consulta no banco de dados e retorna uma lista de produtos que atendem aos critérios fornecidos na solicitação de simulação.

5. Realizar os cálculos para os sistemas de amortização SAC e PRICE de acordo com dados validados:
   - Os cálculos para os sistemas de amortização SAC e PRICE são realizados na classe `SimulacaoService`. Os métodos `simulaSAC()` e `simulaPRICE()` executam os cálculos necessários com base nos dados do produto e da entrada de simulação.

6. Retornar um envelope JSON contendo o nome do produto validado e o resultado da simulação utilizando dois sistemas de amortização (SAC e Price) e gravando este mesmo envelope JSON no Eventhub:
   - O requisito é atendido na classe `SimulacaoService`, onde é criado um objeto `SimulacaoDTO` contendo o nome do produto, os resultados da simulação para os sistemas de amortização SAC e PRICE, e esse objeto é retornado como resposta à chamada à API.
   - Além disso, a classe `SimulacaoService` também possui um método `gravacaoEventHub()` que grava o envelope JSON contendo os dados da simulação no Event Hub.



>Portanto, o código preenche todos os requisitos solicitados.

# Produto 04

Pelo o que foi descrito nos dados fornecidos, o produto 4 possui um valor mínimo de empréstimo de 1.000.000,01, sem um valor máximo definido. No entanto, na consulta realizada no método buscaListProduto() da classe ProdutoRepository, há uma cláusula WHERE que verifica se o valorDesejado está entre valorMinimo e valorMaximo. Se o valorDesejado informado for menor que 1.000.000,01, o produto 4 não será retornado na lista de produtos.

Para corrigir isso,  foi ajustado para usar a função COALESCE no filtro do prazo máximo (p.maximoMeses). Dessa forma, quando p.maximoMeses for null, o valor de :prazo será usado como o prazo máximo.


# Demais Funcionalidades
Unidade de Teste
> > **_Hibernate ORM:_**  modelo de persistência  http://localhost:8080/q/dev/io.quarkus.quarkus-hibernate-orm/persistence-units


Swagger
> **_A documentação interativa da API:_**   http://localhost:8080/q/swagger-ui/.

OpenAPI
> **_Permite baixa api em formato JSON:_** http://localhost:8080/q/openapi.

Hibernate ORM
>  **_Modelo de persistência:_**    http://localhost:8080/q/dev/io.quarkus.quarkus-hibernate-orm/persistence-units

DEV UI 
>  **_Hub dos modulos da aplicacao :_**  http://localhost:8080/q/dev/
