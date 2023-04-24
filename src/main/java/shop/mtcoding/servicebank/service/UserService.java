package shop.mtcoding.servicebank.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import shop.mtcoding.servicebank.core.exception.Exception400;
import shop.mtcoding.servicebank.core.exception.Exception401;
import shop.mtcoding.servicebank.core.exception.Exception404;
import shop.mtcoding.servicebank.core.exception.Exception500;
import shop.mtcoding.servicebank.core.session.SessionUser;
import shop.mtcoding.servicebank.dto.user.UserRequest;
import shop.mtcoding.servicebank.dto.user.UserResponse;
import shop.mtcoding.servicebank.model.user.User;
import shop.mtcoding.servicebank.model.user.UserRepository;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    @Transactional // Springframework Transaction (주의)
    public UserResponse.JoinOutDTO 회원가입(UserRequest.JoinInDTO joinInDTO) {
        try {
            // 1. 동일 유저네임 존재 검사
            Optional<User> userOP = userRepository.findByUsername(joinInDTO.getUsername());
            if (userOP.isPresent()) {
                throw new Exception400("username", "동일한 유저네임이 존재합니다");
            }

            // 2. 회원가입
            User userPS = userRepository.save(joinInDTO.toEntity());

            // 3. DTO 응답
            return new UserResponse.JoinOutDTO(userPS);
        } catch (Exception e) {
            throw new Exception500("회원가입 오류 : " + e.getMessage());
        }

    }

    // 트랜젝션, 비지니스 로직, 레이지 로딩
    @Transactional(readOnly = true) // 변경감지 하지 않음
    public SessionUser 로그인(UserRequest.LoginInDTO loginInDTO) {
        // 1. 회원 존재 유무 확인
        User userPS = userRepository.findByUsername(loginInDTO.getUsername())
                .orElseThrow(() -> new Exception404("유저네임을 찾을 수 없습니다"));

        // 2. 패스워드 검증
        if (!userPS.getPassword().equals(loginInDTO.getPassword())) {
            throw new Exception401("패스워드 검증에 실패하였습니다");
        }

        // 3. DTO 응답
        return new SessionUser(userPS);
    }
}
