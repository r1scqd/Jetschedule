/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.rescqd.jetschedule.ui.components

import androidx.annotation.StringRes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.*

data class Message(
    val id: Long = UUID.randomUUID().mostSignificantBits,
    @StringRes val messageId: Int? = null,
    @StringRes val actionId: Int? = null,
    val performAction: () -> Unit = {},
    val dismissAction: () -> Unit = {},
    val messageText: String? = null
)

/**
 * Class responsible for managing Snackbar messages to show on the screen
 */
object SnackbarManager {
    private val _messages: MutableStateFlow<List<Message>> = MutableStateFlow(emptyList())
    val messages: StateFlow<List<Message>> get() = _messages.asStateFlow()

    fun showMessage(@StringRes messageTextId: Int, id: Long? = null) {
        _messages.update { currentMessages ->
            currentMessages + Message(
                id = id ?: UUID.randomUUID().mostSignificantBits,
                messageId = messageTextId
            )
        }
    }

    fun showMessage(messageText: String, id: Long? = null){
        _messages.update { currentMessages ->
            currentMessages + Message(
                id = id ?: UUID.randomUUID().mostSignificantBits,
                messageText = messageText
            )
        }
    }

    fun showMessage(message: Message) {
        _messages.update {
            it + message
        }
    }

    fun setMessageShown(messageId: Long) {
        _messages.update { currentMessages ->
            currentMessages.filterNot { it.id == messageId }
        }
    }
}
