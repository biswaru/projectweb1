package com.application.projectweb1.projectweb1.services;

import com.application.projectweb1.projectweb1.dto.EmployeeDTO;
import com.application.projectweb1.projectweb1.entities.EmployeeEntity;
import com.application.projectweb1.projectweb1.repositories.EmployeeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public EmployeeDTO updateEmployeeById(Long employeeId, EmployeeDTO employeeDTO) {
        EmployeeEntity employeeEntity = modelMapper.map(employeeDTO, EmployeeEntity.class);
        employeeEntity.setId(employeeId);
        EmployeeEntity saveEmployeeEntity = employeeRepository.save(employeeEntity);
        return modelMapper.map(saveEmployeeEntity, EmployeeDTO.class);
    }

    public boolean deleteEmployeeById(Long employeeId) {
        boolean exist = employeeRepository.existsById(employeeId);
        if(!exist) return false;
        employeeRepository.deleteById(employeeId);
        return true;
    }
}
