# ARSW Collaborative Architecture Board — Lab 04 

This repository contains the implementation of **Lab #4 — Architecture Foundation**.

The goal is **not** to practice REST syntax. The goal is to build a small backend with explicit architectural boundaries, dependency inversion, constructor injection, consistent error handling, tests, and architecture evidence.

## Technology baseline

- Java 21
- Spring Boot 3.x
- Maven
- In-memory persistence for this lab

## Architecture

```text
REST Controller
      |
      v
Application Service
      |
      v
BoardRepository (port)
      |
      v
InMemoryBoardRepository (adapter)
```
## REST API

The application provides the following Board operations:

- `POST /api/boards` — Create a new Board.
- `GET /api/boards/{boardId}` — Get an existing Board.
- `PUT /api/boards/{boardId}` — Replace the state of an existing Board.

The complete API contract and error responses are 
documented in `docs/api-contract.md`.
## Run

From the project root, run:

```bash
mvn spring-boot:run
```

The application includes a small landing page at:

```text
http://localhost:8080/
```

## Verify

```bash
mvn test
```

The current implementation have 12 automated tests.

Expected result:

```text
Tests run: 12, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

## Continuity rule

Your completed Lab 04 repository becomes the conceptual baseline for **Lab 05 — Interactive Board**. Avoid unnecessary changes to contracts and package boundaries.
