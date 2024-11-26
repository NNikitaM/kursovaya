package ru.demo.investmentapp.repository;

import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.demo.investmentapp.model.Commission;

public class CommissionDao extends BaseDao<Commission>{
    public CommissionDao() {
        super(Commission.class);
    }

    @Override
    public void save(Commission entity) {
        Session session = getCurrentSession();
        Transaction tx1 = session.beginTransaction();

        ru.demo.investmentapp.model.Transaction transaction =
                session.get(ru.demo.investmentapp.model.Transaction.class, entity.getTransaction().getTransactionId());
        if (transaction == null) {
            throw new IllegalArgumentException("Операция не найдена с id: " + entity.getTransaction().getTransactionId());
        }

        if (entity.getCommissionId() != null) {
            Commission mergedCommission = session.merge(entity);
            mergedCommission.setTransaction(transaction);
            session.update(mergedCommission);
        } else {
            entity.setTransaction(transaction);
            session.persist(entity);
        }

        tx1.commit();
    }
}
