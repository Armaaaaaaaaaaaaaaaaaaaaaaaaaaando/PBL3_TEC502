# BET distribuída

## Introdução

Com o avanço da internet e a crescente acessibilidade a smartphones, as apostas online se tornaram uma forma popular e amplamente acessível de entretenimento e participação em eventos diversos. No entanto, o modelo tradicional de apostas online enfrenta desafios significativos, como a dependência de intermediários, representados pelas casas de apostas, e a vulnerabilidade a bloqueios governamentais. Diante desse cenário, propõe-se o desenvolvimento de um novo sistema de apostas online baseado em tecnologias de ledger distribuído (DLT – Distributed Ledger Technology). O objetivo é criar uma solução descentralizada que elimine a necessidade de intermediários, mitigando os riscos de censura e promovendo maior segurança e transparência nas transações.

O sistema proposto permitirá que os usuários cadastrem eventos – como lançamentos de moedas – ou apostem em eventos já cadastrados. Os eventos são nada mais do que as possibilidades de jogos que podem ser iniciados como apostas. Serão incorporados mecanismos para garantir a confiabilidade do sistema e das movimentações financeiras entre os participantes. Além disso, as odds (probabilidades) para cada evento poderão ser fixadas com base em valores predefinidos, ou calculadas dinamicamente, considerando o volume de apostas e os valores apostados em cada opção.

Esse projeto visa explorar o potencial das tecnologias de ledger distribuído para transformar o mercado de apostas online, oferecendo uma plataforma mais transparente, acessível e independente para seus usuários.

## Fundamentação

Para o desenvolvimento do sistema de apostas online descentralizado, foi utilizada uma abordagem baseada na tecnologia de blockchain, com suporte de ferramentas especializadas como Remix e Ganache, e integração com uma aplicação desenvolvida em Java. Abaixo, descrevemos como cada elemento foi utilizado no processo.

**Tecnologias utilizadas**

Para a implementação deste projeto de sistema de apostas, foram utilizadas diversas tecnologias integradas. A seguir, estão detalhadas cada uma delas:

**Ganache**

  O Ganache é uma ferramenta essencial no desenvolvimento de aplicações descentralizadas (DApps). Ele fornece um blockchain Ethereum simulado localmente, permitindo que desenvolvedores testem seus contratos inteligentes de forma rápida e segura antes de implantá-los em redes reais. No projeto, o Ganache foi usado como um ambiente de testes, onde os contratos inteligentes escritos em Solidity foram implantados e as interações entre eles e a aplicação Java foram simuladas. Essa abordagem garantiu um controle total sobre as transações e dados, além de facilitar a detecção e correção de erros durante o desenvolvimento.

**Remix IDE e Solidity**

O Remix IDE é uma plataforma de desenvolvimento integrado, sendo possível criar e testar contratos inteligentes na linguagem Solidity. Essa linguagem é amplamente utilizada no ecossistema Ethereum para programar contratos inteligentes devido à sua robustez e integração nativa com a rede. No projeto, o Remix foi usado para implementar e testar funcionalidades como a criação de eventos de apostas, registro de apostas e distribuição de prêmios. O ambiente do Remix, com seu suporte a debugging e execução direta em blockchains simulados, acelerou a validação das lógicas implementadas.

**Java e Web3j**

A aplicação Java foi desenvolvida para ser a ponte entre os usuários e a blockchain. A biblioteca Web3j desempenhou um papel fundamental ao permitir a interação entre a aplicação e a rede Ethereum. Essa biblioteca fornece uma API fácil de usar para realizar operações como implantação de contratos, envio de transações e consulta de dados na blockchain.

‘A Web3j possibilita diversas operações essenciais para o projeto, como:

  - Conexão com a rede Ethereum: A biblioteca suporta tanto redes públicas quanto privadas, como é o caso do uso do Ganache, permitindo o envio de transações e consultas a dados armazenados na blockchain.

  - Execução de transações: Permite chamar funções de contratos inteligentes, passando parâmetros e recebendo respostas.  

  - Gerenciamento de contas: Com suporte a carteiras digitais, possibilita autenticar usuários por meio de chaves privadas para realizar operações de maneira segura.

  - Monitoramento de eventos: A biblioteca pode ser utilizada para escutar eventos emitidos por contratos inteligentes, criando fluxos de trabalho reativos com base em ações realizadas na blockchain.

