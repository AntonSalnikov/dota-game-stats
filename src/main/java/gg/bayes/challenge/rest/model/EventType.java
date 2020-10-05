package gg.bayes.challenge.rest.model;

import lombok.NonNull;

public enum EventType {

    ITEMS_BEING_PURCHASED,
    HEROES_KILLING_EACH_OTHER,
    SPELLS_BEING_CAST,
    DAMAGE_BEING_DONE,
    OTHER;

    /**
     * Defines the type of the event
     *
     * @param event to be parsed
     *
     * @return type of the event or {@link EventType#OTHER} if type was not properly
     * defined or unexpected
     *
     * @throws NullPointerException if {@code event} is {@code null}
     */
    public static EventType parse(@NonNull String event) {

        switch (event) {
            case "buys": return ITEMS_BEING_PURCHASED;
            case "is_killed": return HEROES_KILLING_EACH_OTHER;
            case "casts": return SPELLS_BEING_CAST;
            case "hits": return DAMAGE_BEING_DONE;
            default: return OTHER;
        }
    }
}
