package com.pricingia.saas.common.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GlobalExceptionHandlerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(new TestController())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void shouldHandleNotFoundException() throws Exception {
        mockMvc.perform(get("/test/not-found"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Resource not found"))
                .andExpect(jsonPath("$.path").value("/test/not-found"));
    }

    @Test
    void shouldHandleBadRequestException() throws Exception {
        mockMvc.perform(get("/test/bad-request"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Invalid input"))
                .andExpect(jsonPath("$.path").value("/test/bad-request"));
    }

    @Test
    void shouldHandleBusinessException() throws Exception {
        mockMvc.perform(get("/test/business"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.status").value(422))
                .andExpect(jsonPath("$.error").value("Unprocessable Entity"))
                .andExpect(jsonPath("$.message").value("Business rule violated"))
                .andExpect(jsonPath("$.path").value("/test/business"));
    }

    @Test
    void shouldHandleUnauthorizedException() throws Exception {
        mockMvc.perform(get("/test/unauthorized"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.error").value("Unauthorized"))
                .andExpect(jsonPath("$.message").value("Missing credentials"))
                .andExpect(jsonPath("$.path").value("/test/unauthorized"));
    }

    @Test
    void shouldHandleServiceUnavailableException() throws Exception {
        mockMvc.perform(get("/test/service-unavailable"))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.status").value(503))
                .andExpect(jsonPath("$.error").value("Service Unavailable"))
                .andExpect(jsonPath("$.message").value("External service down"))
                .andExpect(jsonPath("$.path").value("/test/service-unavailable"));
    }

    @Test
    void shouldHandleMethodArgumentNotValidException() throws Exception {
        mockMvc.perform(post("/test/validation")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("name: must not be blank"))
                .andExpect(jsonPath("$.path").value("/test/validation"));
    }

    @Test
    void shouldHandleHttpRequestMethodNotSupportedException() throws Exception {
        mockMvc.perform(post("/test/not-found"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value(405))
                .andExpect(jsonPath("$.error").value("Method Not Allowed"))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.path").value("/test/not-found"));
    }

    @Test
    void shouldHandleUnexpectedException() throws Exception {
        mockMvc.perform(get("/test/unexpected"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.error").value("Internal Server Error"))
                .andExpect(jsonPath("$.message").value("Unexpected error"))
                .andExpect(jsonPath("$.path").value("/test/unexpected"));
    }

    @RestController
    @RequestMapping("/test")
    static class TestController {

        @GetMapping("/not-found")
        public void notFound() {
            throw new NotFoundException("Resource not found");
        }

        @GetMapping("/bad-request")
        public void badRequest() {
            throw new BadRequestException("Invalid input");
        }

        @GetMapping("/business")
        public void business() {
            throw new BusinessException("Business rule violated");
        }

        @GetMapping("/unauthorized")
        public void unauthorized() {
            throw new UnauthorizedException("Missing credentials");
        }

        @GetMapping("/service-unavailable")
        public void serviceUnavailable() {
            throw new ServiceUnavailableException("External service down");
        }

        @GetMapping("/unexpected")
        public void unexpected() {
            throw new RuntimeException("Something went horribly wrong");
        }

        @PostMapping("/validation")
        public void validation(@Valid @RequestBody ValidatedRequest request) {
            // Do nothing
        }
    }

    record ValidatedRequest(@NotBlank String name) {}
}