Além das tecnologias é fundamental o entendimento de diversos conceitos que foram utilizados na formulação da solução para o presente projeto, sendo os conceitos necessários descritos abaixo:

**Distributed Ledger Technology (DLT)**

O termo Distributed Ledger Technology (DLT) refere-se a um conjunto de tecnologias que permitem o registro e a sincronização de dados em múltiplos locais por meio de um ledger descentralizado, sem a necessidade de uma autoridade central.

Diferente de bancos de dados tradicionais, as DLTs garantem que as alterações nos registros sejam consensuadas entre os participantes da rede, promovendo segurança, transparência e resistência a falhas. As DLTs podem assumir diferentes formas, dependendo de como os dados são estruturados e validados. Elas se destacam por características como descentralização, imutabilidade e eficiência, sendo amplamente aplicáveis em setores que exigem alta confiabilidade em registros compartilhados. Entre os tipos de DLTs, destaca-se a blockchain, que é o modelo mais conhecido e amplamente adotado.

**Blockchain**

É fundamental observar que o sistema de apostas desenvolvido, para funcionar de forma coerente, precisa ter uma série de propriedades essenciais, como transparência, segurança e imutabilidade, além de eliminar a necessidade de intermediários.

Essas características são indispensáveis para garantir a confiabilidade e a eficiência do projeto como um todo. Nesse contexto, a tecnologia blockchain foi escolhida como base do sistema justamente por oferecer todas essas propriedades de forma integrada. Com sua descentralização, imutabilidade e transparência, o blockchain permite que os usuários interajam diretamente uns com os outros, eliminando intermediários como as casas de apostas tradicionais. Cada transação realizada, desde o cadastro de eventos até a distribuição de prêmios, é registrada de forma imutável, assegurando a confiabilidade e a segurança do sistema.

Com isso, compreender os diferentes tipos de blockchain é essencial para escolher a tecnologia que melhor se alinha aos objetivos do sistema de apostas desenvolvido. Dada a existência de diferentes necessidades de segurança, escalabilidade e controle, é possível ter variados modelos que podem ser considerados.

A blockchain pública, como o Bitcoin e o Ethereum, é completamente descentralizada e aberta a qualquer pessoa. Qualquer usuário pode validar transações, garantir a segurança da rede e acessar o registro de transações, o que garante total transparência e imutabilidade. No entanto, ela tem limitações de escalabilidade e pode consumir muita energia, especialmente em redes que utilizam Proof of Work (PoW). Esse mecanismo é um algoritmo de consenso que exige que os mineradores resolvam problemas matemáticos complexos para validar transações e incluir blocos na cadeia. Embora o PoW seja altamente seguro, ele demanda grande capacidade computacional, o que impacta diretamente no consumo energético. Na seção sobre consenso será tratada de forma mais descritiva esse aspecto, que é fundamental a ser observado na aplicação.

A blockchain privada, como o Hyperledger, é controlada por uma única organização ou um grupo restrito de participantes. Apenas usuários autorizados podem validar transações, o que oferece maior controle, privacidade e eficiência, além de melhorar a velocidade e reduzir custos. No entanto, sua centralização pode ser vista como uma desvantagem, pois diminui a resistência a falhas e censura.

Alternativamente, a blockchain de consórcio e a híbrida oferecem um equilíbrio entre transparência e controle, permitindo colaboração entre múltiplos participantes ou segmentando o acesso à rede conforme necessário. Essas soluções podem atender casos mais complexos, como um sistema de apostas que combine interação pública com componentes privados para segurança ou compliance regulatório. Mais especificamente, a blockchain híbrida mescla as características das blockchains pública e privada, permitindo que algumas partes da rede sejam acessíveis publicamente, enquanto outras são restritas a usuários específicos. Isso oferece flexibilidade em termos de privacidade e controle, mas pode ser mais complexa para implementar e gerenciar.

