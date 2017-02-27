package com.ykode.coral.core

import org.junit.Test
import com.nhaarman.mockito_kotlin.*
import com.ykode.coral.core.handlers.AsyncHandler
import java.util.*

data class Person(val name: String, val age: Int)
data class PersonCreated(val name: String, val age: Int) : Event<Person>
data class PersonNameChanged(val name: String) : Event<Person>

class EventStoreTest {

  @Test
  fun testEventStoreExtensions() {

    // Given
    val store  = mock<EventStore<UUID, Person>>()
    val successReceiver = mock<(List<EventInfo<UUID, Person>>) -> Unit>()
    val failureReceiver = mock<(Throwable) -> Unit>()

    val fakeId = UUID.fromString("1D86295B-0213-4780-B52F-5616F794DABE")
    val personCreated = PersonCreated("Didiet", 22)
    val personNameChanged = PersonNameChanged("Didiet Noor")

    val info1 = EventInfo.newBuilder(fakeId, personCreated, 0).build()
    val info2 = EventInfo.newBuilder(fakeId, personNameChanged, 1).build()
    val infos = listOf(info1, info2)

    doAnswer {
      @Suppress("UNCHECKED")
      val handler = it.arguments[1] as AsyncHandler<List<EventInfo<UUID, Person>>>
      handler.onSuccess(infos)
    }.whenever(store).load(eq(fakeId), any())

    doAnswer {
      @Suppress("UNCHECKED")
      val handler = it.arguments[2] as AsyncHandler<List<EventInfo<UUID, Person>>>
      handler.onSuccess(listOf(info1))
    }.whenever(store).load(eq(fakeId), eq(0), any())

    // When
    store.load(fakeId, successReceiver, failureReceiver)

    // Then
    verify(successReceiver, times(1)).invoke(eq(infos))
    verify(failureReceiver, never()).invoke(any())

    reset(successReceiver)
    reset(failureReceiver)

    // When

    store.load(fakeId, 0, successReceiver, failureReceiver)

    // Then
    verify(successReceiver, times(1)).invoke(eq(listOf(info1)))
    verify(failureReceiver, never()).invoke(any())

  }
}