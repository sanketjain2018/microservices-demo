package in.sj.config;



import in.sj.exception.UserServiceClientException;

import feign.Response;
import feign.codec.ErrorDecoder;

public class UserServiceErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(
            String methodKey,
            Response response) {

        if (response.status() == 404) {
            return new UserServiceClientException(
                    "User Service returned 404 for method: "
                            + methodKey
            );
        }

        if (response.status() >= 500) {
            return new UserServiceClientException(
                    "User Service is currently unavailable"
            );
        }

        return new UserServiceClientException(
                "User Service request failed with status: "
                        + response.status()
        );
    }
}