Para o desenvolvimento do projeto e para fins de testes, foi utilizada uma rede privada local com o auxílio do Ganache. Essa ferramenta simula uma blockchain em um ambiente de desenvolvimento, permitindo total controle sobre a rede e suas operações. É chamada de "privada" porque somente os desenvolvedores têm acesso a ela, e os dados permanecem restritos. Por ser uma rede local, o Ganache oferece a flexibilidade necessária para configurar rapidamente diferentes cenários e monitorar todas as transações realizadas. Ele é especialmente útil para validar funcionalidades antes da implementação em uma rede de produção, garantindo maior eficiência no processo de desenvolvimento.

**Consenso**

O consenso em blockchain é o mecanismo que permite que todos os participantes da rede (nós) cheguem a um acordo sobre o estado atual da blockchain, garantindo que todas as transações sejam validadas e registradas de forma confiável, mesmo sem a necessidade de um intermediário. Esse processo é crucial para assegurar a segurança, integridade e consistência da rede.

Nas redes públicas, como o Ethereum, os mecanismos de consenso mais conhecidos incluem Proof of Work (PoW) e Proof of Stake (PoS).

  - PoW exige que os mineradores resolvam problemas matemáticos complexos para validar transações e incluir novos blocos na cadeia, garantindo alta segurança, mas com grande consumo de energia.
  - PoS, por outro lado, seleciona validadores com base na quantidade de tokens que possuem e estão dispostos a "apostar" como garantia, sendo uma alternativa mais eficiente em termos de consumo energético.

No entanto, em redes privadas, como a configurada no Ganache para este projeto, o ambiente é controlado e composto por participantes confiáveis. Isso permite a utilização de mecanismos de consenso mais simples e eficientes, como Raft ou IBFT (Istanbul Byzantine Fault Tolerance).

  - Raft é um protocolo de consenso baseado em liderança, no qual um nó líder é responsável por coordenar as atualizações e propagar as transações para os nós seguidores. Esse modelo garante que todos os nós mantenham a mesma versão dos dados, oferecendo alta eficiência e simplicidade em cenários de confiança restrita. Ele é especialmente adequado para redes privadas, pois reduz a complexidade computacional e o custo energético associados a métodos como o PoW.

  - IBFT, por sua vez, é mais resiliente a falhas e nós mal-intencionados, sendo capaz de tolerar um número limitado de nós comprometidos enquanto mantém a consistência da rede.

O Ganache, usado neste projeto para simular uma rede privada local, fornece flexibilidade na escolha de mecanismos de consenso, permitindo que se tenha a configuração de um ambiente controlado para testes e validações. Embora ele suporte diferentes modelos, o Raft é comumente utilizado em ambientes locais devido à sua simplicidade e eficiência. Isso torna o Ganache uma ferramenta ideal para simular redes privadas.

**Contratos Inteligentes**

Os contratos inteligentes, ou smart contracts, são programas autônomos que operam diretamente na blockchain. Eles são projetados para executar automaticamente um conjunto de regras predefinidas entre as partes envolvidas, sem necessidade de intervenção humana ou de terceiros confiáveis. Esses contratos garantem que as operações sejam realizadas de forma segura, transparente e imutável, características fundamentais para aplicações descentralizadas, como o sistema de apostas desenvolvido neste projeto.
 
A principal função dos contratos inteligentes é permitir a automação de processos complexos e críticos, eliminando riscos associados à intervenção humana ou à manipulação de dados. No sistema de apostas descentralizado, os contratos inteligentes desempenham um papel central na implementação da lógica de negócios e no gerenciamento das operações principais. Para desenvolvê-los e testá-los, foi utilizada a ferramenta Remix, que oferece um ambiente prático e eficiente para escrever, compilar e depurar contratos inteligentes.

Os contratos criados foram projetados para executar as seguintes operações principais:

  - Registro de novos eventos.

  - Registro de apostas: Garante que os usuários possam apostar em eventos previamente cadastrados, com regras claras para validação e registro das apostas.

  - Cálculo e distribuição de prêmios: Após a finalização de um evento, os contratos calculam automaticamente os vencedores com base nos resultados registrados, distribuindo os valores correspondentes de forma transparente e segura.

  - Definição de odds: As regras para as odds foram incorporadas nos contratos, permitindo dois modelos:
    - Odds fixas: Baseadas na probabilidade estatística de cada resultado.
    - Odds dinâmicas: Ajustadas com base no volume de apostas realizadas em cada opção, promovendo maior flexibilidade e equilíbrio no sistema.

