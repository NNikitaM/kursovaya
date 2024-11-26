package ru.demo.investmentapp.repository;

import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.demo.investmentapp.model.CompanyInfo;
import ru.demo.investmentapp.model.Instrument;

public class CompanyInfoDao extends BaseDao<CompanyInfo>{
    public CompanyInfoDao() {
        super(CompanyInfo.class);
    }


    @Override
    public void save(CompanyInfo entity) {
        Session session = getCurrentSession();
        Transaction tx1 = session.beginTransaction();

       Instrument instrument = session.get(Instrument.class, entity.getInstrument().getInstrumentId());
        if (instrument == null) {
            throw new IllegalArgumentException("Инструмент не найден с id: " + entity.getInstrument().getInstrumentId());
        }

        if (entity.getInfoId() != null) {
            CompanyInfo mergedCompanyInfo = session.merge(entity);
            mergedCompanyInfo.setInstrument(instrument);
            session.update(mergedCompanyInfo);
        } else {
            entity.setInstrument(instrument);
            session.persist(entity);
        }

        tx1.commit();
    }
}
