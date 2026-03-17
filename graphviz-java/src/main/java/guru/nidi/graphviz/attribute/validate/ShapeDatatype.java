/*
 * Copyright © 2015 Stefan Niederhauser (nidin@gmx.ch)
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
package guru.nidi.graphviz.attribute.validate;

import guru.nidi.graphviz.attribute.Shape;

import java.util.Set;

import static guru.nidi.graphviz.attribute.validate.ValidatorMessage.Severity.ERROR;

class ShapeDatatype extends Datatype {
    private static final Set<String> NAMES = shapeNames();

    private static Set<String> shapeNames() {
        final Set<String> names = fieldNames(Shape.class);
        names.add("polygon");
        return names;
    }

    ShapeDatatype() {
        super("shape");
    }

    @Override
    ValidatorMessage validate(Object value) {
        if (!NAMES.contains(value.toString())) {
            return new ValidatorMessage(ERROR, "has the invalid shape '" + value + "'.");
        }
        return null;
    }
}
