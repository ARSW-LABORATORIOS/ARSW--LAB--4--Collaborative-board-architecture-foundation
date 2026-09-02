package edu.eci.arsw.collabboard.application.service;

import edu.eci.arsw.collabboard.application.exception.BoardNotFoundException;
import edu.eci.arsw.collabboard.domain.model.Board;
import edu.eci.arsw.collabboard.domain.model.BoardElement;
import edu.eci.arsw.collabboard.domain.model.ElementType;
import edu.eci.arsw.collabboard.infrastructure.persistence.InMemoryBoardRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BoardApplicationServiceTest {

    private final BoardApplicationService service =
            new BoardApplicationService(new InMemoryBoardRepository());

    @Test
    void shouldCreateAndReadBoard() {
        Board created = service.createBoard("Architecture Session");
        Board loaded = service.getBoard(created.id());

        assertEquals(created, loaded);
    }

    @Test
    void shouldFailWithConcreteExceptionWhenBoardDoesNotExist() {
        assertThrows(BoardNotFoundException.class,
                () -> service.getBoard("missing-board"));
    }

    @Test
    void shouldReplaceBoardSuccessfully() {
        Board created = service.createBoard("Initial Name");
        BoardElement element = new BoardElement("e1", ElementType.RECTANGLE, 0, 0, 100, 50, "");

        Board replaced = service.replaceBoard(created.id(), "Updated Name", List.of(element));

        assertEquals(created.id(), replaced.id());
        assertEquals("Updated Name", replaced.name());
        assertEquals(1, replaced.elements().size());
    }

    @Test
    void shouldFailReplacingNonExistentBoard() {
        assertThrows(BoardNotFoundException.class,
                () -> service.replaceBoard("ghost-id", "Name", List.of()));
    }

    @Test
    void shouldGenerateUniqueIdsForEachBoard() {
        Board b1 = service.createBoard("Board One");
        Board b2 = service.createBoard("Board Two");

        assertNotEquals(b1.id(), b2.id());
    }
}
