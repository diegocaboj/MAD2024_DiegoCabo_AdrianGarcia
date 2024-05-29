package es.upm.btb.helloworldkt.externalService

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

    interface ApiService {
        @GET
        fun getParkings(@Url url: String): Call<APImodel>
    }
