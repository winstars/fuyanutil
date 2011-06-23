package utils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.logicalcobwebs.proxool.ProxoolDataSource;
//import douyu.mvc.Controller;

import java.io.PrintWriter;
/**
 * User: qiukx
 * Date: 2011-6-13
 * Time: 9:48:24
 * Company: www.tieland.com.cn
 */
//@Controller
public class DBC {
    private Connection connection = null;
    private DataSource dataSource = null;

	//public void index(PrintWriter out) {
	//	out.println("error");
	//}

	private static DBC instance = new DBC();

	public static DBC getInstance(){
		return instance;
	}

    public DBC() {
		try{
			ProxoolDataSource ds = new ProxoolDataSource();
			this.dataSource = ds;
			ds.setDriver("org.h2.Driver");
			ds.setDriverUrl("jdbc:h2:tcp://localhost/~/fuyanutil");
			ds.setUser("sa");
			ds.setPassword("");
			ds.setAlias("DB");
			ds.setPrototypeCount(1);
			ds.setMaximumConnectionCount(10);
			ds.setMinimumConnectionCount(1);
			ds.setTrace(true);
			ds.setVerbose(true);
		}catch(Exception e){
			e.printStackTrace();
		}
    }

    /**
     * 获取一个CONNECTION连接
     *
     * @return
     */
    public Connection getConnection() {
        try {
            if (connection==null || connection.isClosed()) {
                connection = dataSource.getConnection();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("------获取连接失败.------");
        }
        return connection;
    }

    /**
     * 关闭连接
     *
     * @throws java.sql.SQLException
     */
    public void close() throws SQLException {
        this.connection.close();
    }

    /**
     * 执行insert操作
     *
     * @param sql
     * @return
     * @throws java.sql.SQLException
     */
    public String insertDB(String sql) throws SQLException {
        Statement statement = null;
        try {
            getConnection();
            statement = connection.createStatement();
            System.out.println("\n------执行INSERT:------\n" + sql + "\n------");
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("------执行INSERT失败.------");
            connection.close();
            connection = null;
            throw new SQLException("执行INSERT失败");
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return null;//TODO 数据库新增操作  将返回一个新增的ID  //暂时不作返回
    }

	public String insertDB(PreparedStatement preparedStatement) throws SQLException {
		try {
            System.out.println("\n------执行INSERT by parepared statement:------");
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("------执行INSERT失败.------");
            connection.close();
            connection = null;
            throw new SQLException("执行INSERT失败");
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return null;//TODO 数据库新增操作  将返回一个新增的ID  //暂时不作返回
	}

    /**
     * 执行update操作
     *
     * @param sql
     * @return
     * @throws java.sql.SQLException
     */
    public int updateDB(String sql) throws SQLException {
        Statement statement = null;
        int row = 0;
        try {
            getConnection();
            statement = connection.createStatement();
            System.out.println("\n------执行UPDATE:------\n" + sql + "\n------");
            row = statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("------执行UPDATE失败.------");
            connection.close();
            connection = null;
            throw new SQLException("执行UPDATE失败");
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return row;//TODO 数据库更新操作
    }

    /**
     * 执行批量insert操作
     *
     * @param sql
     * @return
     * @throws java.sql.SQLException
     */
    public int insertBatch(String sql, String[][] data) throws SQLException {
        PreparedStatement preparedStatement = null;
        int row = 0;
        try {
            getConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(sql);
            for (String[] _data : data) {
                for (int i = 0; i < _data.length; i++) {
                    preparedStatement.setString(i + 1, _data[i]);
                }
                preparedStatement.addBatch();
            }
            System.out.println("\n------执行批量INSERT:------\n" + sql + "\n------");
            preparedStatement.executeBatch();
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("------执行批量INSERT失败.------");
            connection.close();
            connection = null;
            throw new SQLException("执行批量INSERT失败");
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return row;//TODO 数据库更新操作
    }

    /**
     * 执行delete操作
     *
     * @param sql
     * @return
     * @throws java.sql.SQLException
     */
    public String deleteDB(String sql) throws SQLException {
        Statement preparedStatement = null;
        try {
            getConnection();
            preparedStatement = connection.createStatement();
            System.out.println("\n------执行DELETE:------\n" + sql + "\n------");
            preparedStatement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("------执行DELETE失败.------");
            connection.close();
            connection = null;
            throw new SQLException("执行DELETE失败");
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return null;//TODO 数据库删除操作
    }

    /**
     * 查询查询操作
     *
     * @param sql
     * @return 返回一个二维数组
     * @throws java.sql.SQLException
     */
    public String[][] getArr(String sql) throws SQLException {
        Statement preparedStatement = null;
        try {
            getConnection();
            preparedStatement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet = null;
            System.out.println("\n------执行SELECT:------\n" + sql + "\n------");
            resultSet = preparedStatement.executeQuery(sql);
            int[] colrow = countRowAndCol(resultSet);
            int cols = colrow[0];
            int rows = colrow[1];
            if (rows == 0) return null;
            String[][] arr = new String[rows][cols];
            int row = 0;
            while (resultSet.next()) {
                for (int i = 1; i <= cols; i++) {
                    arr[row][i - 1] = resultSet.getString(i);
                }
                row++;
            }
            return arr;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("执行SELECT失败.");
            connection.close();
            connection = null;
            throw new SQLException("执行SELECT失败");
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        //TODO 一般用来多条记录的查询
    }

    /**
     * 启动事务
     *
     * @return
     * @throws java.sql.SQLException
     */
    public String beginTransaction() throws SQLException {
        try {
            getConnection();
            System.out.println("------启动事务.------");
            connection.setAutoCommit(false);
            return null;
        } catch (SQLException e) {
            System.out.println("------启动事务失败.------");
            connection.close();
            connection = null;
            throw new SQLException("启动事务失败");
        }
    }

    /**
     * 提交事务
     *
     * @return
     * @throws java.sql.SQLException
     */
    public String endTransaction() throws SQLException {
        try {
            System.out.println("------提交事务.------");
            connection.commit();
            connection.setAutoCommit(true);
            return null;
        } catch (SQLException e) {
            System.out.println("------提交事务失败.------");
            connection.rollback();
            connection.close();
            connection = null;
            throw new SQLException("提交事务失败");
        }
    }

    private int[] countRowAndCol(ResultSet resultSet) throws SQLException {
        int cols = resultSet.getMetaData().getColumnCount();
        System.out.println("------一共" + cols + "列------");
        resultSet.last();
        int rows = resultSet.getRow();
        System.out.println("------共取到" + rows + "行数据.------");
        resultSet.beforeFirst();
        return new int[]{cols, rows};
    }

	public Statement getStatement() throws SQLException{
		getConnection();
		return connection.createStatement();
	}
}
