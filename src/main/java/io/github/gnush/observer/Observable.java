package io.github.gnush.observer;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CopyOnWriteArrayList;

public abstract class Observable<Subject, Delta> {
    private final CopyOnWriteArrayList<Observer<Subject, Delta>> observers;

    public Observable() {
        observers = new CopyOnWriteArrayList<>();
    }

    public void attach(@NotNull Observer<Subject, Delta> observer) {
        observers.addIfAbsent(observer);
    }

    public void detach(@NotNull Observer<Subject, Delta> observer) {
        observers.remove(observer);
    }

    public void notifyObservers(@NotNull Subject subject, Delta delta) {
        observers.forEach(obs -> obs.update(subject, delta));
    }
}
