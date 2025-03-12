package code.challenge.observer;

import org.jetbrains.annotations.NotNull;

public interface Observer<Subject, Delta> {
    void update(@NotNull Subject subject, @NotNull Delta delta);
}
