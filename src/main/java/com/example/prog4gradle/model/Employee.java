package com.example.prog4gradle.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String image;
    private String sex; // H/F
    @ElementCollection
    private List<String> phones; // Liste de téléphones
    private String address; // Adresse exacte
    private String personalEmail; // Email perso
    private String professionalEmail; // Email pro
    private String cinNumber; // Numéro CIN
    private LocalDate cinDeliveryDate; // Date de délivrance du CIN
    private String cinDeliveryPlace; // Lieu de délivrance du CIN
    private String jobTitle; // Fonction au sein de l'entreprise
    private int numberOfChildren; // Nombre d'enfants à charge
    private LocalDate hireDate; // Date d'embauche
    private LocalDate departureDate; // Date de départ
    private String socioProfessionalCategory; // Catégorie socio-professionnelle
    private String cnapsNumber; // Numéro CNAPS (alphanumérique)

    @Transient
    private MultipartFile photo;
}