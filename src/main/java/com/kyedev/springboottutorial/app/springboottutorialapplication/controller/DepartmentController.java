package com.kyedev.springboottutorial.app.springboottutorialapplication.controller;


import com.kyedev.springboottutorial.app.springboottutorialapplication.entity.Department;
import com.kyedev.springboottutorial.app.springboottutorialapplication.service.DepartmentService;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/departments")
public class DepartmentController {
    private final DepartmentService departmentService;

    @Autowired
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping()
    public Department saveDepartment(@RequestBody Department department) {
        return departmentService.saveDepartment(department);
    }

    @GetMapping
    public List<Department> getDepartments(
        @Nullable String name,
        @Nullable String code,
        @Nullable String address
    ) {
        return departmentService.getDepartments(name, code, address);
    }

    @GetMapping(value = "/{departmentId}")
    public Department getDepartmentById(@PathVariable("departmentId") Long departmentId) {
        return departmentService.getDepartmentById(departmentId);
    }

    @DeleteMapping(value = "/{departmentId}")
    public String deleteDepartmentById(@PathVariable("departmentId") Long departmentId) {
         departmentService.deleteDepartmentById(departmentId);
         return "Department deleted successfully";
    }

    @PutMapping(value = "/{departmentId}")
    public Department updateDepartment(@PathVariable("departmentId") Long departmentId, @RequestBody Department department) {
        return departmentService.updateDepartment(departmentId, department);
    }

}
