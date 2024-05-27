package hobbiedo.global.base.dto;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorReasonDTO {
	private String code;
	private HttpStatus httpStatus;
	private String message;
}
