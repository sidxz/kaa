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

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.kaaproject.avro.ui.gwt.client.util.BusyAsyncCallback;
import org.kaaproject.avro.ui.shared.RecordField;
import org.kaaproject.kaa.common.dto.ServerProfileSchemaDto;
import org.kaaproject.kaa.common.dto.ctl.CTLSchemaDto;
import org.kaaproject.kaa.server.admin.client.KaaAdmin;
import org.kaaproject.kaa.server.admin.client.mvp.ClientFactory;
import org.kaaproject.kaa.server.admin.client.mvp.place.ServerProfileSchemaPlace;
import org.kaaproject.kaa.server.admin.client.mvp.view.BaseSchemaView;
import org.kaaproject.kaa.server.admin.client.mvp.view.widget.RecordPanel;
import org.kaaproject.kaa.server.admin.client.util.ErrorMessageCustomizer;
import org.kaaproject.kaa.server.admin.client.util.Utils;

public class ServerProfileSchemaActivity
        extends AbstractDetailsActivity<ServerProfileSchemaDto, BaseSchemaView, ServerProfileSchemaPlace>
        implements ErrorMessageCustomizer, RecordPanel.FormDataLoader {

    private static final String  LEFT_SQUARE_BRACKET = "[";
    private static final String  RIGHT_SQUARE_BRACKET = "]";
    private static final String  SEMICOLON = ";";

    protected String applicationId;

    public ServerProfileSchemaActivity(ServerProfileSchemaPlace place,
                                       ClientFactory clientFactory) {
        super(place, clientFactory);
        this.applicationId = place.getApplicationId();
    }

    protected ServerProfileSchemaDto newSchema() {
        CTLSchemaDto ctlDto = new CTLSchemaDto();
        ServerProfileSchemaDto serverProfileSchemaDto = new ServerProfileSchemaDto();
        serverProfileSchemaDto.setSchemaDto(ctlDto);
        return serverProfileSchemaDto;
    }

    @Override
    protected BaseSchemaView getView(boolean create) {
        if (create) {
            return clientFactory.getCreateServerProfileSchemaView();
        } else {
            return clientFactory.getServerProfileSchemaView();
        }
    }

    @Override
    protected String getEntityId(ServerProfileSchemaPlace place) {
        return place.getSchemaId();
    }

    @Override
    protected ServerProfileSchemaDto newEntity() {
        ServerProfileSchemaDto schema = newSchema();
        schema.setApplicationId(applicationId);
        return schema;
    }

    @Override
    protected void bind(final EventBus eventBus) {
        super.bind(eventBus);
    }

    @Override
    protected void onEntityRetrieved() {
        detailsView.getName().setValue(entity.getSchemaDto().getName());
        detailsView.getDescription().setValue(entity.getSchemaDto().getDescription());
        detailsView.getCreatedUsername().setValue(entity.getSchemaDto().getCreatedUsername());
        detailsView.getEndpointCount().setValue("");
        if (create) {
            createEmptySchemaForm(new BusyAsyncCallback<RecordField>() {
                @Override
                public void onSuccessImpl(RecordField result) {
                    detailsView.getSchemaForm().setValue(result);
                }

                @Override
                public void onFailureImpl(Throwable caught) {
                    Utils.handleException(caught, detailsView);
                }
            });
            detailsView.getSchemaForm().setFormDataLoader(this);
        } else {
            detailsView.getVersion().setValue(entity.getSchemaDto().getMetaInfo().getVersion() + ".0");
            detailsView.getCreatedDateTime().setValue(Utils.millisecondsToDateTimeString(entity.getCreatedTime()));
            detailsView.getSchemaForm().setValue(entity.getSchemaForm());
        }
    }

    @Override
    protected void onSave() {
        entity.getSchemaDto().setName(detailsView.getName().getValue());
        entity.getSchemaDto().setDescription(detailsView.getDescription().getValue());
        entity.setSchemaForm(detailsView.getSchemaForm().getValue());
        String applicationId = place.getApplicationId();
        entity.getSchemaDto().setApplicationId(applicationId);
        entity.setApplicationId(applicationId);
    }

    @Override
    public String customizeErrorMessage(Throwable caught) {
        String errorMessage = caught.getLocalizedMessage();
        int leftSquareBracketIndex = errorMessage.indexOf(LEFT_SQUARE_BRACKET);
        int rightSquareBracketIndex = -1;
        if (leftSquareBracketIndex != -1) {
            rightSquareBracketIndex = errorMessage.indexOf(RIGHT_SQUARE_BRACKET, leftSquareBracketIndex);
        }
        if (rightSquareBracketIndex != -1) {
            StringBuilder builder = new StringBuilder();
            builder.append("Incorrect json schema: Please check your schema at");
            String[] array = errorMessage.substring(leftSquareBracketIndex, rightSquareBracketIndex).split(SEMICOLON);
            if (array != null && array.length == 2) {
                builder.append(array[1]);
                errorMessage = builder.toString();
            }
        } else {
            return "Incorrect schema: Please validate your schema.";
        }
        return errorMessage;
    }

    protected void createEmptySchemaForm(AsyncCallback<RecordField> callback) {
        KaaAdmin.getDataSource().createSimpleEmptySchemaForm(callback);
    }

    @Override
    protected void getEntity(String id,
                             AsyncCallback<ServerProfileSchemaDto> callback) {
        KaaAdmin.getDataSource().getServerProfileSchemaForm(id, callback);
    }

    @Override
    protected void editEntity(ServerProfileSchemaDto entity,
                              AsyncCallback<ServerProfileSchemaDto> callback) {
        KaaAdmin.getDataSource().editServerProfileSchemaForm(entity, callback);
    }

    @Override
    public void loadFormData(String fileItemName,
                             AsyncCallback<RecordField> callback) {
        KaaAdmin.getDataSource().generateSimpleSchemaForm(fileItemName, callback);
    }
}