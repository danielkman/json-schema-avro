/*
 * Copyright (c) 2014, Francis Galiegue (fgaliegue@gmail.com)
 *
 * This software is dual-licensed under:
 *
 * - the Lesser General Public License (LGPL) version 3.0 or, at your option, any
 *   later version;
 * - the Apache Software License (ASL) version 2.0.
 *
 * The text of both licenses is available under the src/resources/ directory of
 * this project (under the names LGPL-3.0.txt and ASL-2.0.txt respectively).
 *
 * Direct link to the sources:
 *
 * - LGPL 3.0: https://www.gnu.org/licenses/lgpl-3.0.txt
 * - ASL 2.0: http://www.apache.org/licenses/LICENSE-2.0.txt
 */

package com.github.fge.jsonschema2avro.writers;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.jsonpointer.JsonPointer;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.core.tree.SchemaTree;
import com.github.fge.jsonschema.core.util.ValueHolder;
import com.github.fge.jsonschema2avro.AvroWriterProcessor;
import org.apache.avro.Schema;

public final class ArrayWriter
    extends AvroWriter
{
    private static final AvroWriter INSTANCE = new ArrayWriter();

    private ArrayWriter()
    {
    }

    public static AvroWriter getInstance()
    {
        return INSTANCE;
    }

    @Override
    protected Schema generate(final AvroWriterProcessor writer,
        final ProcessingReport report, final SchemaTree tree)
        throws ProcessingException
    {
        final JsonNode node = tree.getNode();
        final JsonPointer ptr = node.has("items") ? JsonPointer.of("items")
            : JsonPointer.of("additionalItems");

        final SchemaTree subTree = tree.append(ptr);
        final ValueHolder<SchemaTree> input
            = ValueHolder.hold("schema", subTree);
        final Schema itemsSchema = writer.process(report, input).getValue();
        return Schema.createArray(itemsSchema);
    }
}
