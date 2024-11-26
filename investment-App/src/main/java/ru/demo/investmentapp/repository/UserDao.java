package ru.demo.investmentapp.repository;

import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.demo.investmentapp.model.Client;
import ru.demo.investmentapp.model.Role;
import ru.demo.investmentapp.model.User;

public class UserDao extends BaseDao<User>{
    public UserDao() {
        super(User.class);
    }

    @Override
    public void save(User entity) {
        Session session = getCurrentSession();
        Transaction tx1 = session.beginTransaction();
        User existingUser = session.get(User.class, entity.getUserName());
        if(existingUser != null) {
            throw new IllegalStateException("Пользователь с этим именем уже существует в базе данных.");
        }

        Client client = session.get(Client.class, entity.getClient().getClientId());
        if (client == null) {
            throw new IllegalArgumentException("Клиент не найден с логином: " + entity.getClient().getClientId());
        }

        Role role = session.get(Role.class, entity.getRole().getRoleId());
        if (role == null) {
            throw new IllegalArgumentException("Роль не найдена с id: " + entity.getRole().getRoleId());
        }

        if (entity.getUserName() != null) {
            User mergedUser = session.merge(entity);
            mergedUser.setClient(client);
            mergedUser.setRole(role);
            session.update(mergedUser);
        } else {
            entity.setClient(client);
            entity.setRole(role);
            session.persist(entity);
        }

        tx1.commit();
    }
}
