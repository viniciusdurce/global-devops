---

# 🌍 API de Relatórios de Eventos Extremos - Global Solution 2025 🚀

## Tema: Desafios Extremos 🌪️

Esta API REST foi desenvolvida como parte da **Global Solution 2025 da FIAP**, abordando o tema "**Desafios Extremos**". O objetivo é fornecer uma plataforma simples para registrar e consultar informações sobre **eventos naturais extremos** (como enchentes 🌊, terremotos 🌍, incêndios 🔥, etc.), permitindo uma resposta mais organizada e baseada em dados.

A solução visa ser uma ferramenta tecnológica que auxilia na **gestão de informações críticas** durante situações de crise, alinhada à proposta do desafio de usar inovação e tecnologia para ajudar pessoas e proteger o meio ambiente. 🤝🌳

---

## Tecnologias Utilizadas 🛠️

* **Linguagem:** Java 21 ☕
* **Framework:** Spring Boot 3.3.1 🍃
* **Build Tool:** Gradle 8.8 ⚙️
* **Persistência:** Spring Data JPA / Hibernate 💾
* **Banco de Dados:** PostgreSQL (executado em container) 🐘
* **Conteinerização:** Docker (via Dockerfile, sem Docker Compose) 🐳

---

## Funcionalidades (CRUD) 📋

A API oferece operações **CRUD** (Create, Read, Update, Delete) para gerenciar relatórios de eventos extremos (`EventReport`).

* **Criar (POST):** ➕ Registra um novo relatório de evento.
* **Ler Todos (GET):** 📚 Lista todos os relatórios de eventos registrados.
* **Ler por ID (GET):** 🔎 Obtém os detalhes de um relatório específico pelo seu ID.
* **Atualizar (PUT):** ✏️ Modifica as informações de um relatório existente.
* **Deletar (DELETE):** ❌ Remove um relatório do sistema.

---

## Pré-requisitos ✅

* JDK 21 ou superior instalado. ☕
* Gradle instalado (ou use o Gradle Wrapper incluído no projeto: `./gradlew`). 🚀
* Docker instalado e rodando. 🐳
* Um cliente HTTP (como `curl`, Postman ou Insomnia) para testar a API. 🧪

---

## Instruções de Execução ▶️

Siga os passos abaixo para configurar e executar a aplicação e o banco de dados usando Docker.

**1. Build da Aplicação (Gerar o JAR)** 🏗️

Navegue até o diretório raiz do projeto (`extreme-events-api`) e execute o comando Gradle para construir o artefato JAR:

```bash
./gradlew build -x test
```

Isso irá gerar o arquivo `.jar` dentro do diretório `build/libs/`.

**2. Construir a Imagem Docker da Aplicação** 🖼️

No mesmo diretório raiz, construa a imagem Docker da API usando o Dockerfile fornecido:

```bash
docker build -t extreme-events-api .
```

**3. Criar uma Rede Docker (Bridge)** 🌐

Para que os containers da aplicação e do banco de dados possam se comunicar pelo nome, crie uma rede Docker dedicada:

```bash
docker network create extreme-events-net
```

**4. Rodar o Container do Banco de Dados (PostgreSQL)** 🐘⬆️

Execute o container do PostgreSQL na rede criada. Certifique-se de definir um volume para persistência dos dados e as variáveis de ambiente necessárias.

```bash
docker run --name postgres-db \
  --network extreme-events-net \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=password \
  -e POSTGRES_DB=extreme_events_db \
  -p 5432:5432 \
  -v postgres_data:/var/lib/postgresql/data \
  -d postgres:16 
```

* `--name postgres-db`: Nome do container do banco.
* `--network extreme-events-net`: Conecta o container à rede criada.
* `-e POSTGRES_USER=...`: Define o usuário do banco.
* `-e POSTGRES_PASSWORD=...`: Define a senha do banco.
* `-e POSTGRES_DB=...`: Define o nome do banco de dados a ser criado.
* `-p 5432:5432`: Mapeia a porta do container para a porta do host.
* `-v postgres_data:/var/lib/postgresql/data`: Cria/usa um volume nomeado `postgres_data` para persistir os dados.
* `-d`: Executa o container em modo detached (background).
* `postgres:16`: Imagem oficial do PostgreSQL.

**Importante:** Os valores de `POSTGRES_USER`, `POSTGRES_PASSWORD` e `POSTGRES_DB` devem corresponder aos que a API espera (configurados via variáveis de ambiente ou `application.properties`).

**5. Rodar o Container da Aplicação** 🚀⬆️

Execute o container da API, conectando-o à mesma rede e passando as variáveis de ambiente para a conexão com o banco de dados.

```bash
docker run --name extreme-events-app \
  --network extreme-events-net \
  -p 8080:8080 \
  -e DB_URL=jdbc:postgresql://postgres-db:5432/extreme_events_db \
  -e DB_USER=postgres \
  -e DB_PASSWORD=password \
  -d extreme-events-api
```

* `--name extreme-events-app`: Nome do container da aplicação.
* `--network extreme-events-net`: Conecta o container à rede criada.
* `-p 8080:8080`: Mapeia a porta 8080 do container (definida no Dockerfile/application.properties) para a porta 8080 do host.
* `-e DB_URL=...`: **Crucial!** Informa à API o endereço do banco dentro da rede Docker (`postgres-db` é o nome do container do banco).
* `-e DB_USER=...`: Usuário do banco.
* `-e DB_PASSWORD=...`: Senha do banco.
* `-d`: Executa o container em modo detached (background).
* `seu-usuario-dockerhub/extreme-events-api:latest`: A imagem Docker da aplicação que você construiu.

**6. Verificar Logs dos Containers** 👁️‍🗨️

Para verificar se os containers subiram corretamente e acompanhar os logs:

