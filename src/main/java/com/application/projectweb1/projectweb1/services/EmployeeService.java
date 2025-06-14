package com.application.projectweb1.projectweb1.services;

import com.application.projectweb1.projectweb1.dto.EmployeeDTO;
import com.application.projectweb1.projectweb1.entities.EmployeeEntity;
import com.application.projectweb1.projectweb1.repositories.EmployeeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

    public EmployeeService(EmployeeRepository employeeRepository, ModelMapper modelMapper) {
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
    }



    public EmployeeDTO getEmployeeById(Long id) {
        EmployeeEntity employeeEntity = employeeRepository.findById(id).orElse(null);
        return modelMapper.map(employeeEntity, EmployeeDTO.class);
    }

    public List<EmployeeDTO> getAllEmployee() {
        List<EmployeeEntity> employeeEntities = employeeRepository.findAll();
        return employeeEntities
                .stream()
                .map(employeeEntity -> modelMapper.map(employeeEntity, EmployeeDTO.class))
                .collect(Collectors.toList());
    }

    public EmployeeDTO createNewEmployee(EmployeeDTO inputEmployee) {
        EmployeeEntity tosaveEntity = modelMapper.map(inputEmployee,EmployeeEntity.class);
        EmployeeEntity saveEmployeeEntity = employeeRepository.save(tosaveEntity);
        return modelMapper.map(saveEmployeeEntity,EmployeeDTO.class);
    }

    /*public EmployeeDTO updateEmployeeById(Long employeeId, EmployeeDTO employeeDTO) {
        EmployeeEntity employeeEntity = modelMapper.map(employeeDTO, EmployeeEntity.class);
        employeeEntity.setId(employeeId);
        EmployeeEntity saveEmployeeEntity = employeeRepository.save(employeeEntity);
        return modelMapper.map(saveEmployeeEntity, EmployeeDTO.class);

        this code is also fine after running few time it will through StaleObjectStateException
    }*/

    public EmployeeDTO updateEmployeeById(Long employeeId, EmployeeDTO employeeDTO) {
        // 1. Fetch existing entity from DB
        EmployeeEntity existingEntity = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        // 2. Update only the fields
        existingEntity.setName(employeeDTO.getName());
        existingEntity.setEmail(employeeDTO.getEmail());
        existingEntity.setAge(employeeDTO.getAge());
        existingEntity.setDateofjoining(employeeDTO.getDateofjoining());
        existingEntity.setIsActive(employeeDTO.getIsActive());

        // 3. Save updated entity
        EmployeeEntity savedEntity = employeeRepository.save(existingEntity);

        return modelMapper.map(savedEntity, EmployeeDTO.class);
    }


    public boolean isExistbyEmployeeId(Long employeeId){
        return employeeRepository.existsById(employeeId);
    }

    public boolean deleteEmployeeById(Long employeeId) {
        boolean exist = isExistbyEmployeeId(employeeId);
        if(!exist) return false;
        employeeRepository.deleteById(employeeId);
        return true;
    }

   /* public EmployeeDTO updatePartialEmployeeById(Long employeeId, Map<String, Object> updates) {
        boolean exist = isExistbyEmployeeId(employeeId);
        if(!exist) return null;
        EmployeeEntity employeeEntity =  employeeRepository.findById(employeeId).get();
        updates.forEach((field, value) -> {
            Field fieldToBeUpdated = ReflectionUtils.findRequiredField(EmployeeEntity.class, field);
            fieldToBeUpdated.setAccessible(true);
            ReflectionUtils.setField(fieldToBeUpdated, employeeEntity, value);
        });
        return modelMapper.map(employeeRepository.save(employeeEntity), EmployeeDTO.class);

        this is also working but it will not able to handle date field to update
    }*/

    public EmployeeDTO updatePartialEmployeeById(Long employeeId, Map<String, Object> updates) {
        boolean exists = isExistbyEmployeeId(employeeId);
        if (!exists) return null;

        EmployeeEntity employeeEntity = employeeRepository.findById(employeeId).get();

        updates.forEach((fieldName, value) -> {
            Field field = ReflectionUtils.findRequiredField(EmployeeEntity.class, fieldName);
            if (field != null) {
                field.setAccessible(true);

                // Convert String to LocalDate if needed
                if (field.getType().equals(LocalDate.class) && value instanceof String) {
                    value = LocalDate.parse((String) value); // Use DateTimeFormatter if custom format
                }

                // Add other type conversions if needed, e.g., enums or nested objects

                ReflectionUtils.setField(field, employeeEntity, value);
            }
        });

        EmployeeEntity savedEntity = employeeRepository.save(employeeEntity);
        return modelMapper.map(savedEntity, EmployeeDTO.class);
    }

}
