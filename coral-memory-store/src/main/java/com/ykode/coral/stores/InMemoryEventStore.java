package com.ykode.coral.stores;

import static com.ykode.coral.stores.CollectionUtilities.Predicate;
import static com.ykode.coral.stores.CollectionUtilities.filter;

import com.ykode.coral.core.EventInfo;
import com.ykode.coral.core.EventStore;
import com.ykode.coral.core.handlers.AsyncHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.annotation.Nonnull;



public class InMemoryEventStore<I, S> implements EventStore<I, S> {
  private List<EventInfo<I, S>> storage = new ArrayList<EventInfo<I, S>>();

  @Override
  public void load(final I id, final AsyncHandler<List<EventInfo<I, S>>> handler) {
    if (storage.size() == 0) {
      handler.onSuccess(new ArrayList<EventInfo<I, S>>());
    } else {
      final List<EventInfo<I, S>> eventInfos = filter(storage, new Predicate<EventInfo<I, S>>() {
        @Override
        public boolean apply(EventInfo<I, S> type) {
          return type.getEntityId().equals(id);
        }
      });

      Collections.sort(eventInfos, new Comparator<EventInfo<I, S>>() {
        @Override
        public int compare(EventInfo<I, S> o1, EventInfo<I, S> o2) {
          if (o1.getVersion() < o2.getVersion()) {
            return -1;
          } else if (o1.getVersion() > o2.getVersion()) {
            return 1;
          } else {
            return 0;
          }
        }
      });

      handler.onSuccess(eventInfos);
    }
  }

  @Override
  public void load(final I id,
                   final int version,
                   final AsyncHandler<List<EventInfo<I, S>>> handler) {
    
    this.load(id, new AsyncHandler<List<EventInfo<I, S>>>() {
      @Override
      public void onError(@Nonnull Exception exception) {
        handler.onError(exception);
      }

      @Override
      public void onSuccess(@Nonnull List<EventInfo<I, S>> result) {
        final List<EventInfo<I, S>> infos = filter(result, new Predicate<EventInfo<I, S>>() {
          @Override
          public boolean apply(EventInfo<I, S> type) {
            return type.getVersion() <= version;
          }
        });
        handler.onSuccess(infos);
      }
    });
  }

  @Override
  public void commit(final EventInfo<I, S> eventInfo, final AsyncHandler<I> handler) {
    storage.add(eventInfo);
    handler.onSuccess(eventInfo.getEntityId());
  }
}
