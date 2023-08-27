package com.example.prog4gradle.controller;

import com.example.prog4gradle.model.Employee;
import com.example.prog4gradle.service.EmployeeService;
import com.itextpdf.text.Image;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;


import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.io.IOException;

@Controller
@AllArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    @GetMapping("/employees")
    public String getEmployees(@RequestParam(value = "firstName", required = false) String firstName,
                               @RequestParam(value = "lastName", required = false) String lastName,
                               @RequestParam(value = "sex", required = false) String sex,
                               @RequestParam(value = "jobTitle", required = false) String jobTitle,
                               @RequestParam(value = "hireDateFrom", required = false) LocalDate hireDateFrom,
                               @RequestParam(value = "hireDateTo", required = false) LocalDate hireDateTo,
                               @RequestParam(value = "departureDateFrom", required = false) LocalDate departureDateFrom,
                               @RequestParam(value = "departureDateTo", required = false) LocalDate departureDateTo,
                               @RequestParam(value = "sortField", required = false) String sortField,
                               @RequestParam(value = "sortOrder", required = false) String sortOrder,
                               Model model) {
        List<Employee> employeeList = employeeService.getFilteredEmployees(firstName, lastName, sex, jobTitle, hireDateFrom, hireDateTo, departureDateFrom, departureDateTo, sortField, sortOrder);
        model.addAttribute("employees", employeeList);
        return "employee-list";
    }

    @GetMapping("/add-employee")
    public String showAddEmployeeForm(Model model) {
        model.addAttribute("employee", new Employee());
        return "add-employee"; // Nom du template Thymeleaf pour afficher le formulaire d'ajout d'employé
    }

    @PostMapping("/add-employee")
    public String addEmployee(@ModelAttribute("employee") Employee employeeModel) {
        employeeService.addEmployee(employeeModel);
        return "redirect:/employees";
    }

    @GetMapping("/employee/{id}")
    public String getEmployeeDetails(@PathVariable("id") Long id, Model model) {
        Employee employee = employeeService.getEmployeeById(id);
        model.addAttribute("employee", employee);
        return "employee-details";
    }

    @GetMapping("/export-employees")
    public void exportEmployeesCSV(@RequestParam(value = "firstName", required = false) String firstName,
                                   @RequestParam(value = "lastName", required = false) String lastName,
                                   @RequestParam(value = "sex", required = false) String sex,
                                   @RequestParam(value = "jobTitle", required = false) String jobTitle,
                                   @RequestParam(value = "hireDateFrom", required = false) LocalDate hireDateFrom,
                                   @RequestParam(value = "hireDateTo", required = false) LocalDate hireDateTo,
                                   @RequestParam(value = "departureDateFrom", required = false) LocalDate departureDateFrom,
                                   @RequestParam(value = "departureDateTo", required = false) LocalDate departureDateTo,
                                   @RequestParam(value = "sortField", required = false) String sortField,
                                   @RequestParam(value = "sortOrder", required = false) String sortOrder,
                                   HttpServletResponse response) throws IOException {
        List<Employee> employeeList = employeeService.getFilteredEmployees(firstName, lastName, sex, jobTitle, hireDateFrom, hireDateTo, departureDateFrom, departureDateTo, sortField, sortOrder);

        // Set the content type and header for CSV file download
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"employees.csv\"");

        // Create a CSV writer using the response's output stream
        CSVPrinter csvPrinter = new CSVPrinter(response.getWriter(), CSVFormat.DEFAULT
                .withHeader("First Name", "Last Name", "Date of Birth", "Sex", "Phones", "Address", "Personal Email",
                        "Professional Email", "CIN Number", "CIN Delivery Date", "CIN Delivery Place", "Job Title",
                        "Number of Children", "Hire Date", "Departure Date", "Socio-Professional Category", "CNAPS Number"));

        // Write employee data to CSV
        for (Employee employee : employeeList) {
            csvPrinter.printRecord(
                    employee.getFirstName(),
                    employee.getLastName(),
                    employee.getDateOfBirth(),
                    employee.getSex(),
                    employee.getPhones(),
                    employee.getAddress(),
                    employee.getPersonalEmail(),
                    employee.getProfessionalEmail(),
                    employee.getCinNumber(),
                    employee.getCinDeliveryDate(),
                    employee.getCinDeliveryPlace(),
                    employee.getJobTitle(),
                    employee.getNumberOfChildren(),
                    employee.getHireDate(),
                    employee.getDepartureDate(),
                    employee.getSocioProfessionalCategory(),
                    employee.getCnapsNumber()
            );
        }

        // Flush and close the CSV printer
        csvPrinter.flush();
        csvPrinter.close();
    }
    @GetMapping("/generate-pdf/{id}")
    public void generatePdf(@PathVariable("id") Long id, HttpServletResponse response) throws IOException, DocumentException {
        Employee employee = employeeService.getEmployeeById(id);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"employee-details.pdf\"");

        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        document.add(new Paragraph("Fiche Employé"));
        if (employee.getImage() != null && !employee.getImage().isEmpty()) {
            byte[] imageBytes = Base64.getDecoder().decode(employee.getImage());
            Image image = Image.getInstance(imageBytes);
            image.scaleToFit(100, 100); // Ajuste la taille de l'image selon tes besoins
            document.add(image);
        }
        document.add(new Paragraph("Nom : " + employee.getFirstName() + " " + employee.getLastName()));
        document.add(new Paragraph("Age : " + employee.getDateOfBirth()));

        document.close();
    }

    // Autres méthodes du contrôleur pour d'autres opérations liées aux employés
}