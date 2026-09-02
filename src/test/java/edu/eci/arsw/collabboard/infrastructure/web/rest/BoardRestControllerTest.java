package edu.eci.arsw.collabboard.infrastructure.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BoardRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createsBoardAndReturns201WithGeneratedId() throws Exception {
        mockMvc.perform(post("/api/boards")
                        .contentType("application/json")
                        .content("{\"name\":\"Architecture Session\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name").value("Architecture Session"))
                .andExpect(jsonPath("$.elements").isArray())
                .andExpect(jsonPath("$.elements").isEmpty());
    }

    @Test
    void rejectsCreateWithBlankName() throws Exception {
        mockMvc.perform(post("/api/boards")
                        .contentType("application/json")
                        .content("{\"name\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_REQUEST"))
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void getsAnExistingBoard() throws Exception {
        String id = createBoard("Sprint Planning");

        mockMvc.perform(get("/api/boards/{boardId}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Sprint Planning"));
    }

    @Test
    void getMissingBoardReturnsUniformNotFoundError() throws Exception {
        mockMvc.perform(get("/api/boards/{boardId}", "does-not-exist"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("BOARD_NOT_FOUND"))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.path").value("/api/boards/does-not-exist"))
                .andExpect(jsonPath("$.timestamp", notNullValue()));
    }

    @Test
    void replacesAnExistingBoard() throws Exception {
        String id = createBoard("Initial Name");
        String body = """
                {
                  "name": "Updated Name",
                  "elements": [
                    {"id": "e1", "type": "RECTANGLE", "x": 0, "y": 0, "width": 100, "height": 50, "text": ""}
                  ]
                }
                """;

        mockMvc.perform(put("/api/boards/{boardId}", id)
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Updated Name"))
                .andExpect(jsonPath("$.elements.length()").value(1));
    }

    @Test
    void replaceMissingBoardReturnsUniformNotFoundError() throws Exception {
        String body = "{\"name\":\"Name\",\"elements\":[]}";

        mockMvc.perform(put("/api/boards/{boardId}", "ghost-id")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("BOARD_NOT_FOUND"));
    }

    @Test
    void replaceWithInvalidElementReturnsUniformBadRequestError() throws Exception {
        String id = createBoard("Board With Bad Element");
        String body = """
                {
                  "name": "Board With Bad Element",
                  "elements": [
                    {"id": "e1", "type": "RECTANGLE", "x": 0, "y": 0, "width": -10, "height": 50, "text": ""}
                  ]
                }
                """;

        mockMvc.perform(put("/api/boards/{boardId}", id)
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_INPUT"));
    }

    private String createBoard(String name) throws Exception {
        String response = mockMvc.perform(post("/api/boards")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(new CreateBoardRequest(name))))
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readTree(response).get("id").asText();
    }
}
