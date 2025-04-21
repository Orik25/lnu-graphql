package ua.edu.lnu.pilavkaorest.client;

public class BooksAfterYearQueryBuilder {
    private StringBuilder query;
    private int year;

    public BooksAfterYearQueryBuilder(int year) {
        this.year = year;
        query = new StringBuilder("query { booksAfterYear(year: " + year + ") {");
    }

    public BooksAfterYearQueryBuilder title() {
        query.append(" title");
        return this;
    }

    public BooksAfterYearQueryBuilder author() {
        query.append(" author");
        return this;
    }

    public BooksAfterYearQueryBuilder year() {
        query.append(" year");
        return this;
    }

    public String build() {
        query.append(" } }");
        return query.toString();
    }
}

