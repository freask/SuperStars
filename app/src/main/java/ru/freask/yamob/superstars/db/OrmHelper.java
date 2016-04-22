package ru.freask.yamob.superstars.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.SparseArray;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

import ru.freask.yamob.superstars.Constants;
import ru.freask.yamob.superstars.models.Star;

public class OrmHelper extends OrmLiteSqliteOpenHelper {
    public static final String TAG = OrmHelper.class.getSimpleName();
    private static final int RECIPE_DAO_NUM = 1;
    private Class[] classes = {
            Star.class
    };
    private SparseArray<CommonDao> daos;
    public OrmHelper(Context context) {
        super(context, getDBName(), null, Constants.DATABASE_VERSION);
        daos = new SparseArray<>();
    }
    public static String getDBName() {
        return Constants.DATABASE_NAME;
    }
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            createAllTables(connectionSource);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void createAllTables(ConnectionSource connectionSource) throws SQLException {
        for (Class className : classes) {
            TableUtils.createTable(connectionSource, className);
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            dropAllTables(connectionSource);
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private void dropAllTables(ConnectionSource connectionSource) throws SQLException {
        for (Class className : classes) {
            TableUtils.dropTable(connectionSource, className, true);
        }
    }
    public void clearDatabase() {
        try {
            dropAllTables(connectionSource);
            createAllTables(connectionSource);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public CommonDao getDaoByClass(Class<?> classInstance) throws SQLException {
        if (classInstance.equals(Star.class)) {
            return getCustomDaoByNum(StarDao.class, RECIPE_DAO_NUM);
        }
        return null;
    }
    private CommonDao getCustomDaoByNum(Class<? extends CommonDao> className, int daoNum) throws SQLException {
        CommonDao dao = daos.get(daoNum);
        if (dao == null) {
            try {
                dao = className.getDeclaredConstructor(ConnectionSource.class).newInstance(getConnectionSource());
                daos.put(daoNum, dao);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return dao;
    }
    private CommonDao getDaoByNum(Class<?> className, int daoNum) throws SQLException {
        CommonDao dao = daos.get(daoNum);
        if (dao == null) {
            dao = new CommonDao(getConnectionSource(), className);
            daos.put(daoNum, dao);
        }
        return dao;
    }
    @Override
    public void close() {
        super.close();
        daos = null;
    }
}
