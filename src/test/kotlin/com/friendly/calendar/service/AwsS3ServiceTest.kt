package com.friendly.calendar.service

import io.kotest.assertions.throwables.shouldNotThrowAny
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.mock.web.MockMultipartFile

@SpringBootTest
class AwsS3ServiceTest(@Autowired val awsS3Service: AwsS3Service) {

    @Test
    fun `AWS S3 파일 업로드가 동작합니다`() {
        shouldNotThrowAny {
            awsS3Service.uploadFile(
                "testFileUpload.png",
                MockMultipartFile("test", ByteArray(0))
            )
        }
    }
}
