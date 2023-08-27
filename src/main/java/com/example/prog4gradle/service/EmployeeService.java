package com.example.prog4gradle.service;

import com.example.prog4gradle.model.Employee;
import com.example.prog4gradle.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id).orElse(null);
    }

    public Employee addEmployee(Employee employeeModel) {
        Employee employee = new Employee();
        // Copie des attributs de l'employé depuis le modèle
        employee.setDateOfBirth(employeeModel.getDateOfBirth());
        employee.setFirstName(employeeModel.getFirstName());
        employee.setLastName(employeeModel.getLastName());
        employee.setSex(employeeModel.getSex());
        employee.setPhones(employeeModel.getPhones());
        employee.setAddress(employeeModel.getAddress());
        employee.setPersonalEmail(employeeModel.getPersonalEmail());
        employee.setProfessionalEmail(employeeModel.getProfessionalEmail());
        employee.setCinNumber(employeeModel.getCinNumber());
        employee.setCinDeliveryDate(employeeModel.getCinDeliveryDate());
        employee.setCinDeliveryPlace(employeeModel.getCinDeliveryPlace());
        employee.setJobTitle(employeeModel.getJobTitle());
        employee.setNumberOfChildren(employeeModel.getNumberOfChildren());
        employee.setHireDate(employeeModel.getHireDate());
        employee.setDepartureDate(employeeModel.getDepartureDate());
        employee.setSocioProfessionalCategory(employeeModel.getSocioProfessionalCategory());
        employee.setCnapsNumber(employeeModel.getCnapsNumber());

        MultipartFile photo = employeeModel.getPhoto();
        if (photo != null && !photo.isEmpty()) {
            try {
                byte[] imageBytes = photo.getBytes();
                String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                employee.setImage(base64Image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return employeeRepository.save(employee);
    }
    public List<Employee> getFilteredEmployees(String firstName, String lastName, String sex, String jobTitle,
                                               LocalDate hireDateFrom, LocalDate hireDateTo,
                                               LocalDate departureDateFrom, LocalDate departureDateTo,
                                               String sortField, String sortOrder) {
        // Vérifier les paramètres et définir leur valeur à null s'ils sont vides
        firstName = (firstName != null && !firstName.isEmpty()) ? firstName : null;
        lastName = (lastName != null && !lastName.isEmpty()) ? lastName : null;
        sex = (sex != null && !sex.isEmpty()) ? sex : null;
        jobTitle = (jobTitle != null && !jobTitle.isEmpty()) ? jobTitle : null;

        List<Employee> employees = employeeRepository.findByFilters(firstName, lastName, sex, jobTitle, hireDateFrom, hireDateTo, departureDateFrom, departureDateTo);

        // Trier la liste si les paramètres de tri sont spécifiés
        if (sortField != null && !sortField.isEmpty()) {
            switch (sortField) {
                case "firstName":
                    employees.sort(Comparator.comparing(Employee::getFirstName));
                    break;
                case "lastName":
                    employees.sort(Comparator.comparing(Employee::getLastName));
                    break;
                // Ajoutez d'autres cas pour d'autres champs si nécessaire
                default:
                    // Aucun tri valide spécifié, ne rien faire
            }

            // Inverser l'ordre si l'ordre de tri est "desc"
            if ("desc".equalsIgnoreCase(sortOrder)) {
                Collections.reverse(employees);
            }
        }

        return employees;
    // Autres méthodes pour effectuer des opérations sur les employés en utilisant le repository
}
}