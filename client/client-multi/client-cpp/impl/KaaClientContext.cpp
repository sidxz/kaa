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

#include "kaa/KaaClientContext.hpp"

#include "kaa/KaaClientProperties.hpp"
#include "kaa/logging/ILogger.hpp"
#include "kaa/IKaaClientStateStorage.hpp"
#include "kaa/context/IExecutorContext.hpp"

namespace kaa {

KaaClientContext::KaaClientContext(const KaaClientProperties &properties,
                 const ILogger &logger,
                 const IKaaClientStateStorage &state,
                 const IExecutorContext &executorContext):
    properties_((KaaClientProperties&)properties), logger_((ILogger&)logger), state_((IKaaClientStateStorage&)state), executorContext_((IExecutorContext&)executorContext)
{}

KaaClientProperties &KaaClientContext::getProperties()
{
    return properties_;
}

ILogger &KaaClientContext::getLogger()
{
    return logger_;
}

IKaaClientStateStorage &KaaClientContext::getStatus()
{
    return state_;
}

IExecutorContext &KaaClientContext::getExecutorContext()
{
    return executorContext_;
}

}