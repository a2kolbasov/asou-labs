/*
 * Copyright © 2019 Alexander Kolbasov
 */

package server;

public interface Controller {
    class Parameters {
        private final String method, query, body;

        public Parameters(String method, String query, String body) {
            this.method = method;
            this.query = query;
            this.body = body;
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
