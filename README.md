# 📹 Hackathon — Microserviço de Processamento de Vídeos

[![Release - Build, Quality Gate and Deploy](https://github.com/FIAP-SOAT-G129/hackathon-video-ms-fase5/actions/workflows/release.yml/badge.svg)](https://github.com/FIAP-SOAT-G129/hackathon-video-ms-fase5/actions/workflows/release.yml)

![Coverage](.github/badges/jacoco.svg)
![Branches](.github/badges/branches.svg)

Este repositório implementa o **Microservice de Processamento de Vídeos**, desenvolvido em **Java 21 com Spring Boot 3**.  
Ele é responsável por receber uploads de vídeos, gerenciar seus metadados e orquestrar o processamento assíncrono para extração de frames e geração de arquivos ZIP.

---

## 🧾 Objetivo do Projeto

Fornecer uma **API RESTful** para o gerenciamento do ciclo de vida de processamento de vídeos — desde o upload inicial, acompanhamento de status até o download dos resultados processados.  
Integra-se com o módulo de **Worker de Processamento** através de mensageria assíncrona utilizando **RabbitMQ**.

> 📄 **Documentação da API (Swagger):** <br/> > http://localhost:8081/swagger-ui/index.html

> 📚 **Wiki do Projeto:** <br/> > https://github.com/FIAP-SOAT-G129/.github/wiki/Fase-5

---

## 🚀 Tecnologias Utilizadas

- **Java 21**
- **Spring Boot 3**
- **PostgreSQL** (Persistência de dados)
- **Redis** (Cache de status)
- **RabbitMQ** (Mensageria assíncrona)
- **Maven** (Gerenciamento de dependências)
- **Flyway** (Controle de migrations)
- **Docker & Docker Compose** (Containerização e orquestração)

---

## 🧩 Domínios Gerenciados
 
 | Entidade      | Descrição                                                                          |
 |:--------------|:-----------------------------------------------------------------------------------|
 | **Video**     | Metadados do vídeo, incluindo status de processamento e caminhos de armazenamento. |
 | **VideoFile** | Registro dos arquivos físicos associados (Original e ZIP resultante).              |

---

## 🧠 Arquitetura
 
O serviço segue os princípios da **Arquitetura Hexagonal (Ports and Adapters)** garantindo:

- Independência entre camadas
- Facilidade de manutenção e evolução
- Testabilidade e baixo acoplamento
- Separação entre regras de negócio e frameworks externos

Estrutura do projeto:

```
 src/
  ├── main/
  │   ├── java/com/hackathon/video/
  │   │   ├── adapter/
  │   │   │   ├── in/                        # Adapters de entrada (Controllers, DTOs, Mensageria)
  │   │   │   │   ├── controller/
  │   │   │   │   ├── dto/
  │   │   │   │   └── messaging/
  │   │   │   └── out/                       # Adapters de saída (JPA, Mensageria, Storage)
  │   │   │       ├── entity/
  │   │   │       ├── identity/
  │   │   │       ├── mapper/
  │   │   │       ├── messaging/
  │   │   │       ├── notification/
  │   │   │       ├── repository/
  │   │   │       └── storage/
  │   │   │
  │   │   ├── application/
  │   │   │   └── usecase/                   # Casos de uso (regras de aplicação)
  │   │   │
  │   │   ├── config/                        # Configurações (Beans, RabbitMQ, etc.)
  │   │   │
  │   │   ├── domain/                        # Núcleo do domínio (regras puras de negócio)
  │   │   │   ├── entity/
  │   │   │   ├── enums/
  │   │   │   └── repository/                # Interfaces (ports)
  │   │   │
  │   │   ├── exception/                     # Tratamento global de exceções
  │   │   │
  │   │   └── utils/                         # Classes utilitárias e helpers
  │   │
  │   └── resources/
  │       ├── db/migration/                  # Scripts Flyway
  │       └── application.yml                # Configurações da aplicação
  │
  └── test/                                  # Testes unitários
```

---

## ⚙️ Como Rodar o Projeto

### ✅ Pré-requisitos
- `Java 21` (opcional, para rodar fora do container)
- `Maven` (opcional, para rodar fora do container)
- `Docker` (para rodar em container)
- `Docker Compose` (para orquestrar containers)

### 🔧 Configuração

A aplicação já vem configurada com valores padrão no `application.yml` para funcionar com o Docker Compose. Caso deseje alterar, as principais variáveis de ambiente são:

```env
APP_PORT=8081

JWT_SECRET=YOUR_SECRET_KEY

MAX_FILE_SIZE=500MB
MAX_REQUEST_SIZE=500MB

DB_HOST=db
DB_PORT=5432
DB_NAME=video_db
DB_USER=user
DB_PASSWORD=password

MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=guest
MAIL_PASSWORD=guest

RABBITMQ_HOST=rabbitmq
RABBITMQ_PORT=5672
RABBITMQ_USER=guest
RABBITMQ_PASSWORD=guest

REDIS_HOST=redis
REDIS_PORT=6379

VIDEOS_STORAGE_PATH=/tmp/videos
ZIPS_STORAGE_PATH/tmp/zips

AUTH_URL=http://auth-service:8080
```

### 🐳 Executando o projeto com Docker Compose

No terminal, navegue até a raiz do projeto e execute:

```bash
 docker-compose up --build
```

A aplicação estará disponível em: http://localhost:8081

Os serviços de infraestrutura estarão acessíveis em:
- **PostgreSQL:** `localhost:5432`
- **RabbitMQ Management:** `localhost:15672` (guest/guest)
- **Redis:** `localhost:6379`

#### ⏹️ Parando os containers

Para parar e remover os containers, execute:

```bash
 docker-compose down
```

---

## 🧪 Testes e Qualidade de Código

O projeto adota boas práticas de testes e qualidade de código, com foco em cobertura e comportamento previsível. Inclui testes de unidade utilizando:

- **JUnit 5**
- **Mockito**

### ▶️ Executando os testes

```bash
 # Executar todos os testes
 mvn test
 
 # Executar testes com relatório de cobertura
 mvn clean verify
```

---

## 👥 Equipe

Desenvolvido pela equipe **FIAP SOAT - G129** como parte do projeto de Arquitetura de Software.

---

## 📄 Licença

Este projeto é parte de um trabalho acadêmico da FIAP.