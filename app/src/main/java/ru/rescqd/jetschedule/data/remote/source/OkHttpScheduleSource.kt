package ru.rescqd.jetschedule.data.remote.source

import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import ru.rescqd.jetschedule.data.remote.network.base.CollegeApi
import ru.rescqd.jetschedule.data.remote.network.entity.BackParamRequestEntity
import ru.rescqd.jetschedule.data.remote.network.entity.CheckFileRequestEntity
import ru.rescqd.jetschedule.data.remote.network.entity.GenerateDataRequestEntity
import ru.rescqd.jetschedule.data.remote.okhttp.OkHttpConfig


class OkHttpScheduleSource(
    config: OkHttpConfig,
): BaseOkHttpSource(config), CollegeApi {
    override suspend fun checkFile(payload: CheckFileRequestEntity): String {

        val req = Request.Builder()
            .post(payload.toString().toRequestBody(contentType))
            .endpoint("/chek_file.php")
            .build()
        return client.newCall(req).suspendEnqueue().body.string()
    }

    override suspend fun backParam(payload: BackParamRequestEntity): JSONObject {

        val req = Request.Builder()
            .post(payload.toString().toRequestBody(contentType))
            .endpoint("/back_parametr.php")
            .build()
        return client.newCall(req).suspendEnqueue().parseJsonResponse()
    }

    override suspend fun generateData(payload: GenerateDataRequestEntity): String {

        val req = Request.Builder()
            .post(payload.toString().toRequestBody(contentType))
            .endpoint("/generate_data.php")
            .build()
        return client.newCall(req).suspendEnqueue().body.string()
    }
}