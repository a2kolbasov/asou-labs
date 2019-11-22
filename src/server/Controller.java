package server;

public interface Controller {
    class Parameters {
        private String query = "", body = "";

        public Parameters setQuery(String query) {
            this.query = query;
            return this;
        }

        public Parameters setBody(String body) {
            this.body = body;
            return this;
        }

        public String getQuery() {
            return query;
        }

        public String getBody() {
            return body;
        }
    }
}
