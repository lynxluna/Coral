package com.ykode.coral.core

import com.ykode.coral.core.handlers.AsyncHandler;

/**
 * Event store load extension
 *
 * @param I the Entity ID type
 * @param S the state type
 * @param id the entity id to be loaded
 * @param success the success callback
 * @prama error the error callback
 */
fun <I, S> EventStore<I, S>.load(id: I, success: (List<EventInfo<I, S>>) -> Unit, error: (Throwable) -> Unit) =
  this.load(id, object : AsyncHandler<List<EventInfo<I, S>>> {
    override fun onError(exception: Exception) = error(exception)
    override fun onSuccess(result: List<EventInfo<I, S>>) = success(result)
  })

fun <I, S> EventStore<I, S>.load(id: I, version: Int, success: (List<EventInfo<I, S>>) -> Unit, error: (Throwable) -> Unit) =
    this.load(id, version, object : AsyncHandler<List<EventInfo<I, S>>> {
      override fun onError(exception: Exception) = error(exception)
      override fun onSuccess(result: List<EventInfo<I, S>>) = success(result)
    })

fun <I, S> EventStore<I, S>.commit(info: EventInfo<I, S>, success: (I) -> Unit, error: (Throwable) -> Unit) =
    this.commit(info, object : AsyncHandler<I> {
      override fun onError(exception: Exception) = error(exception)
      override fun onSuccess(result: I) = success(result)
    })