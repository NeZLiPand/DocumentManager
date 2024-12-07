package tech.innovatelu.document_manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DocumentManager_SearchMethodTest {

    private DocumentManager documentManager;

    @BeforeEach
    void setUp() {
        documentManager = new DocumentManager();

        documentManager.save(DocumentManager.Document.builder()
                                                     .id("1")
                                                     .title("First Document")
                                                     .content("This is the content of the first document.")
                                                     .author(DocumentManager.Author.builder()
                                                                                   .id("A1")
                                                                                   .name("Author One")
                                                                                   .build())
                                                     .created(Instant.parse("2024-01-01T10:00:00Z"))
                                                     .build());

        documentManager.save(DocumentManager.Document.builder()
                                                     .id("2")
                                                     .title("Second Document")
                                                     .content("This document talks about Java.")
                                                     .author(DocumentManager.Author.builder()
                                                                                   .id("A2")
                                                                                   .name("Author Two")
                                                                                   .build())
                                                     .created(Instant.parse("2024-02-01T10:00:00Z"))
                                                     .build());

        documentManager.save(DocumentManager.Document.builder()
                                                     .id("3")
                                                     .title("Third Document")
                                                     .content("Java is a great programming language.")
                                                     .author(DocumentManager.Author.builder()
                                                                                   .id("A3")
                                                                                   .name("Author Three")
                                                                                   .build())
                                                     .created(Instant.parse("2024-03-01T10:00:00Z"))
                                                     .build());

        documentManager.save(DocumentManager.Document.builder()
                                                     .id("4")
                                                     .title("Fourth Document")
                                                     .content("Content about something else.")
                                                     .author(DocumentManager.Author.builder()
                                                                                   .id("A1")
                                                                                   .name("Author One")
                                                                                   .build())
                                                     .created(Instant.parse("2024-04-01T10:00:00Z"))
                                                     .build());

        documentManager.save(DocumentManager.Document.builder()
                                                     .id("5")
                                                     .title("Fifth Document")
                                                     .content("This document contains important information.")
                                                     .author(DocumentManager.Author.builder()
                                                                                   .id("A2")
                                                                                   .name("Author Two")
                                                                                   .build())
                                                     .created(Instant.parse("2024-05-01T10:00:00Z"))
                                                     .build());
    }

    @Test
    void testSearchWithNullRequest() {
        List<DocumentManager.Document> results = documentManager.search(null);

        assertEquals(5,
                     results.size());
    }

    @Test
    void testSearchByTitlePrefix() {
        DocumentManager.SearchRequest request = DocumentManager.SearchRequest.builder()
                                                                             .titlePrefixes(List.of("First"))
                                                                             .build();

        List<DocumentManager.Document> results = documentManager.search(request);

        assertEquals(1,
                     results.size());
        assertEquals("First Document",
                     results.get(0)
                            .getTitle());
    }

    @Test
    void testSearchByContent() {
        DocumentManager.SearchRequest request = DocumentManager.SearchRequest.builder()
                                                                             .containsContents(List.of("Java"))
                                                                             .build();

        List<DocumentManager.Document> results = documentManager.search(request);

        assertEquals(2,
                     results.size());
    }

    @Test
    void testSearchByAuthorId() {
        DocumentManager.SearchRequest request = DocumentManager.SearchRequest.builder()
                                                                             .authorIds(List.of("A1"))
                                                                             .build();

        List<DocumentManager.Document> results = documentManager.search(request);

        assertEquals(2,
                     results.size());
    }

    @Test
    void testSearchByDateRange() {
        DocumentManager.SearchRequest request = DocumentManager.SearchRequest.builder()
                                                                             .createdFrom(Instant.parse("2024-01-01T00:00:00Z"))
                                                                             .createdTo(Instant.parse("2024-03-31T23:59:59Z"))
                                                                             .build();

        List<DocumentManager.Document> results = documentManager.search(request);

        assertEquals(3,
                     results.size());
    }

    @Test
    void testSearchWithNoResults() {
        DocumentManager.SearchRequest request = DocumentManager.SearchRequest.builder()
                                                                             .titlePrefixes(List.of("Nonexistent"))
                                                                             .build();

        List<DocumentManager.Document> results = documentManager.search(request);

        assertTrue(results.isEmpty());
    }

    @Test
    void testSearchMultipleFilters() {
        DocumentManager.SearchRequest request = DocumentManager.SearchRequest.builder()
                                                                             .titlePrefixes(List.of("First",
                                                                                                    "Second",
                                                                                                    "Third"))
                                                                             .containsContents(List.of("Java"))
                                                                             .authorIds(List.of("A2",
                                                                                                "A3"))
                                                                             .createdFrom(Instant.parse("2024-01-01T00:00:00Z"))
                                                                             .createdTo(Instant.parse("2024-05-01T10:00:00Z"))
                                                                             .build();

        List<DocumentManager.Document> results = documentManager.search(request);

        assertEquals(2,
                     results.size());
    }
}
