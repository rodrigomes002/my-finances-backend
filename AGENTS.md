# Contexto do projeto

## Objetivo
Ajudar usuários com a gestão financeira pessoal, dar vizibilidade dos gastos pessoais.

## Stack tecnológica
- Linguagem: Java
- Framework: SpringBoot

## Arquitetura
- Seguir a arquitetura Hexagonal com DDD.
- Separar responsabilidades por camada.
- Evitar regras de negócio em controllers.
- Utilizar injeção de dependência.
- Respeitar os princípios SOLID.

## Regras de desenvolvimento
- Seguir padrões KISS e SOLID
- Reutilizar componentes existentes.
- Evitar duplicação de código.
- Não adicionar dependências sem necessidade.
- Não alterar contratos de API sem autorização.
- Criar testes para novas regras de negócio.

## Antes de alterar código
- Identificar os componentes envolvidos.
- Verificar dependências e efeitos colaterais.
- Explicar brevemente a abordagem.
- Implementar a menor alteração necessária.