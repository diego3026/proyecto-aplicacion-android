package com.example.shopu.data.network

import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@Singleton
class FileService @Inject constructor(private val firabaseClient: FirebaseClient){
    suspend fun downloadFile(url: String): Result<String> {
        return suspendCancellableCoroutine { continuation ->
            val httpsReference = firabaseClient.storage.getReferenceFromUrl(url)
            val localFile = File.createTempFile("downloaded", "pdf")

            httpsReference.getFile(localFile)
                .addOnSuccessListener {
                    continuation.resume(Result.success("File downloaded to ${localFile.absolutePath}"))
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }
    }
}