Com esses recursos, os contratos inteligentes garantem que todas as operações do sistema sejam realizadas de forma confiável, sem a necessidade de intermediários e com total transparência para os usuários. Além disso, a descentralização proporcionada pela blockchain reduz o risco de manipulação ou fraude, consolidando os contratos inteligentes como um componente essencial para o sucesso do projeto.

## Metodologia

O desenvolvimento deste sistema foi dividido em etapas fundamentais que garantiram a criação de um ambiente funcional, seguro e transparente para apostas online, integrando contratos inteligentes baseados em blockchain com uma aplicação Java que serve como interface para os usuários.

Inicialmente, foram definidos os requisitos do sistema, como descentralização, transparência e segurança nas operações. Esses requisitos guiaram o desenvolvimento dos contratos inteligentes, que foram implementados utilizando a linguagem Solidity no ambiente Remix IDE. A blockchain local foi simulada com o Ganache, permitindo testes detalhados em um ambiente controlado. Os contratos foram projetados para atender funcionalidades como registro de eventos, gerenciamento de apostas, atualização de saldos dos usuários após a conclusão dos eventos e manutenção de um histórico público de transações e resultados.

Para a interface com os contratos, desenvolvemos uma aplicação em Java, utilizando a biblioteca Web3j para comunicação com a blockchain Ethereum. A integração foi feita estabelecendo uma conexão HTTP com o nó local do Ganache, permitindo que a aplicação interagisse diretamente com os contratos. Os métodos do contrato, como criação de eventos, registro de apostas e consulta de dados, foram mapeados na aplicação Java para facilitar a interação do usuário com o sistema. Além disso, a aplicação automatizou processos, como a distribuição de prêmios após a conclusão de um evento, garantindo que as transações fossem realizadas de forma eficiente e confiável.

Os testes foram realizados em duas etapas principais: primeiro, os contratos foram validados no Remix e no Ganache para garantir que suas funcionalidades estavam corretas. Em seguida, é necessário ser feito com cenários reais e a aplicação Java, incluindo múltiplos usuários realizando apostas em eventos simultâneos. Esses testes também incluíram a validação da integridade dos dados armazenados na blockchain, como histórico de transações e saldos atualizados. 

Por fim, a interface Java foi projetada para ser intuitiva, permitindo que os usuários realizassem operações como criar eventos, registrar apostas, consultar históricos e visualizar resultados com facilidade. A aplicação passou por melhorias com base nos testes realizados, garantindo uma experiência fluida e eficiente. Essa abordagem metodológica permitiu alcançar os objetivos do projeto, entregando um sistema de apostas descentralizado, seguro e funcional.

**Simulação com Ganache**

Durante a fase de desenvolvimento e testes, foi utilizado o Ganache para simular uma blockchain local. Esta ferramenta nos permitiu:

  - Criar uma rede privada: Foram realizadas transações e interações com contratos inteligentes sem incorrer em custos reais, o que foi fundamental para testar o sistema de forma segura e controlada.

  - Simular cenários específicos: Foram testadas diferentes condições, como múltiplos cenários de apostas e a distribuição automática de prêmios, garantindo que todas as funcionalidades fossem executadas conforme o esperado.

  - Monitorar transações em tempo real: Foi possível acompanhar o fluxo de transações, verificando a execução correta dos contratos inteligentes e a integridade dos dados processados.

Então, em resumo,  o Remix permite a escrita de contratos inteligentes utilizando a linguagem Solidity, que é amplamente usada para o desenvolvimento de contratos na Ethereum Virtual Machine (EVM). Com o Remix, foi possível compilar, testar e interagir com os contratos de forma intuitiva e eficiente. A ferramenta permite conectar-se a diferentes redes blockchain, seja em testnets públicas como Ethereum Mainnet, Ropsten, Goerli e Sepolia, ou em redes privadas, como as criadas com o Ganache. No caso de redes privadas, foi configurado o Remix para interagir com uma instância local do Ganache, permitindo simular transações de forma controlada antes do deploy na blockchain.

