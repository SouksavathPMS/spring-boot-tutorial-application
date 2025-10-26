package com.kyedev.springboottutorial.app.springboottutorialapplication.service;

import com.kyedev.springboottutorial.app.springboottutorialapplication.entity.Department;
import com.kyedev.springboottutorial.app.springboottutorialapplication.error.exceptions.DepartmentNotFoundException;
import com.kyedev.springboottutorial.app.springboottutorialapplication.repository.DepartmentRepository;
import com.kyedev.springboottutorial.app.springboottutorialapplication.service.impl.DepartmentServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;



@ExtendWith(MockitoExtension.class)
@DisplayName("Department Service Tests")
class DepartmentServiceTest {

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    @Mock
    private DepartmentRepository departmentRepository;

    private Department testDepartment;
    private Department anotherDepartment;

    @BeforeEach
    void setUp() {
        testDepartment = Department.builder()
                .departmentId(1L)
                .departmentName("IT Department")
                .departmentCode("IT001")
                .departmentAddress("Building A, Floor 3")
                .build();

        anotherDepartment = Department.builder()
                .departmentId(2L)
                .departmentName("HR Department")
                .departmentCode("HR001")
                .departmentAddress("Building B, Floor 1")
                .build();
    }

    @Test
    @DisplayName("Should save department successfully")
    void whenSaveDepartment_thenReturnSavedDepartment() {
        // Given
        when(departmentRepository.save(any(Department.class))).thenReturn(testDepartment);

        // When
        Department savedDepartment = departmentService.saveDepartment(testDepartment);

        // Then
        assertNotNull(savedDepartment);
        assertEquals(savedDepartment.getDepartmentId(), testDepartment.getDepartmentId());
        assertEquals(savedDepartment.getDepartmentName(), testDepartment.getDepartmentName());
        assertEquals(savedDepartment.getDepartmentCode(), testDepartment.getDepartmentCode());
        assertEquals(savedDepartment.getDepartmentAddress(), testDepartment.getDepartmentAddress());
        verify(departmentRepository, times(1)).save(testDepartment);
    }


