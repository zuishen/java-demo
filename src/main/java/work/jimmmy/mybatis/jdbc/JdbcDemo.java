package work.jimmmy.mybatis.jdbc;

import java.sql.*;

/**
 * JDBC操作
 *
 * 1.注册驱动: 告诉程序该使用哪一个数据库驱动jar
 * 2.获取数据库连接对象 Connection: DriverManager.getConnection
 *      mysql 6.0以下: com.mysql.jdbc.Driver, jdbc:mysql://localhost:3306/test
 *      mysql 6.0及以上: com.mysql.cj.jdbc.Driver, jdbc:mysql://localhost:3306/test?useSSL=false&serverTimezone=UTC
 * 3.定义sql
 * 4.获取执行sql的对象 statement
 * 5.执行SQL
 * 6.处理返回结果
 * 7.释放资源
 */
public class JdbcDemo {
    private static final String DRIVER_CLASS = "com.mysql.cj.jdbc.Driver";

    private static final String DB_URL = "jdbc:mysql://localhost:3306/test?useSSL=false&serverTimezone=UTC";

    private static final String DB_USER = "root";

    private static final String DB_PASSWD = "123456Aa";

    public static void main(String[] args) {
        try {
            insertDemo("jimmy", 100.0);
            queryDemo();
            updateByIdDemo("jimmmy", 99.0, 1);
            queryDemo();
            insertDemo("ss", 100.0);
            queryDemo();
            deleteDemo("jimmy");
            queryDemo();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private static void queryDemo() throws ClassNotFoundException, SQLException {
        // 手动加载Driver类（注册驱动）
        Class.forName(DRIVER_CLASS);
        // 获得连接
        Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWD);
        // 获得statement
        PreparedStatement statement = connection.prepareStatement("select id, name, score from student");
        // 执行查询
        ResultSet rs = statement.executeQuery();
        // 遍历结果集
        while (rs.next()) {
            // 封装实体
            Student student = new Student();
            student.setId(rs.getInt("id"));
            student.setName(rs.getString("name"));
            student.setScore(rs.getDouble("score"));
            System.out.println(student);
        }
        rs.close();
        statement.close();
        connection.close();
    }

    private static void insertDemo(String name, Double score) throws ClassNotFoundException, SQLException {
        Student student = new Student();
        student.setName(name);
        student.setScore(score);
        // 手动加载Driver类（注册驱动）
        Class.forName(DRIVER_CLASS);
        // 获得连接
        Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWD);
        // 获得statement
        PreparedStatement statement = connection.prepareStatement("insert into student(name, score) values (?,?)");
        // 设置占位符参数
        statement.setString(1, student.getName());
        statement.setDouble(2, student.getScore());
        // 执行更新操作
        int rows = statement.executeUpdate();
        System.out.println("Insert " + rows + " records.");
        // 释放资源
        statement.close();
        connection.close();
    }

    private static void updateByIdDemo(String name, Double score, Integer id) throws ClassNotFoundException {
        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setScore(score);
        // 手动加载Driver类（注册驱动）
        Class.forName(DRIVER_CLASS);
        // 获得连接
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWD);
            statement = connection.prepareStatement("update student set name=?,score=? where id=?");
            // 设置占位符参数
            statement.setString(1, student.getName());
            statement.setDouble(2, student.getScore());
            statement.setInt(3, student.getId());
            // 执行更新操作
            int rows = statement.executeUpdate();
            System.out.println("Update " + rows + " rows.");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
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
    }

    private static void deleteDemo(String name) throws ClassNotFoundException {
        // 注册驱动：告诉程序该使用哪一个数据库驱动jar
        Class.forName(DRIVER_CLASS);
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWD);
             PreparedStatement preparedStatement = connection.prepareStatement("delete from student where name=?")) {
            preparedStatement.setString(1, name);
            int rows = preparedStatement.executeUpdate();
            System.out.println("Delete " + rows + " records.");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