O Remix não define diretamente o tipo de blockchain (pública ou privada) nem o mecanismo de consenso utilizado. No entanto, a ferramenta permite a conexão com redes de diferentes consórcios e protocolos, e o tipo de consenso dependerá da configuração da rede com a qual se está interagindo. Para redes públicas, como o Ethereum, o consenso é baseado em Proof of Stake (PoS) ou Proof of Work (PoW), enquanto em redes privadas, como aquelas configuradas no Ganache, pode-se escolher entre mecanismos de consenso como Raft ou IBFT. E como mencionado, utilizamos para testes uma rede privada local com o ganache. 

## Resultados

Durante os testes realizados, foram implantados contratos inteligentes utilizando a plataforma Ethereum, com a execução de transações por meio do Ganache para simular o ambiente real. A seguir, estão detalhados os principais resultados alcançados: Inicialmente, os contratos inteligentes foram implantados na blockchain local. Cada contrato gerencia um conjunto de funcionalidades relacionadas a eventos esportivos e apostas, como criação de eventos, registro de apostas, e cálculo de resultados. No Ganache, é possível visualizar os endereços gerados para os contratos e acompanhar o consumo de gás em cada transação. O processo de implantação foi bem-sucedido, com os contratos respondendo corretamente às chamadas realizadas pela aplicação cliente.

 O processo de implantação é essencial para registrar os contratos na rede e garantir que eles estejam prontos para receber transações. Na Figura 1, é possível observar a interface do Remix IDE no momento em que um dos contratos foi implantado, utilizando o endpoint local do Ganache, o local onde está com um retângulo rosa. A interface mostra os detalhes do contrato, como os métodos disponíveis, os endereços gerados e as interações possíveis. Após a execução da transação, o Ganache registrou a criação do contrato na blockchain simulada, indicado pelo retângulo verde. Também é possível considerar a quantidade de gas que é necessário para que o contrato seja utilizado.

<p align="center"><strong></strong></p>
<p align="center">
  <img src="Imagens/token ring.png" width = "400" />
</p>
<p align="center"><strong>
</strong> Figura 1.  Visualização da interface do Remix IDE.</p>

 Após a execução da transação, o Ganache registrou a criação do contrato na blockchain simulada. A Figura 2 apresenta a visão do Ganache após a implantação do contrato. É possível verificar o endereço gerado para o contrato implantado, juntamente com a transação correspondente, exibindo o consumo de gás e o status da execução. Essa integração entre o Remix IDE e o Ganache foi crucial para validar a lógica e o comportamento dos contratos antes de integrá-los à aplicação principal.

<p align="center"><strong></strong></p>
<p align="center">
  <img src="Imagens/token ring.png" width = "400" />
</p>
<p align="center"><strong>
</strong> Figura 2.  Exibição da interface do ganache com exemplo de contrato implantado.</p>

Durante o desenvolvimento, o Remix IDE foi utilizado como uma ferramenta essencial para testar as funcionalidades dos contratos inteligentes. A interface do Remix, como mostrado na Figura 3, permite realizar transações e interagir diretamente com os métodos dos contratos implantados. No exemplo da figura 3, o contrato FootballBetting foi implantado, e suas principais funções são exibidas na interface. Essas funções incluem:

  - createEvent: criação de novos eventos, especificando equipes e probabilidades.
  - placeBet: registro de apostas em eventos existentes.
  - resolveEvent: resolução de eventos com base no resultado.
  - withdraw: retirada de fundos dos saldos dos apostadores.
  - balances: consulta de saldo para endereços específicos.
  - bets e events: recuperação de informações sobre apostas e eventos registrados.
  - owner: identificação do proprietário do contrato.

<p align="center"><strong></strong></p>
<p align="center">
  <img src="Imagens/token ring.png" width = "400" />
</p>
<p align="center"><strong>
</strong> Figura 3.   Exibição da interface do remix IDE.</p>

O Remix permite realizar testes para cada uma dessas funções manualmente, simulando as transações que serão executadas pela aplicação cliente desenvolvida em Java. Na aplicação final, essas mesmas interações serão realizadas automaticamente por meio da integração do Java com a blockchain, utilizando a biblioteca web3j para envio de transações e chamadas de leitura.

