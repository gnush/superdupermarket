package io.github.gnush.datasource.db;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.TransactionException;

import java.util.List;
import java.util.Optional;

public class CommodityCategoryDao {
    public void saveOrUpdateId(CommodityCategory category) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            var result = session.createQuery(
                    "FROM CommodityCategory c where c.name='"+category.getName()+"'",
                    CommodityCategory.class
            ).uniqueResult();

            if (result != null) {
                category.setId(result.getId());
            } else {
                transaction = session.beginTransaction();
                session.persist(category);
                transaction.commit();
            }
        } catch (TransactionException e) {
            if (transaction != null)
                transaction.rollback();
        } catch (Exception e) {
            System.out.println("Couldn't save category '" + category.getName() + "': " + e.getMessage());
        }
    }

    public List<CommodityCategory> categories() {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM CommodityCategory", CommodityCategory.class).list();
        }
    }

    public Optional<CommodityCategory> category(String name) {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM CommodityCategory c where name='"+name+"'",
                    CommodityCategory.class
            ).uniqueResultOptional();
        }
    }
}
