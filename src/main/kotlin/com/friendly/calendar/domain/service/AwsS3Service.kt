package com.friendly.calendar.service

import com.friendly.calendar.config.AwsS3Client
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.net.URLEncoder

@Service
class AwsS3Service(val s3Client: AwsS3Client) : FileService {
    override fun uploadFile(filePath: String, multipartFile: MultipartFile) {
        val encodedFileName = URLEncoder.encode(multipartFile.originalFilename, "UTF-8")
        s3Client.uploadFile(filePath, encodedFileName, multipartFile)
    }

    override fun readFile() {
        TODO("Not yet implemented")
    }

    override fun deleteFile() {
        TODO("Not yet implemented")
    }

    override fun replaceFile() {
        TODO("Not yet implemented")
    }
}
