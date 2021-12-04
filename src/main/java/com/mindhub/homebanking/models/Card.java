package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private CardType type;
    private CardColor color;
    private String number;
    private String cvv;
    private LocalDateTime thruDate;
    private LocalDateTime fromDate;
    private String cardHolder;
    private boolean cardDelete = false;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="cardHolder_id")
    private Client client;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private Account account;

    public Card() { }

    public Card(Client client, Account account,  CardType type, CardColor color, String number, String cvv, LocalDateTime thruDate, LocalDateTime fromDate, boolean cardDelete) {
        this.client = client;
        this.type = type;
        this.color = color;
        this.number = number;
        this.cvv = cvv;
        this.thruDate = thruDate;
        this.fromDate = fromDate;
        this.cardHolder = client.getFirstName() + " " + client.getLastName();
        this.cardDelete = cardDelete;
        this.account = account;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public CardType getType() { return type; }
    public void setType(CardType type) { this.type = type; }

    public CardColor getColor() { return color; }
    public void setColor(CardColor color) { this.color = color; }

    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }

    public String getCvv() { return cvv; }
    public void setCvv(String cvv) { this.cvv = cvv; }

    public LocalDateTime getThruDate() { return thruDate; }
    public void setThruDate(LocalDateTime thruDate) { this.thruDate = thruDate; }

    public LocalDateTime getFromDate() { return fromDate; }
    public void setFromDate(LocalDateTime fromDate) { this.fromDate = fromDate; }

    public String getCardHolder() { return cardHolder; }
    public void setCardHolder(String cardHolder) { this.cardHolder = cardHolder; }

    public boolean isCardDelete() { return cardDelete; }
    public void setCardDelete(boolean cardDelete) { this.cardDelete = cardDelete; }

    public Account getAccount() {
        return account;
    }
    public void setAccount(Account account) {
        this.account = account;
    }


    @JsonIgnore
    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }

    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", type=" + type +
                ", color=" + color +
                ", number='" + number + '\'' +
                ", cvv=" + cvv +
                ", thruDate=" + thruDate +
                ", fromDate=" + fromDate +
                ", cardHolder='" + cardHolder + '\'' +
                ", client=" + client +
                '}';
    }
}
