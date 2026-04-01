/*
 * Copyright © 2026 Stefan Niederhauser (nidin@gmx.ch)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package guru.nidi.graphviz.model;

import guru.nidi.graphviz.engine.*;
import guru.nidi.graphviz.parse.Parser;
import org.apache.commons.lang3.mutable.Mutable;
import org.junit.jupiter.api.*;

import java.io.IOException;

import static guru.nidi.graphviz.engine.Format.SVG;
import static guru.nidi.graphviz.model.Factory.graph;
import static guru.nidi.graphviz.model.Factory.mutNode;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SimpleTest {
    @BeforeAll
    static void init() {
        Graphviz.useEngine(new GraphvizV8Engine(), new GraphvizJdkEngine());
    }

    @AfterAll
    static void end() {
        Graphviz.releaseEngine();
    }

    @Test
    void simple() {
        final Graphviz viz = Graphviz.fromString("digraph g { \"a\\b'c\" -> b; }");
        assertNotNull(viz.render(SVG).toString());
    }

    @Test
    void dotError() {
        try {
            System.out.println("Try error...");
            Graphviz.fromString("g { a -> b; }").render(SVG).toString();
            fail("Wrong dot file should throw");
        } catch (GraphvizException e) {
            assertThat(e.getMessage(), containsString("syntax error in line 1 near 'g'"));
        }
    }

    //Test added by Blake Ranniker on 3/17/2026
    @Test
    void removeNodeTest() throws IOException {
        final MutableGraph g = new Parser().read("digraph g { }");
        final MutableNode t = mutNode("testNode");
        final MutableNode t1 = mutNode("testNodeFriend");

        g.add(t, t1);
        t1.addLink(t);

        System.out.println(g.nodes());
        g.remove(t);

        final boolean nodeMissed = g.nodes().contains(t);
        System.out.println(g.nodes());

        boolean edgeMissed = false;
        for (final Link l : g.links()) {
            if (l.to() == t || l.from() == t) {
                edgeMissed = true;
                break;
            }
        }

        assertFalse(edgeMissed || nodeMissed);
    }

    //Test added by Blake Ranniker on 3/17/2026
    @Test
    void removeLinkTest() throws IOException {
        final MutableGraph g = new Parser().read("digraph g { }");

        final MutableNode t = mutNode("testNode");
        final MutableNode t1 = mutNode("testNodeFriend");

        g.add(t, t1);
        final Link l = t.linkTo(t1);

        t.addLink(l);

        g.remove(t, l);
        assertFalse(t.links.contains(l));
    }
}
