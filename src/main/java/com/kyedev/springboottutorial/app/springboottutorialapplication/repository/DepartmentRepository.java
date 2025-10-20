package com.kyedev.springboottutorial.app.springboottutorialapplication.repository;


import com.kyedev.springboottutorial.app.springboottutorialapplication.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long>, JpaSpecificationExecutor<Department> {

}
