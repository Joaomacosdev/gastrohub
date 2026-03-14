# 🍽️ GastroHub

> 📐 **Tech Lead:** consulte o [Planejamento de Execução da Fase 1](./PLANEJAMENTO.md) para o roadmap completo por sprints, critérios de DoD e pontos de atenção técnica.

---

## 📌 O que o projeto faz

O **GastroHub** é um sistema backend desenvolvido para gerenciamento de usuários em uma plataforma de restaurantes compartilhada.

A aplicação permite:

- Cadastro de usuários (clientes e donos de restaurante)
- Atualização de dados do usuário
- Alteração de senha em endpoint separado
- Busca de usuários por nome
- Validação de login (login e senha)
- Garantia de e-mail único
- Registro da data da última atualização

---

## 🚀 Tecnologias utilizadas

<p align="left">
<img src="https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk"/>
<img src="https://img.shields.io/badge/Spring_Boot-3.2.x-brightgreen?style=for-the-badge&logo=springboot"/>
<img src="https://img.shields.io/badge/Spring_Data_JPA-blue?style=for-the-badge&logo=spring"/>
<img src="https://img.shields.io/badge/Spring_Security-6.x-darkgreen?style=for-the-badge&logo=springsecurity"/>
<img src="https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white"/>
<img src="https://img.shields.io/badge/Flyway-10.x-CC0200?style=for-the-badge&logo=flyway"/>
<img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white"/>
<img src="https://img.shields.io/badge/JUnit_5-25A162?style=for-the-badge&logo=java&logoColor=white"/>
<img src="https://img.shields.io/badge/Mockito-6DB33F?style=for-the-badge"/>
<img src="https://img.shields.io/badge/MapStruct-1.5.x-FF6F00?style=for-the-badge"/>
<img src="https://img.shields.io/badge/Swagger-SpringDoc_2.x-85EA2D?style=for-the-badge&logo=swagger"/>
</p>

---

## ⚙️ Como instalar e rodar localmente

### 🔧 Pré-requisitos

- Java 21
- Maven
- Docker + Docker Compose

---

### 🐳 Rodando com Docker

    git clone https://github.com/seu-usuario/gastrohub.git
    cd gastrohub
    docker-compose up --build

Acesse:

- API: http://localhost:8080
- Swagger: http://localhost:8080/swagger-ui.html

---

### 💻 Rodando sem Docker

    mvn clean install
    mvn spring-boot:run

---

## 📸 Screenshots / GIFs

### 🔹 Swagger UI
![Swagger](https://via.placeholder.com/800x400?text=Swagger+UI)

### 🔹 Cadastro de usuário
![Cadastro](https://via.placeholder.com/800x400?text=Cadastro+de+Usuario)

### 🔹 Busca de usuários
![Busca](https://via.placeholder.com/800x400?text=Busca+de+Usuarios)

---

## 🌐 Deploy

🚧 Em breve...

---

## ⚔️ Desafios enfrentados

### 🔸 Organização do código
- Uso de **DTOs + MapStruct** para desacoplamento e manutenção

### 🔸 Validação de dados
- Uso de **Bean Validation** e constraints no banco para garantir integridade

### 🔸 Padronização de erros
- Implementação do padrão **ProblemDetail (RFC 7807)**

### 🔸 Testes automatizados
- Utilização de **JUnit + Mockito** para garantir qualidade e confiabilidade

### 🔸 Documentação
- Integração com **SpringDoc OpenAPI (Swagger)** para documentação automática

### 🔸 Ambiente
- Uso de **Docker Compose** para padronizar execução da aplicação e banco de dados

---

## 📂 Estrutura do projeto

    src/main/java/br/com/gastrohub/
     ├── config/           # CorsConfig, OpenApiConfig
     ├── infra/
     │   ├── exception/    # NotFoundException, GlobalExceptionHandler (RFC 7807)
     │   └── security/     # SecurityConfiguration, JwtAuthFilter, JwtTokenProvider
     ├── user/
     │   ├── controller/   # UserController, AuthController
     │   ├── dto/          # Request e Response DTOs
     │   ├── entity/       # User, Endereco, enums
     │   ├── mapper/       # UserMapper (MapStruct)
     │   ├── repository/   # UserRepository
     │   ├── service/      # UserService, AuthService e implementações
     │   └── strategy/     # ValidateUserStrategy
     └── notification/     # Módulo de notificações (futuro)

---

## 👨‍💻 Autor(es)

Projeto desenvolvido para o **Tech Challenge - FIAP** 🚀