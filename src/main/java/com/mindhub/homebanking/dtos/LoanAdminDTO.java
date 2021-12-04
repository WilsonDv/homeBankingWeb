package com.mindhub.homebanking.dtos;


import java.util.ArrayList;
import java.util.List;

public class LoanAdminDTO {

    private String name;
    private double maxAmount;
    private double percentage;
    private List<Integer> payments = new ArrayList<>();

    public LoanAdminDTO() { }

    public LoanAdminDTO(String name, double maxAmount, double percentage, List<Integer> payments) {
        this.name = name;
        this.maxAmount = maxAmount;
        this.percentage = percentage;
        this.payments = payments;
    }

    public String getName() { return name;}
    public void setName(String name) { this.name = name; }

    public double getMaxAmount() { return maxAmount; }
    public void setMaxAmount(double maxAmount) { this.maxAmount = maxAmount; }

    public double getPercentage() { return percentage; }
    public void setPercentage(double percentage) { this.percentage = percentage; }

    public List<Integer> getPayments() {
        return payments;
    }

    public void setPayments(List<Integer> payments) {
        this.payments = payments;
    }
}
