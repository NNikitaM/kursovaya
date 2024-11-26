package ru.demo.investmentapp.repository;

import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.demo.investmentapp.model.Portfolio;
import ru.demo.investmentapp.model.User;

public class PortfolioDao extends BaseDao<Portfolio>{
    public PortfolioDao() {
        super(Portfolio.class);
    }

    @Override
    public void save(Portfolio entity) {
        Session session = getCurrentSession();
        Transaction tx1 = session.beginTransaction();

        User user = session.get(User.class, entity.getUser().getUserName());
        if (user == null) {
            throw new IllegalArgumentException("Пользователь не найден с логином: " + entity.getUser().getUserName());
        }

        if (entity.getPortfolioId() != null) {
            Portfolio mergedPortfolio = session.merge(entity);
            mergedPortfolio.setUser(user);
            session.update(mergedPortfolio);
        } else {
            entity.setUser(user);
            session.persist(entity);
        }

        tx1.commit();
    }
}
