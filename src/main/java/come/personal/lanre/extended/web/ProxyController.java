package come.personal.lanre.extended.web;

import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Controller
public class ProxyController {

    private final RestTemplate restTemplate;
    private static final Logger logger = LoggerFactory.getLogger(ProxyController.class);

    public ProxyController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @RequestMapping(value = "/proxy", method = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE })
    public ResponseEntity<String> proxyRequest(
        @RequestParam String url,
        HttpMethod method,
        HttpEntity<String> httpEntity,
        HttpServletRequest request
    ) {
        String targetUrl = url.startsWith("http") ? url : "http://" + url;

        HttpHeaders headers = new HttpHeaders();
        headers.putAll(getHeaders(request));

        HttpEntity<String> requestEntity = new HttpEntity<>(httpEntity.getBody(), headers);

        // Logging request details
        logRequestDetails(request, url, headers, httpEntity.getBody());

        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = restTemplate.exchange(targetUrl, method, requestEntity, String.class);
            // Logging response details
            logResponseDetails(responseEntity.getHeaders(), responseEntity.getBody());
        } catch (HttpClientErrorException ex) {
            logger.error(ex.toString(), ex);
            // Extract information from the exception
            HttpStatus statusCode = ex.getStatusCode();
            String responseBody = ex.getResponseBodyAsString();
            logResponseDetails(ex.getResponseHeaders(), responseBody);
            // Create a new ResponseEntity using the extracted information
            responseEntity = new ResponseEntity<>(responseBody, statusCode);
            // Now you can use the responseEntity object as needed
            // For example, you can return it from a method or handle it further
        }
        return responseEntity;
    }

    private HttpHeaders getHeaders(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                headers.add(headerName, request.getHeader(headerName));
            }
        }
        return headers;
    }

    private void logRequestDetails(HttpServletRequest request, String url, HttpHeaders headers, String body) {
        logger.info("Request URL: {}", url);
        logger.info("Request Method: {}", request.getMethod());
        logger.info("Request Headers: {}", headers);
        logger.info("Request Body: {}", body);
    }

    private void logResponseDetails(HttpHeaders headers, String body) {
        logger.info("Response Headers: {}", headers);
        logger.info("Response Body: {}", body);
    }
}
