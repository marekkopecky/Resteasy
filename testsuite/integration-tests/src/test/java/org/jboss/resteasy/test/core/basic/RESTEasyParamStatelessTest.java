package org.jboss.resteasy.test.core.basic;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.logging.Logger;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.test.core.basic.resource.RESTEasyParamStatelessResource;
import org.jboss.resteasy.test.providers.jsonb.basic.JsonBindingTest;
import org.jboss.resteasy.utils.PortProviderUtil;
import org.jboss.resteasy.utils.TestUtil;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * @tpSubChapter Configuration
 * @tpChapter Integration tests
 * @tpTestCaseDetails Test for new param annotations.
 * @tpSince RESTEasy 4.0.0
 */
@RunWith(Arquillian.class)
@RunAsClient
public class RESTEasyParamStatelessTest {
    protected static final Logger logger = Logger.getLogger(JsonBindingTest.class.getName());

    static ResteasyClient client;

    @Deployment
    public static Archive<?> deploySimpleResource() {
        WebArchive war = TestUtil.prepareArchive(RESTEasyParamStatelessTest.class.getSimpleName());
        return TestUtil.finishContainerPrepare(war, null, RESTEasyParamStatelessResource.class);
    }

    @Before
    public void init() {
        client = new ResteasyClientBuilder().build();
    }

    @After
    public void after() throws Exception {
        client.close();
    }

    private String generateURL(String path) {
        return PortProviderUtil.generateURL(path, RESTEasyParamStatelessTest.class.getSimpleName());
    }

    /**
     * @tpTestDetails Test new param annotations
     * @tpSince RESTEasy 4.0.0
     */
    @Test
    public void testNewParamAnnotationsParallel() throws Exception {
        int AVAILABLE_THREADS = 10;
        int ITERATION_OF_EACH_THREAD = 100;
        int RESTEASY_CLIENT_POOL_SIZE = AVAILABLE_THREADS;

        client = new ResteasyClientBuilder().connectionPoolSize(RESTEASY_CLIENT_POOL_SIZE).build();

        AtomicInteger errorCounter = new AtomicInteger(0);

        ForkJoinPool forkJoinPool = new ForkJoinPool(AVAILABLE_THREADS);
        forkJoinPool.submit(() ->
                //parallel task here, for example
                IntStream.range(1, AVAILABLE_THREADS).parallel().forEach(i -> {
                            for (Integer j = 0; j < ITERATION_OF_EACH_THREAD; j++) {
                                logger.info("Thread " + i + " start request " + j);
                                String defaultValue = String.format("%d-%d", i, j);
                                Response response = client.target(generateURL(String.format("/%d-%d/%d-%d/%d-%d/%d-%d",
                                                                    i, j, i, j, i, j, i, j)))
                                        .queryParam("queryParam0", defaultValue)
                                        .queryParam("queryParam1", defaultValue)
                                        .queryParam("queryParam2", defaultValue)
                                        .queryParam("queryParam3", defaultValue)
                                        .matrixParam("matrixParam0", defaultValue)
                                        .matrixParam("matrixParam1", defaultValue)
                                        .matrixParam("matrixParam2", defaultValue)
                                        .matrixParam("matrixParam3", defaultValue)
                                        .request()
                                        .header("headerParam0", defaultValue)
                                        .header("headerParam1", defaultValue)
                                        .header("headerParam2", defaultValue)
                                        .header("headerParam3", defaultValue)
                                        .cookie("cookieParam0", defaultValue)
                                        .cookie("cookieParam1", defaultValue)
                                        .cookie("cookieParam2", defaultValue)
                                        .cookie("cookieParam3", defaultValue)
                                        .post(Entity.form(new Form()
                                                .param("formParam0", defaultValue)
                                                .param("formParam1", defaultValue)
                                                .param("formParam2", defaultValue)
                                                .param("formParam3", defaultValue)
                                        ));
                                if (200 != response.getStatus()) {
                                    logger.error("expected response code is 200, get: " + response.getStatus());
                                    errorCounter.incrementAndGet();
                                }
                                String message = response.readEntity(String.class);
                                if (!defaultValue.equals(message)) {
                                    logger.error("expected value: " + defaultValue + ", get: " + message);
                                    errorCounter.incrementAndGet();
                                }
                                logger.info("expected value: " + defaultValue + ", get: " + message);
                                logger.info("Thread " + i + "'s request " + j + " is done");
                            }
                        }
                )
        ).get();

        Assert.assertEquals(0, errorCounter.get());

        client.close();


    }

}
