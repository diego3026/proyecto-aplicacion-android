package com.example.mercaditu.di

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest

class SupabaseClient {
    private val supabaseUrl: String = "https://wcfvebhpiszdvvvjywxy.supabase.co"
    private val supabaseKey: String = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6IndjZnZlYmhwaXN6ZHZ2dmp5d3h5Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3MTk5Mzg2NjcsImV4cCI6MjAzNTUxNDY2N30.us5vHZWV3cdv2xA_3srd5GuTN0Gt4K9ppVu8eNjrfIo"

    private val client: SupabaseClient = createSupabaseClient(
        supabaseUrl = supabaseUrl,
        supabaseKey = supabaseKey
    ) {
        install(Auth)
        install(Postgrest)
    }

    fun getClient(): SupabaseClient {
        return client
    }

    fun getPostgrest(): Postgrest {
        return client.postgrest
    }

    fun getAuth(): Auth {
        return client.auth
    }

}
