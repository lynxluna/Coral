package com.bhinneka.coral.core;

import com.bhinneka.coral.core.handlers.AsyncHandler;

import java.util.List;

/**
 * Implement this store to persist and load {@link EventInfo EventInfo}.
 *
 * @param <I> entity id.
 * @param <S> the state of the entity.
 */
public interface EventStore<I, S> {

  /**
   * Loads all events for and entity of specific id.
   *
   * @param id the entity id to query
   * @param handler the handler of the load operation
   */
  void load(I id, AsyncHandler<List<EventInfo<I, S>>> handler);

  /**
   * Loads all events of an entity to specific version.
   *
   * @param id the entity id.
   * @param version the last version queried.
   * @param handler the handler of load operation.
   */
  void load(I id, int version, AsyncHandler<List<EventInfo<I, S>>> handler);

  /**
   * Commits an event to the event store.
   *  @param eventInfo event information to be persisted.
   * @param handler the handler of the commit operation
   */
  void commit(EventInfo<I, S> eventInfo, AsyncHandler<I> handler);
}
