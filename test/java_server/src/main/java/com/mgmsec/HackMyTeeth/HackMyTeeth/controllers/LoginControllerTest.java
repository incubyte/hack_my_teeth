import com.mgmsec.HackMyTeeth.HackMyTeeth.controllers.LoginController;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LoginControllerTest {

    @Test
    public void testLogin2() {
        LoginController loginController = new LoginController();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        Model model = new Model() {
            @Override
            public Model addAttribute(String attributeName, Object attributeValue) {
                return null;
            }

            @Override
            public Model addAttribute(Object attributeValue) {
                return null;
            }

            @Override
            public Model addAllAttributes(Collection<?> attributeValues) {
                return null;
            }

            @Override
            public Model addAllAttributes(Map<String, ?> attributes) {
                return null;
            }

            @Override
            public Model mergeAttributes(Map<String, ?> attributes) {
                return null;
            }

            @Override
            public boolean containsAttribute(String attributeName) {
                return false;
            }

            @Override
            public Map<String, Object> asMap() {
                return null;
            }
        };

        ModelAndView modelAndView = loginController.login2(request, response, model);

        assertNotNull(modelAndView);
        assertEquals("login", modelAndView.getViewName());
    }

    @Test
    public void testSetSessionCookie() {
        LoginController loginController = new LoginController();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        String username = "testUser";
        int role = 1;
        int userID = 123;

        loginController.setSessionCookie(request, response, username, role, userID);

        Cookie[] cookies = response.getCookies();
        assertNotNull(cookies);
        assertEquals(1, cookies.length);
        assertEquals("SESSIONCOOKIE", cookies[0].getName());
        assertEquals("", cookies[0].getValue());
        assertEquals("/", cookies[0].getPath());
        assertEquals(-1, cookies[0].getMaxAge());
        assertEquals(false, cookies[0].getSecure());
        assertEquals(true, cookies[0].isHttpOnly());
    }
}