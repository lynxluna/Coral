package com.bhinneka.coral.core;

import com.bhinneka.coral.core.util.ValueObject;

import java.util.Date;
import javax.annotation.Nonnull;

/**
 * An event info, containing about executed event information. This contains all
 * information needed to be persisted. This class is immutable, to create the instance
 * you need to call {@link #newBuilder(Object, Event, int) newBuilder}.
 *
 * @param <I> The type of id used by entity.
 * @param <S> The type of state in which {@link #event} is applied.
 */
public class EventInfo<I, S> implements ValueObject<EventInfo.Builder> {
  private final I entityId;
  private final Event<S> event;
  private final int version;
  private final Date date;

  private EventInfo(@Nonnull I entityId,
                    @Nonnull Event<S> event,
                    int version,
                    @Nonnull Date date) {
    this.entityId = entityId;
    this.event = event;
    this.version = version;
    this.date = date;
  }

  /**
   * Returns the entity id.
   *
   * @return entity id
   */
  public I getEntityId() {
    return entityId;
  }

  /**
   * Returns the event contained.
   *
   * @return event
   */
  public Event<S> getEvent() {
    return event;
  }

  /**
   * Returns the version in which event is contained.
   *
   * @return version
   */
  public int getVersion() {
    return version;
  }

  /**
   * Returns when the event is emitted.
   *
   * @return date
   */
  public Date getDate() {
    return date;
  }

  /**
   * Creates a new instance of {@link Builder}.
   *
   * @param <I> the type of entity id.
   * @param <S> the type of the state.
   * @param entityId The id of the entity where the event happened
   * @param event    The contained {@link Event}
   * @param version  The last version of the
   *                 entity where the @{@link Event} after the event emitted.
   * @return a new instance of {@link EventInfo}
   */
  public static <I, S> Builder<I, S> newBuilder(@Nonnull I entityId,
                                                @Nonnull Event<S> event,
                                                int version) {
    return new Builder<I, S>(entityId, event, version, new Date());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    EventInfo<?, ?> eventInfo = (EventInfo<?, ?>) o;

    if (version != eventInfo.version) {
      return false;
    }

    if (!entityId.equals(eventInfo.entityId)) {
      return false;
    }

    if (!event.equals(eventInfo.event)) {
      return false;
    }
    return date.equals(eventInfo.date);
  }

  @Override
  public int hashCode() {
    int result = entityId.hashCode();
    result = 31 * result + event.hashCode();
    result = 31 * result + version;
    result = 31 * result + date.hashCode();
    return result;
  }

  /**
   * The Builder for copying values and change some properties.
   */
  @Override
  public Builder<I, S> copyBuilder() {
    return new Builder<I, S>(entityId, event, version, date);
  }

  /**
   * Builder for {@link EventInfo}.
   */
  public static class Builder<I, S> {
    private I entityId;
    private Event<S> event;
    private int version;
    private Date date;

    Builder(@Nonnull I entityId, @Nonnull Event<S> event, int version, Date date) {
      this.entityId = entityId;
      this.event = event;
      this.version = version;
      this.date = date;
    }

    /**
     * Sets the Entity Id.
     *
     * @param entityId the entity id.
     * @return builder
     */
    public Builder setEntityId(@Nonnull I entityId) {
      this.entityId = entityId;
      return this;
    }

    /**
     * Sets the {@link Event Event}.
     *
     * @param event event to be set.
     * @return builder.
     */
    public Builder setEvent(@Nonnull Event<S> event) {
      this.event = event;
      return this;
    }

    /**
     * Sets the version.
     *
     * @param version entity version.
     * @return builder
     */
    public Builder setVersion(int version) {
      this.version = version;
      return this;
    }

    /**
     * Sets the date when {@link Event Event} is happened.
     *
     * @param date the date.
     * @return builder.
     */
    public Builder setDate(@Nonnull Date date) {
      this.date = date;
      return this;
    }

    /**
     * Builds The {@link EventInfo EventInfo} instance.
     *
     * @return an {@link EventInfo} instance.
     */
    public EventInfo<I, S> build() {
      return new EventInfo<I, S>(entityId, event, version, date);
    }
  }
}
