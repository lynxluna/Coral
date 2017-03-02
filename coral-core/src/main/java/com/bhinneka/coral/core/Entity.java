package com.bhinneka.coral.core;

import javax.annotation.Nonnull;

/**
 * Entity is the combination of identity and state.
 *
 * @param <I> the type of the identity
 * @param <S> the type of the state
 */
public class Entity<I, S> {
  private I id;
  private S state;
  private int version;

  /**
   * Constructs an entity state.
   *
   * @param id the entity identity
   * @param state the state.
   * @param version the version of the entity.
   */
  public Entity(@Nonnull I id, @Nonnull S state, int version) {
    this.id = id;
    this.state = state;
    this.version = version;
  }

  /**
   * The equality operator, it only compares the identity.
   *
   * @param o the other entity.
   * @return equality.
   */
  @Override
  public boolean equals(Object o) {
    return this.id == ((Entity<?, ?>) o).id;
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  /**
   * Getting the identity.
   *
   * @return entity identity.
   */
  public I getId() {
    return id;
  }

  /**
   * Getting the state.
   *
   * @return entity state.
   */
  public S getState() {
    return state;
  }

  /**
   * Getting entity version.
   *
   * @return entity version.
   */
  public int getVersion() {
    return version;
  }
}
