package io.getcoral.core;

import io.getcoral.core.util.ValueObject;

/**
 * Command information. Contains command information to be executed. This is also a {@link ValueObject}
 * instance. This class is immutable. To create an instance of this class you need to invoke
 *
 * @param <I>
 * @param <S>
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

  public I getEntityId() {
    return entityId;
  }

  public Command<S> getCommand() {
    return command;
  }

  public int getTargetVersion() {
    return targetVersion;
  }

  @Override
  public Builder CopyBuilder() {
    return new Builder(entityId, command, targetVersion);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    CommandInfo<?, ?> that = (CommandInfo<?, ?>) o;

    if (targetVersion != that.targetVersion) return false;
    if (entityId != null ? !entityId.equals(that.entityId) : that.entityId != null) return false;
    return command.equals(that.command);
  }

  @Override
  public int hashCode() {
    int result = entityId != null ? entityId.hashCode() : 0;
    result = 31 * result + command.hashCode();
    result = 31 * result + targetVersion;
    return result;
  }

  public class Builder {
    private I entityId;
    private Command<S> command;
    private int targetVersion;

    private Builder(I entityId, Command<S> command, int targetVersion) {
      this.entityId = entityId;
      this.command = command;
      this.targetVersion = targetVersion;
    }

    private Builder(Command<S> command) {
      this.command = command;
    }

    public Builder setEntityId(I entityId) {
      this.entityId = entityId; return this;
    }

    public Builder setCommand(Command<S> command) {
      this.command = command; return this;
    }

    public Builder setTargetVersion(int targetVersion) {
      this.targetVersion = targetVersion; return this;
    }
  }
}
