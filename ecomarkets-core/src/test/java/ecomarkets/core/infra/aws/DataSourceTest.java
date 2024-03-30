package ecomarkets.infra.aws;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.sql.DataSource;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
public class DataSourceTest {
    @Inject
    DataSource ds;

    @Test
    @Order(0)
    public void testDataSource() {
        assertNotNull(ds);
        try(var conn = ds.getConnection();
            var stmt = conn.createStatement()) {
            assertNotNull(conn);
            assertNotNull(stmt);
            var rs = stmt.executeQuery("SELECT 1+1");
            assertNotNull(rs);
            assertTrue(rs.next());
            assertEquals(2, rs.getInt(1));
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
            assert false;
        }
    }
}
