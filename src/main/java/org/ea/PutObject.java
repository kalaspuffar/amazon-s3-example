package org.ea;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import java.io.File;
import java.util.List;

public class PutObject {
    static String bucketName = "daniel-testing-bucket";

    public static AWSCredentials getCredentials() {
        AWSCredentials credentials = new BasicAWSCredentials(
            "[KEY]",
            "[SECRET]"
        );
        return credentials;
    }

    public static AmazonS3 getAmazonClient() {
        AmazonS3 s3client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(getCredentials()))
                .withRegion(Regions.US_EAST_1)
                .build();

        return s3client;
    }

    public static AmazonS3 getLocalClient() {
        AWSCredentials localCred = new BasicAWSCredentials(
            "[KEY]",
            "[SECRET]"
        );

        AmazonS3 s3client = AmazonS3Client.builder()
                .withCredentials((new AWSStaticCredentialsProvider(localCred)))
                .withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration("http://mon1.ea.org:8080", "")
                )
                .withPathStyleAccessEnabled(true)
                .build();

        return s3client;
    }

    public static void main(String[] args) {
        //AmazonS3 s3client = getAmazonClient();
        AmazonS3 s3client = getLocalClient();

        List<Bucket> buckets = s3client.listBuckets();
        for(Bucket bucket : buckets) {
            System.out.println(bucket.getName());
        }

        s3client.putObject(
                bucketName,
                "pom.xml",
                new File("pom.xml")
        );

        ObjectListing objectListing = s3client.listObjects(bucketName);
        for(S3ObjectSummary os : objectListing.getObjectSummaries()) {
            System.out.println(os.getKey());
        }
    }
}
