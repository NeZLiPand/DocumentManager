package tech.innovatelu.document_manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class DocumentManager_FindByIdMethodTest {

    private DocumentManager documentManager;

    @BeforeEach
    void setUp() {
        documentManager = new DocumentManager();

        documentManager.save(DocumentManager.Document.builder()
                                                     .id("1")
                                                     .title("First Document")
                                                     .content("Content of the first document")
                                                     .author(DocumentManager.Author.builder()
                                                                                   .id("A1")
                                                                                   .name("Author One")
                                                                                   .build())
                                                     .created(Instant.parse("2024-01-01T10:00:00Z"))
                                                     .build());

        documentManager.save(DocumentManager.Document.builder()
                                                     .id("2")
                                                     .title("Second Document")
                                                     .content("Content of the second document")
                                                     .author(DocumentManager.Author.builder()
                                                                                   .id("A2")
                                                                                   .name("Author Two")
                                                                                   .build())
                                                     .created(Instant.parse("2024-02-01T10:00:00Z"))
                                                     .build());
    }

    @Test
    void testFindByIdWithExistingId() {
        Optional<DocumentManager.Document> result = documentManager.findById("1");

        assertTrue(result.isPresent());
        assertEquals("1",
                     result.get()
                           .getId());
        assertEquals("First Document",
                     result.get()
                           .getTitle());
        assertEquals("Content of the first document",
                     result.get()
                           .getContent());
        assertEquals("A1",
                     result.get()
                           .getAuthor()
                           .getId());
        assertEquals(Instant.parse("2024-01-01T10:00:00Z"),
                     result.get()
                           .getCreated());
    }

    @Test
    void testFindByIdWithNonExistingId() {
        Optional<DocumentManager.Document> result = documentManager.findById("non-existent-id");

        assertFalse(result.isPresent());
    }

    @Test
    void testFindByIdWithNullId() {
        assertThrowsExactly(IllegalArgumentException.class,
                            () -> documentManager.findById(null));
    }

}
