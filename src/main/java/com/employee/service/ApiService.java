package com.employee.service;

import com.employee.dto.ApiResponse;
import com.employee.dto.Employee;
import com.employee.model.EmployeeModel;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.swing.text.html.Option;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

@Service
public class ApiService {

    private final WebClient webClient;

    public ApiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8000/demo/api/v1").build();
    }

    public Mono<List<Employee>> getDataFromApi() {
        try {
            return webClient.get()
                    .uri("/employees") // Append endpoint to base URL
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                            response -> Mono.error(new RuntimeException("API call failed!")))
                    .bodyToMono(new ParameterizedTypeReference<List<Employee>>() {}); // You can convert to any DTO here
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public Mono<Employee> getEmployee(Long id) {
        try {
            return webClient.get()
                    .uri("/employees/" + id.toString())
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                            response -> Mono.error(new RuntimeException("API call failed!")))
                    .bodyToMono(Employee.class);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public Mono<Employee> updateEmployee(EmployeeModel employee) {
        try {
            return webClient.put()
                    .uri("/employees")
                    .bodyValue(employee)
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                            response -> Mono.error(new RuntimeException("API call failed!")))
                    .bodyToMono(Employee.class);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public Mono<Employee> createEmployee(EmployeeModel employee) {
        try {
            return webClient.post()
                    .uri("/employees")
                    .bodyValue(employee)
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                            response -> Mono.error(new RuntimeException("API call failed!")))
                    .bodyToMono(Employee.class);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public Mono<ApiResponse> deleteEmployee(Long id) {
        try {
            return webClient.delete()
                    .uri("/employees/" + id.toString())
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                            response -> Mono.error(new RuntimeException("API call failed!")))
                    .bodyToMono(ApiResponse.class);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

}
