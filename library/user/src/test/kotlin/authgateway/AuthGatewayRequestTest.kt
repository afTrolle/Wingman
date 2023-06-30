@file:OptIn(ExperimentalSerializationApi::class)

import dev.trolle.wingman.db.Database
import dev.trolle.wingman.user.tinder.tinder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.Json
import kotlinx.serialization.protobuf.ProtoBuf
import tinder.services.authgateway.AuthGatewayRequest
import tinder.services.authgateway.AuthGatewayResponse
import tinder.services.authgateway.Phone
import kotlin.test.Test
import kotlin.test.assertEquals

internal class AuthGatewayRequestTest {

    @Test
    fun genreateprotodescirption() {
        val proto = ProtoBuf { encodeDefaults = false }
        val requestData = readFile("loginStartRequestData.bin")

        val requestDecoded = proto.decodeFromByteArray(AuthGatewayRequest.serializer(), requestData)
        val requestEncoded =
            proto.encodeToByteArray(AuthGatewayRequest.serializer(), requestDecoded)

        val rawHex = requestData.toHexString()
        val requestHex = requestEncoded.toHexString()
        assertEquals(rawHex, requestHex)
    }

    private fun ByteArray.toHexString() =
        joinToString("") { it.toString(radix = 16).padStart(2, '0') }


    @Test
    fun parseLoginRequests() {
        val proto = ProtoBuf { encodeDefaults = false }
        listOf(
            "login_request_1.bin",
            "login_request_2.bin",
            "login_request_3.bin",
        ).map {
            readFile(it)
        }.map {
            val decoded = proto.decodeFromByteArray(AuthGatewayRequest.serializer(), it)
            val encoded = proto.encodeToByteArray(AuthGatewayRequest.serializer(), decoded)
            val dataHex = it.toHexString()
            val encodedHex = encoded.toHexString()
            assertEquals(dataHex, encodedHex)
        }
    }

    @Test
    fun parseLoginResponse() {
        val proto = ProtoBuf { encodeDefaults = false }
        listOf(
            "login_response_1.bin",
//            "login_response_2.bin",
            "login_response_3.bin",
        ).asSequence().map {
            println(it)
            readFile(it)
        }.forEach {
            val decoded = proto.decodeFromByteArray(AuthGatewayResponse.serializer(), it)
            val encoded = proto.encodeToByteArray(AuthGatewayResponse.serializer(), decoded)
            val decodedtwo = proto.decodeFromByteArray(AuthGatewayResponse.serializer(), it)
            val dataHex = it.toHexString()
            val encodedHex = encoded.toHexString()

            assertEquals(dataHex, encodedHex)
        }
    }

    fun readFile(name: String) = AuthGatewayRequestTest::class.java.getResourceAsStream(name)!!
        .readAllBytes()


    @Test
    fun requestSignIn() {

        runBlocking {
            val tinder = tinder(
                ProtoBuf { encodeDefaults = false },
                Json,
                object : Database {
                    override suspend fun <T> set(
                        key: String,
                        serializer: SerializationStrategy<T>,
                        value: T,
                    ) {
                    }

                    override suspend fun <T> get(
                        key: String,
                        deserializer: DeserializationStrategy<T>,
                    ): T? = null

                    override suspend fun <T> getFlow(
                        key: String,
                        deserializer: DeserializationStrategy<T>,
                    ): Flow<T> = emptyFlow()
                },
            )

            tinder.login(AuthGatewayRequest(phone = Phone(phone = "46768041639")))
        }
    }
}