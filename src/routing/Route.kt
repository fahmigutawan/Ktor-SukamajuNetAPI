package com.example.routing

import com.example.data.model.ComputerModel
import com.example.data.model.FoodModel
import com.example.data.model.PedagangModel
import com.example.data.receive_request.*
import com.example.data.send_response.*
import com.example.model.receive_request.LoginRequest
import com.example.model.send_response.LoginResponse
import com.example.model.send_response.MetaResponse
import com.example.util.ResultSetConvert
import com.example.util.TokenManager
import com.example.util.connectToDatabase
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.PreparedStatement

fun Route.mainRoute() {
    get("/") {
        call.respondText("Welcome to Sukamaju Net API Service")
    }
}

fun Route.userLoginRoute(path: String = "/user_login") {
    post(path) {
        val body = call.receive<LoginRequest>()
        val username = body.username
        val password = body.password

        connectToDatabase(
            onError = {
                call.respond(
                    LoginResponse(
                        MetaResponse(
                            "false",
                            it.message ?: ""
                        ),
                        ""
                    )
                )
            }
        ) { conn ->
            // Get the count
            val usernameCount = "SELECT COUNT(*) as count " +
                    "FROM customer " +
                    "WHERE username=?"
            val usernameCountStatement: PreparedStatement = conn.prepareStatement(usernameCount)
            usernameCountStatement.setString(1, username)
            val resultCount = usernameCountStatement.executeQuery()

            // Get the data
            val command = "SELECT * " +
                    "FROM customer " +
                    "WHERE username=?"
            val statement: PreparedStatement = conn.prepareStatement(command)
            statement.setString(1, username)
            val result = statement.executeQuery()

            // Operation
            if (resultCount.next() && result.next()) {
                if (resultCount.getInt("count") > 0) {
                    if (result.getString("password") == password) {
                        call.respond(
                            LoginResponse(
                                MetaResponse(
                                    "true",
                                    ""
                                ),
                                TokenManager
                                    .generateJwtToken(
                                        result.getString("username"),
                                        result.getString("customer_id")
                                    )
                            )
                        )
                    } else {
                        // Wrong password
                        call.respond(
                            LoginResponse(
                                MetaResponse(
                                    "false",
                                    "Pasword salah"
                                ),
                                ""
                            )
                        )
                    }
                } else {
                    // No user found
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
            } else {
                // No user found
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
    }
}

fun Route.adminLoginRoute(path: String = "/admin_login") {
    post(path) {
        val body = call.receive<LoginRequest>()
        val username = body.username
        val password = body.password

        connectToDatabase(
            onError = {
                call.respond(
                    LoginResponse(
                        MetaResponse(
                            "false",
                            it.message ?: ""
                        ),
                        ""
                    )
                )
            },
            onConnect = { conn ->
                // Get the count
                val usernameCount = "SELECT COUNT(*) as count " +
                        "FROM admin " +
                        "WHERE username=?"
                val usernameCountStatement = conn.prepareStatement(usernameCount)
                usernameCountStatement.setString(1, username)
                val resultCount = usernameCountStatement.executeQuery()

                // Get the data
                val command = "SELECT * " +
                        "FROM admin " +
                        "WHERE username=?"
                val statement = conn.prepareStatement(command)
                statement.setString(1, username)
                val result = statement.executeQuery()

                // Operation
                if (resultCount.next() && result.next()) {
                    if (resultCount.getInt("count") > 0) {
                        if (result.getString("password") == password) {
                            // Success
                            call.respond(
                                LoginResponse(
                                    MetaResponse(
                                        "true",
                                        ""
                                    ),
                                    TokenManager
                                        .generateJwtToken(
                                            result.getString("username"),
                                            result.getString("pegawai_id")
                                        )
                                )
                            )
                        } else {
                            // Wrong password
                            call.respond(
                                LoginResponse(
                                    MetaResponse(
                                        "false",
                                        "Password salah"
                                    ),
                                    ""
                                )
                            )
                        }
                    } else {
                        // No user found
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
                } else {
                    // No user found
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
        )
    }
}

fun Route.getUserInfo(path: String = "/query_own_user") {
    get(path) {
        val principal = call.principal<JWTPrincipal>()
        val user_id = principal!!.payload.getClaim("user_id").asString()

        connectToDatabase(
            onError = {
                call.respond(
                    CustomerInfoResponse(
                        metaResponse = MetaResponse(
                            "true",
                            it.message ?: ""
                        ),
                        data = null
                    )
                )
            },
            onConnect = { conn ->
                val query = "select * " +
                        "from customer_information " +
                        "where customer_id=?"
                val statement = conn.prepareStatement(query)
                statement.setString(1, user_id)
                val res = statement.executeQuery()

                if (res.next()) {
                    val result = ResultSetConvert.toUserInfo(res)

                    call.respond(
                        CustomerInfoResponse(
                            metaResponse = MetaResponse(
                                "true",
                                "Query own user success"
                            ),
                            data = result
                        )
                    )
                } else {
                    call.respond(
                        CustomerInfoResponse(
                            metaResponse = MetaResponse(
                                "true",
                                "User tidak ditemukan"
                            ),
                            data = null
                        )
                    )
                }
            }
        )
    }
}

fun Route.getUserInfoById(path: String = "/query_user_by_id/{user_id}") {
    post(path) {
        val user_id = call.parameters["user_id"]

        connectToDatabase(
            onError = {
                call.respond(
                    CustomerInfoResponse(
                        metaResponse = MetaResponse(
                            "true",
                            it.message ?: ""
                        ),
                        data = null
                    )
                )
            },
            onConnect = { conn ->
                val query = "select * " +
                        "from customer_information " +
                        "where customer_id=?"
                val statement = conn.prepareStatement(query)
                statement.setString(1, user_id)
                val res = statement.executeQuery()

                if (res.next()) {
                    val result = ResultSetConvert.toUserInfo(res)

                    call.respond(
                        CustomerInfoResponse(
                            metaResponse = MetaResponse(
                                "true",
                                "Query own user success"
                            ),
                            data = result
                        )
                    )
                } else {
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
        )
    }
}

fun Route.getAdminInfo(path: String = "/query_own_admin") {
    get(path) {
        val principal = call.principal<JWTPrincipal>()
        val user_id = principal!!.payload.getClaim("user_id").asString()

        connectToDatabase(
            onError = {
                call.respond(
                    PegawaiInfoResponse(
                        metaResponse = MetaResponse(
                            "false",
                            it.message ?: ""
                        ),
                        data = null
                    )
                )
            },
            onConnect = { conn ->
                val query = "select * " +
                        "from pegawai_information " +
                        "where pegawai_id=?"
                val statement = conn.prepareStatement(query)
                statement.setString(1, user_id)
                val res = statement.executeQuery()

                if (res.next()) {
                    val result = ResultSetConvert.toAdminInfo(res)

                    call.respond(
                        PegawaiInfoResponse(
                            metaResponse = MetaResponse(
                                "true",
                                "Query admin sukses"
                            ),
                            data = result
                        )
                    )
                } else {
                    call.respond(
                        PegawaiInfoResponse(
                            metaResponse = MetaResponse(
                                "false",
                                "Username admin tidak ditemukan"
                            ),
                            data = null
                        )
                    )
                }
            }
        )
    }
}

fun Route.getPegawaiById(path: String = "/query_pegawai_by_id/{peg_id}") {
    post(path) {
        val pegawai_id = call.parameters["peg_id"]

        connectToDatabase(
            onError = {
                call.respond(
                    PegawaiInfoResponse(
                        metaResponse = MetaResponse(
                            "false",
                            it.message ?: ""
                        ),
                        data = null
                    )
                )
            },
            onConnect = { conn ->
                val query = "select * " +
                        "from pegawai_information " +
                        "where pegawai_id=?"
                val statement = conn.prepareStatement(query)
                statement.setString(1, pegawai_id)
                val res = statement.executeQuery()

                if (res.next()) {
                    val result = ResultSetConvert.toAdminInfo(res)

                    call.respond(
                        PegawaiInfoResponse(
                            metaResponse = MetaResponse(
                                "true",
                                "Query pegawai sukses"
                            ),
                            data = result
                        )
                    )
                } else {
                    call.respond(
                        PegawaiInfoResponse(
                            metaResponse = MetaResponse(
                                "false",
                                "Pegawai tidak ditemukan"
                            ),
                            data = null
                        )
                    )
                }
            }
        )
    }
}

fun Route.getComputersList(path: String = "/query_computers") {
    get(path) {
        val result = ArrayList<ComputerModel>()

        connectToDatabase(
            onError = {
                call.respond(
                    ComputerListResponse(
                        MetaResponse(
                            "false",
                            it.message ?: ""
                        ),
                        result
                    )
                )
            },
            onConnect = { conn ->
                val query = "select pc.*, kat.kategori_word " +
                        "from komputer pc " +
                        "join kategori_komputer kat " +
                        "on kat.kategori_id = pc.kategori_id"
                val statement = conn.createStatement()
                val res = statement.executeQuery(query)

                while (res.next()) {
                    result.add(
                        ResultSetConvert.toComputerModel(res)
                    )
                }

                call.respond(
                    ComputerListResponse(
                        MetaResponse(
                            "true",
                            "Query computers sukses"
                        ),
                        result
                    )
                )
            }
        )
    }
}

fun Route.getComputerById(path: String = "query_computers_by_id/{pc_id}") {
    post(path) {
        val komputer_id = call.parameters["pc_id"]

        connectToDatabase(
            onError = {
                call.respond(
                    ComputerByIdResponse(
                        MetaResponse(
                            "false",
                            it.message ?: ""
                        ),
                        null
                    )
                )
            },
            onConnect = { conn ->
                val query = "select pc.*, kat.kategori_word " +
                        "from komputer pc " +
                        "join kategori_komputer kat " +
                        "on kat.kategori_id = pc.kategori_id " +
                        "where pc.komputer_id=?"
                val statement = conn.prepareStatement(query)
                statement.setString(1, komputer_id)
                val res = statement.executeQuery()

                if (res.next()) {
                    call.respond(
                        ComputerByIdResponse(
                            MetaResponse(
                                "true",
                                "Query komputer sukses"
                            ),
                            ResultSetConvert.toComputerModel(res)
                        )
                    )
                } else {
                    call.respond(
                        ComputerByIdResponse(
                            MetaResponse(
                                "false",
                                "Query komputer gagal"
                            ),
                            null
                        )
                    )
                }
            }
        )
    }
}

fun Route.getFoodsList(path: String = "/query_foods") {
    get(path) {
        val result = ArrayList<FoodModel>()

        connectToDatabase(
            onError = {
                call.respond(
                    FoodListResponse(
                        MetaResponse(
                            "false",
                            it.message ?: ""
                        ),
                        result
                    )
                )
            },
            onConnect = { conn ->
                val query = "select mkn.*, kat.kategori_word " +
                        "from makanan mkn " +
                        "join kategori_makanan kat " +
                        "on kat.kategori_id = mkn.kategori_id"
                val statement = conn.prepareStatement(query)
                val res = statement.executeQuery()

                while (res.next()) {
                    result.add(ResultSetConvert.toFoodModel(res))
                }

                call.respond(
                    FoodListResponse(
                        MetaResponse(
                            "true",
                            "Query makanan berhasil"
                        ),
                        result
                    )
                )
            }
        )
    }
}

fun Route.getFoodById(path: String = "/query_food_by_id/{food_id}") {
    post(path) {
        val makanan_id = call.parameters["food_id"]

        connectToDatabase(
            onError = {
                call.respond(
                    FoodByIdResponse(
                        MetaResponse(
                            "false",
                            it.message ?: ""
                        ),
                        null
                    )
                )
            },
            onConnect = { conn ->
                val query = "select mkn.*, kat.kategori_word " +
                        "from makanan mkn " +
                        "join kategori_makanan kat " +
                        "on kat.kategori_id = mkn.kategori_id " +
                        "where mkn.makanan_id=?"
                val statement = conn.prepareStatement(query)
                statement.setString(1, makanan_id)
                val res = statement.executeQuery()

                if (res.next()) {
                    call.respond(
                        FoodByIdResponse(
                            MetaResponse(
                                "true",
                                "Query makanan sukses"
                            ),
                            ResultSetConvert.toFoodModel(res)
                        )
                    )
                } else {
                    call.respond(
                        FoodByIdResponse(
                            MetaResponse(
                                "false",
                                "Query makanan gagal"
                            ),
                            null
                        )
                    )
                }


            }
        )
    }
}

fun Route.makeComputerTransaction(path: String = "/make_computer_transaction") {
    post(path) {
        val body = call.receive<ComputerTransactionRequest>()

        connectToDatabase(
            onError = {
                call.respond(
                    ComputerTransactionResponse(
                        MetaResponse(
                            "false",
                            it.message ?: "Transaksi komputer gagal"
                        )
                    )
                )
            },
            onConnect = { conn ->
                val query = "begin try " +
                        "begin transaction " +
                        "if((select status from komputer where komputer_id=?) = 'not ready') " +
                        "rollback transaction " +
                        "else  " +
                        "update komputer set status='not ready' where komputer_id=? " +
                        "" +
                        "declare @count int " +
                        "set @count = (select count(*) from komputer) " +
                        "" +
                        "insert into order_komputer values( " +
                        "concat('order-PC',@count), " +
                        "?, " +
                        "?, " +
                        "?, " +
                        "'1' " +
                        ") " +
                        "commit transaction " +
                        "end try " +
                        "begin catch " +
                        "rollback transaction " +
                        "end catch "

                val statement = conn.prepareStatement(query)
                statement.setString(1, body.komputer_id)
                statement.setString(2, body.komputer_id)
                statement.setString(3, body.customer_id)
                statement.setString(4, body.komputer_id)
                statement.setString(5, body.harga.toString())
                statement.executeUpdate()

                call.respond(
                    ComputerTransactionResponse(
                        MetaResponse(
                            "true",
                            "Transaksi komputer berhasil"
                        )
                    )
                )
            }
        )
    }
}

fun Route.endComputerTransaction(path: String = "/end_computer_transaction/trx_id={transaction_id}") {
    post(path) {
        val principal = call.principal<JWTPrincipal>()
        val user_id = principal!!.payload.getClaim("user_id").asString()
        val trx_id = call.parameters["transaction_id"]

        connectToDatabase(
            onError = {
                call.respond(
                    ComputerTransactionResponse(
                        MetaResponse(
                            "false",
                            it.message ?: ""
                        )
                    )
                )
            },
            onConnect = { conn ->
                val query = "begin try " +
                        "begin transaction " +
                        "declare @komputer_id varchar(5) " +
                        "if(select customer_id from order_komputer where order_id = '?') != ?" +
                        "rollback transaction" +
                        "set @komputer_id = (" +
                        "select pc.komputer_id from order_komputer ord join komputer pc on pc.komputer_id = ord.komputer_id where ord.order_id = ? " +
                        ")" +
                        "update komputer set status = 'ready' where komputer_id = @komputer_id " +
                        "update order_komputer set status = '3' where order_id = ? " +
                        "commit transaction " +
                        "end try " +
                        "begin catch " +
                        "rollback transaction " +
                        "end catch "
                val statement = conn.prepareStatement(query)
                statement.setString(1, user_id)
                statement.setString(2, trx_id)
                statement.setString(3, trx_id)

                statement.executeUpdate()

                call.respond(
                    ComputerTransactionResponse(
                        MetaResponse(
                            "true",
                            "Transaksi berhasil diakhiri"
                        )
                    )
                )
            }
        )
    }
}

fun Route.getPedagangInfoById(path:String = "/query_pedagang_by_id/{pdg_id}"){
    post(path) {
        val pedagang_id = call.parameters["pdg_id"]

        connectToDatabase(
            onError = {
                call.respond(
                    PedagangByIdResponse(
                        MetaResponse(
                            "false",
                            it.message ?: ""
                        ),
                        null
                    )
                )
            },
            onConnect = { conn ->
                val query = "select * " +
                        "from pedagang_information " +
                        "where pedagang_id=?"
                val statement = conn.prepareStatement(query)
                statement.setString(1, pedagang_id)
                val res = statement.executeQuery()

                if(res.next()){
                    call.respond(
                        PedagangByIdResponse(
                            MetaResponse(
                                "true",
                                "Query pedagang sukses"
                            ),
                            PedagangModel(
                                pedagang_id = res.getString("pedagang_id"),
                                stand_name = res.getString("stand_name"),
                                stand_number = res.getString("stand_number")
                            )
                        )
                    )
                }else{
                    call.respond(
                        PedagangByIdResponse(
                            MetaResponse(
                                "false",
                                "Pedagang tidak ditemukan"
                            ),
                            null
                        )
                    )
                }
            }
        )
    }
}

fun Route.getAllPegawai(path: String = "/query_all_pegawai") {

}
