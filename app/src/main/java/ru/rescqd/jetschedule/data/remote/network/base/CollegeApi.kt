package ru.rescqd.jetschedule.data.remote.network.base

import org.json.JSONObject
import ru.rescqd.jetschedule.data.remote.network.entity.BackParamRequestEntity
import ru.rescqd.jetschedule.data.remote.network.entity.CheckFileRequestEntity
import ru.rescqd.jetschedule.data.remote.network.entity.GenerateDataRequestEntity


interface CollegeApi {

    suspend fun checkFile(payload: CheckFileRequestEntity): String

    suspend fun backParam(payload: BackParamRequestEntity): JSONObject

    suspend fun generateData(payload: GenerateDataRequestEntity): String

}

