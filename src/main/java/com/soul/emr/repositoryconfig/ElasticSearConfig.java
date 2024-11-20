package com.soul.emr.repositoryconfig;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.elasticsearch.repository.config.EnableReactiveElasticsearchRepositories;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Base64;
@Configuration
@EnableReactiveElasticsearchRepositories(basePackages = "com.soul.emr.model.repository.elasticsearchrepository")
@EnableElasticsearchRepositories(basePackages = "com.soul.emr.model.repository.elasticsearchrepository")
public class ElasticSearConfig extends ElasticsearchConfiguration {
    @Value("${spring.elasticsearch.client.certificate}")
    private String certificateBase64;
    @Value("${spring.elasticsearch.uris}")
    private String hostAndPort;
    @Value("${spring.elasticsearch.username}")
    private String elasticUserName;
    @Value("${spring.elasticsearch.password}")
    private String elasticPassword;

    @Override
    public @NotNull ClientConfiguration clientConfiguration() {
        final ClientConfiguration clientConfiguration;
        try {
            clientConfiguration = ClientConfiguration.builder()
                    .connectedTo(hostAndPort)
                    .usingSsl(getSSLContext())
                    .withBasicAuth(elasticUserName, elasticPassword)
                    .build();
        } catch (KeyStoreException | CertificateException | IOException | NoSuchAlgorithmException |
                 KeyManagementException e) {
            throw new RuntimeException(e);
        }
        return clientConfiguration;
    }

    private SSLContext getSSLContext() throws KeyStoreException, CertificateException, IOException, NoSuchAlgorithmException, KeyManagementException {
        byte[] decode = Base64.getDecoder().decode(certificateBase64);
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        Certificate ca;
        try (InputStream certificateInputStream = new ByteArrayInputStream(decode)) {
            ca = cf.generateCertificate(certificateInputStream);
        } catch (IOException | CertificateException e) {
            throw new RuntimeException(e);
        }
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);

        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, tmf.getTrustManagers(), null);
        return context;
    }
}