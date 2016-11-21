package learn.java.webservice.exception.advice;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import learn.java.webservice.exception.UserNotFoundException;

@ControllerAdvice
public class LearnExceptionAdvice {

	@ExceptionHandler(Throwable.class)
	public void handleError(HttpServletRequest rq, HttpServletResponse rp, Throwable th) throws IOException {
		rp.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		rp.getWriter().write(th.getMessage());
		th.printStackTrace(rp.getWriter());
		rp.getWriter().flush();
		rp.getWriter().close();
	}
	
//	@ResponseBody
//	@ExceptionHandler(UserNotFoundException.class)
//	@ResponseStatus(HttpStatus.NOT_FOUND)
//	VndErrors userNotFoundExceptionHandler(UserNotFoundException ex) {
//		return new VndErrors("error", ex.getMessage());
//	}
	
}