Ao longo dos testes, foram criados diversos eventos esportivos, cada um configurado com nomes de times, odds específicas e identificadores únicos. Os eventos foram registrados nos contratos por meio de transações, cujo status foi confirmado no Ganache. Essas transações incluem a função createEvent, que exige o envio de parâmetros codificados e atualiza o estado do contrato com as informações do evento. As odds definidas foram ajustadas corretamente, permitindo que os usuários realizassem apostas com base em probabilidades realistas.

Os usuários cadastrados na aplicação foram capazes de realizar depósitos e apostar em eventos esportivos utilizando seus saldos. Para cada aposta, os contratos inteligentes calculam os valores em tempo real e registram os dados na blockchain. O Ganache mostrou claramente o fluxo de transações, com detalhes como o endereço do usuário, o contrato correspondente e o valor apostado. Todas as transações foram executadas sem erros, e o saldo dos usuários foi atualizado corretamente após cada aposta.

Após o término de cada evento, os resultados foram processados pelos contratos inteligentes. O algoritmo responsável pelo cálculo dos vencedores distribui os prêmios automaticamente entre os usuários que apostaram corretamente. 

Por fim, o histórico de eventos e apostas foi acessado diretamente na blockchain. A aplicação recuperou as informações com sucesso, mostrando uma visão consolidada dos dados. Entre as informações destacadas estavam os nomes dos times, as odds, o total apostado em cada evento, os vencedores, e os prêmios distribuídos. Além disso, o Ganache forneceu suporte visual para acompanhar cada etapa do fluxo, desde a criação do contrato até o encerramento das transações.


## Conclusão

Este projeto demonstrou como contratos inteligentes podem ser aplicados para criar um sistema de apostas descentralizado, integrando ferramentas como Remix IDE, Ganache e uma aplicação em Java. A funcionalidade desenvolvida cumpriu os objetivos iniciais, permitindo criar eventos, gerenciar apostas e resolver resultados de forma transparente e segura.

No entanto, há melhorias que poderiam ser implementadas para tornar o sistema mais robusto e eficiente. Entre elas, destacam-se a possibilidade de suportar mais tipos de eventos, mais elementos poderiam ser considerados para realizar os cálculos automáticos e dinâmicos das odds, otimização do consumo de gás para reduzir custos e uma melhor interface para simulações em tempo real. Além disso, aprimorar o acompanhamento das respostas e explorar novas funcionalidades, como um oráculo para avaliar resultados das apostas, que agregaria maior valor ao projeto.

De forma geral, o projeto cumpriu seu propósito de demonstrar a aplicação prática de contratos inteligentes para gerenciar apostas de maneira descentralizada e transparente. A integração entre as ferramentas e a lógica implementada mostrou-se funcional.


## Equipe

- Fábio S. Miranda
- Armando de Lima Almeida

## Tutor

- Antonio A T R Coutinho

## Referências

GEEKSFORGEEKS. Blockchain and Distributed Ledger Technology (DLT). Disponível em: https://www.geeksforgeeks.org/blockchain-and-distributed-ledger-technology-dlt/. Acesso em: 12 dez. 2024.

GEEKSFORGEEKS. Consensus Algorithms in Blockchain. Disponível em: https://www.geeksforgeeks.org/consensus-algorithms-in-blockchain/. Acesso em: 12 dez. 2024.

GEEKSFORGEEKS. What is Chaincode in Hyperledger Fabric? Disponível em: https://www.geeksforgeeks.org/what-is-chaincode-in-hyperledger-fabric/. Acesso em: 12 dez. 2024.

REMIX IDE. Remix IDE Documentation. Disponível em: https://remix-ide.readthedocs.io/en/latest/. Acesso em: 12 dez. 2024.

GOBLOCKCHAIN. Interação entre Remix e Ganache. Disponível em: https://medium.com/goblockchain-colabs/intera%C3%A7%C3%A3o-entre-remix-e-ganache-d6eec3c1f884. Acesso em: 12 dez. 2024.

TRUFFLE SUITE. Truffle Suite Documentation. Disponível em: https://archive.trufflesuite.com/docs/. Acesso em: 12 dez. 2024.

WEB3J. Web3j Documentation. Disponível em: https://docs.web3j.io/4.8.7/. Acesso em: 12 dez. 2024.
