INTRODU��O

Este documento descreve sobre a api desenvolvida para a realiza��o de avalia��o de conhecimento em desenvolvimento de software para a empresa Upflux. Aqui pode ser encontrada informa��es sobre as suas caracter�sticas e descri��o de como rod�-la para testes.

ALTERA��ES EXECUTADAS

Conforme a avalia��o permitia, realizei as seguintes altera��es em contrapartida do que se encontra no enunciado original enviado.

1) Utilizei a tecnologia Java 8 com o ecossistema Spring, para a parte de backend, ao inv�s de C#, como foi pedido. Para a base de dados utilizei o MongoDB com hospedagem em Cloud. Como a avalia��o pede, s�o aceitos apenas arquivos do tipo JSON.

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

3) O par�metro <ID> acima troquei por <NAME> para relacion�-lo ao nome do arquivo na BD. O Id na base de dados continua tendo o mesmo significado de Id, adicionei uma regra extra na l�gica do sistema para o Name ser uma esp�cie de Id tamb�m.

4) Adicionei a seguinte regra de neg�cio:

O sistema verifica se o cliente que estiver submetendo um arquivo utilizando o verbo http PUT, o mesmo garante que o arquivo j� exista na BD e n�o deixa fazer um insert caso o arquivo n�o existir. Pensei na integridade dos dados, caso o cliente erre o nome do arquivo e acredite que estar� fazendo um update quando, na verdade, estar� inserindo um arquivo novo (indesejado). Tamb�m para evitar que, por erros dos clientes, a base fique populada com dados errados. A regra do POST eu mantive, ou seja, faz a inser��o dos dados novos e, se o arquivo j� existir, verifica se t�m altera��es e, s� se tiver, faz o update.

5) Defini as seguintes propriedades aceitas nos arquivos JSON que ser�o inputados:

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

RODANDO A APLICA��O

Para iniciar a aplica��o, rode numa bash o comando abaixo: (pode ser em qualquer S.O., desde de que tenha o Java instalado na m�quina, pode ser a JRE mesmo...)

java -jar file-comparator-api-0.0.1-SNAPSHOT.jar

O arquivo jar acima est� em \upflux-master\file-comparator-api\

Ent�o, a partir deste comando, os servi�os subir�o e, ao t�rmino da execu��o do comando, voc� poder� consultar os dados com as seguintes URLs (no Windows ou Linux, d� CTRL C na bash para terminar os servi�os):

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

http://localhost:8080/file/v01/diff/from/[file-name]  (GET)  , este tamb�m registra altera��es em /file/v01/diff

http://localhost:8080/file/v01/diff/to/[file-name]  (GET)  , este tamb�m registra altera��es em /file/v01/diff

Voc� poder� consultar os dados na base MongoDB em cloud via web browser ou com uma ferramenta de linha de comando chamada cURL (este d� para enviar POST, PUT, DELETE para a aplica��o). Por�m, a melhor maneira de se fazer isto � utilizando o Postman.

ESTRUTURA E TESTES UNIT�RIOS

Os testes foram desenvolvidos em Java com a ajuda dos frameworks JUnit 4 e do Mockito, este �ltimo para emular os servi�os da cloud nos testes.

A maneira mais �gil para se rodar os testes � utilizando o Maven. Deve-se ter instalado na m�quina que ir� realizar os testes, al�m de baixar o projeto do reposit�rio git em https://github.com/fmedeiros13/upflux/archive/master.zip e rodar os comandos Maven da raiz \upflux-master\file-comparator-api\, onde est� localizado o arquivo de gerenciamento Java, pom.xml.

Os comandos mais utilizados s�o "mvn clean" e "mvn install", al�m do "mvn test". Os nomes j� dizem tudo.

A estrutura de diret�rios tamb�m pode ser facilmente entendida fazendo-se o download do projeto.

Obrigado, qualquer d�vida envie um email para fmedeiros13@yahoo.com.br