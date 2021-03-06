package com.kiri.hackjak.db;

import com.kiri.hackjak.db.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table TAXI_PHONE.
 */
public class TaxiPhone {

    private Long id;
    /** Not-null value. */
    private String phone;
    private long idTaxi;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient TaxiPhoneDao myDao;

    private Taxi taxi;
    private Long taxi__resolvedKey;


    public TaxiPhone() {
    }

    public TaxiPhone(Long id) {
        this.id = id;
    }

    public TaxiPhone(Long id, String phone, long idTaxi) {
        this.id = id;
        this.phone = phone;
        this.idTaxi = idTaxi;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getTaxiPhoneDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /** Not-null value. */
    public String getPhone() {
        return phone;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public long getIdTaxi() {
        return idTaxi;
    }

    public void setIdTaxi(long idTaxi) {
        this.idTaxi = idTaxi;
    }

    /** To-one relationship, resolved on first access. */
    public Taxi getTaxi() {
        long __key = this.idTaxi;
        if (taxi__resolvedKey == null || !taxi__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            TaxiDao targetDao = daoSession.getTaxiDao();
            Taxi taxiNew = targetDao.load(__key);
            synchronized (this) {
                taxi = taxiNew;
            	taxi__resolvedKey = __key;
            }
        }
        return taxi;
    }

    public void setTaxi(Taxi taxi) {
        if (taxi == null) {
            throw new DaoException("To-one property 'idTaxi' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.taxi = taxi;
            idTaxi = taxi.getId();
            taxi__resolvedKey = idTaxi;
        }
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

}
