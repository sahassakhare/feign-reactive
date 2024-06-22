/**
 * Copyright 2018 The Feign Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package reactivefeign.webclient;

import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactivefeign.ReactiveFeignBuilder;
import reactivefeign.ReactiveOptions;
import reactivefeign.client.ReactiveHttpRequest;
import reactivefeign.client.ReadTimeoutException;
import reactor.netty.http.client.HttpClient;

import java.util.function.BiFunction;

import static reactivefeign.webclient.NettyClientHttpConnectorBuilder.buildNettyClientHttpConnector;


/**
 * {@link WebClient} based implementation of reactive Feign
 *
 * @author Sergii Karpenko
 */
public class WebReactiveFeign {

    public static <T> Builder<T> builder() {
        return builder(WebClient.builder());
    }

    public static <T> Builder<T> builder(WebClient.Builder webClientBuilder) {
        return new Builder<>(webClientBuilder);
    }

    public static <T> Builder<T> builder(WebClient.Builder webClientBuilder,
                                         WebClientFeignCustomizer webClientCustomizer) {
        return new Builder<>(webClientBuilder, webClientCustomizer);
    }

    public static class Builder<T> extends CoreWebBuilder<T> {

        private HttpClient httpClient;
        private WebReactiveOptions options = WebReactiveOptions.DEFAULT_OPTIONS;

        protected Builder(WebClient.Builder webClientBuilder) {
            super(webClientBuilder);
        }

        protected Builder(WebClient.Builder webClientBuilder, WebClientFeignCustomizer webClientCustomizer) {
            super(webClientBuilder, webClientCustomizer);
        }

        public ReactiveFeignBuilder<T> httpClient(HttpClient httpClient){
            this.httpClient = httpClient;
            return this;
        }

        @Override
        public Builder<T> options(ReactiveOptions options) {
            this.options = (WebReactiveOptions) options;
            return this;
        }

        @Override
        protected ClientHttpConnector clientConnector() {
            return buildNettyClientHttpConnector(httpClient, options);
        }

        @Override
        public BiFunction<ReactiveHttpRequest, Throwable, Throwable> errorMapper(){
            return (request, throwable) -> {
                if(throwable instanceof WebClientRequestException
                   && throwable.getCause() instanceof io.netty.handler.timeout.ReadTimeoutException){
                    return new ReadTimeoutException(throwable, request);
                }
                return null;
            };
        }
    }



}


