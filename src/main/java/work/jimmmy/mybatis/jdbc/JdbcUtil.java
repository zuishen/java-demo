package work.jimmmy.mybatis.jdbc;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;

/**
 * 目的：简化书写
 * 分析：
 * 	1. 注册驱动也抽取
 * 	2. 抽取一个方法获取连接对象
 * 		- 需求：不想传递参数（麻烦），还得保证工具类的通用性。
 * 		- 解决：配置文件
 * 			jdbc.properties
 * 				url=
 * 				user=
 * 				password=
 */
public class JdbcUtil {
    private static String url;

    private static String user;

    private static String password;

    private static String driver;

    /*
     * 文件的读取，只需要读取一次即可拿到这些值。使用静态代码块
     */
    static {
        Properties properties = new Properties();
        // 获取类根路径下的文件方式--》ClassLoader类加载器
        try {
            properties.load(JdbcUtil.class.getClassLoader().getResourceAsStream("jdbc.properties"));
            url = properties.getProperty("url");
            user = properties.getProperty("user");
            password = properties.getProperty("password");
            driver = properties.getProperty("driver");
            Class.forName(driver);
            System.out.println("load driver success.");
        } catch (IOException e) {
            System.out.println("Failed to load jdbc properties");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    /**
     * 释放资源
     *
     * @param stmt sql语句对象
     * @param connection 数据库连接对象
     */
    public static void close(Statement stmt, Connection connection) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    /**
     * 释放资源
     * @param rs 结果集
     * @param stmt sql语句对象
     * @param connection 数据库连接对象
     */
    public static void close(ResultSet rs, Statement stmt, Connection connection) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        close(stmt, connection);
    }

    /**
     * demo
     *
     * @param args args
     */
    public static void main(String[] args) {
        String username = "jimmy";
        String password = "123";
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            connection = JdbcUtil.getConnection();
            pstmt = connection.prepareStatement("select * from user where username=? and password=?");
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            rs = pstmt.executeQuery();
            System.out.println("Login: " + (rs.next() ? "success" : "failed"));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JdbcUtil.close(rs, pstmt, connection);
        }
    }
}
