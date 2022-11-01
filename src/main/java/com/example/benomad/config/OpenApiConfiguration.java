package com.example.benomad.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(info = @Info(
        title = "BeNomad API documentation",
        description = "" +
                "Never gonna give you up",
        contact = @Contact(
                name = "Nomads",
                email = "travelkyrgyzstan2022@gmail.com"
        ),
        license = @License(
                name = "SIGMA Licence",
                url = "https://github.com/thombergs/code-examples/blob/master/LICENSE")),
        servers = {@Server(url = "http://localhost:8080"), @Server(url = "https://benomad-backend.herokuapp.com")}
)
public class OpenApiConfiguration {
}