    @Test
    @DisplayName("Should return all departments when no filters provided")
    void whenNoFilters_thenReturnAllDepartments() {
        // Given
        List<Department> departments = Arrays.asList(testDepartment, anotherDepartment);
        when(departmentRepository.findAll()).thenReturn(departments);

        // When
        List<Department> result = departmentService.getDepartments(null, null, null);

        // Then
        assertEquals(departments.size(), result.size());
        verify(departmentRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return all departments when all filters are empty strings")
    void whenEmptyFilters_thenReturnAllDepartments() {
        // Given
        List<Department> departments = Arrays.asList(testDepartment, anotherDepartment);
        when(departmentRepository.findAll()).thenReturn(departments);

        // When
        List<Department> result = departmentService.getDepartments("", "", "");

        // Then
        assertEquals(departments.size(), result.size());
        verify(departmentRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should filter departments by name")
    void whenNameFilters_thenReturnAllDepartments() {
        // Given
        String departmentName = "IT";
        when(departmentRepository.findAll(any(Specification.class))).thenReturn(Collections.singletonList(testDepartment));

        // When
        List<Department> result = departmentService.getDepartments(departmentName, null, null);

        // Then
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertTrue(result.get(0).getDepartmentName().contains(departmentName));
        verify(departmentRepository, times(1)).findAll(any(Specification.class));
    }

    @Test
    @DisplayName("Should filter departments by code")
    void whenValidDepartmentCode_thenReturnFilteredDepartments() {
        // Given
        String departmentCode = "IT001";
        when(departmentRepository.findAll(any(Specification.class))).thenReturn(Collections.singletonList(testDepartment));

        // When
        List<Department> result = departmentService.getDepartments(null, departmentCode, null);

        // Then
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertTrue(result.get(0).getDepartmentCode().contains(departmentCode));
        verify(departmentRepository, times(1)).findAll(any(Specification.class));
    }

    @Test
    @DisplayName("Should filter departments by address")
    void whenValidDepartmentAddress_thenReturnFilteredDepartments() {
        // Given
        String departmentAddress = "Building A";
        when(departmentRepository.findAll(any(Specification.class))).thenReturn(Collections.singletonList(testDepartment));

        // When
        List<Department> result = departmentService.getDepartments(null, null, departmentAddress);

        // Then
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertTrue(result.get(0).getDepartmentAddress().contains(departmentAddress));
        verify(departmentRepository, times(1)).findAll(any(Specification.class));
    }

    @Test
    @DisplayName("Should filter departments by multiple criteria")
    void whenMultipleFilters_thenReturnFilteredDepartments() {
        // Given
        String name = "IT";
        String code = "IT001";
        String address = "Building A";
        when(departmentRepository.findAll(any(Specification.class)))
                .thenReturn(Collections.singletonList(testDepartment));

        // When
        List<Department> result = departmentService.getDepartments(name, code, address);

        // Then
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(departmentRepository, times(1)).findAll(any(Specification.class));
    }

    @Test
    @DisplayName("Should return empty list when no departments match filters")
    void whenNoMatchingDepartments_thenReturnEmptyList() {
        // Given
        String nonExistentName = "NONEXISTENT";
        when(departmentRepository.findAll(any(Specification.class)))
                .thenReturn(Collections.emptyList());

        // When
        List<Department> result = departmentService.getDepartments(nonExistentName, null, null);

        // Then
        assertTrue(result.isEmpty());
        verify(departmentRepository, times(1)).findAll(any(Specification.class));
    }

    // ==================== GET DEPARTMENT BY ID TESTS ====================

    @Test
    @DisplayName("Should return department when valid ID is provided")
    void whenValidId_thenReturnDepartment() throws DepartmentNotFoundException {
        // Given
        Long departmentId = 1L;
        when(departmentRepository.findById(departmentId))
                .thenReturn(Optional.of(testDepartment));

        // When
        Department found = departmentService.getDepartmentById(departmentId);

        // Then
        assertNotNull(found);
        assertEquals(testDepartment.getDepartmentId(), found.getDepartmentId());
        assertEquals(testDepartment.getDepartmentName(), found.getDepartmentName());
        verify(departmentRepository, times(1)).findById(departmentId);
    }

    @Test
    @DisplayName("Should throw DepartmentNotFoundException when department not found")
    void whenInvalidId_thenThrowException() {
        // Given
        Long invalidId = 999L;
        when(departmentRepository.findById(invalidId))
                .thenReturn(Optional.empty());

        // When & Then
        DepartmentNotFoundException exception = assertThrows(
                DepartmentNotFoundException.class,
                () -> departmentService.getDepartmentById(invalidId)
        );

        assertTrue(exception.getMessage().contains("999"));
        verify(departmentRepository, times(1)).findById(invalidId);
    }

    // ==================== DELETE DEPARTMENT TESTS ====================

    @Test
    @DisplayName("Should delete department when valid ID is provided")
    void whenValidId_thenDeleteDepartment() {
        // Given
        Long departmentId = 1L;
        when(departmentRepository.existsById(departmentId)).thenReturn(true);
        doNothing().when(departmentRepository).deleteById(departmentId);

        // When
        departmentService.deleteDepartmentById(departmentId);

        // Then
        verify(departmentRepository, times(1)).existsById(departmentId);
        verify(departmentRepository, times(1)).deleteById(departmentId);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when deleting non-existent department")
    void whenDeletingNonExistentDepartment_thenThrowException() {
        // Given
        Long invalidId = 999L;
        when(departmentRepository.existsById(invalidId)).thenReturn(false);

        // When & Then
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> departmentService.deleteDepartmentById(invalidId)
        );

        assertTrue(exception.getMessage().contains("999"));
        verify(departmentRepository, times(1)).existsById(invalidId);
        verify(departmentRepository, never()).deleteById(anyLong());
    }

    // ==================== UPDATE DEPARTMENT TESTS ====================

    @Test
    @DisplayName("Should update all department fields when all values are provided")
    void whenUpdatingAllFields_thenReturnUpdatedDepartment() {
        // Given
        Long departmentId = 1L;
        Department updateRequest = new Department();
        updateRequest.setDepartmentName("Updated IT");
        updateRequest.setDepartmentCode("IT002");
        updateRequest.setDepartmentAddress("Building C, Floor 5");

        when(departmentRepository.findById(departmentId))
                .thenReturn(Optional.of(testDepartment));
        when(departmentRepository.save(any(Department.class)))
                .thenReturn(testDepartment);

        // When
        Department updated = departmentService.updateDepartment(departmentId, updateRequest);

        // Then
        assertNotNull(updated);
        verify(departmentRepository, times(1)).findById(departmentId);
        verify(departmentRepository, times(1)).save(testDepartment);

        // Verify the department was updated
        ArgumentCaptor<Department> deptCaptor = ArgumentCaptor.forClass(Department.class);
        verify(departmentRepository).save(deptCaptor.capture());
        Department savedDept = deptCaptor.getValue();
        assertEquals("Updated IT", savedDept.getDepartmentName());
        assertEquals("IT002", savedDept.getDepartmentCode());
        assertEquals("Building C, Floor 5", savedDept.getDepartmentAddress());
    }

    @Test
    @DisplayName("Should update only name when only name is provided")
    void whenUpdatingOnlyName_thenUpdateOnlyName() {
        // Given
        Long departmentId = 1L;
        Department updateRequest = new Department();
        updateRequest.setDepartmentName("Updated IT");
        updateRequest.setDepartmentCode(null);
        updateRequest.setDepartmentAddress(null);

        when(departmentRepository.findById(departmentId))
                .thenReturn(Optional.of(testDepartment));
        when(departmentRepository.save(any(Department.class)))
                .thenReturn(testDepartment);

        // When
        departmentService.updateDepartment(departmentId, updateRequest);

        // Then
        verify(departmentRepository, times(1)).save(testDepartment);
        assertEquals("Updated IT", testDepartment.getDepartmentName());
        assertEquals("IT001", testDepartment.getDepartmentCode()); // unchanged
        assertEquals("Building A, Floor 3", testDepartment.getDepartmentAddress()); // unchanged
    }

    @Test
    @DisplayName("Should not update fields when empty strings are provided")
    void whenEmptyStringsProvided_thenDoNotUpdate() {
        // Given
        Long departmentId = 1L;
        String originalName = testDepartment.getDepartmentName();
        String originalCode = testDepartment.getDepartmentCode();
        String originalAddress = testDepartment.getDepartmentAddress();

        Department updateRequest = new Department();
        updateRequest.setDepartmentName("");
        updateRequest.setDepartmentCode("  ");
        updateRequest.setDepartmentAddress("");

        when(departmentRepository.findById(departmentId))
                .thenReturn(Optional.of(testDepartment));
        when(departmentRepository.save(any(Department.class)))
                .thenReturn(testDepartment);

        // When
        departmentService.updateDepartment(departmentId, updateRequest);

        // Then
        assertEquals(originalName, testDepartment.getDepartmentName());
        assertEquals(originalCode, testDepartment.getDepartmentCode());
        assertEquals(originalAddress, testDepartment.getDepartmentAddress());
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when updating non-existent department")
    void whenUpdatingNonExistentDepartment_thenThrowException() {
        // Given
        Long invalidId = 999L;
        Department updateRequest = new Department();
        updateRequest.setDepartmentName("Updated Name");

        when(departmentRepository.findById(invalidId))
                .thenReturn(Optional.empty());

        // When & Then
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> departmentService.updateDepartment(invalidId, updateRequest)
        );

        assertTrue(exception.getMessage().contains("999"));
        verify(departmentRepository, times(1)).findById(invalidId);
        verify(departmentRepository, never()).save(any(Department.class));
    }

    @Test
    @DisplayName("Should handle null update request gracefully")
    void whenNullFieldsInUpdateRequest_thenKeepOriginalValues() {
        // Given
        Long departmentId = 1L;
        String originalName = testDepartment.getDepartmentName();
        String originalCode = testDepartment.getDepartmentCode();
        String originalAddress = testDepartment.getDepartmentAddress();

        Department updateRequest = new Department();
        // All fields are null by default

        when(departmentRepository.findById(departmentId))
                .thenReturn(Optional.of(testDepartment));
        when(departmentRepository.save(any(Department.class)))
                .thenReturn(testDepartment);

        // When
        departmentService.updateDepartment(departmentId, updateRequest);

        // Then
        assertEquals(originalName, testDepartment.getDepartmentName());
        assertEquals(originalCode, testDepartment.getDepartmentCode());
        assertEquals(originalAddress, testDepartment.getDepartmentAddress());
        verify(departmentRepository, times(1)).save(testDepartment);
    }

}