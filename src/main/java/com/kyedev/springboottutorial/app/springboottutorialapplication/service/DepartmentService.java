package com.kyedev.springboottutorial.app.springboottutorialapplication.service;

import com.kyedev.springboottutorial.app.springboottutorialapplication.entity.Department;
import jakarta.annotation.Nullable;

import java.util.List;

public interface DepartmentService {
    Department saveDepartment(Department department);

    List<Department> getDepartments(
        @Nullable String name,
        @Nullable String code,
        @Nullable String address
    );

    Department getDepartmentById(Long departmentId);

    void deleteDepartmentById(Long departmentId);

    Department updateDepartment(Long departmentId, Department department);
}
