/*
 * Copyright 2016 Phaneesh Nagaraja <phaneesh.n@gmail.com>.
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
package io.dropwizard.riemann;

import com.codahale.metrics.riemann.Riemann;
import com.codahale.metrics.riemann.RiemannReporter;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.lifecycle.Managed;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author phaneesh
 */
@Slf4j
public abstract class RiemannBundle<T extends Configuration> implements ConfiguredBundle<T> {

    private static Riemann riemann;

    private static RiemannReporter riemannReporter;

    public abstract RiemannConfig getRiemannConfiguration(T configuration);

    @Override
    public void initialize(Bootstrap<?> bootstrap) {

    }

    @Override
    public void run(T configuration, Environment environment) {
        environment.lifecycle().manage(new Managed() {
            @Override
            public void start() throws Exception {
                if (riemann == null) {
                    final val riemannConfig = getRiemannConfiguration(configuration);
                    try {
                        riemann = new Riemann(riemannConfig.getHost(), riemannConfig.getPort());
                        RiemannReporter.Builder builder = RiemannReporter.forRegistry(environment.metrics()).tags(riemannConfig.getTags())
                                .prefixedWith(riemannConfig.getPrefix())
                                .useSeparator(".")
                                .convertDurationsTo(TimeUnit.MILLISECONDS).convertRatesTo(TimeUnit.SECONDS);
                        riemannReporter = builder.build(riemann);
                        riemannReporter.start(riemannConfig.getPollingInterval(), TimeUnit.SECONDS);
                        log.info("Started Riemann metrics reporter on {}:{} with prefix {} and tagged with: {}", riemannConfig.getHost(),
                                riemannConfig.getPort(), riemannConfig.getPrefix(), Joiner.on(",").join(riemannConfig.getTags()));
                    } catch (IOException e) {
                        log.error("Error starting Riemann reporter", e);
                    }
                }
            }

            @Override
            public void stop() throws Exception {
                if(riemannReporter != null) {
                    riemannReporter.stop();
                }
                if(riemann != null) {
                    riemann.close();
                }
            }
        });
    }
}
