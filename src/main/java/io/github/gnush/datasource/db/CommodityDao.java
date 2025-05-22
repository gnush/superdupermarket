package io.github.gnush.datasource.db;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.TransactionException;

import java.util.Collections;
import java.util.List;

public class CommodityDao {
    private final CommodityCategoryDao categoryDao = new CommodityCategoryDao();

    public void save(CommodityEntity commodity) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            categoryDao.saveOrUpdateId(commodity.getCategory());

            transaction = session.beginTransaction();
            session.persist(commodity);
            transaction.commit();
        } catch (TransactionException e) {
            if (transaction != null)
                transaction.rollback();
        } catch (Exception e) {
            System.out.println("Couldn't save commodity'" + commodity.getLabel() + "': " + e.getMessage());
        }
    }

    public List<CommodityEntity> commodities() {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM CommodityEntity", CommodityEntity.class).list();
        }
    }

    public List<CommodityEntity> commodities(String category) {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            var maybeCategory = categoryDao.category(category);

            if (maybeCategory.isPresent()) {
                return session.createQuery(
                        "FROM CommodityEntity c where c.category.id="+maybeCategory.get().getId(),
                        CommodityEntity.class
                ).list();
            } else {
                return Collections.emptyList();
            }
        }
    }

    public void deleteAll() {
        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.createMutationQuery("DELETE FROM CommodityEntity").executeUpdate();
            transaction.commit();
        } catch (TransactionException e) {
            if (transaction != null) {
                System.out.println("Error during commodity deletion, rolling back transaction: " + e.getMessage());
                transaction.rollback();
            }
        } catch (Exception e) {
            System.out.println("Error during commodity deletion: " + e.getMessage());
        }
    }
}
