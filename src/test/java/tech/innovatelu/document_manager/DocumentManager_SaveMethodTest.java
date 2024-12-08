package tech.innovatelu.document_manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class DocumentManager_SaveMethodTest {

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
    }

    @Test
    void testSaveNewDocumentWithId() {
        DocumentManager.Document document = DocumentManager.Document.builder()
                                                                    .id("2")
                                                                    .title("Second Document")
                                                                    .content("Content of the second document")
                                                                    .author(DocumentManager.Author.builder()
                                                                                                  .id("A2")
                                                                                                  .name("Author Two")
                                                                                                  .build())
                                                                    .created(Instant.now())
                                                                    .build();

        DocumentManager.Document savedDocument = documentManager.save(document);

        assertEquals("2",
                     savedDocument.getId());
        assertEquals("Second Document",
                     savedDocument.getTitle());

        Optional<DocumentManager.Document> retrieved = documentManager.findById("2");
        assertTrue(retrieved.isPresent());
        assertEquals("Second Document",
                     retrieved.get()
                              .getTitle());
    }

    @Test
    void testSaveNewDocumentWithoutId() {
        DocumentManager.Document document = DocumentManager.Document.builder()
                                                                    .title("New Document")
                                                                    .content("Content without ID")
                                                                    .author(DocumentManager.Author.builder()
                                                                                                  .id("A3")
                                                                                                  .name("Author Three")
                                                                                                  .build())
                                                                    .created(Instant.now())
                                                                    .build();

        DocumentManager.Document savedDocument = documentManager.save(document);

        assertNotNull(savedDocument.getId());
        assertFalse(savedDocument.getId()
                                 .isEmpty());
        assertEquals("New Document",
                     savedDocument.getTitle());

        Optional<DocumentManager.Document> retrieved = documentManager.findById(savedDocument.getId());
        assertTrue(retrieved.isPresent());
        assertEquals("New Document",
                     retrieved.get()
                              .getTitle());
    }

    @Test
    void testUpdateExistingDocument() {
        DocumentManager.Document updatedDocument = DocumentManager.Document.builder()
                                                                           .id("1")
                                                                           .title("Updated Document")
                                                                           .content("Updated content")
                                                                           .author(DocumentManager.Author.builder()
                                                                                                         .id("A1")
                                                                                                         .name("Author One")
                                                                                                         .build())
                                                                           .created(Instant.now())
                                                                           .build();

        DocumentManager.Document savedDocument = documentManager.save(updatedDocument);

        assertEquals("1",
                     savedDocument.getId());
        assertEquals("Updated Document",
                     savedDocument.getTitle());
        assertEquals("Updated content",
                     savedDocument.getContent());
        assertEquals(Instant.parse("2024-01-01T10:00:00Z"),
                     savedDocument.getCreated());

        Optional<DocumentManager.Document> retrieved = documentManager.findById("1");
        assertTrue(retrieved.isPresent());
        assertEquals("Updated Document",
                     retrieved.get()
                              .getTitle());
    }

    @Test
    void testSaveNullDocumentThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                                           () -> documentManager.save(null));

        assertEquals("Document cannot be null",
                     exception.getMessage());
    }

    @Test
    void testSaveDocumentWithNonExistentId() {
        DocumentManager.Document document = DocumentManager.Document.builder()
                                                                    .id("non-existent-id")
                                                                    .title("Non-existent ID Document")
                                                                    .content("Content of a non-existent ID document")
                                                                    .author(DocumentManager.Author.builder()
                                                                                                  .id("A4")
                                                                                                  .name("Author Four")
                                                                                                  .build())
                                                                    .created(Instant.now())
                                                                    .build();

        DocumentManager.Document savedDocument = documentManager.save(document);

        assertEquals("non-existent-id",
                     savedDocument.getId());
        assertEquals("Non-existent ID Document",
                     savedDocument.getTitle());
    }

}
