package com.pasukanlangit.id.hero_datasource_test.network

import com.pasukanlangit.id.hero_datasource.network.HeroService
import com.pasukanlangit.id.hero_datasource.network.HeroServiceImpl
import com.pasukanlangit.id.hero_datasource_test.network.data.HeroDataEmpty
import com.pasukanlangit.id.hero_datasource_test.network.data.HeroDataMalformed
import com.pasukanlangit.id.hero_datasource_test.network.data.HeroDataValid
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.http.*

class HeroServiceFake {
    companion object Factory {
        fun build(
            type: HeroServiceResponseType
        ): HeroService = HeroServiceImpl(
            httpClient = HttpClient(MockEngine){
                install(JsonFeature){
                    serializer = KotlinxSerializer(
                        kotlinx.serialization.json.Json {
                            ignoreUnknownKeys = true
                        }
                    )
                }
                engine {
                    addHandler { request ->
                        when(request.url.toString()){
                            "http://api.opendota.com/api/heroStats" -> {
                                val responseHeader = headersOf(
                                    "Content-Type" to listOf("application/json", "charset=utf-8")
                                )
                                when(type){
                                    is HeroServiceResponseType.EmptyList -> {
                                        respond(
                                            content = HeroDataEmpty.data,
                                            status = HttpStatusCode.OK,
                                            headers = responseHeader
                                        )
                                    }
                                    HeroServiceResponseType.Http404 -> {
                                        respond(
                                            content = HeroDataEmpty.data,
                                            status = HttpStatusCode.NotFound,
                                            headers = responseHeader
                                        )
                                    }
                                    HeroServiceResponseType.MalformedData -> {
                                        respond(
                                            content = HeroDataMalformed.data,
                                            status = HttpStatusCode.OK,
                                            headers = responseHeader
                                        )
                                    }
                                    HeroServiceResponseType.ValidData -> {
                                        respond(
                                            content = HeroDataValid.data,
                                            status = HttpStatusCode.OK,
                                            headers = responseHeader
                                        )
                                    }
                                }
                            }
                            else -> error("Unhandled ${request.url.fullPath}")
                        }
                    }
                }
            }
        )
    }
}