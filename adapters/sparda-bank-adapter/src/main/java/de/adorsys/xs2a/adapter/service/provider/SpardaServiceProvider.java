/*
 * Copyright 2018-2018 adorsys GmbH & Co KG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.adorsys.xs2a.adapter.service.provider;

import de.adorsys.xs2a.adapter.adapter.BaseAccountInformationService;
import de.adorsys.xs2a.adapter.adapter.BasePaymentInitiationService;
import de.adorsys.xs2a.adapter.http.HttpClientFactory;
import de.adorsys.xs2a.adapter.service.*;
import de.adorsys.xs2a.adapter.service.model.Aspsp;
import de.adorsys.xs2a.adapter.service.util.SpardaOauthParamsAdjustingService;

public class SpardaServiceProvider implements AccountInformationServiceProvider, PaymentInitiationServiceProvider,
                                                  Oauth2ServiceFactory {

    @Override
    public AccountInformationService getAccountInformationService(Aspsp aspsp, HttpClientFactory httpClientFactory, Pkcs12KeyStore keyStore) {
        return new BaseAccountInformationService(aspsp, httpClientFactory.getHttpClient(getAdapterId()));
    }

    @Override
    public PaymentInitiationService getPaymentInitiationService(String baseUrl, HttpClientFactory httpClientFactory, Pkcs12KeyStore keyStore) {
        return new BasePaymentInitiationService(baseUrl, httpClientFactory.getHttpClient(getAdapterId()));
    }

    @Override
    public Oauth2Service getOauth2Service(Aspsp aspsp, HttpClientFactory httpClientFactory, Pkcs12KeyStore keyStore) {
        return new SpardaOauth2Service(aspsp, httpClientFactory.getHttpClient(getAdapterId()),
            new SpardaOauthParamsAdjustingService(aspsp, keyStore));
    }

    @Override
    public String getAdapterId() {
        return "sparda-bank-adapter";
    }
}
