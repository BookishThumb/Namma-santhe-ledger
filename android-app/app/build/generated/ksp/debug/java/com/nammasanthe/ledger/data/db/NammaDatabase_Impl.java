package com.nammasanthe.ledger.data.db;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.nammasanthe.ledger.data.db.dao.CustomerDao;
import com.nammasanthe.ledger.data.db.dao.CustomerDao_Impl;
import com.nammasanthe.ledger.data.db.dao.TransactionDao;
import com.nammasanthe.ledger.data.db.dao.TransactionDao_Impl;
import com.nammasanthe.ledger.data.db.dao.VendorDao;
import com.nammasanthe.ledger.data.db.dao.VendorDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class NammaDatabase_Impl extends NammaDatabase {
  private volatile VendorDao _vendorDao;

  private volatile CustomerDao _customerDao;

  private volatile TransactionDao _transactionDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(2) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `vendors` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `pin` TEXT NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `customers` (`id` TEXT NOT NULL, `vendor_id` TEXT NOT NULL, `name` TEXT NOT NULL, `customer_code` TEXT NOT NULL, `pin` TEXT NOT NULL, `phone` TEXT NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_customers_customer_code` ON `customers` (`customer_code`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `transactions` (`id` TEXT NOT NULL, `customer_id` TEXT NOT NULL, `vendor_id` TEXT NOT NULL, `amount` REAL NOT NULL, `type` TEXT NOT NULL, `date` TEXT NOT NULL, `note` TEXT, PRIMARY KEY(`id`))");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_transactions_customer_id` ON `transactions` (`customer_id`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_transactions_vendor_id` ON `transactions` (`vendor_id`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'c58c6b49ef4b9296e7a16fe912056f5b')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `vendors`");
        db.execSQL("DROP TABLE IF EXISTS `customers`");
        db.execSQL("DROP TABLE IF EXISTS `transactions`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsVendors = new HashMap<String, TableInfo.Column>(3);
        _columnsVendors.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVendors.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVendors.put("pin", new TableInfo.Column("pin", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysVendors = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesVendors = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoVendors = new TableInfo("vendors", _columnsVendors, _foreignKeysVendors, _indicesVendors);
        final TableInfo _existingVendors = TableInfo.read(db, "vendors");
        if (!_infoVendors.equals(_existingVendors)) {
          return new RoomOpenHelper.ValidationResult(false, "vendors(com.nammasanthe.ledger.data.db.entities.VendorEntity).\n"
                  + " Expected:\n" + _infoVendors + "\n"
                  + " Found:\n" + _existingVendors);
        }
        final HashMap<String, TableInfo.Column> _columnsCustomers = new HashMap<String, TableInfo.Column>(6);
        _columnsCustomers.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCustomers.put("vendor_id", new TableInfo.Column("vendor_id", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCustomers.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCustomers.put("customer_code", new TableInfo.Column("customer_code", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCustomers.put("pin", new TableInfo.Column("pin", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCustomers.put("phone", new TableInfo.Column("phone", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCustomers = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCustomers = new HashSet<TableInfo.Index>(1);
        _indicesCustomers.add(new TableInfo.Index("index_customers_customer_code", true, Arrays.asList("customer_code"), Arrays.asList("ASC")));
        final TableInfo _infoCustomers = new TableInfo("customers", _columnsCustomers, _foreignKeysCustomers, _indicesCustomers);
        final TableInfo _existingCustomers = TableInfo.read(db, "customers");
        if (!_infoCustomers.equals(_existingCustomers)) {
          return new RoomOpenHelper.ValidationResult(false, "customers(com.nammasanthe.ledger.data.db.entities.CustomerEntity).\n"
                  + " Expected:\n" + _infoCustomers + "\n"
                  + " Found:\n" + _existingCustomers);
        }
        final HashMap<String, TableInfo.Column> _columnsTransactions = new HashMap<String, TableInfo.Column>(7);
        _columnsTransactions.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("customer_id", new TableInfo.Column("customer_id", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("vendor_id", new TableInfo.Column("vendor_id", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("amount", new TableInfo.Column("amount", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("type", new TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("date", new TableInfo.Column("date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("note", new TableInfo.Column("note", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTransactions = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTransactions = new HashSet<TableInfo.Index>(2);
        _indicesTransactions.add(new TableInfo.Index("index_transactions_customer_id", false, Arrays.asList("customer_id"), Arrays.asList("ASC")));
        _indicesTransactions.add(new TableInfo.Index("index_transactions_vendor_id", false, Arrays.asList("vendor_id"), Arrays.asList("ASC")));
        final TableInfo _infoTransactions = new TableInfo("transactions", _columnsTransactions, _foreignKeysTransactions, _indicesTransactions);
        final TableInfo _existingTransactions = TableInfo.read(db, "transactions");
        if (!_infoTransactions.equals(_existingTransactions)) {
          return new RoomOpenHelper.ValidationResult(false, "transactions(com.nammasanthe.ledger.data.db.entities.TransactionEntity).\n"
                  + " Expected:\n" + _infoTransactions + "\n"
                  + " Found:\n" + _existingTransactions);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "c58c6b49ef4b9296e7a16fe912056f5b", "c22f27c20263984fc7a65038a41c4595");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "vendors","customers","transactions");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `vendors`");
      _db.execSQL("DELETE FROM `customers`");
      _db.execSQL("DELETE FROM `transactions`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(VendorDao.class, VendorDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(CustomerDao.class, CustomerDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(TransactionDao.class, TransactionDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public VendorDao vendorDao() {
    if (_vendorDao != null) {
      return _vendorDao;
    } else {
      synchronized(this) {
        if(_vendorDao == null) {
          _vendorDao = new VendorDao_Impl(this);
        }
        return _vendorDao;
      }
    }
  }

  @Override
  public CustomerDao customerDao() {
    if (_customerDao != null) {
      return _customerDao;
    } else {
      synchronized(this) {
        if(_customerDao == null) {
          _customerDao = new CustomerDao_Impl(this);
        }
        return _customerDao;
      }
    }
  }

  @Override
  public TransactionDao transactionDao() {
    if (_transactionDao != null) {
      return _transactionDao;
    } else {
      synchronized(this) {
        if(_transactionDao == null) {
          _transactionDao = new TransactionDao_Impl(this);
        }
        return _transactionDao;
      }
    }
  }
}
