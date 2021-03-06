package com.kiri.hackjak.db;

import com.kiri.hackjak.db.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table TRAYEK_ROUTE_DETAIL.
 */
public class TrayekRouteDetail {

    private Long id;
    private long idWaypoint;
    private long idRuteTrayek;
    private int urut;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient TrayekRouteDetailDao myDao;

    private TrayekRoute trayekRoute;
    private Long trayekRoute__resolvedKey;

    private TrayekWaypoint trayekWaypoint;
    private Long trayekWaypoint__resolvedKey;


    public TrayekRouteDetail() {
    }

    public TrayekRouteDetail(Long id) {
        this.id = id;
    }

    public TrayekRouteDetail(Long id, long idWaypoint, long idRuteTrayek, int urut) {
        this.id = id;
        this.idWaypoint = idWaypoint;
        this.idRuteTrayek = idRuteTrayek;
        this.urut = urut;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getTrayekRouteDetailDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getIdWaypoint() {
        return idWaypoint;
    }

    public void setIdWaypoint(long idWaypoint) {
        this.idWaypoint = idWaypoint;
    }

    public long getIdRuteTrayek() {
        return idRuteTrayek;
    }

    public void setIdRuteTrayek(long idRuteTrayek) {
        this.idRuteTrayek = idRuteTrayek;
    }

    public int getUrut() {
        return urut;
    }

    public void setUrut(int urut) {
        this.urut = urut;
    }

    /** To-one relationship, resolved on first access. */
    public TrayekRoute getTrayekRoute() {
        long __key = this.idRuteTrayek;
        if (trayekRoute__resolvedKey == null || !trayekRoute__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            TrayekRouteDao targetDao = daoSession.getTrayekRouteDao();
            TrayekRoute trayekRouteNew = targetDao.load(__key);
            synchronized (this) {
                trayekRoute = trayekRouteNew;
            	trayekRoute__resolvedKey = __key;
            }
        }
        return trayekRoute;
    }

    public void setTrayekRoute(TrayekRoute trayekRoute) {
        if (trayekRoute == null) {
            throw new DaoException("To-one property 'idRuteTrayek' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.trayekRoute = trayekRoute;
            idRuteTrayek = trayekRoute.getId();
            trayekRoute__resolvedKey = idRuteTrayek;
        }
    }

    /** To-one relationship, resolved on first access. */
    public TrayekWaypoint getTrayekWaypoint() {
        long __key = this.idWaypoint;
        if (trayekWaypoint__resolvedKey == null || !trayekWaypoint__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            TrayekWaypointDao targetDao = daoSession.getTrayekWaypointDao();
            TrayekWaypoint trayekWaypointNew = targetDao.load(__key);
            synchronized (this) {
                trayekWaypoint = trayekWaypointNew;
            	trayekWaypoint__resolvedKey = __key;
            }
        }
        return trayekWaypoint;
    }

    public void setTrayekWaypoint(TrayekWaypoint trayekWaypoint) {
        if (trayekWaypoint == null) {
            throw new DaoException("To-one property 'idWaypoint' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.trayekWaypoint = trayekWaypoint;
            idWaypoint = trayekWaypoint.getId();
            trayekWaypoint__resolvedKey = idWaypoint;
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
