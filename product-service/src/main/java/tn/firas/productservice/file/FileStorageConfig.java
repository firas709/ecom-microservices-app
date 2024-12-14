package tn.firas.productservice.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class FileStorageConfig {
    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    @Bean
    public Path fileStorageLocation() {
        return Paths.get(uploadDir).toAbsolutePath().normalize();
    }
}