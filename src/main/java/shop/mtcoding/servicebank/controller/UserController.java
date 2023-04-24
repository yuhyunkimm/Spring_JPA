package shop.mtcoding.servicebank.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import shop.mtcoding.servicebank.core.session.SessionUser;
import shop.mtcoding.servicebank.dto.ResponseDTO;
import shop.mtcoding.servicebank.dto.user.UserRequest;
import shop.mtcoding.servicebank.dto.user.UserResponse;
import shop.mtcoding.servicebank.service.UserService;

@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;
    private final HttpSession session;

    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody @Valid UserRequest.JoinInDTO joinInDTO, Errors errors) {
        UserResponse.JoinOutDTO joinOutDTO = userService.회원가입(joinInDTO);
        ResponseDTO<?> responseDTO = new ResponseDTO<>().data(joinOutDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserRequest.LoginInDTO loginInDTO, Errors errors) {
        SessionUser sessionUser = userService.로그인(loginInDTO);
        session.setAttribute("sessionUser", sessionUser);
        ResponseDTO<?> responseDTO = new ResponseDTO<>().data(sessionUser);
        return ResponseEntity.ok(responseDTO);
    }
}
