package tn.firas.userservice.entities;


import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "forgot_password_tokens")
public class ForgetPasswordToken {

    @Id
    private String id;

    @Indexed(unique = true)
    private String token;
    private Date expirationTime;
    private boolean verified = false;

    private static final int EXPIRATION_TIME = 1;

    @DBRef
    private User user;

    @CreatedDate
    private LocalDateTime createdAt;


    public Date getTokenExpirationTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, EXPIRATION_TIME);
        return new Date(calendar.getTime().getTime());
    }

}
