package org.example.restaurantvoting.user.to;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.example.restaurantvoting.common.HasIdAndEmail;
import org.example.restaurantvoting.common.to.NamedTo;
import org.example.restaurantvoting.common.validation.NoHtml;

@Value
@EqualsAndHashCode(callSuper = true)
public class UserTo extends NamedTo implements HasIdAndEmail {

    @Email
    @NotBlank
    @Size(max = 64)
    @NoHtml
    String email;

    @NotBlank
    @Size(min = 5, max = 32)
    String password;

    public UserTo(Integer id, String name, String email, String password) {
        super(id, name);
        this.email = email;
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserTo:" + id + '[' + email + ']';
    }
}
