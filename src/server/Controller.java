package server;

public interface Controller {
    class Parameters {
        private String method = "", query = "", body = "";

        public Parameters() {}

        public Parameters(String method, String query, String body) {
            this.method = method;
            this.query = query;
            this.body = body;
        }

        public Parameters setQuery(String query) {
            this.query = query;
            return this;
        }

        public Parameters setBody(String body) {
            this.body = body;
            return this;
        }

        public Parameters setMethod(String method) {
            this.method = method;
            return this;
        }

        public String getQuery() {
            return query;
        }

        public String getBody() {
            return body;
        }

        public String getMethod() {
            return method;
        }
    }
}
