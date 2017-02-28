package com.ykode.coral.core

import java.util.*

fun <I, S> infoFrom(id: I, event: Event<S>, version: Int, timestamp: Date = Date()) =
    EventInfo.newBuilder(id, event, version).setDate(timestamp).build()

fun <I, S> infoFrom(command: Command<S>, id: I?, targetVersion: Int? = null) =
    CommandInfo.newBuilder<I, S>(command).setEntityId(id).setTargetVersion(targetVersion?:0).build()