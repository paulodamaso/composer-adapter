/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2020 artipie.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.artpie.composer;

import com.google.common.io.ByteSource;
import java.io.IOException;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;

/**
 * PHP Composer package.
 *
 * @since 0.1
 */
public final class Package {

    /**
     * Package binary content.
     */
    private final ByteSource content;

    /**
     * Ctor.
     *
     * @param content Package binary content.
     */
    public Package(final ByteSource content) {
        this.content = content;
    }

    /**
     * Extract name from package.
     *
     * @return Package name.
     * @throws IOException In case exception occurred on reading content.
     */
    public Name name() throws IOException {
        return new Name(this.mandatoryString("name"));
    }

    /**
     * Extract version from package.
     *
     * @return Package version.
     * @throws IOException In case exception occurred on reading content.
     */
    public String version() throws IOException {
        return this.mandatoryString("version");
    }

    /**
     * Reads string value from package JSON root. Throws exception if value not found.
     *
     * @param name Attribute value.
     * @return String value.
     * @throws IOException In case exception occurred on reading content.
     */
    private String mandatoryString(final String name) throws IOException {
        final JsonString string = this.json().getJsonString(name);
        if (string == null) {
            throw new IllegalStateException(String.format("Bad package, no '%s' found.", name));
        }
        return string.getString();
    }

    /**
     * Reads package content as JSON object.
     *
     * @return Package JSON object.
     * @throws IOException In case exception occurred on reading content.
     */
    private JsonObject json() throws IOException {
        try (JsonReader reader = Json.createReader(this.content.openStream())) {
            return reader.readObject();
        }
    }
}
