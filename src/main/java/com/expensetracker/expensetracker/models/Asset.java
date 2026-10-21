package com.expensetracker.expensetracker.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Table(name = "assets")
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    
    @NotNull(message = "Name is required")
    @Size(max = 255, message = "Name cannot exceed 255 characters")
    private String name;

    @NotNull(message = "Unit  is required")
    @Size(max = 11, message = "Unit cannot exceed 11 characters")
    private String unit;
    
    @NotNull(message = "Price  is required")
    @Size(max = 11, message = "Price cannot exceed 11 characters")
    private String prce;
    
    @NotNull(message = "Quantity  is required")
    @Size(max = 5, message = "Unit cannot exceed 5 characters")
    private String qtyy;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Date is required")
    private LocalDate date;

  


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
    

    public String getPrce() {
        return prce;
    }

    public void setPrce(String prce) {
        this.prce = prce;
    }
    

    public String getQtyy() {
        return qtyy;
    }

    public void setQtyy(String qtyy) {
        this.qtyy = qtyy;
    }
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    
  

}
