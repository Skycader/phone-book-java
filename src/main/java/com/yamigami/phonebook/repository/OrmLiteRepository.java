package com.yamigami.phonebook.repository;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.yamigami.phonebook.model.Contact;

import java.util.List;

public class OrmLiteRepository implements ContactRepository {
    private final Dao<Contact, Long> contactDao;

    public OrmLiteRepository(String dbPath) {
        try {
            // Создаём подключение к SQLite
            String url = "jdbc:sqlite:" + dbPath;
            ConnectionSource connectionSource = new JdbcConnectionSource(url);

            // Получаем DAO для Contact
            contactDao = DaoManager.createDao(connectionSource, Contact.class);

            // Создаём таблицу, если не существует
            TableUtils.createTableIfNotExists(connectionSource, Contact.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize database", e);
        }
    }

    @Override
    public void save(Contact contact) {
        try {
            contactDao.createOrUpdate(contact);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save contact", e);
        }
    }

    @Override
    public List<Contact> findAll(int limit, int offset) {
        try {
            return contactDao.queryBuilder()
                    .limit((long) limit)
                    .offset((long) offset)
                    .query();
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch contacts", e);
        }
    }

    @Override
    public List<Contact> findByPhone(String phonePart) {
        try {
            return contactDao.queryBuilder()
                    .where()
                    .like("phone", "%" + phonePart + "%")
                    .query();
        } catch (Exception e) {
            throw new RuntimeException("Failed to search by phone", e);
        }
    }

    @Override
    public List<Contact> findByName(String namePart) {
        try {
            return contactDao.queryBuilder()
                    .where()
                    .like("name", "%" + namePart + "%")
                    .query();
        } catch (Exception e) {
            throw new RuntimeException("Failed to search by name", e);
        }
    }

    @Override
    public boolean deleteByPhone(String phone) {
        try {
            Contact contact = findByPhoneExact(phone);
            if (contact != null) {
                contactDao.delete(contact);
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete contact", e);
        }
    }

    @Override
    public Contact findByPhoneExact(String phone) {
        try {
            return contactDao.queryBuilder()
                    .where()
                    .eq("phone", phone)
                    .queryForFirst();
        } catch (Exception e) {
            throw new RuntimeException("Failed to find contact", e);
        }
    }

    @Override
    public long count() {
        try {
            return contactDao.countOf();
        } catch (Exception e) {
            throw new RuntimeException("Failed to count contacts", e);
        }
    }
}