package com.employee.controller;

import com.employee.model.EmployeeModel;
import com.employee.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import reactor.core.publisher.Mono;

@Controller
public class HomeController {

    @Autowired
    private final ApiService apiService;

    public HomeController(ApiService apiService) {
        this.apiService = apiService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("message", "Welcome to Thymeleaf with Spring Boot!");
        return "home";
    }

    @GetMapping("/createEmployee")
    public String createEmployee(Model model) {
        model.addAttribute("showStatus", false);
        model.addAttribute("employeeModel", new EmployeeModel());
        return "createEmployee";
    }

    @PostMapping("/saveEmployee")
    public Mono<String> saveEmployee(@ModelAttribute EmployeeModel employeeModel, Model model) {

        model.addAttribute("showStatus", true);

        if( employeeModel.getId() != null ) {
            return this.apiService.updateEmployee(employeeModel).doOnSuccess(employee -> {
                model.addAttribute("employeeModel", employee);
            })
            .thenReturn("createEmployee");
        }
        else {
            return this.apiService.createEmployee(employeeModel).doOnSuccess(employee -> {
                        model.addAttribute("employeeModel", employee);
                    })
                    .thenReturn("createEmployee");
        }
    }

    @GetMapping("/editEmployee/{id}")
    public Mono<String> editEmployee(@PathVariable("id") Long id, Model model) {

        return this.apiService.getEmployee(id)
                .doOnSuccess(employee -> {
                    model.addAttribute("employeeModel", employee);
                })
                .thenReturn("createEmployee");
    }

    @GetMapping("/deleteEmployee/{id}")
    public Mono<String> deleteEmployee(@PathVariable("id") Long id, Model model) {

        return this.apiService.deleteEmployee(id)
                .doOnSuccess(apiResponse -> {
                    model.addAttribute("message", apiResponse.getMessage());
                })
                .thenReturn("deleteEmployeeInfo");
    }

    @GetMapping("/showEmployees")
    public Mono<String> showEmployees(Model model) {

        model.addAttribute("employees", null);

        return this.apiService.getDataFromApi()
                .doOnSuccess(employees -> {
                    model.addAttribute("employees", employees);
                })
                .thenReturn("showEmployees");
    }



}
