package com.example.routing

import com.example.data.receive_request.ComputerByIdRequest
import com.example.data.receive_request.CustomerInfoByUserIdRequest
import com.example.data.receive_request.FoodRequest
import com.example.data.receive_request.PegawaiByIdRequest
import com.example.data.send_response.*
import com.example.data.table.*
import com.example.model.receive_request.LoginRequest
import com.example.model.send_response.LoginResponse
import com.example.model.send_response.MetaResponse
import com.example.util.AdminStatus
import com.example.util.DbUrl
import com.example.util.TokenManager
import com.example.util.adminPriviledge
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.DriverManager

fun Route.mainRoute() {
    get("/") {
        call.respondText("Welcome to Sukamaju Net API Service")
    }
}

fun Route.userLoginRoute() {
    post("/user_login") {
        val body = call.receive<LoginRequest>()
        val username = body.username

        transaction {
            customer.select { customer.username eq username }.limit(1).firstOrNull()
        }?.let {
            val result = customer.toCustomerModel(it)

            if (result.password == body.password) {
                call.respond(
                    LoginResponse(
                        MetaResponse(
                            "true",
                            ""
                        ),
                        TokenManager.generateJwtToken(result.username, result.customer_id)
                    )
                )
                return@post
            } else {
                call.respond(
                    LoginResponse(
                        MetaResponse(
                            "false",
                            "Password Salah"
                        ),
                        ""
                    )
                )
                return@post
            }
        }

        call.respond(
            LoginResponse(
                MetaResponse(
                    "false",
                    "Username tidak ditemukan"
                ),
                ""
            )
        )
    }
}

fun Route.adminLoginRoute() {
    post("/admin_login") {
        val body = call.receive<LoginRequest>()
        val username = body.username

        transaction {
            admin.select { admin.username eq username }.limit(1).firstOrNull()
        }?.let {
            val result = admin.toAdminModel(it)

            if (result.password == body.password) {
                call.respond(
                    LoginResponse(
                        MetaResponse(
                            "true",
                            ""
                        ),
                        TokenManager.generateJwtToken(result.username, result.pegawai_id)
                    )
                )
                return@post
            } else {
                call.respond(
                    LoginResponse(
                        MetaResponse(
                            "false",
                            "Password Salah"
                        ),
                        ""
                    )
                )
                return@post
            }
        }

        call.respond(
            LoginResponse(
                MetaResponse(
                    "false",
                    "Username tidak ditemukan"
                ),
                ""
            )
        )
    }
}

fun Route.getUserInfo() {
    get("/query_own_user") {
        val principal = call.principal<JWTPrincipal>()
        val user_id = principal!!.payload.getClaim("user_id").asString()

        transaction {
            customer_information.select { customer_information.customer_id eq user_id }.firstOrNull()
        }?.let {
            val result = customer_information.toCustomerInfoModel(it)

            call.respond(
                CustomerInfoResponse(
                    metaResponse = MetaResponse(
                        "true",
                        "Query own user success"
                    ),
                    data = result
                )
            )
        }
    }
}

fun Route.getUserInfoById() {
    post("/query_user_by_userid") {
        val body = call.receive<CustomerInfoByUserIdRequest>()

        transaction {
            customer_information.select {
                customer_information.customer_id eq body.customer_id
            }.firstOrNull()
        }?.let {
            val customerInfo = customer_information.toCustomerInfoModel(it)

            call.respond(
                CustomerInfoResponse(
                    metaResponse = MetaResponse(
                        "true",
                        "Query user success"
                    ),
                    data = customerInfo
                )
            )

            return@post
        }

        call.respond(
            CustomerInfoResponse(
                metaResponse = MetaResponse(
                    "false",
                    "User tidak ditemukan"
                ),
                data = null
            )
        )
    }
}

