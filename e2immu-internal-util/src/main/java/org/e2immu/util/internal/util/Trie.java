/*
 * e2immu: a static code analyser for effective and eventual immutability
 * Copyright 2020-2021, Bart Naudts, https://www.e2immu.org
 *
 * This program is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for
 * more details. You should have received a copy of the GNU Lesser General Public
 * License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.e2immu.util.internal.util;

import org.e2immu.annotation.FinalFields;
import org.e2immu.annotation.Modified;
import org.e2immu.annotation.NotModified;
import org.e2immu.support.Freezable;

import java.util.*;
import java.util.function.BiConsumer;

/*
to make this class @ImmutableContainer, we have to secure the get() methods
and the visitor by exposing copied lists, rather than the ones belonging to the fields.
 */
@FinalFields(after = "frozen")
public class Trie<T> extends Freezable {

    private final TrieNode<T> root = new TrieNode<>();

    private static class TrieNode<T> {

        List<T> data;

        Map<String, TrieNode<T>> map;
    }

    @NotModified
    private TrieNode<T> goTo(String[] strings) {
        return goTo(strings, strings.length);
    }

    @NotModified
    private TrieNode<T> goTo(String[] strings, int upToPosition) {
        TrieNode<T> node = root;
        for (int i = 0; i < upToPosition; i++) {
            if (node.map == null) return null;
            node = node.map.get(strings[i]);
            if (node == null) return null;
        }
        return node;
    }

    public boolean isStrictPrefix(String[] prefix) {
        TrieNode<T> node = goTo(prefix);
        return node != null && node.data == null;
    }

    public List<T> get(String[] strings) {
        TrieNode<T> node = goTo(strings);
        return node == null ? null : node.data;
    }

    public List<T> get(String[] strings, int upToPosition) {
        TrieNode<T> node = goTo(strings, upToPosition);
        return node == null ? null : node.data == null ? List.of() : node.data;
    }

    @NotModified
    public void visitLeaves(String[] strings, BiConsumer<String[], List<T>> visitor) {
        TrieNode<T> node = goTo(strings);
        if (node == null) return;
        if (node.map != null) {
            node.map.forEach((s, n) -> {
                if (n.map == null) { // leaf
                    visitor.accept(new String[]{s}, n.data);
                }
            });
        }
    }

    @Modified
    public void visit(String[] strings, BiConsumer<String[], List<T>> visitor) {
        TrieNode<T> node = goTo(strings);
        if (node == null) return;
        recursivelyVisit(node, new Stack<>(), visitor);
    }

    @Modified
    public void visitThrowing(String[] strings, ThrowingBiConsumer<String[], List<T>> visitor) {
        TrieNode<T> node = goTo(strings);
        if (node == null) return;
        recursivelyVisit(node, new Stack<>(), (s, t) -> {
            try {
                visitor.accept(s, t);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private static <T> void recursivelyVisit(@Modified TrieNode<T> node,
                                             Stack<String> strings,
                                             BiConsumer<String[], List<T>> visitor) {
        if (node.data != null) {
            visitor.accept(strings.toArray(String[]::new), node.data);
        }
        if (node.map != null) {
            node.map.forEach((s, n) -> {
                strings.push(s);
                recursivelyVisit(n, strings, visitor);
                strings.pop();
            });
        }
    }

    @Modified
    public T addIfNodeDataEmpty(String[] strings, T data) {
        TrieNode<T> node = ensureTrieNode(strings);
        if (node.data.isEmpty()) {
            node.data.add(data);
            return data;
        }
        return node.data.get(0);
    }

    @Modified
    public void add(String[] strings, T data) {
        TrieNode<T> node = ensureTrieNode(strings);
        node.data.add(Objects.requireNonNull(data));
    }

    private TrieNode<T> ensureTrieNode(String[] strings) {
        ensureNotFrozen();
        TrieNode<T> node = root;
        for (String s : strings) {
            TrieNode<T> newTrieNode;
            if (node.map == null) { // 2.0.1
                node.map = new HashMap<>();
                newTrieNode = new TrieNode<>();
                node.map.put(s, newTrieNode);
            } else {
                newTrieNode = node.map.computeIfAbsent(s, k -> new TrieNode<>());
            }
            node = newTrieNode;
        }
        if (node.data == null) node.data = new LinkedList<>();
        return node;
    }
}
