package ru.demo.investmentapp.repository;

import ru.demo.investmentapp.model.Client;

public class ClientDao extends BaseDao<Client>{
    public ClientDao() {
        super(Client.class);
    }

}
