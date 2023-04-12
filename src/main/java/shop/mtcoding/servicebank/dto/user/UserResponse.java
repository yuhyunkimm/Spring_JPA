package shop.mtcoding.servicebank.dto.user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import shop.mtcoding.servicebank.core.util.MyDateUtils;
import shop.mtcoding.servicebank.model.user.User;

// 응답 DTO는 서비스 배우고 나서 하기 (할 수 있으면 해보기)
public class UserResponse {
    @Setter @Getter
    public static class LoginDto {
        private Long id;
        private String username;
        private String createdAt;

        public LoginDto(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.createdAt = MyDateUtils.toStringFormat(user.getCreatedAt());
        }
    }

    @Setter @Getter
    public static class JoinDto {
        private Long id;
        private String username;
        private String fullName;

        public JoinDto(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.fullName = user.getFullName();
        }
    }
}
