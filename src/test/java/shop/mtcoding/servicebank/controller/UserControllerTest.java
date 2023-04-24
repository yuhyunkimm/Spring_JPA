package shop.mtcoding.servicebank.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import shop.mtcoding.servicebank.core.advice.MyValidAdvice;
import shop.mtcoding.servicebank.dto.user.UserRequest;
import shop.mtcoding.servicebank.dto.user.UserResponse;
import shop.mtcoding.servicebank.model.user.User;
import shop.mtcoding.servicebank.service.UserService;

@EnableAspectJAutoProxy // AOP 작동 활성화
@Import(MyValidAdvice.class) // Aspect 클래스 로드
@WebMvcTest(UserController.class) // f -> ds(exceptionhandler, interceptor, viewResolve)
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
        joinInDTO.setUsername("a");
        joinInDTO.setPassword("");
        joinInDTO.setEmail("ssar@nate.com");
        joinInDTO.setFullName("쌀만고");
        String requestBody = om.writeValueAsString(joinInDTO);

        // stub(가정)
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
        ResultActions resultActions = mvc
                .perform(post("/join").content(requestBody).contentType(MediaType.APPLICATION_JSON_VALUE));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println(requestBody);
        // then

    }
}
