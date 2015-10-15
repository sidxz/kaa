/*
 * Copyright 2014 CyberVision, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kaaproject.kaa.server.admin.client.mvp.activity;

import com.google.common.io.BaseEncoding;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import org.kaaproject.avro.ui.gwt.client.widget.BusyPopup;
import org.kaaproject.avro.ui.shared.RecordField;
import org.kaaproject.kaa.common.dto.EndpointProfileDto;
import org.kaaproject.kaa.common.dto.EndpointProfileViewDto;
import org.kaaproject.kaa.common.dto.EndpointUserDto;
import org.kaaproject.kaa.common.dto.ProfileSchemaDto;
import org.kaaproject.kaa.common.dto.TopicDto;
import org.kaaproject.kaa.server.admin.client.KaaAdmin;
import org.kaaproject.kaa.server.admin.client.mvp.ClientFactory;
import org.kaaproject.kaa.server.admin.client.mvp.place.EndpointProfilePlace;
import org.kaaproject.kaa.server.admin.client.mvp.view.EndpointProfileView;

import java.util.List;

public class EndpointProfileActivity extends
        AbstractDetailsActivity<EndpointProfileViewDto, EndpointProfileView, EndpointProfilePlace> {

    private String applicationId;

    public EndpointProfileActivity(EndpointProfilePlace place, ClientFactory clientFactory) {
        super(place, clientFactory);
        this.applicationId = place.getApplicationId();
    }

    @Override
    public void start(AcceptsOneWidget containerWidget, EventBus eventBus) {
        super.start(containerWidget, eventBus);
        BusyPopup.hidePopup();
    }

    protected void bind(final EventBus eventBus) {
        super.bind(eventBus);
    }

    @Override
    protected String getEntityId(EndpointProfilePlace place) {
//        return endpointKeyHash;
        return place.getEndpointKeyHash();
    }

    @Override
    protected EndpointProfileView getView(boolean create) {
        return clientFactory.getEndpointProfileView();
    }

    @Override
    protected EndpointProfileViewDto newEntity() {
        return null;
    }

    @Override
    protected void onEntityRetrieved() {

        EndpointProfileDto profileDto = entity.getEndpointProfileDto();
        EndpointUserDto userDto = entity.getEndpointUserDto();
        ProfileSchemaDto profileSchemaDto = entity.getProfileSchemaDto();

        detailsView.getKeyHash()             .setValue(BaseEncoding.base64().encode(profileDto.getEndpointKeyHash()));

        if (userDto != null) {
            detailsView.getUserID()              .setValue(userDto.getId());
            detailsView.getUserName()            .setValue(userDto.getUsername());
            detailsView.getUserExternalID()      .setValue(userDto.getExternalId());
        } else {
            detailsView.getUserID()        .setValue("");
            detailsView.getUserName()      .setValue("");
            detailsView.getUserExternalID().setValue("");
        }

        detailsView.getNotificationVersion() .setValue(profileDto.getNotificationVersion() + "");
        detailsView.getSystemNfVersion()     .setValue(profileDto.getSystemNfVersion() + "");
        detailsView.getUserNfVersion()       .setValue(profileDto.getUserNfVersion() + "");
        detailsView.getLogSchemaVer()        .setValue(profileDto.getLogSchemaVersion() + "");


        detailsView.getGroupsGrid().getDataGrid().setRowData(entity.getGroupDtoList());

        List<TopicDto> endpointNotificationTopics = entity.getEndpointNotificationTopics();
        if (endpointNotificationTopics != null) {
            detailsView.getTopicsGrid().getDataGrid().setRowData(endpointNotificationTopics);
        }

        detailsView.getSchemaName() .setValue(profileSchemaDto.getName());
        detailsView.getDescription().setValue(profileSchemaDto.getDescription());
//        if (profileSchemaDto.getSchemaForm() != null) {
////            detailsView.getSchemaForm().setValue(profileSchemaDto.getSchemaForm());
//            detailsView.getSchemaForm().setValue(entity.getEndpointProfileRecord());
//        }
        RecordField endpointProfileRecord = entity.getEndpointProfileRecord();
        if (endpointProfileRecord != null) {
            detailsView.getSchemaForm().setValue(endpointProfileRecord);
//            detailsView.getSchemaForm().setValue(profileSchemaDto.getSchemaForm(), true);
        }
    }

    @Override
    protected void onSave() {}

    @Override
    protected void getEntity(String id, final AsyncCallback<EndpointProfileViewDto> callback) {
        KaaAdmin.getDataSource().getEndpointProfileViewDtoByEndpointProfileKeyHash(id, callback);
    }

    @Override
    protected void editEntity(EndpointProfileViewDto entity, AsyncCallback<EndpointProfileViewDto> callback) {}
}
