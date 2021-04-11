package work.jimmmy.mybatis.jdbc;

import java.sql.*;

public class JdbcDemo {
    public static void main(String[] args) {
        try {
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
}
