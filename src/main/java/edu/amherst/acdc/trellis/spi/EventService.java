/*
 * Copyright Amherst College
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
package edu.amherst.acdc.trellis.spi;

import static java.util.Objects.requireNonNull;

import java.util.Optional;

/**
 * The EventService provides a mechanism by which events can be emitted
 * to a message broker.
 *
 * @author acoburn
 */
public interface EventService {

    /**
     * Emit an event to the event service
     * @param event the event
     */
    void emit(Event event);

    /**
     * Serialize an event as a JSON string
     * @param event the event
     * @return the serialized event
     */
    static Optional<String> serialize(final Event event) {
        requireNonNull(event, "The event may not be null!");
        final ActivityStreamSerializer serializer = new ActivityStreamSerializer(event);
        return serializer.serialize();
    }
}
