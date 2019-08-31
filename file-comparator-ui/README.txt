INTRODUÇÃO
Olá2.
Este documento descreve sobre a api desenvolvida para a realização de avaliação de conhecimento em desenvolvimento de software para a empresa Upflux. Aqui pode ser encontrada informações sobre as suas características e descrição de como rodá-la para testes.

ALTERAÇÕES EXECUTADAS

Conforme a avaliação permitia, realizei as seguintes alterações em contrapartida do que se encontra no enunciado original enviado.

1) Utilizei a tecnologia Java 8 com o ecossistema Spring, para a parte de backend, ao invés de C#, como foi pedido. Para a base de dados utilizei o MongoDB com hospedagem em Cloud. Como a avaliação pede, são aceitos apenas arquivos do tipo JSON.

2) URIs alteradas de:

<host>/v01/diff/<ID>/from 
<host>/v01/diff/<ID>/to 
<host>/v01/diff/<ID>/diff

Para:

<host>file/v01/from/<NAME> 
<host>file/v01/to/<NAME>
<host>file/v01/diff/<NAME>

E adicionadas:

<host>file/v01/diff/from/<NAME>
<host>file/v01/diff/to/<NAME>

3) O parâmetro <ID> acima troquei por <NAME> para relacioná-lo ao nome do arquivo na BD. O Id na base de dados continua tendo o mesmo significado de Id, adicionei uma regra extra na lógica do sistema para o Name ser uma espécie de Id também.

4) Adicionei a seguinte regra de negócio:

O sistema verifica se o cliente que estiver submetendo um arquivo utilizando o verbo http PUT, o mesmo garante que o arquivo já exista na BD e não deixa fazer um insert caso o arquivo não existir. Pensei na integridade dos dados, caso o cliente erre o nome do arquivo e acredite que estará fazendo um update quando, na verdade, estará inserindo um arquivo novo (indesejado). Também para evitar que, por erros dos clientes, a base fique populada com dados errados. A regra do POST eu mantive, ou seja, faz a inserção dos dados novos e, se o arquivo já existir, verifica se têm alterações e, só se tiver, faz o update.

5) Defini as seguintes propriedades aceitas nos arquivos JSON que serão inputados:

Em "from" e "to":

id
date
name
content

Em "diff":

id
id_from
id_to
date
date_from
date_to
name
name_from
name_to
content
content_from
content_to

RODANDO A APLICAÇÃO

Para iniciar a aplicação, rode numa bash o comando abaixo: (pode ser em qualquer S.O., desde de que tenha o Java instalado na máquina, pode ser a JRE mesmo...)

java -jar file-comparator-api-0.0.1-SNAPSHOT.jar

O arquivo jar acima está em \upflux-master\file-comparator-api\

Então, a partir deste comando, os serviços subirão e, ao término da execução do comando, você poderá consultar os dados com as seguintes URLs (no Windows ou Linux, dê CTRL C na bash para terminar os serviços):

http://localhost:8080/file/v01/from   (GET, POST e PUT)

http://localhost:8080/file/v01/from/delete/[file-name] (DELETE)

http://localhost:8080/file/v01/from/[file-name]  (GET)

------------------------------------------------------------------

http://localhost:8080/file/v01/to   (GET, POST e PUT)

http://localhost:8080/file/v01/to/delete/[file-name] (DELETE)

http://localhost:8080/file/v01/to/[file-name]  (GET)

------------------------------------------------------------------

http://localhost:8080/file/v01/diff   (GET, POST e PUT)

http://localhost:8080/file/v01/diff/delete/[file-name]   (DELETE)

http://localhost:8080/file/v01/diff/[file-name]  (GET)

http://localhost:8080/file/v01/diff/from/[file-name]  (GET)  , este também registra alterações em /file/v01/diff

http://localhost:8080/file/v01/diff/to/[file-name]  (GET)  , este também registra alterações em /file/v01/diff

Você poderá consultar os dados na base MongoDB em cloud via web browser ou com uma ferramenta de linha de comando chamada cURL (este dá para enviar POST, PUT, DELETE para a aplicação). Porém, a melhor maneira de se fazer isto é utilizando o Postman.

ESTRUTURA E TESTES UNITÁRIOS

Os testes foram desenvolvidos em Java com a ajuda dos frameworks JUnit 4 e do Mockito, este último para emular os serviços da cloud nos testes.

A maneira mais ágil para se rodar os testes é utilizando o Maven. Deve-se ter instalado na máquina que irá realizar os testes, além de baixar o projeto do repositório git em https://github.com/fmedeiros13/upflux/archive/master.zip e rodar os comandos Maven da raiz \upflux-master\file-comparator-api\, onde está localizado o arquivo de gerenciamento Java, pom.xml.

Os comandos mais utilizados são "mvn clean" e "mvn install", além do "mvn test". Os nomes já dizem tudo.

A estrutura de diretórios também pode ser facilmente entendida fazendo-se o download do projeto.

Obrigado, qualquer dúvida envie um email para fmedeiros13@yahoo.com.br
