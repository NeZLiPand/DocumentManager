package tech.innovatelu.document_manager;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

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
    // TODO add in commit info about removed uselles info from doc
    private final Map<String, Document> documentStorage = new ConcurrentHashMap<>();

    /**
     * Implementation of this method should upsert the [document] to your
     * storage and generate unique [id] if it does not exist, don't change [created]
     * field.
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
                return putNewDocument(document);
            }
        } else {
            return putNewDocument(setUniqueId(document));
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
        return getDocumentFromStorage(idOldDocument).map(Document::getCreated)
                                                    .orElseThrow(() -> new IllegalArgumentException("Document with ID "
                                                                                                    + idOldDocument
                                                                                                    + " not found"));
    }

    private Document putNewDocument(Document document) {
        documentStorage.put(document.getId(),
                            document);
        return document;
    }

    private Document setUniqueId(Document document) {
        document.setId(UUID.randomUUID()
                           .toString());
        return document;
    }

    /**
     * Implementation of this method should find documents which match with [request].
     *
     * @param searchRequest - search request, each field could be null
     * @return list matched documents
     */
    public List<Document> search(SearchRequest searchRequest) {
        return documentStorage.values()
                              .stream()
                              .filter(document -> matchesSearchRequest(document,
                                                                       searchRequest))
                              .collect(Collectors.toList());
    }

    private boolean matchesSearchRequest(Document document,
                                         SearchRequest searchRequest) {
        if (searchRequest == null)
            return true;

        return matching(document,
                        searchRequest);
    }

    private boolean matching(Document document,
                             SearchRequest searchRequest) {
        return matchesTitle(document,
                            searchRequest)
               && matchesContent(document,
                                 searchRequest)
               && matchesAuthorIds(document,
                                   searchRequest)
               && matchesCreatedFrom(document,
                                     searchRequest)
               && matchesCreatedTo(document,
                                   searchRequest);
    }

    private boolean matchesTitle(Document document,
                                 SearchRequest searchRequest) {
        return (searchRequest.getTitlePrefixes() == null
                || searchRequest.getTitlePrefixes()
                                .stream()
                                .anyMatch(prefix -> document.getTitle()
                                                            .startsWith(prefix)));
    }

    private boolean matchesContent(Document document,
                                   SearchRequest searchRequest) {
        return (searchRequest.getContainsContents() == null
                || searchRequest.getContainsContents()
                                .stream()
                                .anyMatch(content -> document.getContent()
                                                             .contains(content)));
    }

    private boolean matchesAuthorIds(Document document,
                                     SearchRequest searchRequest) {
        return (searchRequest.getAuthorIds() == null
                || searchRequest.getAuthorIds()
                                .contains(document.getAuthor()
                                                  .getId()));
    }

    private boolean matchesCreatedFrom(Document document,
                                       SearchRequest searchRequest) {
        return (searchRequest.getCreatedFrom() == null
                || !document.getCreated()
                            .isBefore(searchRequest.getCreatedFrom()));
    }

    private boolean matchesCreatedTo(Document document,
                                     SearchRequest searchRequest) {
        return (searchRequest.getCreatedTo() == null
                || !document.getCreated()
                            .isAfter(searchRequest.getCreatedTo()));
    }

    /**
     * Implementation of this method should find document by [id].
     *
     * @param id - document id
     * @return optional document
     */
    public Optional<Document> findById(String id) {
        return getDocumentFromStorage(id);
    }

    private Optional<Document> getDocumentFromStorage(String id) {
        return Optional.ofNullable(documentStorage.get(id));
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
