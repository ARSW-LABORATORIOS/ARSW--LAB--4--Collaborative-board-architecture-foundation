# REST Contract — Lab 04

Base path: `/api/boards`

Todos los `Board` se serializan como:

```json
{
  "id": "string",
  "name": "string",
  "elements": [
    {
      "id": "string",
      "type": "RECTANGLE | TEXT",
      "x": 0,
      "y": 0,
      "width": 0,
      "height": 0,
      "text": "string"
    }
  ]
}
```

| Method | Resource | Request | Success response | Error cases |
|---|---|---|---|---|
| POST | `/api/boards` | `{"name": "string"}` | `201 Created` + `Board` (id generado por el servidor, `elements: []`) | `400 INVALID_REQUEST` si `name` es vacío/nulo |
| GET | `/api/boards/{boardId}` | - | `200 OK` + `Board` | `404 BOARD_NOT_FOUND` si el id no existe |
| PUT | `/api/boards/{boardId}` | `{"name": "string", "elements": [BoardElement...]}` | `200 OK` + `Board` reemplazado (conserva el `id` de la URL) | `404 BOARD_NOT_FOUND` si el board no existe; `400 INVALID_REQUEST`/`INVALID_INPUT` si `name` es vacío, `elements` es nulo, o algún elemento viola sus invariantes (id vacío, tipo nulo, dimensiones negativas) |

## Error contract

Toda respuesta de error usa el mismo cuerpo `ApiError`, sin exponer stack traces ni mensajes internos de Java:

```json
{
  "timestamp": "2026-09-02T10:15:30Z",
  "status": 404,
  "code": "BOARD_NOT_FOUND",
  "message": "Board not found: <boardId>",
  "path": "/api/boards/<boardId>"
}
```

| Code | HTTP status | Cuándo ocurre |
|---|---|---|
| `BOARD_NOT_FOUND` | 404 | GET/PUT sobre un `boardId` que no existe |
| `INVALID_REQUEST` | 400 | Falla `@Valid` en el body (`name` vacío, `elements` nulo) |
| `INVALID_INPUT` | 400 | El dominio rechaza los datos al construir `Board`/`BoardElement` (invariantes) |
| `MALFORMED_REQUEST` | 400 | Body ausente o JSON mal formado |
| `INTERNAL_ERROR` | 500 | Error inesperado no mapeado a los casos anteriores |

No hay desviaciones sobre el contrato mínimo propuesto en la guía del laboratorio.
