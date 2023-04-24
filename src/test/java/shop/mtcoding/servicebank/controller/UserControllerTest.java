package shop.mtcoding.servicebank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import shop.mtcoding.servicebank.core.advice.MyValidAdvice;
import shop.mtcoding.servicebank.core.session.SessionUser;
import shop.mtcoding.servicebank.dto.user.UserRequest;
import shop.mtcoding.servicebank.dto.user.UserResponse;
import shop.mtcoding.servicebank.model.account.Account;
import shop.mtcoding.servicebank.model.transaction.Transaction;
import shop.mtcoding.servicebank.model.user.User;
import shop.mtcoding.servicebank.service.UserService;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@EnableAspectJAutoProxy // AOP 작동 활성화
@Import(MyValidAdvice.class) // Aspect 클래스 로드
@WebMvcTest(UserController.class) // f -> ds(exceptionHandler, interceptor, viewResolver, messageConverter) -> c
public class UserControllerTest {
        @Autowired
        private MockMvc mvc;

        private ObjectMapper om = new ObjectMapper();

        @MockBean
        private UserService userService;

        @Test
        public void join_test() throws Exception {
                // given
                UserRequest.JoinInDTO joinInDTO = new UserRequest.JoinInDTO();
                joinInDTO.setUsername("ssar");
                joinInDTO.setPassword("1234");
                joinInDTO.setEmail("ssar@nate.com");
                joinInDTO.setFullName("쌀만고");
                String requestBody = om.writeValueAsString(joinInDTO);
                System.out.println(requestBody);

                // stub (가정)
                User user = User.builder()
                                .id(1L)
                                .username("ssar")
                                .password("1234")
                                .email("ssar@nate.com")
                                .fullName("쌀만고")
                                .createdAt(LocalDateTime.now())
                                .build();
                UserResponse.JoinOutDTO joinOutDTO = new UserResponse.JoinOutDTO(user);
                Mockito.when(userService.회원가입(any())).thenReturn(joinOutDTO);

                // when
                ResultActions resultActions = mvc.perform(
                                post("/join").content(requestBody).contentType(MediaType.APPLICATION_JSON_VALUE));
                String responseBody = resultActions.andReturn().getResponse().getContentAsString();
                System.out.println(responseBody);

                // then
                resultActions.andExpect(jsonPath("$.status").value(200));
                resultActions.andExpect(jsonPath("$.data.id").value(1));
                resultActions.andExpect(jsonPath("$.data.username").value("ssar"));
                resultActions.andExpect(jsonPath("$.data.fullName").value("쌀만고"));
        }

        @Test
        public void login_test() throws Exception {
                // given
                UserRequest.LoginInDTO loginInDTO = new UserRequest.LoginInDTO();
                loginInDTO.setUsername("ssar");
                loginInDTO.setPassword("1234");
                String requestBody = om.writeValueAsString(loginInDTO);
                System.out.println(requestBody);

                // stub (가정)
                User user = User.builder()
                                .id(1L)
                                .username("ssar")
                                .password("1234")
                                .email("ssar@nate.com")
                                .fullName("쌀만고")
                                .createdAt(LocalDateTime.now())
                                .build();
                SessionUser sessionUser = new SessionUser(user);
                Mockito.when(userService.로그인(any())).thenReturn(sessionUser);

                // when
                ResultActions resultActions = mvc.perform(
                                post("/login").content(requestBody).contentType(MediaType.APPLICATION_JSON_VALUE));
                String responseBody = resultActions.andReturn().getResponse().getContentAsString();
                System.out.println(responseBody);

                // then
                resultActions.andExpect(jsonPath("$.status").value(200));
                resultActions.andExpect(jsonPath("$.data.id").value(1));
                resultActions.andExpect(jsonPath("$.data.username").value("ssar"));
        }
}