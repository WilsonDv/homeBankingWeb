package com.mindhub.homebanking.services;


import com.mindhub.homebanking.models.Client;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface PdfService {
    public void export(HttpServletResponse response, Client client1, Client client2, String numberAccount1, String numberAccount2,
                       double amount, String description) throws IOException;
}
