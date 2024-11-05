# Discapp
Um aplicativo de mensagens inspirado no Discord, permitindo a criação de servidores e canais de chat para mensagens em tempo real.

## 🚀 Tecnologias utilizadas

- **Java 21**
- **Spring Boot 3.3.4**
- **Spring Data JPA**
- **Spring Security**
- **Spring Web**
- **Spring Validation**
- **JWT**
- **Lombok**
- **WebSocket**
- **PostgreSQL**
- **Docker**


## 💻 Rodar projeto com docker

### Pré-requisitos
- [Docker](https://www.docker.com/) instalado na sua máquina. Se você não tem, siga [este guia](https://docs.docker.com/get-docker/).
- [Docker Compose](https://docs.docker.com/compose/) instalado (geralmente já incluído no Docker).
### Executando com Docker Compose
1. Clone o repositório:
   ```bash
   git clone https://github.com/seu_usuario/discordapp.git
   cd discordapp
2. Inicie os serviços usando o docker-compose:
   ```bash
   docker-compose up -d
3. Para parar e remover os containers
   ```bash
   docker-compose down

## 🔗 API Endpoints

### Autenticação

| Método | Endpoint                     | Descrição                |
|--------|------------------------------|--------------------------|
| POST   | `/api/auth/register`         | Registro de usuário      |
| POST   | `/api/auth/login`            | Login de usuário         |

### Servidores

| Método | Endpoint                           | Descrição                     |
|--------|------------------------------------|-------------------------------|
| POST   | `/api/servers`                     | Criar servidor                |
| GET    | `/api/servers`                     | Listar todos os servidores    |
| GET    | `/api/servers/{serverId}`         | Obter servidor por ID         |
| DELETE | `/api/servers/{serverId}/{userId}`| Deletar servidor              |
| POST   | `/api/servers/{serverId}/channels`| Criar canal para servidor     |
| GET    | `/api/servers/{serverId}/channels`| Listar canais por servidor    |
| GET    | `/api/servers/{serverId}/channels/{channelId}` | Obter canal por ID  |

### Mensagens

| Método | Endpoint                                         | Descrição                      |
|--------|--------------------------------------------------|--------------------------------|
| POST   | `/api/servers/{serverId}/channels/{channelId}/messages` | Enviar mensagem                |
| GET    | `/api/servers/{serverId}/channels/{channelId}/messages` | Obter mensagens por canal      |

## 🔄 Repositório do Front-end

O repositório do front-end pode ser encontrado em: [discordapp-front](https://github.com/eduardo-araujo1/discordapp-front)

