import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LoginControllerTest {

    @Test
    public void testFirstPageRequestMethod() {
        // Arrange
        LoginController controller = new LoginController();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        Model model = mock(Model.class);

        // Act
        ModelAndView result = controller.firstPage(model, request);

        // Assert
        assertEquals("login", result.getViewName(), "The view name should be 'login'");
    }
}