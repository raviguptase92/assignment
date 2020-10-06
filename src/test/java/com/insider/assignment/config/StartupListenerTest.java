package com.insider.assignment.config;

import com.insider.assignment.service.AsyncStoryUpdateService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.doThrow;

public class StartupListenerTest {

    @InjectMocks
    private StartupListener startupListener = new StartupListener();

    @Mock
    private AsyncStoryUpdateService asyncStoryUpdateService;

    @Before
    public void setUp() {
        asyncStoryUpdateService = Mockito.mock(AsyncStoryUpdateService.class);
        ReflectionTestUtils.setField(startupListener, "asyncStoryUpdateService", asyncStoryUpdateService);
    }

    @Test
    public void testOnApplicationEventException() throws Exception {
        doThrow(new RuntimeException("Any exception")).when(asyncStoryUpdateService).updateTopStories();
        startupListener.onApplicationEvent(new ContextRefreshedEvent(new AnnotationConfigApplicationContext()));
    }

    @Test
    public void testOnApplicationHappyFlow() throws Exception {
        startupListener.onApplicationEvent(new ContextRefreshedEvent(new AnnotationConfigApplicationContext()));
    }
}
