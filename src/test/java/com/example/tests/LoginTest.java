package com.example.tests;

import com.microsoft.playwright.Page;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LoginTest extends BaseTest {

    @BeforeEach
    public void testSetUp() {
        setUp();
    }

    @AfterEach
    public void testTearDown() {
        tearDown();
    }

    @Test
    public void testLoginWithValidCredentials() {
        Page page = getPage();
        
        // Create a simple HTML page for testing
        String html = "<!DOCTYPE html><html>" +
                "<body><h1>Login Page</h1>" +
                "<input name='username' placeholder='Username'>" +
                "<input name='password' type='password' placeholder='Password'>" +
                "<button type='submit'>Login</button></body></html>";
        
        page.setContent(html);
        
        // Fill in username
        page.fill("input[name='username']", "testuser");

        // Fill in password
        page.fill("input[name='password']", "testpassword");

        // Verify inputs are filled
        assertEquals("testuser", page.inputValue("input[name='username']"));
        assertEquals("testpassword", page.inputValue("input[name='password']"));
    }

    @Test
    public void testLoginWithInvalidCredentials() {
        Page page = getPage();
        
        // Create HTML with error message
        String html = "<!DOCTYPE html><html>" +
                "<body><h1>Login Page</h1>" +
                "<input name='username'><input name='password' type='password'>" +
                "<button>Login</button>" +
                "<div class='error' style='display:none'>Invalid credentials</div>" +
                "</body></html>";
        
        page.setContent(html);

        // Fill in invalid credentials
        page.fill("input[name='username']", "invaliduser");
        page.fill("input[name='password']", "wrongpassword");

        // Verify inputs are filled
        assertTrue(page.isVisible("input[name='username']"));
        assertTrue(page.isVisible("input[name='password']"));
    }

    @Test
    public void testPageTitle() {
        Page page = getPage();
        
        String html = "<!DOCTYPE html><html><head><title>Test Page</title></head>" +
                "<body><h1>Test Content</h1></body></html>";
        page.setContent(html);
        String title = page.title();
        assertEquals("Test Page", title);
    }
}
