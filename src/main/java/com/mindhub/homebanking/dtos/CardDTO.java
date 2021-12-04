package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;

import java.time.LocalDateTime;

public class CardDTO {
    private Long id;
    private String cardHolder;
    private CardType type;
    private CardColor color;
    private String number;
    private String cvv;
    private LocalDateTime thruDate;
    private LocalDateTime fromDate;
    private boolean cardDelete = false;
    private Account account;

    public CardDTO() { }

    public CardDTO(Card card) {
        this.id = card.getId();
        this.type = card.getType();
        this.color = card.getColor();
        this.number = card.getNumber();
        this.cvv = card.getCvv();
        this.thruDate = card.getThruDate();
        this.fromDate = card.getFromDate();
        this.cardHolder = card.getCardHolder();
        this.cardDelete = card.isCardDelete();
        this.account = card.getAccount();
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

    @Override
    public String toString() {
        return "CardDTO{" +
                "id=" + id +
                ", cardHolder='" + cardHolder + '\'' +
                ", type=" + type +
                ", color=" + color +
                ", number='" + number + '\'' +
                ", cvv=" + cvv +
                ", thruDate=" + thruDate +
                ", fromDate=" + fromDate +
                '}';
    }
}
