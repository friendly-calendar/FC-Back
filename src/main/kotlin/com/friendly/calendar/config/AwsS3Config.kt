package com.friendly.calendar.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@ConfigurationProperties(prefix = "aws.s3")
@Component
class AwsS3Config {

    lateinit var accessKey: String

    lateinit var secretKey: String

    lateinit var bucket: String

    lateinit var region: String
}
