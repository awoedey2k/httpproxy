package come.personal.lanre.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Switch Http Proxy.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link tech.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private final HttpTimeout httpTimeout = new HttpTimeout();

    public HttpTimeout getHttpTimeout() {
        return httpTimeout;
    }

    public static class HttpTimeout {

        private int connect = 60000;
        private int read = 60000;

        public int getConnect() {
            return connect;
        }

        public void setConnect(int connect) {
            this.connect = connect;
        }

        public int getRead() {
            return read;
        }

        public void setRead(int read) {
            this.read = read;
        }
    }
}
