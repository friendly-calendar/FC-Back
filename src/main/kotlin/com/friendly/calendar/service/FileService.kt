package com.friendly.calendar.service

import org.springframework.web.multipart.MultipartFile

interface FileService {

    fun uploadFile(filePath: String, multipartFile: MultipartFile)

    fun readFile()

    fun deleteFile()

    fun replaceFile()
}
