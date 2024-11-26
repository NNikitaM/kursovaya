package ru.demo.investmentapp.service;

import ru.demo.investmentapp.model.Client;
import ru.demo.investmentapp.repository.ClientDao;

import java.util.List;

public class ClientService {
    private ClientDao clientDao = new ClientDao();

    public ClientService() {
    }

    public List<Client> findAll() {
        return clientDao.findAll();
    }

    public Client findOne(final long id) {
        return clientDao.findOne(id);
    }

    public void save(final Client entity) {
        if (entity == null)
            return;
        clientDao.save(entity);
    }

    public void update(final Client entity) {
        if (entity == null)
            return;
        clientDao.update(entity);
    }

    public void delete(final Client entity) {
        if (entity == null)
            return;
        clientDao.delete(entity);
    }

    public void deleteById(final Long id) {
        if (id == null)
            return;
        clientDao.deleteById(id);
    }
}
