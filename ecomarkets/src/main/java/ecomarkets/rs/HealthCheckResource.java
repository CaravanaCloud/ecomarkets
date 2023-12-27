package ecomarkets.rs;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/_hc")
public class HealthCheckResource {
    @Inject
    DataSource ds;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, String> getHealthCheck() {
        var result = Map.of(
            "project.version", getVersion(),
            "datasrouce.isValid", isValidDatasource()
        );
        return result;
    }

    public String isValidDatasource(){
        try(var conn = ds.getConnection()){
            return "" + conn.isValid(30);
        }catch (Exception e){
            return e.getMessage();
        }
    }

    private static String getVersion() {
        var prop = new Properties();
        try (var input = HealthCheckResource.class
            .getClassLoader()
            .getResourceAsStream("version.properties")) {
            if (input == null)
                return "0.0.0";
            prop.load(input);
            return prop.getProperty("project.version");
        } catch (IOException ex) {
            ex.printStackTrace();
            return "";
        }
    }

}
