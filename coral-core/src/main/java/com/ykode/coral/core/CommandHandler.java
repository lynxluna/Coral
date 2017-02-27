package com.ykode.coral.core;

import com.ykode.coral.core.handlers.AsyncHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.annotation.Nonnull;

/**
 * Command Handler classe, it execute commands.
 *
 * @param <I> Identity Type
 * @param <S> State Type
 */
public final class CommandHandler<I, S> {
  private final Aggregate<S> aggregate;
  private final EventStore<I, S> store;
  private final IdGenerator<I> idGenerator;

  /**
   * Creates command handler. Needs aggregate, store, and identity generator.
   *
   * @param aggregate the aggregate behaviour.
   * @param store the event store.
   * @param idGenerator the entity id generator.
   */
  public CommandHandler(@Nonnull Aggregate<S> aggregate,
                        @Nonnull EventStore<I, S> store,
                        @Nonnull IdGenerator<I> idGenerator) {
    this.aggregate = aggregate;
    this.store = store;
    this.idGenerator = idGenerator;
  }

  /**
   * Handles command executed against an entity.
   *
   * @param id Entity Identity.
   * @param command command to be handled.
   * @param handler the handler of type {@link AsyncHandler} to catch the result of execution.
   */
  public void handle(final I id, final @Nonnull Command<S> command,
              final AsyncHandler<List<EventInfo<I, S>>> handler) {
    if (null == id) {
      try {
        final List<Event<S>> evts = aggregate.exec(aggregate.getZero(), command);
        final I newId = idGenerator.nextValue();

        final List<EventInfo<I, S>> results = new ArrayList<EventInfo<I, S>>();
        for (int i = 0; i < evts.size(); ++i) {
          results.add(EventInfo.newBuilder(newId, evts.get(i), i).build());
        }

        handler.onSuccess(results);
      } catch (Exception e) {
        handler.onError(e);
      }
    } else {
      try {
        final AsyncHandler<List<EventInfo<I, S>>> loadHandler =
            new AsyncHandler<List<EventInfo<I, S>>>() {
              @Override
              public void onError(@Nonnull Exception exception) {
                handler.onError(exception);
              }

              @Override
              public void onSuccess(@Nonnull List<EventInfo<I, S>> infos) {
                Collections.sort(infos, new Comparator<EventInfo<I, S>>() {
                  @Override
                  public int compare(EventInfo<I, S> o1, EventInfo<I, S> o2) {
                    if (o1.getVersion() < o1.getVersion()) {
                      return -1;
                    } else if (o1.getVersion() > o1.getVersion()) {
                      return 1;
                    } else {
                      return 0;
                    }
                  }
                });

                S state = aggregate.getZero();
                for (final EventInfo<I, S> info : infos) {
                  state = aggregate.apply(state, info.getEvent());
                }

                final int lastVer = infos.get(infos.size() - 1).getVersion();
                final List<EventInfo<I, S>> eventInfos = new ArrayList<EventInfo<I, S>>();
                try {
                  List<Event<S>> results = aggregate.exec(state, command);

                  for (int i = 0; i < results.size(); ++i) {
                    eventInfos.add(EventInfo.newBuilder(id,
                        results.get(i), lastVer + i + 1).build());
                  }
                  handler.onSuccess(eventInfos);
                } catch (Exception e) {
                  handler.onError(e);
                }
            }
        };
        store.load(id, loadHandler);
      } catch (Exception e) {
        handler.onError(e);
      }
    }
  }
}
