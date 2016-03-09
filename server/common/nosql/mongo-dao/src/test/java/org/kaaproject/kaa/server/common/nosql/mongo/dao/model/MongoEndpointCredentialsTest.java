/**
 *  Copyright 2014-2016 CyberVision, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.kaaproject.kaa.server.common.nosql.mongo.dao.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Assert;
import org.junit.Test;
import org.kaaproject.kaa.common.dto.EndpointCredentialsDto;

/**
 * @author Bohdan Khablenko
 *
 * @since v0.9.0
 */
public class MongoEndpointCredentialsTest {

    private static final String APPLICATION_ID = "application_id";
    private static final String ENDPOINT_ID = "endpoint_id";
    private static final String PUBLIC_KEY = "public_key";

    @Test
    public void equalsVerifierTest() throws Exception {
        EqualsVerifier.forClass(MongoEndpointCredentials.class).suppress(Warning.NONFINAL_FIELDS).verify();
    }

    @Test
    public void dataConversionTest() throws Exception {
        EndpointCredentialsDto endpointCredentials = new EndpointCredentialsDto(APPLICATION_ID, ENDPOINT_ID, PUBLIC_KEY);
        MongoEndpointCredentials endpointCredentialsModel = new MongoEndpointCredentials(endpointCredentials);
        Assert.assertEquals(endpointCredentials, endpointCredentialsModel.toDto());
    }
}