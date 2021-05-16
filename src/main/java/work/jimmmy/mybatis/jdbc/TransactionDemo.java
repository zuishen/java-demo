package work.jimmmy.mybatis.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 事务：一个包含多个步骤的业务操作。如果这个业务操作被事务管理，则这多个步骤要么同时成功，要么同时失败
 * 操作：1.开启事务 2.提交事务 3.回滚事务
 * 使用Connection对象来管理事务
 *   - 开启事务：setAutoCommit(boolean autoCommit) : 调用该方法设置参数为false，即开启事务
 *   - 提交事务：commit() : 在所有sql都执行完后提交事务
 *   - 回滚事务：rollback() : 在catch中回滚事务
 */
public class TransactionDemo {
    public static void main(String[] args) {
        // zhangsan给lisi转500元
        Connection connection = null;
        PreparedStatement transferPstmt = null;
        PreparedStatement receivePstmt = null;
        Object problem = null;
        try {
            connection = JdbcUtil.getConnection();
            connection.setAutoCommit(false);
            transferPstmt = connection.prepareStatement("update account set balance = balance - ? where name = ?");
            transferPstmt.setDouble(1, 500.0);
            transferPstmt.setString(2, "zhangsan");
            transferPstmt.executeUpdate();
            // 抛出空指针异常
            problem.equals("abc");
            receivePstmt = connection.prepareStatement("update account set balance = balance + ? where name = ?");
            receivePstmt.setDouble(1, 500.0);
            receivePstmt.setString(2, "lisi");
            receivePstmt.executeUpdate();
            connection.commit();
        } catch (Exception throwables) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            throwables.printStackTrace();
        } finally {
            JdbcUtil.close(transferPstmt, null);
            JdbcUtil.close(receivePstmt, connection);
        }
    }
}
