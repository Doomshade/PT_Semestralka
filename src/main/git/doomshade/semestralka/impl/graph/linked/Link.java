package main.git.doomshade.semestralka.impl.graph.linked;

import main.git.doomshade.semestralka.adt.IValueHolder;


/**
 * @param <T>
 * @deprecated již není třeba, zatím to tu ale keepnu pro jistotu
 */
@Deprecated
public class Link<T extends Comparable<T>> {
    IValueHolder holder;
    T neighbour;
    Link<T> next;

    Link(IValueHolder holder, T neighbour, Link<T> next) {
        this.holder = holder;
        this.neighbour = neighbour;
        this.next = next;
    }
}
