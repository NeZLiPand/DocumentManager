package tech.innovatelu.document_manager;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * For implement this task focus on clear code, and make this solution as simple
 * readable as possible. Don't worry about performance, concurrency, etc. You
 * can use an in-memory collection for store data.
 * <p>
 * Please, don't change class name, and signature for methods "save", "search",
 * "findById" Implementations should be in a single class. This class could be
 * auto tested
 */
public class DocumentManager {

    private final Map<String, Document> documentStorage = new ConcurrentHashMap<>();

    /**
     * Task Implementation of this method should upsert the [document] to your
     * storage and generate unique [id] if it does not exist, don't change [created]
     * field.
     *
     * Solution This method upsert the [document] to your storage and generate
     * unique [id] if it does not exist, but will not change [created] field.
     *
     * @param document - document content and author data
     * @return saved document
     */
    public Document save(Document document) {
        if (document == null)
            throw new IllegalArgumentException("Document cannot be null");

        return upsertDocument(document);
    }

    private Document upsertDocument(Document document) {
        String id = document.getId();

        if (idIsNeitherNullNorEmpty(id)) {
            if (isDocumentPresentInStorage(id)) {
                return updateDocument(document);
            } else {
                return putNewDocument(id,
                                      document);
            }
        } else {
            id = getUniqueId();
            document.setId(id);
            return putNewDocument(id,
                                  document);
        }

    }

    private boolean idIsNeitherNullNorEmpty(String string) {
        return string != null
               && !string.isEmpty();
    }

    private boolean isDocumentPresentInStorage(String id) {
        return documentStorage.containsKey(id);
    }

    private Document updateDocument(Document document) {
        String idOfDocument = document.getId();
        Instant oldCreationData = getOldDocumentCreationDate(idOfDocument);
        Document updatedDocument = Document.builder()
                                           .author(document.getAuthor())
                                           .content(document.getContent())
                                           .created(oldCreationData)
                                           .id(idOfDocument)
                                           .title(document.getTitle())
                                           .build();
        documentStorage.put(idOfDocument,
                            document);
        return updatedDocument;

    }

    private Instant getOldDocumentCreationDate(String idOldDocument) {
        return Optional.ofNullable(documentStorage.get(idOldDocument))
                       .map(Document::getCreated)
                       .orElseThrow(() -> new IllegalArgumentException("Document with ID "
                                                                       + idOldDocument
                                                                       + " not found"));
    }

    private Document putNewDocument(String id,
                                    Document document) {
        documentStorage.put(id,
                            document);
        return document;
    }

    private String getUniqueId() {
        return UUID.randomUUID()
                   .toString();
    }

    /**
     * Task Implementation of this method should find documents which match with
     * [request].
     *
     * @param request - search request, each field could be null
     * @return list matched documents
     */
    public List<Document> search(SearchRequest request) {

        return Collections.emptyList();
    }

    /**
     * Task Implementation of this method should find document by [id].
     *
     * @param id - document id
     * @return optional document
     */
    public Optional<Document> findById(String id) {

        return Optional.empty();
    }

    @Data
    @Builder
    public static class SearchRequest {
        private List<String> titlePrefixes;
        private List<String> containsContents;
        private List<String> authorIds;
        private Instant createdFrom;
        private Instant createdTo;
    }

    @Data
    @Builder
    public static class Document {
        private String id;
        private String title;
        private String content;
        private Author author;
        private Instant created;
    }

    @Data
    @Builder
    public static class Author {
        private String id;
        private String name;
    }

}
