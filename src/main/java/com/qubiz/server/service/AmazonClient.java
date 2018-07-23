package com.qubiz.server.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/*
 ******************************
 # Created by Tirla Ovidiu #
 # 22.07.2018 #
 ******************************
*/
@Service
public class AmazonClient {
    private AmazonS3 amazonS3;

    @Value("${amazonProperties.endpointUrl}")
    private String endpointUrl;
    @Value("${amazonProperties.bucketName}")
    private String bucketName;
    @Value("${amazonProperties.accessKey}")
    private String accessKey;
    @Value("${amazonProperties.secretKey}")
    private String secretKey;


    @PostConstruct
    private void initializeAmazon() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        this.amazonS3 = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion("eu-east")
                .build();
    }

    public String uploadPhoto(BufferedImage image, int jobId) throws IOException {
        String fileName = jobId + "/" + UUID.randomUUID();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(image, "PNG", os);
        byte[] buffer = os.toByteArray();
        InputStream is = new ByteArrayInputStream(buffer);
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentLength(buffer.length);
        meta.setContentType("image/png");
        amazonS3.putObject(new PutObjectRequest(bucketName, fileName, is, meta));
        return fileName;
    }

    public void deletePhoto(String fileName) {
        amazonS3.deleteObject(bucketName, fileName);
    }

    public String getEndpointUrl() {
        return endpointUrl;
    }
}
