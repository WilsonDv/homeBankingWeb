package com.mindhub.homebanking.models;

import javax.persistence.Column;
import javax.persistence.ElementCollection;

public class Payments {

    @ElementCollection
    @Column(name="payment_id")
    private Loan loan;

    public Payments() { }

    public Payments( Loan loan) {

        this.loan = loan;
    }

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }

    @Override
    public String toString() {
        return "Payments{" +
                ", loan=" + loan +
                '}';
    }
}
