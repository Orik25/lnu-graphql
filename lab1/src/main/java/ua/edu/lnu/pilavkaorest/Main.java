package ua.edu.lnu.pilavkaorest;

import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.schema.StaticDataFetcher;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import ua.edu.lnu.pilavkaorest.model.Book;
import ua.edu.lnu.pilavkaorest.model.Publisher;
import ua.edu.lnu.pilavkaorest.model.PublishingDetails;
import ua.edu.lnu.pilavkaorest.util.FileUtils;
import ua.edu.lnu.pilavkaorest.util.JsonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Main {

    private static final String SCHEMA_PATH = "/graphql/schema/schema.graphqls";
    private static final String DATA_PATH = "/data/books.json";
    private static final Function<String, String> buildQueryPath = fileName -> String.format("/graphql/query/%s.graphql", fileName);

    private static List<Book> books;
    private static GraphQL graphQL;

    public static void main1(String[] args) {
        books = loadDataFromStorage();
        graphQL = createGraphQLEntryPoint(SCHEMA_PATH);

//        var allBooksResult = graphQL.execute(FileUtils.readFileContent(buildQueryPath.apply("all-books"))).toSpecification();
//        System.out.println(JsonUtils.serializeToJson(allBooksResult));
//
//        var addBookResult = graphQL.execute(FileUtils.readFileContent(buildQueryPath.apply("add-book"))).toSpecification();
//        System.out.println(JsonUtils.serializeToJson(addBookResult));
//
//        var allBooksAfterUpdateResult = graphQL.execute(FileUtils.readFileContent(buildQueryPath.apply("all-books"))).toSpecification();
//        System.out.println(JsonUtils.serializeToJson(allBooksAfterUpdateResult));

//        var bookByTitle = graphQL.execute(FileUtils.readFileContent(buildQueryPath.apply("book-by-title"))).toSpecification();
//        System.out.println(JsonUtils.serializeToJson(bookByTitle));

        var booksAfterYear = graphQL.execute(FileUtils.readFileContent(buildQueryPath.apply("books-after-year"))).toSpecification();
        System.out.println(JsonUtils.serializeToJson(booksAfterYear));

        var authorName = graphQL.execute("query { authorName }").toSpecification();
        System.out.println(authorName);

        System.out.println();

    }

    private static List<Book> loadDataFromStorage() {
        return new ArrayList<>(JsonUtils.loadListFromJsonFile(DATA_PATH, Book[].class));
    }

    private static GraphQL createGraphQLEntryPoint(String schemaPath) {
        TypeDefinitionRegistry typeRegistry = parseSchema(schemaPath);
        RuntimeWiring runtimeWiring = buildWiring();
        GraphQLSchema graphQLSchema = new SchemaGenerator().makeExecutableSchema(typeRegistry, runtimeWiring);
        return GraphQL.newGraphQL(graphQLSchema).build();
    }

    private static TypeDefinitionRegistry parseSchema(String schemaPath) {
        String schema = FileUtils.readFileContent(schemaPath);
        SchemaParser schemaParser = new SchemaParser();
        return schemaParser.parse(schema);
    }

    private static RuntimeWiring buildWiring() {
        DataFetcher<List<Book>> allBooksFetcher = env -> books;

        DataFetcher<Book> bookByTitleFetcher = env -> books.stream()
                .filter(book -> book.getTitle().equalsIgnoreCase(env.getArgument("title")))
                .findFirst()
                .orElse(null);

        DataFetcher<List<Book>> bookAfterYearFetcher = env -> books.stream()
                .filter(book -> book.getYear() > (int) env.getArgument("year"))
                .toList();

        DataFetcher<Book> addBookFetcher = env -> {
            Map<String, Object> input = env.getArgument("input");
            Map<String, Object> publisherInput = (Map<String, Object>) input.get("publisher");
            Map<String, Object> publishingDetailsInput = (Map<String, Object>) publisherInput.get("publishingDetails");

            PublishingDetails publishingDetails = new PublishingDetails(
                    (int) publishingDetailsInput.get("foundedYear"),
                    (String) publishingDetailsInput.get("headquarters")
            );

            Publisher publisher = new Publisher(
                    (String) publisherInput.get("name"),
                    (String) publisherInput.get("country"),
                    publishingDetails
            );

            Book book = new Book((String) input.get("title"), (String) input.get("author"), (int) input.get("year"), publisher);
            books.add(book);
            return book;
        };

        StaticDataFetcher staticDataFetcher = new StaticDataFetcher("Orest");

        return RuntimeWiring.newRuntimeWiring()
                .type("Query", builder -> builder
                        .dataFetcher("allBooks", allBooksFetcher)
                        .dataFetcher("bookByTitle", bookByTitleFetcher)
                        .dataFetcher("booksAfterYear", bookAfterYearFetcher)
                        .dataFetcher("authorName", staticDataFetcher))
                .type("Mutation", builder -> builder
                        .dataFetcher("addBook", addBookFetcher))
                .build();
    }
}