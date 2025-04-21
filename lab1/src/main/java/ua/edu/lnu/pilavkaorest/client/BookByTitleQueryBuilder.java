package ua.edu.lnu.pilavkaorest.client;

public class BookByTitleQueryBuilder {
    private StringBuilder query = new StringBuilder("query { bookByTitle(title: \"");

    private String title;

    public BookByTitleQueryBuilder(String title) {
        this.title = title;
        query.append(title).append("\") {");
    }

    public BookByTitleQueryBuilder title() {
        query.append(" title");
        return this;
    }

    public BookByTitleQueryBuilder author() {
        query.append(" author");
        return this;
    }

    public BookByTitleQueryBuilder year() {
        query.append(" year");
        return this;
    }

    public PublisherQueryBuilder publisher() {
        return new PublisherQueryBuilder(this);
    }

    public String build() {
        query.append(" } }");
        return query.toString();
    }

    public class PublisherQueryBuilder {
        private BookByTitleQueryBuilder parent;

        public PublisherQueryBuilder(BookByTitleQueryBuilder parent) {
            this.parent = parent;
            parent.query.append(" publisher {");
        }

        public PublisherQueryBuilder name() {
            parent.query.append(" name");
            return this;
        }

        public PublisherQueryBuilder country() {
            parent.query.append(" country");
            return this;
        }

        public PublishingDetailsQueryBuilder publishingDetails() {
            return new PublishingDetailsQueryBuilder(this);
        }

        public BookByTitleQueryBuilder endPublisher() {
            parent.query.append(" }");
            return parent;
        }
    }

    public class PublishingDetailsQueryBuilder {
        private PublisherQueryBuilder parent;

        public PublishingDetailsQueryBuilder(PublisherQueryBuilder parent) {
            this.parent = parent;
            parent.parent.query.append(" publishingDetails {");
        }

        public PublishingDetailsQueryBuilder foundedYear() {
            parent.parent.query.append(" foundedYear");
            return this;
        }

        public PublishingDetailsQueryBuilder headquarters() {
            parent.parent.query.append(" headquarters");
            return this;
        }

        public PublisherQueryBuilder endPublishingDetails() {
            parent.parent.query.append(" }");
            return parent;
        }
    }
}