```bash
# Logs do container da aplicação
docker logs -f extreme-events-app

# Logs do container do banco de dados
docker logs -f postgres-db
```

Pressione `Ctrl+C` para sair da visualização dos logs.

---

## Endpoints da API 🔗

A API estará acessível em `http://localhost:8080/api/v1/events`.

| Método HTTP | URL                             | Descrição                                 |
| :---------- | :------------------------------ | :---------------------------------------- |
| `POST`      | `/api/v1/events`                | Cria um novo relatório de evento.         |
| `GET`       | `/api/v1/events`                | Lista todos os relatórios de eventos.     |
| `GET`       | `/api/v1/events/{id}`           | Obtém um relatório pelo ID.               |
| `PUT`       | `/api/v1/events/{id}`           | Atualiza um relatório existente pelo ID.  |
| `DELETE`    | `/api/v1/events/{id}`           | Deleta um relatório pelo ID.              |

---

## Exemplos de Requisições 🌐

**Criar um Evento (POST)** ✨

* **URL:** `POST http://localhost:8080/api/v1/events`
* **Headers:** `Content-Type: application/json`
* **Body (JSON):**

```json
{
  "eventType": "Enchente",
  "location": "São Paulo, SP",
  "severity": 4,
  "description": "Inundação severa na Zona Leste após chuvas intensas."
}
```

**Atualizar um Evento (PUT)** 🔄

* **URL:** `PUT http://localhost:8080/api/v1/events/{id}` (substitua `{id}` pelo ID do evento a ser atualizado)
* **Headers:** `Content-Type: application/json`
* **Body (JSON):**

```json
{
  "eventType": "Enchente",
  "location": "São Paulo, SP - Zona Leste",
  "severity": 5,
  "description": "Atualização: Nível da água subindo rapidamente. Necessário evacuação imediata."
}
```

---

## Evidenciando a Persistência de Dados 💾✨

Um requisito crucial do desafio é garantir que os dados **persistam** mesmo que os containers sejam parados e removidos. Isso é alcançado através do uso de um **Volume Docker** para o container do banco de dados (`-v postgres_data:/var/lib/postgresql/data`).

Siga estes passos para demonstrar a persistência:

**1. Certifique-se que os Containers Estão Rodando:** ✅

Verifique se ambos os containers (`extreme-events-app` e `postgres-db`) estão em execução:

```bash
docker ps
```

**2. Certifique-se de que inseriu algum dado no banco através da API!** ➕

**3. Verificação Direta no Banco:** 🐘🔍

Para evidenciar os dados, vamos nos conectar diretamente ao container do PostgreSQL e consultar a tabela:

```bash
# Conectar ao container do banco
docker exec -it postgres-db psql -U postgres -d extreme_events_db

# Dentro do psql, executar a query:
SELECT * FROM event_reports; 

# Para sair do psql, digite:
\q
```

Isso mostrará todo o conteúdo (como se fosse um GET All) diretamente no banco de dados.

**3. Pare e Remova os Containers:** 🛑🗑️

Agora, pare e remova **ambos** os containers. **Importante:** O volume `postgres_data` *não* será removido com os containers.

```bash
docker stop extreme-events-app postgres-db
docker rm extreme-events-app postgres-db
```

Verifique que não há mais containers rodando com `docker ps`.

**4. Reinicie os Containers:** 🔄⬆️

Reinicie os containers **exatamente** com os mesmos comandos `docker run` que você usou inicialmente (Passos 4 e 5 da seção "Instruções de Execução"). Isso garante que o container do banco de dados se reconecte ao volume `postgres_data` existente.

* Primeiro, o banco de dados:
    ```bash
    docker run --name postgres-db \
      --network extreme-events-net \
      -e POSTGRES_USER=postgres \
      -e POSTGRES_PASSWORD=password \
      -e POSTGRES_DB=extreme_events_db \
      -p 5432:5432 \
      -v postgres_data:/var/lib/postgresql/data \
      -d postgres:16
    ```
* Depois, a aplicação (aguarde alguns segundos para o banco iniciar):
    ```bash
    docker run --name extreme-events-app \
      --network extreme-events-net \
      -p 8080:8080 \
      -e DB_URL=jdbc:postgresql://postgres-db:5432/extreme_events_db \
      -e DB_USER=postgres \
      -e DB_PASSWORD=password \
      -d extreme-events-api
    ```

**6. Verifique a Persistência:** ✨🧐

Aguarde alguns segundos para a aplicação iniciar completamente (você pode acompanhar com `docker logs -f extreme-events-app`).

Agora, tente consultar novamente os eventos que você criou no passo 2:
- primeiro se conecte com o container do banco
  - ```bash
    docker exec -it postgres-db psql -U postgres -d extreme_events_db
    ```
- depois faça a consulta
  - ```bash 
    SELECT * FROM event_reports;
    ```

Você também pode testar isso via API tester, faça um GET novamente e veja se os dados ainda estão lá! 🎉

## ▶️ Link do vídeo explicativo: 

## 🤝 Grupo

<table>
  <tr>
    <td align="center">
    <p>RM550989<p>
      <a href="https://github.com/nichol6s">
        <img src="https://avatars.githubusercontent.com/u/126689414?v=4" width="115px;" alt="Foto do Igor no GitHub"/><br>
        <sub>
          <strong>Igor Ribeiro</strong>
        </sub>
      </a>
    </td>
    <td align="center">
      <p>RM550427<p>
      <a href="https://github.com/VitorKubica">
        <img src="https://avatars.githubusercontent.com/u/127512951?v=4" width="115px;" alt="Foto do Durce"/><br>
        <sub>
          <strong>Vinicius Durce</strong>
        </sub>
      </a>
    </td>
   <tr>
</table>

