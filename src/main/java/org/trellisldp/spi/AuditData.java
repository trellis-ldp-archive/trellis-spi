/*
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
package org.trellisldp.spi;

import static java.time.Instant.now;
import static java.util.Arrays.asList;
import static org.trellisldp.vocabulary.RDF.type;
import static org.trellisldp.vocabulary.Trellis.PreferAudit;

import java.util.ServiceLoader;
import java.util.List;

import org.apache.commons.rdf.api.BlankNodeOrIRI;
import org.apache.commons.rdf.api.Dataset;
import org.apache.commons.rdf.api.IRI;
import org.apache.commons.rdf.api.RDF;

import org.trellisldp.vocabulary.AS;
import org.trellisldp.vocabulary.PROV;
import org.trellisldp.vocabulary.XSD;

/**
 * The Audit Event class provides methods for adding audit-related quads
 * to datasets.
 *
 * @author acoburn
 */
public class AuditData {

    private static RDF rdf = ServiceLoader.load(RDF.class).iterator().next();

    /**
     * Create audit-related creation data
     * @param subject the subject
     * @param session the session
     * @return the quads
     */
    public Dataset creationData(final BlankNodeOrIRI subject, final Session session) {
        return genData(subject, session, asList(PROV.Activity, AS.Create));
    }

    /**
     * Create audit-related deletion data
     * @param subject the subject
     * @param session the session
     * @return the quads
     */
    public Dataset deletionData(final BlankNodeOrIRI subject, final Session session) {
        return genData(subject, session, asList(PROV.Activity, AS.Delete));
    }

    /**
     * Create audit-related update data
     * @param subject the subject
     * @param session the session
     * @return the quads
     */
    public Dataset updateData(final BlankNodeOrIRI subject, final Session session) {
        return genData(subject, session, asList(PROV.Activity, AS.Update));
    }

    private Dataset genData(final BlankNodeOrIRI subject, final Session session, final List<IRI> types) {
        final Dataset dataset = rdf.createDataset();
        types.forEach(t ->
            dataset.add(rdf.createQuad(PreferAudit, subject, type, t)));
        dataset.add(rdf.createQuad(PreferAudit, subject, PROV.wasAssociatedWith, session.getAgent()));
        dataset.add(rdf.createQuad(PreferAudit, subject, PROV.startedAtTime,
                    rdf.createLiteral(now().toString(), XSD.dateTime)));
        session.getDelegatedBy().ifPresent(delegate ->
                dataset.add(rdf.createQuad(PreferAudit, subject, PROV.actedOnBehalfOf, delegate)));
        return dataset;
    }
}
