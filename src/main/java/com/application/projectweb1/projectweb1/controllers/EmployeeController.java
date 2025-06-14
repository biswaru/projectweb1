package com.application.projectweb1.projectweb1.controllers;

import com.application.projectweb1.projectweb1.dto.EmployeeDTO;
import com.application.projectweb1.projectweb1.services.EmployeeService;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping(path = "/employees")
public class EmployeeController {


    private final EmployeeService employeeService;
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }



    @GetMapping("/{employeeId}")
    public EmployeeDTO getEmployeeById(@PathVariable(name = "employeeId") Long Id){
        return employeeService.getEmployeeById(Id);
    }

    @GetMapping
    public List<EmployeeDTO> getAllEmployees(@RequestParam(required = false,name = "inputage") Integer age,
                                                @RequestParam(required = false)String name){
        return employeeService.getAllEmployee();
    }

    @PostMapping
    public EmployeeDTO createNewEmployee(@RequestBody EmployeeDTO inputEmployee){
        return employeeService.createNewEmployee(inputEmployee);
    }

    @PutMapping(path = "/{employeeId}")
    public EmployeeDTO updateEmployeeById(@RequestBody EmployeeDTO employeeDTO,
                                          @PathVariable Long employeeId){
        return employeeService.updateEmployeeById(employeeId, employeeDTO);
    }

    @DeleteMapping(path = "/{employeeId}")
    public boolean deleteEmployeeById(@PathVariable Long employeeId){
        return employeeService.deleteEmployeeById(employeeId);
    }
}
