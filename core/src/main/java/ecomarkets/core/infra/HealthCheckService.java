package ecomarkets.core.infra;

import ecomarkets.core.domain.core.product.image.ImageRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

@ApplicationScoped
public class HealthCheckService {
    @Inject
    DataSource ds;

    @Inject
    ImageRepository imageRepository;

    public Map<String, String> getHealthCheck() {
        var result = Map.of(
            "project.version", getVersion(),
            "datasource.isValid", isValidDatasource()
        );
        return result;
    }

    public String getBucketConfigured(){
        return imageRepository.getBucketName();
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
        try (var input = HealthCheckService.class
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