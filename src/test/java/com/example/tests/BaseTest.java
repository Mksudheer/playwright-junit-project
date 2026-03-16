package com.example.tests;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;

public class BaseTest {
    protected static Playwright playwright;
    protected static Browser browser;
    
    // ThreadLocal for thread-safe page and context isolation
    protected static ThreadLocal<BrowserContext> contextThreadLocal = new ThreadLocal<>();
    protected static ThreadLocal<Page> pageThreadLocal = new ThreadLocal<>();
    
    // Getters for page and context
    protected Page getPage() {
        return pageThreadLocal.get();
    }
    
    protected BrowserContext getContext() {
        return contextThreadLocal.get();
    }
    
    // Setters for page and context
    protected void setPage(Page page) {
        pageThreadLocal.set(page);
    }
    
    protected void setContext(BrowserContext context) {
        contextThreadLocal.set(context);
    }

    @BeforeAll
    public static void setupBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch();
    }

    @AfterAll
    public static void teardownBrowser() {
        playwright.close();
    }

    public void setUp() {
        BrowserContext context = browser.newContext();
        Page page = context.newPage();
        setContext(context);
        setPage(page);
    }

    public void tearDown() {
        Page page = getPage();
        BrowserContext context = getContext();
        
        if (page != null) {
            page.close();
        }
        if (context != null) {
            context.close();
        }
        
        // Clean up ThreadLocal
        pageThreadLocal.remove();
        contextThreadLocal.remove();
    }
}
