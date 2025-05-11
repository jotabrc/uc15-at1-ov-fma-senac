# PROJETO INTEGRADOR UC15 — SENAC

# DOCUMENTAÇÃO TÉCNICA

**Responsible**: João Carlos Roveda Ostrovski  
**System name**: OV-FMA \- Ostro Veda Finance Management Application (Aplicação de Gerenciamento Financeiro)  
**Versão**: 1.0.0

# DESCRIÇÃO

Gestão financeira, usuários podem inserir seus créditos e débitos, sejam eles recorrentes ou únicos, e desta forma planejar seu orçamento.

# TECNOLOGIAS

* Linguagem de programação: Java;
* Database: MySQL;
* OS: Windows.

# DEPENDÊNCIAS

* FLYWAY
  * Database migration, irá auxiliar na criação do banco de dados e sua manutenção, possibilitando uma evolução contínua e estruturada.
* JDBC
  * MySQL Connector, será o driver de conexão com o banco de dados.
* Lombok
  * Visando um código limpo, bem estruturado e conciso, será utilizado para anotar as classes e para geração de código boilerplate na compilação do projeto.
* SLF4J
  * Logs com Logback.

# FUNCIONAL

* Usuários
  * Registro de conta;
  * Atualização de dados de cadastro;
  * Autenticação;
  * Registro de débitos e créditos;
    * Recorrentes ou não.
  * Atualização de débitos e créditos;
  * Visualização de dados.
* Administradores
  * Todos os recursos que os usuários;
  * Desativar contas de usuário.


# Diagram
![ov-fma.drawio(6).png](docs/ov-fma.drawio%286%29.png)