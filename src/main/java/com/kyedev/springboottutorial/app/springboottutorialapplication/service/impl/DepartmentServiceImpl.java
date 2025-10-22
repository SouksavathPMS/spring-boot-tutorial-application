package com.kyedev.springboottutorial.app.springboottutorialapplication.service.impl;


import com.kyedev.springboottutorial.app.springboottutorialapplication.entity.Department;
import com.kyedev.springboottutorial.app.springboottutorialapplication.error.exceptions.DepartmentNotFoundException;
import com.kyedev.springboottutorial.app.springboottutorialapplication.repository.DepartmentRepository;
import com.kyedev.springboottutorial.app.springboottutorialapplication.service.DepartmentService;
import io.micrometer.common.lang.Nullable;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public Department saveDepartment(Department department) {
        return departmentRepository.save(department);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Department> getDepartments(
            @Nullable String name,
            @Nullable String code,
            @Nullable String address
    ) {
        if ((name == null || name.trim().isEmpty()) &&
                (code == null || code.trim().isEmpty()) &&
                (address == null || address.trim().isEmpty())) {
            return departmentRepository.findAll();
        }

        Specification<Department> spec = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (name != null && !name.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("departmentName")),
                        "%" + name.toLowerCase() + "%"
                ));
            }

            if (code != null && !code.trim().isEmpty()) {
                predicates.add(criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("departmentCode")),
                        code.toLowerCase()
                ));
            }

            if (address != null && !address.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("departmentAddress")),
                        "%" + address.toLowerCase() + "%"
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return departmentRepository.findAll(spec);
    }

    @Override
    @Transactional(readOnly = true)
    public Department getDepartmentById(Long departmentId) throws DepartmentNotFoundException {
        Optional<Department> optionalDepartment = departmentRepository.findById(departmentId);
        if(optionalDepartment.isEmpty()) {
            throw new DepartmentNotFoundException("Department with id " + departmentId + " not found");
        }
        return optionalDepartment.get();
    }

    @Override
    public void deleteDepartmentById(Long departmentId) {
        if (!departmentRepository.existsById(departmentId)) {
            throw new EntityNotFoundException(
                    "Department not found with id: " + departmentId);
        }
        departmentRepository.deleteById(departmentId);
    }

    @Override
    public Department updateDepartment(Long departmentId, Department department) {
        Department existingDepartment = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Department not found with id: " + departmentId));

        String newDepartmentName = department.getDepartmentName();
        String newDepartmentCode = department.getDepartmentCode();
        String newDepartmentAddress = department.getDepartmentAddress();

        if (Objects.nonNull(newDepartmentName) && !newDepartmentName.trim().isEmpty()) {
            existingDepartment.setDepartmentName(newDepartmentName);
        }
        if (Objects.nonNull(newDepartmentCode) && !newDepartmentCode.trim().isEmpty()) {
            existingDepartment.setDepartmentCode(newDepartmentCode);
        }
        if (Objects.nonNull(newDepartmentAddress) && !newDepartmentAddress.trim().isEmpty()) {
            existingDepartment.setDepartmentAddress(newDepartmentAddress);
        }

        return departmentRepository.save(existingDepartment);
    }
}