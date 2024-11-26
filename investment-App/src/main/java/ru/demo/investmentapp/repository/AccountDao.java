package ru.demo.investmentapp.repository;

import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.demo.investmentapp.model.Account;
import ru.demo.investmentapp.model.Client;

public class AccountDao extends BaseDao<Account> {
    public AccountDao() {
        super(Account.class);
    }

    @Override
    public void save(Account entity) {
        Session session = getCurrentSession();
        Transaction tx1 = session.beginTransaction();

        Client client = session.get(Client.class, entity.getClient().getClientId());
        if (client == null) {
            throw new IllegalArgumentException("Клиент не найден с логином: " + entity.getClient().getClientId());
        }

        if (entity.getAccountId() != null) {
            Account mergedAccount = session.merge(entity);
            mergedAccount.setClient(client);
            session.update(mergedAccount);
        } else {
            entity.setClient(client);
            session.persist(entity);
        }

        tx1.commit();
    }
}
