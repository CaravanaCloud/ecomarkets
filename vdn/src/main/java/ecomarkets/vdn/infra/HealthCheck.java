package ecomarkets.vdn.infra;

import java.io.IOException;
import java.util.Map;

import ecomarkets.core.infra.HealthCheckService;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/_hc")
public class HealthCheck extends HttpServlet{

    @Inject
    HealthCheckService healthCheckService;

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        for(Map.Entry<String, String> m : healthCheckService.getHealthCheck().entrySet()){
            resp.getOutputStream().println(m.getKey() + ": " + m.getValue());
        }

    }
}