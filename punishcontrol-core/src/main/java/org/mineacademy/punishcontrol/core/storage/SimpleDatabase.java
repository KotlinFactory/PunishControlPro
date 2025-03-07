package org.mineacademy.punishcontrol.core.storage;

import de.leonhard.storage.util.Valid;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import lombok.RequiredArgsConstructor;

/**
 * Represents a simple MySQL database
 *
 * <p>Before running queries make sure to call connect() methods.
 *
 * <p>You can also override {@link #onConnected()} to run your code after the connection
 * has been established.
 *
 * <p>To use this class you must know the MySQL command syntax!
 */
public class SimpleDatabase {

  /**
   * Map of variables you can use with the {} syntax in SQL
   */
  private final Map<String, String> sqlVariables = new HashMap<>();
  /**
   * The established connection, or null if none
   */
  Connection connection;
  /**
   * The last credentials from the connect function, or null if never called
   */
  private LastCredentials lastCredentials;

  // --------------------------------------------------------------------
  // Connecting
  // --------------------------------------------------------------------

  /**
   * Attempts to establish a new database connection
   *
   * @param host
   * @param port
   * @param database
   * @param user
   * @param password
   */
  public final void connect(
      final String host,
      final int port,
      final String database,
      final String user,
      final String password) {
    connect(host, port, database, user, password, null);
  }

  /**
   * Attempts to establish a new database connection, you can then use {table} in SQL to
   * replace with your table name
   *
   * @param host
   * @param port
   * @param database
   * @param user
   * @param password
   * @param table
   */
  public final void connect(
      final String host,
      final int port,
      final String database,
      final String user,
      final String password,
      final String table) {
    connect(host, port, database, user, password, table, true);
  }

  /**
   * Attempts to establish a new database connection you can then use {table} in SQL to
   * replace with your table name
   *
   * @param host
   * @param port
   * @param database
   * @param user
   * @param password
   * @param table
   * @param autoReconnect
   */
  public final void connect(
      final String host,
      final int port,
      final String database,
      final String user,
      final String password,
      final String table,
      final boolean autoReconnect) {
    connect(
        "jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect="
        + autoReconnect,
        user,
        password,
        table);
  }

  /**
   * Connects to the database
   *
   * @param url
   * @param user
   * @param password
   */
  public final void connect(final String url, final String user, final String password) {
    connect(url, user, password, null);
  }

  /**
   * Connects to the database you can then use {table} in SQL to replace with your table
   * name*
   *
   * @param url
   * @param user
   * @param password
   * @param table
   */
  public final void connect(
      final String url, final String user, final String password, final String table) {
    lastCredentials = new LastCredentials(url, user, password, table);

    try {
      connection = DriverManager.getConnection(url, user, password);

      onConnected();

    } catch (final SQLException e) {
      e.printStackTrace();

      System.out.println("Failed to connect to MySQL database");
      System.out.println("URL: " + url);
      System.out.println("Error: " + e.getMessage());
    }
  }

  /**
   * Attempts to connect using last known credentials. Fails gracefully if those are not
   * provided i.e. connect function was never called
   */
  private void connectUsingLastCredentials() {
    if (lastCredentials != null) {
      connect(
          lastCredentials.url,
          lastCredentials.user,
          lastCredentials.password,
          lastCredentials.table);
    }
  }

  /**
   * Called automatically after the first connection has been established
   */
  protected void onConnected() {
  }

  // --------------------------------------------------------------------
  // Disconnecting
  // --------------------------------------------------------------------

  /**
   * Attempts to close the connection, if not null
   */
  protected final void close() {
    if (connection != null) {
      synchronized (connection) {
        try {
          connection.close();

        } catch (final SQLException e) {
          System.err.println("Error closing MySQL connection");
        }
      }
    }
  }

  // --------------------------------------------------------------------
  // Querying
  // --------------------------------------------------------------------

  /**
   * Attempts to execute a new update query
   *
   * <p>Make sure you called connect() first otherwise an error will be thrown
   *
   * @param sql
   */
  protected final void update(String sql) {
    checkEstablished();

    synchronized (connection) {
      if (!isConnected()) {
        connectUsingLastCredentials();
      }

      sql = replaceVariables(sql);

      try {
        final Statement statement = connection.createStatement();

        statement.executeUpdate(sql);
        statement.close();

      } catch (final SQLException e) {
        System.err.println("Error on updating MySQL with: " + sql);
        e.printStackTrace();
      }
    }
  }

