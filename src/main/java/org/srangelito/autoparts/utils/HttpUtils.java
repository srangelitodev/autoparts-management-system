package org.srangelito.autoparts.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public final class HttpUtils {
    private HttpUtils() throws UnsupportedOperationException {
    }

    public static <T> ResponseEntity<T> buildOkResponseEntity(HttpHeaders httpHeaders, T body) {
        return ResponseEntity.ok().headers(httpHeaders).body(body);
    }

    public static HttpHeaders buildAttachmentHeaders(String mime, String fileName) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.parseMediaType(mime));
        httpHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
        return httpHeaders;
    }
}
