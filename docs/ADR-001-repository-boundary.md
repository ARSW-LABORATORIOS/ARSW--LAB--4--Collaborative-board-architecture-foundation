# ADR-001 — Repository Boundary

## Status
Proposed

## Context
The application needs to store and retrieve Boards, so if
the application service depends directly on a single persistence
implementation like in-memory storage, it could be troublesome
in the future.

## Decision
We decided to separate the application logic from persistence
through the BoardRepository interface. BoardApplicationService
depends on this abstraction and receives its implementation
through dependency injection, and InMemoryBoardRepository
implements this interface to provide the current in-memory storage.

## Positive consequences
With this implementation, we reduce the coupling between the
application logic and the infrastructure. It also allows us to
replace the persistence implementation without modifying
BoardApplicationService.

## Trade-off
We introduced more components and abstractions. This helps
provide a better separation of responsibilities, but it increases
the complexity of the project a little.

## Evidence / validation
InMemoryBoardRepository is used as the current persistence
adapter, while BoardApplicationService works with the
BoardRepository abstraction. We ran 12 tests successfully
to validate our implementation, and we did not get any failures
or errors.