  /**
   * Attempts to execute a new query
   *
   * <p>Make sure you called connect() first otherwise an error will be thrown
   *
   * @param sql
   * @return
   */
  protected final ResultSet query(String sql) {
    checkEstablished();

    synchronized (connection) {
      if (!isConnected()) {
        connectUsingLastCredentials();
      }

      sql = replaceVariables(sql);

      //			Debugger.debug("mysql", "Querying MySQL with: " + sql);
      try {

        final Statement statement = connection.createStatement();
        final ResultSet resultSet = statement.executeQuery(sql);

        return resultSet;

      } catch (final SQLException e) {
        System.err.println("Error on querying MySQL with: " + sql);
        e.printStackTrace();
      }
    }

    return null;
  }

  protected final ResultSet query0(String sql) throws SQLException {
    checkEstablished();

    synchronized (connection) {
      if (!isConnected()) {
        connectUsingLastCredentials();
      }

      sql = replaceVariables(sql);

      //			Debugger.debug("mysql", "Querying MySQL with: " + sql);

      final Statement statement = connection.createStatement();
      final ResultSet resultSet = statement.executeQuery(sql);

      return resultSet;

    }
  }

  /**
   * Attempts to return a prepared statement
   *
   * <p>Make sure you called connect() first otherwise an error will be thrown
   *
   * @param sql
   * @return
   * @throws SQLException
   */
  protected final java.sql.PreparedStatement prepareStatement(String sql)
      throws SQLException {
    checkEstablished();

    synchronized (connection) {
      if (!isConnected()) {
        connectUsingLastCredentials();
      }

      sql = replaceVariables(sql);

      //			Debugger.debug("mysql", "Preparing statement: " + sql);

      return connection.prepareStatement(sql);
    }
  }

  /**
   * Is the connection established, open and valid? Performs a blocking ping request to
   * the database
   *
   * @return whether the connection driver was set
   */
  public final boolean isConnected() {
    if (!isLoaded()) {
      return false;
    }

    synchronized (connection) {
      try {
        return connection != null && !connection.isClosed() && connection.isValid(0);

      } catch (final SQLException ex) {
        return false;
      }
    }
  }

  // --------------------------------------------------------------------
  // Non-blocking checking
  // --------------------------------------------------------------------

  /**
   * Return the table from last connection, throwing an error if never connected
   *
   * @return
   */
  protected final String getTable() {
    checkEstablished();

    return getOrEmpty(lastCredentials.table);
  }

  /**
   * Checks if the connect() function was called
   */
  private void checkEstablished() {
    Valid.checkBoolean(isLoaded(), "Connection was never established");
  }

  /**
   * Return true if the connect function was called so that the driver was loaded
   *
   * @return
   */
  public final boolean isLoaded() {
    return connection != null;
  }

  // --------------------------------------------------------------------
  // Variables
  // --------------------------------------------------------------------

  /**
   * Adds a new variable you can then use in your queries. The variable name will be added
   * {} brackets automatically.
   *
   * @param name
   * @param value
   */
  protected final void addVariable(final String name, final String value) {
    sqlVariables.put(name, value);
  }

  /**
   * Replace the {table} and {@link #sqlVariables} in the sql query
   *
   * @param sql
   * @return
   */
  private String replaceVariables(String sql) {

    for (final Entry<String, String> entry : sqlVariables.entrySet()) {
      sql = sql.replace("{" + entry.getKey() + "}", entry.getValue());
    }

    return sql.replace("{table}", getTable());
  }

  private String getOrEmpty(final String input) {
    return input == null || "none".equalsIgnoreCase(input) ? "" : input;
  }

  /**
   * Stores last known credentials from the connect() functions
   */
  @RequiredArgsConstructor
  private static final class LastCredentials {

    /**
     * The connecting URL, for example:
     *
     * <p>jdbc:mysql://host:port/database
     */
    private final String url;

    /**
     * The user name for the database
     */
    private final String user;

    /**
     * The password for the database
     */
    private final String password;

    /**
     * The table. Never used in this class, only stored for your convenience
     */
    private final String table;
  }
}
