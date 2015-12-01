/*
 * Copyright 2014-2015 CyberVision, Inc.
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

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.kaaproject.avro.ui.gwt.client.widget.grid.AbstractGrid;
import org.kaaproject.avro.ui.gwt.client.widget.grid.event.RowActionEvent;
import org.kaaproject.kaa.common.dto.ServerProfileSchemaDto;
import org.kaaproject.kaa.common.dto.admin.RecordKey;
import org.kaaproject.kaa.server.admin.client.KaaAdmin;
import org.kaaproject.kaa.server.admin.client.mvp.ClientFactory;
import org.kaaproject.kaa.server.admin.client.mvp.activity.grid.AbstractDataProvider;
import org.kaaproject.kaa.server.admin.client.mvp.data.ServerProfileSchemasDataProvider;
import org.kaaproject.kaa.server.admin.client.mvp.place.ServerProfileSchemaPlace;
import org.kaaproject.kaa.server.admin.client.mvp.place.ServerProfileSchemasPlace;
import org.kaaproject.kaa.server.admin.client.mvp.view.BaseListView;
import org.kaaproject.kaa.server.admin.client.mvp.view.grid.KaaRowAction;
import org.kaaproject.kaa.server.admin.client.servlet.ServletHelper;
import org.kaaproject.kaa.server.admin.client.util.Utils;

public class ServerProfileSchemasActivity extends AbstractListActivity<ServerProfileSchemaDto, ServerProfileSchemasPlace> {

    private String applicationId;

    public ServerProfileSchemasActivity(ServerProfileSchemasPlace place, ClientFactory clientFactory) {
        super(place, ServerProfileSchemaDto.class, clientFactory);
        this.applicationId = place.getApplicationId();
    }

    @Override
    protected BaseListView<ServerProfileSchemaDto> getView() {
        return clientFactory.getServerProfileSchemasView();
    }

    @Override
    protected AbstractDataProvider<ServerProfileSchemaDto> getDataProvider(
            AbstractGrid<ServerProfileSchemaDto,?> dataGrid) {
        return new ServerProfileSchemasDataProvider(dataGrid, listView, applicationId);
    }

    @Override
    protected Place newEntityPlace() {
        return new ServerProfileSchemaPlace(applicationId, "");
    }

    @Override
    protected Place existingEntityPlace(String id) {
        return new ServerProfileSchemaPlace(applicationId, id);
    }

    @Override
    protected void deleteEntity(String id, AsyncCallback<Void> callback) {
        callback.onSuccess((Void)null);
    }

    @Override
    protected void onCustomRowAction(RowActionEvent<String> event) {
        int schemaId = Integer.valueOf(event.getClickedId());
        if (event.getAction() == KaaRowAction.DOWNLOAD_SCHEMA) {
            KaaAdmin.getDataSource().getRecordData(applicationId, schemaId, RecordKey.RecordFiles.SERVER_PROFILE_SCHEMA,
                    new AsyncCallback<String>() {
                @Override
                public void onFailure(Throwable caught) {
                    Utils.handleException(caught, listView);
                }
                @Override
                public void onSuccess(String key) {
                    ServletHelper.downloadRecordLibrary(key);
                }
            });
        }
    }
}