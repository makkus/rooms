/*
 * Rooms
 *
 * Copyright (c) 2014, Markus Binsteiner. All rights reserved.
 *
 * This file is part of Things.
 *
 * Rooms is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package rooms.config;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.MongoRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import rooms.actions.LightAction;
import rooms.actions.LightUtil;
import rooms.readers.LightStateReader;
import things.config.ThingActions;
import things.config.ThingQueries;
import things.config.ThingReaders;
import things.config.ThingWriters;
import things.connectors.xstream.XstreamConnector;
import things.thing.ActionManager;
import things.thing.DefaultActionManager;
import things.thing.ThingControl;
import things.thing.ThingsObjectMapper;
import things.types.AnnotationTypeFactory;
import things.types.ThingType;
import things.types.TypeRegistry;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.File;

/**
 * Project: things
 * <p>
 * Written by: Markus Binsteiner
 * Date: 11/05/14
 * Time: 2:16 PM
 */
@Configuration
@ComponentScan({"things.thing", "things.view.rest", "rooms.config", "rooms.view.websockets"})
@EnableAutoConfiguration(exclude = {HibernateJpaAutoConfiguration.class, DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class, MongoTemplateAutoConfiguration.class, MongoRepositoriesAutoConfiguration.class, MongoAutoConfiguration.class})
public class RoomConfig {

    @Bean
    public ActionManager actionManager() {
        return new DefaultActionManager();
    }

    @Bean
    public XstreamConnector defaultConnector() {
        return new XstreamConnector(new File("/home/markus/ttt/things/"), new File("/home/markus/ttt/values"));
    }

    @Bean
    public LightAction lightAction() throws Exception {
        LightAction lc = new LightAction();
        return lc;
    }

    @Bean
    LightStateReader lightStateReader() {
        return new LightStateReader();
    }

    @Bean
    public LightUtil lightUtil() throws Exception {
        return new LightUtil();
    }

    @Bean
    public MetricRegistry metricRegistry() {
        return new MetricRegistry();
    }

    @Bean
    public ObjectMapper objectMapper() {
        ThingsObjectMapper tom = new ThingsObjectMapper(typeRegistry());
        return tom;
    }

    @Bean
    public EmbeddedServletContainerFactory servletContainer() {
        //return new JettyEmbeddedServletContainerFactory();
        return new TomcatEmbeddedServletContainerFactory();
    }

    @Bean
    ThingActions thingActions() throws Exception {
        ThingActions ta = new ThingActions();
        ta.addAction(lightAction());
        return ta;
    }

    @Bean
    public ThingControl thingControl() throws Exception {
        ThingControl tc = new ThingControl();
        return tc;
    }

    @Bean
    public ThingQueries thingQueries() {
        ThingQueries tq = new ThingQueries();
        return tq;
    }

    @Bean
    public ThingReaders thingReaders() throws Exception {

        ThingReaders tr = new ThingReaders();
        tr.addReader("bridge/*", defaultConnector());
        tr.addReader("light/*", defaultConnector());
        tr.addReader("profile/*", defaultConnector());
        tr.addReader("lightState/*", lightStateReader());

        return tr;
    }

    @Bean
    public ThingWriters thingWriters() throws Exception {

        ThingWriters tw = new ThingWriters();
        tw.addWriter("bridge/*", defaultConnector());
        tw.addWriter("light/*", defaultConnector());
        tw.addWriter("profile/*", defaultConnector());

        return tw;
    }

    @Bean
    public TypeRegistry typeRegistry() {
        TypeRegistry tr = new TypeRegistry();
        for ( ThingType tt : AnnotationTypeFactory.getAllTypes() ) {
            tr.addType(tt);
        }
        return tr;
    }

    @Bean(name = "valueValidator")
    public Validator validator() {
        ValidatorFactory factory =
                Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        return validator;
    }

}
