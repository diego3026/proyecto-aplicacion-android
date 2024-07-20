package com.example.shopu.domain

import com.example.shopu.data.network.FileService
import com.example.shopu.ui.register.model.UserRegister
import javax.inject.Inject

class DownloadFileUseCase @Inject constructor(private val fileService: FileService){
    suspend operator fun invoke(url: String) = fileService.downloadFile(url)
}