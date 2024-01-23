package hello.springmvc.basic.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.springmvc.basic.HelloData;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Controller
public class RequestBodyJsonController {
    private ObjectMapper objectMappr = new ObjectMapper();

    @RequestMapping("/request-body-json-v1")
    public void requestBodyJsonV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        log.info("messageBody = {}", messageBody);
        HelloData helloData = objectMappr.readValue(messageBody, HelloData.class);
        log.info("username = {}, age = {}", helloData.getUsername(), helloData.getAge());

        response.getWriter().write("ok");
    }

    @ResponseBody
    @PostMapping("/request-body-json-v2")
    public String requestBodyJsonV2(@RequestBody String messageBody) throws IOException {

        log.info("messageBody = {}", messageBody);
        HelloData helloData = objectMappr.readValue(messageBody, HelloData.class);
        log.info("username = {}, age = {}", helloData.getUsername(), helloData.getAge());

        return "ok";
    }

    @ResponseBody
    @PostMapping("/request-body-json-v3")
    public String requestBodyJsonV3(@RequestBody HelloData helloData) {

        // HelloData helloData = objectMappr.readValue(messageBody, HelloData.class) <- 이 코드를 생략할 수 있다. 이유는 @RequestBody에 직접 만든 객체를 지정할 수 있기 때문.
        // HttpMessageConverter 사용 -> MappingJackson2HttpMessageConverter를 호출해서 type이 application/json인 데이터를 우리가 원하는 문자나, 객체(HelloData)로 변환해줌.
        log.info("username = {}, age = {}", helloData.getUsername(), helloData.getAge());
        // 따라서 json 형태로 http 메세지 값을 보내도 helloData 형으로 변환이 되어서 get()으로 값을 가져올 수 있음.

        return "ok";
    }

    @ResponseBody
    @PostMapping("/request-body-json-v4")
    public String requestBodyJsonV4(HttpEntity<HelloData> httpEntity) {

        HelloData data = httpEntity.getBody();
        log.info("username = {}, age = {}", data.getUsername(), data.getAge());

        return "ok";
    }

    @ResponseBody
    @PostMapping("/request-body-json-v5")
    public HelloData requestBodyJsonV4(@RequestBody HelloData data) {

        log.info("username = {}, age = {}", data.getUsername(), data.getAge());

        return data; // HelloData 형으로 받아서 데이터를 처리하고 return 해줄 땐 json 형식으로 바로 보내줄 수도 있다. -> {"username": "userA", "age": "20"}
    }
}