fun Route.getAdminInfo() {
    get("/query_own_admin") {
        val principal = call.principal<JWTPrincipal>()
        val user_id = principal!!.payload.getClaim("user_id").asString()

        transaction {
            pegawai_information.select { pegawai_information.pegawai_id eq user_id }.firstOrNull()
        }?.let {
            val result = pegawai_information.toPegawaiInfoModel(it)

            call.respond(
                PegawaiInfoResponse(
                    metaResponse = MetaResponse(
                        "true",
                        "Query pegawai success"
                    ),
                    data = result
                )
            )
            return@get
        }

        call.respond(
            PegawaiInfoResponse(
                metaResponse = MetaResponse(
                    "false",
                    "Query pegawai gagal"
                ),
                data = null
            )
        )
    }
}

fun Route.getPegawaiById() {
    post("/query_pegawai_by_pegawaiid") {
        val body = call.receive<PegawaiByIdRequest>()

        transaction {
            pegawai_information.select { pegawai_information.pegawai_id eq body.pegawai_id }.firstOrNull()
        }?.let {
            val result = pegawai_information.toPegawaiInfoModel(it)

            call.respond(
                PegawaiInfoResponse(
                    metaResponse = MetaResponse(
                        "true",
                        "Query pegawai success"
                    ),
                    data = result
                )
            )
            return@post
        }

        call.respond(
            PegawaiInfoResponse(
                metaResponse = MetaResponse(
                    "false",
                    "Query pegawai gagal"
                ),
                data = null
            )
        )
    }
}

fun Route.getComputersList() {
    get("/query_computers") {
        val result = transaction {
            komputer.join(
                otherTable = kategori_komputer,
                joinType = JoinType.INNER,
                onColumn = komputer.kategori_id,
                otherColumn = kategori_komputer.kategori_id
            ).selectAll().map {
                komputer.toComputerModel(it)
            }
        }

        call.respond(
            ComputerListResponse(
                MetaResponse(
                    "success",
                    "Query computer list success"
                ),
                result
            )
        )
    }
}

fun Route.getComputerById() {
    post("query_computers_by_id") {
        val body = call.receive<ComputerByIdRequest>()

        transaction {
            komputer.join(
                otherTable = kategori_komputer,
                joinType = JoinType.INNER,
                onColumn = komputer.kategori_id,
                otherColumn = kategori_komputer.kategori_id
            ).select {
                komputer.komputer_id eq body.komputer_id
            }.firstOrNull()
        }?.let {
            call.respond(
                ComputerByIdResponse(
                    metaResponse = MetaResponse(
                        "true",
                        "Query komputer by komputer_id success"
                    ),
                    komputer.toComputerModel(it)
                )
            )
            return@post
        }

        call.respond(
            ComputerByIdResponse(
                metaResponse = MetaResponse(
                    "false",
                    "Komputer tidak ditemukan"
                ),
                null
            )
        )
    }
}

fun Route.getFoodsList() {
    get("/query_foods") {
        val result = transaction {
            makanan.join(
                otherTable = kategori_makanan,
                joinType = JoinType.INNER,
                onColumn = makanan.kategori_id,
                otherColumn = kategori_makanan.kategori_id
            ).selectAll().map {
                makanan.toMakananModel(it)
            }
        }

        call.respond(
            FoodListResponse(
                MetaResponse("true", "Query all food success"),
                result
            )
        )
    }
}

fun Route.getFoodById() {
    post("/query_food_by_makananid"){
        val body = call.receive<FoodRequest>()

        transaction {
            makanan.join(
                otherTable = kategori_makanan,
                joinType = JoinType.INNER,
                onColumn = makanan.kategori_id,
                otherColumn = kategori_makanan.kategori_id
            ).select {
                makanan.makanan_id eq body.makanan_id
            }.firstOrNull()
        }?.let {
            call.respond(
                FoodByIdResponse(
                    MetaResponse("true", "Query makanan sukses"),
                    makanan.toMakananModel(it)
                )
            )
            return@post
        }

        call.respond(
            FoodByIdResponse(
                MetaResponse("false", "Query makanan tidak ditemukan"),
                null
            )
        )
    }
}