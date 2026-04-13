package br.com.gastrohub.infra.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.net.URI;
import java.time.Instant;

public class ProblemDetailFactory {

    public static ProblemDetail create(HttpStatus status, String title, String detail, String path) {
        ProblemDetail pd = ProblemDetail.forStatus(status);
        pd.setTitle(title);
        pd.setDetail(detail);
        pd.setInstance(URI.create(path));
        pd.setProperty("timestamp", Instant.now());
        return pd;
    }
}
