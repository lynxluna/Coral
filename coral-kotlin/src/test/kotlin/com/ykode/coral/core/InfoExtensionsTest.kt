package com.ykode.coral.core

import org.junit.Test
import java.util.*
import kotlin.test.assertEquals


class InfoExtensionsTest {

  @Test
  fun testEventInfoExtension() {
    val fakeId = UUID.fromString("1D86295B-0213-4780-B52F-5616F794DABE")
    val personCreated = PersonCreated("Didiet", 22)
    val stamp = Date()

    val i = EventInfo.newBuilder(fakeId, personCreated, 0).setDate(stamp).build()
    val info = infoFrom(fakeId, personCreated, 0, stamp)

    assertEquals(i, info)
  }

  @Test
  fun testCommandInfoExtension() {
    val fakeId = UUID.fromString("1D86295B-0213-4780-B52F-5616F794DABE")
    val createPerson = CreatePerson("Didiet", 22)

    val i = CommandInfo.newBuilder<UUID, Person>(createPerson).setEntityId(fakeId).build()
    val info = infoFrom(command = createPerson, id = fakeId)

    assertEquals(i, info)
  }
}

