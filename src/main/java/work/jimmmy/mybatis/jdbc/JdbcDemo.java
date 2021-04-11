package work.jimmmy.mybatis.jdbc;

import java.sql.*;

public class JdbcDemo {
    public static void main(String[] args) {
        try {
            insertDemo();
            queryDemo();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private static void queryDemo() throws ClassNotFoundException, SQLException {
        // 手动加载Driver类（注册驱动）
        Class.forName("com.mysql.jdbc.Driver");
        // 获得连接
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "123456Aa");
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

    private static void insertDemo() throws ClassNotFoundException, SQLException {
        Student student = new Student();
        student.setId(1);
        student.setName("jimmy");
        student.setScore(100.0);
        // 手动加载Driver类（注册驱动）
        Class.forName("com.mysql.jdbc.Driver");
        // 获得连接
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "123456Aa");
        // 获得statement
        PreparedStatement statement = connection.prepareStatement("insert into student(id, name, score) values (?,?,?)");
        // 设置占位符参数
        statement.setInt(1, student.getId());
        statement.setString(2, student.getName());
        statement.setDouble(3, student.getScore());
        // 执行更新操作
        statement.execute();
        // 释放资源
        statement.close();
        connection.close();
    }
}
