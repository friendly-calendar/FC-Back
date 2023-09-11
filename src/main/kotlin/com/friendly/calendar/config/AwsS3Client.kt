package com.friendly.calendar.config

import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.s3.model.PutObjectResult
import org.springframework.stereotype.Component
import java.io.InputStream

@Component
class AwsS3Client(val config: AwsS3Config) {

    private val client: AmazonS3

    init {
        val credentialsProvider = object : AWSCredentialsProvider {
            override fun getCredentials() = BasicAWSCredentials(config.accessKey, config.secretKey)
            override fun refresh() {}
        }
        client = AmazonS3ClientBuilder.standard()
            .withCredentials(credentialsProvider)
            .withRegion(config.region)
            .build()
    }

    fun uploadFile(filePath: String, fileName: String, content: InputStream): PutObjectResult {
        return client.putObject(config.bucket, "$filePath/$fileName", content, null)
    }
}
