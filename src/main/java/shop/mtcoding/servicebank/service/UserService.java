package shop.mtcoding.servicebank.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import shop.mtcoding.servicebank.core.session.SessionUser;
import shop.mtcoding.servicebank.dto.user.UserRequest;
import shop.mtcoding.servicebank.dto.user.UserResponse;
import shop.mtcoding.servicebank.model.user.UserRepository;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public UserResponse.JoinOutDTO 회원가입(UserRequest.JoinInDTO joinInDTO) {
        return null;
    }

    public SessionUser 로그인(UserRequest.LoginInDTO loginInDTO) {
        return null;
    }
}
