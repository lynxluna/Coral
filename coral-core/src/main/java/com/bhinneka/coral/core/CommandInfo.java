package com.bhinneka.coral.core;

import com.bhinneka.coral.core.util.ValueObject;

/**
 * Command information. Contains command information to be executed.
 * This is also a {@link ValueObject} instance. This class is immutable.
 * To create an instance of this class you need to invoke
 * {@link #newBuilder(Command) newBuilder}
 *
 * @param <I> The type of identity used by entity
 * @param <S> The type of the state in which {@link #command} is applied to
 */
public class CommandInfo<I, S> implements ValueObject<CommandInfo.Builder> {
  private final I entityId;
  private final Command<S> command;
  private final int targetVersion;

  private CommandInfo(I entityId, Command<S> command, int targetVersion) {
    this.entityId = entityId;
    this.command = command;
    this.targetVersion = targetVersion;
  }

  /**
   * Getting Entity ID.
   *
   * @return Entity Identity
   */
  public I getEntityId() {
    return entityId;
  }

  /**
   * Getting encapsulated command.
   *
   * @return command to be executed
   */
  public Command<S> getCommand() {
    return command;
  }

  /**
   * Getting target version in which command will be executed.
   *
   * @return entity target version
   */
  public int getTargetVersion() {
    return targetVersion;
  }

  /**
   * Creates new builder for {@link CommandInfo}.
   *
   * @param command the command to be included in command info
   * @param <I> Identity type
   * @param <S> State type
   * @return a builder instance for {@link CommandInfo} class.
   */
  public static <I, S> Builder<I, S> newBuilder(final Command<S> command) {
    return new Builder<I, S>(null, command, 0);
  }

  /**
   * Creating builder to copy current {@link CommandInfo} values.
   *
   * @return an CommandInfo builder.
   */
  @Override
  public Builder<I, S> copyBuilder() {
    return new Builder<I, S>(entityId, command, targetVersion);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    CommandInfo<?, ?> that = (CommandInfo<?, ?>) o;

    if (targetVersion != that.targetVersion) {
      return false;
    }

    if (entityId != null ? !entityId.equals(that.entityId) : that.entityId != null) {
      return false;
    }
    return command.equals(that.command);
  }

  @Override
  public int hashCode() {
    int result = entityId != null ? entityId.hashCode() : 0;
    result = 31 * result + command.hashCode();
    result = 31 * result + targetVersion;
    return result;
  }

  /**
   * Builder for Command Info
   *
   * @param <I> Entity identity type.
   * @param <S> State type.
   */
  public static class Builder<I, S> {
    private I entityId;
    private Command<S> command;
    private int targetVersion;

    private Builder(I entityId, Command<S> command, int targetVersion) {
      this.entityId = entityId;
      this.command = command;
      this.targetVersion = targetVersion;
    }

    /**
     * Setting entity id of the builder.
     *
     * @param entityId the entity id.
     * @return builder with new entity id.
     */
    public Builder setEntityId(I entityId) {
      this.entityId = entityId;
      return this;
    }

    /**
     * Setting command of the builder.
     *
     * @param command the command.
     * @return the builder with new command
     */
    public Builder setCommand(Command<S> command) {
      this.command = command;
      return this;
    }

    /**
     * Setting target version of the builder.
     *
     * @param targetVersion target version.
     * @return the builder with target version.
     */
    public Builder setTargetVersion(int targetVersion) {
      this.targetVersion = targetVersion;
      return this;
    }

    /**
     * Creates {@link CommandInfo instance}.
     *
     * @return CommandInfo instance.
     */
    public CommandInfo<I, S> build() {
      return new CommandInfo<I, S>(entityId, command, targetVersion);
    }
  }
}
